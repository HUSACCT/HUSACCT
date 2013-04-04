package husacct.define;

import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.savechain.ISaveable;
import husacct.common.services.IObservableService;
import husacct.define.domain.Project;

import java.util.ArrayList;

import javax.swing.JInternalFrame;

import org.jdom2.Element;

public interface IDefineService extends ISaveable, IObservableService {
	
	public RuleDTO[] getDefinedRules();
	public ModuleDTO[] getRootModules();
	public ApplicationDTO getApplicationDetails();
	public void createApplication(String name, ArrayList<Project> projects, String version);
	public ModuleDTO[] getChildrenFromModule(String logicalPath);
	public String getParentFromModule(String logicalPath);
	public boolean isDefined();
	public boolean isMapped();
	
	public JInternalFrame getDefinedGUI();
	
	public Element getLogicalArchitectureData();
	public void loadLogicalArchitectureData(Element e);
}
