package husacct.define;

import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.savechain.ISaveable;

import javax.swing.JFrame;

import org.jdom2.Element;

public interface IDefineService extends ISaveable{
	
	public RuleDTO[] getDefinedRules();
	public ModuleDTO[] getRootModules();
	public ApplicationDTO getApplicationDetails();
	public void createApplication(String name, String[] paths, String language, String version);
	public ModuleDTO[] getChildsFromModule(String logicalPath);
	public String getParentFromModule(String logicalPath);
	public boolean isDefined();
	public boolean isMapped();
	
	public JFrame getDefinedGUI();
	
	public Element getLogicalArchitectureData();
	public void loadLogicalArchitectureData(Element e);
}
