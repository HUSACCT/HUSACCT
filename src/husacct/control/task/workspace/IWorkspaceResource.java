package husacct.control.task.workspace;

import java.util.HashMap;

import org.jdom2.Document;

public interface IWorkspaceResource {

	public Document load(HashMap<String, Object> dataValues);
	public void save(Document doc, HashMap<String, Object> dataValues);
	
}
