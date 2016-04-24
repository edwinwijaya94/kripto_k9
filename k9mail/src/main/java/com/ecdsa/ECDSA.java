package ecdsa;
/**
 * 
 */
// package util;
import java.awt.Point;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Random;
// import test.ECC;
// import SHA1;
/**
 * @author nashir
 *
 */
public class ECDSA {
    static Point pubKey;

	
	public static String sign(String message, long privateKey) throws UnsupportedEncodingException {
		BigInteger h = SHA1.getHashBigInteger(message);
		BigInteger r = BigInteger.ZERO;
		BigInteger s = BigInteger.ZERO;
//                int ss = 0;
//                int aa = 0;
		while (r.compareTo(BigInteger.ZERO) == 0 || s.compareTo(BigInteger.ZERO) == 0) {
			Random rnd = new Random();
//                        System.out.println(ss);
//                        ss++;
			BigInteger k;
			do {
				k = new BigInteger(BigInteger.valueOf(ECC.m).bitLength(), rnd);
                                System.out.println(k);
			} while (k.compareTo(BigInteger.ZERO) == 0 || k.compareTo(BigInteger.valueOf(ECC.m)) >= 0);
			
			Point C = ECC.generatePublicKey(k.intValue(),ECC.p);
//                        pubKey = C;
                        
			r = BigInteger.valueOf(C.x % ECC.m);
			s = k.modInverse(BigInteger.valueOf(ECC.m)).multiply(h.add(r.multiply(BigInteger.valueOf(privateKey)))).mod(BigInteger.valueOf(ECC.m));
		}
		
		return "/" + r.toString(16) + "-" + s.toString(16);
	}
	
	 public static boolean verify(String message, Point publicKey) throws UnsupportedEncodingException {
//	 	if (!ECC.isValidPoint(publicKey)) {
//	 		System.out.println("Invalid public key.");
//	 		return false;
//	 	}
		
                byte[] messBytes = message.getBytes();
	 	String signature = "";
	 	int i = messBytes.length - 1;
	 	while ('/' != (char) messBytes[i]) {
	 		signature = (char) messBytes[i] + signature;
	 		i--;
	 	}
		
	 	byte[] oriMessage = new byte[i];
                String oriM ="";
	 	for (int j = 0; j < i; j++) {
	 		oriMessage[j] = messBytes[j];
                        oriM += (char) oriMessage[j];
	 	}
                System.out.println("ori " + oriM);

	 	String[] dsPoint = signature.split("-");
                System.out.println("1 "+ dsPoint[0]);
                System.out.println("2 "+ dsPoint[1]);
                
	 	BigInteger[] ds = new BigInteger[2];
	 	ds[0] = new BigInteger(dsPoint[0], 16);
	 	ds[1] = new BigInteger(dsPoint[1], 16);
	 	if (ds[0].compareTo(BigInteger.ZERO) < 1 || ds[0].compareTo(BigInteger.valueOf(ECC.m)) > -1) {
	 		System.out.println("Wrong Sx.");
	 		return false;
	 	}
                System.out.println("ds1 "+ds[1] + "compare to "+  BigInteger.valueOf(ECC.m));
//                System.out.println(ds[1].compareTo(BigInteger.ZERO));
	 	if (ds[1].compareTo(BigInteger.ZERO) < 1 || ds[1].compareTo(BigInteger.valueOf(ECC.m)) > -1) {
	 		System.out.println("Wrong Sy.");
	 		return false;
	 	}

	 	BigInteger h = SHA1.getHashBigInteger(oriM);
                System.out.println("h = "+h.toString());
	 	BigInteger w = ds[1].modInverse(BigInteger.valueOf(ECC.m));
                System.out.println("w = " + w);
	 	BigInteger u1 = h.multiply(w).mod(BigInteger.valueOf(ECC.m));
                System.out.println("u1 = " + u1.toString());
	 	BigInteger u2 = ds[0].multiply(w).mod(BigInteger.valueOf(ECC.m));
                System.out.println("u2 = " + u2.toString());
//                BigInteger[] P = EllipticCurve.addPoint(EllipticCurve.getPublicKey(u1), EllipticCurve.multiplyPoint(u2, publicKey));
	 	Point P = ECC.doPlus(ECC.generatePublicKey(u1.longValue(),ECC.p), ECC.doScalarMultiply(u2.longValue(), publicKey));
                System.out.println("P = " + P);
	 	System.out.println("A "+BigInteger.valueOf(P.x % ECC.m).toString()+" B "+ds[0]);
                return ((BigInteger.valueOf(P.x % ECC.m)).compareTo(ds[0])) == 0;
	 }
	
	public static void main(String[] args) throws UnsupportedEncodingException {
//            System.out.println("inverse" + getInverse(BigInteger.valueOf(7),BigInteger.valueOf(20)));
		String mess = "333";
                System.out.println(ECC.doScalarMultiply(11, new Point(1,1)));
//		byte[] mByte= mess.getBytes();
                String signs = sign(mess,ECC.k);
		System.out.println("sign" +signs);
                System.out.println(verify("333"+signs,ECC.generatePublicKey(ECC.k, ECC.p)));

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