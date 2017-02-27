package husacct.define.domain.services;

import husacct.ServiceProvider;
import husacct.common.dto.RuleTypeDTO;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleFactory;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import org.apache.log4j.Logger;

public class DefaultRuleDomainService {

	private ModuleStrategy _module;
	private RuleTypeDTO[] defaultRuleTypeDTOs = null;
	private ArrayList<AppliedRuleStrategy> defaultRules = new ArrayList<>();
	private AppliedRuleFactory factory = new AppliedRuleFactory();
	public static DefaultRuleDomainService instance;
    private final Logger logger = Logger.getLogger(DefaultRuleDomainService.class);


	public static DefaultRuleDomainService getInstance() {
		return (instance == null) ? instance = new DefaultRuleDomainService()
				: instance;
	}

	public void addDefaultRules(ModuleStrategy newModule) {
		_module = newModule;
		retrieveRuleTypeDTOsByModule();
		generateRules();
		saveDefaultRules();
	}

	private void retrieveRuleTypeDTOsByModule() {
		try{
			if(!_module.getType().equals("Root")){
				String type = _module.getType();
				defaultRuleTypeDTOs = ServiceProvider.getInstance().getValidateService().getDefaultRuleTypesOfModule(type);
			}
        } catch (Exception e) {
	        this.logger.error(new Date().toString() + " DefaultRuleTypes not retrieved correctly from Validate: "  + e );
	        //e.printStackTrace();
        }
	}

	private void generateRules() {
		if (defaultRuleTypeDTOs.length > 0) {
			for (RuleTypeDTO rule : defaultRuleTypeDTOs) {
				generateRule(rule);
			}
		}
	}

	private void generateRule(RuleTypeDTO ruleType) {
		AppliedRuleStrategy newRule = factory.createRule(ruleType.getKey());
		newRule.setAppliedRule(
				"This is a default rule for this type of module.\n"
						+ ruleType.getDescriptionKey(), _module, _module);
		newRule.setEnabled(true);
		defaultRules.add(newRule);
	}

	
	private void saveDefaultRules() {
		for (AppliedRuleStrategy defaultRule : defaultRules) {
		SoftwareArchitecture.getInstance().addAppliedRule(defaultRule);
		}
	}

	public boolean isMandatoryRule(AppliedRuleStrategy rule) {
		_module = rule.getModuleFrom();
		retrieveRuleTypeDTOsByModule();
		for (RuleTypeDTO ruleType : defaultRuleTypeDTOs) {
			if (rule.getRuleTypeKey().equals(ruleType.getKey())) {
				return true;
			}
		}
		return false;
	}

	// TODO: Needs to be revised when Validate is working (loop through list
	// from the validate service)
	public boolean isMandatoryRule(String ruleTypeKey, ModuleStrategy moduleFrom) {
		if (ruleTypeKey.equals("IsNotAllowedToMakeSkipCall")
				|| ruleTypeKey.equals("IsNotAllowedToMakeBackCall")) {
			if (Objects.equals(moduleFrom.getType(), "Layer")) {
				return true;
			}
		} else if (ruleTypeKey.equals("VisibilityConvention")) {
			if (moduleFrom.getType().equals("Component")
					|| moduleFrom.getType().equals("ExternalLibrary")) {
				return true;
			}
		} else if (ruleTypeKey.equals("FacadeConvention")) {
			if (moduleFrom.getType().equals("Component")) {
				return true;
			}
		}
		return false;
	}

	public AppliedRuleStrategy[] generateLayerModuleRules() {
		return null;
	}

	public void removeDefaultRules(ModuleStrategy module) {
		_module = module;
		retrieveRuleTypeDTOsByModule();

		ArrayList<Long> appliedRuleIds = new ArrayList<>();
		if (defaultRuleTypeDTOs.length > 0) {
			for (RuleTypeDTO rule : defaultRuleTypeDTOs) {
				for (AppliedRuleStrategy appliedRule : SoftwareArchitecture.getInstance().getAppliedRules()) {
					if (appliedRule.getModuleFrom().getId() == _module.getId()	&& rule.getKey().equals(appliedRule.getRuleTypeKey())) {
						appliedRuleIds.add(appliedRule.getId());
					}
				}
			}
			for (Long appliedRuleid : appliedRuleIds) {
				SoftwareArchitecture.getInstance().removeAppliedRule(appliedRuleid);
			}
		}
	}

	public void updateModuleRules(ModuleStrategy updatedModule) {
		_module = updatedModule;
		for (AppliedRuleStrategy appliedRule : SoftwareArchitecture
				.getInstance().getAppliedRules()) {
			if (appliedRule.getModuleFrom().getId() == _module.getId()) {
				appliedRule.setModuleFrom(_module);
			}
		}
	}

}
