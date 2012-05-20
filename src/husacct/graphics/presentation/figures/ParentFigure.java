package husacct.graphics.presentation.figures;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
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

	protected int minWidth = 300;
	protected int minHeight = 300;
	
	private ArrayList<Figure> childrenOwnImpl;
	private double x, y;

	public ParentFigure(String name) {
		super(name);
		childrenOwnImpl = new ArrayList<Figure>();

		body = new RectangleFigure();
		text = new TextFigure(name);
		text.set(AttributeKeys.FONT_BOLD, true);
		children.add(body);
		children.add(text);

		body.set(AttributeKeys.FILL_COLOR, defaultBackgroundColor);
		
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
				double oldX = x;
				double oldY = y;
				double newX = ((BaseFigure)e.getFigure()).getBounds().getX();
				double newY = ((BaseFigure)e.getFigure()).getBounds().getY();
				double difX = newX - oldX;
				double difY = newY - oldY;
				for(Figure fig : childrenOwnImpl){
					((BaseFigure)fig).updateLocation(fig.getBounds().getX()+difX, fig.getBounds().getY()+difY);
				}
				x = newX;
				y = newY;
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
		double plusY = (((lead.y - anchor.y) - text.getBounds().height) / 2);

		Point2D.Double textAnchor = (Double) anchor.clone();
		textAnchor.x += plusX;
		textAnchor.y += plusY;
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
	
	public void addChildFigure(Figure figure) {
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
				double parentStartX = getBounds().getX();
				double parentWidth = getBounds().getWidth();
				double parentEndX = parentStartX + parentWidth;
				double parentStartY = getBounds().getY();
				double parentHeight = getBounds().getHeight();
				double parentEndY = parentStartY + parentHeight;

				BaseFigure figure = ((BaseFigure) e.getFigure());
				java.awt.geom.Rectangle2D.Double figureBounds = figure.getBounds();
				double figureX = figureBounds.getX();
				double figureWidth = figureBounds.getWidth();
				double figureY = figureBounds.getY();
				double figureHeight = figureBounds.getHeight();

				boolean outsideLeft = figureX < parentStartX;
				boolean outsideRight = (figureX + figureWidth) > parentEndX;
				if (outsideLeft || outsideRight) {
					figure.willChange();
					if (outsideLeft) {
						figure.updateLocation(parentStartX + 20, figureY);
					} else {
						figure.updateLocation(parentEndX - figureWidth - 20, figureY);
					}
					figure.changed();
				}

				boolean outsideTop = figureY < parentStartY;
				boolean outsideBottom = (figureY + figureHeight) > parentEndY;
				if (outsideTop || outsideBottom) {
					figure.willChange();
					if (outsideTop) {
						figure.updateLocation(figureX, parentStartY + 20);
					} else {
						figure.updateLocation(figureX, parentEndY - figureHeight - 20);
					}
					figure.changed();
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
	}

	@Override
	public int getLayer() {
		return -2;
	}

	@Override
	public boolean isModule() {
		return false;
	}

	@Override
	public boolean isLine() {
		return false;
	}
}
