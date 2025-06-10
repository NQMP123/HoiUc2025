package com.ngocrong.security;

import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Utility class for generating and verifying matrix based challenges.
 */
public class MatrixChallenge {

    public static final int SIZE = 25;
    // Large prime less than 2^32 for modular arithmetic
    public static final long MOD = 4294967291L;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final long[][] DEFAULT_SECRET;

    static {
        java.util.Random seed = new java.util.Random(0x5eedfacel);
        DEFAULT_SECRET = new long[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                DEFAULT_SECRET[i][j] = Math.floorMod(seed.nextLong(), MOD);
            }
        }
    }

    public static long[][] defaultSecret() {
        long[][] copy = new long[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(DEFAULT_SECRET[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }

    private MatrixChallenge() {}

    /**
     * Generates a random matrix of the given size.
     */
    public static long[][] randomMatrix() {
        long[][] m = new long[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                m[i][j] = Math.floorMod(RANDOM.nextLong(), MOD);
            }
        }
        return m;
    }

    /**
     * Performs matrix multiplication using modular arithmetic.
     */
    public static long[][] multiply(long[][] a, long[][] b) {
        long[][] r = new long[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int k = 0; k < SIZE; k++) {
                long aik = a[i][k];
                for (int j = 0; j < SIZE; j++) {
                    r[i][j] = (r[i][j] + aik * b[k][j]) % MOD;
                }
            }
        }
        return r;
    }

    /**
     * Computes the response matrix for the provided secret key and challenge.
     * The algorithm uses the formula secret * challenge * secret (mod MOD).
     */
    public static long[][] computeResponse(long[][] secret, long[][] challenge) {
        return multiply(multiply(secret, challenge), secret);
    }

    /**
     * Verifies the client's response.
     */
    public static boolean verify(long[][] secret, long[][] challenge, long[][] response) {
        long[][] expected = computeResponse(secret, challenge);
        for (int i = 0; i < SIZE; i++) {
            if (!Arrays.equals(expected[i], response[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Flattens the given matrix to a byte array. Each value is reduced modulo
     * 256 to obtain a single byte. The array length will be SIZE*SIZE.
     */
    public static byte[] flatten(long[][] matrix) {
        byte[] out = new byte[SIZE * SIZE];
        int idx = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                out[idx++] = (byte) (matrix[i][j] & 0xFF);
            }
        }
        return out;
    }

    /**
     * Derives a byte key from the provided secret and challenge matrix. The
     * response matrix is computed and flattened to bytes.
     */
    public static byte[] deriveKey(long[][] secret, long[][] challenge) {
        return flatten(computeResponse(secret, challenge));
    }
}
