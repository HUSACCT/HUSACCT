package husacct.define.domain.appliedrule;

import husacct.ServiceProvider;
import husacct.common.dto.CategoryDTO;
import husacct.define.domain.appliedrule.propertyrules.FacadeConventionRule;
import husacct.define.domain.appliedrule.propertyrules.InterfaceConventionRule;
import husacct.define.domain.appliedrule.propertyrules.NamingConventionExceptionRule;
import husacct.define.domain.appliedrule.propertyrules.NamingConventionRule;
import husacct.define.domain.appliedrule.propertyrules.SuperClassInheritanceConvention;
import husacct.define.domain.appliedrule.propertyrules.VisibilityConventionExceptionRule;
import husacct.define.domain.appliedrule.propertyrules.VisibilityConventionRule;
import husacct.define.domain.appliedrule.relationrules.IsAllowedToUseRule;
import husacct.define.domain.appliedrule.relationrules.IsNotAllowedToMakeBackCallRule;
import husacct.define.domain.appliedrule.relationrules.IsNotAllowedToMakeSkipCallRule;
import husacct.define.domain.appliedrule.relationrules.IsNotAllowedToUseRule;
import husacct.define.domain.appliedrule.relationrules.IsOnlyAllowedToUseRule;
import husacct.define.domain.appliedrule.relationrules.IsOnlyModuleAllowedToUseRule;
import husacct.define.domain.appliedrule.relationrules.MustUseRule;
import husacct.define.domain.services.ModuleDomainService;

import java.util.HashMap;

import org.apache.log4j.Logger;


public class AppliedRuleFactory {

	private static Logger logger = Logger.getLogger(AppliedRuleFactory.class);

	private static final String[] ruleTypes = new String[]{
		"IsNotAllowedToUse",
		"IsNotAllowedToMakeBackCall",
		"IsNotAllowedToMakeSkipCall",
		"IsAllowedToUse",
		"IsOnlyAllowedToUse",
		"IsOnlyModuleAllowedToUse",
		"MustUse",
		"NamingConvention",
		"NamingConventionException",
		"VisibilityConvention",
		"VisibilityConventionException",
		"InterfaceConvention",
		"SuperClassInheritanceConvention",
		"FacadeConvention"
	};

	private static final Class<?>[] ruleClasses = new Class[]{
		IsNotAllowedToUseRule.class,
		IsNotAllowedToMakeBackCallRule.class,
		IsNotAllowedToMakeSkipCallRule.class,
		IsAllowedToUseRule.class,
		IsOnlyAllowedToUseRule.class,
		IsOnlyModuleAllowedToUseRule.class,
		MustUseRule.class,
		NamingConventionRule.class,
		NamingConventionExceptionRule.class,
		VisibilityConventionRule.class,
		VisibilityConventionExceptionRule.class,
		InterfaceConventionRule.class,
		SuperClassInheritanceConvention.class,
		FacadeConventionRule.class
	};
	
	public CategoryDTO[] getCategories(){
		return ServiceProvider.getInstance().getValidateService().getCategories();
	}

	public String[] getRuletypeOptions(){
		return ruleTypes;
	}

	public AppliedRuleStrategy createRule(String choice){
		for(int i = 0; i < ruleTypes.length; i++){
			if(ruleTypes[i].equals(choice)) try{
				AppliedRuleStrategy newRule = (AppliedRuleStrategy)ruleClasses[i].newInstance();
				newRule.setRuleType(choice);
				return newRule;
			}catch (InstantiationException ex) {
				logger.error("Instantiation Error in RuleFactory: " + ex.toString());
			} catch (IllegalAccessException ex) {
				logger.error("Instantiation Error in RuleFactory: " + ex.toString());
			}
		}
		logger.error("Error in AppliedRuleFactory: Illegal choice: ");
		throw new IllegalArgumentException("Illegal choice");
	}
	public AppliedRuleStrategy createDummyRule(String choice){
		for(int i = 0; i < ruleTypes.length; i++){
			if(ruleTypes[i].equals(choice)) try{
				AppliedRuleStrategy newRule = (AppliedRuleStrategy)ruleClasses[i].newInstance();
				newRule.setRuleType(choice);
				newRule.setId(-1);
				return newRule;
			}catch (InstantiationException ex) {
				logger.error("Instantiation Error in RuleFactory: " + ex.toString());
			} catch (IllegalAccessException ex) {
				logger.error("Instantiation Error in RuleFactory: " + ex.toString());
			}
		}
		logger.error("Error in AppliedRuleFactory: Illegal choice: ");
		throw new IllegalArgumentException("Illegal choice");
	}
	
	public AppliedRuleStrategy createRuleWithModules(HashMap<String, Object> ruleDetails){
		ModuleDomainService mds = new ModuleDomainService();
		for(int i = 0; i < ruleTypes.length; i++){
			if(ruleTypes[i].equals(ruleDetails.get("ruleTypeKey"))) try{
				AppliedRuleStrategy newRule = (AppliedRuleStrategy)ruleClasses[i].newInstance();
				newRule.setRuleType(""+ruleDetails.get("ruleTypeKey"));
				newRule.setId(-1);
				newRule.setModuleFrom(mds.getModuleById((long) ruleDetails.get("moduleFromId")));
				if(Integer.parseInt(""+ruleDetails.get("moduleToId")) != -1)
					newRule.setModuleTo(mds.getModuleById((long) ruleDetails.get("moduleToId")));
				else
					newRule.setModuleTo(mds.getModuleById((long) ruleDetails.get("moduleFromId")));
				return newRule;
			}catch (InstantiationException ex) {
				logger.error("Instantiation Error in RuleFactory: " + ex.toString());
			} catch (IllegalAccessException ex) {
				logger.error("Instantiation Error in RuleFactory: " + ex.toString());
			}
		}
		logger.error("Error in AppliedRuleFactory: Illegal choice: ");
		throw new IllegalArgumentException("Illegal choice");
	}
}
