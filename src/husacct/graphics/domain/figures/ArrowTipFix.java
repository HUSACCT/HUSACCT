package husacct.graphics.domain.figures;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.Figure;
import org.jhotdraw.draw.decoration.ArrowTip;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Created by Lars on 29-4-2016.
 */
public class ArrowTipFix extends ArrowTip {


    public ArrowTipFix(double angle, double outerRadius, double innerRadius, boolean isFilled, boolean isStroked, boolean isSolid) {
        super(angle, outerRadius, innerRadius, isFilled, isStroked, isSolid);
    }

    public ArrowTipFix(double angle, double outerRadius, double innerRadius) {
        super(angle, outerRadius, innerRadius);
    }

    public ArrowTipFix() {
        super();
    }

    @Override
    public void draw(Graphics2D g, Figure f, Point2D.Double p1, Point2D.Double p2) {
        java.awt.geom.Path2D.Double path = getTransformedDecoratorPath(f, p1, p2);
        Color color;
        if(isFilled()) {
            if(isSolid()) {
                color = f.get(AttributeKeys.STROKE_COLOR);
            } else {
                color = f.get(AttributeKeys.FILL_COLOR);
            }

            if(color != null) {
                g.setColor(color);
                g.fill(path);
            }
        }

        if(isStroked()) {
            color = f.get(AttributeKeys.STROKE_COLOR);
            if(color != null) {
                g.setColor(color);

                // this is the only change compared to ArrowTip:
                Stroke stroke = isSolid() ? new BasicStroke() : AttributeKeys.getStroke(f);
                
                g.setStroke(stroke);
                g.draw(path);
            }
        }
    }

    private java.awt.geom.Path2D.Double getTransformedDecoratorPath(Figure f, Point2D.Double p1, Point2D.Double p2) {
        java.awt.geom.Path2D.Double path = this.getDecoratorPath(f);
        double strokeWidth = f.get(AttributeKeys.STROKE_WIDTH).doubleValue();
        AffineTransform transform = new AffineTransform();
        transform.translate(p1.x, p1.y);
        transform.rotate(Math.atan2(p1.x - p2.x, p2.y - p1.y));
        if(strokeWidth > 1.0D) {
            transform.scale(1.0D + (strokeWidth - 1.0D) / 2.0D, 1.0D + (strokeWidth - 1.0D) / 2.0D);
        }

        path.transform(transform);
        return path;
    }
}
