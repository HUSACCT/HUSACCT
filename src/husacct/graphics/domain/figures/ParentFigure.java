package husacct.graphics.domain.figures;


import husacct.common.Resource;
import java.awt.Color;
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
import org.jhotdraw.draw.event.FigureEvent;
import org.jhotdraw.draw.event.FigureListener;

public class ParentFigure extends BaseFigure {
	private static final long	serialVersionUID		= 101138923385231941L;
	private static final Color	defaultContainerColor	= new Color(204, 204,
																255);
	private RectangleFigure		body;
	private TextFigure			moduleName;
	private TextFigure			moduleStereotype;
	private BufferedImage 		moduleIcon;
	private ImageFigure 		moduleIconFigure;
	
	protected int				minWidth				= 400;
	protected int				minHeight				= 400;
	
	private ArrayList<Figure>	childrenOwnImpl;
	private double				currentPositionX, currentPositionY;
	
	public ParentFigure(String uniqueName, String type) {
		super(uniqueName, uniqueName, type);
		childrenOwnImpl = new ArrayList<Figure>();
		
		body = new RectangleFigure();
		body.set(AttributeKeys.FILL_COLOR, defaultContainerColor);
		children.add(body);

		moduleName = new TextFigure(uniqueName);
		moduleName.set(AttributeKeys.FONT_BOLD, true);
		children.add(moduleName);

		moduleStereotype = new TextFigure('\u00AB' + type + '\u00BB');
		if (type.equals("facade")) {
			moduleStereotype = new TextFigure('\u00AB' + "Interface" + '\u00BB');
		}
		children.add(moduleStereotype);

		moduleIconFigure = new ImageFigure();
		moduleIconFigure.set(AttributeKeys.STROKE_WIDTH, 0.0);
		moduleIconFigure.set(AttributeKeys.FILL_COLOR, defaultContainerColor);
		try {
			URL componentImageURL = null;
			// Set Icons: First icons Intended Architecture diagram, second implemented, third default.
			if (type.toLowerCase().equals("layer")) {
				componentImageURL = Resource.get(Resource.ICON_LAYER);
			} else if (type.toLowerCase().equals("component")) {
				componentImageURL = Resource.get(Resource.ICON_COMPONENT);
			} else if (type.toLowerCase().equals("facade")) {
				componentImageURL = Resource.get(Resource.ICON_FACADE);
			} else if (type.toLowerCase().equals("subsystem")) {
				componentImageURL = Resource.get(Resource.ICON_SUBSYSTEM);
			} else if (type.toLowerCase().equals("library")) {
				componentImageURL = Resource.get(Resource.ICON_EXTERNALLIB_GREEN);
			} else if (type.toLowerCase().equals("externallibrary")) {
				componentImageURL = Resource.get(Resource.ICON_EXTERNALLIB_BLUE);
			} else if (type.toLowerCase().equals("package")) {
				componentImageURL = Resource.get(Resource.ICON_PACKAGE);
			} else if (type.toLowerCase().equals("class")) {
				componentImageURL = Resource.get(Resource.ICON_CLASS_PUBLIC);
			} else if (type.toLowerCase().equals("interface")) {
				componentImageURL = Resource.get(Resource.ICON_INTERFACE_PUBLIC);
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

		
		baseZIndex = -2;
		resetLayer();
		
		setSizeable(true);
		
		addFigureListener(new FigureListener() {
			@Override
			public void areaInvalidated(FigureEvent e) {
			}
			
			@Override
			public void attributeChanged(FigureEvent e) {
			}
			
			@Override
			public void figureAdded(FigureEvent e) {
			}
			
			@Override
			public void figureChanged(FigureEvent e) {
				double oldX = currentPositionX;
				double oldY = currentPositionY;
				double newX = ((BaseFigure) e.getFigure()).getBounds().getX();
				double newY = ((BaseFigure) e.getFigure()).getBounds().getY();
				double difX = newX - oldX;
				double difY = newY - oldY;
				for (Figure fig : childrenOwnImpl)
					((BaseFigure) fig).updateLocation(fig.getBounds().getX()
							+ difX, fig.getBounds().getY() + difY);
				currentPositionX = newX;
				currentPositionY = newY;
			}
			
			@Override
			public void figureHandlesChanged(FigureEvent e) {
			}
			
			@Override
			public void figureRemoved(FigureEvent e) {
			}
			
			@Override
			public void figureRequestRemove(FigureEvent e) {
			}
		});
	}
	
	@Override
	public boolean add(Figure figure) {
		BaseFigure bf = (BaseFigure) figure;
		bf.setInContainer(true);
		
		childrenOwnImpl.add(figure);
		figure.addFigureListener(new FigureListener() {
			@Override
			public void areaInvalidated(FigureEvent e) {
			}
			
			@Override
			public void attributeChanged(FigureEvent e) {
			}
			
			@Override
			public void figureAdded(FigureEvent e) {
			}
			
			@Override
			public void figureChanged(FigureEvent e) {
				double parentFigureStartX = getBounds().getX();
				double parentFigureWidth = getBounds().getWidth();
				double parentFigureEndX = parentFigureStartX
						+ parentFigureWidth;
				double parentFigureStartY = getBounds().getY();
				double parentFigureHeight = getBounds().getHeight();
				double parentFigureEndY = parentFigureStartY
						+ parentFigureHeight;
				
				BaseFigure childFigure = (BaseFigure) e.getFigure();
				java.awt.geom.Rectangle2D.Double figureBounds = childFigure
						.getBounds();
				double childFigureX = figureBounds.getX();
				double childFigureWidth = figureBounds.getWidth();
				double childFigureY = figureBounds.getY();
				double childFigureHeight = figureBounds.getHeight();
				
				boolean outsideLeft = childFigureX < parentFigureStartX;
				boolean outsideRight = childFigureX + childFigureWidth > parentFigureEndX;
				if (outsideLeft || outsideRight) {
					childFigure.willChange();
					if (outsideLeft) childFigure.updateLocation(
							parentFigureStartX, childFigureY);
					else
						childFigure.updateLocation(parentFigureEndX
								- childFigureWidth, childFigureY);
					childFigure.changed();
				}
				
				boolean outsideTop = childFigureY < parentFigureStartY;
				boolean outsideBottom = childFigureY + childFigureHeight > parentFigureEndY;
				if (outsideTop || outsideBottom) {
					childFigure.willChange();
					if (outsideTop) childFigure.updateLocation(childFigureX,
							parentFigureStartY);
					else
						childFigure.updateLocation(childFigureX,
								parentFigureEndY - childFigureHeight);
					childFigure.changed();
				}
			}
			
			@Override
			public void figureHandlesChanged(FigureEvent e) {
			}
			
			@Override
			public void figureRemoved(FigureEvent e) {
			}
			
			@Override
			public void figureRequestRemove(FigureEvent e) {
			}
		});
		return true;
	}
	
	@Override
	public ParentFigure clone() {
		ParentFigure other = (ParentFigure) super.clone();
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
	
	public BaseFigure[] getChildFigures() {
		return childrenOwnImpl.toArray(new BaseFigure[] {});
	}
	
	@Override
	public boolean isParent() {
		return true;
	}
	
	@Override
	public void raiseLayer() {
		// zIndex = raiseZIndex-1;
		for (BaseFigure childFigure : getChildFigures())
			childFigure.raiseLayer();
	}
	
	@Override
	public void resetLayer() {
		super.resetLayer();
		for (BaseFigure childFigure : getChildFigures())
			childFigure.resetLayer();
	}
	
	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
		// Initialize element sizes
		double nameWidth = moduleName.getBounds().width;
		double textGap = 5;
		double marginY = 4;
		double iconMarginX = 3;
		double stereotypeWidth = moduleStereotype.getBounds().width;
		double iconWidth = 0;
		if (moduleIcon != null) {
			 iconWidth = moduleIcon.getWidth();
		}
		double totalHeaderWidth = textGap + nameWidth + textGap + stereotypeWidth + textGap + iconWidth + iconMarginX;

		// Set bounds body
		if (lead.x - anchor.x < minWidth) lead.x = anchor.x + minWidth;
		if (lead.y - anchor.y < minHeight) lead.y = anchor.y + minHeight;
		if (lead.x - anchor.x < totalHeaderWidth) lead.x = anchor.x + totalHeaderWidth;
		body.setBounds(anchor, lead);
		
		// Position name
		double namePlusX = ((lead.x - anchor.x - iconWidth - iconMarginX) - (nameWidth + textGap + stereotypeWidth)) / 2;
		Point2D.Double nameTextAnchor = (Double) anchor.clone();
		nameTextAnchor.x += namePlusX;
		nameTextAnchor.y += marginY;
		moduleName.setBounds(nameTextAnchor, null);

		// Position stereotype
		Point2D.Double stereoTypeTextAnchor = (Double) anchor.clone();
		stereoTypeTextAnchor.x = nameTextAnchor.x + nameWidth + textGap;
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
		
		invalidate();
	}
	
	public void setMinimalSizes(int minWidth, int minHeight) {
		this.minWidth = minWidth;
		this.minHeight = minHeight;
	}
	
	public void updateBounds(Point2D.Double anchor, Point2D.Double lead) {
		willChange();
		setBounds(anchor, lead);
		changed();
	}

	@Override
    public String toString() {
        String representation = "";
        representation += "\name: " + super.name;
        representation += "\nType: " + type;
        representation += "\n";
        return representation;
    }
}
