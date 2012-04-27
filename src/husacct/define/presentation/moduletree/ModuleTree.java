package husacct.define.presentation.moduletree;

import java.awt.Component;

import javax.swing.JTree;

public class ModuleTree extends JTree {

	private static final long serialVersionUID = 3282591641481691737L;
	
	public ModuleTree(Component c) {
		super(new ModuleTreeModel(c));
	    setCellRenderer(new ModuleCellRenderer(getCellRenderer()));
	}
}
