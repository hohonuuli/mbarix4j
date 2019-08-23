package mbarix4j.io;

import java.io.File;
import java.util.TreeSet;

/**
 * <p>Utilities for working with files.</p><hr><br>
 *
 * <p>MBARI provides this documentation and code &quot;as is&quot;, with no
 * warranty, express or implied, of its quality or consistency. It is provided
 * without support and without obligation on the part of the Monterey Bay
 * Aquarium Research Institute to assist in its use, correction, modification,
 * or enhancement. This information should not be published or distributed to
 * third parties without specific written permission from MBARI.</p><br>
 *
 * Copyright 2002 MBARI.<br>
 * MBARI Proprietary Information. All rights reserved.<br><hr><br>
 *
 * $Log: FileUtilities.java,v $
 * Revision 1.1  2006/01/09 21:16:58  brian
 * initial import
 *
 * Revision 1.1  2002/07/02 21:51:30  brian
 * Coallating shared files into the mbari.jar project
 *
 * Revision 1.5  2002/02/08 00:55:54  brian
 * Wokring on creating a tool to add rov altitude. utm coords, bathymetry and depth to ROV navigation files. The tools can be used iteratively so that if the ROV data is outside a grid you, it will fill in what it can but allow the user to rerun the data on another grid. THen it just fills in values that are missing.
 *
 * Revision 1.4  2002/01/30 00:23:56  brian
 * Added comments to all classes and methods. Tested the accuracy of AsciiRasterGridReader and AsciiRasterGridWriter. Also checked the the GridUtil was populating the spatial grids correctly and accurately. The data files generated matched up perfectly with the orginal text files. Also implemented an RovAltitudeFilter class to encapsulate different filtering methods.
 *<br><hr><br>
 *
 * @author  : $Author: brian $
 * @version : $Revision: 1.1 $
 */
public class FileUtilities {

    private FileUtilities() {

        // Not instantiable
    }

    /**
     *
     * @param file
     * @param newExtension
     * @return
     */
    public static File changeExtension(File file, String newExtension) {
        final String path = file.getAbsolutePath();
        int idx = path.lastIndexOf('.');
        if (idx < 0) {
            idx = path.length();
        }

        return new File(path.substring(0, idx) + newExtension);
    }

    /**
     * List the available root directories. On Windows, a number of roots may exist
     * (A:\, b:\ c:\, etc.). The mechanism provided by Sun lists all roots, even
     * if they don't exist. This method only returns an array of files for those
     * roots that actually exist.
     * @return A list of roots that exist on a computer.
     */
    public static File[] getDirRoots() {
        File[] rootsList = File.listRoots();
        TreeSet<File> goodRoots = new TreeSet<>();
        for (int k = 0; k < rootsList.length; k++) {
            if (rootsList[k].exists()) {
                goodRoots.add(rootsList[k]);
            }
        }

        return goodRoots.toArray(new File[goodRoots.size()]);
    }

    /**
     *
     * @param file The file whos extension we want
     * @return The extension, including the leading period. null if the file doesn not
     *      have an extension
     */
    public static String getExtension(File file) {
        final String path = file.getAbsolutePath();
        int idx = path.lastIndexOf('.');
        if (idx < 0) {
            return null;
        }
        else {
            return path.substring(idx);
        }
    }
}
