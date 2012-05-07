package husacct.define;

import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;

import javax.swing.JInternalFrame;

import org.jdom2.Element;

public class DefineServiceStub implements IDefineService{

	@Override
	public void createApplication(String name, String[] paths, String language, String version) {
		
	}
	
	@SuppressWarnings("unused")
	@Override
	public RuleDTO[] getDefinedRules() {
		//Temporary architecture
		String logicalPath = "InfrastructureLayer.locationbasedDAO";
		String[] physicalPaths = new String[] {"infrastructure.socialmedia.locationbased.foursquare.AccountDAO",
				"infrastructure.socialmedia.locationbased.foursquare.FriendsDAO",
				"infrastructure.socialmedia.locationbased.foursquare.IMap",
				"infrastructure.socialmedia.locationbased.foursquare.HistoryDAO"};
		String type = "Module";
		ModuleDTO[] subModules = new ModuleDTO[]{};
		ModuleDTO lbDAOModule = new ModuleDTO(logicalPath,physicalPaths, type, subModules);
		
		logicalPath = "DomainLayer.locationbasedConnections.latitudeConnection";
		physicalPaths = new String[] {"domain.locationbased.latitude.Account",
				"domain.locationbased.latitude.Friends", "domain.locationbased.latitude.Map"};
		type = "Module";
		subModules = new ModuleDTO[]{};
		ModuleDTO latitudeModule = new ModuleDTO(logicalPath,physicalPaths, type, subModules);
		
		logicalPath = "DomainLayer.locationbasedConnections.foursquareConnection";
		physicalPaths = new String[] {"domain.locationbased.foursquare.Account",
				"domain.locationbased.foursquare.Friends", "domain.locationbased.foursquare.Map"};
		type = "Module";
		subModules = new ModuleDTO[]{};
		ModuleDTO fqConnectionModule = new ModuleDTO(logicalPath,physicalPaths, type, subModules);
		
		logicalPath = "DomainLayer.locationbasedHistory";
		physicalPaths = new String[] {"domain.locationbased.foursquare.History"};
		type = "Module";
		subModules = new ModuleDTO[]{};
		ModuleDTO lbHistoryModule = new ModuleDTO(logicalPath,physicalPaths, type, subModules);
		
		logicalPath = "DomainLayer.locationbasedConnections";
		physicalPaths = new String[] {};
		subModules = new ModuleDTO[]{fqConnectionModule, latitudeModule};
		type = "Module";
		ModuleDTO lbConnectionsModule = new ModuleDTO(logicalPath,physicalPaths, type, subModules);
		
		logicalPath = "InfrastructureLayer";
		type = "Layer";
		subModules = new ModuleDTO[]{lbDAOModule};
		ModuleDTO infrastructureLayer = new ModuleDTO(logicalPath,physicalPaths, type, subModules);
		
		logicalPath = "DomainLayer";
		type = "Layer";
		subModules = new ModuleDTO[]{lbConnectionsModule, lbHistoryModule};
		ModuleDTO domainLayer = new ModuleDTO(logicalPath,physicalPaths, type, subModules);
		
		//ACTUAL RULES
		//ACTUAL RULES
		String ruleTypeKey = "IsNotAllowedToUse";
			//IGNORE FOR ELABORATION VERSION
			String[] violationTypeKeys = new String[]{"InvocMethod", "InvocConstructor","ExtendsAbstract", "Implements"};
		ModuleDTO moduleFrom = lbConnectionsModule;			
		ModuleDTO moduleTo = lbDAOModule;
		RuleDTO[] exceptionRules = new RuleDTO[]{};
		RuleDTO ruleOne = new RuleDTO(ruleTypeKey,moduleFrom, moduleTo, violationTypeKeys, exceptionRules);

		ruleTypeKey = "IsNotAllowedToUse";		
			//IGNORE FOR ELABORATION VERSION
			violationTypeKeys = new String[] {"ExtendsConcrete"};
		moduleFrom = lbHistoryModule;
		moduleTo = lbDAOModule;
		exceptionRules = new RuleDTO[]{};
		RuleDTO ruleTwo = new RuleDTO(ruleTypeKey,moduleFrom, moduleTo, violationTypeKeys, exceptionRules);
		
//		RuleDTO[] rules = new RuleDTO[]{ruleOne, ruleTwo};
		
		ruleTypeKey = "IsAllowedToUse";		
		//IGNORE FOR ELABORATION VERSION
		violationTypeKeys = new String[] {"ExtendsConcrete"};
		moduleFrom = lbHistoryModule;
		moduleTo = lbDAOModule;
		exceptionRules = new RuleDTO[]{};
		RuleDTO ruleThree = new RuleDTO(ruleTypeKey,moduleFrom, moduleTo, violationTypeKeys, exceptionRules);
	
		ruleTypeKey = "IsOnlyAllowedToUse";		
		//IGNORE FOR ELABORATION VERSION
		violationTypeKeys = new String[] {"ExtendsConcrete"};
		moduleFrom = lbHistoryModule;
		moduleTo = lbDAOModule;
		exceptionRules = new RuleDTO[]{};
		RuleDTO ruleFour = new RuleDTO(ruleTypeKey,moduleFrom, moduleTo, violationTypeKeys, exceptionRules);
	
		ruleTypeKey = "IsOnlyModuleAllowedToUse";		
		//IGNORE FOR ELABORATION VERSION
		violationTypeKeys = new String[] {"ExtendsConcrete"};
		moduleFrom = lbHistoryModule;
		moduleTo = lbDAOModule;
		exceptionRules = new RuleDTO[]{};
		RuleDTO ruleFive = new RuleDTO(ruleTypeKey,moduleFrom, moduleTo, violationTypeKeys, exceptionRules);
		
		ruleTypeKey = "MustUse";		
		//IGNORE FOR ELABORATION VERSION
		violationTypeKeys = new String[] {"ExtendsConcrete"};
		moduleFrom = lbHistoryModule;
		moduleTo = lbDAOModule;
		exceptionRules = new RuleDTO[]{};
		RuleDTO ruleSix = new RuleDTO(ruleTypeKey,moduleFrom, moduleTo, violationTypeKeys, exceptionRules);
		
		ruleTypeKey = "BackCall";		
		//IGNORE FOR ELABORATION VERSION
		violationTypeKeys = new String[] {"ExtendsConcrete"};
		moduleFrom = lbHistoryModule;
		moduleTo = new ModuleDTO();
		exceptionRules = new RuleDTO[]{};
		RuleDTO ruleSeven = new RuleDTO(ruleTypeKey,moduleFrom, moduleTo, violationTypeKeys, exceptionRules);
		
		ruleTypeKey = "SkipCall";		
		//IGNORE FOR ELABORATION VERSION
		violationTypeKeys = new String[] {"ExtendsConcrete"};
		moduleFrom = lbHistoryModule;
		moduleTo = new ModuleDTO();
		exceptionRules = new RuleDTO[]{};
		RuleDTO ruleEight = new RuleDTO(ruleTypeKey,moduleFrom, moduleTo, violationTypeKeys, exceptionRules);
	
		RuleDTO[] rules = new RuleDTO[]{ruleOne, ruleTwo, ruleThree, ruleFour, ruleFive, ruleSix, ruleSeven, ruleEight};

		return rules;
	}

	@Override
	public ModuleDTO[] getRootModules() {			
		//Gets only the top level abstraction Modules
		
		String logicalPath = "InfrastructureLayer";
		String[] physicalPaths = new String[]{};
		String type = "Layer";
		ModuleDTO[] subModules = new ModuleDTO[]{};
		ModuleDTO infrastructureLayer = new ModuleDTO(logicalPath,physicalPaths, type, subModules);
		
		logicalPath = "DomainLayer";
		physicalPaths = new String[]{};
		type = "Layer";
		subModules = new ModuleDTO[]{};
		ModuleDTO domainLayer = new ModuleDTO(logicalPath,physicalPaths, type, subModules);

		ModuleDTO[] allLayers = new ModuleDTO[]{domainLayer,infrastructureLayer};
		return allLayers;
	}

	@Override
	public ApplicationDTO getApplicationDetails() {
		String name = "Application1";
		String[] paths = new String[] {"c:/Application1/"};
		String programmingLanguage = "Java";
		String version = "1.0";
		ApplicationDTO application = new ApplicationDTO(name, paths, programmingLanguage, version);
		return application;
	}

	@Override
	public ModuleDTO[] getChildsFromModule(String tmplogicalPath) {
		String logicalPath = "DomainLayer.locationbasedHistory";
		String[] physicalPaths = new String[] {"domain.locationbased.foursquare.History"};
		String type = "Module";
		ModuleDTO[] subModules = new ModuleDTO[]{};
		ModuleDTO lbHistoryModule = new ModuleDTO(logicalPath,physicalPaths, type, subModules);
		
		logicalPath = "DomainLayer.locationbasedConnections";
		physicalPaths = new String[] {};
		type = "Module";
		subModules = new ModuleDTO[]{};
		ModuleDTO lbConnectionsModule = new ModuleDTO(logicalPath,physicalPaths, type, subModules);
		
		ModuleDTO[] childModules = new ModuleDTO[]{lbConnectionsModule,lbHistoryModule};
		return childModules;
	}

	@Override
	public String getParentFromModule(String logicalPath) {
		//returns parent from DomainLayer
		return "**";
		
		//another example:
		//parent from module: DomainLayer.locationbasedHistory would be
		//return "DomainLayer";
	}

	public JInternalFrame getDefinedGUI(){
		return new JInternalFrame();
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
