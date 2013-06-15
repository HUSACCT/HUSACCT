package husacct.define.persistency;

import husacct.define.domain.Application;
import husacct.define.domain.Project;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleFactory;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleFactory;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.module.modules.Layer;
import husacct.define.domain.services.AppliedRuleDomainService;
import husacct.define.domain.services.ModuleDomainService;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

public class XMLDomain {

	private Element workspace;
	private Application application;
	private SoftwareArchitecture softwareArchitecture;
	private AppliedRuleDomainService ruleService = new AppliedRuleDomainService();
	private ModuleDomainService moduleService = new ModuleDomainService();
	private AppliedRuleFactory ruleFactory = new AppliedRuleFactory();
	private ArrayList<AppliedRuleStrategy> appliedRules;

	public XMLDomain(Element workspaceData) {
		workspace = workspaceData;
		softwareArchitecture = SoftwareArchitecture.getInstance();
	}

	public Application createApplication(){
		List<Element> applicationProperties = workspace.getChildren();

		Element name = applicationProperties.get(0);
		Element version = applicationProperties.get(1);
		Element projects = applicationProperties.get(2);
		Element architecture = applicationProperties.get(3);
		ArrayList<Project> projectsList = getProjectsFromElement(projects);

		application = new Application(name.getValue(), projectsList, version.getValue());
		application.setArchitecture(createArchitectureFromElement(architecture));
		return application;
	}

	private ArrayList<Project> getProjectsFromElement(Element XMLElement){
		ArrayList<Project> projects = new ArrayList<Project>();
		for(Element project : XMLElement.getChildren("Project")){
			projects.add(getProjectFromElement(project));
		}
		return projects;
	}

	private Project getProjectFromElement(Element XMLElement){
		Project project = new Project();
		project.setName(XMLElement.getChild("name").getText());
		project.setProgrammingLanguage(XMLElement.getChild("programmingLanguage").getText());
		project.setVersion(XMLElement.getChild("version").getText());
		project.setDescription(XMLElement.getChild("description").getText());

		ArrayList<String> projectPaths = new ArrayList<String>();
		List<Element> pathElements = XMLElement.getChild("paths").getChildren("path");
		for (Element path : pathElements) {
			projectPaths.add(path.getText());
		}
		project.setPaths(projectPaths);

		return project;
	}

	private SoftwareArchitecture createArchitectureFromElement(Element XMLElement){
		softwareArchitecture.setName(XMLElement.getChild("name").getValue());
		softwareArchitecture.setDescription(XMLElement.getChild("description").getValue());

		// Check if there are modules in the XML
		if(XMLElement.getChild("modules").getChildren().size() > 0){
			createModulesFromXML((long)0, XMLElement.getChild("modules"));
		}

		// Check if there are rules in the XML
		if(XMLElement.getChild("rules").getChildren().size() > 0){
			createAppliedRulesFromXML(XMLElement.getChild("rules"));
		}

		return softwareArchitecture;
	}

	private void createModulesFromXML(long parentId, Element XMLElement){
		for(Element module : XMLElement.getChildren()){
			ModuleStrategy newModule;
			ModuleFactory factory 		= new ModuleFactory();
			String moduleType		 	= module.getChildText("type");
			String moduleDescription	= module.getChildText("description");
			int moduleId 		 		= Integer.parseInt(module.getChildText("id"));
			String moduleName 		 	= module.getChildText("name");

			newModule = factory.createModule(moduleType);
			if(newModule.getType().equalsIgnoreCase("Layer")){
				((Layer)newModule).setHierarchicalLevel(Integer.parseInt((module.getChildText("HierarchicalLevel"))));
			}

			newModule.set(moduleName, moduleDescription);
			newModule.setId(moduleId);
			moduleService.addModuleToParent(parentId, newModule);

			// Add submodules
			if(hasSubmodules(module)){
				createModulesFromXML(newModule.getId(), module.getChild("SubModules"));
			}
		}
	}

	private boolean hasSubmodules(Element XMLElement){
		if(XMLElement.getChildren("SubModules").size() > 0){
			return true;
		}
		return false;
	}

	private void createAppliedRulesFromXML(Element XMLElement){
		for(Element appliedRule : XMLElement.getChildren()){
			AppliedRuleStrategy dummyRule = createDummyRule(appliedRule);

			if(!ruleService.isMandatory(dummyRule.getRuleType(), dummyRule.getModuleFrom())){
				long newID = ruleService.addAppliedRule(
						dummyRule.getRuleType(),
						dummyRule.getDescription(),
						dummyRule.getDependencies(),
						dummyRule.getRegex(),
						dummyRule.getModuleFrom(),
						dummyRule.getModuleTo(),
						dummyRule.isEnabled());
				AppliedRuleStrategy newRule = ruleService.getAppliedRuleById(newID);
				newRule.setId(dummyRule.getId());
				if(hasExceptions(appliedRule)){
					newRule.setExceptions(getExceptionsFromXML(appliedRule));
				}
			}
		}
	}

	private AppliedRuleStrategy getAppliedRuleFromXML(Element XMLElement){
		AppliedRuleStrategy dummyRule = createDummyRule(XMLElement);
		long newID = ruleService.addAppliedRule(
				dummyRule.getRuleType(),
				dummyRule.getDescription(),
				dummyRule.getDependencies(),
				dummyRule.getRegex(),
				dummyRule.getModuleFrom(),
				dummyRule.getModuleTo(),
				dummyRule.isEnabled());
		AppliedRuleStrategy newRule = ruleService.getAppliedRuleById(newID);
		newRule.setId(dummyRule.getId());
		return newRule;
	}

	private AppliedRuleStrategy createDummyRule(Element appliedRule){
		String ruleTypeKey 	= appliedRule.getChildText("type");
		String description	= appliedRule.getChildText("description");
		boolean enabled 	= Boolean.parseBoolean(appliedRule.getChildText("enabled"));
		String regex 	= appliedRule.getChildText("regex");
		int ruleId 		= Integer.parseInt(appliedRule.getChildText("id"));

		Element dependencies= appliedRule.getChild("dependencies");

		String[] dependencyList = getDependenciesFromXML(dependencies);

		int moduleFromId 	= Integer.parseInt(appliedRule.getChild("moduleFrom").getChild("ModuleStrategy").getChildText("id"));
		int moduleToId 	= Integer.parseInt(appliedRule.getChild("moduleTo").getChild("ModuleStrategy").getChildText("id"));

		AppliedRuleStrategy dummyRule 	= ruleFactory.createDummyRule(ruleTypeKey);
		ModuleStrategy moduleFrom 			= moduleService.getModuleById(moduleFromId);
		ModuleStrategy moduleTo 			= moduleService.getModuleById(moduleToId);

		dummyRule.setAppliedRule(description, dependencyList, regex, moduleFrom, moduleTo, enabled);
		dummyRule.setId(ruleId);
		return dummyRule;
	}

	//TODO: Check if this function really works
	private String[] getDependenciesFromXML(Element XMLElement){
		ArrayList<String> dependencies = new ArrayList<String>();
		for(Element dependency : XMLElement.getChildren("dependency")){
			dependencies.add(dependency.getValue());
		}
		return dependencies.toArray(new String[dependencies.size()]);
	}

	private boolean hasExceptions(Element XMLElement){
		return (XMLElement.getChildren("exceptions").size() > 0)? true : false;
	}
	
	//TODO: Check if this function really works
	private ArrayList<AppliedRuleStrategy> getExceptionsFromXML(Element XMLElement){
		ArrayList<AppliedRuleStrategy> exceptions = new ArrayList<AppliedRuleStrategy>();
		for(Element exception : XMLElement.getChild("exceptions").getChildren("AppliedRule")){
			exceptions.add(getAppliedRuleFromXML(exception));
		}
		return exceptions;
	}
}

/*
 * This is the old XMLDomain, in case some of the functionality is needed.
 */
//package husacct.define.persistency;
//
//import husacct.define.domain.Application;
//import husacct.define.domain.Project;
//import husacct.define.domain.SoftwareArchitecture;
//import husacct.define.domain.SoftwareUnitDefinition;
//import husacct.define.domain.SoftwareUnitDefinition.Type;
//import husacct.define.domain.appliedrule.AppliedRuleFactory;
//import husacct.define.domain.appliedrule.AppliedRuleStrategy;
//import husacct.define.domain.module.Component;
//import husacct.define.domain.module.ExternalSystem;
//import husacct.define.domain.module.Layer;
//import husacct.define.domain.module.Module;
//import husacct.define.domain.module.SubSystem;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import org.jdom2.Element;
//
//public class XMLDomain {
//
//    private Element workspace;
//
//    public XMLDomain(Element workspaceData) {
//	workspace = workspaceData;
//    }
//
//    public Application getApplication() {
//	List<Element> applicationProperties = getWorkspaceChildren();
//
//	Element ApName = applicationProperties.get(0);
//	Element ApVersion = applicationProperties.get(1);
//	Element ApProjects = applicationProperties.get(2);
//	ArrayList<Project> applicationProjects = getProjects(ApProjects);
//
//	Application XMLAp = new Application(ApName.getText(),
//		applicationProjects, ApVersion.getText());
//	XMLAp.setArchitecture(getArchitecture());
//
//	return XMLAp;
//    }
//
//    @SuppressWarnings("rawtypes")
//    public AppliedRuleStrategy getAppliedRuleFromXML(Element e) {
//	Element ruleDescription = e.getChild("description");
//	Element ruleRegex = e.getChild("regex");
//	Element ruleId = e.getChild("id");
//	Element ruleType = e.getChild("type");
//	Element ruleModuleFrom = e.getChild("moduleFrom").getChild("Module");
//	Module moduleFrom = ruleModuleFrom == null ? new Module()
//	: getModuleFromXML(ruleModuleFrom);
//	Element ruleModuleTo = e.getChild("moduleTo").getChild("Module");
//	Module moduleTo = ruleModuleTo == null ? new Module()
//	: getModuleFromXML(ruleModuleTo);
//	Element ruleExceptions = e.getChild("exceptions");
//	Element ruleEnabled = e.getChild("enabled");
//	Element ruleDependencies = e.getChild("dependencies");
//
//	ArrayList<String> dependencies = new ArrayList<String>();
//	List<Element> ruleDependencyList = ruleDependencies
//		.getChildren("dependency");
//	Iterator DependencyIterator = ruleDependencyList.iterator();
//	if (ruleDependencyList.size() > 0) {
//	    while (DependencyIterator.hasNext()) {
//		Object o = DependencyIterator.next();
//		if (o instanceof Element) {
//		    dependencies.add(((Element) o).getValue());
//		}
//	    }
//	}
//
//	boolean enabled = Boolean.parseBoolean(ruleEnabled.getValue());
//
//	AppliedRuleFactory ruleFactory = new AppliedRuleFactory();
//	AppliedRuleStrategy AppliedXMLRule = ruleFactory.createRule(ruleType.getValue());
//	AppliedXMLRule.setAppliedRule(ruleDescription.getValue(), dependencies.toArray(new String[dependencies.size()]),ruleRegex.getValue(), moduleFrom, moduleTo, enabled);
//	AppliedXMLRule.setId(Integer.parseInt(ruleId.getValue()));
//
//	if (ruleExceptions != null) {
//	    List<Element> ExceptionList = ruleExceptions
//		    .getChildren("AppliedRule");
//	    Iterator ExceptionIterator = ExceptionList.iterator();
//	    if (ExceptionList.size() > 0) {
//		while (ExceptionIterator.hasNext()) {
//		    Object o = ExceptionIterator.next();
//		    if (o instanceof Element) {
//			AppliedXMLRule
//			.addException(getAppliedRuleFromXML((Element) o));
//		    }
//		}
//	    }
//	}
//
//	AppliedXMLRule.setId(Integer.parseInt(ruleId.getValue()));
//
//	return AppliedXMLRule;
//    }
//
//    @SuppressWarnings("rawtypes")
//    public ArrayList<AppliedRuleStrategy> getAppliedRules(
//	    Element ApplicationArchitecture) {
//	Element AppliedRulesRoot = ApplicationArchitecture.getChild("rules");
//	ArrayList<AppliedRuleStrategy> ruleList = new ArrayList<AppliedRuleStrategy>();
//
//	if (AppliedRulesRoot != null) {
//	    List<Element> AppliedRules = AppliedRulesRoot
//		    .getChildren("AppliedRule");
//	    if (AppliedRules.size() > 0) {
//		Iterator appliedIterator = AppliedRules.iterator();
//		while (appliedIterator.hasNext()) {
//		    Object o = appliedIterator.next();
//		    if (o instanceof Element) {
//			ruleList.add(getAppliedRuleFromXML((Element) o));
//		    }
//		}
//	    }
//	}
//
//	return ruleList;
//    }
//
//    public SoftwareArchitecture getArchitecture() {
//	List<Element> applicationProperties = getWorkspaceChildren();
//	Element ApArchitecture = applicationProperties.get(3);
//	Element ArchitectureName = ApArchitecture.getChild("name");
//	Element ArchitectureDescription = ApArchitecture
//		.getChild("description");
//	SoftwareArchitecture XMLArchitecture = new SoftwareArchitecture(
//		ArchitectureName.getText(), ArchitectureDescription.getText());
//	Element ArchitectureModuleRoot = ApArchitecture.getChild("modules");
//	if (ArchitectureModuleRoot != null) {
//	    List<Element> ArchitectureModules = ArchitectureModuleRoot
//		    .getChildren("Module");
//	    if (ArchitectureModules.size() > 0) {
//		@SuppressWarnings("rawtypes")
//		Iterator moduleIterator = ArchitectureModules.iterator();
//		while (moduleIterator.hasNext()) {
//		    Object o = moduleIterator.next();
//
//		    if (o instanceof Element) {
//			XMLArchitecture
//			.addModule(getModuleFromXML((Element) o));
//		    }
//		}
//	    }
//
//	    XMLArchitecture.setModules(getModules(ArchitectureModules));
//	}
//
//	XMLArchitecture.setAppliedRules(getAppliedRules(ApArchitecture));
//	return XMLArchitecture;
//    }
//
//    /*
//     * public ArrayList<AppliedRule> getAppliedRules() { List<Element>
//     * applicationProperties = this.getWorkspaceChildren(); return
//     * this.getAppliedRules((Element)applicationProperties.get(4)); }
//     */
//
//    @SuppressWarnings("rawtypes")
//    public Module getModuleFromXML(Element e) {
//	Element ModuleType = e.getChild("type");
//	String ModuleTypeText = ModuleType.getText();
//	Module xmlModule;
//
//	String moduleName = e.getChild("name").getValue();
//	String moduleDescription = e.getChild("description").getValue();
//
//	String moduleId = e.getChild("id").getValue();
//
//	// type detection..
//	if (ModuleTypeText.equalsIgnoreCase("ExternalSystem")) {
//	    xmlModule = new ExternalSystem(moduleName, moduleDescription);
//	} else if (ModuleTypeText.equalsIgnoreCase("component")) {
//	    xmlModule = new Component(moduleName, moduleDescription);
//	} else if (ModuleTypeText.equalsIgnoreCase("layer")) {
//	    xmlModule = new Layer(
//		    moduleName,
//		    moduleDescription,
//		    Integer.parseInt(e.getChild("HierarchicalLevel").getValue()));
//	} else if (ModuleTypeText.equalsIgnoreCase("subsystem")) {
//	    xmlModule = new SubSystem(moduleName, moduleDescription);
//	} else {
//	    xmlModule = new Module(moduleName, moduleDescription);
//	}
//	xmlModule.setId(Long.parseLong(moduleId));
//
//	Element SoftwareUnitDefinitions = e.getChild("SoftwareUnitDefinitions");
//	if (SoftwareUnitDefinitions != null) {
//	    List<Element> SoftwareUnitDefinitionsList = SoftwareUnitDefinitions
//		    .getChildren("SoftwareUnitDefinition");
//	    Iterator SUDIterator = SoftwareUnitDefinitionsList.iterator();
//	    while (SUDIterator.hasNext()) {
//		Object o = SUDIterator.next();
//
//		if (o instanceof Element) {
//		    xmlModule
//		    .addSUDefinition(getSoftwareUnitDefinitionFromXML((Element) o));
//		}
//	    }
//	}
//
//	Element SubModules = e.getChild("SubModules");
//	if (SubModules != null) {
//	    List<Element> SubModulesList = SubModules.getChildren("Module");
//	    Iterator ModuleIterator = SubModulesList.iterator();
//	    while (ModuleIterator.hasNext()) {
//		Object o = ModuleIterator.next();
//
//		if (o instanceof Element) {
//		    xmlModule.addSubModule(getModuleFromXML((Element) o));
//		}
//	    }
//
//	}
//
//	return xmlModule;
//    }
//
//    public ArrayList<Module> getModules(List<Element> modules) {
//	ArrayList<Module> returnList = new ArrayList<Module>();
//
//	for (int i = 0; i < modules.size(); i++) {
//	    Element theModule = modules.get(i);
//	    returnList.add(getModuleFromXML(theModule));
//	}
//
//	return returnList;
//    }
//
//    private Project getProject(Element projectElement) {
//	Project project = new Project();
//	project.setName(projectElement.getChild("name").getText());
//	project.setProgrammingLanguage(projectElement.getChild(
//		"programmingLanguage").getText());
//	project.setVersion(projectElement.getChild("version").getText());
//	project.setDescription(projectElement.getChild("description").getText());
//
//	ArrayList<String> projectPaths = new ArrayList<String>();
//	List<Element> pathElements = projectElement.getChild("paths")
//		.getChildren("path");
//	for (Element path : pathElements) {
//	    projectPaths.add(path.getText());
//	}
//	project.setPaths(projectPaths);
//
//	return project;
//    }
//
//    private ArrayList<Project> getProjects(Element projectsElement) {
//	ArrayList<Project> projects = new ArrayList<Project>();
//	List<Element> projectElements = projectsElement.getChildren("Project");
//	for (Element project : projectElements) {
//	    projects.add(getProject(project));
//	}
//	return projects;
//    }
//
//    public SoftwareUnitDefinition getSoftwareUnitDefinitionFromXML(Element e) {
//	Element SUDName = e.getChild("name");
//	Element SUDType = e.getChild("type");
//	Type SoftwareUnitDefinitionType;
//
//	if (SUDType.getValue().toUpperCase().equals("CLASS")) {
//	    SoftwareUnitDefinitionType = Type.CLASS;
//	} else if (SUDType.getValue().toUpperCase().equals("INTERFACE")) {
//	    SoftwareUnitDefinitionType = Type.INTERFACE;
//	} else {
//	    SoftwareUnitDefinitionType = Type.PACKAGE;
//	}
//
//	return new SoftwareUnitDefinition(SUDName.getText(),
//		SoftwareUnitDefinitionType);
//    }
//
//    private List<Element> getWorkspaceChildren() {
//	return workspace.getChildren();
//    }
//
//    public Element getWorkspaceData() {
//	return workspace;
//    }
//}