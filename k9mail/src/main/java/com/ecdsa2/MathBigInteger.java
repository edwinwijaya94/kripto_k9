/**
 * 
 */
package com.ecdsa2;

import java.math.BigInteger;

/**
 * @author Vincent Edwin Randi
 *
 */
public class MathBigInteger {

	/**
	 * calculate sqrt of BigInteger
	 * @param n BigInteger
	 * @return BigInteger sqrt
	 */
	public static BigInteger sqrt(BigInteger n) {
		BigInteger a = BigInteger.ONE;
		BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
		while (b.compareTo(a) >= 0) {
			BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
			if (mid.multiply(mid).compareTo(n) > 0)
				b = mid.subtract(BigInteger.ONE);
			else
				a = mid.add(BigInteger.ONE);
		}
		return a.subtract(BigInteger.ONE);
	}
}
