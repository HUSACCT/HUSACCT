package husacct.define.task.components;

import java.awt.Component;
import java.util.ArrayList;

public class AnalyzedModuleComponent extends Component{

	private static final long serialVersionUID = 1713515026043620607L;
	
	ArrayList<AnalyzedModuleComponent> children;
	
	private String uniqueName = "";
	private String type = "";
	private String visibility;
	
	public AnalyzedModuleComponent() {
		this.children = new ArrayList<AnalyzedModuleComponent>();
	}
	
	public AnalyzedModuleComponent(String uniqueName, String name, String type, String visibility) {
		this();
		this.setUniqueName(uniqueName);
		this.setName(name);
		this.setType(type);
		this.setVisibility(visibility);
	}

	public void addChild(AnalyzedModuleComponent child) {
		this.children.add(child);
	}
	
	public void addChild(int index, AnalyzedModuleComponent child) {
		this.children.add(index, child);
	}

	public void setChildren(ArrayList<AnalyzedModuleComponent> children) {
		this.children = children;
	}

	public ArrayList<AnalyzedModuleComponent> getChildren() {
		return this.children;
	}

	public void removeChild(AnalyzedModuleComponent child) {
		for(Component currentchild : this.children) {
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
}
