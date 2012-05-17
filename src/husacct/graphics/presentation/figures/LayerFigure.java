package husacct.graphics.presentation.figures;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.TextFigure;

public class LayerFigure extends BaseFigure {
	private static final long serialVersionUID = 101138923385231941L;
	private RectangleFigure body;
	private TextFigure text;

	protected int minWidth = 300;
	protected int minHeight = 50;

	public LayerFigure(String name) {
		super(name);

		body = new RectangleFigure();
		text = new TextFigure(name);
		text.set(AttributeKeys.FONT_BOLD, true);
		children.add(body);
		children.add(text);

		body.set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);
	}

	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
		if ((lead.x - anchor.x) < minWidth) {
			lead.x = anchor.x + minWidth;
		}
		if ((lead.y - anchor.y) < minHeight) {
			lead.y = anchor.y + minHeight;
		}

		body.setBounds(anchor, lead);

		// textbox centralising
		double plusX = (((lead.x - anchor.x) - text.getBounds().width) / 2);
		double plusY = (((lead.y - anchor.y) - text.getBounds().height) / 2);

		Point2D.Double textAnchor = (Double) anchor.clone();
		textAnchor.x += plusX;
		textAnchor.y += plusY;
		text.setBounds(textAnchor, null);

		invalidate();
	}

	@Override
	public LayerFigure clone() {

		LayerFigure other = (LayerFigure) super.clone();
		other.body = body.clone();
		other.text = text.clone();

		other.children = new ArrayList<Figure>();
		other.children.add(other.body);
		other.children.add(other.text);

		return other;
	}

	@Override
	public boolean isModule() {
		return true;
	}

	@Override
	public boolean isLine() {
		return false;
	}
}
