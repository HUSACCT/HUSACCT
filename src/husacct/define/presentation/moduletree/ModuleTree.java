package husacct.define.presentation.moduletree;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

public class ModuleTree extends JTree {

	private static final long serialVersionUID = 3282591641481691737L;
	
	public ModuleTree(Component c) {
		super(new ModuleTreeModel(c));
		TreeCellRenderer currentCellRenderer = this.getCellRenderer();
		ModuleCellRenderer newCellRenderer = new ModuleCellRenderer(currentCellRenderer);
	    this.setCellRenderer(newCellRenderer);
	    this.setDefaultSettings();
	}
	
	public void setDefaultSettings() {
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}
}
