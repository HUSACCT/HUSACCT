package husacct.analyse.presentation;

import husacct.common.dto.AnalysedModuleDTO;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

class SoftwareTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 1L;
	private JLabel label;

	SoftwareTreeCellRenderer() {
        label = new JLabel();
        label.setOpaque(true);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree,  Object value,  boolean selected, boolean expanded,
    												boolean leaf, int row, boolean hasFocus) {
    	
    	try{
    		DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        	AnalysedModuleDTO moduleSelected = (AnalysedModuleDTO)node.getUserObject();
        	
        	if(moduleSelected.type.equals("root")){
        		label.setIcon(new ImageIcon("img/folder.png"));
        	}
        	else if(moduleSelected.type.equals("package")){
        		label.setIcon(new ImageIcon("img/package.png"));
        	}else{
        		label.setIcon(new ImageIcon("img/class.gif"));
        	}
            
            label.setText(moduleSelected.name);
            if (selected)label.setBackground(backgroundSelectionColor);
            else label.setBackground(backgroundNonSelectionColor);
    	}catch(ClassCastException e){
    	}
        
        return label;
    }
}