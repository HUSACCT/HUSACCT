package husacct.define;

import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.services.ObservableService;
import husacct.define.domain.Application;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.Module;
import husacct.define.domain.services.AppliedRuleDomainService;
import husacct.define.domain.services.AppliedRuleExceptionDomainService;
import husacct.define.domain.services.ModuleDomainService;
import husacct.define.domain.services.SoftwareArchitectureDomainService;
import husacct.define.persistency.PersistentDomain;
import husacct.define.persistency.PersistentDomain.DomainElement;
import husacct.define.task.ApplicationController;
import husacct.define.task.AppliedRuleController;
import husacct.define.task.DefinitionController;
import husacct.define.task.JtreeController;
import husacct.define.task.JtreeStateEngine;
import husacct.define.task.SoftwareUnitController;

import java.util.ArrayList;

import javax.swing.JInternalFrame;

import org.jdom2.Element;

public class DefineServiceImpl extends ObservableService implements
	IDefineService {
    private AppliedRuleDomainService appliedRuleService = new AppliedRuleDomainService();
    private SoftwareArchitectureDomainService defineDomainService = new SoftwareArchitectureDomainService();
    private DomainParser domainParser = new DomainParser();
    private AppliedRuleExceptionDomainService exceptionService = new AppliedRuleExceptionDomainService();
    private ModuleDomainService moduleService = new ModuleDomainService();

    public DefineServiceImpl() {
	super();
	reset();
    }

    @Override
    public void analyze() {
	JtreeStateEngine.instance().analyze();

    }

    @Override
    public void createApplication(String name, ArrayList<ProjectDTO> projects,
	    String version) {
	defineDomainService.createApplication(name, projects, version);
    }

    @Override
    public ApplicationDTO getApplicationDetails() {
	Application app = defineDomainService.getApplicationDetails();
	ApplicationDTO appDTO = domainParser.parseApplication(app);
	return appDTO;
    }

    public AppliedRuleController getAppliedRuleController() {
	return new AppliedRuleController(0, -1);
    }

    @Override
    public ModuleDTO[] getChildrenFromModule(String logicalPath) {
	ModuleDTO[] childModuleDTOs;
	if (logicalPath.equals("**")) {
	    childModuleDTOs = getRootModules();
	} else {
	    Module module = moduleService.getModuleByLogicalPath(logicalPath);
	    ModuleDTO moduleDTO = domainParser.parseModule(module);
	    childModuleDTOs = moduleDTO.subModules;
	}

	// Removing nested childs
	for (ModuleDTO modDTO : childModuleDTOs) {
	    modDTO.subModules = new ModuleDTO[] {};
	}

	return childModuleDTOs;
    }

    @Override
    public JInternalFrame getDefinedGUI() {
	ApplicationController applicationController = new ApplicationController();
	applicationController.initUi();
	JInternalFrame jinternalFrame = applicationController
		.getApplicationFrame();
	jinternalFrame.setVisible(false);
	return jinternalFrame;
    }

    @Override
    public RuleDTO[] getDefinedRules() {
	AppliedRuleStrategy[] rules = appliedRuleService.getAppliedRules();
	RuleDTO[] ruleDTOs = domainParser.parseRules(rules);
	return ruleDTOs;
    }

    public DefinitionController getDefinitionController() {
	return DefinitionController.getInstance();
    }

    @Override
    public Element getLogicalArchitectureData() {
	PersistentDomain pd = new PersistentDomain(defineDomainService,
		moduleService, appliedRuleService, exceptionService);
	pd.setParseData(DomainElement.LOGICAL);
	return pd.getWorkspaceData();
    }

    @Override
    public String getParentFromModule(String logicalPath) {
	String parentLogicalPath = "";
	if (logicalPath.contains(".")) {
	    String[] moduleNames = logicalPath.split("\\.");
	    parentLogicalPath += moduleNames[0];
	    for (int i = 1; i < moduleNames.length - 1; i++) {
		parentLogicalPath += "." + moduleNames[i];
	    }
	    // Check if exists, an exception will automaticly be thrown
	    SoftwareArchitecture.getInstance().getModuleByLogicalPath(
		    parentLogicalPath);
	} else {
	    parentLogicalPath = "**";
	}
	return parentLogicalPath;
    }

    @Override
    public ModuleDTO[] getRootModules() {
	Module[] modules = moduleService.getRootModules();
	ModuleDTO[] moduleDTOs = domainParser.parseRootModules(modules);
	return moduleDTOs;
    }

    public SoftwareUnitController getSoftwareUnitController() {
	return new SoftwareUnitController(0);
    }

    @Override
    public Element getWorkspaceData() {
	PersistentDomain pd = new PersistentDomain(defineDomainService,
		moduleService, appliedRuleService, exceptionService);
	return pd.getWorkspaceData();
    }

    @Override
    public boolean isDefined() {
	boolean isDefined = false;
	if (SoftwareArchitecture.getInstance().getModules().size() > 0) {
	    isDefined = true;
	}
	return isDefined;
    }

    @Override
    public boolean isMapped() {
	boolean isMapped = false;
	ArrayList<Module> modules = SoftwareArchitecture.getInstance()
		.getModules();
	for (Module module : modules) {
	    if (module.isMapped()) {
		isMapped = true;
	    }
	}
	return isMapped;
    }

    @Override
    public void isReanalyzed() {
	JtreeController.instance().setLoadState(false);

    }

    @Override
    public void loadLogicalArchitectureData(Element e) {
	PersistentDomain pd = new PersistentDomain(defineDomainService,
		moduleService, appliedRuleService, exceptionService);
	pd.setParseData(DomainElement.LOGICAL);
	pd.loadWorkspaceData(e);
    }

    @Override
    public void loadWorkspaceData(Element workspaceData) {
	PersistentDomain pd = new PersistentDomain(defineDomainService,
		moduleService, appliedRuleService, exceptionService);
	pd.loadWorkspaceData(workspaceData);
    }

    private void reset() {
	// TODO this is just a hotfix
	defineDomainService = new SoftwareArchitectureDomainService();
	moduleService = new ModuleDomainService();
	appliedRuleService = new AppliedRuleDomainService();
	domainParser = new DomainParser();
	exceptionService = new AppliedRuleExceptionDomainService();

	SoftwareArchitecture.setInstance(new SoftwareArchitecture());
	DefinitionController.setInstance(new DefinitionController());
    }
}
