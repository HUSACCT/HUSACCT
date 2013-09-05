package husacct.graphics.presentation.decorators;

import husacct.graphics.presentation.figures.BaseFigure;

import java.awt.Color;

import org.jhotdraw.draw.AttributeKeys;

public class ViolationsDecorator implements Decorator {
	private Color	oldStrokeColor;
	private Color	severityColor;
	
	public ViolationsDecorator(Color severityColor) {
		this.severityColor = severityColor;
	}
	
	@Override
	public void decorate(BaseFigure f) {
		oldStrokeColor = f.get(AttributeKeys.STROKE_COLOR);
		f.setStrokeColor(severityColor);
	}
	
	@Override
	public void deDecorate(BaseFigure f) {
		f.setStrokeColor(oldStrokeColor);
	}
	
}
