package husacct.define.analyzer;

import husacct.define.domain.services.WarningMessageService;
import husacct.define.domain.warningmessages.WarningMessage;
import husacct.define.presentation.moduletree.CombinedModuleTreeModel;
import husacct.define.task.JtreeController;
import husacct.define.task.components.AbstractCombinedComponent;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;
import java.util.Collections;

public class AnalyzedUnitComparator {



	public void calucalteChanges(AbstractCombinedComponent left,AbstractCombinedComponent right)
	{
	ArrayList<AbstractCombinedComponent> toBeDeleted = new ArrayList<AbstractCombinedComponent>();
	ArrayList<AbstractCombinedComponent> toBeAaded = new ArrayList<AbstractCombinedComponent>();
	int leftsize= left.getChildren().size();
	int rightsize= right.getChildren().size();
	Collections.sort(left.getChildren());
	Collections.sort(right.getChildren());
	if(leftsize==rightsize)
	{
		
		isequal(left,right,toBeDeleted,toBeAaded);
	}else if (leftsize>rightsize) {
		isLessEqual(left, right, toBeDeleted, toBeAaded);
		
		
	}else if (leftsize<rightsize) {
		isequal(left, right, toBeDeleted, toBeAaded);
		isMoreEqual(left, right, toBeDeleted, toBeAaded);
	
	}
		
	for(AbstractCombinedComponent remove :toBeDeleted)
	{
		
		AbstractCombinedComponent parent = remove.getParentofChild();
		int index = parent.getChildren().indexOf(remove);
		parent.getChildren().remove(index);
	}
	
	
	for(AbstractCombinedComponent newAbstractCombinedComponent: toBeAaded)
	{
		if (WarningMessageService.getInstance().hasCodeLevelWarning((AnalyzedModuleComponent)newAbstractCombinedComponent)) {
			if (newAbstractCombinedComponent.getType().toLowerCase().equals("package")) {
				((AnalyzedModuleComponent)newAbstractCombinedComponent).freeze();
				left.addChild(newAbstractCombinedComponent);
			}
			
		} else {
			 left.addChild(newAbstractCombinedComponent);
		}
		
		
	}
	
	JtreeController.instance().getTree().setModel(new CombinedModuleTreeModel(left));
	
	}

	
	
	private void isequal(AbstractCombinedComponent left, AbstractCombinedComponent right, ArrayList<AbstractCombinedComponent> toBeDeleted, ArrayList<AbstractCombinedComponent> toBeAaded) {
		
		for (int i = 0; i < left.getChildren().size(); i++)
		{
			compareAbstractCombinedComponent(left.getChildren().get(i), right.getChildren().get(i), toBeDeleted, toBeAaded);
		 calucalteChanges(left.getChildren().get(i), right.getChildren().get(i));
		}
		 Collections.sort(left.getChildren());
		 Collections.sort(right.getChildren());
		}

	private void isLessEqual(AbstractCombinedComponent left, AbstractCombinedComponent right,
		ArrayList<AbstractCombinedComponent> toBeDeleted, ArrayList<AbstractCombinedComponent> toBeAaded) {
		int leftindex=left.getChildren().size();
		int rightindex=right.getChildren().size()-1;
		Collections.sort(left.getChildren());
		Collections.sort(right.getChildren());
		for (int i = 0; i < leftindex; i++) {
			
		if(rightindex>=i)
		{
			
			if(left.getChildren().get(i).getUniqueName().equals(right.getChildren().get(i).getUniqueName()))
			{
				ChekifTypeChanged(left,right);
			}else{
				toBeDeleted.add(left.getChildren().get(i));
				toBeAaded.add(right.getChildren().get(i));
			}
			
			
			
		}
		else
		{
			toBeDeleted.add(left.getChildren().get(i));
		}
		
		
		}
		
	}



		private void isMoreEqual(AbstractCombinedComponent left, AbstractCombinedComponent right,
			ArrayList<AbstractCombinedComponent> toBeDeleted, ArrayList<AbstractCombinedComponent> toBeAaded){
		
		int leftsize= left.getChildren().size();
		int rightsize= right.getChildren().size();
		
		for (int i = (rightsize-(rightsize-leftsize)); i < rightsize; i++) {
			
		boolean isfound=false;
			for (AbstractCombinedComponent u : toBeDeleted) {
			if(u.getUniqueName().equals(right.getChildren().get(i).getUniqueName()))
			{
				isfound=true;
				break;
			}
			}
			if (!isfound) {
				
				toBeAaded.add(right.getChildren().get(i));
			}
		}
		
	}
	

	private void compareAbstractCombinedComponent(AbstractCombinedComponent left ,AbstractCombinedComponent right
				,ArrayList<AbstractCombinedComponent>toBeDeleted,ArrayList<AbstractCombinedComponent> toBeAaded){
		
			String AbstractCombinedComponentL	=left.getUniqueName();
			String AbstractCombinedComponentR	=right.getUniqueName();

			if (AbstractCombinedComponentL.equals(AbstractCombinedComponentR)) {
				
				ChekifTypeChanged(left,right);
				
				}else if (!AbstractCombinedComponentL.equals(AbstractCombinedComponentR)) {
		         toBeDeleted.add(left);
		         toBeAaded.add(right);
				}
			     Collections.sort(left.getChildren());
			     Collections.sort(right.getChildren());
				}
			
			
	
   private void ChekifTypeChanged(AbstractCombinedComponent left, AbstractCombinedComponent right) {
		
		if(!left.getType().equals(right.getType()))
	{
			  
		left.setType(right.getType());
	}
		
	}
   
   
 


	

}





















