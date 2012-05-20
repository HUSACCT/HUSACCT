package husacct.validate;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.dto.ViolationTypeDTO;
import husacct.common.savechain.ISaveable;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;

import java.awt.Color;
import java.io.File;
import java.util.Calendar;
import java.util.List;

import javax.swing.JInternalFrame;

import org.jdom2.Element;

public class ValidateServiceStub implements IValidateService, ISaveable{

	private ViolationTypeDTO constructorCall = new ViolationTypeDTO("InvocConstructor","InvocConstructorDescription", false);
	private ViolationTypeDTO extendingAbstractClass = new ViolationTypeDTO("ExtendsAbstract","ExtendsDescription", false);
	private ViolationTypeDTO implementationOfInterface = new ViolationTypeDTO("Implements","ImplementsDescription", false);
	private ViolationTypeDTO extendClass = new ViolationTypeDTO("ExtendsConcrete","ExtendsDescription", false);

	private RuleTypeDTO ruleType = new RuleTypeDTO("IsNotAllowedToUse","IsNotAllowedToUseDescription",
			new ViolationTypeDTO[] { constructorCall,extendingAbstractClass,implementationOfInterface,extendClass }, new RuleTypeDTO[] {});

	private CategoryDTO category = new CategoryDTO("Legality of Dependency", new RuleTypeDTO[] { ruleType });

	private ViolationDTO violation1 = new ViolationDTO("domain.locationbased.foursquare.Account", "infrastructure.socialmedia.locationbased.foursquare.AccountDAO", "Domain layer.locationbasedConnections.foursquareConnection", "Infrastructure layer.locationbasedDAO" ,constructorCall, ruleType, "Module locationbasedConnections  is not allowed to use Module locationbasedDAO", 12, Color.GREEN, "", "Low", 1);
	private ViolationDTO violation2 = new ViolationDTO("domain.locationbased.foursquare.Friends", "infrastructure.socialmedia.locationbased.foursquare.FriendsDAO", "Domain layer.locationbasedConnections.foursquareConnection", "Infrastructure layer.locationbasedDAO",extendingAbstractClass, ruleType, "Module locationbasedConnections is not allowed to use Module locationbasedDAO", 18, Color.ORANGE, "Custom", "", 2);
	private ViolationDTO violation3 = new ViolationDTO("domain.locationbased.foursquare.Map", "infrastructure.socialmedia.locationbased.foursquare.IMap", "Domain layer.locationbasedConnections.latitudeConnection", "Infrastructure layer.locationbasedDAO",implementationOfInterface, ruleType, "Module locationbasedConnections is not allowed to use Module locationbasedDAO", 19, Color.RED, "", "High", 3);
	private ViolationDTO violation4 = new ViolationDTO("domain.locationbased.foursquare.History", "infrastructure.socialmedia.locationbased.foursquare.HistoryDAO ", "Domain layer.locationbasedConnections.latitudeConnection", "Infrastructure layer.locationbasedDAO",extendClass, ruleType, "Module locationbasedHistory is not allowed to use Module locationbasedDAO", 56, Color.RED, "Catastrophic", "", 4);

	private boolean validationExecuted;

	public ValidateServiceStub(){
		this.validationExecuted = false;
	}

	public CategoryDTO[] getCategories() {
		return new CategoryDTO[] { category };
	}

	public String[] getExportExtentions() {
		return new String[] { "pdf", "xml", "html" };
	}

	public void checkConformance() {
		validationExecuted = true;
	}

	public void exportViolations(String name, String fileType, String path) {

	}

	public JInternalFrame getBrowseViolationsGUI(){
		return new JInternalFrame();
	}

	@Override
	public JInternalFrame getConfigurationGUI() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isValidated() {
		return validationExecuted;
	}

	@Override
	public Element getWorkspaceData() {
		return new Element("validateStubElement");
	}

	@Override
	public void loadWorkspaceData(Element workspaceData) {
		this.validationExecuted = true;
	}

	@Override
	public ViolationDTO[] getViolationsByLogicalPath(String logicalpathFrom, String logicalpathTo) {
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

	@Override
	public ViolationDTO[] getViolationsByPhysicalPath(String physicalpathFrom, String physicalpathTo) {
		//If you want to have the rules of one logicalmodule logicalpathFrom and logicalpathTo should be the same
		if(physicalpathFrom.equals("Domain layer") && physicalpathTo.contains("Infrastructure layer")){
			return new ViolationDTO[] { violation1,violation2,violation3,violation4 };
		}
		else if(physicalpathFrom.equals("Domain layer.locationbasedConnections") && physicalpathTo.contains("Infrastructure layer")){
			return new ViolationDTO[] { violation1,violation2,violation3};
		}
		else if(physicalpathFrom.equals("Domain layer.locationbasedConnections.foursquareConnection") && physicalpathTo.contains("Infrastructure layer")){
			return new ViolationDTO[] { violation1,violation2,violation3};
		}
		else if(physicalpathFrom.equals("Domain layer.locationbasedConnections.latitudeConnection") && physicalpathTo.contains("Infrastructure layer")){
			return new ViolationDTO[] { };
		}
		else if(physicalpathFrom.equals("Domain layer.locationbasedHistory") && physicalpathTo.contains("Infrastructure layer")){
			return new ViolationDTO[] { violation4};
		}
		else{
			return new ViolationDTO[] { };
		}
	}

	@Override
	public List<Violation> getViolationsByDate(Calendar date) {
		return null;
	}

	@Override
	public Calendar[] getViolationHistoryDates() {
		Calendar date1 = Calendar.getInstance();
		date1.set(2012, 3, 28);

		Calendar date2 = Calendar.getInstance();	
		return new Calendar[] {date1, date2};
	}

	@Override
	public void saveInHistory(String description) {
	}

	@Override
	public JInternalFrame getViolationHistoryGUI() {
		return new JInternalFrame();
	}

	@Override
	public void exportViolations(File file, String fileType, Calendar date) {
		// TODO Auto-generated method stub
		
	}
}