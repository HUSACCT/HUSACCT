package husacct.graphics.task.layout;

import husacct.common.ListUtils;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.RelationFigure;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.jhotdraw.draw.AbstractCompositeFigure;
import org.jhotdraw.draw.ConnectionFigure;
import org.jhotdraw.draw.Figure;
import org.lambda.functions.implementations.F1;
import org.lambda.functions.implementations.S1;

public class LayeredLayoutStrategy implements LayoutStrategy {
	private static final double VERT_ITEM_SPACING = 40.0;
	private static final double HORZ_ITEM_SPACING = 35.0;

	private AbstractCompositeFigure drawing;
	private SortedNodeList nodes = new SortedNodeList();
	List<Figure> connectors = null;

	private static final F1<Node, Boolean> rootLambda = new F1<Node, Boolean>(new Node(null, 0)) {
		{
			ret(a.getLevel() == 0);
		}
	};
	private static final F1<Figure, Boolean> connectorLambda = new F1<Figure, Boolean>(null) {
		{
			ret(isConnector(a));
		}
	};

	public LayeredLayoutStrategy(AbstractCompositeFigure theDrawing) {
		drawing = theDrawing;
	}

	@Override
	public void doLayout(int screenWidth, int screenHeight) {
		initLayout();
		calculateLayout();
		balanceLayout();
		applyLayout();
	}

	private void initLayout() {
		nodes.clear();
		connectors = ListUtils.select(drawing.getChildren(), connectorLambda);
	}

	private void calculateLayout() {
		for (Figure f : connectors) {
			ConnectionFigure cf = (ConnectionFigure) f;

			Node startNode = getNode(cf.getStartFigure());
			Node endNode = getNode(cf.getEndFigure());

			startNode.connectTo(endNode);
			boolean isCyclic = startNode.isCyclicChain(endNode);
			if (!isCyclic) {
				updateNodes(startNode, endNode);
			} else if (isCyclic) {
				if (startNode.getLevel() == Node.UNINITIALIZED || endNode.getLevel() == Node.UNINITIALIZED)
					updateNodes(startNode, endNode);
			}
		}

		final List<Node> compareList = nodes.readOnlyCopy();
		S1<Figure> addUnconnectedFigures = new S1<Figure>(null, compareList) {
			{
				if (a != null && !isConnector(a) && !compareList.contains(a))
					getNode(a);
			}
		};
		ListUtils.apply(drawing.getChildren(), addUnconnectedFigures);
	}
	
	private void balanceLayout() {
		
	}

	private void updateNodes(Node startNode, Node endNode) {
		int startLevel = startNode.getLevel();
		int endLevel = endNode.getLevel();

		if (startLevel == Node.UNINITIALIZED) {
			startLevel = 0;
			endLevel = 1;
		} else if (endLevel == Node.UNINITIALIZED) {
			endLevel = startLevel + 1;
		} else {
			int deltaLevel = Math.abs(endLevel - startLevel);

			// TODO: Expand this one for more 'special' cases
			if (endLevel == startLevel) {
				// NOTE: This keeps bumping down nodes in case they're on the same level. Do we want that?
				endLevel = startLevel + 1;
			} else if (endLevel == 0) {
				endLevel = startLevel + 1;
			} else if (startLevel == 0 && deltaLevel >= 2) {
				startLevel = endLevel - 1;
			}
		}

		startNode.setLevel(startLevel);
		endNode.setLevel(endLevel);
	}

	private void applyLayout() {
		List<Node> rootNodes = ListUtils.select(nodes, rootLambda);

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

		if (node.getLevel() == 0)
			position = calculateStartPosition(node, startPoint);

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

	private Point2D.Double calculateStartPosition(Node node, Double startPoint) {
		Point2D.Double retVal = (Point2D.Double) startPoint.clone();
		int lowestLevel = Integer.MAX_VALUE;

		List<Node> connections = node.getConnections();
		for (Node child : connections) {
			lowestLevel = Math.min(child.getLevel(), lowestLevel);
		}

		if (lowestLevel != Integer.MAX_VALUE && lowestLevel > 1) {
			retVal.y += (lowestLevel - 1) * (node.getHeight() + VERT_ITEM_SPACING);
		}

		return retVal;
	}

	private void updatePosition(Figure figure, Point2D.Double point) {
		Rectangle2D.Double bounds = figure.getBounds();
		Point2D.Double anchor = new Point2D.Double(point.x, point.y);
		Point2D.Double lead = new Point2D.Double(point.x + bounds.width, point.y + bounds.height);

		figure.willChange();
		figure.setBounds(anchor, lead);
		figure.changed();

		// System.out.println(String.format("Moving %s to (%d, %d)", ((BaseFigure) figure).getName(), (int) anchor.x,
		// (int) anchor.y));
	}

	private Node getNode(Figure figure) {
		if (nodes.contains(figure)) {
			return nodes.getByFigure(figure);
		} else {
			Node node = new Node(figure);
			nodes.add(node);

			return node;
		}
	}

	// TODO: Patrick: I'm not quite sure if this code should be here. We really
	// need to discuss this kind of code. It's ugly and unneccessary I think.
	// Perhaps this code should be re-located into the RelationFigure
	private static boolean isConnector(Figure figure) {
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
