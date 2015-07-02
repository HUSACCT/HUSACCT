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
	private static final long	serialVersionUID	= -2743753116624138171L;
	private RectangleFigure		body;
	private TextFigure			moduleName;
	private TextFigure			moduleStereotype;
	private BufferedImage 		moduleIcon;
	private ImageFigure 		moduleIconFigure;
	private String type;					

	public int					MIN_WIDTH			= 100;
	public int					MIN_HEIGHT			= 65;
	
	public ModuleFigure(String name, String stereotype) {
		super(name);
		this.type = stereotype;
		if (type.toLowerCase().equals("facade")) {
			type = "Interface";
		}
		
		body = new RectangleFigure();
		body.set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);
		children.add(body);
		
		moduleStereotype = new TextFigure('\u00AB' + type + '\u00BB');
		children.add(moduleStereotype);
		
		moduleName = new TextFigure(name);
		moduleName.set(AttributeKeys.FONT_BOLD, true);
		children.add(moduleName);
		
		moduleIconFigure = new ImageFigure();
		moduleIconFigure.set(AttributeKeys.STROKE_WIDTH, 0.0);
		moduleIconFigure.set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);
		try {
			URL componentImageURL = null;
			if (type.toLowerCase().equals("layer")) {
				componentImageURL = Resource.get(Resource.ICON_LAYER);
			} else if (type.toLowerCase().equals("component")) {
				componentImageURL = Resource.get(Resource.ICON_COMPONENT);
			} else if (type.toLowerCase().equals("subsystem")) {
				componentImageURL = Resource.get(Resource.ICON_SUBSYSTEM);
			} else if (type.toLowerCase().equals("library")) {
				componentImageURL = Resource.get(Resource.ICON_EXTERNALLIB_GREEN);
			} else if (type.toLowerCase().equals("externallibrary")) {
				componentImageURL = Resource.get(Resource.ICON_EXTERNALLIB_BLUE);
			} else if (type.toLowerCase().equals("interface")) {
				componentImageURL = Resource.get(Resource.ICON_FACADE);
				type = "Interface";
			} else{
				componentImageURL = Resource.get(Resource.ICON_MODULE);
			}
			if(componentImageURL != null){
				moduleIcon = ImageIO.read(componentImageURL);
				moduleIconFigure.setImage(null, moduleIcon);
				children.add(moduleIconFigure);
			}
		} catch (Exception e) {
			moduleIconFigure = null;
			Logger.getLogger(this.getClass()).warn("failed to load component icon image file");
		}
	}
	
	@Override
	public boolean isModule() {
		return true;
	}
	
	@Override
	public ModuleFigure clone() {
		ModuleFigure other = (ModuleFigure) super.clone();
		other.body = body.clone();
		other.moduleName = moduleName.clone();
		other.moduleStereotype = moduleStereotype.clone();
		other.moduleIconFigure = moduleIconFigure.clone();
		
		other.children = new ArrayList<Figure>();
		other.children.add(other.body);
		other.children.add(other.moduleName);
		other.children.add(other.moduleStereotype);
		if (moduleIconFigure != null)
			other.children.add(other.moduleIconFigure);
		
		return other;
	}
	
	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
		if (lead.x - anchor.x < MIN_WIDTH) lead.x = anchor.x + MIN_WIDTH;
		if (lead.y - anchor.y < MIN_HEIGHT) lead.y = anchor.y + MIN_HEIGHT;
		
		// Calculate max text width +20 extra
		double maxTextWidth;
		if(moduleName.getBounds().width >= moduleStereotype.getBounds().width) 
			maxTextWidth = moduleName.getBounds().width + 20;
		else
			maxTextWidth = moduleStereotype.getBounds().width + 20;
		if ((lead.x - anchor.x) < maxTextWidth)
			lead.x = anchor.x + maxTextWidth;

		body.setBounds(anchor, lead);
		
		// Calculate max text height
		double totalTextHeight = moduleName.getBounds().height + moduleStereotype.getBounds().height;
		
		// Centralize text boxes
		double plusX = ((lead.x - anchor.x) - maxTextWidth) / 2;
		double plusY = ((lead.y - anchor.y) - totalTextHeight) / 2;
		
		// Centralize moduleStereotype
		Point2D.Double moduleStereoTypeTextAnchor = (Double) anchor.clone();
		moduleStereoTypeTextAnchor.x += plusX	+ (maxTextWidth - moduleStereotype.getBounds().width) / 2;
		moduleStereoTypeTextAnchor.y += plusY;
		moduleStereotype.setBounds(moduleStereoTypeTextAnchor, null);
		
		// Centralize moduleName
		Point2D.Double moduleNametextAnchor = (Double) anchor.clone();
		moduleNametextAnchor.x += plusX + (maxTextWidth - moduleName.getBounds().width) / 2;
		moduleNametextAnchor.y += plusY + moduleStereotype.getBounds().height;
		moduleName.setBounds(moduleNametextAnchor, null);

		if (moduleIconFigure != null) {
			double iconAnchorX = lead.x - 3 - moduleIcon.getWidth();
			double iconAnchorY = anchor.y + 4;
			double iconLeadX = iconAnchorX + moduleIcon.getWidth();
			double iconLeadY = iconAnchorY + moduleIcon.getHeight();
			moduleIconFigure.setBounds(new Point2D.Double(iconAnchorX, iconAnchorY), new Point2D.Double(iconLeadX, iconLeadY));
		}
		
		invalidate();
	}
	
	@Override
    public String toString() {
        String representation = "";
        representation += "\nName: " + super.name;
        representation += "\nType: " + type;
        representation += "\n";
        return representation;
    }
}
