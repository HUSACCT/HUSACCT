package husacct.graphics.presentation.figures;

import husacct.graphics.task.layout.ContainerLayoutStrategy;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.draw.event.FigureEvent;
import org.jhotdraw.draw.event.FigureListener;

public class ParentFigure extends BaseFigure {
	private static final long serialVersionUID = 101138923385231941L;
	private RectangleFigure body;
	private TextFigure text;

	protected int minWidth = 400;
	protected int minHeight = 400;
	protected int minPadding = 20;
	
	private ArrayList<Figure> childrenOwnImpl;
	private double currentPositionX, currentPositionY;

	public ParentFigure(String name) {
		super(name);
		childrenOwnImpl = new ArrayList<Figure>();

		body = new RectangleFigure();
		text = new TextFigure(name);
		text.set(AttributeKeys.FONT_BOLD, true);
		children.add(body);
		children.add(text);

		body.set(AttributeKeys.FILL_COLOR, new Color(204,204,255));
		
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
			public void figureHandlesChanged(FigureEvent e) {
			}

			@Override
			public void figureChanged(FigureEvent e){
				double oldX = currentPositionX;
				double oldY = currentPositionY;
				double newX = ((BaseFigure)e.getFigure()).getBounds().getX();
				double newY = ((BaseFigure)e.getFigure()).getBounds().getY();
				double difX = newX - oldX;
				double difY = newY - oldY;
				for(Figure fig : childrenOwnImpl){
					((BaseFigure)fig).updateLocation(fig.getBounds().getX()+difX, fig.getBounds().getY()+difY);
				}
				currentPositionX = newX;
				currentPositionY = newY;
			}

			@Override
			public void figureAdded(FigureEvent e) {
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
	public void setBounds(Point2D.Double anchor, Point2D.Double lead) {
		if ((lead.x - anchor.x) < minWidth) {
			lead.x = anchor.x + minWidth;
		}
		if ((lead.y - anchor.y) < minHeight) {
			lead.y = anchor.y + minHeight;
		}

		body.setBounds(anchor, lead);

		// textbox centralising
		double plusX = (((lead.x - anchor.x) - text.getBounds().width) / 2);

		Point2D.Double textAnchor = (Double) anchor.clone();
		textAnchor.x += plusX;
		text.setBounds(textAnchor, null);

		invalidate();
	}

	@Override
	public ParentFigure clone() {
		ParentFigure other = (ParentFigure) super.clone();
		other.body = body.clone();
		other.text = text.clone();

		other.children = new ArrayList<Figure>();
		other.children.add(other.body);
		other.children.add(other.text);

		return other;
	}
	
	public BaseFigure[] getChildFigures(){
		return childrenOwnImpl.toArray(new BaseFigure[]{});
	}
	
	public void updateLayout() {
		ContainerLayoutStrategy cls = new ContainerLayoutStrategy(this);
		cls.doLayout(0, 0);
		
		Rectangle newSize = new Rectangle();
		for (Figure f : childrenOwnImpl) {
			Rectangle2D.Double bounds = f.getBounds();
			
			newSize.add(new Point2D.Double(bounds.x + bounds.width, bounds.y + bounds.height));
		}
		minWidth = newSize.width + minPadding;
		minHeight = newSize.height + minPadding;
		
		Rectangle2D.Double bounds = getBounds();
		Point2D.Double anchor = new Point2D.Double(bounds.x, bounds.y);
		Point2D.Double lead = new Point2D.Double(anchor.x + minWidth + 10, anchor.y + minHeight + 10);
		
		
		willChange();
		setBounds(anchor, lead);
		changed();
	}
	
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
			public void figureHandlesChanged(FigureEvent e) {
			}

			@Override
			public void figureChanged(FigureEvent e) {
				double parentFigureStartX = getBounds().getX();
				double parentFigureWidth = getBounds().getWidth();
				double parentFigureEndX = parentFigureStartX + parentFigureWidth;
				double parentFigureStartY = getBounds().getY();
				double parentFigureHeight = getBounds().getHeight();
				double parentFigureEndY = parentFigureStartY + parentFigureHeight;

				BaseFigure childFigure = ((BaseFigure) e.getFigure());
				java.awt.geom.Rectangle2D.Double figureBounds = childFigure.getBounds();
				double childFigureX = figureBounds.getX();
				double childFigureWidth = figureBounds.getWidth();
				double childFigureY = figureBounds.getY();
				double childFigureHeight = figureBounds.getHeight();

				boolean outsideLeft = childFigureX < parentFigureStartX;
				boolean outsideRight = (childFigureX + childFigureWidth) > parentFigureEndX;
				if (outsideLeft || outsideRight) {
					childFigure.willChange();
					if (outsideLeft) {
						childFigure.updateLocation(parentFigureStartX, childFigureY);
					} else {
						childFigure.updateLocation(parentFigureEndX - childFigureWidth, childFigureY);
					}
					childFigure.changed();
				}

				boolean outsideTop = childFigureY < parentFigureStartY;
				boolean outsideBottom = (childFigureY + childFigureHeight) > parentFigureEndY;
				if (outsideTop || outsideBottom) {
					childFigure.willChange();
					if (outsideTop) {
						childFigure.updateLocation(childFigureX, parentFigureStartY);
					} else {
						childFigure.updateLocation(childFigureX, parentFigureEndY - childFigureHeight);
					}
					childFigure.changed();
				}
			}

			@Override
			public void figureAdded(FigureEvent e) {
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
	public boolean isParent() {
		return true;
	}
	
	@Override
	public void raiseLayer(){
//		zIndex = raiseZIndex-1;
		for(BaseFigure childFigure : getChildFigures()){
			childFigure.raiseLayer();
		}
	}
	
	@Override
	public void resetLayer(){
		super.resetLayer();
		for(BaseFigure childFigure : getChildFigures()){
			childFigure.resetLayer();
		}
	}
}
