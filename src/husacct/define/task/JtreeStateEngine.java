package husacct.define.task;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser.object_creation_expression2_return;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AnalyzedModuleComponent;
import husacct.define.task.components.RegexComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

public class JtreeStateEngine {
 private static JtreeStateEngine instance =null;
private 	ArrayList<Map<Long,AbstractCombinedComponent>> orderofinsertions = new ArrayList<Map<Long,AbstractCombinedComponent>>();
	
	
	
	
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


private void flush() {
   AnalyzedModuleTree mainTree = JtreeController.instance().getTree();
   for (Map<Long,AbstractCombinedComponent> result1: orderofinsertions) 
   {
	for(Long key: result1.keySet())
	{
		AnalyzedModuleComponent unitTobeRestored= (AnalyzedModuleComponent) result1.get(key);
		
		
		
		if (unitTobeRestored.getType().toUpperCase().equals("REGEX".toUpperCase())) {
			flushRegix(unitTobeRestored,mainTree);
			
		}else
		{
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
	AnalyzedModuleComponent currentparent = JtreeController.instance().GetRootOfModel();
	
	compareChilderens(currentparent, newdata);
	
	
	
	
	
	
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
		 System.out.println("heluuurrr joohnnyy");
	 }
	   }

private void restoreflushRegix(long id,AnalyzedModuleComponent unitTobeRestored,
		AnalyzedModuleTree mainTree) {
	
	for(AbstractCombinedComponent result : unitTobeRestored.getChildren())
	{
		mainTree.removeTreeItem(id,(AnalyzedModuleComponent)result);
	}
}

private void compareChilderens(AnalyzedModuleComponent parentComponentleft, AnalyzedModuleComponent parentComponentright)
{
Collections.sort(parentComponentleft.getChildren());
Collections.sort(parentComponentright.getChildren());
	
	for (int i = 0; i < parentComponentleft.getChildren().size(); i++) {
	AbstractCombinedComponent left = parentComponentleft.getChildren().get(i);
	AbstractCombinedComponent right = parentComponentright.getChildren().get(i);

	if(left.getUniqueName().toUpperCase().equals(right.getUniqueName().toUpperCase()))
	{
		if(!left.getType().toUpperCase().equals(right.getType().toUpperCase()))
		{
			System.out.println("Type has changed from: "+left.getType()+" to: "+right.getType());
			left.setType(right.getType().toUpperCase());
		}
		
		
	}else
	{
		parentComponentleft.addChild(left);
		System.out.println("new code detected+ "+left.getUniqueName() );
	}
		
		compareChilderens((AnalyzedModuleComponent)left,(AnalyzedModuleComponent) right);
		
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
				System.out.println("first began");
				flush();
				
				System.out.println("first stopeed");
				
			}
		});
		
		
		Thread second = new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("second began"); 
				compare(newdata);
					System.out.println("second stopeed");
			}
		});
		
		Thread third = new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("third began");
				restoreFlush();
				
				System.out.println("third stopeed");
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
	for (int i=0;i< orderofinsertions.size();i++) {
		
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
