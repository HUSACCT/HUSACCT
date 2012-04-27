package husacct.validate.domain.exception;

public class RuleInstantionException extends Exception {
	private static final long serialVersionUID = -7817967411904174045L;

	public RuleInstantionException(){
		super();
	}
	
	public RuleInstantionException(String ruleTypeKey){
		super(String.format("Cannot instantiate ruletype: %s", ruleTypeKey));
	}
}