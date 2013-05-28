package husacct.control.task.codeviewer;

import java.util.ArrayList;

import nl.kuiperd.jcodeviewer.API;

public class InternalCodeviewerImpl implements CodeviewerService {

	API codeviewerAPI;
	
	public InternalCodeviewerImpl () {
		
	}
	
	@Override
	public void displayErrorsInFile(String fileName, ArrayList<Integer> errorLines) {
		codeviewerAPI = new API();
		codeviewerAPI.setErrors(errorLines);
		codeviewerAPI.openFile(fileName);
	}

}
