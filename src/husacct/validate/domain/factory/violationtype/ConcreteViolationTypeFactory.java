package husacct.validate.domain.factory.violationtype;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.violationtype.IViolationType;
import husacct.validate.domain.validation.violationtype.ViolationTypes;

public class ConcreteViolationTypeFactory extends AbstractViolationType {
	private final EnumSet<ViolationTypes> defaultDependencies;
	private final EnumSet<ViolationTypes> defaultAccess;
	private final EnumSet<ViolationTypes> defaultPackaging;
	
	ConcreteViolationTypeFactory(ConfigurationServiceImpl configuration, String languageName) {
		super(configuration, languageName);
		
		this.defaultDependencies = EnumSet.allOf(ViolationTypes.class);
		this.defaultAccess = EnumSet.of(ViolationTypes.PUBLIC, ViolationTypes.PROTECTED, ViolationTypes.DEFAULT, ViolationTypes.PRIVATE);
		this.defaultPackaging = EnumSet.of(ViolationTypes.CLASS, ViolationTypes.PACKAGE);
		this.defaultDependencies.removeAll(defaultAccess);
		this.defaultDependencies.removeAll(defaultPackaging);
	}


	@Override
	public List<ViolationType> createViolationTypesByRule(String ruleTypeKey) {
		if (isCategoryLegalityOfDependency(ruleTypeKey)) {
			return generateViolationTypes(ruleTypeKey, defaultDependencies);
		} else if (isVisibilityConventionRule(ruleTypeKey)) {
			return generateViolationTypes(ruleTypeKey, defaultAccess);
		} else if (isNamingConvention(ruleTypeKey)) {
			return generateViolationTypes(ruleTypeKey, defaultPackaging);
		} else if (isInheritanceConvention(ruleTypeKey)) {
			return generateViolationTypes(ruleTypeKey, EnumSet.of(ViolationTypes.INHERITANCE)); 
		} else {
			return Collections.emptyList(); // Alternative: EnumSet.noneOf(ViolationTypes.class)
		}
	}


	@Override
	List<IViolationType> createViolationTypesMetaData() {
		return Arrays.asList(EnumSet.allOf(ViolationTypes.class).toArray(new IViolationType[] {}));
	}
}