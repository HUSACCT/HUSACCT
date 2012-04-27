package husacct.control.task.workspace.savers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


public class XmlSaver implements ISaveWorkspace{

	private Logger logger = Logger.getLogger(XmlSaver.class);
	
	@Override
	public void save(Document doc, HashMap<String, Object> dataValues) {
		
		File file = (File) dataValues.get("file");
		try {
			FileOutputStream outputStream = new FileOutputStream(file);
			XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());
			xout.output(doc, outputStream);
		} catch (Exception e){
			logger.error(e.getMessage());
			new RuntimeException(e);
		}
	}

}