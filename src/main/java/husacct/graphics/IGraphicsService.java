package husacct.graphics;

import javax.swing.JInternalFrame;

import org.jdom2.Element;

import husacct.common.services.IObservableService;

public interface IGraphicsService extends IObservableService {
	
	void drawAnalysedArchitecture();
	
	void drawDefinedArchitecture();
	
	void drawModuleAndRuleArchitecture();
	
	JInternalFrame getAnalysedArchitectureGUI();
	
	JInternalFrame getDefinedArchitectureGUI();
	
	JInternalFrame getModuleAndRuleGUI();
	
	Element getWorkspaceData();
	
	void loadWorkspaceData(Element workspaceData);
}
