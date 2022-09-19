package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class Security {

    public static String encrypt(String string){

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(string.getBytes());

            StringBuilder builder = new StringBuilder();
            for(byte b : md.digest()){
                builder.append(String.format("%02x", b));
            }
            return builder.toString();

        } catch (NoSuchAlgorithmException e) {
            return string + "_FAKE_HASHED";
        }
    }

    public static String genRandomId(){
        return UUID.randomUUID().toString();
    }
}
