package husacct.define.presentation.moduletree;

import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.AnalyzedModuleComponent;
import husacct.define.task.components.ComponentComponent;
import husacct.define.task.components.ExternalLibraryComponent;
import husacct.define.task.components.LayerComponent;
import husacct.define.task.components.SoftwareArchitectureComponent;
import husacct.define.task.components.SubSystemComponent;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class CombinedModuleCellRenderer extends DefaultTreeCellRenderer {
	
	private static final long serialVersionUID = 6225304804416378425L;

	/**
	 * only TreeCellRenderer method
	 * Compute the String to display, and pass it to the wrapped renderer
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		if(value instanceof AbstractCombinedComponent) {
			AbstractCombinedComponent component = (AbstractCombinedComponent) value;
			super.getTreeCellRendererComponent(tree, component.getName(), selected, expanded, leaf, row, hasFocus);
			this.determineIcon(component);
			this.checkEnabled(component, tree);
		}
		return this;
	}
	
	private void determineIcon(AbstractCombinedComponent component) {
		if(component instanceof AnalyzedModuleComponent) {
			AnalyzedModuleComponent analyzedModuleComponent = (AnalyzedModuleComponent) component;
			this.setIcon(this.determineAnalyzedModuleComponentIcon(analyzedModuleComponent));
		} else if(component instanceof AbstractDefineComponent) {
			AbstractDefineComponent abstractDefineComponent = (AbstractDefineComponent) component;
			this.setIcon(this.determineAbstractDefineComponentIcon(abstractDefineComponent));
		}
	}
	
	private ImageIcon determineAnalyzedModuleComponentIcon(AnalyzedModuleComponent component) {
		ImageIcon icon = new ImageIcon();
		if(component.getType().equals("PACKAGE")) {
			icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/common/resources/icon-package.png"));
		} else if(component.getType().equals("CLASS")) {
			icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/common/resources/icon-class.png"));
		} else if(component.getType().equals("INTERFACE")) {
			icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/common/resources/icon-interface.png"));
		}
		return icon;
	}
	
	private ImageIcon determineAbstractDefineComponentIcon(AbstractDefineComponent component) {
		ImageIcon icon = new ImageIcon();
		if(component instanceof ComponentComponent) {
			icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/common/resources/icon-component.png"));
		} else if(component instanceof ExternalLibraryComponent) {
			icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/common/resources/icon-library.png"));
		} else if(component instanceof LayerComponent) {
			icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/common/resources/icon-layer.png"));
		} else if(component instanceof SubSystemComponent) {
			icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/common/resources/icon-module.png"));
		} else if(component instanceof SoftwareArchitectureComponent) {
			icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/common/resources/icon-software_architecture.png"));
		}
		return icon;
	}
	
	private void checkEnabled(AbstractCombinedComponent component, JTree tree) {
		if(component instanceof AbstractDefineComponent && tree instanceof CombinedModuleTree) {
			AbstractDefineComponent abstractDefineComponent = (AbstractDefineComponent) component;
			CombinedModuleTree moduleTree = (CombinedModuleTree) tree;
			if(abstractDefineComponent.getModuleId() == moduleTree.getSelectedModuleId() || abstractDefineComponent.getModuleId() == -1L) {
				this.setEnabled(false);
				return;
			}
		}
		this.setEnabled(true);
	}
}
