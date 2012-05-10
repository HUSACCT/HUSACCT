package husacct.validate.domain.factory.violationtype.java;

import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.violationtype.csharp.CSharpAccessModifiers;
import husacct.validate.domain.validation.violationtype.csharp.CSharpDependencyRecognition;
import husacct.validate.domain.validation.violationtype.java.JavaDependencyRecognition;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

class CSharpViolationTypeFactory extends AbstractViolationType {
	private final EnumSet<CSharpDependencyRecognition> defaultDependencies = EnumSet.allOf(CSharpDependencyRecognition.class);		
	private final EnumSet<CSharpAccessModifiers> defaultAccess = EnumSet.allOf(CSharpAccessModifiers.class);
	private static final String csharpViolationTypesRootPackagename = "csharp";	

	public CSharpViolationTypeFactory(ConfigurationServiceImpl configuration){
		super(configuration, "C#");
		this.allViolationKeys = generator.getAllViolationTypes(csharpViolationTypesRootPackagename);
	}

	@Override
	public List<ViolationType> createViolationTypesByRule(String ruleTypeKey) {
		if(isCategoryLegalityOfDependency(ruleTypeKey)){
			return generateViolationTypes(defaultDependencies);
		}
		else if(isVisibilityConvenctionRule(ruleTypeKey)){
			return generateViolationTypes(defaultAccess);
		}
		else if(isNamingConvention(ruleTypeKey)){
			return generateViolationTypes(EnumSet.noneOf(JavaDependencyRecognition.class));
		}
		else if(isLoopsInModule(ruleTypeKey)){
			return generateViolationTypes(defaultDependencies);
		}
		else{
			return Collections.emptyList();
		}
	}
	
	@Override
	public HashMap<String, List<ViolationType>> getAllViolationTypes(){		
		return getAllViolationTypes(allViolationKeys);
	}
}