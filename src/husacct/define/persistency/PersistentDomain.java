package husacct.define.persistency;

import java.util.ArrayList;
import org.jdom2.Element;

import husacct.ServiceProvider;
import husacct.common.dto.ProjectDTO;
import husacct.common.savechain.ISaveable;
import husacct.define.domain.Application;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.Project;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.Module;
import husacct.define.domain.services.AppliedRuleDomainService;
import husacct.define.domain.services.AppliedRuleExceptionDomainService;
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
	private AppliedRuleDomainService appliedRuleService;
	private AppliedRuleExceptionDomainService exceptionService;
	
	private DomainXML domainParser;
	private XMLDomain XMLParser;
	private DomainElement parseData = DomainElement.APPLICATION;
	
	public PersistentDomain(SoftwareArchitectureDomainService ds, ModuleDomainService ms, AppliedRuleDomainService ards, AppliedRuleExceptionDomainService ared) {
		domainService = ds;
		moduleService = ms;
		appliedRuleService = ards;
		exceptionService = ared;
	}
	
	public void setExceptionService(AppliedRuleExceptionDomainService ARED) {
		exceptionService = ARED;
	}
	
	public void setDefineDomainService(SoftwareArchitectureDomainService ds) {
		domainService = ds;
	}
	
	public void setXMLDomain(XMLDomain xd) {
		XMLParser = xd;
	}

	public void setParseData(DomainElement de) {
		parseData = de;
	}
	
	/**
	 * Resets all workspace date prior to import.
	 */
	private void resetWorkspaceData() {	
		appliedRuleService.removeAppliedRules();
		moduleService.removeAllModules();
	}

	@Override
	public Element getWorkspaceData() {
		domainParser = new DomainXML(SoftwareArchitecture.getInstance());
			
		switch (parseData){
			case LOGICAL:
				domainParser.setParseLogical(false);
				return domainParser.getApplicationInXML( domainService.getApplicationDetails() );
			case APPLICATION:
			case PHYSICAL:
			default:
				return domainParser.getApplicationInXML( domainService.getApplicationDetails() );
		}
	}

	@Override
	public void loadWorkspaceData(Element workspaceData) {
		resetWorkspaceData();
		
		XMLParser = new XMLDomain(workspaceData);	
		Application workspaceApplication = XMLParser.getApplication();
		SoftwareArchitecture workspaceArchitecture = XMLParser.getArchitecture();
		ArrayList<AppliedRule> AppliedRules = XMLParser.getAppliedRules();
		
		switch (parseData) {
			default:
				ArrayList<ProjectDTO> projects = new ArrayList<ProjectDTO>();
				for (Project project : workspaceApplication.getProjects()) {
					projects.add(new ProjectDTO(
							project.getName(),
							project.getPaths(),
							project.getProgrammingLanguage(),
							project.getVersion(),
							project.getDescription(),
							null
					));
				}
				ServiceProvider.getInstance().getDefineService().createApplication(workspaceApplication.getName(), projects, workspaceApplication.getVersion());
				domainService.createNewArchitectureDefinition(workspaceArchitecture.getName());
				for (Module m : workspaceArchitecture.getModules()) {
					long rootModule = moduleService.addModuleToRoot(m);
					if (m.getSubModules().size() > 0) {
						for (Module subModule : m.getSubModules()) {
							moduleService.addModuleToParent(rootModule, subModule);
						}
					}
				}
				for (AppliedRule ApplRule : AppliedRules) {
					long addedRule = appliedRuleService.addAppliedRule(ApplRule.getRuleType(), ApplRule.getDescription(), ApplRule.getDependencies(), ApplRule.getRegex(), ApplRule.getModuleFrom().getId(), ApplRule.getModuleTo().getId(), ApplRule.isEnabled());
					if (ApplRule.getExceptions().size() > 0) {
						for (AppliedRule Ap : ApplRule.getExceptions()) {
							exceptionService.addExceptionToAppliedRule(addedRule, Ap.getRuleType(), Ap.getDescription(), Ap.getModuleFrom().getId(), Ap.getModuleTo().getId(), Ap.getDependencies());
						}
					}
				}
		}		
	}
}
