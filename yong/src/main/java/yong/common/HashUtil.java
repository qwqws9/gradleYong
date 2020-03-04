package yong.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HashUtil {

    public static String sha512(String str, String salt) {
        String sha = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(str.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            sha = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (StringUtils.isEmpty(sha)) {
                sha = null;
            }
        }
        return sha;
    }
}
