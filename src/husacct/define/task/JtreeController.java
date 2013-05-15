package husacct.define.task;



import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.presentation.moduletree.CombinedModuleTreeModel;
import husacct.define.presentation.moduletree.ModuleTree;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AnalyzedModuleComponent;
import husacct.define.task.components.RegexComponent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.tree.TreePath;




import husacct.define.domain.module.Module;
import husacct.define.domain.services.SoftwareUnitDefinitionDomainService;

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
		JtreeStateEngine.instance().removeSoftwareUnit(moduleId,unitTobeRemoved);
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
	instance.regixRegistry.put(regExName,regixwrapper);
	return regixwrapper;
}

public AnalyzedModuleTree getRegixTree(String editingRegEx) {
	
	RegexComponent  result = new RegexComponent();
	result.setWrapper((AnalyzedModuleComponent) instance.regixRegistry.get(editingRegEx));
	editTree = new AnalyzedModuleTree(result.getWrapper());
	
	return editTree;
}

public  void restoreRegexWrapper(String name) {
	   
	AbstractCombinedComponent regixwrapper= regixRegistry.get(name);
	for (AbstractCombinedComponent result : regixwrapper.getChildren() ) {
		instance.tree.restoreTreeItem((AnalyzedModuleComponent)result);
		
	}
	
	

	
}

public AbstractCombinedComponent getMappedunits() {
	
	return (AbstractCombinedComponent) instance.getTree().getModel().getRoot();
}

public void editRegex(long moduleId,ArrayList<AnalyzedModuleComponent> components,
		String editingRegEx) {
	AbstractCombinedComponent temp =   regixRegistry.get(editingRegEx);
	   
   for(AbstractCombinedComponent result : components)
   {
	   int index =temp.getChildren().indexOf(result);
	   
	   if(index !=-1)
	   {
		   tree.restoreTreeItem((AnalyzedModuleComponent) result);
		   
		   temp.getChildren().remove(index);
		   if(temp.getChildren().size()==0)
		   {
			   SoftwareUnitDefinitionDomainService domainService = new SoftwareUnitDefinitionDomainService();
			   domainService.removeRegExSoftwareUnit(moduleId, editingRegEx);
			   AbstractCombinedComponent parent=   temp.getParentofChild();
			 int indexofchild = parent.getChildren().indexOf(temp);
			 parent.getChildren().remove(indexofchild);
			 Collections.sort(parent.getChildren());
			 parent.updateChilderenPosition();
			 
			   
		   }
		   
	   }
	   
	   
   }
	
	
	
}



}