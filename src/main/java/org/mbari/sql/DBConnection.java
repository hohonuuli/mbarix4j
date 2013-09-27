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


/*
 * The Monterey Bay Aquarium Research Institute (MBARI) provides this
 * documentation and code 'as is', with no warranty, express or
 * implied, of its quality or consistency. It is provided without support and
 * without obligation on the part of MBARI to assist in its use, correction,
 * modification, or enhancement. This information should not be published or
 * distributed to third parties without specific written permission from MBARI
 */
package org.mbari.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

//~--- classes ----------------------------------------------------------------

/**
 * <p>Convience class used to establish a connection to a database. This class
 * is not called directly by a developer, rather it is used to support
 * <code>ConnectionFactory</code>.</p>
 *
 * @author <a href="http://www.mbari.org">MBARI</a>
 * @version $Id: DBConnection.java 332 2006-08-01 18:38:46Z hohonuuli $
 */
@Deprecated
class DBConnection {

    /**
	 * @uml.property  name="password"
	 */
    private String password;
    /**
	 * @uml.property  name="url"
	 */
    private String url;
    /**
	 * @uml.property  name="user"
	 */
    private String user;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     *
     * @param ref
     *
     * @throws DBException
     */
    DBConnection(String ref) throws DBException {
        ResourceBundle rb = ResourceBundle.getBundle(ref);
        url = rb.getString("db.url");
        user = rb.getString("db.user");
        password = rb.getString("db.password");
        String driver = rb.getString("jdbc.driver");
        int timeout = Integer.parseInt(rb.getString("db.timeout"));

        // The driver used and the URL are specified in vars.properties
        try {
            Class.forName(driver);
        } catch (Exception e) {
            DBException s = new DBException();
            s.initCause(e);
            throw s;
        }

        DriverManager.setLoginTimeout(timeout);
    }

    //~--- get methods --------------------------------------------------------

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @return
     *
     * @throws DBException
     */
    Connection getConnection() throws DBException {
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            DBException s = new DBException();
            s.initCause(e);
            throw s;
        }

        return con;
    }
}
