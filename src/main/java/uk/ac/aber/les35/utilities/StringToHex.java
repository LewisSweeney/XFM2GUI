package uk.ac.aber.les35.utilities;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Converts a string to hexadecimal values which are then stored as a space delimited string
 * TODO: Fix bug with characterset
 */
public class StringToHex {
    static String Conversion(String str) {
        String rawString = str;
        ByteBuffer buffer = StandardCharsets.US_ASCII.encode(rawString);

        str = StandardCharsets.US_ASCII.decode(buffer).toString();

        StringBuilder sb = new StringBuilder();
        //Converting string to character array
        char[] ch = str.toCharArray();
       // ArrayList<String> hexStringList = new ArrayList<>();
        for (char c : ch) {
            String hexString = Integer.toHexString(c);
            // hexStringList.add(hexString);
            System.out.println(hexString + " : " + c);
            sb.append(hexString).append(" ");
        }
        return sb.toString();
    }
}
