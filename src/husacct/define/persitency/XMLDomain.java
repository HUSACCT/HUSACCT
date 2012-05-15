package husacct.define.persitency;

import husacct.define.domain.Application;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.SoftwareUnitDefinition.Type;
import husacct.define.domain.module.Component;
import husacct.define.domain.module.ExternalLibrary;
import husacct.define.domain.module.Layer;
import husacct.define.domain.module.Module;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Element;

public class XMLDomain {

	private Element workspace;

	public XMLDomain(Element workspaceData) {
		this.workspace = workspaceData;
	}

	public Element getWorkspaceData() {
		return this.workspace;
	}

	private List<Element> getWorkspaceChildren() {
		return this.workspace.getChildren();
	}

	public Application getApplication() {
		List<Element> applicationProperties = this.getWorkspaceChildren();

	    Element ApName = (Element)applicationProperties.get(0);
	    Element ApLanguage = (Element)applicationProperties.get(1);
	    Element ApVersion = (Element)applicationProperties.get(2);

	    Application XMLAp = new Application(ApName.getText(), ApLanguage.getText());
	    XMLAp.setVersion(ApVersion.getText());
	    XMLAp.setArchitecture( this.getArchitecture() );

	    return XMLAp;
	}

	public SoftwareArchitecture getArchitecture() {
		List<Element> applicationProperties = this.getWorkspaceChildren();
		Element ApArchitecture = (Element)applicationProperties.get(3);
    	
    	Element ArchitectureName = (Element)ApArchitecture.getChild("name");
    	Element ArchitectureDescription = (Element)ApArchitecture.getChild("description");
    	
    	SoftwareArchitecture XMLsa = new SoftwareArchitecture(ArchitectureName.getText(), ArchitectureDescription.getText());
    	XMLsa.setAppliedRules(this.getAppliedRules(ApArchitecture));
    	
    	Element ArchitectureModuleRoot  = ApArchitecture.getChild("modules");
    	if (ArchitectureModuleRoot != null) {
	    	List<Element> ArchitectureModules = ArchitectureModuleRoot.getChildren("Module");
	    	// kijken of er modules beschikbaar zijn.. if yes; bouwen!
	    	if (ArchitectureModules.size() > 0) {
	    		Iterator moduleIterator = ArchitectureModules.iterator();
	    		while (moduleIterator.hasNext()){ 
	    			Object o = moduleIterator.next();

	    			if (o instanceof Element) {
	    				XMLsa.addModule(this.getModuleFromXML((Element) o));
	    			}
	    		}
	    	}
    	
	    	XMLsa.setModules(this.getModules(ArchitectureModules));
    	}
    	
    	return XMLsa;
	}

	public ArrayList<AppliedRule> getAppliedRules(Element ApplicationArchitecture) {
		Element AppliedRulesRoot = ApplicationArchitecture.getChild("rules");
		ArrayList<AppliedRule> ruleList = new ArrayList<AppliedRule>();
		if (AppliedRulesRoot != null) {
    		List<Element> AppliedRules = AppliedRulesRoot.getChildren("AppliedRule");
    		for (int i = 0; i < AppliedRules.size(); i++) {
    			ruleList.add( this.getAppliedRuleFromXML((Element)AppliedRules.get(i)));
    		}
    	}

		return ruleList;
	}

	public ArrayList<Module> getModules(List<Element> modules) {		
		ArrayList<Module> returnList = new ArrayList<Module>();

		for (int i = 0; i < modules.size(); i++) {
			Element theModule = (Element)modules.get(i);
			returnList.add(this.getModuleFromXML(theModule));
		}

		return returnList;
	}

	public AppliedRule getAppliedRuleFromXML(Element e) {
		Element ruleDescription = e.getChild("description");
		Element ruleRegex = e.getChild("regex");
		Element ruleId = e.getChild("id");
		Element ruleType = e.getChild("type");
		Element ruleUsedModule = e.getChild("usedmodule").getChild("Module");
		Module usedModule = ruleUsedModule == null ? new Module() : this.getModuleFromXML(ruleUsedModule);
		Element ruleRestrictedModule = e.getChild("restrictedmodule").getChild("Module");
		Module restrictedModule = ruleRestrictedModule == null ? new Module() : this.getModuleFromXML(ruleRestrictedModule);
		Element ruleExceptions = e.getChild("exceptions");
		Element ruleEnabled = e.getChild("enabled");
		Element ruleDependencies = e.getChild("dependencies");

		ArrayList<String> dependencies = new ArrayList<String>();
		List<Element> ruleDependencyList = ruleDependencies.getChildren("dependency");
		Iterator DependencyIterator = ruleDependencyList.iterator();
		while (DependencyIterator.hasNext()) {
			Object o = DependencyIterator.next();
			if (o instanceof Element) {
				dependencies.add(((Element) o).getValue());
			}
		}

		Boolean enabled = true;
		if (ruleEnabled.getValue().toLowerCase().equals("false")) {
			enabled = false;
		}

		ArrayList<AppliedRule> ExceptionsArray = new ArrayList<AppliedRule>();
		List<Element> ExceptionList = ruleExceptions.getChildren("AppliedRule");
		Iterator ExceptionIterator = ExceptionList.iterator();
		while (ExceptionIterator.hasNext()) {
			Object o = ExceptionIterator.next();
			if (o instanceof Element) {
				ExceptionsArray.add(this.getAppliedRuleFromXML((Element) o));
			}
		}

		AppliedRule AppliedXMLRule = new AppliedRule(ruleType.getValue(), ruleDescription.getValue(), dependencies.toArray(new String[dependencies.size()]), ruleRegex.getValue(), usedModule, restrictedModule, enabled);
		AppliedXMLRule.setId(Integer.parseInt(ruleId.getValue()));
		AppliedXMLRule.setExceptions(ExceptionsArray);

		return AppliedXMLRule;
	}

	public Module getModuleFromXML(Element e) {
		Element ModuleType = e.getChild("type");
		String ModuleTypeText = ModuleType.getText().toLowerCase();
		Module xmlModule;

		String moduleName = e.getChild("name").getValue();
		String moduleDescription = e.getChild("description").getValue();

		// type detection..
		if (ModuleTypeText.equals("externallibrary")) {
			xmlModule = new ExternalLibrary(moduleName, moduleDescription);
		} else if (ModuleTypeText.equals("component")) {
			xmlModule = new Component(moduleName, moduleDescription);
		} else if (ModuleTypeText.equals("layer")) {
			xmlModule = new Layer(moduleName, moduleDescription, Integer.parseInt(e.getChild("HierarchicalLevel").getValue()));
		} else {
			xmlModule = new Module(moduleName, moduleDescription);
		}

		Element SoftwareUnitDefinitions = e.getChild("SoftwareUnitDefinitions");
		if (SoftwareUnitDefinitions != null) {
			List<Element> SoftwareUnitDefinitionsList = SoftwareUnitDefinitions.getChildren("SoftwareUnitDefinition");
			Iterator SUDIterator = SoftwareUnitDefinitionsList.iterator();
    		while (SUDIterator.hasNext()){ 
    			Object o = SUDIterator.next();
    			
    			if (o instanceof Element) {
    				xmlModule.addSUDefinition(this.getSoftwareUnitDefinitionFromXML((Element) o));
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
					xmlModule.addSubModule( this.getModuleFromXML((Element) o ));
				}
			}
			
		}

		return xmlModule;
	}

	public SoftwareUnitDefinition getSoftwareUnitDefinitionFromXML(Element e) {
		Element SUDName = e.getChild("name");
		Element SUDType = e.getChild("type");
		Type SoftwareUnitDefinitionType;

		if (SUDType.getValue().toUpperCase().equals("CLASS")) {
			SoftwareUnitDefinitionType = Type.CLASS;
		} else if (SUDType.getValue().toUpperCase().equals("METHOD")) {
			SoftwareUnitDefinitionType = Type.METHOD;
		} else {
			SoftwareUnitDefinitionType = Type.PACKAGE;
		}

		return new SoftwareUnitDefinition(SUDName.getText(), SoftwareUnitDefinitionType);
	}
}