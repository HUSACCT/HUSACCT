package husacct.graphics.task.layout;

import husacct.graphics.presentation.figures.BaseFigure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.jhotdraw.draw.Figure;

public class Node {
	public static final int UNINITIALIZED = Integer.MIN_VALUE;
	
	private String name;
	private Figure figure;
	private int level = UNINITIALIZED;
	private boolean positionUpdated = false;
	private ArrayList<Node> parents = new ArrayList<Node>();
	private ArrayList<Node> connectedTo = new ArrayList<Node>();

	public Node(Figure f, int l) {
		figure = f;
		level = l;
		
		if (f != null) 
			name = ((BaseFigure)f).getName();
	}

	public Node(Figure f) {
		this(f, UNINITIALIZED);
	}

	public void addParent(Node n) {
		parents.add(n);
	}
	
	public void removeParent(Node n) {
		parents.remove(n);
	}
	
	public List<Node> getParents() {
		return Collections.unmodifiableList(parents);
	}	
	
	public void removeConnectionTo(Node n) {
		connectedTo.remove(n);
	}

	public void connectTo(Node n) {
		if (equals(n))
			throw new IllegalArgumentException("Cannot connect a node to itself!");

		if (!connectedTo.contains(n))
			connectedTo.add(n);
	}

	public boolean isConnectedTo(Node n) {
		return connectedTo.contains(n);
	}

	public List<Node> getConnections() {
		return Collections.unmodifiableList(connectedTo);
	}

	public int getConnectionCount() {
		return connectedTo.size();
	}

	public void setLevel(int newLevel) {
		level = newLevel;
	}

	public int getLevel() {
		return level;
	}

	public Figure getFigure() {
		return figure;
	}

	public boolean isPositionUpdated() {
		return positionUpdated;
	}

	public void setPositionUpdated(boolean newValue) {
		positionUpdated = newValue;
	}

	// TODO: Update to make use of the parent nodes
	public boolean isCyclicChain(Node n) {
		Vector<Node> open = new Vector<Node>();
		Vector<Node> closed = new Vector<Node>();
		open.addAll(connectedTo);

		while (open.size() > 0) {
			Node nextNode = open.get(0);
			open.removeElementAt(0);
			closed.add(nextNode);

			for (Node node : nextNode.connectedTo) {
				if (!closed.contains(node))
					open.add(node);
			}
			
			if (equals(nextNode))
				return true;
		}

		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Node) {
			Node rhs = (Node) o;
			return this.figure.equals(rhs.figure);
		} else if (o instanceof Figure) {
			Figure f = (Figure) o;
			return figure.equals(f);
		}

		return false;
	}

	public int getWidth() {
		return (int) figure.getBounds().width;
	}

	public int getHeight() {
		return (int) figure.getBounds().height;
	}
	
	public String getName() {
		return name;
	}

	public boolean isParentOf(Node endNode) {
		return connectedTo.contains(endNode) && endNode.getParents().contains(this);
	}
	
	public boolean isChildOf(Node n) {
		return parents.contains(n) && n.isParentOf(this);
	}
	
	
	
//	public void setStartOfChain(boolean newValue) {
//		chainStart = newValue;
//	}
//	
//	public boolean isStartOfChain() {
//		return chainStart;
//	}
}
