package husacct.define.task.persistency;

import java.util.ArrayList;

import husacct.define.domain.Application;
import husacct.define.domain.Project;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.module.modules.Layer;
import husacct.define.domain.services.AppliedRuleDomainService;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;

import org.jdom2.Element;

public class DomainXML {
    private SoftwareArchitecture softwareArchitecture;
	private AppliedRuleDomainService ruleService = new AppliedRuleDomainService();
    private Boolean parseLogical = true;

    public DomainXML(SoftwareArchitecture sa) {
    	softwareArchitecture = sa;
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
		
		Element ruleId = new Element("id");
		ruleId.addContent(Long.toString(AR.getId()));
		XMLAppliedRule.addContent(ruleId);
	
		Element ruleType = new Element("type");
		ruleType.addContent(AR.getRuleTypeKey());
		XMLAppliedRule.addContent(ruleType);
	
		if (AR.getModuleFrom() instanceof ModuleStrategy) {
		    Element moduleFrom = new Element("moduleFrom");
		    long fromId = AR.getModuleFrom().getId();
		    moduleFrom.addContent(Long.toString(fromId));
		    XMLAppliedRule.addContent(moduleFrom);
		}
	
		if (AR.getModuleTo() instanceof ModuleStrategy) {
		    Element moduleTo = new Element("moduleTo");
		    long toId = AR.getModuleTo().getId();
		    moduleTo.addContent(Long.toString(toId));
		    XMLAppliedRule.addContent(moduleTo);
		}
	
		Element ruleEnabled = new Element("enabled");
		if (AR.isEnabled()) {
		    ruleEnabled.addContent("true");
		} else {
		    ruleEnabled.addContent("false"); }
		XMLAppliedRule.addContent(ruleEnabled);
	
		Element ruleDescription = new Element("description");
		ruleDescription.addContent(AR.getDescription());
		XMLAppliedRule.addContent(ruleDescription);
	
		Element ruleRegex = new Element("regex");
		ruleRegex.addContent(AR.getRegex());
		XMLAppliedRule.addContent(ruleRegex);
	
		Element dependencies = new Element("dependencies");
		if (AR.getDependencyTypes().length > 0) {
		    for (int i = 0; i < AR.getDependencyTypes().length; i++) {
			Element dependency = new Element("dependency");
			dependency.addContent(AR.getDependencyTypes()[i].toString());
			dependencies.addContent(dependency);
		    }
		}
		XMLAppliedRule.addContent(dependencies);
		
		Element isException = new Element("isException");
		if (AR.isException()) {
			isException.addContent("true");
		} else {
			isException.addContent("false"); }
		XMLAppliedRule.addContent(isException);
		
		if (AR.getParentAppliedRule() instanceof AppliedRuleStrategy) {
		    Element parentAppliedRuleId = new Element("parentAppliedRuleId");
		    long parentId = AR.getParentAppliedRule().getId(); 
		    parentAppliedRuleId.addContent(Long.toString(parentId));
		    XMLAppliedRule.addContent(parentAppliedRuleId);
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
		    moduleLevel.addContent("" + ((Layer) module).getHierarchicalLevel());
		    xmlModule.addContent(moduleLevel);
		}
	
		/**
		 * Check for units and add them to the XML root of this element
		 */
		if (module.getUnits().size() > 0) {
		    Element units = new Element("SoftwareUnitDefinitions");
		    Element physicalPaths = new Element("PhysicalPaths");
		    for (SoftwareUnitDefinition SUD : module.getUnits()) {
		    	units.addContent(getSoftwareUnitDefinitionInXML(SUD));
		    	physicalPaths.addContent(getPhysicalPathInXML(SUD.getName()));
		    }
		    xmlModule.addContent(units);
		    xmlModule.addContent(physicalPaths);
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
		try{
			Element SAName = new Element("name");
			SAName.addContent(softwareArchitecture.getName());
			XMLArchitecture.addContent(SAName);
		
			Element SADescription = new Element("description");
			SADescription.addContent(softwareArchitecture.getDescription());
			XMLArchitecture.addContent(SADescription);
		
			
			ArrayList<ModuleStrategy> modules = softwareArchitecture.getModules();
			if (modules.size() > 0) {
			    Element SAModules = new Element("modules");
			    for (ModuleStrategy m : modules) {
			    	SAModules.addContent(getModuleInXML(m));
			    }
			    XMLArchitecture.addContent(SAModules);
			}
		
			ArrayList<AppliedRuleStrategy> appliedRules = ruleService.getAllAppliedRules();
			if (appliedRules.size() > 0) {
			    Element SARules = new Element("rules");
			    for (AppliedRuleStrategy appliedRule : appliedRules) {
			    	SARules.addContent(getAppliedRuleInXML(appliedRule));
			    }
			    XMLArchitecture.addContent(SARules);
			}
		
		}catch(Exception er){
			er.getStackTrace();
		}
		return XMLArchitecture;
    }

    public Element getSoftwareUnitDefinitionInXML(SoftwareUnitDefinition SUD) {
		Element XMLSoftwareUnitDefinition = new Element("SoftwareUnitDefinition");
	
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
