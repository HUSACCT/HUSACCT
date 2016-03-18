package husacct.define.domain.services.stateservice;

import husacct.define.domain.services.stateservice.state.factory.StateEngineFactory;
import husacct.define.task.JtreeStateEngine;

public class StateService {

	private static JtreeStateEngine instance = null;
	
	public static JtreeStateEngine instance() {
		StateEngineFactory sateEngineFactory = new  StateEngineFactory();
		if (instance == null ) {
			return instance = sateEngineFactory.createInstance("java");
		} else {
			return instance;
		}
	}	
}
