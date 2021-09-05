package mbarix4j3.util;

public class HexUtil {

    public static String toHex(byte[] bytes) {
        var sb = new StringBuilder();
        for (byte b: bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }


    public static byte[] fromHex(String hex) {
        Preconditions.require(hex.length() % 2 == 0,
                () -> String.format("Invalid hexadecimal String: %s", hex));

        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i+= 2) {
            bytes[i / 2] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
        }
        return bytes;
    }
}
