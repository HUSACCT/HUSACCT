package husaccttest.graphics;

import javax.swing.JInternalFrame;

import org.jdom2.Element;

import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.services.IServiceListener;
import husacct.define.IDefineService;

public class DefineServiceStub implements IDefineService {

	@Override
	public Element getWorkspaceData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadWorkspaceData(Element workspaceData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addServiceListener(IServiceListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyServiceListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	public RuleDTO[] getDefinedRules() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModuleDTO[] getRootModules() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApplicationDTO getApplicationDetails() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createApplication(String name, String[] paths, String language, String version) {
		// TODO Auto-generated method stub

	}

	@Override
	public ModuleDTO[] getChildrenFromModule(String logicalPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParentFromModule(String logicalPath) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDefined() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMapped() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JInternalFrame getDefinedGUI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element getLogicalArchitectureData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void loadLogicalArchitectureData(Element e) {
		// TODO Auto-generated method stub

	}

}
