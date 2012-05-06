package husacct.define.presentation.moduletree;

import husacct.define.task.components.AnalyzedModuleComponent;

import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;

public class AnalyzedModuleTree extends JTree {

	private static final long serialVersionUID = 3282591641481691737L;
	
	public AnalyzedModuleTree(AnalyzedModuleComponent rootComponent) {
		super(new AnalyzedModuleTreeModel(rootComponent));
		AnalyzedModuleCellRenderer moduleCellRenderer = new AnalyzedModuleCellRenderer();
	    this.setCellRenderer(moduleCellRenderer);
	    this.setDefaultSettings();
	}
	
	public void setDefaultSettings() {
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}
}
