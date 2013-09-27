/*
 * Copyright 2005 MBARI
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 2.1 
 * (the "License"); you may not use this file except in compliance 
 * with the License. You may obtain a copy of the License at
 *
 * http://www.gnu.org/copyleft/lesser.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.mbari.sql;

import java.sql.Connection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//~--- classes ----------------------------------------------------------------

/**
 * <h2><u>Description</u></h2>
 * <p>Provides a single point to obtain connections to databases. To add new
 * database create a properties file with the following contents:
 * <pre>
 * db.url= jdbc:inetpool:inetdae7://solstice:1433/VIMS
 * db.user=everyone
 * db.password=guest
 * db.timeout=10
 * db.description=VARS KnowledgeBase Database
 * jdbc.driver=com.inet.pool.PoolDriver
 * </pre>
 * Of course the url, user, password and driver will vary with different databases
 * and drivers. Place the directory conntaining the properties file on the classpath.
 * In this example we'll call the properties file 'vars.properties'. To open a
 * connection to the database use:
 * <code>Connection con = ConnectionFactory.getConnection("vars")</code>. If our
 * properties file was named 'expd.properties' we would use
 * <code>ConnectionFactory.getConnection("expd")</code> instead.
 * </p>
 *
 * <h2><u>UML</u></h2>
 * <pre>                  *
 *   [ConnectionFactory]-->[DBConnection]
 * </pre>
 *
 * @author <a href="mailto:brian@mbari.org">Brian Schlining</a>
 * @version $Id: ConnectionFactory.java 265 2006-06-20 05:30:09Z hohonuuli $
 */
@Deprecated
public class ConnectionFactory {

    /**
     * This maintains a list of connections so that we don't have to keep creating new
     * ones. We reuse previously created ones instead.
     */
    private static final Map dbConnections = Collections.synchronizedMap(new HashMap());


    /**
     *
     * @param ref A reference to the property file. For example if the property
     *            file is 'vars.properties' ref would be 'vars'
     * @return
     * @throws DBException Thrown if unable to obtain a connection to the database
     */
    public static synchronized Connection getConnection(String ref)
            throws DBException {
        Connection con = null;
        if (dbConnections.containsKey(ref)) {
            con = ((DBConnection) dbConnections.get(ref)).getConnection();
        } else {
            DBConnection dc = new DBConnection(ref);
            dbConnections.put(ref, dc);
            con = dc.getConnection();
        }

        return con;
    }
}
