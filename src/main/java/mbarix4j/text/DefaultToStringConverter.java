/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mbarix4j.text;

/**
 *
 * @author brian
 */
public class DefaultToStringConverter implements ObjectToStringConverter<Object> {

    public String convert(Object object) {
        return object.toString();
    }

}
