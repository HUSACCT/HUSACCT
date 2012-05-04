package husacct.validate.domain.configuration;

import husacct.analyse.AnalyseServiceStub;
import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.exception.DefaultSeverityNotFoundException;
import husacct.validate.domain.exception.RuleInstantionException;
import husacct.validate.domain.exception.RuleTypeNotFoundException;
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

import org.apache.log4j.Logger;

public class SeverityPerTypeRepository {
	private Logger logger = Logger.getLogger(SeverityPerTypeRepository.class);
	private HashMap<String, HashMap<String, Severity>> severitiesPerTypePerProgrammingLanguage;
	private final RuleTypesFactory ruletypefactory;	
	private final ConfigurationServiceImpl configuration;

	public SeverityPerTypeRepository(ConfigurationServiceImpl configuration){
		this.ruletypefactory = new RuleTypesFactory(configuration);
		this.configuration = configuration;

		severitiesPerTypePerProgrammingLanguage = new HashMap<String, HashMap<String, Severity>>();
		}


	public void initializeDefaultSeverities() {
		AnalyseServiceStub analyse = new AnalyseServiceStub();
		for(String programmingLanguage : analyse.getAvailableLanguages()){
			initializeDefaultSeverityForLanguage(programmingLanguage);
		}		
	}

	private void initializeDefaultSeverityForLanguage(String programmingLanguage){
		severitiesPerTypePerProgrammingLanguage.put(programmingLanguage, new HashMap<String, Severity>());
		for(Entry<String, List<RuleType>> entry : ruletypefactory.getRuleTypes(programmingLanguage).entrySet()){			
			HashMap<String, Severity> severityPerType = severitiesPerTypePerProgrammingLanguage.get(programmingLanguage);

			for(RuleType ruleType : entry.getValue()){					
				severityPerType.put(ruleType.getKey(), ruleType.getSeverity());

				for(ViolationType violationType : ruleType.getViolationTypes()){						
					severityPerType.put(violationType.getViolationtypeKey(), violationType.getSeverity());
				}
			}
		}
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
			System.out.println("here");
			Severity oldSeverity = severitiesPerType.get(key);
			System.out.println(oldSeverity);
			if(oldSeverity != null){
				Severity defaultSeverity = getDefaultRuleKey(language, key);
				System.out.println(defaultSeverity);
				if(defaultSeverity != null){
					severitiesPerType.remove(key);
					System.out.println("removed");
					severitiesPerType.put(key, defaultSeverity);
					System.out.println("added");
				}
			}
		}
	}

	private Severity getDefaultRuleKey(String language, String key){
		try{
			System.out.println(key);
			Severity severity = ruletypefactory.generateRuleType(key).getSeverity();
			System.out.println("yay");
			System.out.println(severity);
			return severity;
		}catch(RuleTypeNotFoundException e){
			AbstractViolationType violationtypefactory = new ViolationTypeFactory().getViolationTypeFactory(language, configuration);
			Severity severity = violationtypefactory.createViolationType(key).getSeverity();
			return severity;
		} catch (RuleInstantionException e) {
			logger.error(e.getMessage(), e);
		}	
		throw new DefaultSeverityNotFoundException();
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