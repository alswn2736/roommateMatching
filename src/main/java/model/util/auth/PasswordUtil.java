package model.util.auth;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

//비밀번호 해시 생성 및 검증 클래스
public class PasswordUtil {
	private static final int ITERATIONS = 600000;
    private static final int KEY_LENGTH = 256;

    public static String hash(String password) {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        byte[] hashed = encode(password, salt);

        return Base64.getEncoder().encodeToString(salt)
                + ":" + Base64.getEncoder().encodeToString(hashed);
    }

    public static boolean matches(String password, String stored) {

        if (password == null || stored == null) {
            return false;
        }

        try {
            String[] parts = stored.split(":");

            if (parts.length != 2) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] savedHash = Base64.getDecoder().decode(parts[1]);

            byte[] inputHash = encode(password, salt);

            return MessageDigest.isEqual(savedHash, inputHash);

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] encode(String password, byte[] salt) {

        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);

        try {
            return SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec)
                    .getEncoded();

        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);

        } finally {
            spec.clearPassword();
        }
    }
}
