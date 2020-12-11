package com.garbagecollectors.app.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UtilityHelper {

    public static StringBuilder hashingPassword(String password)  {
        MessageDigest md = null;
        try{
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        md.update(password.getBytes());

        byte[] byteData = md.digest();

        // convert the byte to hex format
        StringBuilder result = new StringBuilder();

        for (byte byt : byteData) {
            result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        }
        return result;
    }
}
