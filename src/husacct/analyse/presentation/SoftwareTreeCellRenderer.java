package husacct.analyse.presentation;

import husacct.analyse.abstraction.language.AnalyseTranslater;
import husacct.common.dto.AnalysedModuleDTO;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

class SoftwareTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 1L;
	private JLabel label;
	
	SoftwareTreeCellRenderer() {
        label = new JLabel();
        label.setOpaque(true);
        
        setBackground(UIManager.getColor("Panel.background"));
		setBackgroundNonSelectionColor(UIManager.getColor("Panel.background"));
		setBackgroundSelectionColor(UIManager.getColor("Panel.background"));
		setTextNonSelectionColor(Color.black);
		setTextSelectionColor(Color.black);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree,  Object value,  boolean selected, boolean expanded,
    												boolean leaf, int row, boolean hasFocus) {
    	
    	try{
    		DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        	AnalysedModuleDTO moduleSelected = (AnalysedModuleDTO)node.getUserObject();
        	ImageIcon icon;
        	if(moduleSelected.uniqueName.equals("")){
        		icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/common/resources/icon-source.png"));
        	}else if(moduleSelected.type.equals("package")){
        		icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/common/resources/icon-package.png"));
        	}else if(moduleSelected.type.equals("class")){
        		icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/common/resources/icon-class.png"));
        	}else if(moduleSelected.type.equals("interface")){
        		icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/common/resources/icon-interface.png"));
        	}else if(moduleSelected.type.equals("enumeration")){
        		icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/common/resources/icon-enumeration.png"));
        	}else{
        		icon = new ImageIcon(getClass().getClassLoader().getResource("husacct/common/resources/icon-module.png"));
        	}
        	label.setIcon(icon);
            
        	if(moduleSelected.name.equals("")) label.setText(AnalyseTranslater.getValue("Application"));
        	else label.setText(moduleSelected.name);
            if (selected)label.setBackground(backgroundSelectionColor);
            else label.setBackground(backgroundNonSelectionColor);
    	}catch(ClassCastException e){
    	}
        
        return label;
    }
}