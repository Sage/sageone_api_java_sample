package org.sage_one_sample.sageone;

import java.security.SecureRandom;

/**
 * Nonce utility class.
 */
public class Nonce {

    public static String generateNonce() {
        final byte[] bytes = new byte[32];
        final SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < bytes.length; i++) {
            final int offset;
            final int range;
            if (secureRandom.nextBoolean()) {
                offset = 48;
                range = 10;
            } else {
                offset = 97;
                range = 6;
            }
            bytes[i] = (byte) (offset + secureRandom.nextInt(range));
        }
        return new String(bytes);
    }
}
