package husacct.control.task;

import java.util.List;

public interface IStateChangeListener {
	public void changeState(List<States> states);
}
