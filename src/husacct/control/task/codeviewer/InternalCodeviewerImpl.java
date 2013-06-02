package husacct.control.task.codeviewer;

import husacct.validate.domain.validation.Severity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import eu.codeviewer.API;
import eu.codeviewer.Error;

public class InternalCodeviewerImpl implements CodeviewerService {

	API codeviewerAPI;
	
	public InternalCodeviewerImpl () {
		codeviewerAPI = new API();
	}
	
	@Override
	public void displayErrorsInFile(String fileName, ArrayList<Integer> errorLines) {
		codeviewerAPI.setErrorLines(errorLines);
		codeviewerAPI.parseFile(fileName);
		codeviewerAPI.show();
	}

	@Override
	public void displayErrorsInFile(String fileName, HashMap<Integer, Severity> errors) {
		ArrayList<Error> errorList = new ArrayList<Error>();
		for(Entry<Integer, Severity> entry : errors.entrySet()) {
			Severity severity = entry.getValue();
			Error error = new Error(entry.getKey(), severity.getColor());
			errorList.add(error);
		}
		codeviewerAPI.setErrors(errorList);
		codeviewerAPI.parseFile(fileName);
		codeviewerAPI.show();
		
	}

}
