package husacct.define.task.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import husacct.ServiceProvider;
import husacct.common.dto.RuleDTO;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.AppliedRuleDomainService;
import husacct.define.domain.services.ModuleDomainService;

public abstract class ReportArchitectureAbstract {

	private ModuleDomainService moduleService;
	private AppliedRuleDomainService ruleService;

	public ReportArchitectureAbstract() {
		moduleService = new ModuleDomainService();
		ruleService = new AppliedRuleDomainService();
    }

	protected RuleDTO[] getAllRulesWithExceptions() {
		return ServiceProvider.getInstance().getDefineService().getDefinedRules();
	}
	
	protected List<ModuleStrategy> getRootModules() {
		ModuleStrategy[] rootModules = moduleService.getRootModules();
		List<ModuleStrategy> rootModuleList = new ArrayList<ModuleStrategy>();
		for(int i = 0; i < rootModules.length; i++) {
			rootModuleList.add(rootModules[i]);
		}
		return rootModuleList;
	}

	
	protected HashMap<String, Boolean> getAppliedRules(long moduleId){
		HashMap<String, Boolean> appliedRules = new HashMap<String, Boolean>();
		for(AppliedRuleStrategy rule : ruleService.getAllMainRules()){
			if(rule.getModuleFrom().getId() == moduleId){
				appliedRules.put(rule.getRuleTypeKey(), rule.isEnabled());
			}			
		}
		return appliedRules;
	}
	
	protected String convertIsIndirectBooleanToString(boolean isIndirect) {
		if (isIndirect) {
			return "direct";
		} else {
			return "indirect";
		}
	}

    public abstract void write(String path);

    protected String translate(String key) {
        return ServiceProvider.getInstance().getLocaleService().getTranslatedString(key);
    }

}