package com.company;

public class EuclidRow {
    public int gcd;
    public int a;
    public int b;
    public EuclidRow(int gcd, int a, int b){
        this.gcd = gcd;
        this.a = a;
        this.b = b;
    }
    public void set(EuclidRow row){
        this.gcd = row.gcd;
        this.a = row.a;
        this.b = row.b;
    }
}
