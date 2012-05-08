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
        	ImageIcon icon;
        	if(moduleSelected.uniqueName.equals("")){
        		icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/analyse/presentation/resources/application.png"));
        	}else if(moduleSelected.type.equals("package")){
        		icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/analyse/presentation/resources/package.png"));
        	}else if(moduleSelected.type.equals("class")){
        		icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/analyse/presentation/resources/class.gif"));
        	}else if(moduleSelected.type.equals("interface")){
        		icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/analyse/presentation/resources/interface.png"));
        	}else{
        		icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/analyse/presentation/resources/module.png"));
        	}
        	label.setIcon(icon);
            
        	if(moduleSelected.name.equals("")) label.setText("Application");
        	else label.setText(moduleSelected.name);
            if (selected)label.setBackground(backgroundSelectionColor);
            else label.setBackground(backgroundNonSelectionColor);
    	}catch(ClassCastException e){
    	}
        
        return label;
    }
}