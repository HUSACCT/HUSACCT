package husacct.control.task.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.jdom2.Document;

public class Cryptographer {
	private static String algorithm = "AES";
	private SecretKeySpec key = null;
	private static Cipher cipher = null;


	public Cryptographer() throws Exception {
		key = new SecretKeySpec(md5("test".getBytes()), "AES");
		cipher = Cipher.getInstance(algorithm);
	}
	public  byte[] encrypt(String input)
			throws InvalidKeyException, 
			BadPaddingException,
			IllegalBlockSizeException {
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] inputBytes = input.getBytes();
		return cipher.doFinal(inputBytes);
	}

	public String decrypt(byte[] encryptionBytes)
			throws InvalidKeyException, 
			BadPaddingException,
			IllegalBlockSizeException {
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] recoveredBytes = 
				cipher.doFinal(encryptionBytes);
		String recovered = 
				new String(recoveredBytes);
		return recovered;
	}
	public byte[] decrypt(byte[] data, byte[] password) {


		try {
			Cipher c = Cipher.getInstance("AES/ECB/NoPadding");
			SecretKeySpec k =
					new SecretKeySpec(md5("test".getBytes()), "AES");
			c.init(Cipher.DECRYPT_MODE, k);
			return c.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		}


		return null;
	}

	public byte[] crypt(byte[] input, byte[] key) {
		System.out.println(input.length);
		byte[] result = new byte[input.length];
		int k = 0;
		for (int i = 0; i < input.length; i++) {
			result[i] = (byte)(input[i] ^ key[k] );
			k++;
			if (k == key.length) 
				k=0;
		}


		return result;
	}

	public byte[] md5(byte[] input) {		


		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return md.digest(input);
		}
		catch (NoSuchAlgorithmException ex) {
			return null;	
		}

	}
}
