package husacct.define.presentation.moduletree;

import java.util.ArrayList;
import java.util.Collections;

import husacct.define.task.JtreeController;
import husacct.define.task.JtreeStateEngine;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AnalyzedModuleComponent;
import husacct.define.task.components.RegexComponent;

import javax.swing.JTree;
import javax.swing.tree.TreeSelectionModel;

public class AnalyzedModuleTree extends JTree {

	private static final long serialVersionUID = 3282591641481691737L;
	
	public AnalyzedModuleTree(AnalyzedModuleComponent rootComponent) {
		super(new CombinedModuleTreeModel(rootComponent));
		CombinedModuleCellRenderer moduleCellRenderer = new CombinedModuleCellRenderer();
	    this.setCellRenderer(moduleCellRenderer);
	    this.setDefaultSettings();
	}
	
	public void setDefaultSettings() {
		this.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
	}
	
	
	
	public void restoreTreeItem(AnalyzedModuleComponent analyzedsoftwarecomponent)
	{
		ArrayList<Integer> position= 	getQueryofposition(analyzedsoftwarecomponent);
		AnalyzedModuleComponent rootComponent=(AnalyzedModuleComponent)this.getModel().getRoot();
		AnalyzedModuleComponent bufferComponent;
		bufferComponent=rootComponent;
	 for(int i=0;i<position.size();i++)
	 {
		if(i+1==position.size())
		{
			bufferComponent.addChild(analyzedsoftwarecomponent);
			Collections.sort(bufferComponent.getChildren());
			bufferComponent.updateChilderenPosition();
			this.setModel(new CombinedModuleTreeModel(rootComponent));
		}
		else{
			int valueofposition =position.get(i);
			bufferComponent=(AnalyzedModuleComponent)bufferComponent.getChildren().get(valueofposition);
		} 
	    }
		}
	
	
	
	public void removeTreeItem(long moduleId,AnalyzedModuleComponent analyzedsoftwarecomponent)
	{
		
		AnalyzedModuleComponent rootComponent=(AnalyzedModuleComponent)this.getModel().getRoot();
		AnalyzedModuleComponent bufferComponent;
		bufferComponent=rootComponent;
		ArrayList<Integer> position= 	getQueryofposition(analyzedsoftwarecomponent);
		
		
		
		 for(int i=0;i<position.size();i++)
		 {
			 if(i+1==position.size())
			{
				
			    int positionOfchild=(position.get(position.size()-1));
				
				
				
				
				JtreeController.instance().registerTreeRemoval(moduleId,bufferComponent.getChildren().get(positionOfchild));
					JtreeStateEngine.instance().registerSate(moduleId,bufferComponent.getChildren().get(positionOfchild));
				
				
				bufferComponent.getChildren().remove(positionOfchild);
				Collections.sort(bufferComponent.getChildren());
				bufferComponent.updateChilderenPosition();
				this.setModel(new CombinedModuleTreeModel(rootComponent));
				
				
			}
			else{
				bufferComponent=(AnalyzedModuleComponent) bufferComponent.getChildren().get(position.get(i));
			} 
		    }
		    }
	
	
	
	
	

	private ArrayList<Integer> getQueryofposition(AnalyzedModuleComponent analyzedsoftwarecomponent) {
		ArrayList<Integer> retrievedposition = new ArrayList<Integer>();
		AnalyzedModuleComponent temp=analyzedsoftwarecomponent;
		boolean stop=true;
		while(stop)
		{
			retrievedposition.add(temp.getAnalyzedModuleComponentPosition());
			if(temp.getParentofChild().getUniqueName().equals("root"))
		{
			stop=false;
		}else{
			temp=temp.getParentofChild();
			continue;
		}
		
			
			
			
		}
		Collections.reverse(retrievedposition);
		
		return retrievedposition;
		
		}

	public void removeRegexTreeItem(long id, RegexComponent softwareunit) {
		
		
		for(AbstractCombinedComponent result: softwareunit.getChildren()){
		
		
		AnalyzedModuleComponent rootComponent=(AnalyzedModuleComponent)this.getModel().getRoot();
		AnalyzedModuleComponent bufferComponent;
		bufferComponent=rootComponent;
		ArrayList<Integer> position= 	getQueryofposition((AnalyzedModuleComponent)result);
		
		
		
		 for(int i=0;i<position.size();i++)
		 {
			 if(i+1==position.size())
			{
				
			    int positionOfchild=(position.get(position.size()-1));
				
				
				
				
								
				
				bufferComponent.getChildren().remove(positionOfchild);
				Collections.sort(bufferComponent.getChildren());
				bufferComponent.updateChilderenPosition();
				this.setModel(new CombinedModuleTreeModel(rootComponent));
				
				
			}
			else{
				bufferComponent=(AnalyzedModuleComponent) bufferComponent.getChildren().get(position.get(i));
			} 
		    }
		 
		 
		
	}
		JtreeController.instance().registerTreeRemoval(id,softwareunit);
		JtreeStateEngine.instance().registerSate(id,softwareunit);

	
	
	
	}
}
