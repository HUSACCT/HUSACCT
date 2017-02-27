package husacct.define.presentation.treetable;

import husacct.common.Resource;
import husacct.define.domain.warningmessages.WarningMessageContainer;


import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class WarningTreeTableCellrenderer extends DefaultTreeCellRenderer {
	
	
	
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		if(value instanceof WarningMessageContainer) {
			WarningMessageContainer m =(WarningMessageContainer)value;
			super.getTreeCellRendererComponent(tree, m.getvalue().getDescription(), selected, expanded, leaf, row, hasFocus);
		this.setIcon(this.determineIcon(m));
			
		}
		return this;

	
	
	}

	private ImageIcon determineIcon(WarningMessageContainer warning) {
		Icon i = new ImageIcon(Resource.get(Resource.ICON_VALIDATE));
		return (ImageIcon) i;
	}
}