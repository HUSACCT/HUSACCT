package husacct.define.task;

import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;
import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.presentation.moduletree.CombinedModuleTreeModel;
import husacct.define.presentation.moduletree.ModuleTree;
import husacct.define.presentation.moduletree.SearchModuleCellRenderer;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;
import java.util.List;

public class JtreeController{
	private  AnalyzedModuleTree tree;
	private static JtreeController instance;	
	private AnalyzedModuleTree resultTree;
	private ModuleTree moduleTree;
	private  boolean isLoaded=false;

	public  JtreeController() {
	}

	public static JtreeController instance() {
		if(instance==null)
		{
			return instance= new JtreeController();
		}
		else {
			return instance;
		}
	}

	public void registerTreeRemoval(ModuleStrategy module) 
	{
		removeSoftWareUnits(module);
		for (ModuleStrategy m : module.getSubModules()) {
			registerTreeRemoval(m);
		}
	}

	private void removeSoftWareUnits(ModuleStrategy module) {
        for (SoftwareUnitDefinition unit : module.getUnits()) {
		AnalyzedModuleComponent softwareUnit= StateService.instance().getAnalyzedSoftWareUnit(unit);
		restoreTreeItem(softwareUnit);
        }
	}

	public  void setCurrentTree(AnalyzedModuleTree instanceoftree) {
		tree=instanceoftree;
	}

	public  boolean isLoaded() {
		return isLoaded;
	}

	public  void setLoadState(boolean value) {
		isLoaded=value;
	}

	public  AnalyzedModuleTree getTree() {
		return tree;
	}

	public AnalyzedModuleComponent getRootOfModel () {
		return (AnalyzedModuleComponent)instance.getTree().getModel().getRoot();
	}

	public void additemgetResultTree(AnalyzedModuleComponent analyzedModule) {
		analyzedModule.detach();
		AnalyzedModuleComponent rootOfResultTree = (AnalyzedModuleComponent)resultTree.getModel().getRoot();
		rootOfResultTree.detach();
		rootOfResultTree.addChild(analyzedModule);
		resultTree.setModel(new CombinedModuleTreeModel(rootOfResultTree));
		resultTree.setCellRenderer(new SearchModuleCellRenderer());
	}

	public ModuleTree getModuleTree() {
		return moduleTree;
	}

	public void setModuleTree(ModuleTree moduleTree) {
		this.moduleTree = moduleTree;
	}

	public AbstractCombinedComponent getMappedunits() {
		return (AbstractCombinedComponent) instance.getTree().getModel().getRoot();
	}

	public void restoreTreeItem(AnalyzedModuleComponent analyzeModuleTobeRestored) {
		tree.restoreTreeItem(analyzeModuleTobeRestored);
		tree.repaint();
	}
	
	public void removeTreeItem(AnalyzedModuleComponent unitToBeinserted) {
		tree.removeTreeItem(unitToBeinserted);
		tree.repaint();
	}
	
	public void setTreeModel(AnalyzedModuleComponent root) {
		if(tree==null) {
			tree= new AnalyzedModuleTree(root);
		}
		else {
			tree.setModel(new CombinedModuleTreeModel(root));
			tree.repaint();
		}
	}
	
	public void restoreTreeItems(ModuleStrategy module) {
		for (SoftwareUnitDefinition unit : module.getUnits()) {
			AnalyzedModuleComponent anal = StateService.instance().getAnalyzedSoftWareUnit(unit);
			restoreTreeItem(anal);
		}
	}
	
	public void restoreTreeItemm(List<String> softwareUnitNames, List<String> types) {
		for (String uniqname : softwareUnitNames) {
			AnalyzedModuleComponent tobeRestored= StateService.instance().getAnalyzedSoftWareUnit(uniqname);
			if (tobeRestored != null){
				tree.restoreTreeItem(tobeRestored);
				tree.repaint();
			}
		}
	}
	
	public List<AbstractCombinedComponent> getRootprojectsModules() {
		List<AbstractCombinedComponent> returnList = new ArrayList<AbstractCombinedComponent>();
		for (AbstractCombinedComponent result : getRootOfModel().getChildren()) {
			if (result.getType().toLowerCase().equals("root")) {
				returnList.addAll(result.getChildren());
			}
		}
		return returnList;
	}
	
}