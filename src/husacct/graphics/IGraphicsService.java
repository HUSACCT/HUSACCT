package husacct.graphics;

import husacct.common.services.IObservableService;

import javax.swing.JInternalFrame;

public interface IGraphicsService extends IObservableService {
	
	void drawAnalysedArchitecture();
	
	void drawAnalysedArchitectureWithViolations();
	
	void drawDefinedArchitecture();
	
	void drawDefinedArchitectureWithViolations();
	
	JInternalFrame getAnalysedArchitectureGUI();
	
	JInternalFrame getDefinedArchitectureGUI();
}
