package husacct.validate.domain.factory.violationtype.java;

import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.violationtype.java.JavaAccessModifiers;
import husacct.validate.domain.validation.violationtype.java.JavaDependencyRecognition;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

class JavaViolationTypeFactory extends AbstractViolationType {
	private final EnumSet<JavaDependencyRecognition> defaultDependencies = EnumSet.allOf(JavaDependencyRecognition.class);
	private final EnumSet<JavaAccessModifiers> defaultAccess = EnumSet.allOf(JavaAccessModifiers.class);	
	private static final String javaViolationTypesRootPackagename = "java";

	public JavaViolationTypeFactory(ConfigurationServiceImpl configuration){
		super(configuration, "Java");
		this.allViolationKeys = generator.getAllViolationTypes(javaViolationTypesRootPackagename);
	}

	@Override
	public List<ViolationType> createViolationTypesByRule(String ruleTypeKey){
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