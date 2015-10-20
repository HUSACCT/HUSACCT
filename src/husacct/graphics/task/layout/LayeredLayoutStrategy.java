package husacct.graphics.task.layout;

import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.ModuleFigure;
import husacct.graphics.presentation.figures.RelationFigure;
import husacct.graphics.task.layout.layered.LayoutStrategy;
import husacct.graphics.task.layout.layered.Node;
import husacct.graphics.task.layout.layered.SortedNodeList;
import husacct.graphics.util.ListUtils;

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
	private static final double	VERT_ITEM_SPACING	= 40.0;
	private static final double	HORZ_ITEM_SPACING	= 35.0;
	
	private static final int	INTERFACE_LEVEL		= 0;
	private static final int	ROOT_LEVEL			= 1;
	
	private static boolean inContainer(Figure a) {
		return ((BaseFigure) a).isInContainer();
	}
	
	private static boolean isConnector(Figure figure) {
		return figure instanceof ConnectionFigure
				|| figure instanceof BaseFigure
				&& figure instanceof RelationFigure;
	}
	
	private AbstractCompositeFigure				drawing;
	private SortedNodeList						nodes			= new SortedNodeList();
	
	List<Figure>								connectors		= null;
	
	private static final F1<Node, Boolean>		rootLambda		= new F1<Node, Boolean>(
																		new Node(
																				null)) {
																	{
																		ret(a.getLevel() == ROOT_LEVEL
																				|| a.getLevel() == INTERFACE_LEVEL);
																	}
																};
	
	private static final F1<Figure, Boolean>	connectorLambda	= new F1<Figure, Boolean>(
																		null) {
																	{
																		ret(isConnector(a));
																	}
																};
	
	private static boolean isInterface(Node node) {
		boolean returnValue = false;
		if (node.getFigure()instanceof ModuleFigure) {
			ModuleFigure mf = (ModuleFigure) node.getFigure();
			if (mf.getType().toLowerCase().equals("interface")) {
				return true;
			}
		}
		return returnValue;
	}
	
	public LayeredLayoutStrategy(AbstractCompositeFigure theDrawing) {
		drawing = theDrawing;
	}
	
	private void applyLayout() {
		List<Node> rootNodes = ListUtils.select(nodes, rootLambda);
		
		Point2D.Double startPoint = new Point2D.Double(HORZ_ITEM_SPACING,
				VERT_ITEM_SPACING);
		
		for (Node n : rootNodes) {
			double width = positionFigure(n, startPoint);
			n.setPositionUpdated(true);
			
			startPoint.y = VERT_ITEM_SPACING;
			startPoint.x += HORZ_ITEM_SPACING + width;
		}
	}
	
	private void calculateLayout() {
		for (Figure f : connectors) {
			ConnectionFigure cf = (ConnectionFigure) f;
			
			Node startNode = getNode(cf.getStartFigure());
			Node endNode = getNode(cf.getEndFigure());
			
			if (!startNode.equals(endNode)) {
				startNode.connectTo(endNode);
				endNode.addParent(startNode);
				
				updateNodes(startNode, endNode);
			} else if (startNode.getLevel() == Node.UNINITIALIZED) startNode
					.setLevel(ROOT_LEVEL);
		}
		
		final List<Node> compareList = nodes.readOnlyCopy();
		S1<Figure> addUnconnectedFigures = new S1<Figure>(null, compareList) {
			{
				if (a != null && !isConnector(a) && !compareList.contains(a)
						&& !inContainer(a)) getNode(a);
			}
		};
		ListUtils.apply(drawing.getChildren(), addUnconnectedFigures);
	}
	
	private Point2D.Double calculateStartPosition(Node node, Double startPoint) {
		Point2D.Double retVal = (Point2D.Double) startPoint.clone();
		int lowestLevel = Integer.MAX_VALUE;
		
		List<Node> connections = node.getConnections();
		for (Node child : connections)
			lowestLevel = Math.min(child.getLevel(), lowestLevel);
		
		if (lowestLevel != Integer.MAX_VALUE && lowestLevel > 1) retVal.y += (lowestLevel - 1)
				* (node.getHeight() + VERT_ITEM_SPACING);
		
		return retVal;
	}
	
	@Override
	public void doLayout() {
		initLayout();
		calculateLayout();
		applyLayout();
	}
	
	private int getLowestParentLevel(Node node) {
		int level = Node.UNINITIALIZED;
		
		for (Node parent : node.getParents())
			if (parent.getLevel() != INTERFACE_LEVEL) level = Math.max(level,
					parent.getLevel());
		
		return level;
	}
	
	private Node getNode(Figure figure) {
		if (nodes.contains(figure)) return nodes.getByFigure(figure);
		else {
			Node node = new Node(figure);
			nodes.add(node);
			
			return node;
		}
	}
	
	private void initializeNodes(Node startNode, Node endNode) {
		int startLevel = startNode.getLevel();
		int endLevel = endNode.getLevel();
		
		if (startLevel == Node.UNINITIALIZED && endLevel == Node.UNINITIALIZED) {
			// Both levels are uninitialized, so make the nodes ROOT_LEVEL and
			// ROOT_LEVEL + 1
			if (isInterface(startNode)) startLevel = INTERFACE_LEVEL;
			else
				startLevel = ROOT_LEVEL;
			
			endLevel = startLevel + 1;
		} else if (startLevel != Node.UNINITIALIZED
				&& endLevel == Node.UNINITIALIZED) {
			// endLevel = startLevel + 1
			if (isInterface(endNode)) endLevel = INTERFACE_LEVEL;
			else
				endLevel = startLevel + 1;
		} else if (startLevel == Node.UNINITIALIZED
				&& endLevel != Node.UNINITIALIZED) if (startNode
				.isParentOf(endNode)
				&& endNode.isParentOf(startNode)
				|| endNode.isParentOf(startNode)) startLevel = endLevel + 1;
		else if (endLevel > ROOT_LEVEL) startLevel = endLevel - 1;
		else
			startLevel = endLevel;
		
		startNode.setLevel(startLevel);
		endNode.setLevel(endLevel);
	}
	
	private void initLayout() {
		nodes.clear();
		connectors = ListUtils.select(drawing.getChildren(), connectorLambda);
	}
	
	// TODO: Patrick: Make sure that recursive calls to positionFigure() take
	// into account
	// that when 2(or more) child nodes are positioned the columnWidth should be
	// as wide as nodeCount * nodeWidth(n) + (nodeCount * HORZ_ITEM_SPACING)
	private double positionFigure(Node node, Point2D.Double startPoint) {
		
		Point2D.Double position = (Point2D.Double) startPoint.clone();
		Figure figure = node.getFigure();
		double columnWidth = 0;
		
		if (node.getLevel() == 0) position = calculateStartPosition(node,
				startPoint);
		
		updatePosition(figure, position);
		node.setPositionUpdated(true);
		
		columnWidth = node.getWidth();
		position.y += node.getHeight() + VERT_ITEM_SPACING;
		List<Node> connections = node.getConnections();
		for (Node child : connections)
			if (!child.isPositionUpdated()
					&& child.getLevel() != node.getLevel()) {
				columnWidth = Math.max(positionFigure(child, position),
						columnWidth);
				position.y = startPoint.y;
				position.x += child.getWidth() + HORZ_ITEM_SPACING;
				
				child.setPositionUpdated(true);
			}
		
		return columnWidth;
	}
	
	private void rebalanceNodes(Node startNode, Node endNode) {
		int startLevel = startNode.getLevel();
		int endLevel = endNode.getLevel();
		int deltaLevel = Math.abs(endLevel - startLevel);
		
		if (startNode.isParentOf(endNode) && endNode.isParentOf(startNode)) {
			int startParent = getLowestParentLevel(startNode);
			int endParent = getLowestParentLevel(endNode);
			
			if (startParent == Node.UNINITIALIZED) startLevel = endParent + 1;
			else if (endParent == Node.UNINITIALIZED) endLevel = startParent + 1;
			else if (startParent < endParent) {
				startLevel = startParent + 1;
				endLevel = startParent + 2;
			} else {
				startLevel = endParent + 1;
				endLevel = endParent + 2;
			}
		}
		
		if (endLevel == startLevel) {
			updateNodesOnEqualLevel(startNode, endNode);
			return;
		} else if (endLevel == 0) endLevel = startLevel + 1;
		else if (startLevel == 0 && deltaLevel >= 2) startLevel = endLevel - 1;
		
		startNode.setLevel(startLevel);
		endNode.setLevel(endLevel);
	}
	
	private void updateNodes(Node startNode, Node endNode) {
		int startLevel = startNode.getLevel();
		int endLevel = endNode.getLevel();
		
		if (startLevel == Node.UNINITIALIZED || endLevel == Node.UNINITIALIZED) initializeNodes(
				startNode, endNode);
		else
			rebalanceNodes(startNode, endNode);
	}
	
	private void updateNodesOnEqualLevel(Node startNode, Node endNode) {
		if (startNode.isParentOf(endNode) && endNode.isParentOf(startNode)) {
			int start = getLowestParentLevel(startNode);
			int end = getLowestParentLevel(endNode);
			
			if (start > end) {
				startNode.setLevel(start + 1);
				endNode.setLevel(start + 2);
			} else {
				endNode.setLevel(end + 1);
				startNode.setLevel(end + 2);
			}
		} else
			endNode.setLevel(startNode.getLevel() + 1);
	}
	
	private void updatePosition(Figure figure, Point2D.Double point) {
		Rectangle2D.Double bounds = figure.getBounds();
		Point2D.Double anchor = new Point2D.Double(point.x, point.y);
		Point2D.Double lead = new Point2D.Double(point.x + bounds.width,
				point.y + bounds.height);
		
		figure.willChange();
		figure.setBounds(anchor, lead);
		figure.changed();
		
		// System.out.println(String.format("Moving %s to (%d, %d)",
		// ((BaseFigure) figure).getName(), (int) anchor.x,
		// (int) anchor.y));
	}
}
