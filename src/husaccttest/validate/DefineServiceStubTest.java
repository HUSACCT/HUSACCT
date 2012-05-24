package husaccttest.validate;

import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleDTO;

import javax.swing.JInternalFrame;

import org.jdom2.Element;

public class DefineServiceStubTest {

	public void createApplication(String name, String[] paths, String language,String version) {

	}
	public RuleDTO[] getDefinedRulesWithException() {
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
		RuleDTO exceptionRule = new RuleDTO("IsAllowedToUse", lbDAOModule, fqConnectionModule, new String[] {"ExtendsConcrete"}, "",new RuleDTO[]{});
		RuleDTO ruleOne = new RuleDTO("IsNotAllowedToUse", lbDAOModule, lbConnectionsModule, new String[]{"InvocMethod", "InvocConstructor","ExtendsAbstract", "Implements"}, "",new RuleDTO[]{exceptionRule});

		RuleDTO ruleTwo = new RuleDTO("IsNotAllowedToUse", lbDAOModule, lbHistoryModule, new String[] {"ExtendsConcrete"}, "",new RuleDTO[]{});
				
		RuleDTO[] rules = new RuleDTO[]{ruleOne, ruleTwo};
		return rules;
	}
	public RuleDTO[] getDefinedRulesScenarioOne() {
		// Temporary architecture
		ModuleDTO lbDAOModule = new ModuleDTO();
		lbDAOModule.logicalPath = "InfrastructureLayer.locationbasedDAO";
		lbDAOModule.physicalPaths = new String[] {
				"infrastructure.socialmedia.locationbased.foursquare.AccountDAO",
				"infrastructure.socialmedia.locationbased.foursquare.FriendsDAO",
				"infrastructure.socialmedia.locationbased.foursquare.IMap",
				"infrastructure.socialmedia.locationbased.foursquare.HistoryDAO" };
		lbDAOModule.subModules = new ModuleDTO[] {};
		lbDAOModule.type = "Module";

		ModuleDTO latitudeModule = new ModuleDTO();
		latitudeModule.logicalPath = "DomainLayer.locationbasedConnections.latitudeConnection";
		latitudeModule.physicalPaths = new String[] {
				"domain.locationbased.latitude.Account",
				"domain.locationbased.latitude.Friends",
				"domain.locationbased.latitude.Map" };
		latitudeModule.subModules = new ModuleDTO[] {};
		latitudeModule.type = "Module";

		ModuleDTO fqConnectionModule = new ModuleDTO();
		fqConnectionModule.logicalPath = "DomainLayer.locationbasedConnections.foursquareConnection";
		fqConnectionModule.physicalPaths = new String[] {
				"domain.locationbased.foursquare.Account",
				"domain.locationbased.foursquare.Friends",
				"domain.locationbased.foursquare.Map" };
		fqConnectionModule.subModules = new ModuleDTO[] {};
		fqConnectionModule.type = "Module";

		ModuleDTO lbHistoryModule = new ModuleDTO();
		lbHistoryModule.logicalPath = "DomainLayer.locationbasedHistory";
		lbHistoryModule.physicalPaths = new String[] { "domain.locationbased.foursquare.History" };
		lbHistoryModule.subModules = new ModuleDTO[] {};
		lbHistoryModule.type = "Module";

		ModuleDTO lbConnectionsModule = new ModuleDTO();
		lbConnectionsModule.logicalPath = "DomainLayer.locationbasedConnections";
		lbConnectionsModule.physicalPaths = new String[] {};
		lbConnectionsModule.subModules = new ModuleDTO[] { fqConnectionModule,
				latitudeModule };
		lbConnectionsModule.type = "Module";

		ModuleDTO infrastructureLayer = new ModuleDTO();
		infrastructureLayer.logicalPath = "InfrastructureLayer";
		infrastructureLayer.subModules = new ModuleDTO[] { lbDAOModule };
		infrastructureLayer.type = "Layer";
		infrastructureLayer.physicalPaths = new String[] {};

		ModuleDTO domainLayer = new ModuleDTO();
		domainLayer.logicalPath = "DomainLayer";
		domainLayer.subModules = new ModuleDTO[] { lbConnectionsModule};
		domainLayer.type = "Layer";
		domainLayer.physicalPaths = new String[] {};

		ModuleDTO abstractInfrastructureLayer = new ModuleDTO();
		abstractInfrastructureLayer.logicalPath = "AbstractInfrastructureLayer";
		abstractInfrastructureLayer.subModules = new ModuleDTO[] {};
		abstractInfrastructureLayer.type = "Layer";
		abstractInfrastructureLayer.physicalPaths = new String[] {};

		// ACTUAL RULES
		// ACTUAL RULES
		RuleDTO ruleOne = new RuleDTO("MustUse", lbHistoryModule, lbConnectionsModule, new String[] { "InvocMethod", "InvocConstructor", "ExtendsAbstract", "Implements" }, "",new RuleDTO[] {});

		RuleDTO ruleTwo = new RuleDTO("IsOnlyModuleAllowedToUse", lbDAOModule, lbHistoryModule, new String[] { "ExtendsConcrete" }, "",new RuleDTO[] {});

		RuleDTO ruleThree = new RuleDTO("SkipCall", new ModuleDTO(), domainLayer, new String[] { "ExtendsConcrete" }, "",new RuleDTO[] {});

		RuleDTO[] rules = new RuleDTO[] { ruleOne, ruleTwo, ruleThree };
		return rules;
	}
	
	public RuleDTO[] getDefinedRulesScenarioTwo() {
		// Temporary architecture
		ModuleDTO lbDAOModule = new ModuleDTO();
		lbDAOModule.logicalPath = "InfrastructureLayer.locationbasedDAO";
		lbDAOModule.physicalPaths = new String[] {
				"infrastructure.socialmedia.locationbased.foursquare.AccountDAO",
				"infrastructure.socialmedia.locationbased.foursquare.FriendsDAO",
				"infrastructure.socialmedia.locationbased.foursquare.IMap",
				"infrastructure.socialmedia.locationbased.foursquare.HistoryDAO" };
		lbDAOModule.subModules = new ModuleDTO[] {};
		lbDAOModule.type = "Module";

		ModuleDTO latitudeModule = new ModuleDTO();
		latitudeModule.logicalPath = "DomainLayer.locationbasedConnections.latitudeConnection";
		latitudeModule.physicalPaths = new String[] {
				"domain.locationbased.latitude.Account",
				"domain.locationbased.latitude.Friends",
				"domain.locationbased.latitude.Map" };
		latitudeModule.subModules = new ModuleDTO[] {};
		latitudeModule.type = "Module";

		ModuleDTO fqConnectionModule = new ModuleDTO();
		fqConnectionModule.logicalPath = "DomainLayer.locationbasedConnections.foursquareConnection";
		fqConnectionModule.physicalPaths = new String[] {
				"domain.locationbased.foursquare.Account",
				"domain.locationbased.foursquare.Friends",
				"domain.locationbased.foursquare.Map" };
		fqConnectionModule.subModules = new ModuleDTO[] {};
		fqConnectionModule.type = "Module";

		ModuleDTO lbHistoryModule = new ModuleDTO();
		lbHistoryModule.logicalPath = "DomainLayer.locationbasedHistory";
		lbHistoryModule.physicalPaths = new String[] { "domain.locationbased.foursquare.History" };
		lbHistoryModule.subModules = new ModuleDTO[] {};
		lbHistoryModule.type = "Module";

		ModuleDTO lbConnectionsModule = new ModuleDTO();
		lbConnectionsModule.logicalPath = "DomainLayer.locationbasedConnections";
		lbConnectionsModule.physicalPaths = new String[] {};
		lbConnectionsModule.subModules = new ModuleDTO[] { fqConnectionModule,
				latitudeModule };
		lbConnectionsModule.type = "Module";

		ModuleDTO infrastructureLayer = new ModuleDTO();
		infrastructureLayer.logicalPath = "InfrastructureLayer";
		infrastructureLayer.subModules = new ModuleDTO[] { lbConnectionsModule };
		infrastructureLayer.type = "Layer";
		infrastructureLayer.physicalPaths = new String[] {};

		// ACTUAL RULES
		// ACTUAL RULES
		RuleDTO ruleOne = new RuleDTO("IsOnlyAllowedToUse", lbDAOModule, lbConnectionsModule, new String[] { "InvocMethod", "InvocConstructor", "ExtendsAbstract", "Implements" }, "",new RuleDTO[] {});

		RuleDTO ruleTwo = new RuleDTO("BackCall", new ModuleDTO(), infrastructureLayer, new String[] { "ExtendsConcrete" }, "",new RuleDTO[] {});

		RuleDTO[] rules = new RuleDTO[] { ruleOne, ruleTwo };
		return rules;
	}

	public ModuleDTO[] getRootModules() {
		// Gets only the top level abstraction Modules
		ModuleDTO infrastructureLayer = new ModuleDTO();
		infrastructureLayer.logicalPath = "InfrastructureLayer";
		infrastructureLayer.subModules = new ModuleDTO[] {};
		infrastructureLayer.type = "Layer";
		infrastructureLayer.physicalPaths = new String[] {};

		ModuleDTO domainLayer = new ModuleDTO();
		domainLayer.logicalPath = "DomainLayer";
		domainLayer.subModules = new ModuleDTO[] {};
		domainLayer.type = "Layer";
		domainLayer.physicalPaths = new String[] {};

		ModuleDTO abstractInfrastructureLayer = new ModuleDTO();
		abstractInfrastructureLayer.logicalPath = "AbstractInfrastructureLayer";
		abstractInfrastructureLayer.subModules = new ModuleDTO[] {};
		abstractInfrastructureLayer.type = "Layer";
		abstractInfrastructureLayer.physicalPaths = new String[] {};

		ModuleDTO[] allLayers = new ModuleDTO[] { domainLayer,
				abstractInfrastructureLayer, infrastructureLayer };
		return allLayers;
	}

	public ApplicationDTO getApplicationDetails() {
		ApplicationDTO application = new ApplicationDTO("Application1", new String[] { "c:/Application1/" }, "Java", "0.1a");
		return application;
	}

	public ModuleDTO[] getSkipCallChildsFromModule(String logicalPath) {
		ModuleDTO[] subModules;
		if (logicalPath.equals("DomainLayer")) {

			ModuleDTO lbConnectionsModule = new ModuleDTO();
			lbConnectionsModule.logicalPath = "DomainLayer.locationbasedConnections";
			lbConnectionsModule.physicalPaths = new String[] {};
			lbConnectionsModule.subModules = new ModuleDTO[] {};

			subModules = new ModuleDTO[] { lbConnectionsModule };
		} else if (logicalPath.equals("DomainLayer.locationbasedConnections")) {

			ModuleDTO latitudeModule = new ModuleDTO();
			latitudeModule.logicalPath = "DomainLayer.locationbasedConnections.latitudeConnection";
			latitudeModule.physicalPaths = new String[] {
					"domain.locationbased.latitude.Account",
					"domain.locationbased.latitude.Friends",
					"domain.locationbased.latitude.Map" };
			latitudeModule.subModules = new ModuleDTO[] {};
			latitudeModule.type = "Module";

			ModuleDTO fqConnectionModule = new ModuleDTO();
			fqConnectionModule.logicalPath = "DomainLayer.locationbasedConnections.foursquareConnection";
			fqConnectionModule.physicalPaths = new String[] {
					"domain.locationbased.foursquare.Account",
					"domain.locationbased.foursquare.Friends",
					"domain.locationbased.foursquare.Map" };
			fqConnectionModule.subModules = new ModuleDTO[] {};
			fqConnectionModule.type = "Module";

			subModules = new ModuleDTO[] { latitudeModule, fqConnectionModule };
		} else if (logicalPath.equals("InfrastructureLayer")) {
			ModuleDTO lbDAOModule = new ModuleDTO();
			lbDAOModule.logicalPath = "InfrastructureLayer.locationbasedDAO";
			lbDAOModule.physicalPaths = new String[] {
					"infrastructure.socialmedia.locationbased.foursquare.AccountDAO",
					"infrastructure.socialmedia.locationbased.foursquare.FriendsDAO",
					"infrastructure.socialmedia.locationbased.foursquare.IMap",
					"infrastructure.socialmedia.locationbased.foursquare.HistoryDAO" };
			lbDAOModule.subModules = new ModuleDTO[] {};
			lbDAOModule.type = "Module";

			subModules = new ModuleDTO[] { lbDAOModule };
		} else if (logicalPath.equals("AbstractInfrastructureLayer")) {
			ModuleDTO lbHistoryModule = new ModuleDTO();
			lbHistoryModule.logicalPath = "DomainLayer.locationbasedHistory";
			lbHistoryModule.physicalPaths = new String[] { "domain.locationbased.foursquare.History" };
			lbHistoryModule.subModules = new ModuleDTO[] {};
			lbHistoryModule.type = "Module";

			subModules = new ModuleDTO[] { lbHistoryModule };
		} else {
			subModules = new ModuleDTO[] {};
		}

		return subModules;
	}

	public ModuleDTO[] getBackCallChildsFromModule(String logicalPath) {
		ModuleDTO[] subModules;
		if (logicalPath.equals("DomainLayer")) {
			ModuleDTO lbHistoryModule = new ModuleDTO();
			lbHistoryModule.logicalPath = "DomainLayer.locationbasedHistory";
			lbHistoryModule.physicalPaths = new String[] { "domain.locationbased.foursquare.History" };
			lbHistoryModule.subModules = new ModuleDTO[] {};
			lbHistoryModule.type = "Module";

			subModules = new ModuleDTO[] { lbHistoryModule };
		} else if (logicalPath.equals("DomainLayer.locationbasedConnections")) {

			ModuleDTO latitudeModule = new ModuleDTO();
			latitudeModule.logicalPath = "DomainLayer.locationbasedConnections.latitudeConnection";
			latitudeModule.physicalPaths = new String[] {
					"domain.locationbased.latitude.Account",
					"domain.locationbased.latitude.Friends",
					"domain.locationbased.latitude.Map" };
			latitudeModule.subModules = new ModuleDTO[] {};
			latitudeModule.type = "Module";

			ModuleDTO fqConnectionModule = new ModuleDTO();
			fqConnectionModule.logicalPath = "DomainLayer.locationbasedConnections.foursquareConnection";
			fqConnectionModule.physicalPaths = new String[] {
					"domain.locationbased.foursquare.Account",
					"domain.locationbased.foursquare.Friends",
					"domain.locationbased.foursquare.Map" };
			fqConnectionModule.subModules = new ModuleDTO[] {};
			fqConnectionModule.type = "Module";

			subModules = new ModuleDTO[] { latitudeModule, fqConnectionModule };
		} else if (logicalPath.equals("InfrastructureLayer")) {

			ModuleDTO lbConnectionsModule = new ModuleDTO();
			lbConnectionsModule.logicalPath = "DomainLayer.locationbasedConnections";
			lbConnectionsModule.physicalPaths = new String[] {};
			lbConnectionsModule.subModules = new ModuleDTO[] {};

			subModules = new ModuleDTO[] { lbConnectionsModule };
		} else if (logicalPath.equals("AbstractInfrastructureLayer")) {
			ModuleDTO lbDAOModule = new ModuleDTO();
			lbDAOModule.logicalPath = "InfrastructureLayer.locationbasedDAO";
			lbDAOModule.physicalPaths = new String[] {
					"infrastructure.socialmedia.locationbased.foursquare.AccountDAO",
					"infrastructure.socialmedia.locationbased.foursquare.FriendsDAO",
					"infrastructure.socialmedia.locationbased.foursquare.IMap",
					"infrastructure.socialmedia.locationbased.foursquare.HistoryDAO" };
			lbDAOModule.subModules = new ModuleDTO[] {};
			lbDAOModule.type = "Module";
			subModules = new ModuleDTO[] { lbDAOModule };
		} else {
			subModules = new ModuleDTO[] {};
		}

		return subModules;
	}

	public String getParentFromModule(String logicalPath) {
		// returns parent from DomainLayer
		return "**";

		// another example:
		// parent from module: DomainLayer.locationbasedHistory would be
		// return "DomainLayer";
	}

	public JInternalFrame getDefinedGUI() {
		return new JInternalFrame();
	}

	public Element getLogicalArchitectureData() {
		// TODO: Implement in Construction I
		Element e = new Element("Root Element");
		return e;
	}

	public void loadLogicalArchitectureData(Element e) {
		// TODO: Implement in Construction I
	}

	public Element getWorkspaceData() {
		// TODO: Implement in Construction I
		Element e = new Element("Root Element");
		return e;
	}

	public void loadWorkspaceData(Element workspaceData) {
		// TODO: Implement in Construction I
	}

	public boolean isDefined() {
		return true;
	}

	public boolean isMapped() {
		return true;
	}

	public ModuleDTO[] getChildsFromModule(String logicalPath) {
		// TODO Auto-generated method stub
		return null;
	}
}
