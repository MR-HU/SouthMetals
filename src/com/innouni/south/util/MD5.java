package com.innouni.south.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * {@code MD5} 加密类<br>
 * <li></li>对外方法：
 * {@link #getMD5}
 * 
 * @author HuGuojun
 * @data 2013-08-13
 */
public class MD5 {
	 
	/**
	 * 对内容加密;
	 * @param content 原文
	 * @return String 密文
	 * @category 静态公用方法 
	 */
    public static String getMD5(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            return getHashString(digest);
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static String getHashString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }
}
