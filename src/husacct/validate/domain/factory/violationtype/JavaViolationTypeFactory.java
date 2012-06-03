package husacct.validate.domain.factory.violationtype;

import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.violationtype.IViolationType;
import husacct.validate.domain.validation.violationtype.JavaViolationTypes;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

class JavaViolationTypeFactory extends AbstractViolationType {
	private final EnumSet<JavaViolationTypes> defaultDependencies;
	private final EnumSet<JavaViolationTypes> defaultAccess;
	private final EnumSet<JavaViolationTypes> defaultPackaging;

	public JavaViolationTypeFactory(ConfigurationServiceImpl configuration){
		super(configuration, "Java");
		this.defaultDependencies = EnumSet.allOf(JavaViolationTypes.class);
		this.defaultAccess = EnumSet.of(JavaViolationTypes.PUBLIC, JavaViolationTypes.PROTECTED, JavaViolationTypes.DEFAULT, JavaViolationTypes.PRIVATE);
		this.defaultPackaging = EnumSet.of(JavaViolationTypes.CLASS, JavaViolationTypes.PACKAGE);
		this.defaultDependencies.removeAll(defaultAccess);
		this.defaultDependencies.removeAll(defaultPackaging);
	}

	@Override
	public List<ViolationType> createViolationTypesByRule(String ruleTypeKey){
		if(isCategoryLegalityOfDependency(ruleTypeKey)){	
			return generateViolationTypes(ruleTypeKey, defaultDependencies);
		}
		else if(isVisibilityConvenctionRule(ruleTypeKey)){
			return generateViolationTypes(ruleTypeKey, defaultAccess);
		}
		else if(isNamingConvention(ruleTypeKey)){
			return generateViolationTypes(ruleTypeKey, defaultPackaging);
		}
		else if(isInterfaceConvention(ruleTypeKey)){
			return generateViolationTypes(ruleTypeKey, EnumSet.noneOf(JavaViolationTypes.class));
		}
		else if(isSubClassConvention(ruleTypeKey)){
			return generateViolationTypes(ruleTypeKey, EnumSet.of(JavaViolationTypes.EXTENDS_ABSTRACT, JavaViolationTypes.EXTENDS_CONCRETE, JavaViolationTypes.EXTENDS_LIBRARY));
		}
		else{
			return Collections.emptyList();
		}
	}

	@Override
	List<IViolationType> createViolationTypesMetaData(){
		return Arrays.asList(EnumSet.allOf(JavaViolationTypes.class).toArray(new IViolationType[]{}));
	}

	@Override
	public HashMap<String, List<ViolationType>> getAllViolationTypes(){
		return getAllViolationTypes(allViolationKeys);
	}
}