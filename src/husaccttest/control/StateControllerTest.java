package husaccttest.control;

import husacct.control.task.MainController;
import husacct.control.task.StateController;

import org.junit.Before;
import org.junit.Test;

public class StateControllerTest {
	
	StateController stateController;
	
	@Before
	public void setup(){
		MainController mainController = new MainController(new String[]{"nogui"});
		stateController = mainController.getStateController();
	}
	
	// TODO: rewrite initial state test
	@Test
	public void testInitialState(){
	
	}
	
	// TODO: rewrite statechange test
	@Test
	public void testStateChangeListener(){
	
	}
}
