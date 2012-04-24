package husacct.graphics.presentation.decorators;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jhotdraw.draw.AttributeKeys;

import husacct.common.dto.ViolationDTO;
import husacct.graphics.presentation.figures.BaseFigure;

public class ViolationsDecorator extends Decorator {

	private static final long serialVersionUID = 4445235736740459408L;
	private ViolationDTO[] violations;
	
	public ViolationsDecorator(BaseFigure decorator,ViolationDTO[] violations) {
		super(decorator);
		this.violations = violations;
	}
	
	public ViolationDTO[] getViolations() {
		return this.violations;
	}

	@Override
	public void draw(Graphics2D arg0)
	{
		this.getDecorator().set(AttributeKeys.STROKE_COLOR, Color.RED);
		super.draw(arg0);
	}
}
