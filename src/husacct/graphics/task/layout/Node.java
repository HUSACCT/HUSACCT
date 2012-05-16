package husacct.graphics.task.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.jhotdraw.draw.Figure;

public class Node {
	private Figure figure;
	private int level = 0;
	private ArrayList<Node> connectedTo = new ArrayList<Node>();

	public Node(Figure f, int l) {
		figure = f;
		level = l;
	}

	public Node(Figure f) {
		this(f, 0);
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
	
	public boolean isCyclicChain(Node n) {
		Vector<Node> unprocessedNodes = new Vector<Node>();
		unprocessedNodes.addAll(connectedTo);
		
		while (unprocessedNodes.size() > 0) {
			Node nextNode = unprocessedNodes.get(0);
			unprocessedNodes.removeElementAt(0);
			
			unprocessedNodes.addAll(nextNode.connectedTo);
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
}
