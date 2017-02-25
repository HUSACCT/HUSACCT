package husacct.graphics.task.modulelayout.layered;

import husacct.graphics.domain.figures.BaseFigure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.jhotdraw.draw.Figure;

public class Node {
	public static final int	UNINITIALIZED	= Integer.MIN_VALUE;
	
	private String			name;
	private Figure			figure;
	private int				level			= UNINITIALIZED;
	private boolean			positionUpdated	= false;
	private ArrayList<Node>	parents			= new ArrayList<>();
	private ArrayList<Node>	connectedTo		= new ArrayList<>();
	
	public Node(Figure f) {
		this(f, UNINITIALIZED);
	}
	
	public Node(Figure f, int l) {
		figure = f;
		level = l;
		
		if (f != null) name = ((BaseFigure) f).getName();
	}
	
	public void addParent(Node n) {
		parents.add(n);
	}
	
	public void connectTo(Node n) {
		if (equals(n)) throw new IllegalArgumentException(
				"Cannot connect a node to itself!");
		
		if (!connectedTo.contains(n)) connectedTo.add(n);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Node) {
			Node rhs = (Node) o;
			return figure.equals(rhs.figure);
		} else if (o instanceof Figure) {
			Figure f = (Figure) o;
			return figure.equals(f);
		}
		
		return false;
	}
	
	public int getConnectionCount() {
		return connectedTo.size();
	}
	
	public List<Node> getConnections() {
		return Collections.unmodifiableList(connectedTo);
	}
	
	public Figure getFigure() {
		return figure;
	}
	
	public int getHeight() {
		return (int) figure.getBounds().height;
	}
	
	public int getLevel() {
		return level;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Node> getParents() {
		return Collections.unmodifiableList(parents);
	}
	
	public int getWidth() {
		return (int) figure.getBounds().width;
	}
	
	public boolean isChildOf(Node n) {
		return parents.contains(n) && n.isParentOf(this);
	}
	
	public boolean isConnectedTo(Node n) {
		return connectedTo.contains(n);
	}
	
	public boolean isCyclicChain(Node n) {
		Vector<Node> open = new Vector<>();
		Vector<Node> closed = new Vector<>();
		open.addAll(connectedTo);
		
		while (open.size() > 0) {
			Node nextNode = open.get(0);
			open.removeElementAt(0);
			closed.add(nextNode);
			
			for (Node node : nextNode.connectedTo)
				if (!closed.contains(node)) open.add(node);
			
			if (equals(nextNode)) return true;
		}
		
		return false;
	}
	
	public boolean isParentOf(Node endNode) {
		return connectedTo.contains(endNode)
				&& endNode.getParents().contains(this);
	}
	
	public boolean isPositionUpdated() {
		return positionUpdated;
	}
	
	public void removeConnectionTo(Node n) {
		connectedTo.remove(n);
	}
	
	public void removeParent(Node n) {
		parents.remove(n);
	}
	
	public void setLevel(int newLevel) {
		level = newLevel;
	}
	
	public void setPositionUpdated(boolean newValue) {
		positionUpdated = newValue;
	}
	
	// public void setStartOfChain(boolean newValue) {
	// chainStart = newValue;
	// }
	//
	// public boolean isStartOfChain() {
	// return chainStart;
	// }
}
