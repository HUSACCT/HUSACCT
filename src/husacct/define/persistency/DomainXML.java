package husacct.define.persistency;

import husacct.define.domain.Application;
import husacct.define.domain.Project;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.module.modules.Layer;

import org.jdom2.Element;

public class DomainXML {

    private SoftwareArchitecture domainSoftwareArchitecture;

    private Boolean parseLogical = true;

    public DomainXML(SoftwareArchitecture sa) {
	domainSoftwareArchitecture = sa;
    }

    public Element getApplicationInXML(Application App) {
	Element XMLApplication = new Element("Application");

	Element applicationName = new Element("name");
	applicationName.addContent(App.getName());
	XMLApplication.addContent(applicationName);

	Element applicationVersion = new Element("version");
	applicationVersion.addContent(App.getVersion());
	XMLApplication.addContent(applicationVersion);

	Element applicationProjects = new Element("projects");
	for (Project project : App.getProjects()) {
	    applicationProjects.addContent(getProjectInXML(project));
	}
	XMLApplication.addContent(applicationProjects);

	XMLApplication.addContent(getSoftwareArchitectureInXML());

	return XMLApplication;
    }

    public Element getAppliedRuleInXML(AppliedRuleStrategy AR) {
	Element XMLAppliedRule = new Element("AppliedRule");

	Element ruleRegex = new Element("regex");
	ruleRegex.addContent(AR.getRegex());
	XMLAppliedRule.addContent(ruleRegex);

	Element ruleDescription = new Element("description");
	ruleDescription.addContent(AR.getDescription());
	XMLAppliedRule.addContent(ruleDescription);

	Element ruleEnabled = new Element("enabled");
	if (AR.isEnabled()) {
	    ruleEnabled.addContent("true");
	} else {
	    ruleEnabled.addContent("false");
	}
	XMLAppliedRule.addContent(ruleEnabled);

	Element ruleId = new Element("id");
	ruleId.addContent(Long.toString(AR.getId()));
	XMLAppliedRule.addContent(ruleId);

	Element ruleType = new Element("type");
	ruleType.addContent(AR.getRuleType());
	XMLAppliedRule.addContent(ruleType);

	if (AR.getModuleFrom() instanceof ModuleStrategy) {
	    Element moduleFrom = new Element("moduleFrom");
	    moduleFrom.addContent(getModuleInXML(AR.getModuleFrom()));
	    XMLAppliedRule.addContent(moduleFrom);
	}

	if (AR.getModuleTo() instanceof ModuleStrategy) {
	    Element moduleTo = new Element("moduleTo");
	    moduleTo.addContent(getModuleInXML(AR.getModuleTo()));
	    XMLAppliedRule.addContent(moduleTo);
	}

	Element dependencies = new Element("dependencies");
	if (AR.getDependencies().length > 0) {
	    for (int i = 0; i < AR.getDependencies().length; i++) {
		Element dependency = new Element("dependency");
		dependency.addContent(AR.getDependencies()[i].toString());
		dependencies.addContent(dependency);
	    }
	}
	XMLAppliedRule.addContent(dependencies);

	if (AR.getExceptions().size() > 0) {
	    Element ruleExceptions = new Element("exceptions");
	    for (AppliedRuleStrategy ap : AR.getExceptions()) {
		ruleExceptions.addContent(getAppliedRuleInXML(ap));
	    }
	    XMLAppliedRule.addContent(ruleExceptions);
	}

	return XMLAppliedRule;
    }

    public Element getModuleInXML(ModuleStrategy module) {
	Element xmlModule = new Element("ModuleStrategy");

	Element moduleType = new Element("type");
	moduleType.addContent(module.getClass().getSimpleName());
	xmlModule.addContent(moduleType);

	Element moduleDescription = new Element("description");
	moduleDescription.addContent(module.getDescription());
	xmlModule.addContent(moduleDescription);

	Element moduleId = new Element("id");
	moduleId.addContent(Long.toString(module.getId()));
	xmlModule.addContent(moduleId);

	Element moduleName = new Element("name");
	moduleName.addContent(module.getName());
	xmlModule.addContent(moduleName);

	/**
	 * build extra elements based on type (ModuleStrategy is generic)
	 */
	if (module.getClass().getSimpleName().toLowerCase().equals("layer")) {
	    Element moduleLevel = new Element("HierarchicalLevel");
	    moduleLevel
		    .addContent("" + ((Layer) module).getHierarchicalLevel());
	    xmlModule.addContent(moduleLevel);
	}

	/**
	 * Check for units and add them to the XML root of this element
	 */
	if (module.getUnits().size() > 0) {
	    Element units = new Element("SoftwareUnitDefinitions");
	    for (SoftwareUnitDefinition SUD : module.getUnits()) {
		units.addContent(getSoftwareUnitDefinitionInXML(SUD));
	    }
	    xmlModule.addContent(units);
	}

	/**
	 * Check for submodules and add them to the root of the parent
	 * (recursive call)
	 */
	if (module.getSubModules().size() > 0) {
	    Element subModule = new Element("SubModules");
	    for (ModuleStrategy m : module.getSubModules()) {
		subModule.addContent(getModuleInXML(m));
	    }
	    xmlModule.addContent(subModule);
	}

	if (module.getUnits().size() > 0) {
	    Element physicalPaths = new Element("PhysicalPaths");

	    for (SoftwareUnitDefinition su : module.getUnits()) {
		physicalPaths.addContent(getPhysicalPathInXML(su.getName()));
	    }

	    xmlModule.addContent(physicalPaths);
	}

	return xmlModule;
    }

    public Boolean getParseLogical() {
	return parseLogical;
    }

    public Element getPhysicalPathInXML(String path) {
	Element XMLPath = new Element("path");
	XMLPath.addContent(path);
	return XMLPath;
    }

    public Element getProjectInXML(Project project) {
	Element XMLProject = new Element("Project");

	Element projectName = new Element("name");
	projectName.addContent(project.getName());
	XMLProject.addContent(projectName);

	Element projectPaths = new Element("paths");
	for (String path : project.getPaths()) {
	    Element projectPath = new Element("path");
	    projectPath.addContent(path);
	    projectPaths.addContent(projectPath);
	}
	XMLProject.addContent(projectPaths);

	Element projectPrLanguage = new Element("programmingLanguage");
	projectPrLanguage.addContent(project.getProgrammingLanguage());
	XMLProject.addContent(projectPrLanguage);

	Element projectVersion = new Element("version");
	projectVersion.addContent(project.getVersion());
	XMLProject.addContent(projectVersion);

	Element projectDescription = new Element("description");
	projectDescription.addContent(project.getDescription());
	XMLProject.addContent(projectDescription);

	return XMLProject;
    }

    public Element getSoftwareArchitectureInXML() {
	Element XMLArchitecture = new Element("Architecture");

	Element SAName = new Element("name");
	SAName.addContent(domainSoftwareArchitecture.getName());
	XMLArchitecture.addContent(SAName);

	Element SADescription = new Element("description");
	SADescription.addContent(domainSoftwareArchitecture.getDescription());
	XMLArchitecture.addContent(SADescription);

	if (domainSoftwareArchitecture.getModules().size() > 0) {
	    Element SAModules = new Element("modules");
	    for (ModuleStrategy m : domainSoftwareArchitecture.getModules()) {
		SAModules.addContent(getModuleInXML(m));
	    }
	    XMLArchitecture.addContent(SAModules);
	}

	if (domainSoftwareArchitecture.getAppliedRules().size() > 0) {
	    Element SARules = new Element("rules");
	    for (AppliedRuleStrategy ar : domainSoftwareArchitecture.getAppliedRules()) {
		SARules.addContent(getAppliedRuleInXML(ar));
	    }
	    XMLArchitecture.addContent(SARules);
	}

	return XMLArchitecture;
    }

    public Element getSoftwareUnitDefinitionInXML(SoftwareUnitDefinition SUD) {
	Element XMLSoftwareUnitDefinition = new Element(
		"SoftwareUnitDefinition");

	Element SudName = new Element("name");
	SudName.addContent(SUD.getName());
	XMLSoftwareUnitDefinition.addContent(SudName);

	Element SudType = new Element("type");
	SudType.addContent(SUD.getType().toString());
	XMLSoftwareUnitDefinition.addContent(SudType);

	return XMLSoftwareUnitDefinition;
    }

    public void setParseLogical(Boolean doParse) {
	parseLogical = doParse;
    }
}
