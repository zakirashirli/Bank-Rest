package com.bank.rest.security;

import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import java.util.Base64;

@Component
public class CryptoService {
    private final byte[] key;
    private final byte[] iv; // 12 bytes for GCM

    public CryptoService(@Value("${app.crypto.aes.key}") String key,
                         @Value("${app.crypto.aes.iv}") String iv) {
        this.key = key.getBytes(); this.iv = iv.getBytes();
    }

    public String encrypt(String plain) {
        try {
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, iv));
            return Base64.getEncoder().encodeToString(c.doFinal(plain.getBytes()));
        } catch (Exception e) { throw new IllegalStateException("Encrypt failed", e); }
    }

    public String decrypt(String enc) {
        try {
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, iv));
            return new String(c.doFinal(Base64.getDecoder().decode(enc)));
        } catch (Exception e) { throw new IllegalStateException("Decrypt failed", e); }
    }
}

