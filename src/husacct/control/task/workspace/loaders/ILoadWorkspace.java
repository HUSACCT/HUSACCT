package husacct.control.task.workspace.loaders;

import java.util.HashMap;

import org.jdom2.Document;

public interface ILoadWorkspace {

	public Document load(HashMap<String, Object> dataValues);
	
}
