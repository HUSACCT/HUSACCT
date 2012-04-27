package husacct.define.presentation.moduletree;

import husacct.define.task.components.AbstractDefineComponent;

import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

public class ModuleTree extends JTree {

	private static final long serialVersionUID = 3282591641481691737L;
	
	public ModuleTree(AbstractDefineComponent rootComponent) {
		super(new ModuleTreeModel(rootComponent));
		ModuleCellRenderer moduleCellRenderer = new ModuleCellRenderer();
	    this.setCellRenderer(moduleCellRenderer);
	    this.setDefaultSettings();
	}
	
	public void setDefaultSettings() {
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}
}
