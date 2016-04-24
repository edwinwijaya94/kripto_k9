package ecdsa;
import java.awt.Point;
import java.math.BigInteger;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Edwin
 */
public class ECC {
    
    /*ATTRIBUTES*/
    
    //elliptic equation coefficient -> (y2=x3+ax+b)%m
    static long a=1;
    static long b=16;
    static long m=251; // m is prime number
    
    //Galois Field
    static ArrayList<Point> GaloisField; //list of points satisfy Galois field
    
    //point multiplication eq. -> q=k.p
//    Point q; //will be public key
    static long k=123; //will be private key
    static Point p = new Point(243,91); // basis point
    
    //set equation
    public static void setEq(long a, long b, long m){
        ECC.a = a;
        ECC.b= b;
        ECC.m = m;
        ECC.GaloisField = new ArrayList<>();
    }
    
    public static void setK(long k){
        ECC.k = k;
    }
    
    public static void setP(Point p){
        ECC.p = p;
    }
    
    //get galois field points
    public static void countGaloisField(){
        for (long i = 0;i<m;i++){
            long x = i; 
            long y2 = (((x*x*x)+(a*x)+b)) % m;
            for(long j =0;j<m;j++){
                if (((j*j) % m) == y2){
                    ECC.GaloisField.add(new Point((int)x,(int)j));
                }
            }
        }  
    }
    
    public static Point doDouble(Point p){
        if (p.y==0){
            p.y = (int) Long.MAX_VALUE;
        }
        long lambda;
        Point out = new Point();
        
        lambda = (((3*p.x*p.x) % m) + a) % m;
        BigInteger inv = BigInteger.valueOf(2*p.y).modInverse(BigInteger.valueOf(m));
        long invs = inv.longValue();
        
        lambda *= invs;
        
        lambda %= m;
        
        out.x = (int) (((lambda*lambda) - (2*p.x)) % m);
        out.y = (int) (((lambda*(p.x-out.x)) - p.y) % m);
        
        if(out.x <0)
            out.x += ECC.m;
        
        if (out.y < 0){
            out.y += m;
        }
//        out.x = (((lambda*lambda) % m) - (2*p.x % m) + m) % m;
//        out.y = (((lambda*(p.x - out.x)) % m) - p.y + m) % m;
        
        return out;
    }
    
    public static Point doPlus(Point p, Point q){
        Point out = new Point();
        if (p.x == Long.MAX_VALUE ){
            out.x = q.x;
            out.y = q.y;
        }
        else if (q.x == Long.MAX_VALUE){
            out.x = p.x;
            out.y = p.y;
        }
        else if(p.x == 0 && p.y == 0){
            out = q;
        }
        else if(q.x == 0 && q.y == 0){
            out = p;
        }
        else if(p.x-q.x == 0){
            out.x = (int) Long.MAX_VALUE;
            out.y = (int) Long.MAX_VALUE;
        }
        else{
            long lambda = (p.y - q.y + m) % m;
//            System.out.println("pxqx "+(p.x-q.x));
            BigInteger inv = BigInteger.valueOf(p.x - q.x).modInverse(BigInteger.valueOf(m));
            long invs = inv.longValue();
            lambda *= invs;
            
            out.x = (int) (((lambda*lambda) - p.x - q.x) % m);
            
            out.y = (int) (((lambda*(p.x-out.x)-p.y)%m));
            
            if(out.x <0)
                out.x += ECC.m;
            
            if (out.y<0){
                out.y+=m;
            }
        }
        return out;
    }
    
    public static Point doMinus(Point p,Point q){
        Point tempQ = new Point();
        
        tempQ.x = q.x;
        tempQ.y = -q.y;
        
        return (doPlus (p,tempQ));
        
    }
    
    // scalar multiplication of a point, count q -> q= k.p
    public static Point doScalarMultiply(long k, Point p){
        
        Point out = new Point(p);
        if(k >= 2){
            out = doDouble(doScalarMultiply(k/2, p));
            if (k % 2 > 0){
                out = doPlus(out,p);
            }
        }
        if(out.x <0)
            out.x += ECC.m;
        
        if(out.y <0)
            out.y += ECC.m;
        
        return out;
    }

    public static Point generatePublicKey(long k, Point B){
        Point q = doScalarMultiply(k, B);
        return q;
    }
    
    public static void main(String[] args){
//        ECC a = new ECC(1,6,11);
        ECC.setEq(12, 14, 283);
        ECC.countGaloisField();
//        for (long i = 0;i<a.GaloisField.size();i++){
//            System.out.println("G"+i+" "+a.GaloisField.get((int) i));
//        }
        System.out.println("doPlus: "+ECC.doPlus(new Point(2,4),new Point(5,9)));
        System.out.println("doMinus: "+ECC.doMinus(new Point(2,4),new Point(5,9)));
        System.out.println("doback: "+ECC.doPlus(new Point(5,9),ECC.doMinus(new Point(2,4),new Point(5,9))));
        System.out.println("doDouble "+ECC.doDouble(new Point(2,4)));
        Point pri = new Point (0,0);
//        a.doScalarMultiply(5,new Point(0,1),pri);
        System.out.println("doMultiply "+doScalarMultiply(5, new Point(0,1)));     
    }
    
//    public Point getQ(){
//        return this.q;
//    }
    
}
