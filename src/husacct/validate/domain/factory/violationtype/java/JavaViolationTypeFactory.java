package husacct.validate.domain.factory.violationtype.java;

import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.violationtype.java.JavaDependencyRecognition;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class JavaViolationTypeFactory extends AbstractViolationType {
	private EnumSet<JavaDependencyRecognition> defaultDependencies = EnumSet.allOf(JavaDependencyRecognition.class);
	//private EnumSet<JavaAccessTypes> defaultAccess = EnumSet.allOf(JavaAccessTypes.class);	
	private static final String javaViolationTypesLocation = "husacct.validate.domain.validation.violationtype.java";

	public JavaViolationTypeFactory(){
		ViolationtypeGenerator generator = new ViolationtypeGenerator();
		this.allViolationKeys = generator.getAllViolationTypeKeys(javaViolationTypesLocation);
	}

	@Override
	public List<ViolationType> createViolationTypesByRule(String ruleKey){
		if(isCategoryLegalityOfDependency(ruleKey)){
			return generateViolationTypes(defaultDependencies);
		}
		else{
			return Collections.emptyList();
		}
	}

	@Override
	public HashMap<String, List<ViolationType>> getAllViolationTypes(){
		ViolationtypeGenerator generator = new ViolationtypeGenerator();
		Map<String, String> violationTypeKeysAndCategories = generator.getAllViolationTypesWithCategory(javaViolationTypesLocation);
		return getAllViolationTypes(violationTypeKeysAndCategories);
	}

	public static void main(String[] args){
		new JavaViolationTypeFactory();
	}
}