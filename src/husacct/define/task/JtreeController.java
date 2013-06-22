package husacct.define.task;



import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.seperatedinterfaces.ISofwareUnitSeperatedInterface;
import husacct.define.domain.services.SoftwareUnitDefinitionDomainService;
import husacct.define.domain.services.UndoRedoService;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.domain.softwareunit.ExpressionUnitDefinition;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;
import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.presentation.moduletree.CombinedModuleTreeModel;
import husacct.define.presentation.moduletree.ModuleTree;
import husacct.define.presentation.moduletree.SearchModuleCellRenderer;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AnalyzedModuleComponent;
import husacct.define.task.components.RegexComponent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.tree.TreePath;


	public class JtreeController implements ISofwareUnitSeperatedInterface{
	private  AnalyzedModuleTree tree;
	private static JtreeController instance;	
	private AnalyzedModuleTree resultTree;
	private ModuleTree moduleTree;
	private AnalyzedModuleTree editTree;
   private  boolean isLoaded=false;

	public  JtreeController()
	{
      UndoRedoService.getInstance().registerObserver(this);
	}

	public static JtreeController instance()
	{
		if(instance==null)
		{
			return instance= new JtreeController();

		}else{
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
		AnalyzedModuleComponent softwareUnit=	StateService.instance().getAnalyzedSoftWareUnit(unit);
		restoreTreeItem(softwareUnit);
        
        }
		
	}

	public  void setCurrentTree(AnalyzedModuleTree instanceoftree)
	{
		tree=instanceoftree;
	}

	public  boolean isLoaded() 
	{
		return isLoaded;
	}

	public  void setLoadState(boolean value) 
	{
		isLoaded=value;
	}

	public  AnalyzedModuleTree getTree() 
	{
		return tree;
	}

	



	public AnalyzedModuleComponent getRootOfModel ()
	{

		return (AnalyzedModuleComponent)instance.getTree().getModel().getRoot();
	}




	public AnalyzedModuleTree getResultTree() 
	{
		RegexComponent root = new RegexComponent("Found results:","Nothing found","SEARCH","public");

		resultTree= new AnalyzedModuleTree(root);
       
		return resultTree;

	}

	public void additemgetResultTree(AnalyzedModuleComponent analyzedModule) 
	{

		
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

 public RegexComponent registerRegix(String regExName) {
	
	 RegexComponent regixwrapper = new RegexComponent(); 
	 TreePath[] paths = instance.resultTree.getSelectionPaths();
	 for (TreePath treePath : paths) {
		AnalyzedModuleComponent op = (AnalyzedModuleComponent)treePath.getLastPathComponent();
		removeTreeItem(StateService.instance().getAnalyzedSoftWareUnit(op.getUniqueName().toLowerCase()));
		
		regixwrapper.addChild(op);
	}
	regixwrapper.setName(regExName);
	regixwrapper.setType("regex");
	regixwrapper.setUniqueName(regExName);
	regixwrapper.setVisibility("public");

	return regixwrapper;
}

public AnalyzedModuleTree getRegixTree(String editingRegEx) {
	
	RegexComponent  result = new RegexComponent("root","editRegex","SEARCH","public");
result.setRegex(new SoftwareUnitDefinitionDomainService().getExpressionByName(DefinitionController.getInstance().getSelectedModuleId(),editingRegEx));
	editTree = new AnalyzedModuleTree(result);
	
	return editTree;
}

public  void restoreRegexWrapper(ExpressionUnitDefinition unit) {
	   
for (SoftwareUnitDefinition result : unit.getExpressionValues()) {
AnalyzedModuleComponent unitToBeRestored=	StateService.instance().getAnalyzedSoftWareUnit(result);
unitToBeRestored.unfreeze();

}
		
	
	
	

	
}

public AbstractCombinedComponent getMappedunits() {
	
	return (AbstractCombinedComponent) instance.getTree().getModel().getRoot();
}


public void restoreTreeItem(AnalyzedModuleComponent analyzeModuleTobeRestored) {
	tree.restoreTreeItem(analyzeModuleTobeRestored);
	tree.repaint();
}

public void removeTreeItem(AnalyzedModuleComponent unitToBeinserted) {
	if (unitToBeinserted instanceof RegexComponent) {
		restoreRegex((RegexComponent) unitToBeinserted);
	}else{
	tree.removeTreeItem(unitToBeinserted);
	tree.repaint();
	}
}

public void setTreeModel(AnalyzedModuleComponent root) {
	if(tree==null)
	{
		tree= new AnalyzedModuleTree(root);
	}else
	{
		tree.setModel(new CombinedModuleTreeModel(root));
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
		if (tobeRestored instanceof RegexComponent) {
			restoreRegex((RegexComponent)tobeRestored);
		}else{
		
			tree.restoreTreeItem(tobeRestored);
		tree.repaint();
		}
		}
	
}

private void restoreRegex(RegexComponent tobeRestored) {
for (AbstractCombinedComponent unit : tobeRestored.getChildren()) {
	AnalyzedModuleComponent referenceditem = StateService.instance().getAnalyzedSoftWareUnit(unit.getUniqueName().toLowerCase());
	tree.removeTreeItem(referenceditem);
	tree.repaint();
}
	
}

@Override
public void addSeperatedSoftwareUnit(List<SoftwareUnitDefinition> units,
		long moduleID) {
	for (SoftwareUnitDefinition softwareUnitDefinition : units) {
	AnalyzedModuleComponent	unitToBeinserted=	StateService.instance().getAnalyzedSoftWareUnit(softwareUnitDefinition);
		removeTreeItem(unitToBeinserted);
		
	}
	
}

@Override
public void removeSeperatedSoftwareUnit(List<SoftwareUnitDefinition> units,
		long moduleId) {
	for (SoftwareUnitDefinition softwareUnitDefinition : units) {
		AnalyzedModuleComponent	unitToBeinserted=	StateService.instance().getAnalyzedSoftWareUnit(softwareUnitDefinition);
			restoreTreeItem(unitToBeinserted);
			
		}
	
}

public List<AbstractCombinedComponent> getRootprojectsModules()
{
	List<AbstractCombinedComponent> returnList = new ArrayList<AbstractCombinedComponent>();
	
	for (AbstractCombinedComponent result : getRootOfModel().getChildren()) {
		if (result.getType().toLowerCase().equals("root")) {
			returnList.addAll(result.getChildren());
		}
	}



return returnList;
}

public RegexComponent createRegexRepresentation(String editingRegEx,
		ArrayList<AnalyzedModuleComponent> components) {
	RegexComponent expresion = new RegexComponent(editingRegEx,"root","REGEX","public");
	for (AnalyzedModuleComponent unit : components) {
		expresion.addChild(unit);
	}
	return expresion;
}

@Override
public void addExpression(long moduleId, ExpressionUnitDefinition expression) {
	for (SoftwareUnitDefinition unit : expression.getExpressionValues()) {
		AnalyzedModuleComponent result = StateService.instance().getAnalyzedSoftWareUnit(unit);
		tree.removeTreeItem(result);
	}
	tree.repaint();
}

@Override
public void removeExpression(long moduleId, ExpressionUnitDefinition expression) {
	for (SoftwareUnitDefinition unit : expression.getExpressionValues()) {
		AnalyzedModuleComponent result = StateService.instance().getAnalyzedSoftWareUnit(unit);
		tree.restoreTreeItem(result);
		
	}	
	tree.repaint();
}

@Override
public void editExpression(long moduleId, ExpressionUnitDefinition oldExpresion, ExpressionUnitDefinition newExpression) {
	// TODO Auto-generated method stub
	
}

public void removeRegexTreeItem(RegexComponent softwareunit) {
	for (AbstractCombinedComponent unit : softwareunit.getChildren()) {
		
		removeTreeItem((AnalyzedModuleComponent)unit);
	}
	
}

@Override
public void switchSoftwareUnitLocation(long fromModule, long toModule,
		List<String> uniqNames) {
	// TODO Auto-generated method stub
	
}


}