package husacct.graphics.task.layout;

import husacct.common.ListUtils;
import husacct.graphics.presentation.Drawing;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.ModuleFigure;
import husacct.graphics.presentation.figures.RelationFigure;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.jhotdraw.draw.ConnectionFigure;
import org.jhotdraw.draw.Figure;
import org.lambda.functions.implementations.F1;
import org.lambda.functions.implementations.S1;

public class LayeredLayoutStrategy implements LayoutStrategy {
	private static final double VERT_ITEM_SPACING = 40.0;
	private static final double HORZ_ITEM_SPACING = 35.0;
	
	private Drawing drawing;
	private NodeList nodes = new NodeList();
	List<Figure> connectors = null;

	public LayeredLayoutStrategy(Drawing theDrawing) {
		drawing = theDrawing;
	}

	// Perhaps an idea to have a list "unprocessedFigures", making it easier /
	// simpler / quicker to see if a figure has been processed yet or not.
	@Override
	public void doLayout(int screenWidth, int screenHeight) {
		connectors = ListUtils.select(drawing.getChildren(), new F1<Figure, Boolean>(null){{ ret(isConnector(a)); }});
		
		indexFigures();	
		updateFigurePositions();
		
		S1<Figure> applyLambda = new S1<Figure>(new RelationFigure("dummy", false, 0)){{ ConnectionFigure cf = (ConnectionFigure)a; cf.updateConnection(); }};
		ListUtils.apply(connectors, applyLambda);
		
		drawing.willChange();
		ListUtils.apply(connectors, applyLambda);
		drawing.changed();
		
		nodes.clear();
	}
	
	private void indexFigures() {
		for (Figure f : connectors) {
			ConnectionFigure cf = (ConnectionFigure)f;
			
			Node startNode = getNode(cf.getStartFigure());
			Node endNode = getNode(cf.getEndFigure());
			
			System.out.println(String.format("%s => %s", nodeName(startNode), nodeName(endNode)));

			startNode.connectTo(endNode);
			if (!startNode.isCyclicChain(endNode)) {
				int startLevel = startNode.getLevel();
				int endLevel = endNode.getLevel();
				int deltaLevel = Math.abs(endLevel - startLevel);
				
				// TODO: Expand this one for more 'special' cases
				if (endLevel == startLevel)
					endNode.setLevel(startLevel + 1);
				else if (endLevel == 0)
					endNode.setLevel(startLevel + 1);
				else if (startLevel == 0 && deltaLevel >= 2)
					startNode.setLevel(endLevel - 1);
			}
		}
	}	

	private void updateFigurePositions() {
		List<Node> rootNodes = ListUtils.select(nodes, new F1<Node, Boolean>(new Node(null, 0)){{ ret(a.getLevel() == 0);}});
		
		Point2D.Double startPoint = new Point2D.Double(HORZ_ITEM_SPACING, VERT_ITEM_SPACING);
		
		for (Node n : rootNodes) {
			double width = positionFigure(n, startPoint);
			n.setPositionUpdated(true);
			
			startPoint.y = VERT_ITEM_SPACING;
			startPoint.x += HORZ_ITEM_SPACING + width;
		}
	}
	
	// TODO: Patrick: Make sure that recursive calls to positionFigure() take into account
	// that when 2(or more) child nodes are positioned the columnWidth should be
	// as wide as nodeCount * nodeWidth(n) + (nodeCount * HORZ_ITEM_SPACING)
	private double positionFigure(Node node, Point2D.Double startPoint) {
		Point2D.Double position = (Point2D.Double) startPoint.clone();
		Figure figure = node.getFigure();
		double columnWidth = 0;

		updatePosition(figure, position);
		node.setPositionUpdated(true);
		
		columnWidth = node.getWidth();
		position.y += node.getHeight() + VERT_ITEM_SPACING;
		
		List<Node> connections = node.getConnections();
		for (Node child : connections) {
			if (!child.isPositionUpdated() && child.getLevel() != node.getLevel()) {
				columnWidth = Math.max(positionFigure(child, position), columnWidth);
				position.y = startPoint.y;
				position.x += child.getWidth() + HORZ_ITEM_SPACING;
				
				child.setPositionUpdated(true);
			}
		}
			
		return columnWidth;
	}
	
	private void updatePosition(Figure figure, Point2D.Double point) {
		Rectangle2D.Double bounds = figure.getBounds();
		Point2D.Double anchor = new Point2D.Double(point.x, point.y);
		Point2D.Double lead = new Point2D.Double(point.x + bounds.width, point.y + bounds.height);
		
		figure.willChange();
		figure.setBounds(anchor, lead);
		figure.changed();
		
		System.out.println(String.format("Moving %s to (%d, %d)", ((BaseFigure)figure).getName(), (int)anchor.x, (int)anchor.y));;
	}
	
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
	
	// TODO: Debug function, please remove after finishing
	// construction of this code.
	private void printPositions() {
		for (Node n : nodes) {
			BaseFigure f = (BaseFigure)n.getFigure();
			System.out.println(String.format("%s: level %d", f.getName(), n.getLevel()));
		}
	}
	
	private String nodeName(Node n) {
		BaseFigure nf = (BaseFigure)n.getFigure(); 
		return nf.getName();
	}	
}
