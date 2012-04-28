package husacct.graphics.presentation.decorators;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.Figure;

import husacct.common.dto.ViolationDTO;
import husacct.graphics.presentation.figures.BaseFigure;

public class ViolationsDecorator extends Decorator {

	private static final long serialVersionUID = 4445235736740459408L;
	private ViolationDTO[] violations;
	private Color severityColor = Color.BLACK;

	public ViolationsDecorator(BaseFigure decorator, ViolationDTO[] violations) {
		super(decorator);
		this.violations = violations;
	}

	public ViolationDTO[] getViolations() {
		return this.violations;
	}

	@Override
	public void draw(Graphics2D arg0) {
		this.getDecorator().set(AttributeKeys.STROKE_COLOR, Color.RED);
		super.draw(arg0);
	}
	
	@Override
	public void setDecorator(Figure decorator) {
		super.setDecorator(decorator);
		
		findHighestSeverityColor();
		decorator.set(AttributeKeys.STROKE_COLOR, severityColor);
	}
	
	private void findHighestSeverityColor() {
		int highestValue = 0;
		int value;
		
		for (ViolationDTO dto : violations) {
			value = dto.getValue();
			
			if (value > highestValue) {
				highestValue = value;
				severityColor = dto.getSeverityColor();
			}
		}
	}
}
