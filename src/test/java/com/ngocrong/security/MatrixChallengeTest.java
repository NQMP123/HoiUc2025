package com.ngocrong.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatrixChallengeTest {

    @Test
    void testVerification() {
        long[][] secret = MatrixChallenge.randomMatrix();
        long[][] challenge = MatrixChallenge.randomMatrix();
        long[][] response = MatrixChallenge.computeResponse(secret, challenge);
        assertTrue(MatrixChallenge.verify(secret, challenge, response));
    }
}
