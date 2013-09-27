package org.mbari.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;

/**
 * Generic class for working with an SQL connection.
 */
public class QueryableImpl implements IQueryable {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Standard format for all Dates. No timezone is displayed.
     * THe date will be formatted for the UTC timezone
     */
    protected final DateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") {{
            setTimeZone(TimeZone.getTimeZone("UTC"));
    }};
    private final String jdbcPassword;
    private final String jdbcUrl;
    private final String jdbcUsername;

    /**
     * Constructs ...
     */
    public QueryableImpl(String jdbcUrl, String jdbcUsername, String jdbcPassword, String driverClass) {
        this.jdbcUrl = jdbcUrl;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;

        try {
            Class.forName(driverClass);
        }
        catch (ClassNotFoundException ex) {
            throw new DBException("Failed to initialize driver class:" + driverClass, ex);
        }
    }

    public QueryResults executeQuery(String sql) {

        // Just wrap the QueryResults returned by the sql query with a QueryResults object
        QueryFunction<QueryResults> queryFunction = new QueryFunction<QueryResults>() {

            public QueryResults apply(ResultSet resultsSet) throws SQLException {
                return new QueryResults(resultsSet);
            }
        };

        return executeQueryFunction(sql, queryFunction);

    }

    public <T> T executeQueryFunction(String query, QueryFunction<T> queryFunction) {
        log.debug("Executing SQL query: \n\t" + query);

        T object = null;
        Connection connection = null;
        try {
            connection = getConnection();
            final Statement stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(query);
            object = queryFunction.apply(rs);
            rs.close();
            stmt.close();
            connection.close();
        }
        catch (Exception e) {
            if (connection != null) {
                log.error("Failed to execute the following SQL on " + jdbcUrl + ":\n" + query, e);

                try {
                    connection.close();
                }
                catch (SQLException ex) {
                    log.error("Failed to close database connection", ex);
                }
            }

            throw new DBException("Failed to execute the following SQL on " + jdbcUrl + ": " + query, e);
        }

        return object;
    }

    public int executeUpdate(String updateSql) {
        log.debug("Executing SQL update: \n\t" + updateSql);

        int n = 0;
        Connection connection = null;
        try {
            connection = getConnection();
            final Statement stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_UPDATABLE);
            n = stmt.executeUpdate(updateSql);
            stmt.close();
            connection.close();
        }
        catch (Exception e) {
            if (connection != null) {
                log.error("Failed to execute the following SQL on " + jdbcUrl + ":\n" + updateSql, e);

                try {
                    connection.close();
                }
                catch (SQLException ex) {
                    log.error("Failed to close database connection", ex);
                }
            }

            throw new DBException("Failed to execute the following SQL on " + jdbcUrl + ": " + updateSql, e);
        }
        return n;
    }


    /**
     * @return A {@link Connection} to the EXPD database. The connection should
     *      be closed when you're done with it.
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        log.debug("Opening JDBC connection:" + jdbcUsername + " @ " + jdbcUrl);
        Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
        return connection;
    }

    /**
     * Closes the connection used in the current thread.
     * @throws SQLException
     * @deprecated Connections are closed after every transaction.
     */
    public void close() {
        // DO NOTHING.
    }
}
