package husacct.define.persistency;

import husacct.define.domain.Application;
import husacct.define.domain.Project;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.SoftwareUnitDefinition.Type;
import husacct.define.domain.appliedrule.AppliedRuleFactory;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleFactory;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.module.modules.Component;
import husacct.define.domain.module.modules.Layer;
import husacct.define.domain.module.modules.SubSystem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;

public class XMLDomain {

    private Element workspace;
    private ModuleFactory factory = new ModuleFactory();

    public XMLDomain(Element workspaceData) {
	workspace = workspaceData;
    }

    public Application getApplication() {
	List<Element> applicationProperties = getWorkspaceChildren();

	Element ApName = applicationProperties.get(0);
	Element ApVersion = applicationProperties.get(1);
	Element ApProjects = applicationProperties.get(2);
	ArrayList<Project> applicationProjects = getProjects(ApProjects);

	Application XMLAp = new Application(ApName.getText(),
		applicationProjects, ApVersion.getText());
	XMLAp.setArchitecture(getArchitecture());

	return XMLAp;
    }

    @SuppressWarnings("rawtypes")
    public AppliedRuleStrategy getAppliedRuleFromXML(Element e) {
	Element ruleDescription = e.getChild("description");
	Element ruleRegex = e.getChild("regex");
	Element ruleId = e.getChild("id");
	Element ruleType = e.getChild("type");
	Element ruleModuleFrom = e.getChild("moduleFrom").getChild("Module");

	ModuleStrategy moduleFrom = ruleModuleFrom == null ? new ModuleStrategy(){}
		: getModuleFromXML(ruleModuleFrom);
	Element ruleModuleTo = e.getChild("moduleTo").getChild("Module");
	ModuleStrategy moduleTo = ruleModuleTo == null ? new ModuleStrategy(){}
		: getModuleFromXML(ruleModuleTo);

	Element ruleExceptions = e.getChild("exceptions");
	Element ruleEnabled = e.getChild("enabled");
	Element ruleDependencies = e.getChild("dependencies");

	ArrayList<String> dependencies = new ArrayList<String>();
	List<Element> ruleDependencyList = ruleDependencies
		.getChildren("dependency");
	Iterator DependencyIterator = ruleDependencyList.iterator();
	if (ruleDependencyList.size() > 0) {
	    while (DependencyIterator.hasNext()) {
		Object o = DependencyIterator.next();
		if (o instanceof Element) {
		    dependencies.add(((Element) o).getValue());
		}
	    }
	}

	boolean enabled = Boolean.parseBoolean(ruleEnabled.getValue());

	AppliedRuleFactory ruleFactory = new AppliedRuleFactory();
	AppliedRuleStrategy AppliedXMLRule = ruleFactory.createRule(ruleType.getValue());
	AppliedXMLRule.setAppliedRule(ruleDescription.getValue(), dependencies.toArray(new String[dependencies.size()]),ruleRegex.getValue(), moduleFrom, moduleTo, enabled);
	AppliedXMLRule.setId(Integer.parseInt(ruleId.getValue()));

	if (ruleExceptions != null) {
	    List<Element> ExceptionList = ruleExceptions
		    .getChildren("AppliedRule");
	    Iterator ExceptionIterator = ExceptionList.iterator();
	    if (ExceptionList.size() > 0) {
		while (ExceptionIterator.hasNext()) {
		    Object o = ExceptionIterator.next();
		    if (o instanceof Element) {
			AppliedXMLRule
			.addException(getAppliedRuleFromXML((Element) o));
		    }
		}
	    }
	}

	AppliedXMLRule.setId(Integer.parseInt(ruleId.getValue()));

	return AppliedXMLRule;
    }

    @SuppressWarnings("rawtypes")
    public ArrayList<AppliedRuleStrategy> getAppliedRules(
	    Element ApplicationArchitecture) {
	Element AppliedRulesRoot = ApplicationArchitecture.getChild("rules");
	ArrayList<AppliedRuleStrategy> ruleList = new ArrayList<AppliedRuleStrategy>();

	if (AppliedRulesRoot != null) {
	    List<Element> AppliedRules = AppliedRulesRoot
		    .getChildren("AppliedRule");
	    if (AppliedRules.size() > 0) {
		Iterator appliedIterator = AppliedRules.iterator();
		while (appliedIterator.hasNext()) {
		    Object o = appliedIterator.next();
		    if (o instanceof Element) {
			ruleList.add(getAppliedRuleFromXML((Element) o));
		    }
		}
	    }
	}

	return ruleList;
    }

    public SoftwareArchitecture getArchitecture() {
	List<Element> applicationProperties = getWorkspaceChildren();
	Element ApArchitecture = applicationProperties.get(3);
	Element ArchitectureName = ApArchitecture.getChild("name");
	Element ArchitectureDescription = ApArchitecture
		.getChild("description");
	SoftwareArchitecture XMLArchitecture = new SoftwareArchitecture(
		ArchitectureName.getText(), ArchitectureDescription.getText());
	Element ArchitectureModuleRoot = ApArchitecture.getChild("modules");
	if (ArchitectureModuleRoot != null) {
	    List<Element> ArchitectureModules = ArchitectureModuleRoot
		    .getChildren("Module");
	    if (ArchitectureModules.size() > 0) {
		@SuppressWarnings("rawtypes")
		Iterator moduleIterator = ArchitectureModules.iterator();
		while (moduleIterator.hasNext()) {
		    Object o = moduleIterator.next();

		    if (o instanceof Element) {
			XMLArchitecture
			.addModule(getModuleFromXML((Element) o));
		    }
		}
	    }

	    XMLArchitecture.setModules(getModules(ArchitectureModules));
	}

	XMLArchitecture.setAppliedRules(getAppliedRules(ApArchitecture));
	return XMLArchitecture;
    }

    /*
     * public ArrayList<AppliedRule> getAppliedRules() { List<Element>
     * applicationProperties = this.getWorkspaceChildren(); return
     * this.getAppliedRules((Element)applicationProperties.get(4)); }
     */

    @SuppressWarnings("rawtypes")
    public ModuleStrategy getModuleFromXML(Element e) {
	Element ModuleType = e.getChild("type");
	String ModuleTypeText = ModuleType.getText();
	ModuleStrategy xmlModule;

	String moduleName = e.getChild("name").getValue();
	String moduleDescription = e.getChild("description").getValue();

	String moduleId = e.getChild("id").getValue();

	// type detection..

	 if (ModuleTypeText.equalsIgnoreCase("ExternalSystem")) {
	 xmlModule = factory.createModule("ExternalLibrary"); 
	xmlModule.set(moduleName, moduleDescription);		
	
	 
	 } else if (ModuleTypeText.equalsIgnoreCase("component")) {
	    xmlModule =factory.createModule("Component");
	    xmlModule.set(moduleName, moduleDescription);
	    	

	} else if (ModuleTypeText.equalsIgnoreCase("layer")) {
	   

			Layer layer=	(Layer)factory.createModule("Layer");
			layer.set(moduleName,moduleDescription);
			layer.setHierarchicalLevel(Integer.parseInt(e.getChild("HierarchicalLevel").getValue()));
	    		xmlModule =		layer;
	    
	} else if (ModuleTypeText.equalsIgnoreCase("subsystem")) {
	    xmlModule =factory.createModule("SubSystem");
	    xmlModule.set(moduleName, moduleDescription);
	    		
	    		
	} else {
	    xmlModule = new ModuleStrategy() {
		};
	    		
	    xmlModule.set(moduleName, moduleDescription);
	}
	xmlModule.setId(Long.parseLong(moduleId));

	Element SoftwareUnitDefinitions = e.getChild("SoftwareUnitDefinitions");
	if (SoftwareUnitDefinitions != null) {
	    List<Element> SoftwareUnitDefinitionsList = SoftwareUnitDefinitions
		    .getChildren("SoftwareUnitDefinition");
	    Iterator SUDIterator = SoftwareUnitDefinitionsList.iterator();
	    while (SUDIterator.hasNext()) {
		Object o = SUDIterator.next();

		if (o instanceof Element) {
		    xmlModule
		    .addSUDefinition(getSoftwareUnitDefinitionFromXML((Element) o));
		}
	    }
	}

	Element SubModules = e.getChild("SubModules");
	if (SubModules != null) {
	    List<Element> SubModulesList = SubModules.getChildren("Module");
	    Iterator ModuleIterator = SubModulesList.iterator();
	    while (ModuleIterator.hasNext()) {
		Object o = ModuleIterator.next();

		if (o instanceof Element) {
		    xmlModule.addSubModule(getModuleFromXML((Element) o));
		}
	    }

	}

	return xmlModule;
    }

    public ArrayList<ModuleStrategy> getModules(List<Element> modules) {
	ArrayList<ModuleStrategy> returnList = new ArrayList<ModuleStrategy>();

	for (int i = 0; i < modules.size(); i++) {
	    Element theModule = modules.get(i);
	    returnList.add(getModuleFromXML(theModule));
	}

	return returnList;
    }

    private Project getProject(Element projectElement) {
	Project project = new Project();
	project.setName(projectElement.getChild("name").getText());
	project.setProgrammingLanguage(projectElement.getChild(
		"programmingLanguage").getText());
	project.setVersion(projectElement.getChild("version").getText());
	project.setDescription(projectElement.getChild("description").getText());

	ArrayList<String> projectPaths = new ArrayList<String>();
	List<Element> pathElements = projectElement.getChild("paths")
		.getChildren("path");
	for (Element path : pathElements) {
	    projectPaths.add(path.getText());
	}
	project.setPaths(projectPaths);

	return project;
    }

    private ArrayList<Project> getProjects(Element projectsElement) {
	ArrayList<Project> projects = new ArrayList<Project>();
	List<Element> projectElements = projectsElement.getChildren("Project");
	for (Element project : projectElements) {
	    projects.add(getProject(project));
	}
	return projects;
    }

    public SoftwareUnitDefinition getSoftwareUnitDefinitionFromXML(Element e) {
	Element SUDName = e.getChild("name");
	Element SUDType = e.getChild("type");
	Type SoftwareUnitDefinitionType;

	if (SUDType.getValue().toUpperCase().equals("CLASS")) {
	    SoftwareUnitDefinitionType = Type.CLASS;
	} else if (SUDType.getValue().toUpperCase().equals("INTERFACE")) {
	    SoftwareUnitDefinitionType = Type.INTERFACE;
	} else {
	    SoftwareUnitDefinitionType = Type.PACKAGE;
	}

	return new SoftwareUnitDefinition(SUDName.getText(),
		SoftwareUnitDefinitionType);
    }

    private List<Element> getWorkspaceChildren() {
	return workspace.getChildren();
    }

    public Element getWorkspaceData() {
	return workspace;
    }
}