package husacct.validate.domain.factory.violationtype;

import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.violationtype.CSharpViolationTypes;
import husacct.validate.domain.validation.violationtype.IViolationType;
import husacct.validate.domain.validation.violationtype.JavaViolationTypes;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

class CSharpViolationTypeFactory extends AbstractViolationType {
	private final EnumSet<CSharpViolationTypes> defaultDependencies;
	private final EnumSet<CSharpViolationTypes> defaultAccess;

	public CSharpViolationTypeFactory(ConfigurationServiceImpl configuration){
		super(configuration, "C#");
		this.defaultDependencies = EnumSet.allOf(CSharpViolationTypes.class);
		this.defaultAccess = EnumSet.of(CSharpViolationTypes.PUBLIC, CSharpViolationTypes.PROTECTED, CSharpViolationTypes.DEFAULT, CSharpViolationTypes.PRIVATE);
		this.defaultDependencies.removeAll(defaultAccess);		
	}

	@Override
	public List<ViolationType> createViolationTypesByRule(String ruleTypeKey) {
		if(isCategoryLegalityOfDependency(ruleTypeKey)){
			return generateViolationTypes(ruleTypeKey, defaultDependencies);
		}
		else if(isVisibilityConvenctionRule(ruleTypeKey)){
			return generateViolationTypes(ruleTypeKey, defaultAccess);
		}
		else if(isNamingConvention(ruleTypeKey)){
			return generateViolationTypes(ruleTypeKey, EnumSet.noneOf(JavaViolationTypes.class));
		}
		else if(isLoopsInModule(ruleTypeKey)){
			return generateViolationTypes(ruleTypeKey, defaultDependencies);
		}
		else{
			return Collections.emptyList();
		}
	}
	
	@Override
	List<IViolationType> createViolationTypesMetaData(){
		return Arrays.asList(EnumSet.allOf(CSharpViolationTypes.class).toArray(new IViolationType[]{}));
	}
	
	@Override
	public HashMap<String, List<ViolationType>> getAllViolationTypes(){		
		return getAllViolationTypes(allViolationKeys);
	}
}