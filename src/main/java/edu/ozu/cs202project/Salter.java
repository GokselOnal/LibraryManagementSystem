package edu.ozu.cs202project;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Salter {
    public static void main(String[] args)
    {
        System.out.println(">: " + salt("a", "CS202Project"));
    }

    public static String salt(String A, String B)
    {
        byte[] hash = new byte[0];

        try {
            KeySpec spec = new PBEKeySpec(
                    A.toCharArray(),
                    B.getBytes(StandardCharsets.UTF_8),
                    65536,
                    128
            );
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            hash = factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

       // System.out.println();
        return Base64.getEncoder().encodeToString(hash);
    }
}
