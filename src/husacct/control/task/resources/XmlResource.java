package husacct.control.task.resources;



import husacct.ServiceProvider;

import java.awt.Dimension;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

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
		}
		catch (Exception ex) {
			Cryptographer crypto = new Cryptographer();
			String password = "";
			while(password.equals("")) {
				password = promptUserForPassword();
				
				if(password.equals("")) {
					break;
				}
				try {
					doc = sax.build(crypto.decrypt(password, file));

				}
				catch (Exception e) {
					password = "";
				}
			}					
		}
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
			Cryptographer crypto = new Cryptographer();
			crypto.encrypt((String)config.get("password"), file);
		}


		return true;
	} catch (Exception e){
		e.printStackTrace();
		logger.error(e.getMessage());
		new RuntimeException(e);
	}
	return false;
}

public String promptUserForPassword() {
	JPanel panel = new JPanel();
	panel.setPreferredSize(new Dimension(150,75));
	JLabel label = new JLabel("Enter a password to open the workspace:");
	JPasswordField pass = new JPasswordField(20);
	panel.add(label);
	panel.add(pass);
	String[] options = new String[]{"OK", "Cancel"};
	int option = JOptionPane.showOptionDialog(null, panel, "The file is locked.",
			JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
			null, options, options[1]);
	if(option == 0) // pressing OK button
	{
		char[] password = pass.getPassword();
		return new String(password);
	}
	else {
		return "";
	}
}

@Override
public boolean save(Document doc, HashMap<String, Object> dataValues) {
	// TODO Auto-generated method stub
	return false;
}







}
