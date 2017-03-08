package husacct.control.task;

import husacct.common.enums.States;

import java.util.List;

public interface IStateChangeListener {
	public void changeState(List<States> states);
}
