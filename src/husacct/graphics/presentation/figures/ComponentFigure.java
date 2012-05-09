package husacct.graphics.presentation.figures;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.ImageFigure;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.TextFigure;

public class ComponentFigure extends NamedFigure {
	private static final long serialVersionUID = -3019979543942866990L;
	private RectangleFigure body;
	private TextFigure text;
	private BufferedImage compIcon;
	private ImageFigure compIconFig;

	protected int minWidth = 130;
	protected int minHeight = 90;

	public ComponentFigure(String name) {
		super(name, false);

		body = new RectangleFigure();
		body.set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);
		children.add(body);
		
		text = new TextFigure(name);
		text.set(AttributeKeys.FONT_BOLD, true);
		children.add(text);

		compIconFig = new ImageFigure();
		compIconFig.set(AttributeKeys.STROKE_WIDTH, 0.0);
		compIconFig.set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);
		
		try	{
			InputStream iconStream = ClassLoader.getSystemResourceAsStream("husacct/graphics/presentation/resources/component.png");
			if(iconStream == null) {
				throw new IOException();
			}
			compIcon = ImageIO.read(iconStream);
			
			compIconFig.setImage(null, compIcon);
			children.add(compIconFig);
		}
		catch(IOException e) {
			compIconFig = null;
			Logger.getLogger(this.getClass()).warn("failed to load component icon image file");
		}
		
	}

	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
		if ((lead.x - anchor.x) < this.minWidth) {
			lead.x = anchor.x + this.minWidth;
		}
		if ((lead.y - anchor.y) < this.minHeight) {
			lead.y = anchor.y + this.minHeight;
		}
		
		// bigger than text
		double requestTextWidth = this.text.getBounds().width+10;
		if((lead.x - anchor.x) < requestTextWidth) {
			lead.x = anchor.x + requestTextWidth;
		}

		body.setBounds(anchor, lead);

		// textbox centralising
		double plusX = (((lead.x - anchor.x) - this.text.getBounds().width) / 2);
		double plusY = (((lead.y - anchor.y) - this.text.getBounds().height) / 2);

		Point2D.Double textAnchor = (Double) anchor.clone();
		textAnchor.x += plusX;
		textAnchor.y += plusY;
		text.setBounds(textAnchor, null);
		
		if(this.compIconFig != null) {
			double iconAnchorX = lead.x - 6 - this.compIcon.getWidth();
			double iconAnchorY = anchor.y + 6;
			double iconLeadX = iconAnchorX + this.compIcon.getWidth();
			double iconLeadY = iconAnchorY + this.compIcon.getHeight();
			this.compIconFig.setBounds(
					new Point2D.Double(iconAnchorX, iconAnchorY), 
					new Point2D.Double(iconLeadX, iconLeadY));
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
		if(compIconFig != null) {
			other.children.add(other.compIconFig);
		}

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
