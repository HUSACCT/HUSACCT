package husacct.graphics.presentation.figures;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.TextFigure;

public class SubsystemFigure extends NamedFigure {
	private static final long serialVersionUID = -2743753116624138171L;
	private RectangleFigure body;
	private TextFigure moduleText;
	private TextFigure text;

	protected int minWidth = 100;
	protected int minHeight = 100;

	public SubsystemFigure(String name) {
		super(name, false);

		body = new RectangleFigure();
		body.set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);
		
		moduleText = new TextFigure('\u00AB'+"subsystem"+'\u00BB');
		
		text = new TextFigure(name);
		text.set(AttributeKeys.FONT_BOLD, true);
		
		children.add(body);
		children.add(moduleText);
		children.add(text);
	}

	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
		if ((lead.x - anchor.x) < this.minWidth) {
			lead.x = anchor.x + this.minWidth;
		}
		if ((lead.y - anchor.y) < this.minHeight) {
			lead.y = anchor.y + this.minHeight;
		}

		body.setBounds(anchor, lead);
		
		// get text sizes
		double maxTextWidth = this.moduleText.getBounds().width;
		if(this.text.getBounds().width > maxTextWidth) {
			maxTextWidth = this.text.getBounds().width;
		}
		double totalTextHeight = this.text.getBounds().height + this.moduleText.getBounds().height;

		// textbox centralising
		double plusX = (((lead.x - anchor.x) - maxTextWidth) / 2);
		double plusY = (((lead.y - anchor.y) - totalTextHeight) / 2);

		Point2D.Double moduleTextAnchor = (Double) anchor.clone();
		moduleTextAnchor.x += plusX + ((maxTextWidth - moduleText.getBounds().width)/2);
		moduleTextAnchor.y += plusY;
		moduleText.setBounds(moduleTextAnchor, null);

		Point2D.Double textAnchor = (Double) anchor.clone();
		textAnchor.x += plusX + ((maxTextWidth - text.getBounds().width)/2);
		textAnchor.y += plusY + moduleText.getBounds().height;
		text.setBounds(textAnchor, null);

		this.invalidate();
	}

	@Override
	public SubsystemFigure clone() {

		SubsystemFigure other = (SubsystemFigure) super.clone();
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

	@Override
	public boolean isLine() {
		return false;
	}
}
