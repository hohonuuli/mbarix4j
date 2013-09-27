/*
 * @(#)DBException.java   2009.12.26 at 08:09:50 PST
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



package org.mbari.sql;

/**
 * <p>DBException is thrown due to database connection problems</p>
 *
 * @author <a href="http://www.mbari.org">MBARI</a>
 * @version $Id: DBException.java 3 2005-10-27 16:20:12Z hohonuuli $
 */
@Deprecated
public class DBException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 4540564617799522054L;

    /**
     * No argument constructor.
     */
    public DBException() {
        super();
    }

    /**
     * @param message
     */
    public DBException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public DBException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public DBException(String message, Throwable cause) {
        super(message, cause);
    }
}
