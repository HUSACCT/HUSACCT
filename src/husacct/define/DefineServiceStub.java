package husacct.define;

import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;

import javax.swing.JFrame;

import org.jdom2.Element;

public class DefineServiceStub implements IDefineService{

	@Override
	public void createApplication(String name, String[] paths, String language, String version) {
		
	}
	
	@Override
	public RuleDTO[] getDefinedRules() {
		//Temporary architecture
		ModuleDTO lbDAOModule = new ModuleDTO();
		lbDAOModule.logicalPath = "InfrastructureLayer.locationbasedDAO";
		lbDAOModule.physicalPaths = new String[] {"infrastructure.socialmedia.locationbased.foursquare.AccountDAO",
				"infrastructure.socialmedia.locationbased.foursquare.FriendsDAO",
				"infrastructure.socialmedia.locationbased.foursquare.IMap",
				"infrastructure.socialmedia.locationbased.foursquare.HistoryDAO"};
		lbDAOModule.subModules = new ModuleDTO[]{};
		lbDAOModule.type = "Module";
		
		ModuleDTO latitudeModule = new ModuleDTO();
		latitudeModule.logicalPath = "DomainLayer.locationbasedConnections.latitudeConnection";
		latitudeModule.physicalPaths = new String[] {"domain.locationbased.latitude.Account",
				"domain.locationbased.latitude.Friends", "domain.locationbased.latitude.Map"};
		latitudeModule.subModules = new ModuleDTO[]{};
		latitudeModule.type = "Module";
		
		ModuleDTO fqConnectionModule = new ModuleDTO();
		fqConnectionModule.logicalPath = "DomainLayer.locationbasedConnections.foursquareConnection";
		fqConnectionModule.physicalPaths = new String[] {"domain.locationbased.foursquare.Account",
				"domain.locationbased.foursquare.Friends", "domain.locationbased.foursquare.Map"};
		fqConnectionModule.subModules = new ModuleDTO[]{};
		fqConnectionModule.type = "Module";
		
		ModuleDTO lbHistoryModule = new ModuleDTO();
		lbHistoryModule.logicalPath = "DomainLayer.locationbasedHistory";
		lbHistoryModule.physicalPaths = new String[] {"domain.locationbased.foursquare.History"};
		lbHistoryModule.subModules = new ModuleDTO[]{};
		lbHistoryModule.type = "Module";
		
		ModuleDTO lbConnectionsModule = new ModuleDTO();
		lbConnectionsModule.logicalPath = "DomainLayer.locationbasedConnections";
		lbConnectionsModule.physicalPaths = new String[] {};
		lbConnectionsModule.subModules = new ModuleDTO[]{fqConnectionModule, latitudeModule};
		lbConnectionsModule.type = "Module";
		
		ModuleDTO infrastructureLayer = new ModuleDTO();
		infrastructureLayer.logicalPath = "InfrastructureLayer";
		infrastructureLayer.subModules = new ModuleDTO[]{lbDAOModule};
		infrastructureLayer.type = "Layer";
		
		ModuleDTO domainLayer = new ModuleDTO();
		domainLayer.logicalPath = "DomainLayer";
		domainLayer.subModules = new ModuleDTO[]{lbConnectionsModule, lbHistoryModule};
		domainLayer.type = "Layer";
		
		//ACTUAL RULES
		//ACTUAL RULES
		RuleDTO ruleOne = new RuleDTO();
		ruleOne.ruleTypeKey = "IsNotAllowedToUse";
			//IGNORE FOR ELABORATION VERSION
			ruleOne.violationTypeKeys = new String[]{"InvocMethod", "InvocConstructor","ExtendsAbstract", "Implements"};
		ruleOne.moduleFrom = lbConnectionsModule;			
		ruleOne.moduleTo = lbDAOModule;
		ruleOne.exceptionRules = new RuleDTO[]{};

		RuleDTO ruleTwo = new RuleDTO();
		ruleTwo.ruleTypeKey = "IsNotAllowedToUse";		
			//IGNORE FOR ELABORATION VERSION
			ruleTwo.violationTypeKeys = new String[] {"ExtendsConcrete"};
		ruleTwo.moduleFrom = lbHistoryModule;
		ruleTwo.moduleTo = lbDAOModule;
		ruleOne.exceptionRules = new RuleDTO[]{};
		
		RuleDTO[] rules = new RuleDTO[]{ruleOne, ruleTwo};
		return rules;
	}

	@Override
	public ModuleDTO[] getRootModules() {			
		//Gets only the top level abstraction Modules
		
		ModuleDTO infrastructureLayer = new ModuleDTO();
		infrastructureLayer.logicalPath = "InfrastructureLayer";
		infrastructureLayer.subModules = new ModuleDTO[]{};
		infrastructureLayer.type = "Layer";
		
		ModuleDTO domainLayer = new ModuleDTO();
		domainLayer.logicalPath = "DomainLayer";
		domainLayer.subModules = new ModuleDTO[]{};
		domainLayer.type = "Layer";

		ModuleDTO[] allLayers = new ModuleDTO[]{domainLayer,infrastructureLayer};
		return allLayers;
	}

	@Override
	public ApplicationDTO getApplicationDetails() {
		ApplicationDTO application = new ApplicationDTO();
		application.name = "Application1";
		application.paths = new String[] {"c:/Application1/"};
		application.programmingLanguage = "Java";
		return application;
	}

	@Override
	public ModuleDTO[] getChildsFromModule(String logicalPath) {
		ModuleDTO lbHistoryModule = new ModuleDTO();
		lbHistoryModule.logicalPath = "DomainLayer.locationbasedHistory";
		lbHistoryModule.physicalPaths = new String[] {"domain.locationbased.foursquare.History"};
		lbHistoryModule.subModules = new ModuleDTO[]{};
		
		ModuleDTO lbConnectionsModule = new ModuleDTO();
		lbConnectionsModule.logicalPath = "DomainLayer.locationbasedConnections";
		lbConnectionsModule.physicalPaths = new String[] {};
		lbConnectionsModule.subModules = new ModuleDTO[]{};
		
		ModuleDTO[] subModules = new ModuleDTO[]{lbConnectionsModule,lbHistoryModule};
		return subModules;
	}

	@Override
	public String getParentFromModule(String logicalPath) {
		//returns parent from DomainLayer
		return "**";
		
		//another example:
		//parent from module: DomainLayer.locationbasedHistory would be
		//return "DomainLayer";
	}

	public JFrame getDefinedGUI(){
		return new JFrame();
	}
	
	public Element getLogicalArchitectureData(){
		//TODO: Implement in Construction I
		Element e = new Element("Root Element");
		return e;
	}

	public void loadLogicalArchitectureData(Element e){
		//TODO: Implement in Construction I
	}

	@Override
	public Element getWorkspaceData() {
		//TODO: Implement in Construction I
		Element e = new Element("Root Element");
		return e;
	}

	@Override
	public void loadWorkspaceData(Element workspaceData) {
		//TODO: Implement in Construction I
	}

	@Override
	public boolean isDefined() {
		return true;
	}

	@Override
	public boolean isMapped() {
		return true;
	}
}
