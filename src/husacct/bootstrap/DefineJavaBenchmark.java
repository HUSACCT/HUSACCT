package husacct.bootstrap;

import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationTypeDTO;

import java.util.ArrayList;
import java.util.HashMap;


public class DefineJavaBenchmark extends AbstractBootstrap{
	private CategoryDTO[] categories = getValidateService().getCategories();

	@Override
	public void execute() {
		defineLogicalArchitecture();
		getControlService().getMainController().getMainGui().getMenu().getDefineMenu().getDefineArchitectureItem().doClick();
	}
	
	private void defineLogicalArchitecture(){
		defineLogicalModules();
		defineRules();
		getDefineService().getDefinitionController().notifyObservers();
	}
	
	private void defineLogicalModules(){
		getDefineService().getDefinitionController().addLayer(-1, "Presentation Layer", "This is the presentation layer of the benchmark");
		getDefineService().getDefinitionController().addLayer(-1, "Domain Layer", "This is the domain layer of the benchmark");
		getDefineService().getDefinitionController().addLayer(-1, "Infrastructure Layer", "This is the presentation layer of the benchmark");
		
		getDefineService().getDefinitionController().addSubSystem(-1, "Subsystem 1", "Subsystem used within the system.");
		getDefineService().getDefinitionController().addComponent(-1, "Component 1", "Component present in the system.");
		getDefineService().getDefinitionController().addExternalLibrary(-1, "External library", "External library used by the system.");
	}
	
	private void defineRules(){
		HashMap<String, Object> ruleDetails = new HashMap<String, Object>();
		
		//Presentation is not allowed to use Infrastructure
		ruleDetails.put("ruleTypeKey", "IsNotAllowedToUse");
		ruleDetails.put("moduleFromId", 0L);
		ruleDetails.put("moduleToId", 4L);
		ruleDetails.put("enabled", true);
		ruleDetails.put("description", "");
		ruleDetails.put("regex", "");
		ruleDetails.put("dependencies", getViolationTypeByRuleType("IsNotAllowedToUse"));
		getDefineService().getAppliedRuleController().save(ruleDetails);
		
		//Domain is not allowed to use Presentation
		ruleDetails.put("moduleFromId", 2L);
		ruleDetails.put("moduleToId", 0L);
		getDefineService().getAppliedRuleController().save(ruleDetails);
		
		//Infrastructure is not allowed to use Presentation
		ruleDetails.put("moduleFromId", 4L);
		ruleDetails.put("moduleToId", 0L);
		getDefineService().getAppliedRuleController().save(ruleDetails);
		
		//Infrastructure is not allowed to use Domain
		ruleDetails.put("moduleFromId", 4L);
		ruleDetails.put("moduleToId", 2L);
		getDefineService().getAppliedRuleController().save(ruleDetails);
	}
	
	private String[] getViolationTypeByRuleType(String ruleTypeKey){
		String[] violationTypes = new String[]{};
		for (CategoryDTO categorie : categories) {
			for (RuleTypeDTO ruleTypeDTO : categorie.ruleTypes){
				if (ruleTypeDTO.getKey().equals("ruleTypeKey")){
					ViolationTypeDTO[] dtos = ruleTypeDTO.getViolationTypes();
					ArrayList<String> violationTypeKeys = new ArrayList<String>();
					for (ViolationTypeDTO dto : dtos){
						violationTypeKeys.add(dto.key);
					}
					violationTypes = new String[violationTypeKeys.size()]; violationTypeKeys.toArray(violationTypes);
				}
			}
		}
		return violationTypes;
	}

}
