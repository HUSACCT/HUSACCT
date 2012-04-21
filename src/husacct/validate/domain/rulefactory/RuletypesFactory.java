package husacct.validate.domain.rulefactory;

import husacct.common.dto.ApplicationDTO;
import husacct.define.DefineServiceImpl;
import husacct.validate.domain.rulefactory.violationtypeutil.AbstractViolationType;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.iternal_tranfer_objects.CategorykeyClassDTO;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

//TODO exception rules;
//TODO remove duplicated code
public class RuletypesFactory {
	private AbstractViolationType violationtypefactory;
	private HashMap<String, CategorykeyClassDTO> allRuletypes;
	private HashMap<String, CategorykeyClassDTO> mainRuleTypes;

	public RuletypesFactory(){
		RuleTypesGenerator ruletypegenerator = new RuleTypesGenerator();
		allRuletypes = ruletypegenerator.generateAllRules();
		mainRuleTypes = ruletypegenerator.generateRules(RuleTypes.mainRuleTypes);
	}

	public List<RuleType> getRuleTypes(){
		ApplicationDTO application = new DefineServiceImpl().getApplicationDetails();
		if(application != null){
			if(application.programmingLanguage == null || application.programmingLanguage.equals("")){
				return generateDefaultRuleTypes();
			}
			else{
				return generateRuleTypes(application.programmingLanguage);
			}
		}
		return Collections.emptyList();
	}


	//Depending on the language give instance of Rule + violationtypes
	private List<RuleType> generateRuleTypes(String language){
		setViolationTypeFactory("Java");
		//TODO uncomment when define service is ready
		//setViolationTypeFactory(language);

		List<RuleType> rules = new ArrayList<RuleType>();

		for(Entry<String, CategorykeyClassDTO> set : mainRuleTypes.entrySet()){
			Class<RuleType> ruletypeClass = set.getValue().getRuleClass();
			String categoryKey = set.getValue().getCategoryKey();
			if(ruletypeClass != null){
				List<ViolationType> violationlist = Collections.emptyList();
				if(violationtypefactory != null){
					violationlist = violationtypefactory.createViolationTypesByRule(set.getKey());
				}

				RuleType rule = generateRuleObject(ruletypeClass, set.getKey(), categoryKey, violationlist);
				if(rule != null){
					rules.add(rule);
				}
			}
		}
		return rules;
	}

	private void setViolationTypeFactory(String language){
		this.violationtypefactory = new ViolationTypeFactory().getViolationTypeFactory();
	}

	public RuleType generateRuleType(String ruleKey){
		setViolationTypeFactory("Java");
		//TODO uncomment when define service is ready
		//setViolationTypeFactory(language);

		CategorykeyClassDTO categoryKeyClass = allRuletypes.get(ruleKey);
		if(categoryKeyClass != null){
			Class<RuleType> ruletypeClass = categoryKeyClass.getRuleClass();
			String categoryKey = categoryKeyClass.getCategoryKey();
			if(ruletypeClass != null){
				List<ViolationType> violationtypes = Collections.emptyList();
				if(violationtypefactory != null){
					violationtypes = violationtypefactory.createViolationTypesByRule(ruleKey);
				}
				return generateRuleObject(ruletypeClass, ruleKey, categoryKey, violationtypes);
			}
		}
		else{
			System.out.println("Key " + ruleKey + " does not exists");
		}
		return null;
	}

	//Return all the default instances of Rule
	private List<RuleType> generateDefaultRuleTypes(){
		List<RuleType> rules = new ArrayList<RuleType>();
		for(Entry<String, CategorykeyClassDTO> set : mainRuleTypes.entrySet()){
			Class<RuleType> ruletypeClass = set.getValue().getRuleClass();
			String categoryKey = set.getValue().getCategoryKey();
			if(ruletypeClass != null){
				RuleType rule = generateRuleObject(ruletypeClass, set.getKey(), categoryKey, new ArrayList<ViolationType>());
				if(rule!=null){
					rules.add(rule);
				}
			}
		}
		return rules;
	}

	private RuleType generateRuleObject(Class<RuleType> ruleClass, String key, String categoryKey, List<ViolationType> violationtypes){
		try {
			RuleType rootRule = (RuleType) ruleClass.getConstructor(String.class, String.class, List.class).newInstance(key, categoryKey, violationtypes);
			List<RuleType> exceptionRuletypes = new ArrayList<RuleType>();
			for(RuleTypes ruletype : rootRule.getExceptionRuleKeys()){				
				exceptionRuletypes.add(generateRuleTypeWithoutExceptionRules(ruletype.toString()));
			}
			rootRule.setExceptionrules(exceptionRuletypes);
			return rootRule;
		} catch (IllegalArgumentException e) {
			System.out.println(e.toString());
		} catch (SecurityException e) {
			System.out.println(e.toString());
		} catch (InstantiationException e) {
			System.out.println(e.toString());
		} catch (IllegalAccessException e) {
			System.out.println(e.toString());
		} catch (InvocationTargetException e) {
			System.out.println(e.toString());
		} catch (NoSuchMethodException e) {
			System.out.println(e.toString());
		}
		return null;
	}
	
	private RuleType generateRuleObjectWithoutExceptionRules(Class<RuleType> ruleClass, String key, String categoryKey, List<ViolationType> violationtypes){
		try {
			return (RuleType) ruleClass.getConstructor(String.class, String.class, List.class).newInstance(key, categoryKey, violationtypes);
		} catch (IllegalArgumentException e) {
			System.out.println(e.toString());
		} catch (SecurityException e) {
			System.out.println(e.toString());
		} catch (InstantiationException e) {
			System.out.println(e.toString());
		} catch (IllegalAccessException e) {
			System.out.println(e.toString());
		} catch (InvocationTargetException e) {
			System.out.println(e.toString());
		} catch (NoSuchMethodException e) {
			System.out.println(e.toString());
		}
		return null;
	}
	
	private RuleType generateRuleTypeWithoutExceptionRules(String ruleKey){
		setViolationTypeFactory("Java");
		//TODO uncomment when define service is ready
		//setViolationTypeFactory(language);

		CategorykeyClassDTO categoryKeyClass = allRuletypes.get(ruleKey);
		if(categoryKeyClass != null){
			Class<RuleType> ruletypeClass = categoryKeyClass.getRuleClass();
			String categoryKey = categoryKeyClass.getCategoryKey();
			if(ruletypeClass != null){
				List<ViolationType> violationtypes = Collections.emptyList();
				if(violationtypefactory != null){
					violationtypes = violationtypefactory.createViolationTypesByRule(ruleKey);
				}
				return generateRuleObjectWithoutExceptionRules(ruletypeClass, ruleKey, categoryKey, violationtypes);
			}
		}
		else{
			System.out.println("Key " + ruleKey + " does not exists");
		}
		return null;
	}
}