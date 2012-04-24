package husacct.define.presentation.moduletree;

import java.awt.Component;
import java.awt.Container;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class ModuleTreeModel implements TreeModel {
	Component root;
	
	public ModuleTreeModel(Component root) {
		this.root = root;
	}
	
	public Object getRoot() {
		return root;
	}
	
	/**
	 * Is this node a leaf? (Leaf nodes are displayed differently by JTree)
	 * Any node that isn't a container is a leaf, since they cannot have
	 * children. We also define containers with no children as leaves.
	 */
	public boolean isLeaf(Object node) {
		if (node instanceof Container) {
			Container nodeContainer = (Container) node;
			return nodeContainer.getComponentCount() == 0;
		} else {
			return true;
		}
	}
	
	public int getChildCount(Object node) {
		if (node instanceof Container) {
			Container nodeContainer = (Container) node;
			return nodeContainer.getComponentCount();
		}
		return 0;
	}
	
	public Object getChild(Object parentNode, int index) {
		if (parentNode instanceof Container) {
			Container nodeContainer = (Container) parentNode;
			return nodeContainer.getComponent(index);
		}
		return null;
	}
	
	public int getIndexOfChild(Object parentNode, Object child) {
		if(parentNode instanceof Container) {
			Container nodeContainer = (Container) parentNode;
			Component[] children = nodeContainer.getComponents();
			return this.checkChildrenForIndex(children, child);
		}
		return -1;
	}
	
	private int checkChildrenForIndex(Component[] children, Object child) {
		if(children != null) {
			for (int i = 0; i < children.length; i++) {
			    if (children[i] == child) {
			    	return i;
			    }
			}
		}
		return -1;
	}
	
	/**
	 * #TODO:: TreeModel never fires any events (since it is not editable)
	 */
	@Override
	public void valueForPathChanged(TreePath path, Object newvalue) {
		
	}
	
	
	/**
	 * #TODO:: TreeModel never fires any events (since it is not editable)
	 */
	@Override
	public void addTreeModelListener(TreeModelListener l) {
		
	}
	
	/**
	 * #TODO:: TreeModel never fires any events (since it is not editable)
	 */
	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		
	}
}