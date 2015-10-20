package husacct.graphics.presentation.figures;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.TextFigure;

public class ProjectFigure extends BaseFigure {
	private static final long	serialVersionUID	= -1533554255484459174L;
	
	public static final Color	projectBackground	= new Color(182, 234, 255);
	
	private RectangleFigure		body;
	private TextFigure			text;
	
	private int					MIN_WIDTH			= 120;
	private int					MIN_HEIGHT			= 60;
	
	public ProjectFigure(String figureName, String uniqueName) {
		super(figureName, uniqueName, "project");
		
		body = new RectangleFigure();
		body.set(AttributeKeys.FILL_COLOR, projectBackground);
		children.add(body);
		
		text = new TextFigure(figureName);
		text.set(AttributeKeys.FONT_BOLD, true);
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
		
		// bigger than text
		double requestTextWidth = text.getBounds().width + 10;
		if ((lead.x - anchor.x) < requestTextWidth) {
			lead.x = anchor.x + requestTextWidth;
		}
		
		body.setBounds(anchor, lead);
		
		// textbox centralising
		double plusX = (((lead.x - anchor.x) - text.getBounds().width) / 2);
		double plusY = (((lead.y - anchor.y) - text.getBounds().height) / 2);
		
		Point2D.Double textAnchor = (Double) anchor.clone();
		textAnchor.x += plusX;
		textAnchor.y += plusY;
		text.setBounds(textAnchor, null);
		
		
		this.invalidate();
	}
	
	@Override
	public ProjectFigure clone() {
		ProjectFigure other = (ProjectFigure) super.clone();
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
}
