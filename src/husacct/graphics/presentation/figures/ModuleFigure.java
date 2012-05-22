package husacct.graphics.presentation.figures;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.TextFigure;

public class ModuleFigure extends BaseFigure {
	private static final long serialVersionUID = -2743753116624138171L;
	private RectangleFigure body;
	private TextFigure moduleText;
	private TextFigure text;

	public static final int MIN_WIDTH = 100;
	public static final int MIN_HEIGHT = 100;

	public ModuleFigure(String name, String type) {
		super(name);

		body = new RectangleFigure();
		body.set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);

		moduleText = new TextFigure('\u00AB' + type + '\u00BB');

		text = new TextFigure(name);
		text.set(AttributeKeys.FONT_BOLD, true);

		children.add(body);
		children.add(moduleText);
		children.add(text);
	}

	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
		if ((lead.x - anchor.x) < MIN_WIDTH) {
			lead.x = anchor.x + MIN_WIDTH;
		}
		if ((lead.y - anchor.y) < MIN_HEIGHT) {
			lead.y = anchor.y + MIN_HEIGHT;
		}

		body.setBounds(anchor, lead);

		// get text sizes
		double maxTextWidth = moduleText.getBounds().width;
		if (text.getBounds().width > maxTextWidth) {
			maxTextWidth = text.getBounds().width;
		}
		double totalTextHeight = text.getBounds().height + moduleText.getBounds().height;

		// textbox centralising
		double plusX = (((lead.x - anchor.x) - maxTextWidth) / 2);
		double plusY = (((lead.y - anchor.y) - totalTextHeight) / 2);

		Point2D.Double moduleTextAnchor = (Double) anchor.clone();
		moduleTextAnchor.x += plusX + ((maxTextWidth - moduleText.getBounds().width) / 2);
		moduleTextAnchor.y += plusY;
		moduleText.setBounds(moduleTextAnchor, null);

		Point2D.Double textAnchor = (Double) anchor.clone();
		textAnchor.x += plusX + ((maxTextWidth - text.getBounds().width) / 2);
		textAnchor.y += plusY + moduleText.getBounds().height;
		text.setBounds(textAnchor, null);

		invalidate();
	}

	@Override
	public ModuleFigure clone() {
		ModuleFigure other = (ModuleFigure) super.clone();
		other.body = body.clone();
		other.text = text.clone();
		other.moduleText = moduleText.clone();

		other.children = new ArrayList<Figure>();
		other.children.add(other.body);
		other.children.add(other.text);
		other.children.add(other.moduleText);

		return other;
	}

	@Override
	public boolean isModule() {
		return true;
	}

}
