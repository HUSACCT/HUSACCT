package husacct.graphics.util;

import java.awt.geom.Point2D;

public class Geom {

    /**
     * http://stackoverflow.com/questions/2676719/calculating-the-angle-between-
     * the-line-defined-by-two-points
     */
    public static double getAngle(Point2D.Double point1, Point2D.Double point2) {
        double dx = point1.x - point2.x;
        // Minus to correct for coord re-mapping
        double dy = -(point1.y - point2.y);

        double inRads = Math.atan2(dy, dx);

        // We need to map to coord system when 0 degree is at 3 O'clock, 270 at
        // 12 O'clock
        if (inRads < 0) {
            inRads = Math.abs(inRads);
        } else {
            inRads = 2 * Math.PI - inRads;
        }

        return Math.toDegrees(inRads);
    }

    public static Point2D.Double getPointMovementFromLineAngle(Point2D.Double pointStart, Point2D.Double pointEnd, double movement) {
        double width;
        if (pointStart.x > pointEnd.x) {
            width = pointStart.x - pointEnd.x;
        } else {
            width = pointEnd.x - pointStart.x;
        }

        double height;
        if (pointStart.y > pointEnd.y) {
            height = pointStart.y - pointEnd.y;
        } else {
            height = pointEnd.y - pointStart.y;
        }

        double diffX = width / (width + height);
        double diffY = height / (width + height);

        double movementX = 0;
        double movementY = 0;

        double angle = getAngle(pointStart, pointEnd);
        if (angle < 90 || (angle > 180 && angle < 270)) {
            movementX += diffY * movement;
            movementY -= diffX * movement;
        } else {
            movementX += diffY * movement;
            movementY += diffX * movement;
        }

        return new Point2D.Double(movementX, movementY);
    }
}
