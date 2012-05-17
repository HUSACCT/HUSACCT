package husacct.define.presentation.moduletree;

import husacct.define.task.components.AnalyzedModuleComponent;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class AnalyzedModuleCellRenderer extends DefaultTreeCellRenderer {
	
	private static final long serialVersionUID = 7222224490353685412L;
	
	public AnalyzedModuleCellRenderer() {
		
	}
	
	/**
	 * only TreeCellRenderer method
	 * Compute the String to display, and pass it to the wrapped renderer
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		if(value instanceof AnalyzedModuleComponent) {
			AnalyzedModuleComponent component = (AnalyzedModuleComponent) value;
			super.getTreeCellRendererComponent(tree, component.getName(), selected, expanded, leaf, row, hasFocus);
			this.determineIcon(component);
		}
		return this;
	}
	
	private void determineIcon(AnalyzedModuleComponent component) {
		ImageIcon icon = new ImageIcon();
		if(component.getType().equals("PACKAGE")) {
			icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/common/resources/icon-package.png"));
		} else if(component.getType().equals("CLASS")) {
			icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/common/resources/icon-class.png"));
		} else if(component.getType().equals("METHOD")) {
			icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/common/resources/icon-method.png"));
		}
		this.setIcon(icon);
	}
}