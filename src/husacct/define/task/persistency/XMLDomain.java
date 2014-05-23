package husacct.define.task.persistency;

import husacct.define.domain.Application;
import husacct.define.domain.Project;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleFactory;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.module.modules.Layer;
import husacct.define.domain.services.AppliedRuleDomainService;
import husacct.define.domain.services.ModuleDomainService;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition.Type;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom2.Element;

public class XMLDomain {

	private Element workspace;
	private Application application;
	private SoftwareArchitecture softwareArchitecture;
	private AppliedRuleDomainService ruleService = new AppliedRuleDomainService();
	private ModuleDomainService moduleService = new ModuleDomainService();
	private final Logger logger = Logger.getLogger(XMLDomain.class);
    private long highestModuleId;
    private long highestAppliedRuleId;


	public XMLDomain(Element workspaceData) {
		workspace = workspaceData;
		softwareArchitecture = SoftwareArchitecture.getInstance();
		highestModuleId = 0;
		highestAppliedRuleId = 0;
	}

	public Application createApplication() {
		try {
			logger.info("Loading application from storage");
			List<Element> applicationProperties = workspace.getChildren();

			Element name = applicationProperties.get(0);
			Element version = applicationProperties.get(1);
			Element projects = applicationProperties.get(2);
			Element architecture = applicationProperties.get(3);
			ArrayList<Project> projectsList = getProjectsFromElement(projects);

			application = new Application(name.getValue(), projectsList, version.getValue());
			application.setArchitecture(createArchitectureFromElement(architecture));
		} catch (Exception exe) {
			logger.error("createApplication() :86 nill");
		}
		return application;
	}

	private ArrayList<Project> getProjectsFromElement(Element XMLElement) {
		ArrayList<Project> projects = new ArrayList<Project>();
		for (Element project : XMLElement.getChildren("Project")) {
			projects.add(getProjectFromElement(project));
		}
		return projects;
	}

	private Project getProjectFromElement(Element XMLElement) {
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

	private SoftwareArchitecture createArchitectureFromElement(Element XMLElement) {
		softwareArchitecture.setName(XMLElement.getChild("name").getValue());
		softwareArchitecture.setDescription(XMLElement.getChild("description").getValue());
		// Check if there are modules in the XML
		if (XMLElement.getChild("modules").getChildren().size() > 0) {
			createModulesFromXML((long) 0, XMLElement.getChild("modules"));
		}
		// Check if there are rules in the XML
		if (XMLElement.getChild("rules").getChildren().size() > 0) {
			createAppliedRulesFromXML(XMLElement.getChild("rules"));
		}
		// Set highestId for ModuleStrategy and AppliedRuleStrategy
		ModuleStrategy.setStaticId(highestModuleId);
		AppliedRuleStrategy.setStaticId(highestAppliedRuleId);

		return softwareArchitecture;
	}

	private void createModulesFromXML(long parentId, Element XMLElement) {
		try{
			for (Element module : XMLElement.getChildren()) {
				ModuleStrategy newModule;
				ModuleFactory factory = new ModuleFactory();
				String moduleType = module.getChildText("type");
				String moduleDescription = module.getChildText("description");
				String moduleName = module.getChildText("name");
				Element SoftwareUnitDefinitions = module.getChild("SoftwareUnitDefinitions");
				int moduleId = Integer.parseInt(module.getChildText("id"));
				// Determine highestModuleId to make sure that new modules (after loading from XML) don't get an existing moduleId, erroneously.
				if(moduleId > highestModuleId){
					highestModuleId = moduleId;
				}
	
				switch (moduleType) {
				case "ExternalLibrary":
					newModule = moduleService.createNewModule("ExternalLibrary");
					break;
				case "Component":
					newModule = moduleService.createNewModule("Component");
					break;
				case "Facade":
					newModule = moduleService.createNewModule("Facade");
					break;
				case "SubSystem":
					newModule = moduleService.createNewModule("SubSystem");
					break;
				case "Layer":
					newModule = moduleService.createNewModule("Layer");
					int HierarchicalLevel = Integer.parseInt(module
							.getChildText("HierarchicalLevel"));
					((Layer) newModule).setHierarchicalLevel(HierarchicalLevel);
					break;
				default:
					newModule = factory.createDummy("Blank");
					break;
				}
	
				boolean fromStorage = true;
				newModule.set(moduleName, moduleDescription, fromStorage);
				newModule.setId(moduleId);
	
				// Add neModule to parent
				if (parentId == 0) {
					try {
						moduleService.addModuleToRoot(newModule);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					moduleService.addModuleToParent(parentId, newModule);
				}
	
				if (SoftwareUnitDefinitions != null) {
					List<Element> SoftwareUnitDefinitionsList = SoftwareUnitDefinitions.getChildren("SoftwareUnitDefinition");
					Iterator SUDIterator = SoftwareUnitDefinitionsList.iterator();
					while (SUDIterator.hasNext()) {
						Object o = SUDIterator.next();
						if (o instanceof Element) {
							newModule.addSUDefinition(getSoftwareUnitDefinitionFromXML((Element) o));
						}
					}
				}
	
				if (hasSubmodules(module)) {
					createModulesFromXML(newModule.getId(),	module.getChild("SubModules"));
				}
			}
			SoftwareArchitecture.getInstance().registerImportedValues();
		} catch (Exception exe) {
			logger.error("createModulesFromXML()" + exe.getMessage());
		}
	}

	public SoftwareUnitDefinition getSoftwareUnitDefinitionFromXML(Element e) {
		Element SUDName = e.getChild("name");
		Element SUDType = e.getChild("type");
		Type SoftwareUnitDefinitionType;

		if (SUDType.getValue().toUpperCase().equals("CLASS")) {
			SoftwareUnitDefinitionType = Type.CLASS;
		} else if (SUDType.getValue().toUpperCase().equals("INTERFACE")) {
			SoftwareUnitDefinitionType = Type.INTERFACE;
		} else if (SUDType.getValue().toUpperCase().equals("EXTERNALLIBRARY")) {
			SoftwareUnitDefinitionType = Type.EXTERNALLIBRARY;
		} else if (SUDType.getValue().toUpperCase().equals("LIBRARY")) {
			SoftwareUnitDefinitionType = Type.LIBRARY;
		} else if (SUDType.getValue().toUpperCase().equals("PACKAGE")) {
			SoftwareUnitDefinitionType = Type.PACKAGE;
		} else if (SUDType.getValue().toUpperCase().equals("SUBSYSTEM")) {
			SoftwareUnitDefinitionType = Type.SUBSYSTEM;
		} else {
			SoftwareUnitDefinitionType = Type.REGEX;
		}

		return new SoftwareUnitDefinition(SUDName.getText(),
				SoftwareUnitDefinitionType);
	}

	private boolean hasSubmodules(Element XMLElement) {
		if (XMLElement.getChildren("SubModules").size() > 0) {
			return true;
		}
		return false;
	}

	private void createAppliedRulesFromXML(Element XMLElement) {
		try{
			// 1) Reload all applied rules
			for (Element appliedRule : XMLElement.getChildren()) {
				AppliedRuleStrategy rule = createRuleFromXML(appliedRule);
				// Determine highestAppliedRuleId to make sure that new rules (after loading from XML) don't get an existing appliedRuleId.
				if(rule.getId() > highestAppliedRuleId){
					highestAppliedRuleId = rule.getId();
				}
			}
			// 2) Get exception rules and establish links from main rules to exceptions 
			ArrayList<AppliedRuleStrategy> exceptionsList = ruleService.getAllExceptionRules();
			for (AppliedRuleStrategy exceptionRule : exceptionsList){
				AppliedRuleStrategy parentRule = exceptionRule.getParentAppliedRule();
				parentRule.addException(exceptionRule);
			}
		} catch (Exception e) {
        	this.logger.warn(new Date().toString() + "Applied rule not reloaded" + e.getMessage());
        }
	}

	private AppliedRuleStrategy createRuleFromXML(Element appliedRule) {
		AppliedRuleStrategy rule = null;
		try{
		long ruleId = Integer.parseInt(appliedRule.getChildText("id"));
		String ruleTypeKey = appliedRule.getChildText("type");
		//int moduleFromId = Integer.parseInt(appliedRule.getChild("moduleFrom").getChild("ModuleStrategy").getChildText("id"));
		//int moduleToId = Integer.parseInt(appliedRule.getChild("moduleTo").getChild("ModuleStrategy").getChildText("id"));
		long moduleFromId = Integer.parseInt(appliedRule.getChildText("moduleFrom"));
		long moduleToId = Integer.parseInt(appliedRule.getChildText("moduleTo"));
		boolean isEnabled = Boolean.parseBoolean(appliedRule.getChildText("enabled"));
		String description = appliedRule.getChildText("description");
		String regex = appliedRule.getChildText("regex");
		Element dependencies = appliedRule.getChild("dependencies");
		String[] dependencyTypes = getDependencyTypesFromXML(dependencies);
		boolean isException = Boolean.parseBoolean(appliedRule.getChildText("isException"));
		long parentRule = -1;
		if (isException)
			parentRule = Integer.parseInt(appliedRule.getChildText("parentAppliedRuleId"));
		 rule = ruleService.reloadAppliedRule(ruleId, ruleTypeKey, description, dependencyTypes, regex, moduleFromId, moduleToId, isEnabled, isException, parentRule);
		} catch (Exception e) {
        	this.logger.warn(new Date().toString() + "Applied rule not reloaded" + e.getMessage());
		}
		return rule;
	}

	private String[] getDependencyTypesFromXML(Element XMLElement) {
		ArrayList<String> dependencies = new ArrayList<String>();
		for (Element dependency : XMLElement.getChildren("dependency")) {
			dependencies.add(dependency.getValue());
		}
		String[] returnValue = null;
		returnValue = dependencies.toArray(new String[dependencies.size()]);
		return returnValue;
	}

}

