/*
 * @(#)Native.java   2010.01.12 at 02:51:03 PST
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



package mbarix4j.nativelib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import mbarix4j.io.IOUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is code borrowed and adapted from the JNA project. It is used
 * for loading native libraries.
 *
 * @author brian
 */
public class Native {

    private static final Logger log = LoggerFactory.getLogger(Native.class);
    private final ClassLoader libClassLoader;
    private File libraryFile;
    private final String libraryName;
    private final File tempDirectory;
    private final String searchPathPrefix;

    /**
     * Constructs ...
     *
     * @param libraryName The name of the library (e.g. rxtxSerial)
     * @param searchPathPrefix Prefix to the resource path used for searching for the native library
     * @param tempDirectory The directory that Native libraries will be stored in if needed
     * @param libClassLoader The classLoader to use to find any resource (native library)
     *  that might need to be extracted
     */
    public Native(String libraryName, String searchPathPrefix, File tempDirectory, ClassLoader libClassLoader) {
        this.libraryName = libraryName;
        this.searchPathPrefix = searchPathPrefix;
        this.tempDirectory = tempDirectory;
        this.libClassLoader = libClassLoader;

        /*
         * First attempt to load from the system library paths. If that fails
         * attempt to extract the stub library from a jar classloader, and load it.
         */
        try {
            System.loadLibrary(libraryName);
            log.info(libraryName + " was found on the java.library.path and loaded");
        }
        catch (UnsatisfiedLinkError e) {
            loadNativeLibraryFromJar();
        }
    }

    /**
     * Attempts to load the native library resource from the filesystem,
     * extracting the  stub library from a resource in a classloader if not already available.
     */
    private void loadNativeLibraryFromJar() {
        String osLibraryName = System.mapLibraryName(libraryName);
        if (osLibraryName == null) {
            throw new Error("Unable to locate a library to use for " + libraryName);
        }

        String prefix = searchPathPrefix == null ? "" : searchPathPrefix;
        if (!prefix.endsWith("/")) {
            prefix += "/";
        }

        String fullResourceName = prefix + osLibraryName;

        log.debug("Looking for resource: " + fullResourceName);

        URL url = libClassLoader.getResource(fullResourceName);

        // Add an ugly hack for OpenJDK (soylatte) - JNI libs use the usual
        // .dylib extension
        if ((url == null) && Platform.isMac() && osLibraryName.endsWith(".dylib")) {
            osLibraryName = osLibraryName.substring(0, osLibraryName.lastIndexOf(".dylib")) + ".jnilib";
            url = libClassLoader.getResource(prefix + osLibraryName);
        }

        if (url == null) {
            throw new UnsatisfiedLinkError(libraryName + " (" + osLibraryName + ") not found in resource path");
        }

        if (url.getProtocol().toLowerCase().equals("file")) {
            try {
                libraryFile = new File(url.toURI());
            }
            catch (URISyntaxException e) {
                libraryFile = new File(url.getPath());
            }

            if (!libraryFile.exists()) {
                throw new Error("File URL " + url + " could not be properly decoded");
            }
        }
        else {
            InputStream is = libClassLoader.getResourceAsStream(fullResourceName);
            if (is == null) {
                throw new Error("Can't obtain " + libraryName + " InputStream");
            }

            FileOutputStream fos = null;
            try {

                /*
                 * Create the temporary directory if needed. Do we want to clean
                 * this up after the app exits?
                 */
                if (!tempDirectory.exists()) {
                    tempDirectory.mkdirs();
                }

                // Suffix is required on windows, or library fails to load
                // Let Java pick the suffix, except on windows, to avoid
                // problems with Web Start.
                libraryFile = new File(tempDirectory, osLibraryName);
                libraryFile.deleteOnExit();

                fos = new FileOutputStream(libraryFile);
                IOUtilities.copy(is, fos);
                log.info(libraryName + " was copied from " + url.toExternalForm() + " to " +
                         libraryFile.getAbsolutePath());

                if (Platform.deleteNativeLibraryAfterVMExit() &&
                        ((libClassLoader == null) || libClassLoader.equals(ClassLoader.getSystemClassLoader()))) {
                    Runtime.getRuntime().addShutdownHook(new DeleteNativeLibrary(libraryFile));
                }
            }
            catch (IOException e) {
                throw new Error("Failed to create temporary file for " + libraryName + " library: " + e);
            }
            finally {
                try {
                    is.close();
                }
                catch (IOException e) {}

                if (fos != null) {
                    try {
                        fos.close();
                    }
                    catch (IOException e) {}
                }
            }

        }

        try {
            System.load(libraryFile.getAbsolutePath());
            addLibraryPath(libraryFile.getParent());
        }
        catch (Exception ex) {
            throw new Error("Failed to load the native library " + libraryFile.getAbsolutePath(), ex);
        }
    }

    /**
     * Adds a directory to the java.library.path at Runtime. May only work with
     * Sun's JVM.
     *
     * @param s The directory, as a string, to add
     * @throws IOException Thrown if was cant get the handle to set the library path.
     *      Usually thrown on non-sun JVM;s
     * @throws IllegalAccessException
     */
    public static void addLibraryPath(String s) throws IOException {
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

    /** For internal use only. */
    public static class DeleteNativeLibrary extends Thread {

        private final File file;

        /**
         * Constructs ...
         *
         * @param file
         */
        public DeleteNativeLibrary(File file) {
            this.file = file;
        }

        /**
         * Remove any unpacked native library.  Forcing the class loader to
         * unload it first is required on Windows, since the temporary native
         * library can't be deleted until the native library is unloaded.  Any
         * deferred execution we might install at this point would prevent the
         * Native class and its class loader from being GC'd, so we instead force
         * the native library unload just a little bit prematurely.
         */
        private boolean deleteNativeLibrary() {
            if (file == null) {
                return true;
            }

            if (file.delete()) {
                return true;
            }

            // Reach into the bowels of ClassLoader to force the native
            // library to unload
            try {
                ClassLoader cl = Native.class.getClassLoader();
                Field f = ClassLoader.class.getDeclaredField("nativeLibraries");
                f.setAccessible(true);
                List libs = (List) f.get(cl);
                for (Iterator i = libs.iterator(); i.hasNext(); ) {
                    Object lib = i.next();
                    f = lib.getClass().getDeclaredField("name");
                    f.setAccessible(true);
                    String name = (String) f.get(lib);
                    String path = file.getAbsolutePath();
                    if (name.equals(path) || (name.indexOf(path) != -1)) {
                        Method m = lib.getClass().getDeclaredMethod("finalize", new Class[0]);
                        m.setAccessible(true);
                        m.invoke(lib, new Object[0]);

                        if (file.exists()) {
                            if (file.delete()) {
                                return true;
                            }

                            return false;
                        }


                        return true;
                    }
                }
            }
            catch (Exception e) {}

            return false;
        }

        /**
         *
         * @param args
         */
        public static void main(String[] args) {
            if (args.length == 1) {
                File file = new File(args[0]);
                if (file.exists()) {
                    long start = System.currentTimeMillis();
                    while (!file.delete() && file.exists()) {
                        try {
                            Thread.sleep(10);
                        }
                        catch (InterruptedException e) {}

                        if (System.currentTimeMillis() - start > 5000) {
                            System.err.println("Could not remove temp file: " + file.getAbsolutePath());
                            break;
                        }
                    }
                }
            }

            System.exit(0);
        }

        /**
         */
        @Override
        public void run() {

            // If we can't force an unload/delete, spawn a new process
            // to do so
            if (!deleteNativeLibrary()) {
                try {
                    Runtime.getRuntime().exec(new String[] { System.getProperty("java.home") + "/bin/java", "-cp",
                            System.getProperty("java.class.path"), getClass().getName(), file.getAbsolutePath(), });
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
