package husacct.graphics.task.modulelayout.layered;

import husacct.graphics.task.modulelayout.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class SortedNodeList extends NodeList {
	
	private boolean compareIfListsOverlap(List<Node> lhs, List<Node> rhs) {
		for (Node n : lhs)
			if (rhs.contains(n)) return true;
		
		return false;
	}
	
	private void mergeLists(List<Node> dest, List<Node> src) {
		if (dest == src) return;
		
		while (!src.isEmpty()) {
			Node node = ListUtils.pop(src);
			
			if (!dest.contains(node)) dest.add(node);
		}
	}
	
	public void sort() {
		ArrayList<ArrayList<Node>> trees = new ArrayList<ArrayList<Node>>();
		
		while (!nodes.isEmpty()) {
			ArrayList<Node> openNodes = new ArrayList<Node>();
			ArrayList<Node> tree = new ArrayList<Node>();
			
			openNodes.add(nodes.get(0));
			while (!openNodes.isEmpty()) {
				Node node = ListUtils.pop(openNodes);
				
				nodes.remove(node);
				for (Node n : node.getConnections())
					if (nodes.contains(n)) openNodes.add(n);
				
				tree.add(node);
			}
			
			trees.add(tree);
		}
		
		if (!trees.isEmpty()) {
			ArrayList<ArrayList<Node>> finalOrdering = new ArrayList<ArrayList<Node>>();
			finalOrdering.add(ListUtils.pop(trees));
			
			while (!trees.isEmpty()) {
				ArrayList<Node> tree = ListUtils.pop(trees);
				
				for (int i = 0; i < finalOrdering.size(); i++) {
					ArrayList<Node> finalOrderedTree = finalOrdering.get(i);
					
					if (compareIfListsOverlap(tree, finalOrderedTree)) mergeLists(
							finalOrderedTree, tree);
					else
						finalOrdering.add(tree);
				}
			}
			
			nodes.clear();
			while (!finalOrdering.isEmpty()) {
				ArrayList<Node> tree = ListUtils.pop(finalOrdering);
				nodes.addAll(tree);
			}
		}
	}
}
