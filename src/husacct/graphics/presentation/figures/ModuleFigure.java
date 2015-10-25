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

	protected int				minWidth				= 100;
	protected int				minHeight				= 50;
	
	public ModuleFigure(String name, String uniqueName, String moduleType) {
		super(name, uniqueName, moduleType.toLowerCase());
		
		body = new RectangleFigure();
		body.set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);
		children.add(body);
		
		moduleName = new TextFigure(name);
		moduleName.set(AttributeKeys.FONT_BOLD, true);
		children.add(moduleName);
		
		moduleStereotype = new TextFigure('\u00AB' + type + '\u00BB');
		if (type.equals("facade")) {
			moduleStereotype = new TextFigure('\u00AB' + "Interface" + '\u00BB');
		}
		children.add(moduleStereotype);
		
		moduleIconFigure = new ImageFigure();
		moduleIconFigure.set(AttributeKeys.STROKE_WIDTH, 0.0);
		moduleIconFigure.set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);
		try {
			URL componentImageURL = null;
			// Set Icons: First icons Intended Architecture diagram, second implemented, third default.
			if (type.equals("layer")) {
				componentImageURL = Resource.get(Resource.ICON_LAYER);
			} else if (type.equals("component")) {
				componentImageURL = Resource.get(Resource.ICON_COMPONENT);
			} else if (type.equals("facade")) {
				componentImageURL = Resource.get(Resource.ICON_FACADE);
			} else if (type.equals("subsystem")) {
				componentImageURL = Resource.get(Resource.ICON_SUBSYSTEM);
			} else if (type.equals("library")) {
				componentImageURL = Resource.get(Resource.ICON_EXTERNALLIB_GREEN);
			} else if (type.equals("externallibrary")) {
				componentImageURL = Resource.get(Resource.ICON_EXTERNALLIB_BLUE);
			} else if (type.equals("package")) {
				componentImageURL = Resource.get(Resource.ICON_PACKAGE);
			} else if (type.equals("class") || type.equals("abstract")) {
				componentImageURL = Resource.get(Resource.ICON_CLASS_PUBLIC);
			} else if (type.equals("interface")) {
				componentImageURL = Resource.get(Resource.ICON_INTERFACE_PUBLIC);
			} else if (type.equals("project")) {
				componentImageURL = Resource.get(Resource.ICONSET_PATH);
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
		// Initialize element sizes
		double textMargin = 5;
		double nameWidth = moduleName.getBounds().width;
		double stereotypeWidth = moduleStereotype.getBounds().width;
		double iconMarginX = 3;
		double marginY = 4;
		double iconWidth = 0;
		if (moduleIcon != null) {
			 iconWidth = moduleIcon.getWidth();
		}
		double totalHeaderWidth = textMargin + stereotypeWidth + textMargin + iconWidth + iconMarginX;
		double totalNameLineWidth = textMargin + nameWidth + textMargin;

		// Set bounds body
		if (lead.x - anchor.x < minWidth) lead.x = anchor.x + minWidth;
		if (lead.y - anchor.y < minHeight) lead.y = anchor.y + minHeight;
		if (lead.x - anchor.x < totalHeaderWidth) lead.x = anchor.x + totalHeaderWidth;
		if (totalNameLineWidth > totalHeaderWidth) lead.x = anchor.x + totalNameLineWidth;
		body.setBounds(anchor, lead);
		
		// Position stereotype
		double stereotypePlusX = ((lead.x - anchor.x - iconWidth - iconMarginX) - (textMargin + stereotypeWidth + textMargin)) / 2;
		Point2D.Double stereoTypeTextAnchor = (Double) anchor.clone();
		stereoTypeTextAnchor.x += stereotypePlusX + textMargin;
		stereoTypeTextAnchor.y += marginY;
		moduleStereotype.setBounds(stereoTypeTextAnchor, null);
		
		// Position icon
		if (moduleIconFigure != null) {
			double iconAnchorX = lead.x - iconMarginX - iconWidth;
			double iconAnchorY = anchor.y + marginY;
			double iconLeadX = iconAnchorX + iconWidth;
			double iconLeadY = iconAnchorY + moduleIcon.getHeight();
			moduleIconFigure.setBounds(new Point2D.Double(iconAnchorX, iconAnchorY), new Point2D.Double(iconLeadX, iconLeadY));
		}
		
		// Position name
		double namePlusX = ((lead.x - anchor.x) - (textMargin + nameWidth + textMargin)) / 2;
		double namePlusY = ((lead.y - anchor.y) - (marginY + moduleStereotype.getBounds().height) - moduleName.getBounds().height) / 2;
		Point2D.Double nameTextAnchor = (Double) anchor.clone();
		nameTextAnchor.x += namePlusX + textMargin;
		nameTextAnchor.y = anchor.y + (moduleStereotype.getBounds().height) + namePlusY;
		moduleName.setBounds(nameTextAnchor, null);

		invalidate();

		
		/*
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
		*/
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
