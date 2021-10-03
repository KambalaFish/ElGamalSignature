package com.company;

import java.util.Random;

public class ElGamalSignature {
    private final int p, g;
    private final Random random;
    public ElGamalSignature(int p, int g){
        this.p = p;
        this.g = g;
        random = new Random();
    }

    private int generateRandomNumber(int min, int max){
        return random.nextInt(max - min + 1) + min;
    }

    private int calculatePowerByMod(int base, int power) {
        int result = 1;
        while (power > 0) {
            if ((power & 1) == 1)
                result = (result * base) % p;
            base = (base * base) % p;
            power = power >> 1;
        }
        return result;
    }

    private EuclidRow generalizedEuclidAlgorithm(int a, int b){
        if (b>a){
            a = a + b;
            b = a - b;
            a = a - b;
        }
        EuclidRow u = new EuclidRow(a, 1, 0);
        EuclidRow v = new EuclidRow(b, 0, 1);
        EuclidRow t = new EuclidRow(0,0,0);

        while (v.gcd!=0){
            int q = u.gcd / v.gcd;
            t.gcd = u.gcd % v.gcd;
            t.a = u.a - q * v.a;
            t.b = u.b - q * v.b;
            u.set(v);
            v.set(t);
        }
        return u;
    }

    private int getK(){
        int k = generateRandomNumber(2, p-2);
        while (generalizedEuclidAlgorithm(p-1, k).gcd!=1){
            k = generateRandomNumber(2, p-2);
        }
        return k;
    }

    private int getReversedK(int k){
        int reversedK = generalizedEuclidAlgorithm(p-1, k).b;
        if (reversedK<0)
            return reversedK + (p-1);
        return reversedK;
    }

    public void sign(){
        int x = generateRandomNumber(2, p-2);
        System.out.println("Alice picked random number x = "+x+" which she keeps in secret.");
        int y = calculatePowerByMod(g, x);
        System.out.println("Alice calculates y = g^x mod p = "+y+". Alice publishes y as her open key.");
        String message = "Hi, Bob!";
        System.out.println("Alice wants to send bob message = "+message);
        int h = message.hashCode() % p;
        h = h==0?1:h;
        System.out.println("Alice calculates h = hash(message) mod p = "+h);
        int k = getK();
        System.out.println("Alice randomly picks number k = "+k+" which is coprime number with number p - 1");
        int r = calculatePowerByMod(g, k);
        System.out.println("Alice calculates r = g^k mod p = "+r);
        int u = (h-x*r) % (p-1);
        u = u<0?u+(p-1):u;
        System.out.println("Alice calculates u = (h - x*r) mod (p - 1) = "+u);
        int reversedK = getReversedK(k);
        int s = reversedK*u % (p-1);
        System.out.println("Alice calculates s = u*k^(-1) mod (p - 1) = "+s);
        System.out.println("Alice sends Bob triplet (message, r, s) = ("+message+", "+r+", "+s+")");
        System.out.println("Bob received triplet, he wants to check authenticity of signature. If y^r * r^s mod p = g^h mod p then signature is authentic");
        System.out.println("Bob calculates h = hash(message) mod p = "+h);
        int yRrS = calculatePowerByMod(y, r) * calculatePowerByMod(r, s) % p;
        System.out.println("Bob calculates y^r * r^s mod p = "+yRrS);
        int gH = calculatePowerByMod(g, h);
        System.out.println("Bob calculates g^h mod p = "+gH);
        if (yRrS==gH){
            System.out.println("Bob can be confident in Alice's signature authenticity");
        } else
            System.out.println("Signature is not authentic");
    }
}
