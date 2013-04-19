package husacct.define.persistency;

import husacct.define.domain.Application;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.Project;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.module.Layer;
import husacct.define.domain.module.Module;

import org.jdom2.Element;

public class DomainXML {

	private SoftwareArchitecture domainSoftwareArchitecture;
	
	private Boolean parseLogical = true;
	
	public DomainXML(SoftwareArchitecture sa){
		this.domainSoftwareArchitecture = sa;
	}
	
	public Boolean getParseLogical() {
		return this.parseLogical;
	}
	
	public void setParseLogical(Boolean doParse) {
		this.parseLogical = doParse;
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

	public Element getSoftwareArchitectureInXML() {
		Element XMLArchitecture = new Element("Architecture");

		Element SAName = new Element("name");
		SAName.addContent(this.domainSoftwareArchitecture.getName());
		XMLArchitecture.addContent(SAName);

		Element SADescription = new Element("description");
		SADescription.addContent(this.domainSoftwareArchitecture.getDescription());
		XMLArchitecture.addContent(SADescription);

		if (this.domainSoftwareArchitecture.getModules().size() > 0) {
			Element SAModules = new Element("modules");
			for (Module m : this.domainSoftwareArchitecture.getModules()) {
				SAModules.addContent(this.getModuleInXML(m));
			}
			XMLArchitecture.addContent(SAModules);
		}

		if (this.domainSoftwareArchitecture.getAppliedRules().size() > 0) {
			Element SARules = new Element("rules");
			for (AppliedRule ar : this.domainSoftwareArchitecture.getAppliedRules()) {
				SARules.addContent(this.getAppliedRuleInXML(ar));
			}
			XMLArchitecture.addContent(SARules);
		}
		
		return XMLArchitecture;
	}

	public Element getModuleInXML(Module module) {
		Element xmlModule = new Element("Module");

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
		 * build extra elements based on type (Module is generic)
		 */
		if (module.getClass().getSimpleName().toLowerCase().equals("layer")) {
			Element moduleLevel = new Element("HierarchicalLevel");
			moduleLevel.addContent("" + ((Layer)module).getHierarchicalLevel());
			xmlModule.addContent(moduleLevel);
		}

		/**
		 * Check for units and add them to the XML root of this element
		 */
		if (module.getUnits().size() > 0) {
			Element units = new Element("SoftwareUnitDefinitions");
			for (SoftwareUnitDefinition SUD : module.getUnits()) {
				units.addContent(this.getSoftwareUnitDefinitionInXML(SUD));
			}
			xmlModule.addContent(units);
		}

		/**
		 * Check for submodules and add them to the root of the parent (recursive call)
		 */
		if (module.getSubModules().size() > 0) {
			Element subModule = new Element("SubModules");
			for (Module m : module.getSubModules()) {
				subModule.addContent(this.getModuleInXML(m));	
			}
			xmlModule.addContent(subModule);
		}

		if (module.getUnits().size() > 0) {
			Element physicalPaths = new Element("PhysicalPaths");
			
			for (SoftwareUnitDefinition su : module.getUnits()){
				physicalPaths.addContent(this.getPhysicalPathInXML(su.getName()));
			}

			xmlModule.addContent(physicalPaths);
		}

		return xmlModule;
	}

	public Element getPhysicalPathInXML(String path) {
		Element XMLPath = new Element("path");
		XMLPath.addContent(path);
		return XMLPath;
	}

	public Element getAppliedRuleInXML(AppliedRule AR) {
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

		if (AR.getModuleFrom() instanceof Module) {
			Element moduleFrom = new Element("moduleFrom");
			moduleFrom.addContent(this.getModuleInXML(AR.getModuleFrom()));
			XMLAppliedRule.addContent(moduleFrom);
		}

		if (AR.getModuleTo() instanceof Module) {
			Element moduleTo = new Element("moduleTo");
			moduleTo.addContent(this.getModuleInXML(AR.getModuleTo()));
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
			for (AppliedRule ap : AR.getExceptions()) {
				ruleExceptions.addContent(this.getAppliedRuleInXML(ap));
			}
			XMLAppliedRule.addContent(ruleExceptions);
		}

		return XMLAppliedRule;
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
		for(Project project : App.getProjects()) {
			Element applicationProject = new Element("project");
			applicationProject.addContent(getProjectInXML(project));
			applicationProjects.addContent(applicationProject);
		}
		XMLApplication.addContent(applicationProjects);
		
		XMLApplication.addContent(this.getSoftwareArchitectureInXML());
		
		return XMLApplication;
	}
	
	public Element getProjectInXML(Project project) {
		Element XMLProject = new Element("Project");
		
		Element projectName = new Element("name");
		projectName.addContent(project.getName());
		XMLProject.addContent(projectName);
		
		Element projectPaths = new Element("paths");
		for(String path : project.getPaths()) {
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
}
