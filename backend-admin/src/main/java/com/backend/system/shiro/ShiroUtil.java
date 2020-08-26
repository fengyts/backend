package com.backend.system.shiro;

import com.backend.util.EncodeUtil;
import com.backend.util.MD5Util;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.Validate;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

public class ShiroUtil {

    private static SecureRandom random = new SecureRandom();

    /** 用户密码加密 */
    public static String encryptPasswdShiro(String passwd) {
        String salt = generateSalt();
        ByteSource byteSalt = ByteSource.Util.bytes(salt);
        SimpleHash result = new SimpleHash(ShiroConstant.MD5, passwd, byteSalt, ShiroConstant.HASH_ITERATIONS);
        String pwd = salt + result.toString();
        return pwd;
    }

//    public static String encryptPasswdCustom(String passwd){
//        String salt = generateSalt();
//        String pwd = MD5Util.encrypt16md5(passwd);
//        return salt + pwd;
//    }

    /**
     * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
     */
    public static String encryptPasswd(String plainPassword) {
        byte[] salt = generateSalt(ShiroConstant.SALT_SIZE8);
        byte[] hashPassword = sha1(plainPassword.getBytes(), salt, ShiroConstant.HASH_ITERATIONS);
        String saltEncode = EncodeUtil.encodeHex(salt);
        String hashPwd = EncodeUtil.encodeHex(hashPassword);
        return  saltEncode + hashPwd;
    }

    private static String generateSalt() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String salt = MD5Util.encrypt16md5(uuid);
        return salt;
    }

    /**
     * 生成随机的Byte[]作为salt.
     *
     * @param numBytes byte数组的大小
     */
    public static byte[] generateSalt(int numBytes) {
        Validate.isTrue(numBytes > 0, "numBytes argument must be a positive integer (1 or larger)", numBytes);
        byte[] bytes = new byte[numBytes];
        random.nextBytes(bytes);
        return bytes;
    }

    public static byte[] sha1(byte[] input, byte[] salt, int iterations) {
        try {
            return digest(input, ShiroConstant.SHA1, salt, iterations);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * 对字符串进行散列, 支持md5与sha1算法.
     */
    private static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations) throws NoSuchAlgorithmException {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            if (salt != null) {
                digest.update(salt);
            }
            byte[] result = digest.digest(input);
            for (int i = 1; i < iterations; i++) {
                digest.reset();
                result = digest.digest(result);
            }
            return result;
        } catch (GeneralSecurityException e) {
            throw e;
        }
    }

//    public static void main(String[] args) {
//        String pwd = encryptPasswdShiro(ShiroConstant.DEFAULT_PASSWD);
//        System.out.println(pwd);
//        System.out.println(pwd.length());
//    }



}
