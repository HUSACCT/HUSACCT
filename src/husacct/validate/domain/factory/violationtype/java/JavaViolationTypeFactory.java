package husacct.validate.domain.factory.violationtype.java;

import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleTypes;
import husacct.validate.domain.validation.violationtype.java.JavaDependencyRecognition;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

class JavaViolationTypeFactory extends AbstractViolationType {
	private EnumSet<JavaDependencyRecognition> defaultDependencies = EnumSet.allOf(JavaDependencyRecognition.class);
	//private EnumSet<JavaAccessTypes> defaultAccess = EnumSet.allOf(JavaAccessTypes.class);	
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
		else if(ruleTypeKey.equals(RuleTypes.INTERFACE_CONVENTION.toString())){			
			return generateViolationTypes(EnumSet.of(JavaDependencyRecognition.IMPLEMENTS));
		}
		else if(ruleTypeKey.equals(RuleTypes.SUBCLASS_CONVENTION.toString())){
			return generateViolationTypes(EnumSet.of(JavaDependencyRecognition.EXTENDS_ABSTRACT, JavaDependencyRecognition.EXTENDS_CONCRETE));
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