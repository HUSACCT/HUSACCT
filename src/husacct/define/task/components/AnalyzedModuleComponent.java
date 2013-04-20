package husacct.define.task.components;

import java.util.ArrayList;

public class AnalyzedModuleComponent extends AbstractCombinedComponent {

	private static final long serialVersionUID = 1713515026043620607L;
	
	private String uniqueName = "";
	private String type = "";
	private String visibility;
	private boolean isfrozen=false;
	
	public AnalyzedModuleComponent() {
		super();
	}
	
	public AnalyzedModuleComponent(String uniqueName, String name, String type, String visibility) {
		this();
		this.setUniqueName(uniqueName);
		this.setName(name);
		this.setType(type);
		this.setVisibility(visibility);
		if(uniqueName.equals("root"))
		{
			setParentOfChild(this);
		}
	}

	public void addChild(AbstractCombinedComponent child) {
		if(!isfrozen){
		child.setParentOfChild(this);
		child.setAnalyzedModuleComponentPosition(children.size());
		}
		this.children.add(child);
	}
	
	public void addChild(int index, AbstractCombinedComponent child) {
		
		child.setParentOfChild(this);
		child.setAnalyzedModuleComponentPosition(index);
		
		this.children.add(index, child);
	}

	public void setChildren(ArrayList<AbstractCombinedComponent> children) {
		for(AbstractCombinedComponent a: children)
		{
			
			a.setParentOfChild(this);
		}
		
		
		this.children = children;
	}

	public ArrayList<AbstractCombinedComponent> getChildren() {
		
		return this.children;
	}

	public void removeChild(AbstractCombinedComponent child) {
		for(AbstractCombinedComponent currentchild : this.children) {
			if(currentchild.equals(child)) {
				this.children.remove(currentchild);
			}
		}
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	public String getUniqueName() {
		return this.uniqueName;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type.toUpperCase();
	}

	public String getVisibility() {
		return this.visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public void updateChilderenPosition() {
		for(int i=0;i<children.size();i++)
		{
		int newPosition=i;
		children.get(i).setAnalyzedModuleComponentPosition(newPosition);
			
		}
		
		
	
	
		
	}
	
	@Override
	public boolean equals(Object analyzedComponent){
	   if(analyzedComponent==null){return false;}
	   
	   if(analyzedComponent instanceof AnalyzedModuleComponent && (this.getUniqueName().equals("root")&& ((AnalyzedModuleComponent)analyzedComponent).getUniqueName().equals("root")))
	   {
		   
		   chekme(this.getChildren(),((AnalyzedModuleComponent)analyzedComponent).getChildren());
		   return false;
	   }
	   else 
	   {
		   return false;
	   }
		
	   
	}
	
	
	
	public void chekme(ArrayList<AbstractCombinedComponent> firstroot,ArrayList<AbstractCombinedComponent> secondroot)
	{
		ArrayList<Integer> found = new ArrayList<Integer>();
		int sizefirst = firstroot.size();
		int sizesecond = secondroot.size();
		int indexfirst=0;
		int indexsecond=0;
		
		if(sizefirst==sizesecond)
		{
			
			for(AbstractCombinedComponent chek : firstroot)
			{
				
				for(AbstractCombinedComponent chek2 : secondroot)
				{
					
				
		     	}
				
			}
		}
		
		
		
		for(AbstractCombinedComponent ab:firstroot)
		{
			
		
		
		}
		
		
		
		
		
		
	}

	public boolean isIsfrozen() {
		return isfrozen;
	
	}

	public void freeze() {
		isfrozen=true;
	}
	
	public void unfreeze()
	{
	 isfrozen=false;

	}
}