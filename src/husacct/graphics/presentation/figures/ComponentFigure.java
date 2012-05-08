package husacct.graphics.presentation.figures;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.TextFigure;

public class ComponentFigure extends NamedFigure {
	private static final long serialVersionUID = -344477017055924400L;

	public static final int SPACE_LEFT = 34;
	public static final int COMP_HEIGHT = 24;
	public static final int COMP_DISTANCE = 8;

	private RectangleFigure mainBox;
	private RectangleFigure box1;
	private RectangleFigure box2;
	private TextFigure textBox;

	public ComponentFigure(String name) {
		super(name, false);

		mainBox = new RectangleFigure();
		box1 = new RectangleFigure();
		box2 = new RectangleFigure();
		textBox = new TextFigure(getName());

		children.add(mainBox);
		children.add(box1);
		children.add(box2);
		children.add(textBox);
		
		set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);
	}

	private void sizeChildBoxes(Point2D.Double anchor, Point2D.Double lead) {
		if ((lead.x - anchor.x) < getMinimumWidth()) {
			lead.x = anchor.x + getMinimumWidth();
		}

		if ((lead.y - anchor.y) < getMinimumHeight()) {
			lead.y = anchor.y + getMinimumHeight();
		}

		Point2D.Double newMainBoxAnchor = (Point2D.Double) anchor.clone();
		newMainBoxAnchor.x += SPACE_LEFT;
		mainBox.setBounds(newMainBoxAnchor, lead);

		Point2D.Double newBox1Anchor = (Point2D.Double) anchor.clone();
		newBox1Anchor.y += COMP_DISTANCE;
		Point2D.Double newBox1Lead = new Point2D.Double(newBox1Anchor.x + SPACE_LEFT * 2, newBox1Anchor.y + COMP_HEIGHT);
		box1.setBounds(newBox1Anchor, newBox1Lead);

		Point2D.Double newBox2Anchor = (Point2D.Double) newBox1Anchor.clone();
		newBox2Anchor.y += COMP_HEIGHT + COMP_DISTANCE;
		Point2D.Double newBox2Lead = new Point2D.Double(newBox2Anchor.x + SPACE_LEFT * 2, newBox2Anchor.y + COMP_HEIGHT);
		box2.setBounds(newBox2Anchor, newBox2Lead);

		Point2D.Double textBoxOrigin = (Point2D.Double) newBox1Anchor.clone();
		textBoxOrigin.x += SPACE_LEFT * 2 + 8;
		textBox.setBounds(textBoxOrigin, null);
	}

	public static int getMinimumHeight() {
		return COMP_DISTANCE + COMP_HEIGHT + COMP_DISTANCE + COMP_HEIGHT + COMP_DISTANCE;
	}

	public static int getMinimumWidth() {
		return SPACE_LEFT * 4;
	}

	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
		this.sizeChildBoxes(anchor, lead);

		invalidate();
	}

	@Override
	public ComponentFigure clone() {
		ComponentFigure that = (ComponentFigure) super.clone();

		that.mainBox = mainBox.clone();
		that.box1 = box1.clone();
		that.box2 = box2.clone();
		that.textBox = textBox.clone();

		that.children = new ArrayList<Figure>();
		that.children.add(that.mainBox);
		that.children.add(that.box1);
		that.children.add(that.box2);
		that.children.add(that.textBox);

		return that;
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
