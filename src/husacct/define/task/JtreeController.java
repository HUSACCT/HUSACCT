package husacct.define.task;


import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AnalyzedModuleComponent;
import husacct.define.task.conventions_checker.AnalyzedComponentHelper;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.module.Module;

public class JtreeController {
private  AnalyzedModuleTree tree;
private static JtreeController instance=null;	

private  Map<Long,LinkedHashMap<String,AbstractCombinedComponent>> moduleRegistry = new LinkedHashMap<Long,LinkedHashMap<String,AbstractCombinedComponent>>();
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
	
	String[] adress=unit.getName().split("\\.");
	AbstractCombinedComponent temp= (AbstractCombinedComponent)instance.tree.getModel().getRoot();
	String re="";
	
	for (int i = 0; i < adress.length; i++) 
	{
		System.out.println(adress[i]+"---------------"+"adresss");
		for(AbstractCombinedComponent item : temp.getChildren())
		{
				AnalyzedModuleComponent chek = (AnalyzedModuleComponent) item;
				String[] result= chek.getUniqueName().split("\\.");
				System.out.println("reault  "+result[result.length-1]+ "  ---"+result.length+"  "+chek.getUniqueName());
				if(adress[i].equals(result[0]))
				{
					
					temp=item;
					System.out.println(result[0]+"---------<><>"+adress[i]);
				}
		
	     }
	
	}
	AnalyzedModuleComponent result = (AnalyzedModuleComponent)temp;
	//quikfix
	
	for(AbstractCombinedComponent r:result.getChildren())
	{
		AnalyzedModuleComponent h = (AnalyzedModuleComponent)r;
		
		if(h.getUniqueName().toLowerCase().equals(re.toLowerCase()))
		{
			result=h;
		}
	}
	
	if(!result.getUniqueName().equals("root"))
	{
		System.out.println(result.getUniqueName());
	instance().tree.removeTreeItem(moduleId, result);
	}
	
	
	
}



 

}



