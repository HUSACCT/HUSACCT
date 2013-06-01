package husacct.define.domain.services.stateservice.state.factory;

import husacct.define.task.CsharpStateEnginge;
import husacct.define.task.JavaStateEngine;
import husacct.define.task.JtreeStateEngine;

public class StateEngineFactory {

	public JtreeStateEngine createInstance(String progaminglanguage)
	{
	JtreeStateEngine instance=null;
		switch (progaminglanguage.toLowerCase()) {
	case "java":
		instance=new JavaStateEngine();
		
		break;
	case "csharp":
		instance=new CsharpStateEnginge();
		
		break;

	default:
		break;
	}
	
	
	return instance;
	}
	
}
