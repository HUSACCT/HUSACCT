package husacct.define.persistency;

import org.jdom2.Element;
import husacct.common.savechain.ISaveable;
import husacct.define.domain.Application;
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
	public enum DomainElement {
		APPLICATION,
		LOGICAL,
		PHYSICAL
	}
	
	private SoftwareArchitectureDomainService domainService;
	private ModuleDomainService moduleService;
	private DomainXML domainParser;
	private XMLDomain XMLParser;
	private DomainElement parseData = DomainElement.APPLICATION;
	
	public PersistentDomain(SoftwareArchitectureDomainService ds, ModuleDomainService ms, AppliedRuleDomainService ards) {
		this.domainService = ds;
		this.moduleService = ms;
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

	@Override
	public Element getWorkspaceData() {
		this.domainParser = new DomainXML();
		switch (this.parseData){
			case LOGICAL:
				return this.domainParser.getApplicationInXML( this.domainService.getApplicationDetails() );
			case APPLICATION:
			case PHYSICAL:
			default:
				return this.domainParser.getApplicationInXML( this.domainService.getApplicationDetails() );
		}
	}

	@Override
	public void loadWorkspaceData(Element workspaceData) {
		// @todo test
		this.XMLParser = new XMLDomain(workspaceData);
		
		// load objects
		Application workspaceApplication = this.XMLParser.getApplication();
		SoftwareArchitecture workspaceArchitecture = this.XMLParser.getArchitecture();
		
		switch (this.parseData) {
			case LOGICAL:
			case APPLICATION:
			case PHYSICAL:
			default:
				// create application
				this.domainService.createApplication(workspaceApplication.getName(), workspaceApplication.getPaths(), workspaceApplication.getLanguage(), workspaceApplication.getVersion());
				// create architecture..
				this.domainService.createNewArchitectureDefinition(workspaceArchitecture.getName());
				// add modules
				for (Module m : workspaceArchitecture.getModules()) {
					this.moduleService.addModuleToRoot(m);
				}
		}		
	}
}
