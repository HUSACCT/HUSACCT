package husacct.define;

import husacct.ServiceProvider;
import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
import husacct.common.dto.CategoryDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationTypeDTO;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.AppliedRuleDomainService;
import husacct.define.domain.services.ModuleDomainService;
import husacct.validate.IValidateService;

import java.util.ArrayList;
import org.apache.log4j.Logger;

// Services for SAR: Software Architecture Reconstruction
public class DefineSarServiceImpl implements IDefineSarService {
	private DefineServiceImpl defineService;
	private AppliedRuleDomainService appliedRuleService;
	private DomainToDtoParser domainParser;
	private ModuleDomainService moduleService;
	private Logger logger = Logger.getLogger(DefineSarServiceImpl.class);


	public DefineSarServiceImpl(DefineServiceImpl defineService) {
		this.defineService = defineService;
		reset();
	}
	
	protected void reset() {
		appliedRuleService = new AppliedRuleDomainService();
		domainParser = new DomainToDtoParser();
		moduleService = new ModuleDomainService();
	}

	@Override
	public void addModule(String name, String parentLogicalPath, String moduleType, int hierarchicalLevel, ArrayList<SoftwareUnitDTO> softwareUnits) {
		try {
			moduleService.addModule(name, parentLogicalPath, moduleType, hierarchicalLevel, softwareUnits);
        } catch (Exception e) {
	        this.logger.warn(" Exception: "  + e );
        }
	}
	
	@Override
	public void editModule(String logicalPath, String newName, int newHierarchicalLevel, ArrayList<SoftwareUnitDTO> newSoftwareUnits) {
		try {
			moduleService.editModule(logicalPath, newName, newHierarchicalLevel, newSoftwareUnits);
        } catch (Exception e) {
	        this.logger.warn(" Exception: "  + e );
        }
	}
	
	@Override
	public void removeModule(String logicalPath) {
		try {
			moduleService.removeModuleById(moduleService.getModuleByLogicalPath(logicalPath).getId());
        } catch (Exception e) {
	        this.logger.warn(" Exception: "  + e );
        }
	}
	
	@Override
	public boolean addMainRule(String moduleFromLogicalPath, String moduleTologicalPath, String ruleTypeKey) {
		boolean ruleAdded = false;
		try {
			appliedRuleService = new AppliedRuleDomainService(); 
	 		moduleService = new ModuleDomainService();
	 		RuleTypeDTO ruletype = getRuleType(ruleTypeKey);
	 		if (ruletype != null) {
	 			String[] violationTypes = new String[ruletype.getViolationTypes().length];
	 			int id = 0;
	 	        for (ViolationTypeDTO v : ruletype.getViolationTypes()){
	 	        	violationTypes[id]= v.key;
	 	        	id ++;
	 	        }
	 	 		long ruleId = appliedRuleService.addAppliedRule(ruleTypeKey, "", violationTypes, "", moduleService.getModuleByLogicalPath(moduleFromLogicalPath), 
	 	 				moduleService.getModuleByLogicalPath(moduleTologicalPath), true, false, null); 
	 	 		if (ruleId >= 0) {
	 	 			ruleAdded = true;
	 	 		} else {
	 		        this.logger.warn(" Rule not added (from, to, ruleTypeKey): " + moduleFromLogicalPath + ", " + moduleTologicalPath + ", " + ruleTypeKey);
	 	 		}
	
	 		} else {
		        this.logger.warn(" RuleTypeKey not found: " + ruleTypeKey);
	 		}
        } catch (Exception e) {
	        this.logger.warn(" Exception: "  + e );
        }
 		return ruleAdded;
	}

	@Override
	public boolean editRule_IsEnabled(String moduleFromLogicalPath, String moduleTologicalPath, String ruleTypeKey, boolean isEnabled) {
		boolean ruleEdited = false;
		try {
			appliedRuleService = new AppliedRuleDomainService(); 
	 		AppliedRuleStrategy foundRule = appliedRuleService.getAppliedMainRuleBy_From_To_RuleTypeKey(moduleFromLogicalPath, moduleTologicalPath, ruleTypeKey);
	 		if (foundRule != null) {
	 			appliedRuleService.updateAppliedRule(foundRule.getId(), foundRule.getRuleTypeKey(), foundRule.getDescription(), foundRule.getDependencyTypes(), 
	 					foundRule.getRegex(), foundRule.getModuleFrom(), foundRule.getModuleTo(), isEnabled);
	 			ruleEdited = true;
	 		} else {
		        this.logger.warn(" Rule not added (from, to, ruleTypeKey): " + moduleFromLogicalPath + ", " + moduleTologicalPath + ", " + ruleTypeKey);
	 		}
        } catch (Exception e) {
	        this.logger.warn(" Exception: "  + e );
        }
 		return ruleEdited;
	}

	@Override
	public ModuleDTO getModule_SelectedInGUI() {
		ModuleDTO selectedModuleDTO =  new ModuleDTO();
		ModuleStrategy selectedModuleStrategy;
		try {
			long selectedModuleId = defineService.getDefinitionController().getSelectedModuleId();
			if (selectedModuleId >= 0) {
				selectedModuleStrategy = moduleService.getModuleById(selectedModuleId);
				if (selectedModuleStrategy != null) {
					selectedModuleDTO = domainParser.parseModule(selectedModuleStrategy);
				} 
			}
        } catch (Exception e) {
	        this.logger.warn(" Exception: "  + e );
        }
		return selectedModuleDTO;
	}

	private RuleTypeDTO getRuleType(String ruleTypeKey) {
		RuleTypeDTO returnValue = null;
 		IValidateService validateService = ServiceProvider.getInstance().getValidateService();
 		CategoryDTO[] categories  = validateService.getCategories();
 		for (CategoryDTO category : categories) {
 			for (RuleTypeDTO ruletype : category.getRuleTypes()) {
 				if (ruletype.getKey().equals(ruleTypeKey)) {
 					return ruletype;
 				}
 			}
 		}
		return returnValue;
	}
}
