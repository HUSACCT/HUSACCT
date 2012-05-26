package husacct.define.presentation.moduletree;

import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.SoftwareUnitDefinition.Type;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.AnalyzedModuleComponent;

import javax.swing.JTree;
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
}
