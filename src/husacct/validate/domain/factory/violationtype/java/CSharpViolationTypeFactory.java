package husacct.validate.domain.factory.violationtype.java;

import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.violationtype.csharp.CSharpDependencyRecognition;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

class CSharpViolationTypeFactory extends AbstractViolationType {
	private EnumSet<CSharpDependencyRecognition> defaultDependencies = EnumSet.allOf(CSharpDependencyRecognition.class);		
	private static final String csharpViolationTypesRootPackagename = "csharp";	

	public CSharpViolationTypeFactory(ConfigurationServiceImpl configuration){
		super(configuration);
		this.allViolationKeys = generator.getAllViolationTypes(csharpViolationTypesRootPackagename);
	}

	@Override
	public List<ViolationType> createViolationTypesByRule(String key) {
		if(isCategoryLegalityOfDependency(key)){
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