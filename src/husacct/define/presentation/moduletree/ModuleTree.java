package husacct.define.presentation.moduletree;

import husacct.define.task.components.AbstractDefineComponent;

import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class ModuleTree extends JTree {

    private static final long serialVersionUID = 3282591641481691737L;

    public ModuleTree(AbstractDefineComponent rootComponent) {
        super(new CombinedModuleTreeModel(rootComponent));
        CombinedModuleCellRenderer moduleCellRenderer = new CombinedModuleCellRenderer();
        this.setCellRenderer(moduleCellRenderer);
        this.setDefaultSettings();
    }

    public void setDefaultSettings() {
        this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    public void setSelectedRow(Long moduleId) {
        ArrayList<Object> pathParts = new ArrayList<Object>();

        TreeModel model = this.getModel();
        if (model != null) {
            Object root = model.getRoot();
            pathParts.add(root);
            walk(root, moduleId, pathParts);
        }
    }

    private void walk(Object o, Long moduleId, ArrayList<Object> pathParts) {
        int cc;
        cc = this.getModel().getChildCount(o);
        for (int i = 0; i < cc; i++) {
            AbstractDefineComponent child = (AbstractDefineComponent) this.getModel().getChild(o, i);

            Long childModuleId = child.getModuleId();
            if (childModuleId == moduleId) {
                pathParts.add(child);
                TreePath path = new TreePath(pathParts.toArray());
                this.setSelectionPath(path);
                return;
            } else {
                if (!this.getModel().isLeaf(child)) {
                    @SuppressWarnings("unchecked")
                    ArrayList<Object> childPathParts = (ArrayList<Object>) pathParts.clone();
                    childPathParts.add(child);
                    walk(child, moduleId, childPathParts);
                }
            }
        }
    }
}
