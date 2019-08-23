/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mbarix4j.text;

/**
 * Converts an object to a string representation
 * @author brian
 */
public interface ObjectToStringConverter<T> {

    String convert(T object);

}
