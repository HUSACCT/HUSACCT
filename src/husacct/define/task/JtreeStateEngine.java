package husacct.define.task;


import husacct.define.analyzer.AnalyzedUnitComparator;
import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AnalyzedModuleComponent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.log4j.Logger;



public class JtreeStateEngine {
private static JtreeStateEngine instance =null;
private ArrayList<Map<Long,AbstractCombinedComponent>> orderofinsertions = new ArrayList<Map<Long,AbstractCombinedComponent>>();
private Logger logger;
	


public JtreeStateEngine()
{
	logger=Logger.getLogger(JtreeStateEngine.class);
}
	
 public static JtreeStateEngine instance()
{
	 
	 if (instance==null) {
	return	instance= new JtreeStateEngine();
	}else{
		
		return instance;
    }
}
 
 public void compareNewData(AnalyzedModuleComponent newdata)
 {
	
	importNewData(newdata);
	
 }


private void flush() 
{
   AnalyzedModuleTree mainTree = JtreeController.instance().getTree();
   for (Map<Long,AbstractCombinedComponent> usersequenceinput: orderofinsertions) 
   {
	for(Long key: usersequenceinput.keySet())
	  {
		AnalyzedModuleComponent unitTobeRestored= (AnalyzedModuleComponent) usersequenceinput.get(key);
		if (unitTobeRestored.getType().toUpperCase().equals("REGEX".toUpperCase())) {
			flushRegix(unitTobeRestored,mainTree);
			
		}else{
			mainTree.restoreTreeItem(unitTobeRestored);
		}
		
	  }
   
    }
	 
}
private void flushRegix(AnalyzedModuleComponent unitTobeRestored,AnalyzedModuleTree mainTree) {
	for(AbstractCombinedComponent result : unitTobeRestored.getChildren())
	{
		mainTree.restoreTreeItem((AnalyzedModuleComponent)result);
	}
	
	
}

private void compare(AnalyzedModuleComponent newdata) {
	AnalyzedModuleComponent currentparent = JtreeController.instance().getRootOfModel();
	AnalyzedUnitComparator c = new AnalyzedUnitComparator();
	c.calucalteChanges(currentparent, newdata);
	
	
}



private void restoreFlush() {
	 AnalyzedModuleTree mainTree = JtreeController.instance().getTree();
	 Collections.reverse(orderofinsertions);
	 ArrayList<Map<Long,AbstractCombinedComponent>> temp =orderofinsertions;
	 
	 orderofinsertions = new ArrayList<Map<Long,AbstractCombinedComponent>>();
	 try{ 
	 for (Map<Long,AbstractCombinedComponent> result1 : temp) 
	   {
		for(Long key :result1.keySet()){
		AnalyzedModuleComponent unitTobeRestored= (AnalyzedModuleComponent) result1.get(key);
		if (unitTobeRestored.getType().toUpperCase().equals("REGEX".toUpperCase())) {
			restoreflushRegix(key,unitTobeRestored,mainTree);
			
		}else{
			mainTree.removeTreeItem(key,unitTobeRestored);
		}	
		
		
		
		}
			
		}
	 }
	 catch(Exception o)
	 {
		 System.out.println(o.getMessage());
	 }
	   }

private void restoreflushRegix(long id,AnalyzedModuleComponent unitTobeRestored,
		AnalyzedModuleTree mainTree) {
	
	for(AbstractCombinedComponent result : unitTobeRestored.getChildren())
	{
		mainTree.removeTreeItem(id,(AnalyzedModuleComponent)result);
	}
}


	

public void registerSate(Long id,AbstractCombinedComponent inpute)
 {
	LinkedHashMap<Long, AbstractCombinedComponent> input = new LinkedHashMap<Long,AbstractCombinedComponent>(); 
	 input.put(id, inpute);
	 
	orderofinsertions.add(0,input);
	 
 }



public void importNewData(final AnalyzedModuleComponent newdata) {
	
		Thread first = new Thread(new Runnable() {
			
			@Override
			public void run() {
				logger.debug("Strating to reanalyze");
				flush();
				
			
				
			}
		});
		
		
		Thread second = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				compare(newdata);
					
			}
		});
		
		Thread third = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				restoreFlush();
				
				System.out.println("Finished Reanalyzing");
			}
		});
			
		
		try {
		first.start();
		first.join();
		second.start();
		second.join();
		third.start();
		
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
}

public void removeSoftwareUnit(long moduleId,
	AnalyzedModuleComponent unitTobeRemoved) {
	int index=0;
	for (int i=0;i< orderofinsertions.size();i++)
	{
		
		for(long key : orderofinsertions.get(i).keySet())
		{
			AnalyzedModuleComponent unitTochek =(AnalyzedModuleComponent) orderofinsertions.get(i).get(key);
		
		if(unitTochek.getUniqueName().toUpperCase().equals(unitTobeRemoved.getUniqueName().toUpperCase()))
				{
					index=i;
				}
		
		}	
	}
	
	orderofinsertions.remove(index);
	
	
}
	
	
	
	
	
	
}
