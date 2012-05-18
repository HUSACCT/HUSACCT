package husacct.define.presentation.moduletree;

import husacct.define.task.components.AbstractCombinedComponent;

import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;

public class CombinedModuleTree extends JTree {

	private static final long serialVersionUID = 1859193273511893860L;
	
	private long selectedModuleId;

	public CombinedModuleTree(AbstractCombinedComponent rootComponent, long selectedModuleId) {
		super(new CombinedModuleTreeModel(rootComponent));
		this.setSelectedModuleId(selectedModuleId);
		CombinedModuleCellRenderer moduleCellRenderer = new CombinedModuleCellRenderer();
	    this.setCellRenderer(moduleCellRenderer);
	    this.setDefaultSettings();
	}
	
	public void setDefaultSettings() {
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}

	public long getSelectedModuleId() {
		return selectedModuleId;
	}

	public void setSelectedModuleId(long selectedModuleId) {
		this.selectedModuleId = selectedModuleId;
	}
}
