package husacct.define.task;


import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.presentation.moduletree.CombinedModuleTreeModel;
import husacct.define.presentation.moduletree.ModuleTree;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.AnalyzedModuleComponent;
import husacct.define.task.components.RegexComponent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.SoftwareUnitRegExDefinition;
import husacct.define.domain.module.Module;

public class JtreeController {
private  AnalyzedModuleTree tree;
private static JtreeController instance;	
private AnalyzedModuleTree resultTree;
private ModuleTree moduleTree;
private AnalyzedModuleTree editTree;

	private  Map<Long,LinkedHashMap<String,AbstractCombinedComponent>> moduleRegistry = new LinkedHashMap<Long,LinkedHashMap<String,AbstractCombinedComponent>>();
	private  Map<String,AbstractCombinedComponent> regixRegistry =new LinkedHashMap<String,AbstractCombinedComponent>()  ;
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



	public  void registerTreeRemoval(long moduleId,AbstractCombinedComponent removedSoftwareunit)
	{

		if(moduleRegistry.get(moduleId) == null)
		{

			LinkedHashMap<String,AbstractCombinedComponent> analyzedComponents = new LinkedHashMap<String, AbstractCombinedComponent>();
			analyzedComponents.put(((AnalyzedModuleComponent) removedSoftwareunit).getUniqueName() , removedSoftwareunit);
			moduleRegistry.put(moduleId, analyzedComponents);

		}else{

			LinkedHashMap<String,AbstractCombinedComponent> analyzedComponents=moduleRegistry.get(moduleId);
			analyzedComponents.put(((AnalyzedModuleComponent) removedSoftwareunit).getUniqueName() , removedSoftwareunit);
		}


	} 


	public void registerTreeRemoval(Module module) 
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
		registerTreeRestore(module.getId());
	}


	public  void registerTreeRestore(long moduleId)
	{
		registerTreeRestoreByModuleId(moduleRegistry.get(moduleId));
	}


	private  void registerTreeRestoreByModuleId(LinkedHashMap<String,AbstractCombinedComponent> content)
	{
		for(String key : content.keySet())
		{
			tree.restoreTreeItem((AnalyzedModuleComponent)content.get(key));
		}
	}


	public  void registerTreeRestore(long moduleId,String removedSoftwareunitUniqName)
	{
		AnalyzedModuleComponent  unitTobeRemoved   =  (AnalyzedModuleComponent) moduleRegistry.get(moduleId).get(removedSoftwareunitUniqName);
		tree.restoreTreeItem(unitTobeRemoved);
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

	//moet naar het tree object ^_^
	public void registerTreeRemoval(long moduleId, SoftwareUnitDefinition unit) {



	}



	public AnalyzedModuleComponent GetRootOfModel ()
	{

		return (AnalyzedModuleComponent)instance.getTree().getModel().getRoot();
	}




	public AnalyzedModuleTree getResultTree() 
	{
		RegexComponent root = new RegexComponent("root","Regix results","regix","public");

		resultTree= new AnalyzedModuleTree(root);

		return resultTree;

	}

	public void additemgetResultTree(AnalyzedModuleComponent anal) 
	{
anal.freeze();
AnalyzedModuleComponent temp= (AnalyzedModuleComponent)resultTree.getModel().getRoot();
temp.freeze();
temp.addChild(anal);

resultTree.setModel(new CombinedModuleTreeModel(temp));



}

public static void registerResultRemovalspiiii() {

	TreeSelectionModel modulepath = instance.moduleTree.getSelectionModel();
	TreePath selectedmodule= modulepath.getSelectionPath();
	long moduleId = ((AbstractDefineComponent)selectedmodule.getLastPathComponent()).getModuleId();
	TreeSelectionModel paths = instance.resultTree.getSelectionModel();
	SoftwareUnitController softwareUnitController  = new SoftwareUnitController(moduleId);
	for (TreePath path : paths.getSelectionPaths()){
		AnalyzedModuleComponent selectedComponent = (AnalyzedModuleComponent) path.getLastPathComponent();
		
			softwareUnitController.save(selectedComponent);
	
}
}

public ModuleTree getModuleTree() {
	
	return moduleTree;
}

public void setModuleTree(ModuleTree moduleTree) {
	this.moduleTree = moduleTree;
}

 public AnalyzedModuleComponent registerRegix(String regExName) {
	AnalyzedModuleComponent regixwrapper = (AnalyzedModuleComponent)instance.resultTree.getModel().getRoot();
	 regixwrapper.setUniqueName(regExName);
	 instance.regixRegistry.put(regExName,regixwrapper);
	return regixwrapper;
}

public AnalyzedModuleTree getRegixTree(String editingRegEx) {
	
	RegexComponent result= (RegexComponent) instance.regixRegistry.get(editingRegEx);
	editTree = new AnalyzedModuleTree(result);
	
	return editTree;
}
}