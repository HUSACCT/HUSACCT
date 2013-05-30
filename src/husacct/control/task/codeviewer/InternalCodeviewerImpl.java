package husacct.control.task.codeviewer;

import java.util.ArrayList;

import eu.codeviewer.API;

public class InternalCodeviewerImpl implements CodeviewerService {

	API codeviewerAPI;
	
	public InternalCodeviewerImpl () {
		codeviewerAPI = new API();
	}
	
	@Override
	public void displayErrorsInFile(String fileName, ArrayList<Integer> errorLines) {
		codeviewerAPI.setErrors(errorLines);
		codeviewerAPI.parseFile(fileName);
		codeviewerAPI.show();
	}

}
