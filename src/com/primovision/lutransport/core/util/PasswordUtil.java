package com.primovision.lutransport.core.util;

import java.util.Random;

import org.apache.log4j.Logger;

public class PasswordUtil {

	private static Logger log = Logger.getLogger(PasswordUtil.class);
	
	public static String generatePin() {
		int n = randomPin(5, 5);
		byte b[] = new byte[n];
		for (int i = 0; i < n; i++) {
			b[i] = (byte)randomPin('1', '9');
		}
		return new String(b, 0);
	}

	public static boolean pinValid(String pin) {
		return ((pin != null) && (pin.length() == 5));
	}

	private static int randomPin(int s, int e){
		Random rendom = new Random();
		int n = s - e + 1;
		int i = rendom.nextInt() % n;
		if (i < 0) {
			i = -i;
		}
		return s + i;
	}

	@SuppressWarnings("deprecation")
	public static String generatePassword(int start, int end) {
		int n = randomPassword(start, end);
		byte b[] = new byte[n];
		for (int i = 0; i < n; i++) {
			b[i] = (byte)randomPin('a', 'z');
		}
		return new String(b, 0);
	}

	private static int randomPassword(int s, int e){
		Random rendom = new Random();
		int n = s - e + 1;
		int i = rendom.nextInt() % n;
		if (i < 0) {
			i = -i;
		}
		return s + i;
	}


	public static boolean validatePassword(String password1, String password2) {
		try {
			String pinData1 = CryptoUtils.byteArrayToHexString(CryptoUtils.computeHash(password1));
			String pinData2 = CryptoUtils.byteArrayToHexString(CryptoUtils.computeHash(password2));
			return pinData1.equalsIgnoreCase(pinData2);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			log.warn("Error while validating Password :"+ex);
			return false;
		}
	}

	public static String encryptPassword(String password) {
		try {
			byte[] pinData = CryptoUtils.computeHash(password);
			return CryptoUtils.byteArrayToHexString(pinData);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			log.warn("Error while encrypting Password :"+ex);
			return null;
		}
	}

	public static void main(String[] args) {
		System.out.println(PasswordUtil.encryptPassword("admin"));
		System.out.println("\u0064");
	}
}
