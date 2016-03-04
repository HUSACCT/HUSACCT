package husacct.analyse.task.analyser.java;

import java.util.LinkedList;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class JavaGeneratorToolkit {
    /**
     * Deletes all children from a certain Tree
     */
    public static void deleteTreeChild(Tree treeNode) {
        for (int child = 0; child < treeNode.getChildCount(); child++) {
            treeNode.deleteChild(child);
        }
    }

    /**
     * Gets a descendant from a ancestor with a certain type. This method walks
     * the tree breadth-first, to make sure it is the closest relative from the ancestor.
     */
    public static CommonTree getFirstDescendantWithType(CommonTree root, int type) {
    	LinkedList<CommonTree> queue = new LinkedList<>();
    	queue.add(root);
    	while(!queue.isEmpty()) {
    		CommonTree first = queue.removeFirst();
    		for (int i = 0; i < first.getChildCount(); i++) {
    			CommonTree child = (CommonTree)first.getChild(i);
    			if (isOfType(child, type))
    				return child;
    			queue.addLast(child);
    		}
    	}
    	return null;
    }
    
    /**
    * Checks whether or not a tree is of a certain type, including null-check
    */
   public static boolean isOfType(CommonTree tree, int type) {
       if (tree == null) {
           return false;
       }
       return tree.getType() == type;
   }
   
}
