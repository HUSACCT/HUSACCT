package husacct.define.task.components;

import java.util.ArrayList;

public class SoftwareArchitectureComponent extends AbstractDefineComponent {

	private static final long serialVersionUID = -7967808118061022966L;
	
	public SoftwareArchitectureComponent() {
		super();
		this.setName("SofwareArchitecture");
		this.setModuleId(-1);
	}

	@Override
	public void addChild(AbstractCombinedComponent child) {
		this.children.add(child);
	}
	
	@Override
	public void addChild(int index, AbstractCombinedComponent child) {
		this.children.add(index, child);
	}

	@Override
	public void setChildren(ArrayList<AbstractCombinedComponent> children) {
		this.children = children;
	}

	@Override
	public ArrayList<AbstractCombinedComponent> getChildren() {
		return this.children;
	}

	@Override
	public void removeChild(AbstractCombinedComponent child) {
		for(AbstractCombinedComponent currentchild : this.children) {
			if(currentchild.equals(child)) {
				this.children.remove(currentchild);
			}
		}
	}
	
	@Override
	public void setModuleId(long moduleId) {
		this.moduleId = moduleId;
	}

	@Override
	public long getModuleId() {
		return this.moduleId;
	}
}
