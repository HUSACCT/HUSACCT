package husacct.graphics;

import husacct.common.savechain.ISaveable;
import husacct.graphics.task.DemoController;

import javax.swing.JInternalFrame;

import org.jdom2.Element;

public class GraphicsServiceDemo implements IGraphicsService, ISaveable
{
	private DemoController controller;
	
	public GraphicsServiceDemo()
	{
		this.controller = new DemoController();
	}

	@Override
	public JInternalFrame getAnalysedArchitectureGUI()
	{
		return controller.getGUI();
	}

	@Override
	public JInternalFrame getDefinedArchitectureGUI()
	{
		return controller.getGUI();
	}

	@Override
	public void drawAnalysedArchitecture() {

	}

	@Override
	public void drawAnalysedArchitectureWithViolations() {

	}

	@Override
	public void drawDefinedArchitecture() {

	}

	@Override
	public void drawDefinedArchitectureWithViolations() {

	}
	
	@Override
	public Element getWorkspaceData() {
		Element data = new Element("ArchitecureGraphicsService");
		data.addContent("testdata");
		return data;
	}

	@Override
	public void loadWorkspaceData(Element workspaceData) {
		//TODO: Set workspace data.
	}
}
