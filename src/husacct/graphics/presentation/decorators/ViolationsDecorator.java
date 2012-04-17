package husacct.graphics.presentation.decorators;

import husacct.common.dto.ViolationDTO;

public class ViolationsDecorator extends Decorator {

	private static final long serialVersionUID = 4445235736740459408L;
	private ViolationDTO[] violations;
	
	public ViolationsDecorator(ViolationDTO[] violations) {
		this.violations = violations;
	}
	
	public ViolationDTO[] getViolations() {
		return this.violations;
	}	
}
