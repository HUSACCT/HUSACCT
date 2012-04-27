package husacct.control.task.workspace.savers;

import java.util.HashMap;

import org.jdom2.Document;

public interface ISaveWorkspace {

	public void save(Document doc, HashMap<String, Object> dataValues);
	
}
