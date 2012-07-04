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

public class ComponentFigure extends BaseFigure {
	private static final long serialVersionUID = -3019979543942866990L;
	private RectangleFigure body;
	private TextFigure text;
	private BufferedImage compIcon;
	private ImageFigure compIconFig;

	public int MIN_WIDTH = 60;
	public int MIN_HEIGHT = 50;

	public ComponentFigure(String figureName) {
		super(figureName);

		body = new RectangleFigure();
		body.set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);
		children.add(body);

		text = new TextFigure(figureName);
		text.set(AttributeKeys.FONT_BOLD, true);
		children.add(text);

		compIconFig = new ImageFigure();
		compIconFig.set(AttributeKeys.STROKE_WIDTH, 0.0);
		compIconFig.set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);

		try {
			URL componentImageURL = Resource.get(Resource.ICON_COMPONENT);
			compIcon = ImageIO.read(componentImageURL);
			compIconFig.setImage(null, compIcon);
			children.add(compIconFig);
		} catch (Exception e) {
			compIconFig = null;
			Logger.getLogger(this.getClass()).warn("failed to load component icon image file");
		}
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

		if (compIconFig != null) {
			double iconAnchorX = lead.x - 6 - compIcon.getWidth();
			double iconAnchorY = anchor.y + 6;
			double iconLeadX = iconAnchorX + compIcon.getWidth();
			double iconLeadY = iconAnchorY + compIcon.getHeight();
			compIconFig.setBounds(new Point2D.Double(iconAnchorX, iconAnchorY), new Point2D.Double(iconLeadX, iconLeadY));
		}

		this.invalidate();
	}

	@Override
	public ComponentFigure clone() {
		ComponentFigure other = (ComponentFigure) super.clone();
		other.body = body.clone();
		other.text = text.clone();
		other.compIconFig = compIconFig.clone();

		other.children = new ArrayList<Figure>();
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
