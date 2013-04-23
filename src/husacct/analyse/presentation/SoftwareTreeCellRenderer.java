package husacct.analyse.presentation;

import husacct.common.Resource;
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
    private AnalyseUIController uiController;

    SoftwareTreeCellRenderer(AnalyseUIController uiController) {
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
            AnalysedModuleDTO moduleSelected = (AnalysedModuleDTO) node.getUserObject();
            ImageIcon icon;
            if (moduleSelected.uniqueName.equals("")) {
                icon = new ImageIcon(Resource.get(Resource.ICON_SOURCE));
            } else if (moduleSelected.type.equals("package")) {
                icon = new ImageIcon(Resource.get(Resource.ICON_PACKAGE));
            } else if (moduleSelected.type.equals("library")) {
                icon = new ImageIcon(Resource.get(Resource.ICON_PACKAGE));
            } else if (moduleSelected.type.equals("class")) {
                if (moduleSelected.visibility.equals("public")) {
                    icon = new ImageIcon(Resource.get(Resource.ICON_CLASS_PUBLIC));
                } else {
                    icon = new ImageIcon(Resource.get(Resource.ICON_CLASS_PRIVATE));
                }
            } else if (moduleSelected.type.equals("interface")) {
                if (moduleSelected.visibility.equals("public")) {
                    icon = new ImageIcon(Resource.get(Resource.ICON_INTERFACE_PUBLIC));
                } else {
                    icon = new ImageIcon(Resource.get(Resource.ICON_INTERFACE_PRIVATE));
                }
            } else if (moduleSelected.type.equals("library")) {
                icon = new ImageIcon(Resource.get(Resource.ICON_INTERFACE_PUBLIC));
            } else if (moduleSelected.type.equals("enumeration")) {
                icon = new ImageIcon(Resource.get(Resource.ICON_ENUMERATION));
            } else {
                icon = new ImageIcon(Resource.get(Resource.ICON_MODULE));
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