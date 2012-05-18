package husacct.define.presentation.moduletree;

import husacct.define.task.components.AbstractCombinedComponent;

import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;

public class CombinedModuleTree extends JTree {

	private static final long serialVersionUID = 1859193273511893860L;

	public CombinedModuleTree(AbstractCombinedComponent rootComponent) {
		super(new CombinedModuleTreeModel(rootComponent));
		CombinedModuleCellRenderer moduleCellRenderer = new CombinedModuleCellRenderer();
	    this.setCellRenderer(moduleCellRenderer);
	    this.setDefaultSettings();
	}
	
	public void setDefaultSettings() {
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}
}
