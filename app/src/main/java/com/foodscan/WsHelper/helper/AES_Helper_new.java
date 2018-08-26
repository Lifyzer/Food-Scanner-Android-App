package com.foodscan.WsHelper.helper;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES_Helper_new {

    public Cipher cipher;
    public SecretKeySpec skeySpec;
    public IvParameterSpec ivParameterSpec;

    /**
     *
     */
    public AES_Helper_new(String masterKey) {

        try {
            skeySpec = getKey(masterKey);
            final byte[] iv = new byte[16];
            Arrays.fill(iv, (byte) 0x00);
            ivParameterSpec = new IvParameterSpec(iv);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Decodes a String using AES-256 and Base64
     *
     * @param text
     * @return desoded String
     */
    public String decode(String text) throws NullPointerException {

        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            //cipher = Cipher.getInstance("AES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
            byte[] encrypedPwdBytes = Base64.decode(text, Base64.DEFAULT);
            byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));
            return new String(decrypedValueBytes);
        } catch (Exception e) {
//			e.printStackTrace();
            return text;
        }
    }

    /**
     * Generates a SecretKeySpec for given password
     *
     * @return SecretKeySpec
     * @throws UnsupportedEncodingException
     */
    private SecretKeySpec getKey(String masterKey) throws UnsupportedEncodingException {

        // You can change it to 128 if you wish
        int keyLength = 256;
        byte[] keyBytes = new byte[keyLength / 8];
        // explicitly fill with zeros
        Arrays.fill(keyBytes, (byte) 0x0);

        // if password is shorter then key length, it will be zero-padded
        // to key length
        byte[] passwordBytes = masterKey.getBytes("UTF-8");
        int length = passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length;
        System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
        return new SecretKeySpec(keyBytes, "AES");
    }


    public static String encrypt(String input, String key) {
        byte[] crypted = null;
        try {
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return Base64.encodeToString(crypted,
                Base64.NO_WRAP);
    }

}
