package husacct.graphics.presentation.figures;

import husacct.common.Resource;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.ImageFigure;
import org.jhotdraw.draw.TextFigure;

public class InterfaceFigure extends ClassFigure {
	private static final long serialVersionUID = 3150088710360391913L;
	private TextFigure interfaceTextFigure;

	private BufferedImage compIcon;
	private ImageFigure compIconFig;
	
	public InterfaceFigure(String figureName) {
		super(figureName);

		interfaceTextFigure = new TextFigure("\u00ABinterface\u00BB");
		children.add(interfaceTextFigure);
		
		compIconFig = new ImageFigure();
		compIconFig.set(AttributeKeys.STROKE_WIDTH, 0.0);
		compIconFig.set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);

		try {
			//TODO There needs to be a icon for Projects
			URL componentImageURL = Resource.get(Resource.ICON_INTERFACE_PUBLIC);
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
		double textWidth = classNameText.getBounds().width + interfaceTextFigure.getBounds().width;
		double requestTextWidth = textWidth + 10;
		if ((lead.x - anchor.x) < requestTextWidth) {
			lead.x = anchor.x + requestTextWidth;
		}

		double width = lead.x - anchor.x;
		double totalHeight = lead.y - anchor.y;
		double middleHeight = Math.floor(totalHeight / 3);
		double bottomHeight = Math.floor(totalHeight / 3);
		double topHeight = totalHeight - middleHeight - bottomHeight;

		top.setBounds(anchor, new Point2D.Double(anchor.x + width, anchor.y + topHeight));
		middle.setBounds(new Point2D.Double(anchor.x, anchor.y + topHeight), new Point2D.Double(anchor.x + width, anchor.y + topHeight + middleHeight));
		bottom.setBounds(new Point2D.Double(anchor.x, anchor.y + topHeight + middleHeight), new Point2D.Double(anchor.x + width, anchor.y + topHeight + middleHeight + bottomHeight));

		// textbox centralising
		double plusX = ((top.getBounds().width - textWidth) / 2);
		double plusY = ((top.getBounds().height - classNameText.getBounds().height) / 2);

		Point2D.Double interfaceTextFigureAchor = (Double) anchor.clone();
		interfaceTextFigureAchor.x += plusX;
		interfaceTextFigureAchor.y += plusY;
		interfaceTextFigure.setBounds(interfaceTextFigureAchor, null);

		Point2D.Double classNameTextFigureAnchor = (Double) anchor.clone();
		classNameTextFigureAnchor.x += plusX + interfaceTextFigure.getBounds().width + 2;
		classNameTextFigureAnchor.y += plusY;
		classNameText.setBounds(classNameTextFigureAnchor, null);

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
	public InterfaceFigure clone() {
		InterfaceFigure other = (InterfaceFigure) super.clone();

		other.interfaceTextFigure = interfaceTextFigure.clone();
		other.compIconFig = compIconFig.clone();
		
		other.children.add(other.interfaceTextFigure);
		if (compIconFig != null) {
			other.children.add(other.compIconFig);
		}
		
		return other;
	}
}
