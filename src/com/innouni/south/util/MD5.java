package com.innouni.south.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * {@code MD5} ������<br>
 * <li></li>���ⷽ����
 * {@link #getMD5}
 * 
 * @author HuGuojun
 * @data 2013-08-13
 */
public class MD5 {
	 
	/**
	 * �����ݼ���;
	 * @param content ԭ��
	 * @return String ����
	 * @category ��̬���÷��� 
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
