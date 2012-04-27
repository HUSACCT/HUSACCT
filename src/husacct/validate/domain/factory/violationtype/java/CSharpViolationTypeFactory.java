package husacct.validate.domain.factory.violationtype.java;

import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.violationtype.csharp.CSharpDependencyRecognition;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CSharpViolationTypeFactory extends AbstractViolationType {
	private EnumSet<CSharpDependencyRecognition> defaultDependencies = EnumSet.allOf(CSharpDependencyRecognition.class);		
	private static final String csharpViolationTypesLocation = "husacct.validate.domain.validation.violationtype.csharp";	

	public CSharpViolationTypeFactory(){
		ViolationtypeGenerator generator = new ViolationtypeGenerator();
		this.allViolationKeys = generator.getAllViolationTypeKeys(csharpViolationTypesLocation);
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
		ViolationtypeGenerator generator = new ViolationtypeGenerator();
		Map<String, String> violationTypeKeysAndCategories = generator.getAllViolationTypesWithCategory(csharpViolationTypesLocation);
		return getAllViolationTypes(violationTypeKeysAndCategories);
	}
}