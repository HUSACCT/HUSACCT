package husaccttest.validate.benchmark;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.PhysicalPathDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.dto.RuleDTO;
import husacct.control.ControlServiceImpl;
import husacct.control.IControlService;
import husacct.control.task.States;
import husacct.define.IDefineService;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleFactory;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.module.modules.Component;
import husacct.define.domain.module.modules.ExternalLibrary;
import husacct.define.domain.module.modules.Facade;
import husacct.define.domain.module.modules.Layer;
import husacct.define.domain.module.modules.SubSystem;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition.Type;
import husacct.define.task.DefinitionController;
import husacct.validate.IValidateService;
import husacct.validate.ValidateServiceImpl;
import husacct.validate.domain.DomainServiceImpl;
import husacct.validate.domain.configuration.ActiveRuleType;
import husacct.validate.domain.configuration.ActiveViolationType;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class RuleTypeSupport {
	private final static String projectSourcePath = "testprojects\\java\\srma";
			//"D:\\Dropbox\\Themaopdracht ASE\\Benchmark test\\3 - Benchmark test Constructie\\SacctTestCode SRMA 2013-04-15\\SacctTestCode SRMA 2013-04-15\\src";
	private final static String applicationName = "HUSACCT_BENCHMARK_SRMA_TEST";
	private final static String programLanguage = "Java";
	private final static String programVersion = "1.0";
	private TreeMap<String, String[]> violationTypesPerRuleType;
	
	private SoftwareArchitecture softwareArchitecture = SoftwareArchitecture.getInstance();
    private AppliedRuleFactory ruleFactory = new AppliedRuleFactory();
	private static Logger logger;
    
	private ArrayList<ProjectDTO> allProjects;
	private DependencyDTO[] allDependencies = null;
	private ArrayList<Violation> allViolations;
	private RuleDTO[] appliedRules;
	
	private IControlService controlService = ServiceProvider.getInstance().getControlService();
	private IAnalyseService analyseService = ServiceProvider.getInstance().getAnalyseService();
	private IDefineService defineService = ServiceProvider.getInstance().getDefineService();
	private IValidateService validateService = ServiceProvider.getInstance().getValidateService();
	private DefinitionController definitionController = new DefinitionController();
	
	private static boolean isAnalysed = false;
	
	
	public void setUpSRMATest() {
		setLog4jConfiguration();
		
		ServiceProvider.getInstance().resetServices();
		StateService.instance();
		controlService = ServiceProvider.getInstance().getControlService();
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		defineService = ServiceProvider.getInstance().getDefineService();
		validateService = ServiceProvider.getInstance().getValidateService();
		
		createApplication();
		analyseApplication();
		
		//setUpSoftwareArchitecture();
		setUpStructureDTO();
		validateDefinedArchitecture();
		
	}
	
	public void createApplication() {
		createProjectDTOs();
		
		((ControlServiceImpl) controlService).getMainController().getWorkspaceController().createWorkspace(applicationName);
		ServiceProvider.getInstance().getDefineService().createApplication(applicationName, allProjects, programVersion);
		defineService = ServiceProvider.getInstance().getDefineService();
		
		((ControlServiceImpl) controlService).getMainController().getWorkspaceController().getCurrentWorkspace().setApplicationData(ServiceProvider.getInstance().getDefineService().getApplicationDetails());
	}
	
	public void createProjectDTOs() {
		ArrayList<String> projectSourcePaths = new ArrayList<String>();
		projectSourcePaths.add(projectSourcePath);
		
		ArrayList<AnalysedModuleDTO> analysedModules = new ArrayList<AnalysedModuleDTO>();
		ProjectDTO project = new ProjectDTO(applicationName, projectSourcePaths, programLanguage, programVersion, "Testapplicatie", analysedModules);
		allProjects = new ArrayList<ProjectDTO>();
		allProjects.add(project);
	}
	
	public void analyseApplication() {
		((ControlServiceImpl) controlService).getMainController().getApplicationController().analyseApplication();

		analyseService = ServiceProvider.getInstance().getAnalyseService();
		
		
		while(!isAnalysed){
			try {
				Thread.sleep((long)10);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			isAnalysed = analyseService.isAnalysed();
		}
		
		allDependencies = analyseService.getAllUnfilteredDependencies();
	}
	
	public void setUpSoftwareArchitecture() {
		definitionController = new DefinitionController();
		
		softwareArchitecture = new SoftwareArchitecture("SoftwareArchitecture", "This architecture is used for testing purposes");
		SoftwareArchitecture.setInstance(softwareArchitecture);
		
		ModuleStrategy layerPresentation = new Layer();
		layerPresentation.set("Presentation", "This is layer 1");
		layerPresentation.addSUDefinition(new SoftwareUnitDefinition("presentation", Type.PACKAGE));
		
		ModuleStrategy layerDomain = new Layer();
		layerDomain.set("Domain", "This is layer 2");
		layerDomain.addSUDefinition(new SoftwareUnitDefinition("domain", Type.PACKAGE));
		
		ModuleStrategy layerTechnology = new Layer();
		layerTechnology.set("Technology", "This is layer 3");
		layerTechnology.addSUDefinition(new SoftwareUnitDefinition("technology", Type.PACKAGE));
		
		//External libraries
		ModuleStrategy externalSystemTwitter4j = new ExternalLibrary();
		externalSystemTwitter4j.set("Twitter", "This is a external system");
		externalSystemTwitter4j.addSUDefinition(new SoftwareUnitDefinition("twitter4j", Type.EXTERNALLIBRARY));
		//externalSystemTwitter4j = ((DefineComponentFactory) defineService).getDefineComponent(externalSystemTwitter4j);
		
		ModuleStrategy externalSystemFoursquareApi = new ExternalLibrary();
		externalSystemFoursquareApi.set("FoursquareApi", "This is a external system");
		externalSystemFoursquareApi.addSUDefinition(new SoftwareUnitDefinition("foursquareapi", Type.EXTERNALLIBRARY));
		
		
		//PROPERTY RULE TYPES
		
		ModuleStrategy subModuleTechPropertyRules = new SubSystem();
		subModuleTechPropertyRules.set("Propertyrules", "This is a subsystem");
		subModuleTechPropertyRules.addSUDefinition(new SoftwareUnitDefinition("technology.propertyrules", Type.PACKAGE));
		
		ModuleStrategy subModuleNamingPrefix = new SubSystem();
		subModuleNamingPrefix.set("Naming_prefix", "This is a subsystem");
		subModuleNamingPrefix.addSUDefinition(new SoftwareUnitDefinition("technology.propertyrules.naming_prefix", Type.PACKAGE));
		
		ModuleStrategy subModuleNamingPostfix = new SubSystem();
		subModuleNamingPostfix.set("Naming_postfix", "This is a subsystem");		
		subModuleNamingPostfix.addSUDefinition(new SoftwareUnitDefinition("technology.propertyrules.naming_postfix", Type.PACKAGE));
		
		ModuleStrategy subModuleNamingMid = new SubSystem();
		subModuleNamingMid.set("Naming_mid", "This is a subsystem");
		subModuleNamingMid.addSUDefinition(new SoftwareUnitDefinition("technology.propertyrules.naming_mid", Type.PACKAGE));
		
		ModuleStrategy component1 = new Component();
		component1.set("Component", "This is a component");
		SoftwareUnitDefinition component1_sud = new SoftwareUnitDefinition("technology.propertyrules.component", Type.PACKAGE);
		component1.addSUDefinition(component1_sud);
		
		ModuleStrategy componentFacade1 = new Facade();
		component1.set("ComponentFacade", "This is a facade");
		SoftwareUnitDefinition componentFacade1_sud1 = new SoftwareUnitDefinition("technology.propertyrules.component.ComponentInterface", Type.CLASS);
		SoftwareUnitDefinition componentFacade1_sud2 = new SoftwareUnitDefinition("technology.propertyrules.component.ComponentDTO", Type.CLASS);
		ArrayList<SoftwareUnitDefinition> componentFacade1_suds = new ArrayList<SoftwareUnitDefinition>();
		componentFacade1_suds.add(componentFacade1_sud1);
		componentFacade1_suds.add(componentFacade1_sud2);
		component1.addSUDefinition(componentFacade1_suds);
		
		ModuleStrategy subModuleSuperClassInheritance = new SubSystem();
		subModuleSuperClassInheritance.set("Superclass_inheritance", "This is a subsystem");
		subModuleSuperClassInheritance.addSUDefinition(new SoftwareUnitDefinition("technology.propertyrules.superclass_inheritance", Type.PACKAGE));
		
		ModuleStrategy subModuleInterfaceInheritance = new SubSystem();
		subModuleInterfaceInheritance.set("Interface_inheritance", "This is a subsystem");
		subModuleInterfaceInheritance.addSUDefinition(new SoftwareUnitDefinition("technology.propertyrules.interface_inheritance", Type.PACKAGE));
		
		ModuleStrategy subModuleDatasource = new SubSystem();
		subModuleNamingPrefix.set("Datasource", "This is a subsystem");
		subModuleNamingPrefix.addSUDefinition(new SoftwareUnitDefinition("technology.propertyrules.Datasource", Type.CLASS));
		
		ModuleStrategy subModuleIDatasource = new SubSystem();
		subModuleNamingPostfix.set("IDatasource", "This is a subsystem");		
		subModuleNamingPostfix.addSUDefinition(new SoftwareUnitDefinition("technology.propertyrules.IDatasource", Type.INTERFACE));
		
		//RELATION RULE TYPES
		
		ModuleStrategy subModulePresRelationRules = new SubSystem();
		subModulePresRelationRules.set("Relationrules", "This is a subsystem");
		subModulePresRelationRules.addSUDefinition(new SoftwareUnitDefinition("presentation.relationrules", Type.PACKAGE));
		
		ModuleStrategy subModulePresSkipCallBan = new SubSystem();
		subModulePresSkipCallBan.set("Skip_call_ban", "This is a subsystem");
		subModulePresSkipCallBan.addSUDefinition(new SoftwareUnitDefinition("presentation.skip_call_ban", Type.PACKAGE));
		
		ModuleStrategy subModulePresNotAllowed = new SubSystem();
		subModulePresNotAllowed.set("Notallowed", "This is a subsystem");
		subModulePresNotAllowed.addSUDefinition(new SoftwareUnitDefinition("presentation.relationrules.notallowed", Type.PACKAGE));
		
		ModuleStrategy subModulePresNotAllowedAllowed = new SubSystem();
		subModulePresNotAllowedAllowed.set("Allowed", "This is a subsystem");
		subModulePresNotAllowedAllowed.addSUDefinition(new SoftwareUnitDefinition("presentation.relationrules.notallowed.allowed", Type.PACKAGE));
		
		ModuleStrategy subModuleDomainRelationRules = new SubSystem();
		subModuleDomainRelationRules.set("Relationrules", "This is a subsystem");
		subModuleDomainRelationRules.addSUDefinition(new SoftwareUnitDefinition("domain.relationrules", Type.PACKAGE));
		
		ModuleStrategy subModuleDomainBackCallBan = new SubSystem();
		subModuleDomainBackCallBan.set("Back_call_ban", "This is a subsystem");
		subModuleDomainBackCallBan.addSUDefinition(new SoftwareUnitDefinition("domain.relationrules.back_call_ban", Type.PACKAGE));
		
		ModuleStrategy subModuleDomainIsAllowedToUse = new SubSystem();
		subModuleDomainIsAllowedToUse.set("Is_allowed_to_use", "This is a subsystem");
		subModuleDomainIsAllowedToUse.addSUDefinition(new SoftwareUnitDefinition("domain.relationrules.is_allowed_to_use", Type.PACKAGE));
		
		ModuleStrategy subModuleDomainIsOnlyAllowedToUse = new SubSystem();
		subModuleDomainIsOnlyAllowedToUse.set("Is_only_allowed_to_use", "This is a subsystem");
		subModuleDomainIsOnlyAllowedToUse.addSUDefinition(new SoftwareUnitDefinition("domain.relationrules.is_only_allowed_to_use", Type.PACKAGE));
		
		ModuleStrategy subModuleDomainIsTheOnlyModuleAllowedToUse1 = new SubSystem();
		subModuleDomainIsTheOnlyModuleAllowedToUse1.set("Is_only_module_allowed_to_use_1", "This is a subsystem");
		subModuleDomainIsTheOnlyModuleAllowedToUse1.addSUDefinition(new SoftwareUnitDefinition("domain.relationrules.is_only_module_allowed_to_use_1", Type.PACKAGE));
		
		ModuleStrategy subModuleDomainIsTheOnlyModuleAllowedToUse2 = new SubSystem();
		subModuleDomainIsTheOnlyModuleAllowedToUse2.set("Is_only_module_allowed_to_use_2", "This is a subsystem");
		subModuleDomainIsTheOnlyModuleAllowedToUse2.addSUDefinition(new SoftwareUnitDefinition("domain.relationrules.is_only_module_allowed_to_use_2", Type.PACKAGE));		
		
		ModuleStrategy subModuleDomainIsNotAllowedToUse = new SubSystem();
		subModuleDomainIsNotAllowedToUse.set("Is_not_allowed_to_use", "This is a subsystem");
		subModuleDomainIsNotAllowedToUse.addSUDefinition(new SoftwareUnitDefinition("domain.relationrules.is_not_allowed_to_use", Type.PACKAGE));
		
		ModuleStrategy subModuleDomainIsNotAllowedToUseViolating = new SubSystem();
		subModuleDomainIsNotAllowedToUseViolating.set("Violating", "This is a subsystem");
		subModuleDomainIsNotAllowedToUseViolating.addSUDefinition(new SoftwareUnitDefinition("domain.relationrules.is_not_allowed_to_use.violating", Type.PACKAGE));
		
		ModuleStrategy subModuleDomainMustUseCorrect = new SubSystem();
		subModuleDomainMustUseCorrect.set("Must_use_correct", "This is a subsystem");
		subModuleDomainMustUseCorrect.addSUDefinition(new SoftwareUnitDefinition("domain.relationrules.must_use_correct", Type.PACKAGE));
		
		ModuleStrategy subModuleDomainMustUseViolating = new SubSystem();
		subModuleDomainMustUseViolating.set("Must_use_correct", "This is a subsystem");
		subModuleDomainMustUseViolating.addSUDefinition(new SoftwareUnitDefinition("domain.relationrules.must_use_violating", Type.PACKAGE));		
		
		ModuleStrategy subModuleTechRelationRules = new SubSystem();
		subModuleTechRelationRules.set("Relationrules", "This is a subsystem");
		subModuleTechRelationRules.addSUDefinition(new SoftwareUnitDefinition("technology.relationrules", Type.PACKAGE));
		
		ModuleStrategy subModuleTechAllowed = new SubSystem();
		subModuleTechAllowed.set("Allowed", "This is a subsystem");
		subModuleTechAllowed.addSUDefinition(new SoftwareUnitDefinition("technology.relationrules.allowed", Type.PACKAGE));
		
		ModuleStrategy subModuleTechAllowedNotAllowed = new SubSystem();
		subModuleTechAllowedNotAllowed.set("Notallowed", "This is a subsystem");
		subModuleTechAllowedNotAllowed.addSUDefinition(new SoftwareUnitDefinition("technology.relationrules.allowed.notallowed", Type.PACKAGE));
		
		ModuleStrategy subModuleTechNotAllowed = new SubSystem();
		subModuleTechNotAllowed.set("Notallowed", "This is a subsystem");
		subModuleTechNotAllowed.addSUDefinition(new SoftwareUnitDefinition("technology.relationrules.notallowed", Type.PACKAGE));
		
		ModuleStrategy subModuleTechNotAllowedAllowed = new SubSystem();
		subModuleTechNotAllowedAllowed.set("Allowed", "This is a subsystem");
		subModuleTechNotAllowedAllowed.addSUDefinition(new SoftwareUnitDefinition("technology.relationrules.notallowed.allowed", Type.PACKAGE));
		
		ModuleStrategy subModuleTechTheonlyone_allowed = new SubSystem();
		subModuleTechTheonlyone_allowed.set("Theonlyone_allowed", "This is a subsystem");
		subModuleTechTheonlyone_allowed.addSUDefinition(new SoftwareUnitDefinition("technology.relationrules.theonlyone_allowed", Type.PACKAGE));
		
		//DEFINE APPLIED RULES
    	AppliedRuleStrategy ruleBackCallBanDomPres = ruleFactory.createDummyRule("IsNotAllowedToMakeBackCall");
    	ruleBackCallBanDomPres.setAppliedRule("Layer Domain is not allowed to make back call to Layer Presentation", layerDomain, layerPresentation);
    	AppliedRuleStrategy ruleBackCallBanTechDom = ruleFactory.createDummyRule("IsNotAllowedToMakeBackCall");
    	ruleBackCallBanTechDom.setAppliedRule("Layer Technology is not allowed to make back call to Layer Domain", layerTechnology, layerDomain);
    	AppliedRuleStrategy ruleBackCallBanTechPres = ruleFactory.createDummyRule("IsNotAllowedToMakeBackCall");
    	ruleBackCallBanTechPres.setAppliedRule("Layer Technology is not allowed to make back call to Layer Presentation", layerTechnology, layerPresentation);
    	AppliedRuleStrategy ruleSkipCallBanTechPres = ruleFactory.createDummyRule("IsNotAllowedToMakeSkipCall");
    	ruleSkipCallBanTechPres.setAppliedRule("Layer Presentation is not allowed to make skip call to Layer Technology", layerPresentation, layerTechnology);
		
    	//> PROPERTY RULE TYPES
    	AppliedRuleStrategy rulePropertyRulesNamingPrefix = ruleFactory.createDummyRule("NamingConvention");
    	rulePropertyRulesNamingPrefix.setAppliedRule("All classes/files/ packages within technology.propertyrules.naming_prefix must have prefix MDB.", new String[]{}, "MDB*", subModuleNamingPrefix, subModuleNamingPrefix, true);
    	AppliedRuleStrategy rulePropertyRulesNamingPostfix = ruleFactory.createDummyRule("NamingConvention");
    	rulePropertyRulesNamingPostfix.setAppliedRule("All classes/files/ packages within technology.propertyrules.naming_postfix must have postfix “MDB”.", new String[]{}, "*MDB", subModuleNamingPostfix, subModuleNamingPostfix, true);
    	AppliedRuleStrategy rulePropertyRulesNamingMid = ruleFactory.createDummyRule("NamingConvention");
    	rulePropertyRulesNamingMid.setAppliedRule("All classes/files/ packages within technology.propertyrules.naming_mid must have “MDB” in the name.", new String[]{}, "*MDB*", subModuleNamingMid, subModuleNamingMid, true);
		AppliedRuleStrategy rulePropertyRulesVisibility = ruleFactory.createDummyRule("VisibilityConvention");
		rulePropertyRulesVisibility.setAppliedRule("All classes in package technology.propertyrules.component have visibility package or lower.", component1, component1);
		AppliedRuleStrategy rulePropertyRulesFacade = ruleFactory.createDummyRule("FacadeConvention");
		rulePropertyRulesFacade.setAppliedRule("Access to the classes in package technology.propertyrules.component is only allowed via its facade: ComponentInterface.", component1, component1);
		AppliedRuleStrategy rulePropertyRulesSuperClass = ruleFactory.createDummyRule("SuperClassInheritanceConvention");
		rulePropertyRulesSuperClass.setAppliedRule("All classes in package technology.propertyrules.superclass_inheritance must extend technology. propertyrules.Datasource", subModuleSuperClassInheritance, subModuleDatasource);
		AppliedRuleStrategy rulePropertyRulesInterface = ruleFactory.createDummyRule("InterfaceInheritanceConvention");
		rulePropertyRulesInterface.setAppliedRule("All classes in package technology.propertyrules.interface_inheritance must implement interface technology.propertyrules.IDatasource", subModuleInterfaceInheritance, subModuleIDatasource);
		
		//> RELATION RULE TYPES
		AppliedRuleStrategy ruleRelationIsAllowedToUse = ruleFactory.createDummyRule("IsAllowedToUse");
		ruleRelationIsAllowedToUse.setAppliedRule("Package domain.relationrules.is_allowed_to_use is only allowed to use package technology.relationrules.allowed.", subModuleDomainIsAllowedToUse, subModuleTechAllowed);
		AppliedRuleStrategy ruleRelationIsOnlyAllowedToUse = ruleFactory.createDummyRule("IsOnlyAllowedToUse");
		ruleRelationIsOnlyAllowedToUse.setAppliedRule("Package domain.relationrules.is_only_allowed_to_use is only allowed to use package technology.relationrules.allowed.", subModuleDomainIsOnlyAllowedToUse, subModuleTechAllowed);
		AppliedRuleStrategy ruleRelationIsTheOnlyModuleAllowedToUse_1 = ruleFactory.createDummyRule("IsTheOnlyModuleAllowedToUse");
		ruleRelationIsTheOnlyModuleAllowedToUse_1.setAppliedRule("Package domain.relationrules.is_the_only_module_allowed_to_use_1 is the only module allowed to use package technology.relationrules.theonlyone_allowed.", subModuleDomainIsTheOnlyModuleAllowedToUse1, subModuleTechTheonlyone_allowed); 
		AppliedRuleStrategy ruleRelationMustUseCorrect = ruleFactory.createDummyRule("MustUse");
		ruleRelationMustUseCorrect.setAppliedRule("Package domain.relationrules.must_use_correct must use package technology.relationrules.allowed.", subModuleDomainMustUseCorrect, subModuleTechAllowed);
		AppliedRuleStrategy ruleRelationMustUseViolating = ruleFactory.createDummyRule("MustUse");
		ruleRelationMustUseViolating.setAppliedRule("Package domain.relationrules.must_use_violating must use package technology.relationrules.allowed.", subModuleDomainMustUseViolating, subModuleTechAllowed);
		AppliedRuleStrategy ruleRelationIsNotAllowedToUse_1 = ruleFactory.createDummyRule("IsNotAllowedToUse");
		ruleRelationIsNotAllowedToUse_1.setAppliedRule("Package domain.relationrules.is_not_allowed_to_use is not allowed to use package technology.relationrules.notallowed.", subModuleDomainIsNotAllowedToUse, subModuleTechNotAllowed);
		AppliedRuleStrategy ruleRelationIsNotAllowedToUse_2 = ruleFactory.createDummyRule("IsNotAllowedToUse");
		ruleRelationIsNotAllowedToUse_2.setAppliedRule("Package domain.relationrules.is_not_allowed_to_use is not allowed to use library foursquareapi.jar (External system). ", subModuleDomainIsNotAllowedToUse, externalSystemFoursquareApi);
		
		
		softwareArchitecture.addAppliedRule(ruleBackCallBanDomPres);
		softwareArchitecture.addAppliedRule(ruleBackCallBanTechDom);
		softwareArchitecture.addAppliedRule(ruleBackCallBanTechPres);
		softwareArchitecture.addAppliedRule(ruleSkipCallBanTechPres);
		softwareArchitecture.addAppliedRule(rulePropertyRulesNamingPrefix);
		softwareArchitecture.addAppliedRule(rulePropertyRulesNamingPostfix);
		softwareArchitecture.addAppliedRule(rulePropertyRulesNamingMid);
		softwareArchitecture.addAppliedRule(rulePropertyRulesVisibility);
		softwareArchitecture.addAppliedRule(rulePropertyRulesFacade);
		softwareArchitecture.addAppliedRule(rulePropertyRulesSuperClass);
		softwareArchitecture.addAppliedRule(rulePropertyRulesInterface);
		softwareArchitecture.addAppliedRule(ruleRelationIsAllowedToUse);
		softwareArchitecture.addAppliedRule(ruleRelationIsOnlyAllowedToUse);
		softwareArchitecture.addAppliedRule(ruleRelationIsTheOnlyModuleAllowedToUse_1);
		softwareArchitecture.addAppliedRule(ruleRelationMustUseCorrect);
		softwareArchitecture.addAppliedRule(ruleRelationMustUseViolating);
		softwareArchitecture.addAppliedRule(ruleRelationIsNotAllowedToUse_1);
		softwareArchitecture.addAppliedRule(ruleRelationIsNotAllowedToUse_2);
		
		//for(AppliedRuleStrategy appRule: softwareArchitecture.getAppliedRules()) {
		//	System.err.println("APPRULE: " + appRule.getId() + " - " + appRule.getRuleType());
		//}
		
		//ADD DEFINED MODULES TO SOFTWARE ARCHITECTURE		
		//System.out.println(">> ID: " + layerPresentation.getId());
		//System.out.println(">- Parent: " + layerPresentation.getparent());
		//softwareArchitecture
		//layerPresentation.setParent(softwareArchitecture);
		
/*		for(ModuleDTO module: defineService.getRootModules()) {
			System.err.println("MOD: " + module.logicalPath + " - " + module.type + " - " + module.subModules);
		}
		for(RuleDTO rule: defineService.getDefinedRules()) {
			System.err.println("RUL: " + rule.ruleTypeKey + " - " + rule.regex + " - " + rule.moduleFrom + " - " + rule.moduleTo);
			for(int i = 0; i < rule.violationTypeKeys.length; i++) {
				System.err.println("> RUL VIOL: " + rule.violationTypeKeys[i]);
			}	
		}

		definitionController.addModule(layerPresentation.getName(), layerPresentation.getDescription(), layerPresentation.getType());
		definitionController.addModule(layerDomain.getName(), layerDomain.getDescription(), layerDomain.getType());
		definitionController.addModule(layerTechnology.getName(), layerTechnology.getDescription(), layerTechnology.getType());
		definitionController.addModule(externalSystemTwitter4j.getName(), externalSystemTwitter4j.getDescription(), externalSystemTwitter4j.getType());
		definitionController.addModule(externalSystemFoursquareApi.getName(), externalSystemFoursquareApi.getDescription(), externalSystemFoursquareApi.getType());
		
		layerPresentation.addSubModule(subModulePresRelationRules);
		
		layerDomain.addSubModule(subModuleDomainRelationRules);
		
		layerTechnology.addSubModule(subModuleTechPropertyRules);
		layerTechnology.addSubModule(subModuleTechRelationRules);
		
		subModuleTechPropertyRules.addSubModule(subModuleNamingPrefix);
		subModuleTechPropertyRules.addSubModule(subModuleNamingPostfix);
		subModuleTechPropertyRules.addSubModule(subModuleNamingMid);
		subModuleTechPropertyRules.addSubModule(component1);
		subModuleTechPropertyRules.addSubModule(subModuleSuperClassInheritance);
		subModuleTechPropertyRules.addSubModule(subModuleInterfaceInheritance);
		subModuleTechPropertyRules.addSubModule(externalSystemTwitter4j);
		
		subModulePresNotAllowed.addSubModule(subModulePresNotAllowedAllowed);
		subModulePresRelationRules.addSubModule(subModulePresSkipCallBan);
		subModulePresRelationRules.addSubModule(subModulePresNotAllowed);
		
		subModuleDomainIsNotAllowedToUse.addSubModule(subModuleDomainIsNotAllowedToUseViolating);
		subModuleDomainRelationRules.addSubModule(subModuleDomainBackCallBan);
		subModuleDomainRelationRules.addSubModule(subModuleDomainIsAllowedToUse);
		subModuleDomainRelationRules.addSubModule(subModuleDomainIsOnlyAllowedToUse);
		subModuleDomainRelationRules.addSubModule(subModuleDomainIsTheOnlyModuleAllowedToUse1);
		subModuleDomainRelationRules.addSubModule(subModuleDomainIsTheOnlyModuleAllowedToUse2);
		subModuleDomainRelationRules.addSubModule(subModuleDomainIsNotAllowedToUse);
		subModuleDomainRelationRules.addSubModule(subModuleDomainMustUseCorrect);
		subModuleDomainRelationRules.addSubModule(subModuleDomainMustUseViolating);
		
		subModuleTechAllowed.addSubModule(subModuleTechAllowedNotAllowed);
		subModuleTechRelationRules.addSubModule(subModuleTechAllowed);
		subModuleTechNotAllowed.addSubModule(subModuleTechNotAllowedAllowed);
		subModuleTechRelationRules.addSubModule(subModuleTechNotAllowed);
		subModuleTechRelationRules.addSubModule(subModuleTechTheonlyone_allowed);
		
		component1.addSubModule(componentFacade1);
		
*/
		//defineService.
	}

	public void setUpStructureDTO() {
		appliedRules = new RuleDTO[18];
		
		generateViolationTypeListPerRuleType();
		
		//General uses modules layer Presentation
		PhysicalPathDTO presRelationAllowedSN = new PhysicalPathDTO("presentation.relationrules.allowed.SocialNetwork", "class");
		ModuleDTO modulePresRelationAllowedSN = new ModuleDTO("Presentation Layer.Relationrules.Allowed.SocialNetwork", new PhysicalPathDTO[] { presRelationAllowedSN }, "class", new ModuleDTO[] {});
		PhysicalPathDTO presRelationAllowed = new PhysicalPathDTO("presentation.relationrules.Allowed", "package");
		ModuleDTO modulePresRelationAllowed = new ModuleDTO("Presentation Layer.Relationrules.Allowed", new PhysicalPathDTO[] { presRelationAllowed }, "package", new ModuleDTO[] { modulePresRelationAllowedSN});
		
		PhysicalPathDTO presRelationNotAllowedAllowedASN2 = new PhysicalPathDTO("presentation.relationrules.notallowed.allowed.ASocialNetwork3", "class");
		ModuleDTO modulePresRelationNotAllowedAllowedASN2 = new ModuleDTO("Presentation Layer.Relationrules.Notallowed.Allowed.ASocialNetwork3", new PhysicalPathDTO[] { presRelationNotAllowedAllowedASN2 }, "class", new ModuleDTO[] {});
		PhysicalPathDTO presRelationNotAllowedAllowed = new PhysicalPathDTO("presentation.relationrules.notallowed.allowed", "package");
		ModuleDTO modulePresRelationNotAllowedAllowed = new ModuleDTO("Presentation Layer.Relationrules.Notallowed.Allowed", new PhysicalPathDTO[] { presRelationNotAllowedAllowed }, "package", new ModuleDTO[] { modulePresRelationNotAllowedAllowedASN2 });
		PhysicalPathDTO presRelationNotAllowedASN = new PhysicalPathDTO("presentation.relationrules.notallowed.ASocialNetwork", "class");
		ModuleDTO modulePresRelationNotAllowedASN = new ModuleDTO("Presentation Layer.Relationrules.Notallowed.ASocialNetwork", new PhysicalPathDTO[] { presRelationNotAllowedASN }, "class", new ModuleDTO[] {});
		PhysicalPathDTO presRelationNotAllowed = new PhysicalPathDTO("presentation.relationrules.notallowed", "package");
		ModuleDTO modulePresRelationNotAllowed = new ModuleDTO("Presentation Layer.Relationrules.Notallowed", new PhysicalPathDTO[] { presRelationNotAllowed }, "package", new ModuleDTO[] { modulePresRelationNotAllowedAllowed, modulePresRelationNotAllowedASN});
		
		PhysicalPathDTO presRelation = new PhysicalPathDTO("presentation.relationrules", "package");
		ModuleDTO modulePresRelation = new ModuleDTO("Presentation Layer.Relationrules", new PhysicalPathDTO[] { presRelation }, "package", new ModuleDTO[] { modulePresRelationAllowed, modulePresRelationNotAllowed});
		
		
		//General used modules layer Domain
		PhysicalPathDTO domainRelationAllowedSN = new PhysicalPathDTO("domain.relationrules.allowed.SocialNetwork", "class");
		ModuleDTO moduleDomainRelationAllowedSN = new ModuleDTO("Domain Layer.Relationrules.Allowed.SocialNetwork", new PhysicalPathDTO[] { domainRelationAllowedSN }, "class", new ModuleDTO[] {});
		PhysicalPathDTO domainRelationAllowed = new PhysicalPathDTO("domain.relationrules.Allowed", "package");
		ModuleDTO moduleDomainRelationAllowed = new ModuleDTO("Domain Layer.Relationrules.Allowed", new PhysicalPathDTO[] { domainRelationAllowed }, "package", 
				new ModuleDTO[] { moduleDomainRelationAllowedSN });
		
		PhysicalPathDTO domainRelation = new PhysicalPathDTO("domain.relationrules", "package");
		ModuleDTO moduleDomainRelation = new ModuleDTO("Domain Layer.Relationrules", new PhysicalPathDTO[] { domainRelation }, "package", new ModuleDTO[] { moduleDomainRelationAllowed });
		
		
		//General used modules layer Technology
		PhysicalPathDTO techRelationAllowedNotallowedSN2 = new PhysicalPathDTO("technology.relationrules.allowed.notallowed.SocialNetwork2", "class");
		ModuleDTO moduleTechRelationAllowedNotallowedSN2 = new ModuleDTO("Technology Layer.Relationrules.Allowed.Notallowed.SocialNetwork2", new PhysicalPathDTO[] { techRelationAllowedNotallowedSN2 }, "class", new ModuleDTO[] {});
		PhysicalPathDTO techRelationAllowedNotallowed = new PhysicalPathDTO("technology.relationrules.allowed.notallowed", "package");
		ModuleDTO moduleTechRelationAllowedNotallowed = new ModuleDTO("Technology Layer.Relationrules.Allowed.Notallowed", new PhysicalPathDTO[] { techRelationAllowedNotallowed }, "package", new ModuleDTO[] { moduleTechRelationAllowedNotallowedSN2 });
		PhysicalPathDTO techRelationAllowedSN = new PhysicalPathDTO("technology.relationrules.allowed.SocialNetwork", "class");
		ModuleDTO moduleTechRelationAllowedSN = new ModuleDTO("Technology Layer.Relationrules.Allowed.SocialNetwork", new PhysicalPathDTO[] { techRelationAllowedSN }, "class", new ModuleDTO[] {});
		PhysicalPathDTO techRelationAllowed = new PhysicalPathDTO("technology.relationrules.Allowed", "package");
		ModuleDTO moduleTechRelationAllowed = new ModuleDTO("Technology Layer.Relationrules.Allowed", new PhysicalPathDTO[] { techRelationAllowed }, "package", new ModuleDTO[] { moduleTechRelationAllowedNotallowed, moduleTechRelationAllowedSN});
		
		PhysicalPathDTO techRelationNotAllowedAllowedASN2 = new PhysicalPathDTO("technology.relationrules.notallowed.allowed.ASocialNetwork2", "class");
		ModuleDTO moduleTechRelationNotAllowedAllowedASN2 = new ModuleDTO("Technology Layer.Relationrules.Notallowed.Allowed.ASocialNetwork2", new PhysicalPathDTO[] { techRelationNotAllowedAllowedASN2 }, "class", new ModuleDTO[] {});
		PhysicalPathDTO techRelationNotAllowedAllowed = new PhysicalPathDTO("technology.relationrules.notallowed.allowed", "package");
		ModuleDTO moduleTechRelationNotAllowedAllowed = new ModuleDTO("Technology Layer.Relationrules.Notallowed.Allowed", new PhysicalPathDTO[] { techRelationNotAllowedAllowed }, "package", new ModuleDTO[] { moduleTechRelationNotAllowedAllowedASN2 });
		PhysicalPathDTO techRelationNotAllowedASN = new PhysicalPathDTO("technology.relationrules.notallowed.ASocialNetwork", "class");
		ModuleDTO moduleTechRelationNotAllowedASN = new ModuleDTO("Technology Layer.Relationrules.Notallowed.ASocialNetwork", new PhysicalPathDTO[] { techRelationNotAllowedASN }, "class", new ModuleDTO[] {});
		PhysicalPathDTO techRelationNotAllowed = new PhysicalPathDTO("technology.relationrules.notallowed", "package");
		ModuleDTO moduleTechRelationNotAllowed = new ModuleDTO("Technology Layer.Relationrules.Notallowed", new PhysicalPathDTO[] { techRelationNotAllowed }, "package", new ModuleDTO[] { moduleTechRelationNotAllowedAllowed, moduleTechRelationNotAllowedASN});
		
		PhysicalPathDTO techRelation = new PhysicalPathDTO("technology.relationrules", "package");
		ModuleDTO moduleTechRelation = new ModuleDTO("Technology Layer.Relationrules", new PhysicalPathDTO[] { techRelation }, "package", new ModuleDTO[] { moduleTechRelationAllowed, moduleTechRelationNotAllowed });
		
		PhysicalPathDTO techRelationTheonlyone_allowedASN = new PhysicalPathDTO("technology.relationrules.theonlyone_allowed.ASocialNetwork", "package");
		ModuleDTO moduleTechRelationTheonlyone_allowedASN = new ModuleDTO("Technology Layer.Relationrules.Theonlyone_allowed.ASocialNetwork", new PhysicalPathDTO[] { techRelationTheonlyone_allowedASN }, "package", new ModuleDTO[] { moduleTechRelationNotAllowedAllowedASN2 });
		PhysicalPathDTO techRelationTheonlyone_allowedSN = new PhysicalPathDTO("technology.relationrules.theonlyone_allowed.SocialNetwork", "class");
		ModuleDTO moduleTechRelationTheonlyone_allowedSN = new ModuleDTO("Technology Layer.Relationrules.Theonlyone_allowed.SocialNetwork", new PhysicalPathDTO[] { techRelationTheonlyone_allowedSN }, "class", new ModuleDTO[] {});
		PhysicalPathDTO techRelationTheonlyone_allowed = new PhysicalPathDTO("technology.relationrules.theonlyone_allowed", "package");
		ModuleDTO moduleTechRelationTheonlyone_allowed = new ModuleDTO("Technology Layer.Relationrules.Theonlyone_allowed", new PhysicalPathDTO[] { techRelationTheonlyone_allowed }, "package", new ModuleDTO[] { moduleTechRelationTheonlyone_allowedASN, moduleTechRelationTheonlyone_allowedSN});
		
		
		//RULE 1: BackCallBan - From layer Domain to layer Presentation
		PhysicalPathDTO rule1_physModuleFrom_1 = new PhysicalPathDTO("domain.relationrules.back_call_ban.Access_2_1", "class");
		ModuleDTO rule1_moduleFrom_1 = new ModuleDTO("Domain Layer.Relationrules.back_call_ban.Access_2_1", new PhysicalPathDTO[] { rule1_physModuleFrom_1 }, "class", new ModuleDTO[] {});

		PhysicalPathDTO rule1_physModuleFrom_2 = new PhysicalPathDTO("domain.relationrules.back_call_ban.Call_2_1", "class");
		ModuleDTO rule1_moduleFrom_2 = new ModuleDTO("Domain Layer.Relationrules.back_call_ban.Call_2_1", new PhysicalPathDTO[] { rule1_physModuleFrom_2 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule1_physModuleFrom_3 = new PhysicalPathDTO("domain.relationrules.back_call_ban.Extend_2_1", "class");
		ModuleDTO rule1_moduleFrom_3 = new ModuleDTO("Domain Layer.Relationrules.back_call_ban.Extend_2_1", new PhysicalPathDTO[] { rule1_physModuleFrom_3 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule1_physModuleFrom_4 = new PhysicalPathDTO("domain.relationrules.back_call_ban.ViolatingAccess_2_1", "class");
		ModuleDTO rule1_moduleFrom_4 = new ModuleDTO("Domain Layer.Relationrules.back_call_ban.ViolatingAccess_2_1", new PhysicalPathDTO[] { rule1_physModuleFrom_4 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule1_physModuleFrom_5 = new PhysicalPathDTO("domain.relationrules.back_call_ban.ViolatingCall_2_1", "class");
		ModuleDTO rule1_moduleFrom_5 = new ModuleDTO("Domain Layer.Relationrules.back_call_ban.ViolatingCall_2_1", new PhysicalPathDTO[] { rule1_physModuleFrom_5 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule1_physModuleFrom_6 = new PhysicalPathDTO("domain.relationrules.back_call_ban.ViolatingExtend_2_1", "class");
		ModuleDTO rule1_moduleFrom_6 = new ModuleDTO("Domain Layer.Relationrules.back_call_ban.ViolatingExtend_2_1", new PhysicalPathDTO[] { rule1_physModuleFrom_6 }, "class", new ModuleDTO[] {});

		PhysicalPathDTO rule1_physModuleFrom = new PhysicalPathDTO("domain", "package");
		ModuleDTO rule1_moduleFrom = new ModuleDTO("Domain Layer", new PhysicalPathDTO[] { rule1_physModuleFrom }, "package", 
				new ModuleDTO[] { rule1_moduleFrom_1, rule1_moduleFrom_2, rule1_moduleFrom_3, rule1_moduleFrom_4, rule1_moduleFrom_5, rule1_moduleFrom_6 });
		
		RuleDTO rule1 = new RuleDTO(RuleTypes.IS_NOT_ALLOWED_BACK_CALL.toString(), false, modulePresRelation, rule1_moduleFrom, 
				violationTypesPerRuleType.get(RuleTypes.IS_NOT_ALLOWED_BACK_CALL.toString()), "", new RuleDTO[] {});
		appliedRules[0] = rule1;	
		
		
		//RULE 2: BackCallBan - From layer Technology to Layer Domain
		//Can't be tested due because the required classes are not present in the layer Technology.
//		PhysicalPathDTO rule2_physModuleFrom_1 = new PhysicalPathDTO("technology.relationrules.back_call_ban.Access_2_1", "class");
//		ModuleDTO rule2_moduleFrom_1 = new ModuleDTO("Technology Layer.Relationrules.back_call_ban.Access_2_1", new PhysicalPathDTO[] { rule2_physModuleFrom_1 }, "class", new ModuleDTO[] {});
//
//		PhysicalPathDTO rule2_physModuleFrom_2 = new PhysicalPathDTO("technology.relationrules.back_call_ban.Call_2_1", "class");
//		ModuleDTO rule2_moduleFrom_2 = new ModuleDTO("Technology Layer.Relationrules.back_call_ban.Call_2_1", new PhysicalPathDTO[] { rule2_physModuleFrom_2 }, "class", new ModuleDTO[] {});
//		
//		PhysicalPathDTO rule2_physModuleFrom_3 = new PhysicalPathDTO("technology.relationrules.back_call_ban.Extend_2_1", "class");
//		ModuleDTO rule2_moduleFrom_3 = new ModuleDTO("Technology Layer.Relationrules.back_call_ban.Extend_2_1", new PhysicalPathDTO[] { rule2_physModuleFrom_3 }, "class", new ModuleDTO[] {});
//		
//		PhysicalPathDTO rule2_physModuleFrom_4 = new PhysicalPathDTO("technology.relationrules.back_call_ban.ViolatingAccess_2_1", "class");
//		ModuleDTO rule2_moduleFrom_4 = new ModuleDTO("Technology Layer.Relationrules.back_call_ban.ViolatingAccess_2_1", new PhysicalPathDTO[] { rule2_physModuleFrom_4 }, "class", new ModuleDTO[] {});
//		
//		PhysicalPathDTO rule2_physModuleFrom_5 = new PhysicalPathDTO("technology.relationrules.back_call_ban.ViolatingCall_2_1", "class");
//		ModuleDTO rule2_moduleFrom_5 = new ModuleDTO("Technology Layer.Relationrules.back_call_ban.ViolatingCall_2_1", new PhysicalPathDTO[] { rule2_physModuleFrom_5 }, "class", new ModuleDTO[] {});
//		
//		PhysicalPathDTO rule2_physModuleFrom_6 = new PhysicalPathDTO("technology.relationrules.back_call_ban.ViolatingExtend_2_1", "class");
//		ModuleDTO rule2_moduleFrom_6 = new ModuleDTO("Technology Layer.Relationrules.back_call_ban.ViolatingExtend_2_1", new PhysicalPathDTO[] { rule2_physModuleFrom_6 }, "class", new ModuleDTO[] {});

		PhysicalPathDTO rule2_physModuleFrom = new PhysicalPathDTO("technology", "package");
		ModuleDTO rule2en3_moduleFrom = new ModuleDTO("Technology Layer", new PhysicalPathDTO[] { rule2_physModuleFrom }, "package", 
				new ModuleDTO[] { }); //rule2_moduleFrom_1, rule2_moduleFrom_2, rule2_moduleFrom_3, rule2_moduleFrom_4, rule2_moduleFrom_5, rule2_moduleFrom_6 });
		
		RuleDTO rule2 = new RuleDTO(RuleTypes.IS_NOT_ALLOWED_BACK_CALL.toString(), false, moduleDomainRelation, rule2en3_moduleFrom, 
				violationTypesPerRuleType.get(RuleTypes.IS_NOT_ALLOWED_BACK_CALL.toString()), "", new RuleDTO[] {});
		appliedRules[1] = rule2;	
		
		
		//RULE 3: BackCallBan - From layer Technology to layer Presentation
		//Can't be tested due because the required classes are not present in the layer Technology.
		RuleDTO rule3 = new RuleDTO(RuleTypes.IS_NOT_ALLOWED_BACK_CALL.toString(), false, moduleDomainRelation, rule2en3_moduleFrom, 
				violationTypesPerRuleType.get(RuleTypes.IS_NOT_ALLOWED_BACK_CALL.toString()), "", new RuleDTO[] {});
		appliedRules[2] = rule3;
		
		
		//RULE 4: SkipCallBan - From layer Presentation to layer Technology
		PhysicalPathDTO rule4_physModuleFrom_1 = new PhysicalPathDTO("presentation.relationrules.back_call_ban.Access_2_2", "class");
		ModuleDTO rule4_moduleFrom_1 = new ModuleDTO("Presentation Layer.Relationrules.back_call_ban.Access_2_2", new PhysicalPathDTO[] { rule4_physModuleFrom_1 }, "class", new ModuleDTO[] {});

		PhysicalPathDTO rule4_physModuleFrom_2 = new PhysicalPathDTO("presentation.relationrules.back_call_ban.Call_2_2", "class");
		ModuleDTO rule4_moduleFrom_2 = new ModuleDTO("Presentation Layer.Relationrules.back_call_ban.Call_2_2", new PhysicalPathDTO[] { rule4_physModuleFrom_2 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule4_physModuleFrom_3 = new PhysicalPathDTO("presentation.relationrules.back_call_ban.Extend_2_2", "class");
		ModuleDTO rule4_moduleFrom_3 = new ModuleDTO("Presentation Layer.Relationrules.back_call_ban.Extend_2_2", new PhysicalPathDTO[] { rule4_physModuleFrom_3 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule4_physModuleFrom_4 = new PhysicalPathDTO("presentation.relationrules.back_call_ban.ViolatingAccess_2_2", "class");
		ModuleDTO rule4_moduleFrom_4 = new ModuleDTO("Presentation Layer.Relationrules.back_call_ban.ViolatingAccess_2_2", new PhysicalPathDTO[] { rule4_physModuleFrom_4 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule4_physModuleFrom_5 = new PhysicalPathDTO("presentation.relationrules.back_call_ban.ViolatingCall_2_2", "class");
		ModuleDTO rule4_moduleFrom_5 = new ModuleDTO("Presentation Layer.Relationrules.back_call_ban.ViolatingCall_2_2", new PhysicalPathDTO[] { rule4_physModuleFrom_5 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule4_physModuleFrom_6 = new PhysicalPathDTO("presentation.relationrules.back_call_ban.ViolatingExtend_2_2", "class");
		ModuleDTO rule4_moduleFrom_6 = new ModuleDTO("Presentation Layer.Relationrules.back_call_ban.ViolatingExtend_2_2", new PhysicalPathDTO[] { rule4_physModuleFrom_6 }, "class", new ModuleDTO[] {});

		PhysicalPathDTO rule4_physModuleFrom = new PhysicalPathDTO("presentation", "package");
		ModuleDTO rule4_moduleFrom = new ModuleDTO("Presentation Layer", new PhysicalPathDTO[] { rule4_physModuleFrom }, "package", 
				new ModuleDTO[] { rule4_moduleFrom_1, rule4_moduleFrom_2, rule4_moduleFrom_3, rule4_moduleFrom_4, rule4_moduleFrom_5, rule4_moduleFrom_6 });
		
		RuleDTO rule4 = new RuleDTO(RuleTypes.IS_NOT_ALLOWED_SKIP_CALL.toString(), false, moduleDomainRelation, rule4_moduleFrom, 
				violationTypesPerRuleType.get(RuleTypes.IS_NOT_ALLOWED_SKIP_CALL.toString()), "", new RuleDTO[] {});
		appliedRules[3] = rule4;
		
		
		//RULE 5: NamingConvention (PR1_1) - Naming_prefix
		PhysicalPathDTO rule5_physModuleFrom_1 = new PhysicalPathDTO("technology.propertyrules.naming_postfix.MDB_AbstractDefaultClass", "class");
		ModuleDTO rule5_moduleFrom_1 = new ModuleDTO("Technology Layer.Propertyrules.naming_postfix.MDB_AbstractDefaultClass", new PhysicalPathDTO[] { rule5_physModuleFrom_1 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule5_physModuleFrom_2 = new PhysicalPathDTO("technology.propertyrules.naming_postfix.MDB_AbstractPublicClass", "class");
		ModuleDTO rule5_moduleFrom_2 = new ModuleDTO("Technology Layer.Propertyrules.naming_postfix.MDB_AbstractPublicClass", new PhysicalPathDTO[] { rule5_physModuleFrom_2 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule5_physModuleFrom_3 = new PhysicalPathDTO("technology.propertyrules.naming_postfix.MDB_DefaultClass", "class");
		ModuleDTO rule5_moduleFrom_3 = new ModuleDTO("Technology Layer.Propertyrules.naming_postfix.MDB_DefaultClass", new PhysicalPathDTO[] { rule5_physModuleFrom_3 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule5_physModuleFrom_4 = new PhysicalPathDTO("technology.propertyrules.naming_postfix.MDB_DefaultFinalClass", "class");
		ModuleDTO rule5_moduleFrom_4 = new ModuleDTO("Technology Layer.Propertyrules.naming_postfix.MDB_DefaultFinalClass", new PhysicalPathDTO[] { rule5_physModuleFrom_4 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule5_physModuleFrom_5_1 = new PhysicalPathDTO("technology.propertyrules.naming_postfix.MDB_sub_notok_class_ok.MDB_AbstractDefaultClass3", "class");
		ModuleDTO rule5_moduleFrom_5_1 = new ModuleDTO("Technology Layer.Propertyrules.naming_postfix.MDB_sub_notok_class_ok.MDB_AbstractDefaultClass3", new PhysicalPathDTO[] { rule5_physModuleFrom_5_1 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule5_physModuleFrom_5 = new PhysicalPathDTO("technology.propertyrules.naming_postfix.MDB_sub_notok_class_ok", "package");
		ModuleDTO rule5_moduleFrom_5 = new ModuleDTO("Technology Layer.Propertyrules.naming_postfix.MDB_sub_notok_class_ok", new PhysicalPathDTO[] { rule5_physModuleFrom_5 }, "package", 
				new ModuleDTO[] { rule5_moduleFrom_5_1 });
		
		PhysicalPathDTO rule5_physModuleFrom_6_1 = new PhysicalPathDTO("technology.propertyrules.naming_postfix.MDB_sub_ok_class_notok.MDB_AbstractDefaultClass2", "class");
		ModuleDTO rule5_moduleFrom_6_1 = new ModuleDTO("Technology Layer.Propertyrules.naming_postfix.MDB_sub_ok_class_notok.MDB_AbstractDefaultClass2", new PhysicalPathDTO[] { rule5_physModuleFrom_6_1 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule5_physModuleFrom_6 = new PhysicalPathDTO("technology.propertyrules.naming_postfix.MDB_sub_ok_class_notok", "package");
		ModuleDTO rule5_moduleFrom_6 = new ModuleDTO("Technology Layer.Propertyrules.naming_postfix.MDB_sub_ok_class_notok", new PhysicalPathDTO[] { rule5_physModuleFrom_6 }, "package", 
				new ModuleDTO[] { rule5_moduleFrom_6_1 });
		
		PhysicalPathDTO rule5_physModuleFrom_7_1 = new PhysicalPathDTO("technology.propertyrules.naming_postfix.MDB_sub_ok_class_ok.MDB_AbstractDefaultClass4", "class");
		ModuleDTO rule5_moduleFrom_7_1 = new ModuleDTO("Technology Layer.Propertyrules.naming_postfix.MDB_sub_ok_class_ok.MDB_AbstractDefaultClass4", new PhysicalPathDTO[] { rule5_physModuleFrom_7_1 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule5_physModuleFrom_7 = new PhysicalPathDTO("technology.propertyrules.naming_postfix.sub_ok_class_ok_MDB", "package");
		ModuleDTO rule5_moduleFrom_7 = new ModuleDTO("Technology Layer.Propertyrules.naming_postfix.MDB_sub_ok_class_ok", new PhysicalPathDTO[] { rule5_physModuleFrom_7 }, "package", 
				new ModuleDTO[] { rule5_moduleFrom_7_1 });
		
		PhysicalPathDTO rule5_physModuleFrom = new PhysicalPathDTO("technology.propertyrules.naming_postfix", "package");
		ModuleDTO rule5_moduleFrom = new ModuleDTO("Technology Layer.Propertyrules.Naming_postfix", new PhysicalPathDTO[] { rule5_physModuleFrom }, "package", 
				new ModuleDTO[] { rule5_moduleFrom_1, rule5_moduleFrom_2, rule5_moduleFrom_3, rule5_moduleFrom_4, rule5_moduleFrom_5, rule5_moduleFrom_6, rule5_moduleFrom_7 });		
		 
		RuleDTO rule5 = new RuleDTO(RuleTypes.NAMING_CONVENTION.toString(), false, rule5_moduleFrom, rule5_moduleFrom, 
				violationTypesPerRuleType.get(RuleTypes.NAMING_CONVENTION.toString()), "MDB*", new RuleDTO[] {});
		appliedRules[4] = rule5;
		
		
		//RULE 6: NamingConvention (PR1_2) - Naming_postfix
		PhysicalPathDTO rule6_physModuleFrom_1 = new PhysicalPathDTO("technology.propertyrules.naming_prefix.AbstractDefaultClass_MDB", "class");
		ModuleDTO rule6_moduleFrom_1 = new ModuleDTO("Technology Layer.Propertyrules.naming_prefix.AbstractDefaultClass_MDB", new PhysicalPathDTO[] { rule6_physModuleFrom_1 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule6_physModuleFrom_2 = new PhysicalPathDTO("technology.propertyrules.naming_prefix.AbstractPublicClass_MDB", "class");
		ModuleDTO rule6_moduleFrom_2 = new ModuleDTO("Technology Layer.Propertyrules.naming_prefix.AbstractPublicClass_MDB", new PhysicalPathDTO[] { rule6_physModuleFrom_2 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule6_physModuleFrom_3 = new PhysicalPathDTO("technology.propertyrules.naming_prefix.DefaultClass_MDB", "class");
		ModuleDTO rule6_moduleFrom_3 = new ModuleDTO("Technology Layer.Propertyrules.naming_prefix.DefaultClass_MDB", new PhysicalPathDTO[] { rule6_physModuleFrom_3 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule6_physModuleFrom_4 = new PhysicalPathDTO("technology.propertyrules.naming_prefix.DefaultFinalClass_MDB", "class");
		ModuleDTO rule6_moduleFrom_4 = new ModuleDTO("Technology Layer.Propertyrules.naming_prefix.DefaultFinalClass_MDB", new PhysicalPathDTO[] { rule6_physModuleFrom_4 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule6_physModuleFrom_5_1 = new PhysicalPathDTO("technology.propertyrules.naming_prefix.sub_notok_class_ok.AbstractDefaultClass4_MDB", "class");
		ModuleDTO rule6_moduleFrom_5_1 = new ModuleDTO("Technology Layer.Propertyrules.naming_prefix.Sub_notok_class_ok.AbstractDefaultClass4_MDB", new PhysicalPathDTO[] { rule6_physModuleFrom_5_1 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule6_physModuleFrom_5 = new PhysicalPathDTO("technology.propertyrules.naming_prefix.sub_notok_class_ok", "package");
		ModuleDTO rule6_moduleFrom_5 = new ModuleDTO("Technology Layer.Propertyrules.naming_prefix.Sub_notok_class_ok", new PhysicalPathDTO[] { rule6_physModuleFrom_5 }, "package", 
				new ModuleDTO[] { rule6_moduleFrom_5_1 });
		
		PhysicalPathDTO rule6_physModuleFrom_6_1 = new PhysicalPathDTO("technology.propertyrules.naming_prefix.sub_ok_class_notok_MDB.AbstractDefaultClass4", "class");
		ModuleDTO rule6_moduleFrom_6_1 = new ModuleDTO("Technology Layer.Propertyrules.naming_prefix.Sub_ok_class_notok_MDB.AbstractDefaultClass4", new PhysicalPathDTO[] { rule6_physModuleFrom_6_1 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule6_physModuleFrom_6 = new PhysicalPathDTO("technology.propertyrules.naming_prefix.sub_ok_class_notok_MDB", "package");
		ModuleDTO rule6_moduleFrom_6 = new ModuleDTO("Technology Layer.Propertyrules.naming_prefix.Sub_ok_class_notok_MDB", new PhysicalPathDTO[] { rule6_physModuleFrom_6 }, "package", 
				new ModuleDTO[] { rule6_moduleFrom_6_1 });
		
		PhysicalPathDTO rule6_physModuleFrom_7_1 = new PhysicalPathDTO("technology.propertyrules.naming_prefix.sub_ok_class_ok_MDB.AbstractDefaultClass2_MDB", "class");
		ModuleDTO rule6_moduleFrom_7_1 = new ModuleDTO("Technology Layer.Propertyrules.naming_prefix.Sub_ok_class_ok_MDB.AbstractDefaultClass2_MDB", new PhysicalPathDTO[] { rule6_physModuleFrom_7_1 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule6_physModuleFrom_7 = new PhysicalPathDTO("technology.propertyrules.naming_prefix.sub_ok_class_ok_MDB", "package");
		ModuleDTO rule6_moduleFrom_7 = new ModuleDTO("Technology Layer.Propertyrules.naming_prefix.Sub_ok_class_ok_MDB", new PhysicalPathDTO[] { rule6_physModuleFrom_7 }, "package", 
				new ModuleDTO[] { rule6_moduleFrom_7_1 });
		
		PhysicalPathDTO rule6_physModuleFrom = new PhysicalPathDTO("technology.propertyrules.naming_prefix", "package");
		ModuleDTO rule6_moduleFrom = new ModuleDTO("Technology Layer.Propertyrules.Naming_prefix", new PhysicalPathDTO[] { rule6_physModuleFrom }, "package", 
				new ModuleDTO[] { rule6_moduleFrom_1, rule6_moduleFrom_2, rule6_moduleFrom_3, rule6_moduleFrom_4, rule6_moduleFrom_5, rule6_moduleFrom_6, rule6_moduleFrom_7 });
		 
		RuleDTO rule6 = new RuleDTO(RuleTypes.NAMING_CONVENTION.toString(), false, rule6_moduleFrom, rule6_moduleFrom, 
				violationTypesPerRuleType.get(RuleTypes.NAMING_CONVENTION.toString()), "*MDB", new RuleDTO[] {});
		appliedRules[5] = rule6;
		
		
		//RULE 7: NamingConvention (PR1_3) - Naming_mid
		PhysicalPathDTO rule7_physModuleFrom_1 = new PhysicalPathDTO("technology.propertyrules.naming_mid.AbstractDefaultMDBClass", "class");
		ModuleDTO rule7_moduleFrom_1 = new ModuleDTO("Technology Layer.Propertyrules.naming_mid.AbstractDefaultMDBClass", new PhysicalPathDTO[] { rule7_physModuleFrom_1 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule7_physModuleFrom_2 = new PhysicalPathDTO("technology.propertyrules.naming_mid.AbstractPublicMDBClass", "class");
		ModuleDTO rule7_moduleFrom_2 = new ModuleDTO("Technology Layer.Propertyrules.naming_mid.AbstractPublicMDBClass", new PhysicalPathDTO[] { rule7_physModuleFrom_2 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule7_physModuleFrom_3 = new PhysicalPathDTO("technology.propertyrules.naming_mid.DefaultFinalMDBClass", "class");
		ModuleDTO rule7_moduleFrom_3 = new ModuleDTO("Technology Layer.Propertyrules.naming_mid.DefaultFinalMDBClass", new PhysicalPathDTO[] { rule7_physModuleFrom_3 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule7_physModuleFrom_4 = new PhysicalPathDTO("technology.propertyrules.naming_mid.DefaultMDBClass", "class");
		ModuleDTO rule7_moduleFrom_4 = new ModuleDTO("Technology Layer.Propertyrules.naming_mid.DefaultMDBClass", new PhysicalPathDTO[] { rule7_physModuleFrom_4 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule7_physModuleFrom_5_1 = new PhysicalPathDTO("technology.propertyrules.naming_mid.sub_notok_class_ok.AbstractDefaultMDBClass4", "class");
		ModuleDTO rule7_moduleFrom_5_1 = new ModuleDTO("Technology Layer.Propertyrules.naming_mid.Sub_notok_class_ok.AbstractDefaultMDBClass4", new PhysicalPathDTO[] { rule7_physModuleFrom_5_1 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule7_physModuleFrom_5 = new PhysicalPathDTO("technology.propertyrules.naming_mid.sub_notok_class_ok", "package");
		ModuleDTO rule7_moduleFrom_5 = new ModuleDTO("Technology Layer.Propertyrules.naming_mid.Sub_notok_class_ok", new PhysicalPathDTO[] { rule7_physModuleFrom_5 }, "package", 
				new ModuleDTO[] { rule7_moduleFrom_5_1 });
		
		PhysicalPathDTO rule7_physModuleFrom_6_1 = new PhysicalPathDTO("technology.propertyrules.naming_mid.sub_ok_MDB_class_notok.AbstractDefaultClass5", "class");
		ModuleDTO rule7_moduleFrom_6_1 = new ModuleDTO("Technology Layer.Propertyrules.naming_mid.Sub_ok_MDB_class_notok.AbstractDefaultClass5", new PhysicalPathDTO[] { rule7_physModuleFrom_6_1 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule7_physModuleFrom_6 = new PhysicalPathDTO("technology.propertyrules.naming_mid.sub_ok_MDB_class_notok", "package");
		ModuleDTO rule7_moduleFrom_6 = new ModuleDTO("Technology Layer.Propertyrules.naming_mid.Sub_ok_MDB_class_notok", new PhysicalPathDTO[] { rule7_physModuleFrom_6 }, "package", 
				new ModuleDTO[] { rule7_moduleFrom_6_1 });
		
		PhysicalPathDTO rule7_physModuleFrom_7_1 = new PhysicalPathDTO("technology.propertyrules.naming_mid.sub_ok_MDB_class_ok.AbstractDefaultMDBClass2", "class");
		ModuleDTO rule7_moduleFrom_7_1 = new ModuleDTO("Technology Layer.Propertyrules.naming_mid.Sub_ok_MDB_class_ok.AbstractDefaultMDBClass2", new PhysicalPathDTO[] { rule7_physModuleFrom_7_1 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule7_physModuleFrom_7 = new PhysicalPathDTO("technology.propertyrules.naming_mid.sub_ok_MDB_class_ok", "package");
		ModuleDTO rule7_moduleFrom_7 = new ModuleDTO("Technology Layer.Propertyrules.naming_mid.Sub_ok_MDB_class_ok", new PhysicalPathDTO[] { rule7_physModuleFrom_7 }, "package", 
				new ModuleDTO[] { rule7_moduleFrom_7_1 });
		
		PhysicalPathDTO rule7_physModuleFrom = new PhysicalPathDTO("technology.propertyrules.naming_mid", "package");
		ModuleDTO rule7_moduleFrom = new ModuleDTO("Technology Layer.Propertyrules.Naming_mid", new PhysicalPathDTO[] { rule7_physModuleFrom }, "package", 
				new ModuleDTO[] { rule7_moduleFrom_1, rule7_moduleFrom_2, rule7_moduleFrom_3, rule7_moduleFrom_4, rule7_moduleFrom_5, rule7_moduleFrom_6, rule7_moduleFrom_7 });
		 
		RuleDTO rule7 = new RuleDTO(RuleTypes.NAMING_CONVENTION.toString(), false, rule7_moduleFrom, rule7_moduleFrom, 
				violationTypesPerRuleType.get(RuleTypes.NAMING_CONVENTION.toString()), "*MDB*", new RuleDTO[] {});
		appliedRules[6] = rule7;
		
		
		//RULE 8: VisibilityConvention (PR3_1)
		//PhysicalPathDTO rule9_physModuleFrom_1 = new PhysicalPathDTO("", "interface");
		PhysicalPathDTO rule8en9_physModuleFrom_1_1 = new PhysicalPathDTO("technology.propertyrules.component.ComponentDTO", "class");
		ModuleDTO rule8en9_moduleFrom_1_1 = new ModuleDTO("Technology Layer.Propertyrules.Component.ComponentFacade.ComponentDTO", 
				new PhysicalPathDTO[] { rule8en9_physModuleFrom_1_1 }, "class", new ModuleDTO[] {});

		PhysicalPathDTO rule8en9_physModuleFrom_1_2 = new PhysicalPathDTO("technology.propertyrules.component.ComponentInterface", "interface");
		ModuleDTO rule8en9_moduleFrom_1_2 = new ModuleDTO("Technology Layer.Propertyrules.Component.ComponentFacade.ComponentInterface", 
				new PhysicalPathDTO[] { rule8en9_physModuleFrom_1_2 }, "interface", new ModuleDTO[] {});
		
		ModuleDTO rule8en9_moduleFrom_1 = new ModuleDTO("Technology Layer.Propertyrules.Component.ComponentFacade", new PhysicalPathDTO[] { }, "facade", 
				new ModuleDTO[] { rule8en9_moduleFrom_1_1, rule8en9_moduleFrom_1_2 });
		
		PhysicalPathDTO rule8en9_physModuleFrom_3 = new PhysicalPathDTO("technology.propertyrules.component.IllegalVisibility1", "class");
		ModuleDTO rule8en9_moduleFrom_3 = new ModuleDTO("Technology Layer.Propertyrules.Component.IllegalVisibility1", new PhysicalPathDTO[] { rule8en9_physModuleFrom_3 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule8en9_physModuleFrom_4 = new PhysicalPathDTO("technology.propertyrules.component.LocationTrends", "class");
		ModuleDTO rule8en9_moduleFrom_4 = new ModuleDTO("Technology Layer.Propertyrules.Component.LocationTrends", new PhysicalPathDTO[] { rule8en9_physModuleFrom_4 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule8en9_physModuleFrom_5 = new PhysicalPathDTO("technology.propertyrules.component.SearchPlaces", "class");
		ModuleDTO rule8en9_moduleFrom_5 = new ModuleDTO("Technology Layer.Propertyrules.Component.SearchPlaces", new PhysicalPathDTO[] { rule8en9_physModuleFrom_5 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule8en9_physModuleFrom_6 = new PhysicalPathDTO("technology.propertyrules.component.SearchTweets", "class");
		ModuleDTO rule8en9_moduleFrom_6 = new ModuleDTO("Technology Layer.Propertyrules.Component.SearchTweets", new PhysicalPathDTO[] { rule8en9_physModuleFrom_6 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule8en9_physModuleFrom_7_1 = new PhysicalPathDTO("technology.propertyrules.component.sub.IllegalVisibility2", "class");
		ModuleDTO rule8en9_moduleFrom_7_1 = new ModuleDTO("Technology Layer.Propertyrules.Component.Sub.IllegalVisibility2", new PhysicalPathDTO[] { rule8en9_physModuleFrom_7_1 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule8en9_physModuleFrom_7_2 = new PhysicalPathDTO("technology.propertyrules.component.sub.LocationTrends2", "class");
		ModuleDTO rule8en9_moduleFrom_7_2 = new ModuleDTO("Technology Layer.Propertyrules.Component.LocationTrends2", new PhysicalPathDTO[] { rule8en9_physModuleFrom_7_2 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule8en9_physModuleFrom_7 = new PhysicalPathDTO("technology.propertyrules.component.sub", "class");
		ModuleDTO rule8en9_moduleFrom_7 = new ModuleDTO("Technology Layer.Propertyrules.Component.SearchTweets", new PhysicalPathDTO[] { rule8en9_physModuleFrom_7 }, "class", 
				new ModuleDTO[] { rule8en9_moduleFrom_7_1, rule8en9_moduleFrom_7_2 });
		
		PhysicalPathDTO rule8en9_physModuleFrom = new PhysicalPathDTO("technology.propertyrules.component", "package");
		ModuleDTO rule8en9_moduleFrom = new ModuleDTO("Technology Layer.Propertyrules.Component", new PhysicalPathDTO[] { rule8en9_physModuleFrom }, "package", 
				new ModuleDTO[] { rule8en9_moduleFrom_1, rule8en9_moduleFrom_3, rule8en9_moduleFrom_4, rule8en9_moduleFrom_5, rule8en9_moduleFrom_6, rule8en9_moduleFrom_7 });
		
		RuleDTO rule8 = new RuleDTO(RuleTypes.VISIBILITY_CONVENTION.toString(), false, rule8en9_moduleFrom, rule8en9_moduleFrom, 
				violationTypesPerRuleType.get(RuleTypes.VISIBILITY_CONVENTION.toString()), "", new RuleDTO[] {});
		appliedRules[7] = rule8;
		//Geef visibility package, default, public en protected mee.
		
		//RULE 9: FaçadeConvention (PR4_1)
		RuleDTO rule9 = new RuleDTO(RuleTypes.FACADE_CONVENTION.toString(), false, moduleTechRelationAllowed, rule8en9_moduleFrom, 
				violationTypesPerRuleType.get(RuleTypes.FACADE_CONVENTION.toString()), "", new RuleDTO[] {});
		appliedRules[8] = rule9;
		
		
		//RULE 10: SuperClassInheritanceConvention (PR5_1)
		PhysicalPathDTO rule10_physModuleTo = new PhysicalPathDTO("technology.propertyrules.Datasource", "class");
		ModuleDTO rule10_moduleTo = new ModuleDTO("Technology Layer.Propertyrules.Datasource", new PhysicalPathDTO[] { rule10_physModuleTo }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule10_physModuleFrom_1 = new PhysicalPathDTO("technology.propertyrules.superclass_inheritance.DirectInheritanceSuper", "class");
		ModuleDTO rule10_moduleFrom_1 = new ModuleDTO("Technology Layer.Propertyrules.Superclass_inheritance.DirectInheritanceSuper", new PhysicalPathDTO[] { rule10_physModuleFrom_1 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule10_physModuleFrom_2 = new PhysicalPathDTO("technology.propertyrules.superclass_inheritance.IndirectInheritanceSuper", "class");
		ModuleDTO rule10_moduleFrom_2 = new ModuleDTO("Technology Layer.Propertyrules.Superclass_inheritance.IndirectInheritanceSuper", new PhysicalPathDTO[] { rule10_physModuleFrom_2 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule10_physModuleFrom_3 = new PhysicalPathDTO("technology.propertyrules.superclass_inheritance.NoInheritanceSuper", "class");
		ModuleDTO rule10_moduleFrom_3 = new ModuleDTO("Technology Layer.Propertyrules.Superclass_inheritance.NoInheritanceSuper", new PhysicalPathDTO[] { rule10_physModuleFrom_3 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule10_physModuleFrom = new PhysicalPathDTO("technology.propertyrules.superclass_inheritance", "package");
		ModuleDTO rule10_moduleFrom = new ModuleDTO("Technology Layer.Propertyrules.Superclass_inheritance", new PhysicalPathDTO[] { rule10_physModuleFrom }, "package", 
				new ModuleDTO[] { rule10_moduleFrom_1, rule10_moduleFrom_2, rule10_moduleFrom_3 });
		
		RuleDTO rule10 = new RuleDTO(RuleTypes.SUPERCLASSINHERITANCE_CONVENTION.toString(), false, rule10_moduleTo, rule10_moduleFrom, 
				violationTypesPerRuleType.get(RuleTypes.SUPERCLASSINHERITANCE_CONVENTION.toString()), "", new RuleDTO[] {});
		appliedRules[9] = rule10;
		
		//RULE 11: InterfaceInheritanceConvention (PR6_1)
		PhysicalPathDTO rule11_physModuleTo = new PhysicalPathDTO("technology.propertyrules.IDatasource", "class");
		ModuleDTO rule11_moduleTo = new ModuleDTO("Technology Layer.Propertyrules.IDatasource", new PhysicalPathDTO[] { rule11_physModuleTo }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule11_physModuleFrom_1 = new PhysicalPathDTO("technology.propertyrules.interface_inheritance.DirectInheritanceInterface", "interface");
		ModuleDTO rule11_moduleFrom_1 = new ModuleDTO("Technology Layer.Propertyrules.Interface_inheritance.DirectInheritanceInterface", new PhysicalPathDTO[] { rule11_physModuleFrom_1 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule11_physModuleFrom_2 = new PhysicalPathDTO("technology.propertyrules.interface_inheritance.IndirectInheritanceInterface", "interface");
		ModuleDTO rule11_moduleFrom_2 = new ModuleDTO("Technology Layer.Propertyrules.Interface_inheritance.IndirectInheritanceInterface", new PhysicalPathDTO[] { rule11_physModuleFrom_2 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule11_physModuleFrom_3 = new PhysicalPathDTO("technology.propertyrules.interface_inheritance.NoInheritanceInterface", "interface");
		ModuleDTO rule11_moduleFrom_3 = new ModuleDTO("Technology Layer.Propertyrules.Interface_inheritance.NoInheritanceInterface", new PhysicalPathDTO[] { rule11_physModuleFrom_3 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule11_physModuleFrom = new PhysicalPathDTO("technology.propertyrules.interface_inheritance", "package");
		ModuleDTO rule11_moduleFrom = new ModuleDTO("Technology Layer.Propertyrules.Interface_inheritance", new PhysicalPathDTO[] { rule11_physModuleFrom }, "package", 
				new ModuleDTO[] { rule11_moduleFrom_1, rule11_moduleFrom_2, rule11_moduleFrom_3 });
		
		RuleDTO rule11 = new RuleDTO(RuleTypes.INTERFACEINHERITANCE_CONVENTION.toString(), false, rule11_moduleTo, rule11_moduleFrom, 
				violationTypesPerRuleType.get(RuleTypes.INTERFACEINHERITANCE_CONVENTION.toString()), "", new RuleDTO[] {});
		appliedRules[10] = rule11;
		
		
		//RULE 12: IsAllowedToUse (RR1_1)
		PhysicalPathDTO rule12_physModuleFrom_1 = new PhysicalPathDTO("domain.relationrules.is_allowed_to_use.Access_1", "class");
		ModuleDTO rule12_moduleFrom_1 = new ModuleDTO("Domain Layer.Relationrules.Is_allowed_to_use.Access_1", new PhysicalPathDTO[] { rule12_physModuleFrom_1 }, "class", new ModuleDTO[] {});

		PhysicalPathDTO rule12_physModuleFrom_2 = new PhysicalPathDTO("domain.relationrules.is_allowed_to_use.Call_1", "class");
		ModuleDTO rule12_moduleFrom_2 = new ModuleDTO("Domain Layer.Relationrules.Is_allowed_to_use.Call_1", new PhysicalPathDTO[] { rule12_physModuleFrom_2 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule12_physModuleFrom_3 = new PhysicalPathDTO("domain.relationrules.is_allowed_to_use.Extend_1_1", "class");
		ModuleDTO rule12_moduleFrom_3 = new ModuleDTO("Domain Layer.Relationrules.Is_allowed_to_use.Extend_1", new PhysicalPathDTO[] { rule12_physModuleFrom_3 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule12_physModuleFrom_4 = new PhysicalPathDTO("domain.relationrules.is_allowed_to_use.ViolatingAccess_1", "class");
		ModuleDTO rule12_moduleFrom_4 = new ModuleDTO("Domain Layer.Relationrules.Is_allowed_to_use.ViolatingAccess_1", new PhysicalPathDTO[] { rule12_physModuleFrom_4 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule12_physModuleFrom_5 = new PhysicalPathDTO("domain.relationrules.is_allowed_to_use.ViolatingCall_1", "class");
		ModuleDTO rule12_moduleFrom_5 = new ModuleDTO("Domain Layer.Relationrules.Is_allowed_to_use.ViolatingCall_1", new PhysicalPathDTO[] { rule12_physModuleFrom_5 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule12_physModuleFrom_6 = new PhysicalPathDTO("domain.relationrules.is_allowed_to_use.ViolatingExtend_1", "class");
		ModuleDTO rule12_moduleFrom_6 = new ModuleDTO("Domain Layer.Relationrules.Is_allowed_to_use.ViolatingExtend_1", new PhysicalPathDTO[] { rule12_physModuleFrom_6 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule12_physModuleFrom = new PhysicalPathDTO("domain.relationrules.is_allowed_to_use", "package");
		ModuleDTO rule12_moduleFrom = new ModuleDTO("Domain Layer.Relationrules.Is_allowed_to_use", new PhysicalPathDTO[] { rule12_physModuleFrom }, "package", 
				new ModuleDTO[] { rule12_moduleFrom_1, rule12_moduleFrom_2, rule12_moduleFrom_3, rule12_moduleFrom_4, rule12_moduleFrom_5, rule12_moduleFrom_6 });
		
		RuleDTO rule12 = new RuleDTO(RuleTypes.IS_ALLOWED_TO_USE.toString(), false, moduleTechRelationAllowed, rule12_moduleFrom, 
				violationTypesPerRuleType.get(RuleTypes.IS_ALLOWED_TO_USE.toString()), "", new RuleDTO[] {});
		appliedRules[11] = rule12;
		
		
		//RULE 13: IsOnlyAllowedToUse (RR1.1_1)
		PhysicalPathDTO rule13_physModuleFrom_1 = new PhysicalPathDTO("domain.relationrules.is_only_allowed_to_use.Access_1_1", "class");
		ModuleDTO rule13_moduleFrom_1 = new ModuleDTO("Domain Layer.Relationrules.Is_only_allowed_to_use.Access_1_1", new PhysicalPathDTO[] { rule13_physModuleFrom_1 }, "class", new ModuleDTO[] {});

		PhysicalPathDTO rule13_physModuleFrom_2 = new PhysicalPathDTO("domain.relationrules.is_only_allowed_to_use.Call_1_1", "class");
		ModuleDTO rule13_moduleFrom_2 = new ModuleDTO("Domain Layer.Relationrules.Is_only_allowed_to_use.Call_1_1", new PhysicalPathDTO[] { rule13_physModuleFrom_2 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule13_physModuleFrom_3 = new PhysicalPathDTO("domain.relationrules.is_only_allowed_to_use.Extend_1_1", "class");
		ModuleDTO rule13_moduleFrom_3 = new ModuleDTO("Domain Layer.Relationrules.Is_only_allowed_to_use.Extend_1_1", new PhysicalPathDTO[] { rule13_physModuleFrom_3 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule13_physModuleFrom_4 = new PhysicalPathDTO("domain.relationrules.is_only_allowed_to_use.ViolatingAccess_1_1", "class");
		ModuleDTO rule13_moduleFrom_4 = new ModuleDTO("Domain Layer.Relationrules.Is_only_allowed_to_use.ViolatingAccess_1_1", new PhysicalPathDTO[] { rule13_physModuleFrom_4 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule13_physModuleFrom_5 = new PhysicalPathDTO("domain.relationrules.is_only_allowed_to_use.ViolatingCall_1_1", "class");
		ModuleDTO rule13_moduleFrom_5 = new ModuleDTO("Domain Layer.Relationrules.Is_only_allowed_to_use.ViolatingCall_1_1", new PhysicalPathDTO[] { rule13_physModuleFrom_5 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule13_physModuleFrom_6 = new PhysicalPathDTO("domain.relationrules.is_only_allowed_to_use.ViolatingExtend_1_1", "class");
		ModuleDTO rule13_moduleFrom_6 = new ModuleDTO("Domain Layer.Relationrules.Is_only_allowed_to_use.ViolatingExtend_1_1", new PhysicalPathDTO[] { rule13_physModuleFrom_6 }, "class", new ModuleDTO[] {});

		PhysicalPathDTO rule13_physModuleFrom = new PhysicalPathDTO("domain.relationrules.is_only_allowed_to_use", "package");
		ModuleDTO rule13_moduleFrom = new ModuleDTO("Domain Layer.Relationrules.Is_only_allowed_to_use", new PhysicalPathDTO[] { rule13_physModuleFrom }, "package", 
				new ModuleDTO[] { rule13_moduleFrom_1, rule13_moduleFrom_2, rule13_moduleFrom_3, rule13_moduleFrom_4, rule13_moduleFrom_5, rule13_moduleFrom_6 });
		
		RuleDTO rule13 = new RuleDTO(RuleTypes.IS_ONLY_ALLOWED_TO_USE.toString(), false, moduleTechRelationAllowed, rule13_moduleFrom, 
				violationTypesPerRuleType.get(RuleTypes.IS_ONLY_ALLOWED_TO_USE.toString()), "", new RuleDTO[] {});
		appliedRules[12] = rule13;		
		
		
		//RULE 14: MustUse (RR1.3_1) - MustUseCorrect
		PhysicalPathDTO rule14_physModuleFrom_1 = new PhysicalPathDTO("domain.relationrules.must_use_correct.Access_1_3", "class");
		ModuleDTO rule14_moduleFrom_1 = new ModuleDTO("Domain Layer.Relationrules.Must_use_correct.Access_1_3", new PhysicalPathDTO[] { rule14_physModuleFrom_1 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule14_physModuleFrom = new PhysicalPathDTO("domain.relationrules.must_use_correct", "package");
		ModuleDTO rule14_moduleFrom = new ModuleDTO("Domain Layer.Relationrules.Must_use_correct", new PhysicalPathDTO[] { rule14_physModuleFrom }, "package", new ModuleDTO[] { rule14_moduleFrom_1 });
		
		RuleDTO rule14 = new RuleDTO(RuleTypes.MUST_USE.toString(), false, moduleTechRelationAllowed, rule14_moduleFrom, 
				violationTypesPerRuleType.get(RuleTypes.MUST_USE.toString()), "", new RuleDTO[] {});
		appliedRules[13] = rule14;
		
		
		//RULE 15: MustUse (RR1.3_2) - MustUseViolating
		PhysicalPathDTO rule15_physModuleFrom_1 = new PhysicalPathDTO("domain.relationrules.must_use_violating.ViolatingAccess_1_3", "class");
		ModuleDTO rule15_moduleFrom_1 = new ModuleDTO("Domain Layer.Relationrules.Must_use_correct.ViolatingAccess_1_3", new PhysicalPathDTO[] { rule15_physModuleFrom_1 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule15_physModuleFrom = new PhysicalPathDTO("domain.relationrules.must_use_violating", "package");
		ModuleDTO rule15_moduleFrom = new ModuleDTO("Domain Layer.Relationrules.Must_use_violating", new PhysicalPathDTO[] { rule15_physModuleFrom }, "package", new ModuleDTO[] { rule15_moduleFrom_1 });
		
		RuleDTO rule15 = new RuleDTO(RuleTypes.MUST_USE.toString(), false, moduleTechRelationAllowed, rule15_moduleFrom, 
				violationTypesPerRuleType.get(RuleTypes.MUST_USE.toString()), "", new RuleDTO[] {});
		appliedRules[14] = rule15;
		
		
		//RULE 16: IsTheOnlyModuleAllowedToUse (RR1.2_1)
		PhysicalPathDTO rule16_physModuleFrom_1 = new PhysicalPathDTO("domain.relationrules.is_the_only_module_allowed_to_use_1.Access_1_2", "class");
		ModuleDTO rule16_moduleFrom_1 = new ModuleDTO("Domain Layer.Relationrules.Is_the_only_module_allowed_to_use_1.Access_1_2", new PhysicalPathDTO[] { rule16_physModuleFrom_1 }, "class", new ModuleDTO[] {});

		PhysicalPathDTO rule16_physModuleFrom_2 = new PhysicalPathDTO("domain.relationrules.is_the_only_module_allowed_to_use_1.Call_1_2", "class");
		ModuleDTO rule16_moduleFrom_2 = new ModuleDTO("Domain Layer.Relationrules.Is_the_only_module_allowed_to_use_1.Call_1_2", new PhysicalPathDTO[] { rule16_physModuleFrom_2 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule16_physModuleFrom_3 = new PhysicalPathDTO("domain.relationrules.is_the_only_module_allowed_to_use_1.Extend_1_2", "class");
		ModuleDTO rule16_moduleFrom_3 = new ModuleDTO("Domain Layer.Relationrules.Is_the_only_module_allowed_to_use_1.Extend_1_2", new PhysicalPathDTO[] { rule16_physModuleFrom_3 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule16_physModuleFrom_4 = new PhysicalPathDTO("domain.relationrules.is_the_only_module_allowed_to_use_1.ViolatingAccess_1_2", "class");
		ModuleDTO rule16_moduleFrom_4 = new ModuleDTO("Domain Layer.Relationrules.Is_the_only_module_allowed_to_use_1.ViolatingAccess_1_2", new PhysicalPathDTO[] { rule16_physModuleFrom_4 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule16_physModuleFrom_5 = new PhysicalPathDTO("domain.relationrules.is_the_only_module_allowed_to_use_1.ViolatingCall_1_2", "class");
		ModuleDTO rule16_moduleFrom_5 = new ModuleDTO("Domain Layer.Relationrules.Is_the_only_module_allowed_to_use_1.ViolatingCall_1_2", new PhysicalPathDTO[] { rule16_physModuleFrom_5 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule16_physModuleFrom_6 = new PhysicalPathDTO("domain.relationrules.is_the_only_module_allowed_to_use_1.ViolatingExtend_1_2", "class");
		ModuleDTO rule16_moduleFrom_6 = new ModuleDTO("Domain Layer.Relationrules.Is_the_only_module_allowed_to_use_1.ViolatingExtend_1_2", new PhysicalPathDTO[] { rule16_physModuleFrom_6 }, "class", new ModuleDTO[] {});

		PhysicalPathDTO rule16_physModuleFrom = new PhysicalPathDTO("domain.relationrules.is_the_only_module_allowed_to_use_1", "package");
		ModuleDTO rule16_moduleFrom = new ModuleDTO("Domain Layer.Relationrules.Is_the_only_module_allowed_to_use_1", new PhysicalPathDTO[] { rule16_physModuleFrom }, "package", 
				new ModuleDTO[] { rule16_moduleFrom_1, rule16_moduleFrom_2, rule16_moduleFrom_3, rule16_moduleFrom_4, rule16_moduleFrom_5, rule16_moduleFrom_6 });
		
		RuleDTO rule16 = new RuleDTO(RuleTypes.IS_THE_ONLY_MODULE_ALLOWED_TO_USE.toString(), false, moduleTechRelationTheonlyone_allowed, rule16_moduleFrom, 
				violationTypesPerRuleType.get(RuleTypes.IS_THE_ONLY_MODULE_ALLOWED_TO_USE.toString()), "", new RuleDTO[] {});
		appliedRules[15] = rule16;
		
		//RULE 17: IsNotAllowedToUse (RR2_1)
		PhysicalPathDTO rule17_physModuleFrom_1 = new PhysicalPathDTO("domain.relationrules.is_not_allowed_to_use.Access_2", "class");
		ModuleDTO rule17_moduleFrom_1 = new ModuleDTO("Domain Layer.Relationrules.Is_not_allowed_to_use.Access_2", new PhysicalPathDTO[] { rule17_physModuleFrom_1 }, "class", new ModuleDTO[] {});

		PhysicalPathDTO rule17_physModuleFrom_2 = new PhysicalPathDTO("domain.relationrules.is_not_allowed_to_use.Call_2", "class");
		ModuleDTO rule17_moduleFrom_2 = new ModuleDTO("Domain Layer.Relationrules.Is_not_allowed_to_use.Call_2", new PhysicalPathDTO[] { rule17_physModuleFrom_2 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule17_physModuleFrom_3 = new PhysicalPathDTO("domain.relationrules.is_not_allowed_to_use.Extend_2", "class");
		ModuleDTO rule17_moduleFrom_3 = new ModuleDTO("Domain Layer.Relationrules.Is_not_allowed_to_use.Extend_2", new PhysicalPathDTO[] { rule17_physModuleFrom_3 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule17_physModuleFrom_4_1 = new PhysicalPathDTO("domain.relationrules.is_not_allowed_to_use.violating.ViolatingAccess_2", "class");
		ModuleDTO rule17_moduleFrom_4_1 = new ModuleDTO("Domain Layer.Relationrules.Is_not_allowed_to_use.Violating.ViolatingAccess_2", new PhysicalPathDTO[] { rule17_physModuleFrom_4_1 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule17_physModuleFrom_4_2 = new PhysicalPathDTO("domain.relationrules.is_not_allowed_to_use.violating.ViolatingCallInstanceLibraryClass_2", "class");
		ModuleDTO rule17_moduleFrom_4_2 = new ModuleDTO("Domain Layer.Relationrules.Is_not_allowed_to_use.Violating.ViolatingCallInstanceLibraryClass_2", new PhysicalPathDTO[] { rule17_physModuleFrom_4_2 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule17_physModuleFrom_4_3 = new PhysicalPathDTO("domain.relationrules.is_not_allowed_to_use.violating.ViolatingCall_2", "class");
		ModuleDTO rule17_moduleFrom_4_3 = new ModuleDTO("Domain Layer.Relationrules.Is_not_allowed_to_use.Violating.ViolatingCall_2", new PhysicalPathDTO[] { rule17_physModuleFrom_4_3 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule17_physModuleFrom_4_4 = new PhysicalPathDTO("domain.relationrules.is_not_allowed_to_use.violating.ViolatingExtend_2", "class");
		ModuleDTO rule17_moduleFrom_4_4 = new ModuleDTO("Domain Layer.Relationrules.Is_not_allowed_to_use.Violating.ViolatingExtend_2", new PhysicalPathDTO[] { rule17_physModuleFrom_4_4 }, "class", new ModuleDTO[] {});
		
		PhysicalPathDTO rule17_physModuleFrom_4 = new PhysicalPathDTO("domain.relationrules.is_not_allowed_to_use.violating", "package");
		ModuleDTO rule17_moduleFrom_4 = new ModuleDTO("Domain Layer.Relationrules.Is_not_allowed_to_use.Extend_1", new PhysicalPathDTO[] { rule17_physModuleFrom_4 }, "package", 
				new ModuleDTO[] { rule17_moduleFrom_4_1, rule17_moduleFrom_4_2, rule17_moduleFrom_4_3, rule17_moduleFrom_4_4 });
		
		PhysicalPathDTO rule17en18_physModuleFrom = new PhysicalPathDTO("domain.relationrules.is_not_allowed_to_use", "package");
		ModuleDTO rule17en18_moduleFrom = new ModuleDTO("Domain Layer.Relationrules.Is_not_allowed_to_use", new PhysicalPathDTO[] { rule17en18_physModuleFrom }, "package", 
				new ModuleDTO[] { rule17_moduleFrom_1, rule17_moduleFrom_2, rule17_moduleFrom_3, rule17_moduleFrom_4 });
		
		RuleDTO rule17 = new RuleDTO(RuleTypes.IS_NOT_ALLOWED_TO_USE.toString(), false, moduleTechRelationNotAllowed, rule17en18_moduleFrom, 
				violationTypesPerRuleType.get(RuleTypes.IS_NOT_ALLOWED_TO_USE.toString()), "", new RuleDTO[] {});
		appliedRules[16] = rule17;
		
		
		//RULE 18: IsNotAllowedToUse (RR2_2) to ExternalSystem FoursquareApi
		//PhysicalPathDTO rule18_physModuleTo = new PhysicalPathDTO("", "externalsystem");
		ModuleDTO rule18_moduleTo = new ModuleDTO("FoursquareApi", new PhysicalPathDTO[] { }, "externalsystem", new ModuleDTO[] {});
		
		RuleDTO rule18 = new RuleDTO(RuleTypes.IS_NOT_ALLOWED_TO_USE.toString(), false, rule18_moduleTo, rule17en18_moduleFrom, 
				violationTypesPerRuleType.get(RuleTypes.IS_NOT_ALLOWED_TO_USE.toString()), "", new RuleDTO[] {});
		appliedRules[17] = rule18;
	}
	
	public void validateDefinedArchitecture() {
		controlService.getState().add(States.VALIDATING);
		
		final ConfigurationServiceImpl configuration = new ConfigurationServiceImpl();
		DomainServiceImpl domainVService = new DomainServiceImpl(configuration);
		logger.info("Before check conformance created rules - count created ruleTypes:" + appliedRules.length);
		domainVService.checkConformance(appliedRules);

		allViolations = (ArrayList<Violation>) configuration.getAllViolations().getValue();
		logger.info("Founded / reported violations: " + allViolations.size());
	}
	
	public void printFileSystemContents(String filePath) {
		File rootPath = new File(filePath);
		File[] list = rootPath.listFiles();

		if (list == null) {
			return;
		}

		for (File file : list) {
			if (file.isDirectory()) {
				System.out.println("> Dir:  " + file.getAbsoluteFile());
				printFileSystemContents(file.getAbsolutePath());
			}
			else {
				System.out.println("> File: " + file.getAbsoluteFile());
			}
		}
	}
	
	
	//GETTERS AND SETTERS
	public static String getProjectSourcePath() {
		return projectSourcePath;
	}
	
	public static String getProgramVersion() {
		return programVersion;
	}

	public static String getProgramLanguage() {
		return programLanguage;
	}

	public static String getApplicationName() {
		return applicationName;
	}

	public SoftwareArchitecture getSoftwareArchitecture() {
		return softwareArchitecture;
	}

	public DependencyDTO[] getAllDependencies() {
		return allDependencies;
	}

	public ArrayList<Violation> getAllViolations() {
		return allViolations;
	}
	
	//GET SERVICES
	public IControlService getControlService() {
		return controlService;
	}

	public IAnalyseService getAnalyseService() {
		return analyseService;
	}

	public IDefineService getDefineService() {
		return defineService;
	}

	public IValidateService getValidateService() {
		return validateService;
	}
	
	//LOG4J
	private static void setLog4jConfiguration() {
		URL propertiesFile = Class.class.getResource("/husacct/common/resources/log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
		logger = Logger.getLogger(RuleTypeSupport.class);
	}
	
	//OTHER METHODS
	
	public void generateViolationTypeListPerRuleType() {
		violationTypesPerRuleType = new TreeMap<String, String[]>();
		
		ArrayList<ActiveRuleType> activeRuleTypesJava = (ArrayList<ActiveRuleType>) ((ValidateServiceImpl) validateService).getConfiguration().getActiveViolationTypes().get("Java");
		
		for(ActiveRuleType activeRuleType: activeRuleTypesJava) {
			//System.out.println("> " + activeRuleType.getRuleType());
			
			int violationTypeSize = 0;
			for(ActiveViolationType activeViolType: activeRuleType.getViolationTypes()) {
				if(activeViolType.isEnabled()) {
					violationTypeSize++;
				}
			}
			
			String[] violationTypes = new String[violationTypeSize];
			int counter = 0;
			for(ActiveViolationType activeViolType: activeRuleType.getViolationTypes()) {
				//System.out.println("==> " + activeViolType.getType() + " - " + activeViolType.isEnabled());
				if(activeViolType.isEnabled()) {
					violationTypes[counter] = activeViolType.getType();
					counter++;
				}
			}
			
			//System.err.println("<==> " + activeRuleType.getRuleType() + " toevoegen");
			violationTypesPerRuleType.put(activeRuleType.getRuleType(), violationTypes);
		}
		
		
	}
}