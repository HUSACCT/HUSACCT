package husacct.define;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.dto.RuleDTO;
import husacct.common.services.ObservableService;
import husacct.define.domain.Application;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.module.modules.Layer;
import husacct.define.domain.services.AppliedRuleDomainService;
import husacct.define.domain.services.ModuleDomainService;
import husacct.define.domain.services.SoftwareArchitectureDomainService;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;
import husacct.define.task.ApplicationController;
import husacct.define.task.AppliedRuleController;
import husacct.define.task.DefinitionController;
import husacct.define.task.JtreeController;
import husacct.define.task.SoftwareUnitController;
import husacct.define.task.persistency.PersistentDomain;
import husacct.define.task.persistency.PersistentDomain.DomainElement;
import husacct.define.task.report.ReportArchitectureAbstract;
import husacct.define.task.report.ReportArchitectureToExcel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JInternalFrame;

import org.apache.log4j.Logger;
import org.jdom2.Element;

public class DefineServiceImpl extends ObservableService implements IDefineService {
	private AppliedRuleDomainService appliedRuleService = new AppliedRuleDomainService();
	private SoftwareArchitectureDomainService defineDomainService = new SoftwareArchitectureDomainService();
	private DomainToDtoParser domainParser = new DomainToDtoParser();
	private ModuleDomainService moduleService = new ModuleDomainService();
	private boolean isMapped;
	protected final IAnalyseService analyseService = ServiceProvider.getInstance().getAnalyseService();
	private Logger logger = Logger.getLogger(DefineServiceImpl.class);


	public DefineServiceImpl() {
		super();
		reset();
		isMapped = false;
	}

	@Override
	public void analyze() {
		StateService.instance().analyze();
	}

	@Override
	public void createApplication(String name, ArrayList<ProjectDTO> projects, String version) {
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
	public ModuleDTO[] getModule_TheChildrenOfTheModule(String logicalPath) {
		ModuleDTO[] childModuleDTOs;
		if (logicalPath.equals("**")) {
			childModuleDTOs = getModule_AllRootModules();
		} else {
			ModuleStrategy module = moduleService.getModuleByLogicalPath(logicalPath);
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
	public HashSet<String> getModule_AllPhysicalClassPathsOfModule(String logicalPath) {
		HashSet<String> resultClasses = new HashSet<String>();
		TreeMap<String, SoftwareUnitDefinition> allAssignedSoftwareUnits = getAllAssignedSoftwareUnitsOfModule(logicalPath);
		// Get the physical classPaths of all classes represented by the SUs
		Set<String> allAssignedSoftwareUnitNames = allAssignedSoftwareUnits.keySet();
		for (String suName : allAssignedSoftwareUnitNames){
			SoftwareUnitDefinition softwareUnit = allAssignedSoftwareUnits.get(suName);
			String suType = softwareUnit.getType().toString();
			if (suType.toLowerCase().equals("class") || suType.toLowerCase().equals("interface") || suType.toLowerCase().equals("library")){
				resultClasses.add(suName);
			}
			// Get all underlying classes (also inner classes) from AnalyseService and add them to resultClasses
			List<String> AllPhysicalClassPaths = analyseService.getAllPhysicalClassPathsOfSoftwareUnit(suName); 
			resultClasses.addAll(AllPhysicalClassPaths); 
		}
		return resultClasses;
	}

	@Override // Returns all paths of subpackages (and subsub, etc) within the assigned software units, but not the paths of these assigned software units
	public HashSet<String> getModule_AllPhysicalPackagePathsOfModule(String logicalPath){
		HashSet<String> resultPackages = new HashSet<String>();
		TreeMap<String, SoftwareUnitDefinition> allAssignedSoftwareUnits = getAllAssignedSoftwareUnitsOfModule(logicalPath);
		// Get the physical classPaths of all classes represented by the SUs
		Set<String> allAssignedSoftwareUnitNames = allAssignedSoftwareUnits.keySet();
		for (String suName : allAssignedSoftwareUnitNames){
			SoftwareUnitDefinition softwareUnit = allAssignedSoftwareUnits.get(suName);
			String suType = softwareUnit.getType().toString();
			if (suType.toLowerCase().equals("package")){
				// Get all underlying packages from AnalyseService and add them to resultPackages
				List<String> AllPhysicalClassPaths = analyseService.getAllPhysicalPackagePathsOfSoftwareUnit(suName); 
				resultPackages.addAll(AllPhysicalClassPaths); 
			}
		}
		return resultPackages;
	}

	private TreeMap<String, SoftwareUnitDefinition> getAllAssignedSoftwareUnitsOfModule(String logicalPath){
		TreeMap<String, SoftwareUnitDefinition> allAssignedSoftwareUnits = new TreeMap<String, SoftwareUnitDefinition>();
		try {
			ModuleStrategy[] modules = null;
			// 1 Get the module(s)
			if (logicalPath.equals("**")) {
				modules = moduleService.getRootModules();
			} else {
				modules = new ModuleStrategy[1];
				modules[0] =(moduleService.getModuleByLogicalPath(logicalPath));
			}
			// 2 Get the assigned SoftwareUnits of the module(s) and all its child modules 
			for (ModuleStrategy module : modules){
				HashMap<String, SoftwareUnitDefinition> softwareUnits = module.getAllAssignedSoftwareUnitsInTree();
				if(softwareUnits != null)
					allAssignedSoftwareUnits.putAll(softwareUnits);
			}
        } catch (Exception e) {
	        this.logger.error(new Date().toString() + " Exception: "  + e );
	        //e.printStackTrace();
        }
		return allAssignedSoftwareUnits;
	}
	
	@Override
	// Gets the hierarchical level of a module. Throws RuntimeException when the module is not found.
	public int getHierarchicalLevelOfLayer(String logicalPath){
		int hierarchicalLevel = 0;
		ModuleStrategy module = moduleService.getModuleByLogicalPath(logicalPath);
		if(module != null){
			if(module.getType().equals("Layer")){
				Layer layer = (Layer) module;
				hierarchicalLevel = layer.getHierarchicalLevel();
			}
		}
		return hierarchicalLevel;
	}

	
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
		ArrayList<AppliedRuleStrategy> rules = appliedRuleService.getAllEnabledAppliedRules();
		RuleDTO[] ruleDTOs = domainParser.parseRules(rules);
		return ruleDTOs;
	}

	public DefinitionController getDefinitionController() {
		return DefinitionController.getInstance();
	}

	@Override
	public Element exportIntendedArchitecture() {
		PersistentDomain pd = new PersistentDomain(defineDomainService, moduleService, appliedRuleService);
		pd.setParseData(DomainElement.LOGICAL);
		return pd.getWorkspaceData();
	}

	@Override
	public ModuleDTO getModule_BasedOnSoftwareUnitName(String physicalPath) {
		ModuleDTO returnValue = null;
        String[] splitted = physicalPath.split("\\.");
        int lenght = splitted.length;
        for(int i = lenght; i > 0; i--){
	        ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleBySoftwareUnit(physicalPath);
			if (module != null){
				returnValue = domainParser.parseModule(module);
				return returnValue;
			} 
			else{
				// Split the last part of physicalPath, if possible, and retry 
		        if(i >= 2){
			        physicalPath = splitted[0];
			        for (int j = 1; j < i-1; j++) {
			        	physicalPath = physicalPath + "."  + splitted[j];
			        }
		        }
		        else{
		        	// Do nothing; logical module not found; physical path may not be assigned to a logical module; return null
		        }
			}
        }
		return returnValue;
	}

	@Override
	public String getModule_TheParentOfTheModule(String logicalPath) {
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
	public ModuleDTO[] getModule_AllRootModules() {
		ModuleStrategy[] modules = moduleService.getRootModules();
		ModuleDTO[] moduleDTOs = domainParser.parseRootModules(modules);
		return moduleDTOs;
	}

	public SoftwareUnitController getSoftwareUnitController() {
		return new SoftwareUnitController(0);
	}

	@Override
	public Element getWorkspaceData() {
		PersistentDomain pd = new PersistentDomain(defineDomainService, moduleService, appliedRuleService);
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
		if(!isMapped) {
			ArrayList<ModuleStrategy> modules = SoftwareArchitecture.getInstance().getModules();
			for (ModuleStrategy module : modules) {
				if (module.isMapped()) {
					isMapped = true;
				}
			}
		}
		return isMapped;
	}

	@Override
	public void importIntendedArchitecture(Element e) {
		reset();
		PersistentDomain pd = new PersistentDomain(defineDomainService, moduleService, appliedRuleService);
		pd.setParseData(DomainElement.LOGICAL);
		pd.loadWorkspaceData(e);
		DefinitionController.getInstance().notifyObservers();
		getDefinitionController().initSettings();
	}

	@Override
	public void loadWorkspaceData(Element workspaceData) {
		PersistentDomain pd = new PersistentDomain(defineDomainService, moduleService, appliedRuleService);
		pd.loadWorkspaceData(workspaceData);
	}

	private void reset() {
		//StateService.instance().reset();
		defineDomainService = new SoftwareArchitectureDomainService();
		moduleService = new ModuleDomainService();
		appliedRuleService = new AppliedRuleDomainService();
		domainParser = new DomainToDtoParser();

		SoftwareArchitecture.setInstance(new SoftwareArchitecture());
		DefinitionController.setInstance(new DefinitionController());
	}

	public void reportArchitecture(String fullFilePath) {
		ReportArchitectureAbstract reporter = new ReportArchitectureToExcel();
		reporter.write(fullFilePath);
	}

}
