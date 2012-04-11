package husacct.define;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;

public interface IDefineService {
	
	public RuleDTO[] getDefinedRules();
	public ModuleDTO[] getRootModules();
	public ApplicationDTO getApplicationDetails();
	public void createApplication(String name, String[] paths, String language, String version);
	public ModuleDTO[] getChildsFromModule(String logicalPath);
	public String getParentFromModule(String logicalPath);
	public Document exportLogicalArchitecture() throws ParserConfigurationException;
	public void importLogicalArchitecture(Document doc);
	public Document exportPhysicalArchitecture() throws ParserConfigurationException;
	public void importPhysicalArchitecture(Document doc);
	public JFrame getDefinedGUI();
}
