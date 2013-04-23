package husacct.define.task.components;

import java.util.ArrayList;

public class RegexComponent  extends AnalyzedModuleComponent{

	private String uniqueName = "";
	private String type = "";
	private String visibility;
	private AnalyzedModuleComponent wrapper;
	private ArrayList<String> removedModules = new ArrayList<>();

	private String regix ="";
	private ArrayList<AbstractCombinedComponent> softwareUnit = new ArrayList<AbstractCombinedComponent>();
	
	public RegexComponent() {
		super();
	}
	public RegexComponent(String uniqueName, String name, String type, String visibility)
	{
		this();
		this.setUniqueName(uniqueName);
		this.setName(name);
		this.setType(type);
		this.setVisibility(visibility);
	}
	
	

	@Override
	public void addChild(AbstractCombinedComponent child) {
		
		softwareUnit.add((AnalyzedModuleComponent)child);
	}

	

	@Override
	public void setChildren(ArrayList<AbstractCombinedComponent> children) {
		
	}

	@Override
	public ArrayList<AbstractCombinedComponent> getChildren() {
		return softwareUnit;
	}

	@Override
	public void removeChild(AbstractCombinedComponent child) {
		
	 int index=	softwareUnit.indexOf(child);
	 if(index>=0)
	 {
		 softwareUnit.remove(index);
	 }
	 
	 
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type.toUpperCase();
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	public AnalyzedModuleComponent getWrapper() {
		return wrapper;
	}
	public void setWrapper(AnalyzedModuleComponent wrapper) {
		this.setUniqueName(wrapper.getUniqueName());
		this.setName(wrapper.getName());
		this.setType(wrapper.type);
		this.setChildren(wrapper.getChildren());
		this.wrapper = wrapper;
	}

}
