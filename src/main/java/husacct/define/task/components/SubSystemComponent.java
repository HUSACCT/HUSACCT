package husacct.define.task.components;

import java.util.ArrayList;

public class SubSystemComponent extends AbstractDefineComponent {

    private static final long serialVersionUID = 6711345066811693165L;

    public SubSystemComponent() {
	super();
    }

    @Override
    public void addChild(AbstractCombinedComponent child) {
	children.add(child);
    }

    @Override
    public void addChild(int index, AbstractCombinedComponent child) {
	children.add(index, child);
    }

    @Override
    public ArrayList<AbstractCombinedComponent> getChildren() {
	return children;
    }

    @Override
    public long getModuleId() {
	return moduleId;
    }

    @Override
    public void removeChild(AbstractCombinedComponent child) {
	for (AbstractCombinedComponent currentchild : children) {
	    if (currentchild.equals(child)) {
		children.remove(currentchild);
	    }
	}
    }

    @Override
    public void setChildren(ArrayList<AbstractCombinedComponent> children) {
	this.children = children;
    }

    @Override
    public void setModuleId(long moduleId) {
	this.moduleId = moduleId;
    }
}
