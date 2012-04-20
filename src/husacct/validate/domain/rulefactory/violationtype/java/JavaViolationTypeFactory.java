package husacct.validate.domain.rulefactory.violationtype.java;

import husacct.validate.domain.rulefactory.violationtypeutil.AbstractViolationType;
import husacct.validate.domain.rulefactory.violationtypeutil.ViolationtypeGenerator;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleTypes;
import husacct.validate.domain.validation.violationtype.java.JavaDependencyTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class JavaViolationTypeFactory extends AbstractViolationType {
	private EnumSet<JavaDependencyTypes> defaultDependencies = EnumSet.allOf(JavaDependencyTypes.class);
	//private EnumSet<JavaAccessTypes> defaultAccess = EnumSet.allOf(JavaAccessTypes.class);	
	private List<String> violationKeys;
	private static final String javaViolationTypesLocation = "husacct.validate.domain.validation.violationtype.java";

	public JavaViolationTypeFactory(){
		ViolationtypeGenerator util = new ViolationtypeGenerator();
		this.violationKeys = util.getAllViolationTypeKeys(javaViolationTypesLocation);
	}

	public List<ViolationType> createViolationTypesByRule(String ruleKey){
		if(ruleKey.equals(RuleTypes.IS_NOT_ALLOWED.toString()) || ruleKey.equals(RuleTypes.IS_ALLOWED.toString()) || ruleKey.equals(RuleTypes.IS_NOT_ALLOWED.toString())||ruleKey.equals(RuleTypes.IS_ONLY_MODULE_ALLOWED.toString())||ruleKey.equals(RuleTypes.MUST_USE.toString())||ruleKey.equals(RuleTypes.BACK_CALL.toString())||ruleKey.equals(RuleTypes.SKIP_CALL.toString())){
			return generateViolationTypes(defaultDependencies);
		}
		else{
			return Collections.emptyList();
		}
	}

	public ViolationType createViolationType(String violationKey){
		List<String> violationKeysToLower = new ArrayList<String>();
		for(String violationtype : violationKeys){
			violationKeysToLower.add(violationtype.toLowerCase());
		}		

		if(violationKeysToLower.contains(violationKey.toLowerCase())){
			return new ViolationType(violationKey);
		}
		else{
			System.out.println("Warning specified violationKey: " + violationKey + " not found");
			return null;
		}
	}
}