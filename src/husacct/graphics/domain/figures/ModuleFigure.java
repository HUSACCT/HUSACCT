package husacct.graphics.domain.figures;


import husacct.common.Resource;
import org.apache.log4j.Logger;
import org.jhotdraw.draw.*;

import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ModuleFigure extends BaseFigure {
	private static final long	serialVersionUID	= -2743753116624138171L;
	public static final double STROKE_WIDTH = 0.0;
	private RectangleFigure		body;
	private TextFigure			moduleName;
	private TextFigure			moduleStereotype;
	private BufferedImage 		moduleIcon;
	private ImageFigure 		moduleIconFigure;
	private BufferedImage		hasRulesIcon;
	private ImageFigure			hasRulesIconFigure;


	protected int				minWidth				= 100;
	protected int				minHeight				= 65;
	
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
		moduleIconFigure.set(AttributeKeys.STROKE_WIDTH, STROKE_WIDTH);
		moduleIconFigure.set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);
		try {
			URL componentImageURL = null;
			// Set Icons: First icons Intended Architecture diagram, second implemented, third default.
			switch(type) {
				case "layer":
					componentImageURL = Resource.get(Resource.ICON_LAYER);
					break;
				case "component":
					componentImageURL = Resource.get(Resource.ICON_COMPONENT);
					break;
				case "facade":
					componentImageURL = Resource.get(Resource.ICON_FACADE);
					break;
				case "subsystem":
					componentImageURL = Resource.get(Resource.ICON_SUBSYSTEM);
					break;
				case "library":
					componentImageURL = Resource.get(Resource.ICON_EXTERNALLIB_GREEN);
					break;
				case "externallibrary":
					componentImageURL = Resource.get(Resource.ICON_EXTERNALLIB_BLUE);
					break;
				case "package":
					componentImageURL = Resource.get(Resource.ICON_PACKAGE);
					break;
				case "class":
					componentImageURL = Resource.get(Resource.ICON_CLASS_PUBLIC);
					break;
				case "abstract":
					componentImageURL = Resource.get(Resource.ICON_CLASS_PUBLIC);
					break;
				case "interface":
					componentImageURL = Resource.get(Resource.ICON_INTERFACE_PUBLIC);
					break;
				case "project":
					componentImageURL = Resource.get(Resource.ICONSET_PATH);
					break;
				default:
					componentImageURL = Resource.get(Resource.ICON_MODULE);
			}
			if(componentImageURL != null){
				moduleIcon = ImageIO.read(componentImageURL);
				moduleIconFigure.setImage(null, moduleIcon);
				children.add(moduleIconFigure);
			}
		} catch (IOException e) {
			moduleIconFigure = null;
			Logger.getLogger(this.getClass()).warn("failed to load component icon image file");
		}

		try {
			hasRulesIconFigure = new ImageFigure();
			hasRulesIconFigure.set(AttributeKeys.STROKE_WIDTH, STROKE_WIDTH);
			hasRulesIconFigure.set(AttributeKeys.STROKE_WIDTH, STROKE_WIDTH);
			hasRulesIconFigure.set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);
			hasRulesIconFigure.setVisible(false);
			hasRulesIcon = ImageIO.read(Resource.get(Resource.ICON_INFO));
			hasRulesIconFigure.setImage(null, hasRulesIcon);
			children.add(hasRulesIconFigure);
		} catch (IOException e) {
			Logger.getLogger(this.getClass()).warn("failed to load rule icon image file");
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
		other.hasRulesIconFigure = hasRulesIconFigure.clone();
		
		other.children = new ArrayList<Figure>();
		other.children.add(other.body);
		other.children.add(other.moduleName);
		other.children.add(other.moduleStereotype);
		if (other.moduleIconFigure != null)
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

		if (hasRulesIconFigure != null) {
			double iconAnchorX = anchor.x + iconMarginX;
			double iconAnchorY = lead.y - hasRulesIcon.getHeight() - iconMarginX;
			double iconLeadX = iconAnchorX + iconWidth;
			double iconLeadY = iconAnchorY + hasRulesIcon.getHeight();
			hasRulesIconFigure.setBounds(new Point2D.Double(iconAnchorX, iconAnchorY), new Point2D.Double(iconLeadX, iconLeadY));
		}
		
		// Position name
		double namePlusX = ((lead.x - anchor.x) - (textMargin + nameWidth + textMargin)) / 2;
		double namePlusY = ((lead.y - anchor.y) - (marginY + moduleStereotype.getBounds().height) - moduleName.getBounds().height) / 2;
		Point2D.Double nameTextAnchor = (Double) anchor.clone();
		nameTextAnchor.x += namePlusX + textMargin;
		nameTextAnchor.y = anchor.y + (moduleStereotype.getBounds().height) + namePlusY;
		moduleName.setBounds(nameTextAnchor, null);

		invalidate();
	}

	public void setVisibilityOfRulesIcon(boolean visible) {
		hasRulesIconFigure.setVisible(visible);
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
