/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mbarix4j.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;


/**
 * Class for modifying the (native) Library Path at runtime. From
 * http://forum.java.sun.com/thread.jspa?threadID=707176.
 * 
 * The code modifies the static String array which the property java.library.path
 * is initially read into. This string array is then used to check paths when 
 * libraries are loaded.
 *
 * Please note that this code uses some internal knowledge of non-public classes
 * so although it works on the Sun JVMs I have tested it on I can't guarantee 
 * that it will work on other JVMs (in such cases it should throw an IOException).
 * 
 * @author brian
 * @deprecated Use {@link Native} instead
 */
public class LibPathHacker {

    /**
     * Adds a directory to the java.library.path at Runtime. May only work with
     * Sun's JVM.
     * 
     * @param s The directory, as a string, to add 
     * @throws IOException Thrown if was cant get the handle to set the library path.
     *      Usually thrown on non-sun JVM;s
     * @throws IllegalAccessException 
     */
    public static void addDir(String s) throws IOException {
        try {
            Field field = ClassLoader.class.getDeclaredField("usr_paths");
            field.setAccessible(true);
            String[] paths = (String[]) field.get(null);
            for (int i = 0; i < paths.length; i++) {
                if (s.equals(paths[i])) {
                    return;
                }
            }
            String[] tmp = new String[paths.length + 1];
            System.arraycopy(paths, 0, tmp, 0, paths.length);
            tmp[paths.length] = s;
            field.set(null, tmp);
            System.setProperty("java.library.path", System.getProperty("java.library.path") + File.pathSeparator + s);
        } catch (IllegalAccessException e) {
            throw new IOException("Failed to get permissions to set library path");
        } catch (NoSuchFieldException e) {
            throw new IOException("Failed to get field handle to set library path");
        }
    }

}
