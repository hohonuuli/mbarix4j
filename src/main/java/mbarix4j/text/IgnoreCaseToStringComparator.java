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


package mbarix4j.text;

import java.util.Comparator;


/**
 * A camparator that compares the values returned by the <code>toString</code>
 * methods of Objects. The comparison is case-insensitive.
 *
 * @author brian
 * @version $Id: IgnoreCaseToStringComparator.java 3 2005-10-27 16:20:12Z hohonuuli $
 */
public class IgnoreCaseToStringComparator implements Comparator {

    /**
     *
     */
    public IgnoreCaseToStringComparator() {
    }


    /**
     * @param o1
     * @param o2
     *
     * @return
     */
    public int compare(final Object o1, final Object o2) {
        String s1 = o1.toString().toLowerCase();
        String s2 = o2.toString().toLowerCase();
        return s1.compareTo(s2);
    }
}
