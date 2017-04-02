package husacct.define.domain.services.stateservice;

import husacct.define.task.JtreeStateEngine;

public class StateService {

	private static JtreeStateEngine instance = null;
	
	public static JtreeStateEngine instance() {
		if (instance == null ) {
			return instance = new JtreeStateEngine();
		} else {
			return instance;
		}
	}	
}
