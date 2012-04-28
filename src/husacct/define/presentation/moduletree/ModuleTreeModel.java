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
		if (!(node instanceof Container)) {
			return true;
		}
		Container c = (Container) node;
		return c.getComponentCount() == 0;
	}
	
	public int getChildCount(Object node) {
		if (node instanceof Container) {
			Container c = (Container) node;
			return c.getComponentCount();
		}
		return 0;
	}
	
	public Object getChild(Object parent, int index) {
		if (parent instanceof Container) {
			Container c = (Container) parent;
			return c.getComponent(index);
		}
		return null;
	}
	
	public int getIndexOfChild(Object parent, Object child) {
		if (!(parent instanceof Container)) {
			return -1;
		}
		
		Container c = (Container) parent;
		Component[] children = c.getComponents();
		  
		if (children == null) {
		    return -1;
		}
		
		for (int i = 0; i < children.length; i++) {
		    if (children[i] == child) {
		    	return i;
		    }
		}
		return -1;
	}
	
	/**
	 * #TODO ANTLR:: TreeModel never fires any events (since it is not editable)
	 */
	@Override
	public void valueForPathChanged(TreePath path, Object newvalue) {
		
	}
	
	
	/**
	 * #TODO ANTLR:: TreeModel never fires any events (since it is not editable)
	 */
	@Override
	public void addTreeModelListener(TreeModelListener l) {
		
	}
	
	/**
	 * #TODO ANTLR:: TreeModel never fires any events (since it is not editable)
	 */
	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		
	}
}