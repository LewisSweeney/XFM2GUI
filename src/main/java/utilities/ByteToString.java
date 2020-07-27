package main.java.utilities;

public class ByteToString {
    public String byteToString(byte[] bytes){
        StringBuilder newString = new StringBuilder();

        for (byte aByte : bytes) {
            int byteAsInt = aByte & 0xff;

            newString.append(Integer.toHexString(byteAsInt));
            newString.append(" ");
        }

        return newString.toString();
    }
}
