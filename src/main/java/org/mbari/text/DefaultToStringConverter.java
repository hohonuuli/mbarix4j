/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mbari.text;

/**
 *
 * @author brian
 */
public class DefaultToStringConverter implements ObjectToStringConverter {

    public String convert(Object object) {
        return object.toString();
    }

}
