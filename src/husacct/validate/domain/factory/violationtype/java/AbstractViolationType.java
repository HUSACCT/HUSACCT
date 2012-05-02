package husacct.validate.domain.factory.violationtype.java;

import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.exception.ViolationTypeNotFoundException;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.iternal_tranfer_objects.CategoryKeySeverityDTO;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

public abstract class AbstractViolationType {
	private Logger logger = Logger.getLogger(AbstractViolationType.class);
	protected List<CategoryKeySeverityDTO> allViolationKeys;
	protected ViolationtypeGenerator generator;
	
	public abstract List<ViolationType> createViolationTypesByRule(String key);
	public abstract HashMap<String, List<ViolationType>> getAllViolationTypes();
	
	AbstractViolationType(ConfigurationServiceImpl configuration){
		generator = new ViolationtypeGenerator();
	}

	protected List<ViolationType> generateViolationTypes(EnumSet<?> enums){
		List<ViolationType> violationtypes = new ArrayList<ViolationType>();
		for(Enum<?> enumValue : enums){
			ViolationType violationtype = generateViolationType(enumValue);
			violationtypes.add(violationtype);
		}
		return violationtypes;
	}
	
	protected HashMap<String, List<ViolationType>> getAllViolationTypes(List<CategoryKeySeverityDTO> keyList){
		HashMap<String, List<ViolationType>> categoryViolations = new HashMap<String, List<ViolationType>>();
		for(CategoryKeySeverityDTO dto : keyList){
			if(categoryViolations.containsKey(dto.getCategory())){
				List<ViolationType> violationtypes = categoryViolations.get(dto.getCategory());
				ViolationType violationtype = createViolationType(dto.getKey());
				violationtypes.add(violationtype);
			}
			else{
				List<ViolationType> violationtypes = new ArrayList<ViolationType>();
				ViolationType violationtype = createViolationType(dto.getKey());
				violationtypes.add(violationtype);	
				categoryViolations.put(dto.getCategory(), violationtypes);
			}
		}
		return categoryViolations;
	}
	
	public ViolationType createViolationType(String violationKey){
		List<String> violationKeysToLower = new ArrayList<String>();
		for(CategoryKeySeverityDTO violationtype : allViolationKeys){
			violationKeysToLower.add(violationtype.getKey().toLowerCase());
		}		

		if(violationKeysToLower.contains(violationKey.toLowerCase())){
			return new ViolationType(violationKey);
		}
		else{
			logger.warn(String.format("Warning specified %s not found in the system", violationKey));			
		}
		throw new ViolationTypeNotFoundException();
	}
	
	private ViolationType generateViolationType(Enum<?> enumValue){
		return new ViolationType(enumValue.toString());
	}
	
	protected boolean isCategoryLegalityOfDependency(String ruleKey){
		if(ruleKey.equals(RuleTypes.IS_NOT_ALLOWED.toString()) || ruleKey.equals(RuleTypes.IS_ALLOWED.toString()) || ruleKey.equals(RuleTypes.IS_NOT_ALLOWED.toString())||ruleKey.equals(RuleTypes.IS_ONLY_MODULE_ALLOWED.toString())||ruleKey.equals(RuleTypes.MUST_USE.toString())||ruleKey.equals(RuleTypes.BACK_CALL.toString())||ruleKey.equals(RuleTypes.SKIP_CALL.toString())){
			return true;
		}
		else return false;
	}
}