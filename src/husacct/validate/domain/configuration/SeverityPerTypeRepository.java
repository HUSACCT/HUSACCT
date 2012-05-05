package husacct.validate.domain.configuration;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.exception.SeverityNotFoundException;
import husacct.validate.domain.factory.ruletype.RuleTypesFactory;
import husacct.validate.domain.factory.violationtype.java.AbstractViolationType;
import husacct.validate.domain.factory.violationtype.java.ViolationTypeFactory;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class SeverityPerTypeRepository {
	private HashMap<String, HashMap<String, Severity>> severitiesPerTypePerProgrammingLanguage;
	private HashMap<String, HashMap<String, Severity>> defaultSeveritiesPerTypePerProgrammingLanguage;
	private final ConfigurationServiceImpl configuration;
	private final RuleTypesFactory ruletypefactory;
	private final IAnalyseService analsyseService = ServiceProvider.getInstance().getAnalyseService();
	private AbstractViolationType violationtypefactory;

	public SeverityPerTypeRepository(ConfigurationServiceImpl configuration){
		this.configuration = configuration;
		this.ruletypefactory = new RuleTypesFactory(configuration);		
		severitiesPerTypePerProgrammingLanguage = new HashMap<String, HashMap<String, Severity>>();
		defaultSeveritiesPerTypePerProgrammingLanguage = new HashMap<String, HashMap<String, Severity>>();
	}


	public void initializeDefaultSeverities() {		
		for(String programmingLanguage : analsyseService.getAvailableLanguages()){
			severitiesPerTypePerProgrammingLanguage.putAll(initializeDefaultSeverityForLanguage(programmingLanguage));
			defaultSeveritiesPerTypePerProgrammingLanguage.putAll(initializeDefaultSeverityForLanguage(programmingLanguage));
		}		
	}

	private HashMap<String, HashMap<String, Severity>> initializeDefaultSeverityForLanguage(String programmingLanguage){
		HashMap<String, HashMap<String, Severity>> severitiesPerTypePerProgrammingLanguage = new HashMap<String, HashMap<String, Severity>>();
		severitiesPerTypePerProgrammingLanguage.put(programmingLanguage, new HashMap<String, Severity>());
		HashMap<String, Severity> severityPerType = severitiesPerTypePerProgrammingLanguage.get(programmingLanguage);
		for(RuleType ruleType : ruletypefactory.getRuleTypes()){			
			severityPerType.put(ruleType.getKey(), ruleType.getSeverity());
		}

		this.violationtypefactory = new ViolationTypeFactory().getViolationTypeFactory(programmingLanguage ,configuration);
		if(violationtypefactory != null){				
			for(Entry<String, List<ViolationType>> violationTypeCategory : violationtypefactory.getAllViolationTypes().entrySet()){	
				for(ViolationType violationType : violationTypeCategory.getValue()){
					severityPerType.put(violationType.getViolationtypeKey(), violationType.getSeverity());
				}				
			}
		}
		return severitiesPerTypePerProgrammingLanguage;
	}


	public HashMap<String, HashMap<String, Severity>> getSeveritiesPerTypePerProgrammingLanguage() {
		return severitiesPerTypePerProgrammingLanguage;
	}

	public Severity getSeverity(String language, String key){
		HashMap<String, Severity> severityPerType = severitiesPerTypePerProgrammingLanguage.get(language);
		if(severityPerType == null){
			throw new SeverityNotFoundException();
		}
		else{
			Severity severity = severityPerType.get(key);
			if(severity == null){
				throw new SeverityNotFoundException();
			}
			else{
				return severity;
			}
		}
	}

	public void restoreDefaultSeverity(String language, String key){
		HashMap<String, Severity> severitiesPerType = severitiesPerTypePerProgrammingLanguage.get(language);

		//if there is no value, autmatically the default severities will be applied
		if(severitiesPerType!= null){
			Severity oldSeverity = severitiesPerType.get(key);
			if(oldSeverity != null){
				Severity defaultSeverity = getDefaultRuleKey(language, key);
				if(defaultSeverity != null){
					severitiesPerType.remove(key);
					severitiesPerType.put(key, defaultSeverity);
				}
			}
		}
	}

	private Severity getDefaultRuleKey(String language, String key){
		HashMap<String, Severity> severityPerType = defaultSeveritiesPerTypePerProgrammingLanguage.get(language);
		if(severityPerType == null){
			throw new SeverityNotFoundException();
		}
		else{
			Severity severity = severityPerType.get(key);
			if(severity == null){
				throw new SeverityNotFoundException();
			}
			else{
				return severity;
			}
		}
	}

	public void restoreAllToDefault(String programmingLanguage){
		initializeDefaultSeverityForLanguage(programmingLanguage);
	}

	public void setSeverityMap(HashMap<String, HashMap<String, Severity>> severitiesPerTypePerProgrammingLanguage){
		this.severitiesPerTypePerProgrammingLanguage = severitiesPerTypePerProgrammingLanguage;
	}

	public void setSeverityMap(String language, HashMap<String, Severity> severityMap) {
		HashMap<String, Severity> local = severitiesPerTypePerProgrammingLanguage.get(language);

		for(Entry<String, Severity> entry : severityMap.entrySet()){
			if(local.containsKey(entry.getKey())){
				local.remove(entry.getKey());
			}
			local.put(entry.getKey(), entry.getValue());
		}
		severitiesPerTypePerProgrammingLanguage.remove(language);
		severitiesPerTypePerProgrammingLanguage.put(language, local);
	}
}