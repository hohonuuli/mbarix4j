//$Header: /home/cvs/iag/brian/mbari/src/main/java/org/mbari/util/StringUtilities.java,v 1.1 2006/01/09 21:16:59 brian Exp $
package mbarix4j.util;

/**
 * <p>Static methods for performing seful String manipulations</p><hr>
 *
 * @author  : $Author: brian $
 * @version : $Revision: 1.1 $
 *
 * <hr><p><font size="-1" color="#336699"><a href="http://www.mbari.org">
 * The Monterey Bay Aquarium Research Institute (MBARI)</a> provides this
 * documentation and code &quot;as is&quot;, with no warranty, express or
 * implied, of its quality or consistency. It is provided without support and
 * without obligation on the part of MBARI to assist in its use, correction,
 * modification, or enhancement. This information should not be published or
 * distributed to third parties without specific written permission from
 * MBARI.</font></p><br>
 *
 * <font size="-1" color="#336699">Copyright 2002 MBARI.<br>
 * MBARI Proprietary Information. All rights reserved.</font><br><hr><br>
 *
 */

/*
 * $Log: StringUtilities.java,v $
 * Revision 1.1  2006/01/09 21:16:59  brian
 * initial import
 *
 * Revision 1.2  2002/07/02 21:49:41  brian
 * updating mbari.jar distribution
 *
 * Revision 1.1  2002/06/06 22:38:22  brian
 * These classes were originally from the mbari package but have been moved here for now.
 *
 */

import java.util.*;

public class StringUtilities {

    private StringUtilities() {
    }

    /**
     * Return the token specified by position in a string using the specified delimiter.
     *  For example, <br>
     *   String str = "Mary;had;a;little;lamb.";
     *   String little = StringUtilities.getToken(str, 4, ";"); // little = "little"
     *
     * @param str The string to parse
     * @param tokenNumber The (ones-based) index of the token to be returned
     * @param delim The token delimiter
     * @return String From position specifed
     */
    public static final String getToken(String str, int tokenNumber, String delim) {
        StringTokenizer st = new StringTokenizer(str, delim);
        String buf = null;
        for (int i = 0; i < tokenNumber; i++) {
            buf = st.nextToken();
        }
        return buf;
    }

    /**
     * Return the token specified by position in a string using white space
     * delimiters (\t\r\n\f and space).
     *  For example, <br>
     *   String str = "Mary had a little lamb.";
     *   String little = StringUtilities.getToken(str, 4); // little = "little"
     *
     * @param str The string to parse
     * @param tokenNumber The (ones-based) index of the token to be returned
     * @return String from position specified
     */
    public static final String getToken(String str, int tokenNumber) {
        return StringUtilities.getToken(str, tokenNumber, " \t\n\r\f");
    }

    /**
     *  Useful method for ordering a 1-D array based on an array of indices
     *  @see mbarix4j.util.MathUtil.uniqueSort()
     *  @param values A 1-D array of data to be sorted based on an array of indices
     *  @param order A 1-D array of indices specifying the ordering of the data.
     */
    public static final String[] order(String[] values, int[] order) {
        String[] out = new String[order.length];
        for (int i = 1; i < order.length; i++) {
            out[i] = values[order[i]];
        }
        return out;
    }
    
    /**
     * Repeat a string. Analagous to pythons ability to do "AB" * 2 = "ABAB". For example:
     * StringUtilities.repeat("AB", 2) = "ABAB"
     * @param s The string to repeat
     * @param i The number of times to repeat the string
     */
    public static final String repeat(String s, int i) {
      return new String(new char[i]).replace("\0", s);
    }

    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsOrderedChars(final String chars, final String text) {
        if (chars == null || text == null ) {
            return false;
        }
        int idx = text.indexOf(chars.charAt(0));
        if (idx >= 0 && chars.length() == 1) {
            return true;
        }
        else if (idx >= 0) {
            return containsOrderedChars(chars.substring(1), text.substring(idx + 1));
        }
        else {
            return false;
        }

    }

}