/*
 * @(#)QueryResults.java   2011.12.10 at 09:06:27 PST
 *
 * Copyright 2009 MBARI
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package mbarix4j.sql;

import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * <p>Stores data returned by a query. Use as: </p>
 * <pre>
 * // Create a collection of String names of the columns you want to store
 * Collection columnNames = new ArrayList();
 * columnNames.add("ConceptName");
 * columnNames.add("LinkName");
 *
 * // Generate a DataStore with fields that match your columnNames
 * QueryResults qr = new QueryResults(columnNames);
 *
 * // Now you can add or retrieve data from linkNames;
 * List linkNames = qr.getResults("LinkName");
 *
 * </pre>
 *
 * @author Brian Schlining
 * @version $Id: QueryResults.java 265 2006-06-20 05:30:09Z hohonuuli $
 */
public class QueryResults {

    private static final Object NO_VALUE = "!_!!_!!!___NO_VALUE___!!!_!!_!";
    private Map<String, List<Object>> resultsMap = new TreeMap<>();

    /**
     * Used for storing values during the coalesce method call. This map is Map<String, Map<Integer, Set>> where values are: Map<columnName, Map<row#, values>>
     */
    private Map<String, Map<Integer, Set<Object>>> duplicateMap;

    public QueryResults(Map<String, List<Object>> data) {
        resultsMap.putAll(data);
    }

    /**
     * Generates a QueryResult from a ResultSet. This reads the metadata and all
     * data.
     *
     * @param resultSet The result set to process
     * @throws SQLException
     */
    public QueryResults(ResultSet resultSet) throws SQLException {

        /*
         * Use the metadata to generate a container to store the data into
         */
        Collection<String> columnNames = new ArrayList<String>();       //
        ResultSetMetaData metaData = resultSet.getMetaData();
        int colCount = metaData.getColumnCount();
        String[] returnTypes = new String[colCount];    // Data types returned
        for (int i = 1; i <= colCount; i++) {
            columnNames.add(metaData.getColumnLabel(i));
            returnTypes[i - 1] = metaData.getColumnClassName(i).toLowerCase();
        }

        for (String colName : columnNames) {
            resultsMap.put(colName, new ArrayList<Object>());
        }

        /*
         * To speed up access we'll pull the Lists out of QueryResults and keep
         * them in an orderlist.
         */
        List<List> dataStores = new ArrayList<List>();    // A List of Lists
        for (String colName : columnNames) {
            dataStores.add(getResults(colName));
        }


        /*
         * Fetch the data
         */
        while (resultSet.next()) {
            for (int i = 0; i < colCount; i++) {
                List data = (List) dataStores.get(i);

                /*
                 * ORACLE JDBC Driver requires special handling of TIMESTAMPS:
                 *
                    ColumnTypeName: getObject Classname / MetaData Classname

                                        ==> 9.2.0.3.0
                                        DATE: java.sql.Timestamp / java.sql.Timestamp
                                        TIMESTAMP: oracle.sql.TIMESTAMP / oracle.sql.TIMESTAMP

                                        ==> 9.2.0.5.0
                                        DATE: java.sql.Timestamp / java.sql.Timestamp
                                        TIMESTAMP: oracle.sql.DATE / oracle.sql.TIMESTAMP

                                        ==> 10.2.0.1.0
                                        DATE: java.sql.Date / java.sql.Timestamp
                                        TIMESTAMP: oracle.sql.TIMESTAMP / oracle.sql.TIMESTAMP
                 */
                if (returnTypes[i].equals("oracle.sql.timestamp")) {
                    data.add(resultSet.getTimestamp(i + 1));
                }
                else {
                    data.add(resultSet.getObject(i + 1));
                }
            }
        }
    }

    /**
     * Tells the query resultsMap to collasce based on unique values in a column
     * @param key
     */
    public void coalesce(String key) {
        duplicateMap = new HashMap<String, Map<Integer, Set<Object>>>();

        /*
         * The keyColumn contains all the values (in order) for the column
         * containing the key that we are coalescing on. Typically the key
         * is a primary key. We also convert this list to an array
         */
        List keyColumn = getResults(key);
        Object[] objects = keyColumn.toArray(new Object[keyColumn.size()]);
        for (int row0 = 0; row0 < objects.length; row0++) {
            Object object = objects[row0];
            if (!NO_VALUE.equals(object)) {
                int row1;
                while ((row1 = keyColumn.lastIndexOf(object)) > row0) {

                    /*
                     * We must do the following if a duplicate is found:
                     * 1) Combine the data into a single comma-separate value.
                     * 2) Tag the duplicate in the object array so we don't process it agains
                     * 3) Keep track of rows marked as duplicates so that we can remove
                     *    them from the final dataset
                     */

                    // combine(row0, row1);
                    storeCoalescedValues(row0, row1);
                    objects[row1] = NO_VALUE;
                }
            }
        }

        combineCoalescedValues();
        removeDuplicates();
    }

    /**
     *
     * @return The number of columns of data in the results
     */
    public int columnCount() {
        return resultsMap.keySet().size();
    }

    private void combineCoalescedValues() {

        /*
         * Iterate through each columnName stored in the duplicateMap
         */
        Collection columnNames = duplicateMap.keySet();
        for (Iterator i = columnNames.iterator(); i.hasNext(); ) {
            String columnName = (String) i.next();
            Map<Integer, Set<Object>> map = duplicateMap.get(columnName);

            /*
             * Iterate through each row containing a duplicate stored for a
             * particular columnName
             */
            Collection rows = map.keySet();
            for (Object row1 : rows) {

                /*
                 * Combine all the values in a row into a comma-separated
                 * String
                 */
                Integer row = (Integer) row1;
                Set rowValues = new TreeSet<Object>(map.get(row));
                Iterator k = rowValues.iterator();
                String value = k.next().toString();
                while (k.hasNext()) {
                    value = value + ", " + k.next();
                }

                /*
                 * Store the coalesced value back in the correct row in the
                 * data storage Lists.
                 */
                resultsMap.get(columnName).set(row, value);
            }
        }
    }

    /**
     *
     *
     * @param columnName
     *
     * @return
     */
    public boolean containsColumnName(Object columnName) {
        return resultsMap.containsKey(columnName);
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @return
     */
    public Set getColumnNames() {
        return resultsMap.keySet();
    }

    /**
     * Retrieve the data as an array. it's oriented as Object[row][col]
     * @return
     */
    public Object[][] getDataArray() {
        Set keySet = resultsMap.keySet();
        int cols = keySet.size();

        /*
         * Get the length of the data
         */
        String key0 = (String) keySet.iterator().next();
        List item = (List) resultsMap.get(key0);
        int rows = item.size();
        Object[][] data = new Object[rows][cols];
        int col = 0;
        for (Iterator i = keySet.iterator(); i.hasNext(); ) {
            Object key = i.next();
            List dataInColumn = (List) resultsMap.get(key);
            for (int row = 0; row < dataInColumn.size(); row++) {
                data[row][col] = dataInColumn.get(row);
            }

            col++;
        }

        return data;
    }

    /**
     *
     * @param columnName
     *
     * @return
     */
    public List getResults(String columnName) {
        Set columnNames = resultsMap.keySet();
        List list = null;
        for (Iterator i = columnNames.iterator(); i.hasNext(); ) {
            String column = (String) i.next();

            /*
             * We need to make the search case-insensitive.
             */
            if (columnName.equalsIgnoreCase(column)) {
                list = (List) resultsMap.get(column);
                break;
            }
        }

        return list;
    }

    /**
     * Retrieve the underlying storage
     */
    public Map<String, List<Object>> getResultsMap() {
        return resultsMap;
    }


    private void removeDuplicates() {
        Collection noValueList = new ArrayList();
        noValueList.add(NO_VALUE);
        Collection dataLists = resultsMap.values();
        for (Iterator j = dataLists.iterator(); j.hasNext(); ) {
            List data = (List) j.next();
            data.removeAll(noValueList);
        }
    }

    /**
     *
     * @return
     */
    public int rowCount() {
        int rows = 0;
        Iterator i = resultsMap.values().iterator();
        if (i.hasNext()) {
            rows = ((List) i.next()).size();
        }

        return rows;

    }

    /**
     * When coalescing data on a key there may be multiple values that are the
     * same for example if a column has the following values: 500, 403, 500, 500
     * the result we want is '500, 403', not '500, 403, 500, 500'. So we store
     * values in a set to avoid duplication. These values need to be combined
     * after all rows have been coalesced.
     * @param row0
     * @param row1
     */
    private void storeCoalescedValues(int row0, int row1) {
        Collection keys = resultsMap.keySet();
        for (Iterator i = keys.iterator(); i.hasNext(); ) {
            String columnName = (String) i.next();
            List data = (List) resultsMap.get(columnName);
            Object obj0 = data.get(row0);
            Object obj1 = data.get(row1);

            /*
             * Only combine them if the values are actually different
             */
            if ((obj0 != null) && (obj1 != null) && !obj0.equals(obj1)) {

                /*
                 * Get the map of duplicate values for a particular column.
                 * The map returned has a key = row number and a value
                 * = set of the coalesced values
                 */
                Map map = (Map) duplicateMap.get(columnName);
                if (map == null) {

                    /*
                     * map = new HashMap<Integer (row), Set (values)>
                     */
                    map = new HashMap();
                    duplicateMap.put(columnName, map);
                }

                Integer rowKey = Integer.valueOf(row0);
                Set set = (Set) map.get(rowKey);
                if (set == null) {
                    set = new HashSet();
                    map.put(rowKey, set);
                }

                set.add(obj0);
                set.add(obj1);
            }

            data.set(row1, NO_VALUE);
        }
    }

    /**
     * <p>Returns a tab-delimted columnar representation fo the data set.
     *  Perfect for loading into Excel or other spreadsheet.</p>
     *
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        /*
         * Write a header line (tab-separated)
         */
        Set keySet = resultsMap.keySet();
        for (Iterator i = keySet.iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            sb.append(key).append("\t");
        }

        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());    // Drop trailing tab
        }
        sb.append("\n");

        /*
         * Write the data
         */
        Object[][] data = getDataArray();
        int rows = data.length;
        if (rows > 0) {
            int cols = data[0].length;
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    Object value = data[row][col];
                    if (value == null) {
                        value = " ";
                    }

                    sb.append(value);

                    if (col < cols - 1) {
                        sb.append("\t");
                    }
                    else {
                        sb.append("\n");
                    }
                }
            }
        }

        return sb.toString();
    }

    /**
     * Writes the toString form of the data to a java.io.Writer. This uses
     * memory more effiently then calling writer.write(queryResults.toString())
     *
     * @param writer The writer to write the tab-delimited data into.
     *
     * @throws IOException
     */
    public void writeFormattedResults(Writer writer) throws IOException {

        /*
         * Write a header line (tab-separated)
         */
        Set keySet = resultsMap.keySet();
        for (Iterator i = keySet.iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            writer.write(key);

            if (i.hasNext()) {
                writer.write("\t");
            }
        }

        writer.write("\n");

        /*
         * Write the data
         */
        Object[][] data = getDataArray();
        int rows = data.length;
        if (rows > 0) {
            int cols = data[0].length;
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    Object value = data[row][col];
                    if (value == null) {
                        value = " ";
                    }

                    writer.write(value.toString());

                    if (col < cols - 1) {
                        writer.write("\t");
                    }
                    else {
                        writer.write("\n");
                    }
                }
            }
        }
    }



}
