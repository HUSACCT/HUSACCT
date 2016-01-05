package husacct.control.task.codeviewer;

import husacct.control.presentation.codeviewer.CodeViewInternalFrame;
import husacct.validate.domain.validation.Severity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import husacct.control.presentation.codeviewer.Error;

public class InternalCodeviewerImpl implements CodeviewerService {

	CodeViewInternalFrame codeViewer;
	
	public InternalCodeviewerImpl(CodeViewInternalFrame codeViewer) {
		this.codeViewer = codeViewer;
	}
	
	@Override
	public void displayErrorsInFile(String fileName, ArrayList<Integer> errorLines) {
		codeViewer.reset();
		codeViewer.setErrorLines(errorLines);
		codeViewer.parseFile(fileName);
		codeViewer.setVisible(true);
	}

	@Override
	public void displayErrorsInFile(String fileName, HashMap<Integer, Severity> errors) {
		codeViewer.reset();
		ArrayList<Error> errorList = new ArrayList<Error>();
		for(Entry<Integer, Severity> entry : errors.entrySet()) {
			Severity severity = entry.getValue();
			Error error = new Error(entry.getKey(), severity.getColor());
			errorList.add(error);
		}
		codeViewer.setErrors(errorList);
		codeViewer.parseFile(fileName);
		codeViewer.setVisible(true);
		
	}

}
