package com.argyriou.loggingexample.demo;

import java.security.SecureRandom;
import java.util.Base64;

public class Obj {
    private final String x;

    public Obj() {
        x = generateRandomString();
    }

    private String generateRandomString() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[64];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().encodeToString(bytes);
    }

    @Override
    public String toString() {
        return "Obj{" +
                "x='" + x + '\'' +
                '}';
    }
}
