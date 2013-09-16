package husacct.graphics.presentation.figures;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import org.jhotdraw.draw.TextFigure;

public class InterfaceFigure extends ClassFigure {
	private static final long	serialVersionUID	= 3150088710360391913L;
	private TextFigure			interfaceTextFigure;
	
	public InterfaceFigure(String figureName) {
		super(figureName);
		
		interfaceTextFigure = new TextFigure("\u00ABinterface\u00BB");
		children.add(interfaceTextFigure);
	}
	
	@Override
	public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
		if ((lead.x - anchor.x) < MIN_WIDTH) {
			lead.x = anchor.x + MIN_WIDTH;
		}
		if ((lead.y - anchor.y) < MIN_HEIGHT) {
			lead.y = anchor.y + MIN_HEIGHT;
		}
		double textWidth = classNameText.getBounds().width
				+ interfaceTextFigure.getBounds().width;
		double requestTextWidth = textWidth + 10;
		if ((lead.x - anchor.x) < requestTextWidth) {
			lead.x = anchor.x + requestTextWidth;
		}
		
		double width = lead.x - anchor.x;
		double totalHeight = lead.y - anchor.y;
		double middleHeight = Math.floor(totalHeight / 3);
		double bottomHeight = Math.floor(totalHeight / 3);
		double topHeight = totalHeight - middleHeight - bottomHeight;
		
		top.setBounds(anchor, new Point2D.Double(anchor.x + width, anchor.y
				+ topHeight));
		middle.setBounds(new Point2D.Double(anchor.x, anchor.y + topHeight),
				new Point2D.Double(anchor.x + width, anchor.y + topHeight
						+ middleHeight));
		bottom.setBounds(new Point2D.Double(anchor.x, anchor.y + topHeight
				+ middleHeight), new Point2D.Double(anchor.x + width, anchor.y
				+ topHeight + middleHeight + bottomHeight));
		
		// textbox centralising
		double plusX = ((top.getBounds().width - textWidth) / 2);
		double plusY = ((top.getBounds().height - classNameText.getBounds().height) / 2);
		
		Point2D.Double interfaceTextFigureAchor = (Double) anchor.clone();
		interfaceTextFigureAchor.x += plusX;
		interfaceTextFigureAchor.y += plusY;
		interfaceTextFigure.setBounds(interfaceTextFigureAchor, null);
		
		Point2D.Double classNameTextFigureAnchor = (Double) anchor.clone();
		classNameTextFigureAnchor.x += plusX
				+ interfaceTextFigure.getBounds().width + 2;
		classNameTextFigureAnchor.y += plusY;
		classNameText.setBounds(classNameTextFigureAnchor, null);
		
		invalidate();
	}
	
	@Override
	public InterfaceFigure clone() {
		InterfaceFigure other = (InterfaceFigure) super.clone();
		
		other.interfaceTextFigure = interfaceTextFigure.clone();
		
		other.children.add(other.interfaceTextFigure);
		
		return other;
	}
}
