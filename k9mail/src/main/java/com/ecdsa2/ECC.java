package com.ecdsa2;

import java.math.BigInteger;
import util.MathBigInteger;

public class ECC {

	public static BigInteger NEUTRAL_VALUE = new BigInteger("-9999");
	public static BigInteger P = new BigInteger("6277101735386680763835789423207666416083908700390324961279"); // as Prim Key
	public static BigInteger R = new BigInteger("6277101735386680763835789423176059013767194773182842284081"); // as Prime Number
	public static BigInteger A = new BigInteger("-3"); // as A
	public static BigInteger B = new BigInteger("64210519e59c80e70fa7e9ab72243049feb8deecc146b9b1", 16); // as B
	public static BigInteger GX = new BigInteger("188da80eb03090f67cbf20eb43a18800f4ff0afd82ff1012", 16); // as Base X
	public static BigInteger GY = new BigInteger("07192b95ffc8da78631011ed6b24cdd573f977a11e794811", 16); // as Base Y
	
	private final static BigInteger p = new BigInteger("6277101735386680763835789423207666416083908700390324961279"); // as Prim Key
	private final static BigInteger a = new BigInteger("-3"); // as a
	private final static BigInteger b = new BigInteger("64210519e59c80e70fa7e9ab72243049feb8deecc146b9b1", 16); // as b
	private final static BigInteger[] g = {new BigInteger("188da80eb03090f67cbf20eb43a18800f4ff0afd82ff1012", 16), new BigInteger("07192b95ffc8da78631011ed6b24cdd573f977a11e794811", 16)}; // as Base Point

	public static BigInteger getA() {
		return a;
	}
	
	public static BigInteger getB() {
		return b;
	}
	
	public static BigInteger getP() {
		return p;
	}
	
	public static BigInteger[] getBasePoint() {
		return g;
	}

	public static boolean isValidPoint(BigInteger[] P) {
		BigInteger LHS = P[1].pow(2).mod(p);
		BigInteger RHS = P[0].pow(3).mod(p);
		RHS = RHS.add(P[0].multiply(a)).mod(p);
		RHS = RHS.add(b).mod(p);
		return LHS.compareTo(RHS) == 0;
	}
	
	public static boolean isValid(BigInteger Px) {
		BigInteger Py = Px.pow(3).mod(p);
		Py = Py.add(Px.multiply(a)).mod(p);
		Py = Py.add(b).mod(p);
		BigInteger Py2 = new BigInteger(MathBigInteger.sqrt(Py).toString());
		return Py2.pow(2).compareTo(Py) == 0;
	}
	
	public static BigInteger getY(BigInteger Px) {
		BigInteger Py = Px.pow(3).mod(p);
		Py = Py.add(Px.multiply(a)).mod(p);
		Py = Py.add(b).mod(p);
		BigInteger Py2 = new BigInteger(MathBigInteger.sqrt(Py).toString());
		return Py2;
	}
	
	/**
	 * calculate 2P
	 * @return
	 */
	public static BigInteger[] doDouble(BigInteger[] P) {
		BigInteger[] retval = new BigInteger[2];
		
		BigInteger lambda = P[0].pow(2).multiply(new BigInteger("3")).add(a).mod(p);
		BigInteger inv = P[1].multiply(new BigInteger("2")).modInverse(p);
		lambda = lambda.multiply(inv).mod(p);
		
		retval[0] = lambda.pow(2).subtract(P[0].multiply(new BigInteger("2"))).mod(p);
		retval[1] = lambda.multiply(P[0].subtract(retval[0])).subtract(P[1]).mod(p);
		
		return retval;
	}
	
	/**
	 * calculate P + Q
	 * @param P
	 * @param Q
	 * @return
	 */
	public static BigInteger[] doPlus(BigInteger[] P, BigInteger[] Q) {		
		BigInteger[] retval = new BigInteger[2];
		
		if (P[0].compareTo(NEUTRAL_VALUE) == 0) {
			retval[0] = Q[0];
			retval[1] = Q[1];
		} else if (Q[0].compareTo(NEUTRAL_VALUE) == 0) {
			retval[0] = P[0];
			retval[1] = P[1];
		} else {
			BigInteger lambda = P[1].subtract(Q[1]).mod(p);
			BigInteger inv = P[0].subtract(Q[0]).modInverse(p);
			lambda = lambda.multiply(inv).mod(p);
			
			retval[0] = lambda.pow(2).subtract(P[0]).subtract(Q[0]).mod(p);
			retval[1] = lambda.multiply(P[0].subtract(retval[0])).subtract(P[1]).mod(p);
		}
		
		return retval;
	}
	
	public static BigInteger[] getPublicKey(BigInteger privateKey) {
		return doScalarMultiply(privateKey, g);
	}
	
	public static BigInteger[] doScalarMultiply(BigInteger k, BigInteger[] P) {
		BigInteger[] retval = new BigInteger[2];
		retval[0] = NEUTRAL_VALUE;
		retval[1] = NEUTRAL_VALUE;
		
		BigInteger[] base = new BigInteger[2];
		base[0] = P[0];
		base[1] = P[1];
		
		String binary = k.toString(2);
		for (int i = binary.length()-1; i >= 0; i--) {
			if (binary.charAt(i) == '1') {
				retval = doPlus(retval, base);
			}
			base = doDouble(base);
		}
		
		return retval;
	}
}
