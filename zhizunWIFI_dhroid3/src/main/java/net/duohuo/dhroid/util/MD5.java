package net.duohuo.dhroid.util;

import java.security.MessageDigest;

import android.util.Log;

public class MD5 {
	
	public static String MD5_PASSWORD = "&RAOwx342i!#fke@!dfkiRSWS453LSBb6sdksSWfop";
	
	/**
	 * MD5加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptMD5(byte[] data) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(data);
		return md5.digest();
	}

	/**
	 * MD5加密32位的加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String encryptMD5(String data) throws Exception {

		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(data.getBytes());
		byte b[] = md5.digest();

		int i;
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		return buf.toString();
	}
	
	public static String MD5_TWO(String sign) {
		String md5 = "";
		Log.d("lxf", "加密原型 = " + sign + MD5_PASSWORD);
		try {
			md5 = MD5.encryptMD5(MD5.encryptMD5(sign + MD5_PASSWORD));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return md5;
	}

}
