package husacct.validate;

import javax.swing.JInternalFrame;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.dto.ViolationTypeDTO;

public class ValidateServiceStub implements IValidateService{

	private ViolationTypeDTO constructorCall = new ViolationTypeDTO("InvocConstructor","InvocConstructorDescription", false);
	private ViolationTypeDTO extendingAbstractClass = new ViolationTypeDTO("Extends","ExtendsDescription", false);
	private ViolationTypeDTO implementationOfInterface = new ViolationTypeDTO("Implements","ImplementsDescription", false);
	private ViolationTypeDTO extendClass = new ViolationTypeDTO("Extends","ExtendsDescription", false);

	private RuleTypeDTO ruleType = new RuleTypeDTO("IsNotAllowedToUse","IsNotAllowedToUseDescription",
			new ViolationTypeDTO[] { constructorCall,extendingAbstractClass,implementationOfInterface,extendClass }, new RuleTypeDTO[] {});

	private CategoryDTO category = new CategoryDTO("Legality of Dependency", new RuleTypeDTO[] { ruleType });

	private ViolationDTO violation1 = new ViolationDTO("domain.locationbased.foursquare.Account", "infrastructure.socialmedia.locationbased.foursquare.AccountDAO", 
			"is not allowed", "Domain layer.locationbasedConnections.foursquareConnection", "Infrastructure layer.locationbasedDAO" ,constructorCall, ruleType);
	private ViolationDTO violation2 = new ViolationDTO("domain.locationbased.foursquare.Friends", "infrastructure.socialmedia.locationbased.foursquare.FriendsDAO",
			"is not allowed", "Domain layer.locationbasedConnections.foursquareConnection", "Infrastructure layer.locationbasedDAO",extendingAbstractClass, ruleType);
	private ViolationDTO violation3 = new ViolationDTO("domain.locationbased.foursquare.Map", "infrastructure.socialmedia.locationbased.foursquare.IMap",
			"is not allowed", "Domain layer.locationbasedConnections.latitudeConnection", "Infrastructure layer.locationbasedDAO",implementationOfInterface, ruleType);
	private ViolationDTO violation4 = new ViolationDTO("domain.locationbased.foursquare.History", "infrastructure.socialmedia.locationbased.foursquare.HistoryDAO ",
			"is not allowed", "Domain layer.locationbasedConnections.latitudeConnection", "Infrastructure layer.locationbasedDAO",extendClass, ruleType);

	public CategoryDTO[] getCategories() {
		return new CategoryDTO[] { category };
	}

	public String[] getExportExtentions() {
		return new String[] { "pdf", "xml", "html" };
	}
	
	public ViolationDTO[] getViolations(String logicalpathFrom, String logicalpathTo){
		//If you want to have the rules of one logicalmodule logicalpathFrom and logicalpathTo should be the same
		if(logicalpathFrom.equals("Domain layer") && logicalpathTo.contains("Infrastructure layer")){
			return new ViolationDTO[] { violation1,violation2,violation3,violation4 };
		}
		else if(logicalpathFrom.equals("Domain layer.locationbasedConnections") && logicalpathTo.contains("Infrastructure layer")){
			return new ViolationDTO[] { violation1,violation2,violation3};
		}
		else if(logicalpathFrom.equals("Domain layer.locationbasedConnections.foursquareConnection") && logicalpathTo.contains("Infrastructure layer")){
			return new ViolationDTO[] { violation1,violation2,violation3};
		}
		else if(logicalpathFrom.equals("Domain layer.locationbasedConnections.latitudeConnection") && logicalpathTo.contains("Infrastructure layer")){
			return new ViolationDTO[] { };
		}
		else if(logicalpathFrom.equals("Domain layer.locationbasedHistory") && logicalpathTo.contains("Infrastructure layer")){
			return new ViolationDTO[] { violation4};
		}
		else{
			return new ViolationDTO[] { };
		}
	}

	public void checkConformance() {

	}

	public void exportViolations(String name, String fileType, String path) {

	}
	
	public JInternalFrame getBrowseViolationsGUI(){
		return new JInternalFrame();
	}
}