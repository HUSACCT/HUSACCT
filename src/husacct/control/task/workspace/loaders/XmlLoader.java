package husacct.control.task.workspace.loaders;


import java.io.File;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

public class XmlLoader implements ILoadWorkspace{

	private Logger logger = Logger.getLogger(XmlLoader.class);

	public Document load(HashMap<String, Object> dataValues) {
		File file = (File) dataValues.get("file");
		SAXBuilder sax = new SAXBuilder();
		Document doc = new Document();
		try {
			doc = sax.build(file);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return doc;
	}
	
}
