package husacct.define;

import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.PhysicalPathDTO;
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
		PhysicalPathDTO[] physicalPaths = new PhysicalPathDTO[]{
				new PhysicalPathDTO("infrastructure.socialmedia.locationbased.foursquare.AccountDAO", "PACKAGE"),
				new PhysicalPathDTO("infrastructure.socialmedia.locationbased.foursquare.FriendsDAO", "PACKAGE"),
				new PhysicalPathDTO("infrastructure.socialmedia.locationbased.foursquare.IMap", "PACKAGE"),
				new PhysicalPathDTO("infrastructure.socialmedia.locationbased.foursquare.HistoryDAO", "PACKAGE")};
		String type = "Module";
		ModuleDTO[] subModules = new ModuleDTO[]{};
		ModuleDTO lbDAOModule = new ModuleDTO(logicalPath,physicalPaths, type, subModules);
		
		logicalPath = "DomainLayer.locationbasedConnections.latitudeConnection";
		physicalPaths = new PhysicalPathDTO[]{
				new PhysicalPathDTO("domain.locationbased.latitude.Account", "PACKAGE"),
				new PhysicalPathDTO("domain.locationbased.latitude.Friends", "PACKAGE"),
				new PhysicalPathDTO("domain.locationbased.latitude.Map", "PACKAGE")};
		type = "Module";
		subModules = new ModuleDTO[]{};
		ModuleDTO latitudeModule = new ModuleDTO(logicalPath,physicalPaths, type, subModules);
		
		logicalPath = "DomainLayer.locationbasedConnections.foursquareConnection";
		physicalPaths = new PhysicalPathDTO[]{
				new PhysicalPathDTO("domain.locationbased.foursquare.Account", "PACKAGE"),
				new PhysicalPathDTO("domain.locationbased.foursquare.Friends", "PACKAGE"),
				new PhysicalPathDTO("domain.locationbased.foursquare.Map", "PACKAGE")};
		type = "Module";
		subModules = new ModuleDTO[]{};
		ModuleDTO fqConnectionModule = new ModuleDTO(logicalPath,physicalPaths, type, subModules);
		
		logicalPath = "DomainLayer.locationbasedHistory";
		physicalPaths = new PhysicalPathDTO[]{
				new PhysicalPathDTO("domain.locationbased.foursquare.History", "PACKAGE")};
		type = "Module";
		subModules = new ModuleDTO[]{};
		ModuleDTO lbHistoryModule = new ModuleDTO(logicalPath,physicalPaths, type, subModules);
		
		logicalPath = "DomainLayer.locationbasedConnections";
		physicalPaths = new PhysicalPathDTO[]{};
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
		
		/*
		 * Actual Rules
		 */
		String ruleTypeKey = "IsNotAllowedToUse";
			String[] violationTypeKeys = new String[]{"InvocMethod", "InvocConstructor","ExtendsAbstract", "Implements"};
		ModuleDTO moduleFrom = lbConnectionsModule;			
		ModuleDTO moduleTo = lbDAOModule;
		String regex = "";
		RuleDTO[] exceptionRules = new RuleDTO[]{};
		RuleDTO ruleOne = new RuleDTO(ruleTypeKey,moduleFrom, moduleTo, violationTypeKeys, regex, exceptionRules);

		ruleTypeKey = "IsNotAllowedToUse";
			violationTypeKeys = new String[] {"ExtendsConcrete"};
		moduleFrom = lbHistoryModule;
		moduleTo = lbDAOModule;
		regex = "";
		exceptionRules = new RuleDTO[]{};
		RuleDTO ruleTwo = new RuleDTO(ruleTypeKey,moduleFrom, moduleTo, violationTypeKeys, regex,  exceptionRules);
		
		ruleTypeKey = "IsAllowedToUse";	
		violationTypeKeys = new String[] {"ExtendsConcrete"};
		moduleFrom = lbHistoryModule;
		moduleTo = lbDAOModule;
		regex = "";
		exceptionRules = new RuleDTO[]{};
		RuleDTO ruleThree = new RuleDTO(ruleTypeKey,moduleFrom, moduleTo, violationTypeKeys, regex,  exceptionRules);
	
		ruleTypeKey = "IsOnlyAllowedToUse";
		violationTypeKeys = new String[] {"ExtendsConcrete"};
		moduleFrom = lbHistoryModule;
		moduleTo = lbDAOModule;
		regex = "";
		exceptionRules = new RuleDTO[]{};
		RuleDTO ruleFour = new RuleDTO(ruleTypeKey,moduleFrom, moduleTo, violationTypeKeys, regex,  exceptionRules);
	
		ruleTypeKey = "IsOnlyModuleAllowedToUse";
		violationTypeKeys = new String[] {"ExtendsConcrete"};
		moduleFrom = lbHistoryModule;
		moduleTo = lbDAOModule;
		regex = "";
		exceptionRules = new RuleDTO[]{};
		RuleDTO ruleFive = new RuleDTO(ruleTypeKey,moduleFrom, moduleTo, violationTypeKeys, regex,  exceptionRules);
		
		ruleTypeKey = "MustUse";
		violationTypeKeys = new String[] {"ExtendsConcrete"};
		moduleFrom = lbHistoryModule;
		moduleTo = lbDAOModule;
		regex = "";
		exceptionRules = new RuleDTO[]{};
		RuleDTO ruleSix = new RuleDTO(ruleTypeKey,moduleFrom, moduleTo, violationTypeKeys, regex,  exceptionRules);
		
		ruleTypeKey = "BackCall";
		violationTypeKeys = new String[] {"ExtendsConcrete"};
		moduleFrom = domainLayer;
		moduleTo = new ModuleDTO();
		regex = "";
		exceptionRules = new RuleDTO[]{};
		RuleDTO ruleSeven = new RuleDTO(ruleTypeKey,moduleFrom, moduleTo, violationTypeKeys, regex,  exceptionRules);
		
		ruleTypeKey = "SkipCall";
		violationTypeKeys = new String[] {"ExtendsConcrete"};
		moduleFrom = domainLayer;
		moduleTo = new ModuleDTO();
		regex = "";
		exceptionRules = new RuleDTO[]{};
		RuleDTO ruleEight = new RuleDTO(ruleTypeKey,moduleFrom, moduleTo, violationTypeKeys, regex, exceptionRules);
	
		//Create a list of all rules
		RuleDTO[] rules = new RuleDTO[]{ruleOne, ruleTwo, ruleThree, ruleFour, ruleFive, ruleSix, ruleSeven, ruleEight};

		return rules;
	}

	@Override
	public ModuleDTO[] getRootModules() {			
		//Gets only the top level abstraction Modules
		
		String logicalPath = "InfrastructureLayer";
		PhysicalPathDTO[] physicalPaths = new PhysicalPathDTO[]{};
		String type = "Layer";
		ModuleDTO[] subModules = new ModuleDTO[]{};
		ModuleDTO infrastructureLayer = new ModuleDTO(logicalPath,physicalPaths, type, subModules);
		
		logicalPath = "DomainLayer";
		physicalPaths = new PhysicalPathDTO[]{};
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
	public ModuleDTO[] getChildrenFromModule(String tmplogicalPath) {
		String logicalPath = "DomainLayer.locationbasedHistory";
		PhysicalPathDTO[] physicalPaths = new PhysicalPathDTO[]{
				new PhysicalPathDTO("domain.locationbased.foursquare.History", "PACKAGE")};
		String type = "Module";
		ModuleDTO[] subModules = new ModuleDTO[]{};
		ModuleDTO lbHistoryModule = new ModuleDTO(logicalPath,physicalPaths, type, subModules);
		
		logicalPath = "DomainLayer.locationbasedConnections";
		physicalPaths = new PhysicalPathDTO[]{};
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
		Element e = new Element("Root Element");
		return e;
	}

	public void loadLogicalArchitectureData(Element e){
	}

	@Override
	public Element getWorkspaceData() {
		Element e = new Element("Root Element");
		return e;
	}

	@Override
	public void loadWorkspaceData(Element workspaceData) {
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
