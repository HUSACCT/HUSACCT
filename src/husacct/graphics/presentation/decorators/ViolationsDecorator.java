package husacct.graphics.presentation.decorators;

import java.awt.Color;

import org.jhotdraw.draw.AttributeKeys;

import husacct.graphics.presentation.figures.BaseFigure;

public class ViolationsDecorator implements Decorator {
	private Color oldStrokeColor;
	private Color severityColor;

	public ViolationsDecorator(Color severityColor) {
		this.severityColor = severityColor;
	}

	@Override
	public void decorate(BaseFigure f) {
		this.oldStrokeColor = f.get(AttributeKeys.STROKE_COLOR);
		f.setStrokeColor(this.severityColor);
	}

	@Override
	public void deDecorate(BaseFigure f) {
		f.setStrokeColor(this.oldStrokeColor);
	}

}
