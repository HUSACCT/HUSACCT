package husaccttest.validate;

import static org.junit.Assert.*;
import husacct.ServiceProvider;
import husacct.common.dto.RuleTypeDTO;
import husacct.validate.IValidateService;

import org.junit.Test;

public class SemanticSupportOfLayersTest {

	@Test
	public void getValidateService() {
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		assertNotNull(validate);
	}

	@Test
	public void getAndPrintAllowedRueTypesOfModuleLayer() {
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		
		RuleTypeDTO[] allowedRuleTypes = validate.getAllowedRuleTypesOfModule("Layer");
		assertNotNull(allowedRuleTypes);
		
		System.out.println("printing allowed rule type list...");
		for (RuleTypeDTO allowedRuleType : allowedRuleTypes) {
			System.out.println("allowedRuleType.getKey : " + allowedRuleType.getKey());
		}
	}
	
	@Test
	public void getAndPrintDefaultRueTypesOfModuleLayer() {
		IValidateService validate = ServiceProvider.getInstance().getValidateService();
		
		RuleTypeDTO[] defaultRuleTypes = validate.getDefaultRuleTypesOfModule("Layer");
		assertNotNull(defaultRuleTypes);
		
		System.out.println("printing default rule type list...");
		for (RuleTypeDTO defaultRuleType : defaultRuleTypes) {
			System.out.println("defaultRuleType.getKey : " + defaultRuleType.getKey());
		}
	}
}