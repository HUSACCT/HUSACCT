package husacct.validate.domain.configuration;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.define.IDefineService;
import husacct.validate.domain.exception.ProgrammingLanguageNotFoundException;
import husacct.validate.domain.exception.RuleTypeNotFoundException;
import husacct.validate.domain.exception.ViolationTypeNotFoundException;
import husacct.validate.domain.factory.ruletype.RuleTypesFactory;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ActiveViolationTypesRepository {

	private final IAnalyseService analsyseService = ServiceProvider.getInstance().getAnalyseService();	
	private final IDefineService defineService = ServiceProvider.getInstance().getDefineService();
	private final RuleTypesFactory ruletypesfactory;
	private Map<String, List<ActiveRuleType>> activeViolationTypes;

	public ActiveViolationTypesRepository(RuleTypesFactory ruletypesfactory) {
		this.activeViolationTypes = new HashMap<String, List<ActiveRuleType>>();
		this.ruletypesfactory = ruletypesfactory;
		initializeActiveViolationTypes();
	}

	private void initializeActiveViolationTypes(){
		for(String programmingLanguage : analsyseService.getAvailableLanguages()){
			List<ActiveRuleType> activeRuleTypes = new ArrayList<ActiveRuleType>();
			this.activeViolationTypes.put(programmingLanguage, activeRuleTypes);
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
	}
	
	public boolean isEnabled(String ruleTypeKey, String violationTypeKey){
		final String currentLanguage = defineService.getApplicationDetails().programmingLanguage;
		return isEnabled(currentLanguage, ruleTypeKey, violationTypeKey);
	}

	public boolean isEnabled(String programmingLanguage, String ruleTypeKey, String violationTypeKey){		
		List<ActiveRuleType> activeRuleTypes = this.activeViolationTypes.get(programmingLanguage);
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

	public Map<String, List<ActiveRuleType>> getActiveViolationTypes() {
		return activeViolationTypes;
	}

	public void setActiveViolationTypes(String programmingLanguage , List<ActiveRuleType> newActiveViolationTypes) {
		if(this.activeViolationTypes.containsKey(programmingLanguage)){
			@SuppressWarnings("unused")
			List<ActiveRuleType> activeViolationTypesForLanguage = this.activeViolationTypes.get(programmingLanguage);
			activeViolationTypesForLanguage = newActiveViolationTypes;
		}
		else{
			this.activeViolationTypes.put(programmingLanguage, newActiveViolationTypes);
		}
	}

	public void setActiveViolationTypes(Map<String, List<ActiveRuleType>> activeViolationTypes){
		this.activeViolationTypes = activeViolationTypes;
	}
}