/*
 * @(#)QueryFunction.java   2009.12.26 at 08:29:21 PST
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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: brian
 * Date: Dec 26, 2009
 * Time: 2:22:46 PM
 * To change this template use File | Settings | File Templates.
 */
public interface QueryFunction<T> {
    T apply(ResultSet resultSet) throws SQLException;
}
