package husacct.define.domain.services;

import husacct.ServiceProvider;
import husacct.common.dto.RuleTypeDTO;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleFactory;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;

import java.util.ArrayList;

public class DefaultRuleDomainService {

	private ModuleStrategy _module;
	private RuleTypeDTO[] defaultRuleTypeDTOs = null;
	private ArrayList<AppliedRuleStrategy> defaultRules = new ArrayList<AppliedRuleStrategy>();
	private AppliedRuleFactory factory = new AppliedRuleFactory();
	public static DefaultRuleDomainService instance;

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
		if(!_module.getType().equals("Root")){
			defaultRuleTypeDTOs = ServiceProvider.getInstance().getValidateService().getDefaultRuleTypesOfModule(_module.getType());
			// When not bootstrapping, service returns nothing.
			if(defaultRuleTypeDTOs.length < 1){
				defaultRuleTypeDTOs = dirtyHack(_module.getType());
			}
		}		
	}

	public RuleTypeDTO[] dirtyHack(String moduleType) {
		ArrayList<RuleTypeDTO> returnhack = new ArrayList<RuleTypeDTO>();
		switch (moduleType) {
		case "SubSystem":
			;
			break;
		case "Layer":
			returnhack
					.add(new RuleTypeDTO(
							"IsNotAllowedToMakeSkipCall",
							"A layer should not access other layers other than the adjectent below",
							null, null));
			returnhack
					.add(new RuleTypeDTO("IsNotAllowedToMakeBackCall",
							"A layer should not access other layers above",
							null, null));
			break;
		case "Component":
			// returnhack.add(new RuleTypeDTO("VisibilityConvention",
			// "",null,null));
			// returnhack.add(new RuleTypeDTO("FacadeConvention",
			// "",null,null));
			break;
		case "ExternalLibrary":
			returnhack.add(new RuleTypeDTO("VisibilityConvention", "", null,
					null));

		}

		RuleTypeDTO[] _temp = new RuleTypeDTO[returnhack.size()];
		_temp = returnhack.toArray(_temp);
		return _temp;
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
			if (rule.getRuleType().equals(ruleType.getKey())) {
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
			if (moduleFrom.getType() == "Layer") {
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
					if (appliedRule.getModuleFrom().getId() == _module.getId()	&& rule.getKey().equals(appliedRule.getRuleType())) {
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
