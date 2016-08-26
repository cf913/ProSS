package picture;

import sample.*;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * GRAYSCALE Shamir's Secret Sharing based Image Secret Sharing
 * In this scheme, SSS is performed over each pixel of the secret image.
 * The input is a color or grayscale image.
 * The output is always a grayscale version of the secret image.
 *
 *
 */
public class ProcessSS {

	//////////////////////////////////////SPLIT///////////////////////////////////////
	/************************************************************************
	 * Splits a Picture into n shadow images such as t or more shadow images
	 * are need to reconstruct the original image.
	 * @param p
	 * 			Secret Picture
	 * @param n
	 * 			Number of shadow images
	 * @param t
	 * 			Threshold
     * @return
     ************************************************************************/
	public static Picture[] ProcessImageSS(Picture p, int n, int t) {
		Picture[] list = new Picture[n];

		for (int k = 0; k < n; k++) {
			list[k] = Utils.createPicture(p.getWidth(), p.getHeight());
		}

		Integer[] triple = new Integer[3];

		for (int i = 0; i < p.getHeight(); i++) {
			for (int j = 0; j < p.getWidth(); j++) {

				Color c = p.getPixel(j, i);

				int r = c.getRed();
				int g = c.getGreen();
				int b = c.getBlue();

				int avg = (r + g + b) / 3;

				String[] s = split(avg, n, t);
				String[] shares = s[0].split("\\n");

				for (int k = 0; k < list.length; k++) {

					String nobracket = shares[k].replaceAll("[\\[\\](){}]","");
					String[] sres = nobracket.split(",");

					triple[0] = Integer.valueOf(sres[0]);
					triple[1] = Integer.valueOf(sres[1]);
					triple[2] = Integer.valueOf(s[1]);

					c.setRed(triple[i%3]);
					c.setGreen(triple[(i+1)%3]);
					c.setBlue(triple[(i+2)%3]);

					list[k].setPixel(j, i, c);

				}
			}
		}
		return list;
	}

	/**********************************************************************************
	 * Perform SSSS over a pixel
	 * @param colour
	 * 			secret colour
	 * @param n
	 * 			number of shares
	 * @param t
	 * 			threshold
     * @return a list [shares, key, t] where shares is the new set of shares,
	 *          key is the prime generated to compute the modulo, and t the threshold
     **********************************************************************************/
	public static String[] split(int colour, int n, int t) {

		BigInteger secret    = new BigInteger(String.valueOf(colour));


		if (secret.compareTo(BigInteger.ZERO) == 0 ) {
			secret = BigInteger.ONE;
		}
		if (secret.compareTo(new BigInteger("251")) >= 0 ) {
			secret = new BigInteger("250");
		}

		BigInteger prime = new BigInteger("251");

		int degree = t - 1;

		BigInteger[] coefs;
		coefs = SSS.newPolynomial(degree, secret);

		String newShares;
		newShares = split_shares(coefs, n, prime);

		String[] result = new String[3];

		result[0] = newShares;
		result[1] = prime.toString();
		result[2] = String.valueOf(t);

		return result;

	}

	/********************************************************************************
	 * Computes shares by calculating the polynomial for given coefficients
	 * by calculating
	 *
	 * f(n) = coef[0] + coef[1]*n + coef[2]*n^2 +...+coef[degree]*n^degree mod prime
	 * for all n
	 * @param coef
	 *          polynomial coefficients
	 * @param n
	 *          number of shares
	 * @param prime
	 *          key for modulo
	 * @return string representation of shares: share1\nshare2\n...sharen\n
	 *
	 ********************************************************************************/
	private static String split_shares(BigInteger[] coef, int n, BigInteger prime) {

		String newShares = "";
		int randomness = 101;

		for (int j = 1; j <= n; j++) {

			BigInteger k = BigInteger.valueOf(j+randomness);
			BigInteger v = coef[0];

			for (int i = 1; i < coef.length; i ++) {
				v = (v.add(k.pow(i).mod(prime).multiply(coef[i])).mod(prime)).mod(prime);
			}

			newShares = newShares.concat("(" + k + "," + v + ")\n");

		}
		return newShares;
	}


	/////////////////////////COMBINE//////////////////////////////////////////////
	/***************************************************
	 * Combines shadow images into the secret picture
	 * @param pics
	 * 			list of shadow images
	 * @return original image
     ***************************************************/
	public static Picture combinePicsSS(Picture[] pics) {
		Picture res = Utils.createPicture(pics[0].getWidth(),pics[0].getHeight());

		Integer[] triple = new Integer[3];

		for (int i = 0; i < pics[0].getHeight(); i++) {
			for (int j = 0; j < pics[0].getWidth(); j++) {

				String shares = "";
				for (int k = 0; k < pics.length; k++) {

					Color c = pics[k].getPixel(j, i);

					int r = c.getRed();
					int g = c.getGreen();
					int b = c.getBlue();

					triple[i%3] = Integer.valueOf(r);
					triple[(i+1)%3] = Integer.valueOf(g);
					triple[(i+2)%3] = Integer.valueOf(b);

					shares = shares.concat("(" + triple[0] + "," + triple[1] + ")\n");
				}

				String result = reconstruct(shares, new BigInteger(String.valueOf(triple[2])));

				Color plop = res.getPixel(j,i);

				plop.setBlue(Integer.parseInt(result));
				plop.setRed(Integer.parseInt(result));
				plop.setGreen(Integer.parseInt(result));

				res.setPixel(j,i,plop);
			}
		}
		return res;
	}

	/*************************************************************************
	 * Performs the reconstruction phase of SSS using SSS.reconstruct_shares
	 * @param shares
	 * 			String representation of shares
	 * @param prime
	 * 			key for modulo
     * @return	secret message
     *************************************************************************/
	public static String reconstruct(String shares, BigInteger prime) {

		String result;

		ArrayList<Pair<Integer, BigInteger>> newShares = new ArrayList<>();
		String[] list = shares.split("\\n");

		for (int i = 0; i < list.length; i++) {
			String nobracket = list[i].replaceAll("[\\[\\](){}]","");
			String[] s = nobracket.split(",");

			int k = Integer.parseInt(s[0]);
			BigInteger v = new BigInteger(String.valueOf(s[1]));
			newShares.add(new Pair<>(k,v));
		}

		BigInteger secret = SSS.reconstruct_shares(newShares,prime);
		result = String.valueOf(secret);

		return result;
	}


}
