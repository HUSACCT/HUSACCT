package husacct.define.task;



import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.module.ToBeImplemented.ModuleStrategy;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.presentation.moduletree.CombinedModuleTreeModel;
import husacct.define.presentation.moduletree.ModuleTree;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AnalyzedModuleComponent;
import husacct.define.task.components.RegexComponent;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.tree.TreePath;


	public class JtreeController {
	private  AnalyzedModuleTree tree;
	private static JtreeController instance;	
	private AnalyzedModuleTree resultTree;
	private ModuleTree moduleTree;
	private AnalyzedModuleTree editTree;
   private  boolean isLoaded=false;

	public  JtreeController()
	{

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
		boolean pass=module.getSubModules().size()>0 ?true:false;

		while(pass)
		{
			for (int i =0; i <module.getSubModules().size(); i++)
			{
				registerTreeRemoval(module.getSubModules().get(i));
				pass=false;
			}

		}
		
	}





	private  void registerTreeRestoreByModuleId(LinkedHashMap<String,AbstractCombinedComponent> content)
	{
		for(String key : content.keySet())
		{
			tree.restoreTreeItem((AnalyzedModuleComponent)content.get(key));
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
		RegexComponent root = new RegexComponent("root","Regix results","SEARCH","public");

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
		
		regixwrapper.addChild((AnalyzedModuleComponent)treePath.getLastPathComponent());
	}
	regixwrapper.setName(regExName);
	regixwrapper.setType("regex");
	regixwrapper.setUniqueName(regExName);
	regixwrapper.setVisibility("public");

	return regixwrapper;
}

public AnalyzedModuleTree getRegixTree(String editingRegEx) {
	
	RegexComponent  result = new RegexComponent();

	editTree = new AnalyzedModuleTree(result.getWrapper());
	
	return editTree;
}

public  void restoreRegexWrapper(String name) {
	   

		
	
	
	

	
}

public AbstractCombinedComponent getMappedunits() {
	
	return (AbstractCombinedComponent) instance.getTree().getModel().getRoot();
}

public void editRegex(long moduleId,ArrayList<AnalyzedModuleComponent> components,
		String editingRegEx) {
	
	
	
	
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
	if(tree==null)
	{
		tree= new AnalyzedModuleTree(root);
	}else
	{
		tree.setModel(new CombinedModuleTreeModel(root));
	}
	
}

public void restoreTreeItems(ModuleStrategy module) {
	for (SoftwareUnitDefinition def : module.getUnits()) {
		StateService.instance().removeSoftwareUnit(module, def);
		
	}
	for (ModuleStrategy mod : module.getSubModules()) {
		restoreTreeItems(mod);
	}
	
	
	
	
}



}