package husacct.define.persistency;

import java.util.ArrayList;
import org.jdom2.Element;

import husacct.ServiceProvider;
import husacct.common.savechain.ISaveable;
import husacct.define.domain.Application;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.Module;
import husacct.define.domain.services.AppliedRuleDomainService;
import husacct.define.domain.services.ModuleDomainService;
import husacct.define.domain.services.SoftwareArchitectureDomainService;

/**
 * This class enabled the feature to have the domain stored in XML format
 * @author Dennis vd Waardenburg
 * @name PersistentDomain
 *
 */
public class PersistentDomain implements ISaveable {
	public enum DomainElement { APPLICATION, LOGICAL, PHYSICAL }
	
	private SoftwareArchitectureDomainService domainService;
	private ModuleDomainService moduleService;
	private AppliedRuleDomainService AppliedRuleService;
	
	private DomainXML domainParser;
	private XMLDomain XMLParser;
	private DomainElement parseData = DomainElement.APPLICATION;
	
	public PersistentDomain(SoftwareArchitectureDomainService ds, ModuleDomainService ms, AppliedRuleDomainService ards) {
		this.domainService = ds;
		this.moduleService = ms;
		this.AppliedRuleService = ards;
	}
	
	public void setDefineDomainService(SoftwareArchitectureDomainService ds) {
		this.domainService = ds;
	}
	
	public void setXMLDomain(XMLDomain xd) {
		this.XMLParser = xd;
	}

	public void setParseData(DomainElement de) {
		this.parseData = de;
	}
	
	/**
	 * Resets all workspace date prior to import.
	 */
	private void resetWorkspaceData() {	
		this.AppliedRuleService.removeAppliedRules();
		this.moduleService.removeAllModules();
	}

	@Override
	public Element getWorkspaceData() {
		this.domainParser = new DomainXML(SoftwareArchitecture.getInstance());
			
		switch (this.parseData){
			case LOGICAL:
				this.domainParser.setParseLogical(false);
				return this.domainParser.getApplicationInXML( this.domainService.getApplicationDetails() );
			case APPLICATION:
			case PHYSICAL:
			default:
				return this.domainParser.getApplicationInXML( this.domainService.getApplicationDetails() );
		}
	}

	@Override
	public void loadWorkspaceData(Element workspaceData) {
		this.resetWorkspaceData();
		
		this.XMLParser = new XMLDomain(workspaceData);	
		Application workspaceApplication = this.XMLParser.getApplication();
		SoftwareArchitecture workspaceArchitecture = this.XMLParser.getArchitecture();
		ArrayList<AppliedRule> AppliedRules = this.XMLParser.getAppliedRules();
		
		switch (this.parseData) {
			default:
				ServiceProvider.getInstance().getDefineService().createApplication(workspaceApplication.getName(), workspaceApplication.getPaths(), workspaceApplication.getLanguage(), workspaceApplication.getVersion());
				this.domainService.createNewArchitectureDefinition(workspaceArchitecture.getName());
				// add modules
				for (Module m : workspaceArchitecture.getModules()) {
					this.moduleService.addModuleToRoot(m);
				}
				for (AppliedRule ApplRule : AppliedRules) {
					this.AppliedRuleService.addAppliedRule(ApplRule.getRuleType(), ApplRule.getDescription(), ApplRule.getDependencies(), ApplRule.getRegex(), ApplRule.getModuleFrom().getId(), ApplRule.getModuleTo().getId(), ApplRule.isEnabled());
				}
		}		
	}
}
