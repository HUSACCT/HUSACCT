package husacct.define.task.persistency;



import husacct.ServiceProvider;
import husacct.common.dto.ProjectDTO;
import husacct.common.savechain.ISaveable;
import husacct.define.domain.Application;
import husacct.define.domain.Project;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.services.AppliedRuleDomainService;
import husacct.define.domain.services.ModuleDomainService;
import husacct.define.domain.services.SoftwareArchitectureDomainService;

import java.util.ArrayList;

import org.jdom2.Element;

/**
 * This class enabled the feature to have the domain stored in XML format
 */
public class PersistentDomain implements ISaveable {

	public enum DomainElement {
		APPLICATION, LOGICAL, PHYSICAL
	}

	private DomainXML domainToXMLParser;
	private XMLDomain xmlToDomainParser;
	private SoftwareArchitectureDomainService domainService;
	private ModuleDomainService moduleService;
	private AppliedRuleDomainService appliedRuleService;
	private DomainElement parseData = DomainElement.APPLICATION;
	private Application workspaceApplication;

	public PersistentDomain(SoftwareArchitectureDomainService ds, ModuleDomainService ms, AppliedRuleDomainService ards) {
		domainService = ds;
		moduleService = ms;
		appliedRuleService = ards;
	}

	@Override
	public Element getWorkspaceData() {
		domainToXMLParser = new DomainXML(SoftwareArchitecture.getInstance());

		switch(parseData){
			case LOGICAL:
				domainToXMLParser.setParseLogical(false);
				return domainToXMLParser.getApplicationInXML(domainService.getApplicationDetails());
			case APPLICATION:
			case PHYSICAL:
			default:
				return domainToXMLParser.getApplicationInXML(domainService.getApplicationDetails());
		}
	}

	@Override
	public void loadWorkspaceData(Element workspaceData) {
		resetWorkspaceData();
		xmlToDomainParser = new XMLDomain(workspaceData);
		workspaceApplication = xmlToDomainParser.createApplication();

		switch (parseData) {
			case LOGICAL:
			case APPLICATION:
			case PHYSICAL:
			default:
				ArrayList<ProjectDTO> projects = new ArrayList<ProjectDTO>();
				for (Project project : workspaceApplication.getProjects()) {
					projects.add(new ProjectDTO(project.getName(), project
							.getPaths(), project.getProgrammingLanguage(), project
							.getVersion(), project.getDescription(), null));
				}
			ServiceProvider.getInstance().getDefineService().createApplication(workspaceApplication.getName(), projects, workspaceApplication.getVersion());
		} 
	}

	/**
	 * Resets all workspace date prior to import.
	 */
	private void resetWorkspaceData() {
		appliedRuleService.removeAppliedRules();
		moduleService.removeAllModules();
		workspaceApplication = new Application();
	}

	public void setDefineDomainService(SoftwareArchitectureDomainService ds) {
		domainService = ds;
	}

	public void setParseData(DomainElement de) {
		parseData = de;
	}

	public void setXMLDomain(XMLDomain xd) {
		xmlToDomainParser = xd;
	}

}