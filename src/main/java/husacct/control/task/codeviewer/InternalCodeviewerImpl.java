package husacct.control.task.codeviewer;

import husacct.control.presentation.codeviewer.CodeViewInternalFrame;
import husacct.validate.domain.validation.Severity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import husacct.control.presentation.codeviewer.Error;
import husacct.control.task.MainController;

public class InternalCodeviewerImpl implements CodeviewerService {

	MainController mainController;
	CodeViewInternalFrame codeViewer;
	
	public InternalCodeviewerImpl(CodeViewInternalFrame codeViewer, MainController mainController) {
		this.mainController = mainController;
		this.codeViewer = codeViewer;
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
		mainController.getViewController().showCodeViewer();
	}

}
