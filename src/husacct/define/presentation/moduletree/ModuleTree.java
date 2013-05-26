package husacct.define.presentation.moduletree;

import husacct.define.presentation.jpopup.ModuletreeContextMenu;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.LayerComponent;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class ModuleTree extends JTree {

    private static final long serialVersionUID = 3282591641481691737L;
    private ModuletreeContextMenu moduletreeContextMenu;
    private MouseListener righclikmenu = new MouseListener() {

	@Override
	public void mouseClicked(MouseEvent e) {

	    if (e.getSource() instanceof ModuleTree
		    && e.getButton() == MouseEvent.BUTTON3) {
		ModuleTree temp = (ModuleTree) e.getSource();
		TreePath path = temp.getSelectionPath();
		AbstractDefineComponent selectedModule = (AbstractDefineComponent) path
			.getLastPathComponent();
		if (selectedModule instanceof LayerComponent) {
		    moduletreeContextMenu.isLayer();
		} else {
		    moduletreeContextMenu.isComponent();
		}

		moduletreeContextMenu
			.show(e.getComponent(), e.getX(), e.getY());
		moduletreeContextMenu.setVisible(true);
	    }

	}

	@Override
	public void mouseEntered(MouseEvent e) {
	    // TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
	    // TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
	    // TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
	    // TODO Auto-generated method stub

	}
    };

    public ModuleTree(AbstractDefineComponent rootComponent) {
	super(new CombinedModuleTreeModel(rootComponent));
	CombinedModuleCellRenderer moduleCellRenderer = new CombinedModuleCellRenderer();
	setCellRenderer(moduleCellRenderer);
	setDefaultSettings();
	addMouseListener(righclikmenu);
    }

    public void setContextMenu(ModuletreeContextMenu moduletreeContextMenu) {

	this.moduletreeContextMenu = moduletreeContextMenu;
    }

    public void setDefaultSettings() {
	getSelectionModel().setSelectionMode(
		TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    public void setSelectedRow(Long moduleId) {
	ArrayList<Object> pathParts = new ArrayList<Object>();

	TreeModel model = getModel();
	if (model != null) {
	    Object root = model.getRoot();
	    pathParts.add(root);
	    walk(root, moduleId, pathParts);
	}
    }

    private void walk(Object o, Long moduleId, ArrayList<Object> pathParts) {
	int cc;
	cc = getModel().getChildCount(o);
	for (int i = 0; i < cc; i++) {
	    AbstractDefineComponent child = (AbstractDefineComponent) getModel()
		    .getChild(o, i);

	    Long childModuleId = child.getModuleId();
	    if (childModuleId == moduleId) {
		pathParts.add(child);
		TreePath path = new TreePath(pathParts.toArray());
		setSelectionPath(path);
		return;
	    } else {
		if (!getModel().isLeaf(child)) {
		    @SuppressWarnings("unchecked")
		    ArrayList<Object> childPathParts = (ArrayList<Object>) pathParts
			    .clone();
		    childPathParts.add(child);
		    walk(child, moduleId, childPathParts);
		}
	    }
	}
    }

}
