package husacct.define.domain.services;

import husacct.ServiceProvider;


import husacct.common.dto.RuleTypeDTO;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.DefaultLayerRulesGenerator;
import husacct.define.domain.SoftwareArchitecture;


import husacct.define.domain.module.Layer;
import husacct.define.domain.module.Module;
import husacct.define.task.AppliedRuleController;

import java.util.ArrayList;





public class DefaultRuleDomainService {
	private Module _module;
	private RuleTypeDTO[] defaultRuleTypeDTOs = null;
	private ArrayList<AppliedRule> defaultRules = new ArrayList<AppliedRule>();
	private ModuleDomainService domainService = new ModuleDomainService();

		public static DefaultRuleDomainService instance;
	public void addDefaultRules(Module newModule) //
	{
		applyDefaultRule(newModule);
		_module = newModule;
		retrieveRuleTypeDTOsByModule();
		generateRules();
		//saveDefaultRules();
	}
	
	private void applyDefaultRule(Module newModule) {
		switch (newModule.getType().toLowerCase()) {
		case "layer":
			isLayer();
			break;

		default:
			break;
		}
		
	}

	private void isLayer() {
		 DefaultLayerRulesGenerator gen = new DefaultLayerRulesGenerator(new AppliedRuleController(-1, -1));
		 SoftwareArchitecture.getInstance().removeLayerAppliedRules();
		gen.applydefualtrules(SoftwareArchitecture.getInstance().getRootModule());
		gen.applychanges();
		
	}

	public static DefaultRuleDomainService getInstance()
	{
		if(instance==null){
			return instance = new DefaultRuleDomainService();
		}else{
			return instance;
		}
		
	}
		
	
	private void retrieveRuleTypeDTOsByModule()
	{
		defaultRuleTypeDTOs = ServiceProvider.getInstance().getValidateService().getDefaultRuleTypesOfModule(_module.getType());
	}
	



	private void generateRules()

	{
		 retrieveRuleTypeDTOsByModule();
	
		if (!defaultRuleTypeDTOs.equals(null))
		{
			for (int i =0; i < defaultRuleTypeDTOs.length;i++)
			{

				System.out.println(defaultRuleTypeDTOs[i].key);

				generateRule(defaultRuleTypeDTOs[i]);
				System.out.println(defaultRuleTypeDTOs[i].key);
				System.out.println(defaultRuleTypeDTOs[i].descriptionKey);

			}
		}
	}
	



	public AppliedRule getBaseRule()
	{
		AppliedRule appliedRule = new AppliedRule();
		
		appliedRule.setDescription("This is a default rule for this type of module.");

		appliedRule.setModuleFrom(_module);
		appliedRule.setEnabled(true);
		
		return appliedRule;
	}
	

	
	
	private void generateRule(RuleTypeDTO ruleType) {		
		switch (ruleType.getKey()) {

			case "Interface":  ;
			break;
			case "Naming":  ;
            break;
			case "Facade":  ;
            break;
			case "SubClass":  ;
            break;       
			case "Visibility":  ;
            break;     
			case "Allowed":  ;
            break;
			case "NotAllowed":  ;
            break;
            case "IsNotAllowedToMakeSkipCall":  
            break;
            case "IsNotAllowedToMakeBackCall":  ;
            break;    
            case "OnlyAllowed":  ;
            break;
            case "MustUse":  ;
            break;
            default: ;
            break;
		}
	}
	private void saveDefaultRules()
	{
		for (AppliedRule defaultRule : defaultRules)
		{

			SoftwareArchitecture.getInstance().addAppliedRule(defaultRule);
		}
	}
	
	public boolean isMandatoryRule(Module module)
	{
		return false;
	}
	
	public AppliedRule[] generateLayerModuleRules()
	{

		return null;
	}
	
	
	
	//RuleTypes
	public AppliedRule skipCall(AppliedRule baseRule)
	{
		Layer layer = (Layer) _module;
		SoftwareArchitecture.getInstance().getTheFirstLayerBelow(layer.getHierarchicalLevel(), SoftwareArchitecture.getInstance().getParentModuleIdByChildId(layer.getId()));
	//	baseRule.setModuleTo(domainService.getRootModule());
		return null;
	}
	
	public AppliedRule backCalln(AppliedRule baseRule)
	{
		return null;
	}
	
	public AppliedRule facadeRule(AppliedRule baseRule)
	{
		return null;
	}


	
	
	public AppliedRule[] generateExternalSystem()
	{
		return null;
	}
	
	
	//RuleTypes
	public void skipCall(RuleTypeDTO rule)
	{
		AppliedRule skipCallRule = getBaseRule();
		skipCallRule.setRuleType("IsNotAllowedToMakeSkipCall");
		skipCallRule.setDescription(skipCallRule.getDescription()+"\n"+rule.getDescriptionKey());
		
		skipCallRule.setModuleTo(skipCallRule.getModuleFrom());
		defaultRules.add(skipCallRule);
	}
	
	public void backCall(RuleTypeDTO rule)
	{
		AppliedRule backCallRule = getBaseRule();
		backCallRule.setRuleType("IsNotAllowedToMakeSkipCall");
		backCallRule.setDescription(backCallRule.getDescription()+"\n"+rule.getDescriptionKey());
		
		backCallRule.setModuleTo(backCallRule.getModuleFrom());
		defaultRules.add(backCallRule);
	}
	
	public void facadeRules(AppliedRule baseRule)
	{
		return;
	}
	
	
	public void createDependacies()
	{
		
		/*
		        InvocMethod
				Exception
				AccessPropertyOrField 
				ExtendsInterface 
				Import 
				ExtendsConcrete 
				Annotation 
				Declaration 
				InvocConstructor 
				ExtendsLibrary 
				ExtendsAbstract 
				Implements 
		*/
		
	}
	
}
