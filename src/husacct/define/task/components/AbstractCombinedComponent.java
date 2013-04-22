package husacct.define.task.components;

import java.awt.Component;
import java.util.ArrayList;

public abstract class AbstractCombinedComponent extends Component implements Comparable<AbstractCombinedComponent>{
	protected String uniqueName = "";
	protected String type = "";
	private static final long serialVersionUID = 1L;
	protected ArrayList<AbstractCombinedComponent> children;
	private AnalyzedModuleComponent parent;
	private int analyzedModuleComponentPosition=0;

	public AbstractCombinedComponent() {
		super();
		this.children = new ArrayList<AbstractCombinedComponent>();
	}
	
	
	public abstract void addChild(AbstractCombinedComponent child);
	public abstract void addChild(int index, AbstractCombinedComponent child);
	public abstract void setChildren(ArrayList<AbstractCombinedComponent> children);
	public abstract ArrayList<AbstractCombinedComponent> getChildren();
	public abstract void removeChild(AbstractCombinedComponent child);
	
	
	public AnalyzedModuleComponent getParentofChild()
	{
		
		return this.parent;
	}
	
	public void setParentOfChild(AbstractCombinedComponent parentofchild)
	{
		 
		parent=(AnalyzedModuleComponent) parentofchild;
	}

	public int getAnalyzedModuleComponentPosition() {
		return analyzedModuleComponentPosition;
	}

	public void setAnalyzedModuleComponentPosition(
			int analyzedModuleComponentPosition) {
		this.analyzedModuleComponentPosition = analyzedModuleComponentPosition;
	}
	
	@Override
	public int compareTo(AbstractCombinedComponent left) {
		
		return this.getUniqueName().toUpperCase().compareTo(left.getUniqueName().toUpperCase());
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

}
