package husacct.define.domain.services;

import husacct.ServiceProvider;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser.return_type_return;
import husacct.common.dto.RuleTypeDTO;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.Component;
import husacct.define.domain.module.Facade;
import husacct.define.domain.module.Layer;
import husacct.define.domain.module.Module;
import java.util.ArrayList;

import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;

public class DefaultRuleDomainService {
	private Module _module;
	private RuleTypeDTO[] defaultRuleTypeDTOs = null;
	private ArrayList<AppliedRule> defaultRules = new ArrayList<AppliedRule>();
	private ModuleDomainService domainService = new ModuleDomainService();
		public static DefaultRuleDomainService instance;
	public void addDefaultRules(Module newModule) //
	{
		_module = newModule;
		retrieveRuleTypeDTOsByModule();
		createBaseRules();		
		generateRules();
		//saveDefaultRules();
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
	
	private void createBaseRules()
	{
		if (!defaultRuleTypeDTOs.equals(null))
		{
			for (int i =0; i < defaultRuleTypeDTOs.length;i++)
			{
				defaultRules.add(baseRule());
				System.out.println(defaultRuleTypeDTOs[i].key);
			}
		}
	}
	
	public AppliedRule baseRule()
	{
		AppliedRule appliedRule = new AppliedRule();
		
		appliedRule.setDescription("This is default rule for this type of module.");
		appliedRule.setModuleFrom(_module);
		appliedRule.setEnabled(true);
		
		return appliedRule;
	}
	
	private void generateRules()
	{
		for (AppliedRule rule : defaultRules)
		{
			generateRule(rule);
		}
	}
	
	private void generateRule(AppliedRule appliedRule) {		
		switch (appliedRule.getRuleType()) {
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
            case "IsNotAllowedToMakeSkipCall":  skipCall(appliedRule);
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
	
	public AppliedRule[] generateExternalSystem()
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


	
	public void setDefaultRule(Module module)
	{
		if(module instanceof Component)
		{
		   processComponent(module);
			
		}
	}

	private void processComponent(Module module) {
	 
		Module parent = module.getparent();
		for(Module result: parent.getSubModules())
		{
			if(result instanceof Facade)
			{
				AppliedRuleDomainService service = new AppliedRuleDomainService();
				String[] temp ={};
				service.addAppliedRule("InterfaceConvention", "",temp , "", module, module, true);
				
				
				
				break;
			}
			
		}
	}
	
	
	
}
