package husacct.define.presentation.moduletree;

import husacct.define.task.components.AbstractDefineComponent;

import java.util.ArrayList;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class ModuleTreeModel implements TreeModel {
	AbstractDefineComponent root;
	
	public ModuleTreeModel(AbstractDefineComponent root) {
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
	public boolean isLeaf(Object nodeObject) {
		if (nodeObject instanceof AbstractDefineComponent) {
			AbstractDefineComponent node = (AbstractDefineComponent) nodeObject;
			ArrayList<AbstractDefineComponent> children = node.getChildren();
			return (children.size() == 0);
		} else {
			return true;
		}
	}
	
	public int getChildCount(Object nodeObject) {
		if (nodeObject instanceof AbstractDefineComponent) {
			AbstractDefineComponent node = (AbstractDefineComponent) nodeObject;
			ArrayList<AbstractDefineComponent> children = node.getChildren();
			return children.size();
		}
		return 0;
	}
	
	public Object getChild(Object nodeObject, int index) {
		if (nodeObject instanceof AbstractDefineComponent) {
			AbstractDefineComponent node = (AbstractDefineComponent) nodeObject;
			ArrayList<AbstractDefineComponent> children = node.getChildren();
			return children.get(index);
		}
		return null;
	}
	
	public int getIndexOfChild(Object nodeObject, Object child) {
		if(nodeObject instanceof AbstractDefineComponent) {
			AbstractDefineComponent node = (AbstractDefineComponent) nodeObject;
			ArrayList<AbstractDefineComponent> children = node.getChildren();
			return this.checkChildrenForIndex(children, child);
		}
		return -1;
	}
	
	private int checkChildrenForIndex(ArrayList<AbstractDefineComponent> children, Object child) {
		if(children != null) {
			for (int i = 0; i < children.size(); i++) {
			    if (children.get(i) == child) {
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