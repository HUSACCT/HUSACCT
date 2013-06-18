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

public class ModuleFigure extends BaseFigure {
	private static final long serialVersionUID = -2743753116624138171L;
	private RectangleFigure body;
	private TextFigure moduleText;
	private TextFigure text;
	private BufferedImage compIcon;
	private ImageFigure compIconFig;

	public int MIN_WIDTH = 50;
	public int MIN_HEIGHT = 50;

	public ModuleFigure(String name, String type) {
		super(name);

		body = new RectangleFigure();
		body.set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);

		moduleText = new TextFigure('\u00AB' + type + '\u00BB');

		text = new TextFigure(name);
		text.set(AttributeKeys.FONT_BOLD, true);

		compIconFig = new ImageFigure();
		compIconFig.set(AttributeKeys.STROKE_WIDTH, 0.0);
		compIconFig.set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);

		try {
			URL componentImageURL = Resource.get(Resource.ICON_MODULE);
			compIcon = ImageIO.read(componentImageURL);
			compIconFig.setImage(null, compIcon);
			children.add(compIconFig);
		} catch (Exception e) {
			compIconFig = null;
			Logger.getLogger(this.getClass()).warn("failed to load component icon image file");
		}
		
		children.add(body);
		children.add(moduleText);
		children.add(text);
	}

	@Override
	public boolean isModule() {
		return true;
	}

	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
		if (lead.x - anchor.x < MIN_WIDTH)
			lead.x = anchor.x + MIN_WIDTH;
		if (lead.y - anchor.y < MIN_HEIGHT)
			lead.y = anchor.y + MIN_HEIGHT;

		body.setBounds(anchor, lead);

		// get text sizes
		double maxTextWidth = moduleText.getBounds().width;
		if (text.getBounds().width > maxTextWidth)
			maxTextWidth = text.getBounds().width;
		double totalTextHeight = text.getBounds().height
				+ moduleText.getBounds().height;

		// textbox centralising
		double plusX = (lead.x - anchor.x - maxTextWidth) / 2;
		double plusY = (lead.y - anchor.y - totalTextHeight) / 2;

		Point2D.Double moduleTextAnchor = (Double) anchor.clone();
		moduleTextAnchor.x += plusX
				+ (maxTextWidth - moduleText.getBounds().width) / 2;
		moduleTextAnchor.y += plusY;
		moduleText.setBounds(moduleTextAnchor, null);

		Point2D.Double textAnchor = (Double) anchor.clone();
		textAnchor.x += plusX + (maxTextWidth - text.getBounds().width) / 2;
		textAnchor.y += plusY + moduleText.getBounds().height;
		text.setBounds(textAnchor, null);
		
		if (compIconFig != null) {
			double iconAnchorX = lead.x - 6 - compIcon.getWidth();
			double iconAnchorY = anchor.y + 6;
			double iconLeadX = iconAnchorX + compIcon.getWidth();
			double iconLeadY = iconAnchorY + compIcon.getHeight();
			compIconFig.setBounds(new Point2D.Double(iconAnchorX, iconAnchorY), new Point2D.Double(iconLeadX, iconLeadY));
		}
		
		invalidate();
	}

	@Override
	public ModuleFigure clone() {
		ModuleFigure other = (ModuleFigure) super.clone();
		other.body = body.clone();
		other.text = text.clone();
		other.moduleText = moduleText.clone();
		other.compIconFig = compIconFig.clone();
		
		other.children = new ArrayList<Figure>();
		other.children.add(other.body);
		other.children.add(other.text);
		other.children.add(other.moduleText);
		if (compIconFig != null) {
			other.children.add(other.compIconFig);
		}
		return other;
	}

}
