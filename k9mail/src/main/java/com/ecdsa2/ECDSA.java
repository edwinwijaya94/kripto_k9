/**
 * 
 */
package com.ecdsa2;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Random;

/**
 * @author Vincent Edwin Randi
 *
 */
public class ECDSA {

	/**
	 * get Digital Signature from message and privatekey
	 * @param message byte[]
	 * @param privateKey BigInteger
	 * @return Signature in string
	 */
	public static String sign(byte[] message, BigInteger privateKey) throws UnsupportedEncodingException {
            String mess = "";
            for (int i = 0;i<message.length;i++){
                mess += (char)message[i];
            }
            BigInteger h = SHA1.getHashBigInteger(mess);
            BigInteger r = BigInteger.ZERO;
            BigInteger s = BigInteger.ZERO;
            while (r.compareTo(BigInteger.ZERO) == 0 || s.compareTo(BigInteger.ZERO) == 0) {
                    Random rnd = new Random();
                    BigInteger k;
                    do {
                            k = new BigInteger(ECC.R.bitLength(), rnd);
                    } while (k.compareTo(BigInteger.ZERO) == 0 || k.compareTo(ECC.R) >= 0);

                    BigInteger[] C = ECC.getPublicKey(k);
                    r = C[0].mod(ECC.R);
                    s = k.modInverse(ECC.R).multiply(h.add(r.multiply(privateKey))).mod(ECC.R);
            }

            return "/" + r.toString(16) + "-" + s.toString(16);
	}

	/**
	 * Verify message that already Signed by ECDSA
	 * @param message byte[]
	 * @param publicKey BigInteger[]
	 * @return verified/not
	 */
	public static boolean verify(byte[] message, BigInteger[] publicKey) throws UnsupportedEncodingException {
		if (!ECC.isValidPoint(publicKey)) {
			System.out.println("Invalid public key.");
			return false;
		}

		String signature = "";
		int i = message.length - 1;
		while ('/' != (char) message[i]) {
			signature = (char) message[i] + signature;
			i--;
		}
		
                String oriM = "";
		
		for (int j = 0; j < i; j++) {
                        oriM+= (char)message[j];
		}

		String[] dsPoint = signature.split("-");
		BigInteger[] ds = new BigInteger[2];
		ds[0] = new BigInteger(dsPoint[0], 16);
		ds[1] = new BigInteger(dsPoint[1], 16);
		if (ds[0].compareTo(BigInteger.ZERO) < 1 || ds[0].compareTo(ECC.R) > -1) {
			System.out.println("Wrong Sx.");
			return false;
		}
		if (ds[1].compareTo(BigInteger.ZERO) < 1 || ds[1].compareTo(ECC.R) > -1) {
			System.out.println("Wrong Sy.");
			return false;
		}

		BigInteger h = SHA1.getHashBigInteger(oriM);
		BigInteger w = ds[1].modInverse(ECC.R);
		BigInteger u1 = h.multiply(w).mod(ECC.R);
		BigInteger u2 = ds[0].multiply(w).mod(ECC.R);
		BigInteger[] P = ECC.doPlus(ECC.getPublicKey(u1), ECC.doScalarMultiply(u2, publicKey));
		return P[0].mod(ECC.P).compareTo(ds[0]) == 0;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
            String mes = "abcssss";
            BigInteger[] bi = {BigInteger.valueOf(1),BigInteger.valueOf(1)};
            System.out.println(ECC.doScalarMultiply(BigInteger.valueOf(11),bi)[0].toString());
            System.out.println(ECC.doScalarMultiply(BigInteger.valueOf(11),bi)[1].toString());
            String s = sign(mes.getBytes(),ECC.P);
            System.out.println("Sign "+s);
            System.out.println("verify "+ verify((mes+s).getBytes(),ECC.getPublicKey(ECC.P)));
            
//		byte[] bytes = null;
//		try {
//			bytes = Files.readAllBytes(Paths.get("res/audio/Anggun - Mimpi.mp3"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		byte[] test = new byte[bytes.length+128];
//		for (int i = 0; i < test.length; i++) {
//			if (i < bytes.length) test[i] = bytes[i];
//			else test[i] = (byte) i;
//		}
//		
//		FileOutputStream fos = null;
//		try {
//			fos = new FileOutputStream("res/test.mp3");
//			fos.write(test);
//			fos.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		System.out.println("Done.");
	}
}
