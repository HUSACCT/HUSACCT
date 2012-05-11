package husacct.validate.domain.factory.violationtype;

import husacct.validate.domain.ConfigurationServiceImpl;
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

	public JavaViolationTypeFactory(ConfigurationServiceImpl configuration){
		super(configuration, "Java");
		this.defaultDependencies = EnumSet.allOf(JavaViolationTypes.class);
		this.defaultAccess = EnumSet.of(JavaViolationTypes.PUBLIC, JavaViolationTypes.PROTECTED, JavaViolationTypes.DEFAULT, JavaViolationTypes.PRIVATE);
		this.defaultDependencies.removeAll(defaultAccess);
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
			return generateViolationTypes(EnumSet.noneOf(JavaViolationTypes.class));
		}
		else if(isLoopsInModule(ruleTypeKey)){
			return generateViolationTypes(defaultDependencies);
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