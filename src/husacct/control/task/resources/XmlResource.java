package husacct.control.task.resources;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.sun.org.apache.xerces.internal.impl.io.MalformedByteSequenceException;

import javax.crypto.*;

public class XmlResource implements IResource{

	private boolean doEncrypt = false;
	private boolean doCompress = false;
	private boolean doPasswordProtection = false;
	private Logger logger = Logger.getLogger(XmlResource.class);

	public Document load(HashMap<String, Object> dataValues) {

		File file = (File) dataValues.get("file");

		SAXBuilder sax = new SAXBuilder();
		Document doc = new Document();
		try {

			doc = sax.build(file);


			//Cryptographer crypto = new Cryptographer();
			//Document doc = crypto.decrypt(file);

			//File file2 = decrypt(file);
			//doc = sax.build(file);
	//	} catch (Exception mfex) {
		//	try {
			System.out.println("DECRYPTING");
				File f = new File(file.getPath() + ".key");
				FileReader fr = new FileReader(f);
				BufferedReader bfr = new BufferedReader(fr);
				String line;
				String output = "";
				String password = "test";
				Cryptographer crypto = new Cryptographer();
				while((line = bfr.readLine()) != null) {
					output += line;
				}
				bfr.close();
				File f2 = new File(file.getPath() + ".dekey");
				FileWriter fw = new FileWriter(f2);
				BufferedWriter bfw = new BufferedWriter(fw);
				bfw.write(new String(crypto.decrypt(output.getBytes())));
				bfw.close();
				
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		//}
		return doc;
	}

	public boolean save(Document doc, HashMap<String, Object> dataValues, HashMap<String, Object> config) {

		//this.doEncrypt = (boolean)config.get("doEncrypt");
		this.doCompress = (boolean)config.get("doCompress");
		this.doPasswordProtection = (boolean)config.get("doPasswordProtection");

		File file = (File) dataValues.get("file");
		try {
			FileOutputStream outputStream = new FileOutputStream(file);
			XMLOutputter xout;
			if(doCompress) {
				System.out.println("compress");
				xout = new XMLOutputter(Format.getRawFormat());
			}
			else {
				xout = new XMLOutputter(Format.getPrettyFormat());
			}
			xout.output(doc, outputStream);

			outputStream.close();
			
			//if(doEncrypt) {
			//	Cryptographer crypto = new Cryptographer();
			//	crypto.encrypt(doc, file);
			//}
			if(doPasswordProtection) {
				FileReader fr = new FileReader(file);
				BufferedReader bfr = new BufferedReader(fr);
				String line;
				String output = "";
				String password = "test";
				Cryptographer crypto = new Cryptographer();
				while((line = bfr.readLine()) != null) {
					output += line;
				}
				bfr.close();
				File f = new File(file.getPath() + ".key");
				FileWriter fw = new FileWriter(f);
				BufferedWriter bfw = new BufferedWriter(fw);
				bfw.write(new String(crypto.encrypt(output)));
				bfw.close();
			}
			

			return true;
		} catch (Exception e){
			e.printStackTrace();
			logger.error(e.getMessage());
			new RuntimeException(e);
		}
		return false;
	}

	@Override
	public boolean save(Document doc, HashMap<String, Object> dataValues) {
		// TODO Auto-generated method stub
		return false;
	}







}
