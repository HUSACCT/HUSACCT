package husacct.validate.domain.configuration;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.define.IDefineService;
import husacct.validate.domain.exception.ProgrammingLanguageNotFoundException;
import husacct.validate.domain.factory.ruletype.RuleTypesFactory;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

class ActiveViolationTypesRepository {

	private final IAnalyseService analsyseService = ServiceProvider.getInstance().getAnalyseService();	
	private final IDefineService defineService = ServiceProvider.getInstance().getDefineService();
	private final RuleTypesFactory ruletypesfactory;
	private final Map<String, List<ActiveRuleType>> startupViolationTypes;
	private Map<String, List<ActiveRuleType>> currentActiveViolationTypes;


	ActiveViolationTypesRepository(RuleTypesFactory ruletypesfactory) {
		this.ruletypesfactory = ruletypesfactory;
		this.startupViolationTypes = initializeActiveViolationTypes();
		this.currentActiveViolationTypes = initializeActiveViolationTypes();
	}

	private Map<String, List<ActiveRuleType>> initializeActiveViolationTypes(){
		Map<String, List<ActiveRuleType>> activeViolationTypes = new HashMap<String, List<ActiveRuleType>>();

		for(String programmingLanguage : analsyseService.getAvailableLanguages()){
			List<ActiveRuleType> activeRuleTypes = new ArrayList<ActiveRuleType>();
			activeViolationTypes.put(programmingLanguage, activeRuleTypes);
			
			for(List<RuleType> ruleTypes : ruletypesfactory.getRuleTypes(programmingLanguage).values()){

				for(RuleType ruleType : ruleTypes){
					final String ruleTypeKey = ruleType.getKey();					
					List<ActiveViolationType> initialActiveViolationTypes = new ArrayList<ActiveViolationType>();

					for(ViolationType violationType : ruleType.getViolationTypes()){
						final String violationTypeKey = violationType.getViolationtypeKey();
						boolean enabled = violationType.isActive();
						ActiveViolationType activeViolationType = new ActiveViolationType(violationTypeKey, enabled);
						initialActiveViolationTypes.add(activeViolationType);
					}
					ActiveRuleType activeRuleType = new ActiveRuleType(ruleTypeKey);
					activeRuleType.setViolationTypes(initialActiveViolationTypes);
					activeRuleTypes.add(activeRuleType);
				}				
			}
		}		
		return activeViolationTypes;
	}

	boolean isEnabled(String ruleTypeKey, String violationTypeKey){
		final String currentLanguage = defineService.getApplicationDetails().programmingLanguage;
		return isEnabled(currentLanguage, ruleTypeKey, violationTypeKey);
	}

	boolean isEnabled(String programmingLanguage, String ruleTypeKey, String violationTypeKey){		
		List<ActiveRuleType> activeRuleTypes = this.currentActiveViolationTypes.get(programmingLanguage);
		if(activeRuleTypes != null){
			for(ActiveRuleType activeRuleType : activeRuleTypes){
				if(activeRuleType.getRuleType().toLowerCase().equals(ruleTypeKey.toLowerCase())){

					if(activeRuleType.getViolationTypes().isEmpty()){
						return false;
					}

					for(ActiveViolationType activeViolationType : activeRuleType.getViolationTypes()){
						if(activeViolationType.getType().toLowerCase().equals(violationTypeKey.toLowerCase())){
							return activeViolationType.isEnabled();
						}					
					}
				}
			}
		}
		else{
			throw new ProgrammingLanguageNotFoundException();
		}
		return false;
	}

	Map<String, List<ActiveRuleType>> getActiveViolationTypes() {
		return currentActiveViolationTypes;
	}

	void setActiveViolationTypes(Map<String, List<ActiveRuleType>> activeViolationTypes){
		for(Entry<String, List<ActiveRuleType>> activeViolationTypeSet : activeViolationTypes.entrySet()){
			if(programmingLanguageExists(activeViolationTypeSet.getKey())){
				setActiveViolationTypes(activeViolationTypeSet.getKey(), activeViolationTypes.get(activeViolationTypeSet.getKey()));
			}			
		}
	}

	void setActiveViolationTypes(String programmingLanguage , List<ActiveRuleType> newActiveViolationTypes) {
		if(programmingLanguageExists(programmingLanguage)){
			List<ActiveRuleType> checkedNewActiveViolationTypes = checkNewActiveViolationTypes(programmingLanguage, newActiveViolationTypes);

			if(currentActiveViolationTypes.containsKey(programmingLanguage)){
				currentActiveViolationTypes.remove(programmingLanguage);
				currentActiveViolationTypes.put(programmingLanguage, newActiveViolationTypes);
			}
			else{
				currentActiveViolationTypes.put(programmingLanguage, newActiveViolationTypes);
			}
		}
		else{
			throw new ProgrammingLanguageNotFoundException(programmingLanguage);
		}
	}

	private boolean programmingLanguageExists(String programmingLanguage){
		for(String language : startupViolationTypes.keySet()){
			if(language.toLowerCase().equals(programmingLanguage.toLowerCase())){
				return true;
			}
		}
		return false;
	}

	private List<ActiveRuleType> checkNewActiveViolationTypes(String programmingLanguage, List<ActiveRuleType> newActiveViolationTypes){
		List<ActiveRuleType> activeViolationTypesForLanguage = new ArrayList<ActiveRuleType>();
		for(ActiveRuleType newActiveRuleType : newActiveViolationTypes){
			if(ruleTypeKeyExists(programmingLanguage, newActiveRuleType.getRuleType())){

				List<ActiveViolationType> activeViolationTypes = new ArrayList<ActiveViolationType>();
				ActiveRuleType activeRuleType = new ActiveRuleType(newActiveRuleType.getRuleType());
				activeRuleType.setViolationTypes(activeViolationTypes);
				activeViolationTypesForLanguage.add(activeRuleType);

				for(ActiveViolationType newActiveViolationType : newActiveRuleType.getViolationTypes()){
					if(violationTypeKeyExists(programmingLanguage, newActiveRuleType.getRuleType(), newActiveViolationType.getType())){
						activeViolationTypes.add(new ActiveViolationType(newActiveViolationType.getType(), newActiveViolationType.isEnabled()));							
					}							
				}						
			}
		}
		return activeViolationTypesForLanguage;
	}

	private boolean ruleTypeKeyExists(String programmingLanguage, String ruleTypeKey){
		if(programmingLanguageExists(programmingLanguage)){

			for(ActiveRuleType activeRuleType : startupViolationTypes.get(programmingLanguage)){
				if(activeRuleType.getRuleType().toLowerCase().equals(ruleTypeKey.toLowerCase())){
					return true;
				}
			}
		}
		else{
			throw new ProgrammingLanguageNotFoundException(programmingLanguage);
		}
		return false;
	}

	private boolean violationTypeKeyExists(String programmingLanguage, String ruleTypeKey, String violationTypeKey){
		if(programmingLanguageExists(programmingLanguage) && ruleTypeKeyExists(programmingLanguage, ruleTypeKey)){

			for(ActiveRuleType activeRuleType : startupViolationTypes.get(programmingLanguage)){

				for(ActiveViolationType activeViolationType : activeRuleType.getViolationTypes()){
					if(activeViolationType.getType().toLowerCase().equals(violationTypeKey.toLowerCase())){
						return true;
					}
				}
			}
		}
		return false;
	}
}