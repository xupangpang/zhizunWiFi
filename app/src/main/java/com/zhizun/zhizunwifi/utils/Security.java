package com.zhizun.zhizunwifi.utils;

//对提交数据的加密和解密，是在json的基础上进行
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Security {

	public static String getPassword(String key, String timestamp) {
		String password = new String();
		int nLen = key.length();
		if (nLen > timestamp.length())
			nLen = timestamp.length();
		for (int i = 0; i < nLen; i++) {
			password += String.format("%c",
					(key.charAt(i) + timestamp.charAt(i)) % 26 + 'a');
		}
		return password;
	}

	public static String encrypt(String key, String input) {

		byte[] crypted = null;

		try {

			SecretKeySpec skey = new SecretKeySpec(getRawKey(key.getBytes()),
					"AES");
			// SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES");

			cipher.init(Cipher.ENCRYPT_MODE, skey);

			crypted = cipher.doFinal(input.getBytes());

		} catch (Exception e) {

		}

		return toHex(crypted);

	}

	public static String decrypt(String key, String input) {

		byte[] output = null;

		try {

			SecretKeySpec skey = new SecretKeySpec(getRawKey(key.getBytes()),
					"AES");

			Cipher cipher = Cipher.getInstance("AES");

			cipher.init(Cipher.DECRYPT_MODE, skey);

			output = cipher.doFinal(toByte(input));

		} catch (Exception e) {

		}

		return new String(output);

	}

	public static String toHex(String txt) {
		return toHex(txt.getBytes());
	}

	public static String fromHex(String hex) {
		return new String(toByte(hex));
	}

	public static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
					16).byteValue();
		return result;
	}

	public static String toHex(byte[] buf) {
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}

	private final static String HEX = "0123456789ABCDEF";

	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}

	private static byte[] getRawKey(byte[] seed) throws Exception {
		String str = new String(seed);
		if (str.length() < 16) {
			for (int i = str.length(); i < 16; i++) {
				str += 'a';
			}
		} else {
			str = str.substring(0, 16);
		}
		byte[] raw = str.getBytes();
		return raw;
	}
	public static String md5(String string) {

	    byte[] hash;
	    try {

	        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));

	    } catch (NoSuchAlgorithmException e) {

	        throw new RuntimeException("Huh, MD5 should be supported?", e);

	    } catch (UnsupportedEncodingException e) {

	        throw new RuntimeException("Huh, UTF-8 should be supported?", e);

	    }
	    StringBuilder hex = new StringBuilder(hash.length * 2);

	    for (byte b : hash) {

	        if ((b & 0xFF) < 0x10) hex.append("0");

	        hex.append(Integer.toHexString(b & 0xFF));

	    }

	    return hex.toString();

	}

}
