package husacct.define.presentation.moduletree;

import java.awt.Component;
import java.util.ArrayList;

import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.SoftwareUnitDefinition.Type;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.AnalyzedModuleComponent;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class CombinedModuleTree extends JTree {

	private static final long serialVersionUID = 1859193273511893860L;
	
	private long selectedModuleId;

	public CombinedModuleTree(AbstractCombinedComponent rootComponent, long selectedModuleId) {
		super(new CombinedModuleTreeModel(rootComponent));
		this.setSelectedModuleId(selectedModuleId);
		CombinedModuleCellRenderer moduleCellRenderer = new CombinedModuleCellRenderer();
	    this.setCellRenderer(moduleCellRenderer);
	    this.setDefaultSettings();
	}
	
	public void setDefaultSettings() {
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}

	public long getSelectedModuleId() {
		return selectedModuleId;
	}

	public void setSelectedModuleId(long selectedModuleId) {
		this.selectedModuleId = selectedModuleId;
	}
	
	public Object getSelectedTreeValue(){
		TreePath path = this.getSelectionPath();
		Object returnObject = null;
		if (path != null){
			AbstractCombinedComponent selectedComponent = (AbstractCombinedComponent) path.getLastPathComponent();
			
			if(selectedComponent instanceof AbstractDefineComponent) {
				AbstractDefineComponent defineComponent = (AbstractDefineComponent) selectedComponent;
				returnObject = defineComponent.getModuleId();
				
			} else if(selectedComponent instanceof AnalyzedModuleComponent) {
				AnalyzedModuleComponent analyzedComponent = (AnalyzedModuleComponent) selectedComponent;
				String uniqueName = analyzedComponent.getUniqueName();
				String stringType = analyzedComponent.getType();
				Type type = Type.valueOf(stringType);
				SoftwareUnitDefinition su = new SoftwareUnitDefinition(uniqueName, type);
				returnObject = su;
			}
		}
		return returnObject;
	}
	
	public void setSelectedRow(Long moduleId){
		ArrayList<Object> pathParts = new ArrayList<Object>();
		
		TreeModel model = this.getModel();
		if (model != null) {
			Object root = model.getRoot();
			pathParts.add(root);
			walk(root, moduleId, pathParts);
		}
	}
	
	private void walk(Object o, Long moduleId, ArrayList<Object> pathParts){
		int childCount;
		childCount = this.getModel().getChildCount(o);
		for( int i=0; i < childCount; i++) {
			Component component = (Component) this.getModel().getChild(o, i );
			
			if (component instanceof AbstractDefineComponent){
			
				AbstractDefineComponent child = (AbstractDefineComponent) this.getModel().getChild(o, i );
				
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
}