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


package org.mbari.util;

/**
 * <p>Represents a persons name. Has an personID property, which is not currently used by
 * anything. Basically, a wrapper for a string. Not stored
 * using Castor. Dan is storing the string name only, not the
 * entire <code>Person</code> object</p>
 *
 * @author <a href="http://www.mbari.org">MBARI</a>
 * @version $Id: Person.java 332 2006-08-01 18:38:46Z hohonuuli $
 */
@Deprecated
public class Person implements java.io.Serializable, Comparable {

    /**
     *
     */
    private static final long serialVersionUID = -4947340601809171040L;

    /**
     *     The name of this <code>Person</code>.
     *     @uml.property  name="name"
     */
    private String name;

    /**
     * Default public constructor required by Castor
     */
    public Person() {
        super();
    }

    /**
     * Create a new <code>Person</code> object with the specified name.
     *
     * @param  name  The name of the <code>Person</code>.
     */
    public Person(String name) {
        setName(name);
    }

    /**
     * Compare this <code>Person</code> object with the specifed object.
     *
     * @param  object                  Description of the Parameter
     * @return                         An integer less than 0 if this <code>Person</code>'s name is
     * less than the argument; an integer equal to 0 if this <code>Person</code>'s name is equal to the argument; an integer
     * greater than 0 if this <code>Person</code>'s name is greater than the argument.
     * @exception  ClassCastException  Description of the Exception
     */
    public int compareTo(Object object) throws ClassCastException {
        if (!(object instanceof Person)) {
            throw new ClassCastException(getClass() + " can't compareTo " + object);
        }

        return this.name.compareTo(((Person) object).name);
    }

    /**
     * Compare this <code>Person</code> object with the passed handle of some other object.
     *
     * @param  object  Description of the Parameter
     * @return         <code><code>true</code></code> if this object has the * same
     * name as the specified object; <code>false</code> otherwise.
     */
    public boolean equals(Object object) {
        return ((object instanceof Person) && name.equals(((Person) object).name));
    }

    /**
     * The hash code value for this <code>Person</code>.
     *
     * @return    The integer hash code for the <code>Person</code>.
     */
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * A String representation. Note the format of the String may change, so
     * do not write code that depends on a consistent format.
     *
     * @return    A String representation of the <code>Person</code> object.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("Person( ");
        buf.append(name);
        buf.append(" )");

        return (buf.toString());
    }

    /**
     *     Gets the name of this <code>Person</code>.
     *     @return     The name of this <code>Person</code>.
     *     @uml.property  name="name"
     */
    public String getName() {
        return name;
    }

    /**
     *     <p>Sets the name. <font color="FFCCCC">WARNING: Names longer than 50 characters will be truncated.</font></p>
     *     @param name   A person's name, such as "Brian Schlining".
     *     @uml.property  name="name"
     */
    public void setName(String name) {
        if ((name != null) && (name.length() > 50)) {
            name = name.substring(0, 50);
        }

        this.name = name;
    }
}
