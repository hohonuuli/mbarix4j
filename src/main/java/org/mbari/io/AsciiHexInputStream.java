
/**
 * Title:        MBARI data processing<p>
 * Description:  <p>
 * Copyright:    Copyright (c) Brian Schlining<p>
 * Company:      MBARI<p>
 * @author Brian Schlining
 * @version 1.0
 */
package org.mbari.io;

import java.io.*;

/**
 * Used for reading ASCII hexadecimal values from a stream. Useful for binary
 * data streams that OASIS controllers convert to Hexadecimal for transmission.
 */
public class AsciiHexInputStream extends FilterInputStream {

  public AsciiHexInputStream(InputStream in) {
     super(in);
  }

  /**
   * Read a single byte (2 ASCII characters).
   *
   * @exception  IOException  If an I/O error occurs
   */
  public int read() throws IOException {
     InputStream in = this.in;
     int ch1 = in.read();
     //if (ch1 < 0)
     //   return -1;
     int ch2 = in.read();
     //if (ch2 < 0)
     //   return -1;
     if ((ch1 | ch2) < 0)
        throw new EOFException();
     ch1 = Character.digit( (char) ch1, 16);
     ch2 = Character.digit( (char) ch2, 16);
     return (char)((ch1 << 4) + (ch2 << 0));
  }


}