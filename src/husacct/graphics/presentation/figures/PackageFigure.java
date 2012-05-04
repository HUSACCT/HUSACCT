package husacct.graphics.presentation.figures;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.TextFigure;

public class PackageFigure extends NamedFigure {
	private static final long serialVersionUID = 5449552267500654293L;

	private RectangleFigure top;
	private RectangleFigure body;
	private TextFigure text;

	private static final int MIN_WIDTH = 150;
	private static final int MIN_HEIGHT = 120;

	public PackageFigure(String name) {
		super(name, false);

		top = new RectangleFigure();
		body = new RectangleFigure();
		text = new TextFigure(this.getName());

		children.add(top);
		children.add(body);
		children.add(text);
	}

	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
		// minimum size
		if ((lead.x - anchor.x) < MIN_WIDTH) {
			lead.x = anchor.x + MIN_WIDTH;
		}
		if ((lead.y - anchor.y) < MIN_HEIGHT) {
			lead.y = anchor.y + MIN_HEIGHT;
		}

		top.setBounds(anchor, new Point2D.Double(anchor.x + (lead.x - anchor.x) * 0.33f, anchor.y + (lead.y - anchor.y)
				* 0.2f));

		Point2D.Double bodyTopLeft = new Point2D.Double(anchor.x, (anchor.y + top.getBounds().height));

		body.setBounds(bodyTopLeft, lead);

		// textbox centralising
		double plusX = (((lead.x - bodyTopLeft.x) - this.text.getBounds().width) / 2);
		double plusY = (((lead.y - bodyTopLeft.y) - this.text.getBounds().height) / 2);

		Point2D.Double textAnchor = (Double) bodyTopLeft.clone();
		textAnchor.x += plusX;
		textAnchor.y += plusY;
		text.setBounds(textAnchor, null);

		this.invalidate();
	}

	@Override
	public PackageFigure clone() {
		PackageFigure other = (PackageFigure) super.clone();

		other.top = this.top.clone();
		other.body = this.body.clone();
		other.text = this.text.clone();

		other.children = new ArrayList<Figure>();
		other.children.add(other.top);
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
