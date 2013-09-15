package husacct.graphics;

import husacct.common.services.IObservableService;

import javax.swing.JInternalFrame;

import org.jdom2.Element;

public interface IGraphicsService extends IObservableService {
	
	void drawAnalysedArchitecture();
	
	void drawAnalysedArchitectureWithViolations();
	
	void drawDefinedArchitecture();
	
	void drawDefinedArchitectureWithViolations();
	
	JInternalFrame getAnalysedArchitectureGUI();
	
	JInternalFrame getDefinedArchitectureGUI();
	
	Element getWorkspaceData();
	
	void loadWorkspaceData(Element workspaceData);
}
