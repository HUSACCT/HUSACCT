package husacct.graphics;

import javax.swing.JInternalFrame;

public interface IGraphicsService {

	JInternalFrame getAnalysedArchitectureGUI();
	JInternalFrame getDefinedArchitectureGUI();
	
	void drawAnalysedArchitecture();
	void drawAnalysedArchitectureWithViolations();
	
	void drawDefinedArchitecture();
	void drawDefinedArchitectureWithViolations();
}
