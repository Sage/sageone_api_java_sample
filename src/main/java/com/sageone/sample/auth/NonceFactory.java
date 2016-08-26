package com.sageone.sample.auth;

import java.math.BigInteger;
import java.security.SecureRandom;

public class NonceFactory {

    final private SecureRandom random;

    public NonceFactory() {
        this.random = new SecureRandom();
    }

    public String getNonce() {
        return new BigInteger(160, random).toString(32);
    }
}
