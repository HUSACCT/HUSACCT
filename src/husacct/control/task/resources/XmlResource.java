package husacct.control.task.resources;

import husacct.ServiceProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class XmlResource implements IResource{

	private boolean doCompress = false;
	private Logger logger = Logger.getLogger(XmlResource.class);

	public Document load(HashMap<String, Object> dataValues) {
		File file = (File) dataValues.get("file");

		SAXBuilder sax = new SAXBuilder();
		Document doc = new Document();
		try {

			doc = sax.build(file);
		}
		catch (Exception e) {
			if(ServiceProvider.getInstance().getControlService().isGuiEnabled()) {
				ServiceProvider.getInstance().getControlService().showErrorMessage("Unable to open file: " + e.getMessage());
			} else {
				logger.error("Unable to open file: " + e.getMessage());
			}
		}
		return doc;
	}

	public boolean save(Document doc, HashMap<String, Object> dataValues, HashMap<String, Object> config) {
		this.doCompress = (boolean)config.get("doCompress");
		File file = (File) dataValues.get("file");
		try {
			FileOutputStream outputStream = new FileOutputStream(file);
			XMLOutputter xout;
			if(doCompress) {
				logger.info(" Compress");
				xout = new XMLOutputter(Format.getCompactFormat());
			}
			else {
				xout = new XMLOutputter(Format.getPrettyFormat());
			}
			xout.output(doc, outputStream);
			outputStream.close();
			return true;
		} catch (Exception e){
			if(ServiceProvider.getInstance().getControlService().isGuiEnabled()) {
				ServiceProvider.getInstance().getControlService().showErrorMessage("Unable to open file: " + e.getMessage());
			} else {
				logger.error("Unable to save file: " + e.getMessage());
			}
		}
		return false;
	}
	
	@Override
	public boolean save(Document doc, HashMap<String, Object> dataValues) {
		HashMap<String,Object> config = new HashMap<String, Object>();
		config.put("doCompress", true);
		save(doc, dataValues, config);
		return false;
	}
}
