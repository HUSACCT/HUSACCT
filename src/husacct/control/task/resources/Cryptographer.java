package husacct.control.task.resources;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Cryptographer {

	
	public Cryptographer() {
		
	}
	
	public void encrypt(String key, File f) {
		try {
		//	String key = "squirrel123"; // needs to be at least 8 characters for DES
			//File f = new File(".");
			
			String EncryptedFileName = f.getName() + ".encrypted";
			String DecryptedFileName = f.getName() + ".decrypted";
			File outputFile = new File(f.getCanonicalPath() + ".encrypted");
			FileInputStream fis = new FileInputStream(f);
			FileOutputStream fos = new FileOutputStream(outputFile);
			encrypt(key, fis, fos);
			f.delete();
			outputFile.renameTo(f);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	public File decrypt(String key, File f) {
		
		try {
			File outputFile = new File(f.getCanonicalPath() + ".decrypted");
			FileInputStream fis2 = new FileInputStream(f);
			FileOutputStream fos2 = new FileOutputStream(outputFile);
			decrypt(key, fis2, fos2);
			return outputFile;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void encrypt(String key, InputStream is, OutputStream os) throws Throwable {
		encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, is, os);
	}

	public static void decrypt(String key, InputStream is, OutputStream os) throws Throwable {
		encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
	}

	public static void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os) throws Throwable {

		DESKeySpec dks = new DESKeySpec(key.getBytes());
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		SecretKey desKey = skf.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE

		if (mode == Cipher.ENCRYPT_MODE) {
			cipher.init(Cipher.ENCRYPT_MODE, desKey);
			CipherInputStream cis = new CipherInputStream(is, cipher);
			doCopy(cis, os);
		} else if (mode == Cipher.DECRYPT_MODE) {
			cipher.init(Cipher.DECRYPT_MODE, desKey);
			CipherOutputStream cos = new CipherOutputStream(os, cipher);
			doCopy(is, cos);
		}
	}

	public static void doCopy(InputStream is, OutputStream os) throws IOException {
		byte[] bytes = new byte[64];
		int numBytes;
		while ((numBytes = is.read(bytes)) != -1) {
			os.write(bytes, 0, numBytes);
		}
		os.flush();
		os.close();
		is.close();
	}

}