package com.foodscan.Security;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public enum AESUtil {
	;
	// 共通鍵
	public static String encrypt(String keyString,String keyString_iv, String stringToEncode) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, makeKey(keyString), makeIv(keyString_iv));
			return Base64.encodeBytes(cipher.doFinal(stringToEncode.getBytes()));
//			return encodeString(ENCRYPTION_KEY,src);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String decrypt(String keyString,String keyString_iv,String src) {
		String decrypted = "";
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, makeKey(keyString), makeIv(keyString_iv));
			decrypted = new String(cipher.doFinal(Base64.decode(src)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return decrypted;
	}

	static AlgorithmParameterSpec makeIv(String keyString_iv) {
		try {
			return new IvParameterSpec(keyString_iv.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	static Key makeKey(String encr_key) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] key = md.digest(encr_key.getBytes("UTF-8"));
			return new SecretKeySpec(key, "AES-128-CBC");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}
}
