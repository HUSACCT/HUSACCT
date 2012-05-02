package husacct.validate.domain.factory.violationtype.java;

import husacct.validate.domain.exception.ViolationTypeNotFoundException;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

public abstract class AbstractViolationType {
	private Logger logger = Logger.getLogger(AbstractViolationType.class);
	protected List<String> allViolationKeys;
	protected ViolationtypeGenerator generator;
	
	public abstract List<ViolationType> createViolationTypesByRule(String key);
	public abstract HashMap<String, List<ViolationType>> getAllViolationTypes();
	
	AbstractViolationType(){
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
	
	protected HashMap<String, List<ViolationType>> getAllViolationTypes(Map<String, String> violationTypeKeysAndCategories){
		HashMap<String, List<ViolationType>> categoryViolations = new HashMap<String, List<ViolationType>>();
		for(Entry<String, String> value : violationTypeKeysAndCategories.entrySet()){
			if(categoryViolations.containsKey(value.getValue())){
				List<ViolationType> violationtypes = categoryViolations.get(value.getValue());
				ViolationType violationtype = createViolationType(value.getKey());
				violationtypes.add(violationtype);
			}
			else{
				List<ViolationType> violationtypes = new ArrayList<ViolationType>();
				ViolationType violationtype = createViolationType(value.getKey());
				violationtypes.add(violationtype);	
				categoryViolations.put(value.getValue(), violationtypes);
			}
		}
		return categoryViolations;
	}
	
	public ViolationType createViolationType(String violationKey){
		List<String> violationKeysToLower = new ArrayList<String>();
		for(String violationtype : allViolationKeys){
			violationKeysToLower.add(violationtype.toLowerCase());
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