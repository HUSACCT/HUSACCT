package husacct.define.presentation.moduletree;

import husacct.define.task.components.AbstractDefineComponent;

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
		if(value instanceof AbstractDefineComponent) {
			AbstractDefineComponent component = (AbstractDefineComponent) value;
			return renderer.getTreeCellRendererComponent(tree,  component.getName(), selected, expanded, leaf, row, hasFocus);
		} else {
			return renderer.getTreeCellRendererComponent(tree,  value.getClass().getName(), selected, expanded, leaf, row, hasFocus);
		}
	}
}