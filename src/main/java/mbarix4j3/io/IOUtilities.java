/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mbarix4j3.io;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author brian
 */
public class IOUtilities {

    /**
     * Copy ALL available data from one stream into another
     * @param in
     * @param out
     * @throws IOException
     */
    public static void copy(InputStream in, OutputStream out) throws IOException {
        ReadableByteChannel source = Channels.newChannel(in);
        WritableByteChannel target = Channels.newChannel(out);

        ByteBuffer buffer = ByteBuffer.allocate(16 * 1024);
        while (source.read(buffer) != -1) {
            buffer.flip(); // Prepare the buffer to be drained
            while (buffer.hasRemaining()) {
                target.write(buffer);
            }
            buffer.clear(); // Empty buffer to get ready for filling
        }

        source.close();
        target.close();

    }

    public static void copy(URL source, Path target) throws IOException {
        var in = source.openStream();
        var out = Files.newOutputStream(target);
        copy(in, out);
        out.close();
        in.close();
    }

    public static void copy(byte[] bytes, Path target) throws IOException {
        var in = new BufferedInputStream(new ByteArrayInputStream(bytes));
        var out = Files.newOutputStream(target);
        copy(in, out);
        out.close();
        in.close();
    }

    public static void copy(InputStream in, Path target) throws IOException {
        var out = Files.newOutputStream(target);
        copy(in, out);
        out.close();
    }

    /**
     * Generate zipped data using a given filename and file content (Payload)
     * @param filename The name of the file to write inside the zip file.
     * @param payload The file data as a byte array
     * @return Zip file data containing the filename using the payload for that filename.
     * @throws IOException
     */
    public static byte[] toZipData(String filename, byte[] payload) throws IOException {
        byte[] data = null;
        try (var bio = new ByteArrayOutputStream(); var zip = new ZipOutputStream(bio)) {
            var zipEntry = new ZipEntry(filename);
            zip.putNextEntry(zipEntry);
            zip.write(payload, 0, payload.length);
            zip.close();
            data = bio.toByteArray();
        }
        return data;
    }

}
