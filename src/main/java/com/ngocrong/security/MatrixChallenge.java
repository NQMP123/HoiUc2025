package com.ngocrong.security;

import java.util.Random;

public class MatrixChallenge {
    public static final int SIZE = 25;
    public static final long MOD = 4294967291L; // Prime near 2^32

    private static final long[][] DEFAULT_SECRET = generateDefaultSecret();

    private static long[][] generateDefaultSecret() {
        Random rnd = new Random(0x5eedface);
        long[][] secret = new long[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                long hi = rnd.nextInt() & 0xffffffffL;
                long lo = rnd.nextInt() & 0xffffffffL;
                long val = (hi << 32) | lo;
                secret[i][j] = val % MOD;
            }
        }
        return secret;
    }

    public static long[][] defaultSecret() {
        long[][] copy = new long[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(DEFAULT_SECRET[i], 0, copy[i], 0, SIZE);
        }
        return copy;
    }

    public static long[][] randomMatrix() {
        Random rnd = new Random();
        long[][] m = new long[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                long hi = rnd.nextInt() & 0xffffffffL;
                long lo = rnd.nextInt() & 0xffffffffL;
                long val = (hi << 32) | lo;
                m[i][j] = val % MOD;
            }
        }
        return m;
    }

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

    public static long[][] computeResponse(long[][] secret, long[][] challenge) {
        return multiply(multiply(secret, challenge), secret);
    }

    public static boolean verify(long[][] secret, long[][] challenge, long[][] response) {
        long[][] expected = computeResponse(secret, challenge);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (expected[i][j] != response[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
