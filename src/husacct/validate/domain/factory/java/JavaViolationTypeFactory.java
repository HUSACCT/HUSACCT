package husacct.validate.domain.factory.java;

import husacct.validate.domain.factory.AbstractViolationType;
import husacct.validate.domain.factory.violationtypeutil.ViolationtypeKeys;
import husacct.validate.domain.violationtype.ViolationType;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class JavaViolationTypeFactory extends AbstractViolationType {
	private EnumSet<JavaDependencyTypes> defaultDependencies = EnumSet.allOf(JavaDependencyTypes.class);
	private EnumSet<JavaAccessTypes> defaultAccess = EnumSet.allOf(JavaAccessTypes.class);	
	private List<String> violationKeys;
	private static final String javaViolationTypesLocation = "husacct.validate.domain.factory.java";

	public JavaViolationTypeFactory(){
		ViolationtypeKeys util = new ViolationtypeKeys();
		this.violationKeys = util.getAllViolationTypeKeys(javaViolationTypesLocation);
	}

	public List<ViolationType> createViolationTypesByRule(String ruleKey){
		if(ruleKey.equals("IsNotAllowedToUse")){
			return generateViolationTypes(defaultDependencies);
		}
		else{
			return Collections.emptyList();
		}
	}

	public ViolationType createViolationType(String violationKey){
		if(violationKeys.contains(violationKey)){
			return new ViolationType(violationKey);
		}
		else{
			System.out.println("Warning specified violationKey: " + violationKey + " not found");
			return null;
		}
	}
}