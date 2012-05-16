package husacct.define.persistency;

import husacct.define.domain.Application;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.module.Layer;
import husacct.define.domain.module.Module;

import org.jdom2.Element;

public class DomainXML {

	public DomainXML(){

	}

	public Element getSoftwareUnitDefinitionInXML(SoftwareUnitDefinition SUD) {
		Element XMLSud = new Element("SoftwareUnitDefinition");

		Element SudName = new Element("name");
		SudName.addContent(SUD.getName());
		XMLSud.addContent(SudName);

		Element SudType = new Element("type");
		SudType.addContent(SUD.getType().toString());
		XMLSud.addContent(SudType);

		return XMLSud;
	}

	public Element getSoftwareArchitectureInXML(SoftwareArchitecture SA) {
		Element XMLArchitecture = new Element("Architecture");

		Element SAName = new Element("name");
		SAName.addContent(SA.getName());
		XMLArchitecture.addContent(SAName);

		Element SADescription = new Element("description");
		SADescription.addContent(SA.getDescription());
		XMLArchitecture.addContent(SADescription);

		if (SA.getModules().size() > 0) {
			Element SAModules = new Element("modules");
			for (Module m : SA.getModules()) {
				SAModules.addContent(this.getModuleInXML(m));
			}
			XMLArchitecture.addContent(SAModules);
		}

		if (SA.getAppliedRules().size() > 0) {
			Element SARules = new Element("rules");
			for (AppliedRule ar : SA.getAppliedRules()) {
				SARules.addContent(this.getAppliedRuleInXML(ar));
			}
			XMLArchitecture.addContent(SARules);
		}

		return XMLArchitecture;
	}

	public Element getModuleInXML(Module M) {
		Element xmlModule = new Element("Module");

		Element moduleType = new Element("type");
		moduleType.addContent(M.getClass().getSimpleName());
		xmlModule.addContent(moduleType);

		Element moduleDescription = new Element("description");
		moduleDescription.addContent(M.getDescription());
		xmlModule.addContent(moduleDescription);

		Element moduleId = new Element("id");
		moduleId.addContent(Long.toString(M.getId()));
		xmlModule.addContent(moduleId);

		Element moduleName = new Element("name");
		moduleName.addContent(M.getName());
		xmlModule.addContent(moduleName);

		/**
		 * build extra elements based on type (Module is generic)
		 */
		if (M.getClass().getSimpleName().toLowerCase().equals("layer")) {
			Element moduleLevel = new Element("HierarchicalLevel");
			moduleLevel.addContent("" + ((Layer)M).getHierarchicalLevel());
			xmlModule.addContent(moduleLevel);
		}

		/**
		 * Check for units and add them to the XML root of this element
		 */
		if (M.getUnits().size() > 0) {
			Element units = new Element("SoftwareUnitDefinitions");
			for (SoftwareUnitDefinition SUD : M.getUnits()) {
				units.addContent(this.getSoftwareUnitDefinitionInXML(SUD));
			}
			xmlModule.addContent(units);
		}

		/**
		 * Check for submodules and add them to the root of the parent (recursive call)
		 */
		if (M.getSubModules().size() > 0) {
			Element subModule = new Element("SubModules");
			for (Module m : M.getSubModules()) {
				subModule.addContent(this.getModuleInXML(m));	
			}
			xmlModule.addContent(subModule);
		}

		if (M.getPhysicalPaths().length > 0) {
			Element physicalPaths = new Element("PhysicalPaths");
			for(int i=0; i < M.getPhysicalPaths().length; i++) {
				physicalPaths.addContent(this.getPhysicalPathInXML(M.getPhysicalPaths()[i].toString()));
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
			Element restrictedModule = new Element("restrictedmodule");
			restrictedModule.addContent(this.getModuleInXML(AR.getModuleFrom()));
			XMLAppliedRule.addContent(restrictedModule);
		}

		if (AR.getModuleTo() instanceof Module) {
			Element usedModule = new Element("usedmodule");
			usedModule.addContent(this.getModuleInXML(AR.getModuleTo()));
			XMLAppliedRule.addContent(usedModule);
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

		Element applicationPrLanguage = new Element("programmingLanguage");
		applicationPrLanguage.addContent(App.getLanguage());
		XMLApplication.addContent(applicationPrLanguage);

		Element applicationVersion = new Element("version");
		applicationVersion.addContent(App.getVersion());
		XMLApplication.addContent(applicationVersion);

		XMLApplication.addContent(this.getSoftwareArchitectureInXML(SoftwareArchitecture.getInstance()));

		return XMLApplication;
	}
}
