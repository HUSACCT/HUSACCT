package husacct.define.presentation.moduletree;

import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.LayerComponent;
import husacct.define.task.components.SoftwareArchitectureComponent;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class ModuleCellRenderer extends DefaultTreeCellRenderer {
	
	private static final long serialVersionUID = 7222224490353685412L;
	
	public ModuleCellRenderer() {
		
	}
	
	/**
	 * only TreeCellRenderer method
	 * Compute the String to display, and pass it to the wrapped renderer
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		if(value instanceof AbstractDefineComponent) {
			AbstractDefineComponent component = (AbstractDefineComponent) value;
			super.getTreeCellRendererComponent(tree,  component.getName(), selected, expanded, leaf, row, hasFocus);
			
			this.determineIcon(value);
		}
		return this;
	}
	
	private void determineIcon(Object value) {
		ImageIcon icon = new ImageIcon();
		if(value instanceof SoftwareArchitectureComponent) {
			icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/define/presentation/resources/icon-software_architecture.png"));
		} else if(value instanceof LayerComponent) {
			icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/define/presentation/resources/icon-layer.png"));
		}
		this.setIcon(icon);
	}
}