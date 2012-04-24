package husacct.define.task.components;

import java.awt.Component;
import java.util.ArrayList;

public class SoftwareArchitectureComponent extends AbstractDefineComponent {

	private static final long serialVersionUID = -7967808118061022966L;
	
	public SoftwareArchitectureComponent() {
		super();
		this.setName("SofwareArchitecture");
	}

	@Override
	public void addChild(AbstractDefineComponent child) {
		this.children.add(child);
	}
	
	@Override
	public void addChild(int index, AbstractDefineComponent child) {
		this.children.add(index, child);
	}

	@Override
	public void setChildren(ArrayList<AbstractDefineComponent> children) {
		this.children = children;
	}

	@Override
	public ArrayList<AbstractDefineComponent> getChildren() {
		return this.children;
	}

	@Override
	public void removeChild(AbstractDefineComponent child) {
		for(Component currentchild : this.children) {
			if(currentchild.equals(child)) {
				this.children.remove(currentchild);
			}
		}
	}
}
