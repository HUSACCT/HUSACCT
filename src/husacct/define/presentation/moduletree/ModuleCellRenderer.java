package husacct.define.presentation.moduletree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

public class ModuleCellRenderer implements TreeCellRenderer {
	TreeCellRenderer renderer;
	
	public ModuleCellRenderer(TreeCellRenderer renderer) {
	  this.renderer = renderer;
	}
	
	/**
	 * only TreeCellRenderer method
	 * Compute the String to display, and pass it to the wrapped renderer
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		String newvalue = value.getClass().getName(); // Component type
		String name = ((Component) value).getName(); // Component name
		if (name != null) {
		    newvalue += " (" + name + ")"; // unless null
		}
		 
		// Use the wrapped renderer object to do the real work
		return renderer.getTreeCellRendererComponent(tree, newvalue, selected, expanded, leaf, row, hasFocus);
	}
}