package husacct.control.task.codeviewer;

import husacct.validate.domain.validation.Severity;

import java.util.HashMap;

public interface CodeviewerService {
	public void displayErrorsInFile(String fileName, HashMap<Integer, Severity> errors);
}
