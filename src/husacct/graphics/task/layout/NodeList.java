package husacct.graphics.task.layout;

import java.util.ArrayList;
import java.util.Iterator;

import org.jhotdraw.draw.Figure;

public class NodeList implements Iterable<Node> {
	private ArrayList<Node> nodes;
	
	public NodeList() {
		nodes = new ArrayList<Node>();
	}
	
	public void add(Node n) {
		nodes.add(n);
	}
	
	public boolean contains(Node n) {
		return nodes.contains(n);
	}
	
	public boolean contains(Figure f) {
		for (Node node : nodes) {
			if (node.equals(f))
				return true;
		}
		
		return false;
	}
	
	public Node getByFigure(Figure f) {
		for (Node n : nodes) {
			if (n.equals(f))
				return n;
		}
		
		// TODO: Patrick: Should this throw an exception or return null?
		// Please check the Java API documentation concerning collections to see how 
		// the Java API solves this.
		return null;
	}

	@Override
	public Iterator<Node> iterator() {
		return nodes.iterator();
	}
	
}