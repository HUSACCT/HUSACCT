package husacct.graphics.task.layout;

import husacct.graphics.presentation.Drawing;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.RelationFigure;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;
import java.util.Vector;

import org.jhotdraw.draw.ConnectionFigure;
import org.jhotdraw.draw.Figure;

public class SmartLayoutStrategy implements LayoutStrategy {
	private static final double VERT_ITEM_SPACING = 40.0;
	private static final double HORZ_ITEM_SPACING = 35.0;
	
	private Drawing drawing;
	private NodeList nodes = new NodeList();

	public SmartLayoutStrategy(Drawing theDrawing) {
		drawing = theDrawing;
	}

	// Perhaps an idea to have a list "unprocessedFigures", making it easier /
	// simpler / quicker to see if a figure has been processed yet or not.
	@Override
	public void doLayout(int screenWidth, int screenHeight) {

		indexFigures(findAllConnectors());
		updatePositions();
	}
	
	private void indexFigures(List<ConnectionFigure> connectors) {
		for (ConnectionFigure cf : connectors) {
			Node startNode = getNode(cf.getStartFigure());
			Node endNode = getNode(cf.getEndFigure());

			startNode.connectTo(endNode);
			if (!startNode.isCyclicChain(endNode))
				endNode.setLevel(startNode.getLevel() + 1);
		}
	}	

	private List<ConnectionFigure> findAllConnectors() {
		List<Figure> figures = drawing.getChildren();
		ArrayList<ConnectionFigure> list = new ArrayList<ConnectionFigure>();

		for (Figure f : figures) {
			if (isConnector(f)) {
				list.add((ConnectionFigure) f);
			}
		}

		return Collections.unmodifiableList(list);
	}
	
	private void updatePositions() {
		List<Node> rootNodes = getRootNodes();
		//List<Node> unconnectedNodes = getUnconnectedNodes();
		
		Point2D.Double startPoint = new Point2D.Double(4 * HORZ_ITEM_SPACING, VERT_ITEM_SPACING);
		
		for (Node n : rootNodes) {
			double width = positionFigure(n, startPoint);
			//startPoint = updateStartPoint(startPoint, width);
		}
	}
	
	// TODO: Patrick: Make sure that recursive calls to positionFigure() take into account
	// that when 2(or more) child nodes are positioned the columnWidth should be
	// as wide as nodeCount * nodeWidth(n) + (nodeCount * HORZ_ITEM_SPACING)
	private double positionFigure(Node node, Point2D.Double startPoint) {
		Figure figure = node.getFigure();
		double columnWidth = 0;

		startPoint = updatePosition(figure, startPoint);
		columnWidth = figure.getBounds().width; // Trainwreck, no work around for this
		
		List<Node> connections = node.getConnections();
		//Point2D.Double 
		for (Node childNode : connections) {
			columnWidth = Math.max(positionFigure(childNode, startPoint), columnWidth);
		}
			
		return columnWidth;
	}
	
	private Point2D.Double updatePosition(Figure figure, Point2D.Double point) {
		Rectangle2D.Double bounds = figure.getBounds();
		Point2D.Double lead = new Point2D.Double(point.x, point.y);
		Point2D.Double anchor = new Point2D.Double(point.x + bounds.width, point.y + bounds.height);
		
		figure.willChange();
		figure.setBounds(anchor, lead);
		figure.changed();
		
		point.y += bounds.height + VERT_ITEM_SPACING;
		return point;
	}
	
	private List<Node> getRootNodes() {
		ArrayList<Node> nodes = new ArrayList<Node>();
		
		for (Node n : nodes) {
			if (n.getConnectionCount() > 0) {
				nodes.add(n);
			}
		}
		
		return Collections.unmodifiableList(nodes);
	}
	
//	private List<Node> getUnconnectedNodes() {
//		ArrayList<Node> nodes = new ArrayList<Node>();
//		
//		for (Node n : nodes) {
//			if (n.getConnectionCount() == 0) {
//				nodes.add(n);
//			}
//		}
//		
//		return Collections.unmodifiableList(nodes);
//	}
	
	private Node getNode(Figure figure) {
		if (nodes.contains(figure)) {
			return nodes.getByFigure(figure);
		} else {
			Node node = new Node(figure, 0);
			nodes.add(node);
			
			return node;
		}
	}	

	// TODO: Patrick: I'm not quite sure if this code should be here. We really
	// need to discuss this kind of code. It's ugly and unneccessary I think.
	// Perhaps this code should be re-located into the RelationFigure
	private boolean isConnector(Figure figure) {
		if (figure instanceof BaseFigure) {
			if (figure instanceof RelationFigure) {
				return true;
			}
		} else if (figure instanceof ConnectionFigure) {
			return true;
		}

		return false;
	}
}
