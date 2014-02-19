package husacct.graphics.presentation.figures;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.TextFigure;

public class ModuleFigure extends BaseFigure {
	private static final long	serialVersionUID	= -2743753116624138171L;
	private RectangleFigure		body;
	private TextFigure			moduleStereotype;
	private TextFigure			moduleName;
	
	public int					MIN_WIDTH			= 100;
	public int					MIN_HEIGHT			= 50;
	
	public ModuleFigure(String name, String type) {
		super(name);
		
		body = new RectangleFigure();
		body.set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);
		
		moduleStereotype = new TextFigure('\u00AB' + type + '\u00BB');
		
		moduleName = new TextFigure(name);
		moduleName.set(AttributeKeys.FONT_BOLD, true);
		
		children.add(body);
		children.add(moduleStereotype);
		children.add(moduleName);
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
		
		other.children = new ArrayList<Figure>();
		other.children.add(other.body);
		other.children.add(other.moduleName);
		other.children.add(other.moduleStereotype);
		
		return other;
	}
	
	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
		if (lead.x - anchor.x < MIN_WIDTH) lead.x = anchor.x + MIN_WIDTH;
		if (lead.y - anchor.y < MIN_HEIGHT) lead.y = anchor.y + MIN_HEIGHT;
		
		body.setBounds(anchor, lead);
		
		// get text sizes
		double maxTextWidth = moduleStereotype.getBounds().width;
		if (moduleName.getBounds().width > maxTextWidth) 
			maxTextWidth = moduleName.getBounds().width;
		double totalTextHeight = moduleName.getBounds().height
				+ moduleStereotype.getBounds().height;
		
		// textbox centralising
		double plusX = (lead.x - anchor.x - maxTextWidth) / 2;
		double plusY = (lead.y - anchor.y - totalTextHeight) / 2;
		
		Point2D.Double moduleTextAnchor = (Double) anchor.clone();
		moduleTextAnchor.x += plusX
				+ (maxTextWidth - moduleStereotype.getBounds().width) / 2;
		moduleTextAnchor.y += plusY;
		moduleStereotype.setBounds(moduleTextAnchor, null);
		
		Point2D.Double textAnchor = (Double) anchor.clone();
		textAnchor.x += plusX + (maxTextWidth - moduleName.getBounds().width) / 2;
		textAnchor.y += plusY + moduleStereotype.getBounds().height;
		moduleName.setBounds(textAnchor, null);
		
		invalidate();
	}
	
}
