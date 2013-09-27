
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
 * Convert
 */
public class AsciiHexReader extends FilterReader implements DataInput {

   public AsciiHexReader(Reader in) {
      super(in);
   }

   /**
    * Read a single byte (2 ASCII characters).
    *
    * @exception  IOException  If an I/O error occurs
    */
   public int read() throws IOException {
      Reader in = this.in;
      int ch1 = in.read();
      int ch2 = in.read();
      if ((ch1 | ch2) < 0)
         throw new EOFException();
      ch1 = Character.digit((char) ch1, 16);
      ch2 = Character.digit((char) ch2, 16);
      return ((ch1 << 4) + (ch2 << 0));
   }

   public boolean readBoolean() throws IOException {
      int ch = this.read();
      return (ch != 0);
   }

   /**
     * Reads and returns one input byte.
     * The byte is treated as a signed value in
     * the range <code>-128</code> through <code>127</code>,
     * inclusive.
     * This method is suitable for
     * reading the byte written by the <code>writeByte</code>
     * method of interface <code>DataOutput</code>.
     *
     * @return     the 8-bit value read.
     * @exception  EOFException  if this stream reaches the end before reading
     *               all the bytes.
     * @exception  IOException   if an I/O error occurs.
     */
   public byte readByte() throws IOException {
      int ch = this.read();
      return (byte)(ch);
    }

    /**
     * See the general contract of the <code>readFully</code>
     * method of <code>DataInput</code>.
     * <p>
     * Bytes
     * for this operation are read from the contained
     * input stream.
     *
     * @param      b   the buffer into which the data is read.
     * @exception  EOFException  if this input stream reaches the end before
     *               reading all the bytes.
     * @exception  IOException   if an I/O error occurs.
     * @see        java.io.FilterInputStream#in
     */
    public final void readFully(byte b[]) throws IOException {
	readFully(b, 0, b.length);
    }

     /**
     * See the general contract of the <code>readFully</code>
     * method of <code>DataInput</code>.
     * <p>
     * Bytes
     * for this operation are read from the contained
     * input stream.
     *
     * @param      b     the buffer into which the data is read.
     * @param      off   the start offset of the data.
     * @param      len   the number of bytes to read.
     * @exception  EOFException  if this input stream reaches the end before
     *               reading all the bytes.
     * @exception  IOException   if an I/O error occurs.
     * @see        java.io.FilterInputStream#in
     */
    public final void readFully(byte b[], int off, int len) throws IOException {
	// Do nothing
    }

    /**
     * Reads one input byte, zero-extends
     * it to type <code>int</code>, and returns
     * the result, which is therefore in the range
     * <code>0</code>
     * through <code>255</code>.
     * This method is suitable for reading
     * the byte written by the <code>writeByte</code>
     * method of interface <code>DataOutput</code>
     * if the argument to <code>writeByte</code>
     * was intended to be a value in the range
     * <code>0</code> through <code>255</code>.
     *
     * @return     the unsigned 8-bit value read.
     * @exception  EOFException  if this stream reaches the end before reading
     *               all the bytes.
     * @exception  IOException   if an I/O error occurs.
     */
   public int readUnsignedByte() throws IOException {
      int ch = this.read();
      return ch;
   }

    /**
     * Reads two input bytes and returns
     * a <code>short</code> value. Let <code>a</code>
     * be the first byte read and <code>b</code>
     * be the second byte. The value
     * returned
     * is:
     * <p><pre><code>(short)((a &lt;&lt; 8) * | (b &amp; 0xff))
     * </code></pre>
     * This method
     * is suitable for reading the bytes written
     * by the <code>writeShort</code> method of
     * interface <code>DataOutput</code>.
     *
     * @return     the 16-bit value read.
     * @exception  EOFException  if this stream reaches the end before reading
     *               all the bytes.
     * @exception  IOException   if an I/O error occurs.
     */
   public short readShort() throws IOException {
      int ch1 = this.read();
      int ch2 = this.read();
      return (short)((ch1 << 8) + (ch2 << 0));
    }

    /**
     * Reads two input bytes and returns
     * an <code>int</code> value in the range <code>0</code>
     * through <code>65535</code>. Let <code>a</code>
     * be the first byte read and
     * <code>b</code>
     * be the second byte. The value returned is:
     * <p><pre><code>(((a &amp; 0xff) &lt;&lt; 8) | (b &amp; 0xff))
     * </code></pre>
     * This method is suitable for reading the bytes
     * written by the <code>writeShort</code> method
     * of interface <code>DataOutput</code>  if
     * the argument to <code>writeShort</code>
     * was intended to be a value in the range
     * <code>0</code> through <code>65535</code>.
     *
     * @return     the unsigned 16-bit value read.
     * @exception  EOFException  if this stream reaches the end before reading
     *               all the bytes.
     * @exception  IOException   if an I/O error occurs.
     */
   public int readUnsignedShort() throws IOException {
      int ch1 = this.read();
      int ch2 = this.read();
      return (ch1 << 8) + (ch2 << 0);
    }

    /**
     * Reads an input <code>char</code> and returns the <code>char</code> value.
     * A Unicode <code>char</code> is made up of two bytes.
     * Let <code>a</code>
     * be the first byte read and <code>b</code>
     * be the second byte. The value
     * returned is:
     * <p><pre><code>(char)((a &lt;&lt; 8) | (b &amp; 0xff))
     * </code></pre>
     * This method
     * is suitable for reading bytes written by
     * the <code>writeChar</code> method of interface
     * <code>DataOutput</code>.
     *
     * @return     the Unicode <code>char</code> read.
     * @exception  EOFException  if this stream reaches the end before reading
     *               all the bytes.
     * @exception  IOException   if an I/O error occurs.
     */
   public char readChar() throws IOException {
      int ch1 = this.read();
      return (char) ch1;
    }

    /**
     * Reads four input bytes and returns an
     * <code>int</code> value. Let <code>a</code>
     * be the first byte read, <code>b</code> be
     * the second byte, <code>c</code> be the third
     * byte,
     * and <code>d</code> be the fourth
     * byte. The value returned is:
     * <p><pre>
     * <code>
     * (((a &amp; 0xff) &lt;&lt; 24) | ((b &amp; 0xff) &lt;&lt; 16) |
     * &#32;((c &amp; 0xff) &lt;&lt; 8) | (d &amp; 0xff))
     * </code></pre>
     * This method is suitable
     * for reading bytes written by the <code>writeInt</code>
     * method of interface <code>DataOutput</code>.
     *
     * @return     the <code>int</code> value read.
     * @exception  EOFException  if this stream reaches the end before reading
     *               all the bytes.
     * @exception  IOException   if an I/O error occurs.
     */
   public int readInt() throws IOException {
      int ch1 = this.read();
      int ch2 = this.read();
      int ch3 = this.read();
      int ch4 = this.read();
      return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }

    /**
     * Reads eight input bytes and returns
     * a <code>long</code> value. Let <code>a</code>
     * be the first byte read, <code>b</code> be
     * the second byte, <code>c</code> be the third
     * byte, <code>d</code>
     * be the fourth byte,
     * <code>e</code> be the fifth byte, <code>f</code>
     * be the sixth byte, <code>g</code> be the
     * seventh byte,
     * and <code>h</code> be the
     * eighth byte. The value returned is:
     * <p><pre> <code>
     * (((long)(a &amp; 0xff) &lt;&lt; 56) |
     *  ((long)(b &amp; 0xff) &lt;&lt; 48) |
     *  ((long)(c &amp; 0xff) &lt;&lt; 40) |
     *  ((long)(d &amp; 0xff) &lt;&lt; 32) |
     *  ((long)(e &amp; 0xff) &lt;&lt; 24) |
     *  ((long)(f &amp; 0xff) &lt;&lt; 16) |
     *  ((long)(g &amp; 0xff) &lt;&lt;  8) |
     *  ((long)(h &amp; 0xff)))
     * </code></pre>
     * <p>
     * This method is suitable
     * for reading bytes written by the <code>writeLong</code>
     * method of interface <code>DataOutput</code>.
     *
     * @return     the <code>long</code> value read.
     * @exception  EOFException  if this stream reaches the end before reading
     *               all the bytes.
     * @exception  IOException   if an I/O error occurs.
     */
   public long readLong() throws IOException {
      return ((long)(this.readInt()) << 32) + (this.readInt() & 0xFFFFFFFFL);
    }

    /**
     * Reads four input bytes and returns
     * a <code>float</code> value. It does this
     * by first constructing an <code>int</code>
     * value in exactly the manner
     * of the <code>readInt</code>
     * method, then converting this <code>int</code>
     * value to a <code>float</code> in
     * exactly the manner of the method <code>Float.intBitsToFloat</code>.
     * This method is suitable for reading
     * bytes written by the <code>writeFloat</code>
     * method of interface <code>DataOutput</code>.
     *
     * @return     the <code>float</code> value read.
     * @exception  EOFException  if this stream reaches the end before reading
     *               all the bytes.
     * @exception  IOException   if an I/O error occurs.
     */
   public float readFloat() throws IOException {
      return Float.intBitsToFloat(this.readInt());
   }

    /**
     * Reads eight input bytes and returns
     * a <code>double</code> value. It does this
     * by first constructing a <code>long</code>
     * value in exactly the manner
     * of the <code>readlong</code>
     * method, then converting this <code>long</code>
     * value to a <code>double</code> in exactly
     * the manner of the method <code>Double.longBitsToDouble</code>.
     * This method is suitable for reading
     * bytes written by the <code>writeDouble</code>
     * method of interface <code>DataOutput</code>.
     *
     * @return     the <code>double</code> value read.
     * @exception  EOFException  if this stream reaches the end before reading
     *               all the bytes.
     * @exception  IOException   if an I/O error occurs.
     */
   public double readDouble() throws IOException {
      return Double.longBitsToDouble(this.readLong());
   }

    /**
     * Reads the next line of text from the input stream.
     * It reads successive bytes, converting
     * each byte separately into a character,
     * until it encounters a line terminator or
     * end of
     * file; the characters read are then
     * returned as a <code>String</code>. Note
     * that because this
     * method processes bytes,
     * it does not support input of the full Unicode
     * character set.
     * <p>
     * If end of file is encountered
     * before even one byte can be read, then <code>null</code>
     * is returned. Otherwise, each byte that is
     * read is converted to type <code>char</code>
     * by zero-extension. If the character <code>'\n'</code>
     * is encountered, it is discarded and reading
     * ceases. If the character <code>'\r'</code>
     * is encountered, it is discarded and, if
     * the following byte converts &#32;to the
     * character <code>'\n'</code>, then that is
     * discarded also; reading then ceases. If
     * end of file is encountered before either
     * of the characters <code>'\n'</code> and
     * <code>'\r'</code> is encountered, reading
     * ceases. Once reading has ceased, a <code>String</code>
     * is returned that contains all the characters
     * read and not discarded, taken in order.
     * Note that every character in this string
     * will have a value less than <code>&#92;u0100</code>,
     * that is, <code>(char)256</code>.
     *
     * @return     if this stream reaches the end before reading all the bytes.
     * @exception  IOException  if an I/O error occurs.
     */
   public String readLine() throws IOException{
      // Don't use this method use BufferedReader.readLine() instead
      return null;
   }

    /**
     * Reads in a string that has been encoded using a modified UTF-8 format.
     * The general contract of <code>readUTF</code>
     * is that it reads a representation of a Unicode
     * character string encoded in Java modified
     * UTF-8 format; this string of characters
     * is then returned as a <code>String</code>.
     * <p>
     * First, two bytes are read and used to
     * construct an unsigned 16-bit integer in
     * exactly the manner of the <code>readUnsignedShort</code>
     * method . This integer value is called the
     * <i>UTF length</i> and specifies the number
     * of additional bytes to be read. These bytes
     * are then converted to characters by considering
     * them in groups. The length of each group
     * is computed from the value of the first
     * byte of the group. The byte following a
     * group, if any, is the first byte of the
     * next group.
     * <p>
     * If the first byte of a group
     * matches the bit pattern <code>0xxxxxxx</code>
     * (where <code>x</code> means "may be <code>0</code>
     * or <code>1</code>"), then the group consists
     * of just that byte. The byte is zero-extended
     * to form a character.
     * <p>
     * If the first byte
     * of a group matches the bit pattern <code>110xxxxx</code>,
     * then the group consists of that byte <code>a</code>
     * and a second byte <code>b</code>. If there
     * is no byte <code>b</code> (because byte
     * <code>a</code> was the last of the bytes
     * to be read), or if byte <code>b</code> does
     * not match the bit pattern <code>10xxxxxx</code>,
     * then a <code>UTFDataFormatException</code>
     * is thrown. Otherwise, the group is converted
     * to the character:<p>
     * <pre><code>(char)(((a&amp; 0x1F) &lt;&lt; 6) | (b &amp; 0x3F))
     * </code></pre>
     * If the first byte of a group
     * matches the bit pattern <code>1110xxxx</code>,
     * then the group consists of that byte <code>a</code>
     * and two more bytes <code>b</code> and <code>c</code>.
     * If there is no byte <code>c</code> (because
     * byte <code>a</code> was one of the last
     * two of the bytes to be read), or either
     * byte <code>b</code> or byte <code>c</code>
     * does not match the bit pattern <code>10xxxxxx</code>,
     * then a <code>UTFDataFormatException</code>
     * is thrown. Otherwise, the group is converted
     * to the character:<p>
     * <pre><code>
     * (char)(((a &amp; 0x0F) &lt;&lt; 12) | ((b &amp; 0x3F) &lt;&lt; 6) | (c &amp; 0x3F))
     * </code></pre>
     * If the first byte of a group matches the
     * pattern <code>1111xxxx</code> or the pattern
     * <code>10xxxxxx</code>, then a <code>UTFDataFormatException</code>
     * is thrown.
     * <p>
     * If end of file is encountered
     * at any time during this entire process,
     * then an <code>EOFException</code> is thrown.
     * <p>
     * After every group has been converted to
     * a character by this process, the characters
     * are gathered, in the same order in which
     * their corresponding groups were read from
     * the input stream, to form a <code>String</code>,
     * which is returned.
     * <p>
     * The <code>writeUTF</code>
     * method of interface <code>DataOutput</code>
     * may be used to write data that is suitable
     * for reading by this method.
     * @return     a Unicode string.
     * @exception  EOFException            if this stream reaches the end
     *               before reading all the bytes.
     * @exception  IOException             if an I/O error occurs.
     * @exception  UTFDataFormatException  if the bytes do not represent a
     *               valid UTF-8 encoding of a string.
     */
    public String readUTF() throws IOException{
      // do nothing
      return null;
    }

/**
     * See the general contract of the <code>skipBytes</code>
     * method of <code>DataInput</code>.
     * <p>
     * Bytes
     * for this operation are read from the contained
     * input stream.
     *
     * @param      n   the number of bytes to be skipped.
     * @return     the actual number of bytes skipped.
     * @exception  IOException   if an I/O error occurs.
     */
   public final int skipBytes(int n) throws IOException {
      int total = 0;
      int cur = 0;

      while ((total<n) && ((cur = (int) in.skip(n-total)) > 0)) {
         total += cur;
      }

      return total;
   }

}