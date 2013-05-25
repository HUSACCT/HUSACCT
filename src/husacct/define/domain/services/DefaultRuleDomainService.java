package husacct.define.domain.services;

import husacct.common.dto.RuleTypeDTO;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.Module;

import java.util.ArrayList;

public class DefaultRuleDomainService {
    public static DefaultRuleDomainService instance;

    public static DefaultRuleDomainService getInstance() {
	if (instance == null) {
	    return instance = new DefaultRuleDomainService();
	}
	return instance;
    }

    private Module _module;
    private ArrayList<AppliedRule> defaultRules = new ArrayList<AppliedRule>();

    private RuleTypeDTO[] defaultRuleTypeDTOs = null;

    public void addDefaultRules(Module newModule) {
	_module = newModule;
	retrieveRuleTypeDTOsByModule();
	generateRules();
	saveDefaultRules();
    }

    public void backCallRule(RuleTypeDTO rule) {
	AppliedRule backCallRule = getBaseRule();
	backCallRule.setRuleType("IsNotAllowedToMakeBackCall");
	backCallRule.setDescription(backCallRule.getDescription() + "\n"
		+ rule.getDescriptionKey());
	backCallRule.setModuleTo(_module);

	defaultRules.add(backCallRule);
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
	    returnhack.add(new RuleTypeDTO("Visibility", "", null, null));
	    returnhack.add(new RuleTypeDTO("FacadeConvention", "", null, null));
	    break;
	case "ExternalLibrary":
	    returnhack.add(new RuleTypeDTO("Visibility", "", null, null));
	}

	RuleTypeDTO[] _temp = new RuleTypeDTO[returnhack.size()];
	_temp = returnhack.toArray(_temp);
	return _temp;
    }

    // RuleTypes
    public void facadeRule(RuleTypeDTO rule) {
	AppliedRule facadeRule = getBaseRule();
	facadeRule.setRuleType("FacadeConvention");
	facadeRule.setDescription(facadeRule.getDescription() + "\n"
		+ rule.getDescriptionKey());
	facadeRule.setModuleTo(_module);

	defaultRules.add(facadeRule);
    }

    public AppliedRule[] generateLayerModuleRules() {
	return null;
    }

    private void generateRule(RuleTypeDTO ruleType) {
	switch (ruleType.getKey()) {
	case "Interface":
	    ;
	    break;
	case "Naming":
	    ;
	    break;
	case "FacadeConvention":
	    facadeRule(ruleType);
	    ;
	    break;
	case "SubClass":
	    ;
	    break;
	case "Visibility":
	    visibilityRule(ruleType);
	    break;
	case "Allowed":
	    ;
	    break;
	case "NotAllowed":
	    ;
	    break;
	case "IsNotAllowedToMakeSkipCall":
	    skipCallRule(ruleType);
	    break;
	case "IsNotAllowedToMakeBackCall":
	    backCallRule(ruleType);
	    break;
	case "OnlyAllowed":
	    ;
	    break;
	case "MustUse":
	    ;
	    break;
	default:
	    ;
	    break;
	}
    }

    private void generateRules() {
	if (defaultRuleTypeDTOs.length > 0) {
	    for (RuleTypeDTO rule : defaultRuleTypeDTOs) {
		generateRule(rule);
	    }
	}
    }

    public AppliedRule getBaseRule() {
	AppliedRule appliedRule = new AppliedRule();

	appliedRule
		.setDescription("This is a default rule for this type of module.");
	appliedRule.setModuleFrom(_module);
	appliedRule.setEnabled(true);

	return appliedRule;
    }

    public boolean isMandatoryRule(AppliedRule rule) {
	_module = rule.getModuleFrom();
	retrieveRuleTypeDTOsByModule();
	for (RuleTypeDTO ruleType : defaultRuleTypeDTOs) {
	    if (rule.getRuleType().equals(ruleType.getKey())) {
		return true;
	    }
	}
	return false;
    }

    public void removeDefaultRules(Module module) {
	_module = module;
	retrieveRuleTypeDTOsByModule();
	ArrayList<Long> appliedRuleIds = new ArrayList<>();
	if (defaultRuleTypeDTOs.length > 0) {
	    for (RuleTypeDTO rule : defaultRuleTypeDTOs) {
		for (AppliedRule appliedRule : SoftwareArchitecture
			.getInstance().getAppliedRules()) {
		    if (appliedRule.getModuleFrom().getId() == _module.getId()
			    && rule.getKey().equals(appliedRule.getRuleType())) {
			appliedRuleIds.add(appliedRule.getId());
		    }
		}
	    }
	    for (Long appliedRuleid : appliedRuleIds) {
		SoftwareArchitecture.getInstance().removeAppliedRule(
			appliedRuleid);
	    }
	}
    }

    private void retrieveRuleTypeDTOsByModule() {
	// defaultRuleTypeDTOs =
	// ServiceProvider.getInstance().getValidateService().getDefaultRuleTypesOfModule(_module.getType());
	defaultRuleTypeDTOs = dirtyHack(_module.getType());
    }

    private void saveDefaultRules() {
	for (AppliedRule defaultRule : defaultRules) {
	    SoftwareArchitecture.getInstance().addAppliedRule(defaultRule);
	}
    }

    public void skipCallRule(RuleTypeDTO rule) {
	AppliedRule skipCallRule = getBaseRule();
	skipCallRule.setRuleType("IsNotAllowedToMakeSkipCall");
	skipCallRule.setDescription(skipCallRule.getDescription() + "\n"
		+ rule.getDescriptionKey());

	skipCallRule.setModuleTo(_module);
	defaultRules.add(skipCallRule);
    }

    public void updateModuleRules(Module updatedModule) {
	_module = updatedModule;
	for (AppliedRule appliedRule : SoftwareArchitecture.getInstance()
		.getAppliedRules()) {
	    if (appliedRule.getModuleFrom().getId() == _module.getId()) {
		appliedRule.setModuleFrom(_module);
	    }
	}
    }

    private void visibilityRule(RuleTypeDTO rule) {
	AppliedRule visibilityRule = getBaseRule();
	visibilityRule.setRuleType("Visibility");
	visibilityRule.setDescription(visibilityRule.getDescription() + "\n"
		+ rule.getDescriptionKey());
	visibilityRule.setModuleTo(_module);

	defaultRules.add(visibilityRule);
    }
}
