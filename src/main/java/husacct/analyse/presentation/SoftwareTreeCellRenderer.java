package husacct.analyse.presentation;

import husacct.common.Resource;
import husacct.common.dto.SoftwareUnitDTO;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class SoftwareTreeCellRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = 1L;
    private JLabel label;
    private AnalyseUIController uiController;

    public SoftwareTreeCellRenderer(AnalyseUIController uiController) {
        label = new JLabel();
        label.setOpaque(true);
        this.uiController = uiController;

        setBackground(UIManager.getColor("Panel.background"));
        setBackgroundNonSelectionColor(UIManager.getColor("Panel.background"));
        setBackgroundSelectionColor(UIManager.getColor("Panel.background"));
        setTextNonSelectionColor(Color.black);
        setTextSelectionColor(Color.black);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
            boolean leaf, int row, boolean hasFocus) {
        try {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            SoftwareUnitDTO moduleSelected = (SoftwareUnitDTO) node.getUserObject();
            ImageIcon icon;
            if(moduleSelected.uniqueName.equals("")){
            	icon = new ImageIcon(Resource.get(Resource.ICON_SOURCE));
            }else{
	            switch(moduleSelected.type){
		            case "package":{  icon = new ImageIcon(Resource.get(Resource.ICON_PACKAGE));
			            if(moduleSelected.uniqueName.equals("xLibraries")){
			            	icon = new ImageIcon(Resource.get(Resource.ICON_EXTERNALLIB_GREEN));
			            }
		            break;
            		}
		            case "library":  icon = new ImageIcon(Resource.get(Resource.ICON_EXTERNALLIB_GREEN)); break;
		            case "class":{
		            	if (moduleSelected.visibility.equals("public")) {
		                    icon = new ImageIcon(Resource.get(Resource.ICON_CLASS_PUBLIC));
		                } 
		            	else {
		                    icon = new ImageIcon(Resource.get(Resource.ICON_CLASS_PRIVATE));
		                }
		            	break;  
			        }
		            case "interface": {
		            	if (moduleSelected.visibility.equals("public")) {
		                    icon = new ImageIcon(Resource.get(Resource.ICON_INTERFACE_PUBLIC));
		                }
		            	else {
		                    icon = new ImageIcon(Resource.get(Resource.ICON_INTERFACE_PRIVATE));
		                }
		            	break;
		            	
		            }
		            case "enumeration" : icon = new ImageIcon(Resource.get(Resource.ICON_ENUMERATION)); break;
		            default: icon = new ImageIcon(Resource.get(Resource.ICON_MODULE)); break;
	            }
            }
            label.setIcon(icon);

            if (moduleSelected.name.equals("")) {
                label.setText(uiController.translate("Application"));
            } else {
                label.setText(moduleSelected.name);
            }
            if (selected) {
                label.setBackground(backgroundSelectionColor);
            } else {
                label.setBackground(backgroundNonSelectionColor);
            }
        } catch (ClassCastException e) {
        }

        return label;
    }
}