package husaccttest.control;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import husacct.control.task.IStateChangeListener;
import husacct.control.task.MainController;
import husacct.control.task.StateController;
import husacct.control.task.States;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class StateControllerTest {
	
	StateController stateController;
	
	@Before
	public void setup(){
		MainController mainController = new MainController();
		stateController = mainController.getStateController();
	}
	
	@Test
	public void testInitialState(){
		List<States> states = stateController.getState();
		assertTrue(states.contains(States.NONE));
		assertFalse(states.contains(States.OPENED));
		assertFalse(states.contains(States.DEFINED));
		assertFalse(states.contains(States.ANALYSED));
		assertFalse(states.contains(States.MAPPED));
		assertFalse(states.contains(States.VALIDATED));
	}
	
	@Test
	public void testStateChangeListener(){
		stateController.addStateChangeListener(new IStateChangeListener() {
			public void changeState(List<States> states) {
				assertTrue(states.contains(States.NONE));
				assertFalse(states.contains(States.OPENED));
				assertTrue(states.contains(States.DEFINED));
				assertFalse(states.contains(States.ANALYSED));
				assertTrue(states.contains(States.MAPPED));
				assertFalse(states.contains(States.VALIDATED));
			}
		});
		
		List<States> states = new ArrayList<States>();
		states.add(States.NONE);
		states.add(States.DEFINED);
		states.add(States.MAPPED);
		
		stateController.setState(states);
	}
}
