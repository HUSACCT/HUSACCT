package husacct.control.task.codeviewer;

import java.util.ArrayList;

public interface CodeviewerService {
	public void displayErrorsInFile(String fileName, ArrayList<Integer> errorLines);
}
