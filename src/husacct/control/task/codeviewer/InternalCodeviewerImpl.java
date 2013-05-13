package husacct.control.task.codeviewer;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import nl.kuiperd.jcodeviewer.API;

public class InternalCodeviewerImpl implements CodeviewerService {

	API codeviewerAPI;
	
	public InternalCodeviewerImpl () {
		
	}
	
	@Override
	public void displayErrorsInFile(String fileName, ArrayList<Integer> errorLines) {
		JOptionPane.showMessageDialog(new JFrame(), "Function temporarly disabled", "Disabled", JOptionPane.ERROR_MESSAGE);
	}

}
