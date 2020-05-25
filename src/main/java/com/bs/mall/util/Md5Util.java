package com.bs.mall.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Md5加密
 */
public class Md5Util {
    public final static String md5key = "Ms2";

    /**
     * Md5加密
     * @param text 明文
     * @param key 密钥
     * @return 密文
     */
    public static String md5(String text, String key){
        key = (key == null) ? md5key : key;
        return DigestUtils.md5Hex(text + key);
    }

    /**
     * MD5验证方法
     * @param text 明文
     * @param key 密钥
     * @param md5 密文
     * @return true/false
     */
    public static boolean verify(String text, String key, String md5){
        key = (key == null) ? md5key : key;
        String md5Str = md5(text, key);
        return md5Str.equalsIgnoreCase(md5);
    }

//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        String text = sc.nextLine();
//        while (!"q".equals(text)) {
//            String md5Str = Md5Util.md5(text, null);
//            System.out.println("Str:" + text +"\nmd5Str:" + md5Str);
//            text = sc.nextLine();
//        }
//    }
}
