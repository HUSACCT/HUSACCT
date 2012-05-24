package husacct.control.task.resources;

import java.util.HashMap;

import org.jdom2.Document;

public interface IResource {

	public Document load(HashMap<String, Object> dataValues);
	public boolean save(Document doc, HashMap<String, Object> dataValues);
	
}
