package husacct.define.domain.services;

import husacct.ServiceProvider;
<<<<<<< HEAD
import husacct.common.dto.RuleTypeDTO;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.SoftwareArchitecture;
=======
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser.return_type_return;
import husacct.common.dto.RuleTypeDTO;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.Component;
import husacct.define.domain.module.Facade;
>>>>>>> develop
import husacct.define.domain.module.Layer;
import husacct.define.domain.module.Module;
import java.util.ArrayList;

<<<<<<< HEAD
=======
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;

>>>>>>> develop
public class DefaultRuleDomainService {
	private Module _module;
	private RuleTypeDTO[] defaultRuleTypeDTOs = null;
	private ArrayList<AppliedRule> defaultRules = new ArrayList<AppliedRule>();
	private ModuleDomainService domainService = new ModuleDomainService();
<<<<<<< HEAD
		
	public void addDefaultRules(Module newModule) //
	{
		_module = newModule;
		retrieveRuleTypeDTOsByModule();		
		generateRules();
		saveDefaultRules();
=======
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
		
>>>>>>> develop
	}
	
	private void retrieveRuleTypeDTOsByModule()
	{
		defaultRuleTypeDTOs = ServiceProvider.getInstance().getValidateService().getDefaultRuleTypesOfModule(_module.getType());
	}
	
<<<<<<< HEAD
	private void generateRules()
=======
	private void createBaseRules()
>>>>>>> develop
	{
		if (!defaultRuleTypeDTOs.equals(null))
		{
			for (int i =0; i < defaultRuleTypeDTOs.length;i++)
			{
<<<<<<< HEAD
				generateRule(defaultRuleTypeDTOs[i]);
				System.out.println(defaultRuleTypeDTOs[i].key);
				System.out.println(defaultRuleTypeDTOs[i].descriptionKey);
=======
				defaultRules.add(baseRule());
				System.out.println(defaultRuleTypeDTOs[i].key);
>>>>>>> develop
			}
		}
	}
	
<<<<<<< HEAD
	public AppliedRule getBaseRule()
	{
		AppliedRule appliedRule = new AppliedRule();
		
		appliedRule.setDescription("This is a default rule for this type of module.");
=======
	public AppliedRule baseRule()
	{
		AppliedRule appliedRule = new AppliedRule();
		
		appliedRule.setDescription("This is default rule for this type of module.");
>>>>>>> develop
		appliedRule.setModuleFrom(_module);
		appliedRule.setEnabled(true);
		
		return appliedRule;
	}
	
<<<<<<< HEAD
	private void generateRule(RuleTypeDTO ruleType) {		
		switch (ruleType.getKey()) {
=======
	private void generateRules()
	{
		for (AppliedRule rule : defaultRules)
		{
			generateRule(rule);
		}
	}
	
	private void generateRule(AppliedRule appliedRule) {		
		switch (appliedRule.getRuleType()) {
>>>>>>> develop
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
<<<<<<< HEAD
            case "IsNotAllowedToMakeSkipCall":  skipCall(ruleType);
            break;
            case "IsNotAllowedToMakeBackCall":  backCall(ruleType);
=======
            case "IsNotAllowedToMakeSkipCall":  skipCall(appliedRule);
            break;
            case "IsNotAllowedToMakeBackCall":  ;
>>>>>>> develop
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
<<<<<<< HEAD
			//if valid?
=======
>>>>>>> develop
			SoftwareArchitecture.getInstance().addAppliedRule(defaultRule);
		}
	}
	
	public boolean isMandatoryRule(Module module)
	{
		return false;
	}
	
	public AppliedRule[] generateLayerModuleRules()
	{
<<<<<<< HEAD
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
	
	public void facadeRule(AppliedRule baseRule)
	{
		return;
	}
=======
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
	
	
	
>>>>>>> develop
}
