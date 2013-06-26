package husacct.graphics.presentation.figures;

import husacct.common.Resource;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.ImageFigure;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.TextFigure;

public class PackageFigure extends BaseFigure {
	private static final long	serialVersionUID	= 5449552267500654293L;
	
	private RectangleFigure		top;
	private RectangleFigure		body;
	private TextFigure			text;
	private BufferedImage		compIcon;
	private ImageFigure			compIconFig;
	
	private int					MIN_WIDTH			= 100;
	private int					MIN_HEIGHT			= 80;
	
	public PackageFigure(String name) {
		super(name);
		
		top = new RectangleFigure();
		body = new RectangleFigure();
		text = new TextFigure(getName());
		
		children.add(top);
		children.add(body);
		children.add(text);
		
		compIconFig = new ImageFigure();
		compIconFig.set(AttributeKeys.STROKE_WIDTH, 0.0);
		compIconFig.set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);
		
		try {
			// TODO There needs to be a icon for Projects
			URL componentImageURL = Resource.get(Resource.ICON_PACKAGE);
			compIcon = ImageIO.read(componentImageURL);
			compIconFig.setImage(null, compIcon);
			children.add(compIconFig);
		} catch (Exception e) {
			compIconFig = null;
			Logger.getLogger(this.getClass()).warn(
					"failed to load component icon image file");
		}
		
		top.set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);
		body.set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);
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
		
		top.setBounds(anchor, new Point2D.Double(anchor.x + (lead.x - anchor.x)
				* 0.33f, anchor.y + (lead.y - anchor.y) * 0.2f));
		
		Point2D.Double bodyTopLeft = new Point2D.Double(anchor.x,
				(anchor.y + top.getBounds().height));
		
		body.setBounds(bodyTopLeft, lead);
		
		// textbox centralising
		double plusX = (((lead.x - bodyTopLeft.x) - text.getBounds().width) / 2);
		double plusY = (((lead.y - bodyTopLeft.y) - text.getBounds().height) / 2);
		
		Point2D.Double textAnchor = (Double) bodyTopLeft.clone();
		textAnchor.x += plusX;
		textAnchor.y += plusY;
		text.setBounds(textAnchor, null);
		
		if (compIconFig != null) {
			double iconAnchorX = lead.x - 6 - compIcon.getWidth();
			double iconAnchorY = anchor.y + 6;
			double iconLeadX = iconAnchorX + compIcon.getWidth();
			double iconLeadY = iconAnchorY + compIcon.getHeight();
			compIconFig.setBounds(new Point2D.Double(iconAnchorX, iconAnchorY),
					new Point2D.Double(iconLeadX, iconLeadY));
		}
		
		invalidate();
	}
	
	@Override
	public PackageFigure clone() {
		PackageFigure other = (PackageFigure) super.clone();
		
		other.top = top.clone();
		other.body = body.clone();
		other.text = text.clone();
		other.compIconFig = compIconFig.clone();
		
		other.children = new ArrayList<Figure>();
		other.children.add(other.top);
		other.children.add(other.body);
		other.children.add(other.text);
		if (compIconFig != null) {
			other.children.add(other.compIconFig);
		}
		
		return other;
	}
	
	@Override
	public boolean isModule() {
		return true;
	}
}
