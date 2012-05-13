package husaccttest.control;

import husacct.control.task.IStateChangeListener;
import husacct.control.task.MainController;
import husacct.control.task.StateController;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StateControllerTest {
	
	StateController stateController;
	
	@Before
	public void setup(){
		MainController mainController = new MainController(new String[]{"nogui"});
		stateController = mainController.getStateController();
	}
	
	@Test
	public void testInitialState(){
		assertEquals(0, stateController.getState());
	}
	
	@Test
	public void testStateChangeListener(){
		stateController.addStateChangeListener(new IStateChangeListener() {
			public void changeState(int state) {
				assertEquals(1, state);
			}
		});
		stateController.setState(StateController.EMPTY);
	}
}
