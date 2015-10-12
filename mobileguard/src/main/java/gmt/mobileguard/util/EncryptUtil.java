package gmt.mobileguard.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Project: StudyDemo
 * Package: gmt.mobileguard.util
 * Created by Genment at 2015/10/10 16:58.
 * <br />
 * <p>详情请看: <a href="http://stackoverflow.com/questions/3934331/android-how-to-encrypt-a-string">Android - how to encrypt a string?</a> (stackoverflow)</p>
 */
public class EncryptUtil {

    private static char[] hextable = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static String byteArrayToHex(byte[] array) {
        StringBuilder sb = new StringBuilder(32);
        for (byte b : array) {
            int di = (b + 256) & 0xFF; // Make it unsigned
            sb.append(hextable[(di >> 4) & 0xF]) // high 4 bits
                    .append(hextable[di & 0xF]);     // low 4 bits
        }
        return sb.toString();
    }

    public static String digest(String src, String algorithm) {
        try {
            MessageDigest m = MessageDigest.getInstance(algorithm);
            m.update(src.getBytes(), 0, src.length());
            return byteArrayToHex(m.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return src;
    }

    public static String md5(String src) {
        return digest(src, "MD5");
    }

    public static String sha(String src) {
        return digest(src, "sha");
    }

    public static String sha1(String src) {
        return digest(src, "sha1");
    }

    public static String sha256(String src) {
        return digest(src, "sha256");
    }

    public static String sha512(String src) {
        return digest(src, "sha512");
    }
}
