package me.gerald.dallas.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class CodeGenerator {
    private final SecureRandom RNG = new SecureRandom();
    public String challenge, verifier;

    /**
     * Generates a valid PCKE challenge code and verifier
     */
    public void generate() {
        try {
            byte[] v = new byte[32];
            RNG.nextBytes(v);
            verifier = Base64.getUrlEncoder().withoutPadding().encodeToString(v);

            byte[] b = verifier.getBytes(StandardCharsets.US_ASCII);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(b, 0, b.length);

            byte[] d = digest.digest();
            challenge = Base64.getUrlEncoder().withoutPadding().encodeToString(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
