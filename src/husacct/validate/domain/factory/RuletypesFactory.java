package husacct.validate.domain.factory;

import husacct.define.DefineServiceStub;
import husacct.common.dto.ApplicationDTO;
import husacct.validate.domain.factory.violationtypeutil.CategorykeyClassDTO;
import husacct.validate.domain.ruletype.Rule;
import husacct.validate.domain.ruletype.RuleTypes;
import husacct.validate.domain.violationtype.ViolationType;
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

	public List<Rule> getRuleTypes(){
		ApplicationDTO application = new DefineServiceStub().getApplicationDetails();
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
	private List<Rule> generateRuleTypes(String language){
		setViolationTypeFactory("Java");
		//TODO uncomment when define service is ready
		//setViolationTypeFactory(language);

		List<Rule> rules = new ArrayList<Rule>();

		for(Entry<String, CategorykeyClassDTO> set : mainRuleTypes.entrySet()){
			Class<Rule> ruletypeClass = set.getValue().getRuleClass();
			String categoryKey = set.getValue().getCategoryKey();
			if(ruletypeClass != null){
				List<ViolationType> violationlist = Collections.emptyList();
				if(violationtypefactory != null){
					violationlist = violationtypefactory.createViolationTypesByRule(set.getKey());
				}

				Rule rule = generateRuleObject(ruletypeClass, set.getKey(), categoryKey, violationlist);
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

	public Rule generateRuleType(String ruleKey){
		setViolationTypeFactory("Java");
		//TODO uncomment when define service is ready
		//setViolationTypeFactory(language);

		CategorykeyClassDTO categoryKeyClass = allRuletypes.get(ruleKey);
		if(categoryKeyClass != null){
			Class<Rule> ruletypeClass = categoryKeyClass.getRuleClass();
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

	//Return all the default instances of Rule (no violationtypes)
	private List<Rule> generateDefaultRuleTypes(){
		List<Rule> rules = new ArrayList<Rule>();
		for(Entry<String, CategorykeyClassDTO> set : mainRuleTypes.entrySet()){
			Class<Rule> ruletypeClass = set.getValue().getRuleClass();
			String categoryKey = set.getValue().getCategoryKey();
			if(ruletypeClass != null){
				Rule rule = generateRuleObject(ruletypeClass, set.getKey(), categoryKey, new ArrayList<ViolationType>());
				if(rule!=null){
					rules.add(rule);
				}
			}
		}
		return rules;
	}

	private Rule generateRuleObject(Class<Rule> ruleClass, String key, String categoryKey, List<ViolationType> violationtypes){
		try {
			return (Rule) ruleClass.getConstructor(String.class, String.class, List.class).newInstance(key, categoryKey, violationtypes);
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
}