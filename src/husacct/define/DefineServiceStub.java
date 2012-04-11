package husacct.define;

import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;

public class DefineServiceStub implements IDefineService{

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
			ruleOne.violationTypeKeys = new String[]{"Invocation of a method/contructor","Extending an abstract class", "Implementing an interface"};
		ruleOne.moduleFrom = lbConnectionsModule;			
		ruleOne.moduleTo = lbDAOModule;
		ruleOne.exceptionRules = new RuleDTO[]{};

		RuleDTO ruleTwo = new RuleDTO();
		ruleTwo.ruleTypeKey = "IsNotAllowedToUse";		
			//IGNORE FOR ELABORATION VERSION
			ruleTwo.violationTypeKeys = new String[] {"Extending a class/struct"};
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

	public Document exportLogicalArchitecture() throws ParserConfigurationException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		  //Get the DocumentBuilder
		  DocumentBuilder parser = factory.newDocumentBuilder();
		  //Create blank DOM Document
		  Document doc = parser.newDocument();
		  return doc;
	}

	public void importLogicalArchitecture(Document doc){
		//TODO
	}
	
	public Document exportPhysicalArchitecture() throws ParserConfigurationException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		  //Get the DocumentBuilder
		  DocumentBuilder parser = factory.newDocumentBuilder();
		  //Create blank DOM Document
		  Document doc = parser.newDocument();
		  return doc;
	}
	
	public void importPhysicalArchitecture(Document doc) {
		//TODO
	}
	
	public JFrame getDefinedGUI(){
		return new JFrame();
	}

	@Override
	public void createApplication(String name, String[] paths, String language, String version) {
		//TODO
	}

}
