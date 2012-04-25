package husacct.validate.domain.exception;

public class RuleTypeNotFoundException extends Exception {
	private static final long serialVersionUID = 3803347656795776775L;

	public RuleTypeNotFoundException(){
		super();
	}

	public RuleTypeNotFoundException(String ruleTypeKey){
		super(String.format("RuleTypeKey: %s not found in the list of ruledefinitions", ruleTypeKey));
	}
}