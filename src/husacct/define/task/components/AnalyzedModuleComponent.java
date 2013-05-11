package husacct.define.task.components;

import java.util.ArrayList;

public class AnalyzedModuleComponent extends AbstractCombinedComponent {

	private static final long serialVersionUID = 1713515026043620607L;
	
	
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
		if(!isfrozen){
		child.setParentOfChild(this);
		child.setAnalyzedModuleComponentPosition(index);
		}
		this.children.add(index, child);
	}

	public void setChildren(ArrayList<AbstractCombinedComponent> children) {
		if(!isfrozen){
		for(AbstractCombinedComponent a: children)
		{
			
			a.setParentOfChild(this);
		}
		
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

	
	public String getVisibility() {
		return this.visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
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

	public void registerchildrenSize() {
		this.sizeOfChildren=this.getChildren().size();
		
	}
	
	public boolean isComplete()
	{
		if(this.type.toLowerCase().equals("package")||this.type.toLowerCase().equals("externallibrary"))
		{
			if(this.children.size()<sizeOfChildren)
			{
				return false;
			}
			else{
				return true;
			}
			
		}else{
				return true;
		}
		
		
	
	}

	public boolean isMapped() {
		
		return isfrozen;
	}



	
}