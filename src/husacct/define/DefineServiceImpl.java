package husacct.define;

import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;
import husacct.define.domain.Application;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.DefineDomainService;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.Module;
import husacct.define.task.ApplicationController;

import javax.swing.JFrame;

import org.jdom2.Element;

public class DefineServiceImpl implements IDefineService {
	private DefineDomainService defineDomainService = new DefineDomainService();
	private DomainParser domainParser = new DomainParser();
	
	@Override
	public void createApplication(String name, String[] paths, String language, String version) {
		defineDomainService.createApplication(name, paths, language, version);
	}
	
	@Override
	public ApplicationDTO getApplicationDetails() {
		Application app = defineDomainService.getApplicationDetails();
		ApplicationDTO appDTO = domainParser.parseApplication(app);
		return appDTO;		
	}
	
	@Override
	public ModuleDTO[] getRootModules() {	
		ModuleDTO[] moduleDTOs = getModules();
		for (ModuleDTO moduleDTO : moduleDTOs){
			moduleDTO.subModules = new ModuleDTO[]{};
		}
		return moduleDTOs;
	}
	
	private ModuleDTO[] getModules() {
		Module[] modules = defineDomainService.getModules();
		ModuleDTO[] moduleDTOs = domainParser.parseModules(modules);
		return moduleDTOs;
	}
	
	@Override
	public RuleDTO[] getDefinedRules() {
		AppliedRule[] rules = defineDomainService.getAppliedRules();
		RuleDTO[] ruleDTOs = domainParser.parseRule(rules);
		return ruleDTOs;
	}

	@Override
	public ModuleDTO[] getChildsFromModule(String logicalPath) {
		Module module = defineDomainService.getModuleByLogicalPath(logicalPath);
		ModuleDTO moduleDTO = domainParser.parseModule(module);
		ModuleDTO[] childModuleDTOs = moduleDTO.subModules;
		//TODO removes subModules from childModulesDTOs
		return childModuleDTOs;
	}

	@Override
	public String getParentFromModule(String logicalPath) {
		String parentLogicalPath = "";
		if (logicalPath.contains(".")){
			String[] moduleNames = logicalPath.split("\\.");
			parentLogicalPath += moduleNames[0];
			for (int i = 1;i<moduleNames.length-1;i++){
				parentLogicalPath += "." + moduleNames[i];
			}
			//Check if exists, an exception with automaticly be thrown
			SoftwareArchitecture.getInstance().getModuleByLogicalPath(parentLogicalPath);
		}
		else {
			parentLogicalPath = "**";
		}
		return parentLogicalPath;
	}
	
	
	public JFrame getDefinedGUI(){
		ApplicationController applicationController = new ApplicationController();
		applicationController.initUi();
		return applicationController.getApplicationFrame();
	}
	
	public Element getLogicalArchitectureData(){
		//TODO: Implement in Construction I
		Element e = new Element("Root Element");
		return e;
	}

	public void loadLogicalArchitectureData(Element e){
		//TODO: Implement in Construction I
	}

	@Override
	public Element getWorkspaceData() {
		//TODO: Implement in Construction I
		Element e = new Element("Root Element");
		return e;
	}

	@Override
	public void loadWorkspaceData(Element workspaceData) {
		//TODO: Implement in Construction I
	}
}
