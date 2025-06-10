using System;

public static class MatrixChallenge
{
    public const int SIZE = 25;
    public const ulong MOD = 4294967291UL;
    private static readonly ulong[][] DEFAULT_SECRET = GenerateDefaultSecret();

    private static ulong[][] GenerateDefaultSecret()
    {
        var rnd = new Random(unchecked((int)0x5eedface));
        var secret = new ulong[SIZE][];
        for (int i = 0; i < SIZE; i++)
        {
            secret[i] = new ulong[SIZE];
            for (int j = 0; j < SIZE; j++)
            {
                ulong val = ((ulong)(uint)rnd.Next() << 32) | (uint)rnd.Next();
                secret[i][j] = val % MOD;
            }
        }
        return secret;
    }

    public static ulong[][] DefaultSecret()
    {
        var copy = new ulong[SIZE][];
        for (int i = 0; i < SIZE; i++)
        {
            copy[i] = new ulong[SIZE];
            Array.Copy(DEFAULT_SECRET[i], copy[i], SIZE);
        }
        return copy;
    }

    public static ulong[][] RandomMatrix()
    {
        var rnd = new Random();
        var m = new ulong[SIZE][];
        for (int i = 0; i < SIZE; i++)
        {
            m[i] = new ulong[SIZE];
            for (int j = 0; j < SIZE; j++)
            {
                ulong val = ((ulong)(uint)rnd.Next() << 32) | (uint)rnd.Next();
                m[i][j] = val % MOD;
            }
        }
        return m;
    }

    public static ulong[][] Multiply(ulong[][] a, ulong[][] b)
    {
        var r = new ulong[SIZE][];
        for (int i = 0; i < SIZE; i++)
        {
            r[i] = new ulong[SIZE];
            for (int k = 0; k < SIZE; k++)
            {
                ulong aik = a[i][k];
                for (int j = 0; j < SIZE; j++)
                {
                    r[i][j] = (r[i][j] + aik * b[k][j]) % MOD;
                }
            }
        }
        return r;
    }

    public static ulong[][] ComputeResponse(ulong[][] secret, ulong[][] challenge)
    {
        return Multiply(Multiply(secret, challenge), secret);
    }

    public static bool Verify(ulong[][] secret, ulong[][] challenge, ulong[][] response)
    {
        var expected = ComputeResponse(secret, challenge);
        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                if (expected[i][j] != response[i][j])
                    return false;
            }
        }
        return true;
    }

    public static byte[] Flatten(ulong[][] matrix)
    {
        var outBytes = new byte[SIZE * SIZE];
        int idx = 0;
        for (int i = 0; i < SIZE; i++)
        {
            for (int j = 0; j < SIZE; j++)
            {
                outBytes[idx++] = (byte)(matrix[i][j] & 0xFF);
            }
        }
        return outBytes;
    }

    public static byte[] DeriveKey(ulong[][] secret, ulong[][] challenge)
    {
        return Flatten(ComputeResponse(secret, challenge));
    }
}
