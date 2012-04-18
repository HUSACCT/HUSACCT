package husacct.graphics.presentation.decorators;

import java.awt.Color;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.Figure;

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
	
	@Override 
	public void setDecorator(Figure newDecorator) {
		Figure oldDecorator = getDecorator();
		oldDecorator.set(AttributeKeys.STROKE_COLOR, Color.BLACK);
		
		super.setDecorator(newDecorator);
		getDecorator().set(AttributeKeys.STROKE_COLOR, Color.RED);
	}
}
