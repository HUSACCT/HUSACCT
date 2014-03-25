package husacct.define.task.components;

import java.util.ArrayList;

public class AnalyzedModuleComponent extends AbstractCombinedComponent {

    private static final long serialVersionUID = 1713515026043620607L;

    private boolean attached = true;
    private boolean isfrozen = false;
    private boolean isRemoved = false;
    private String visibility;

    public AnalyzedModuleComponent() {
    	super();
    }

    public AnalyzedModuleComponent(String uniqueName, String name, String type, String visibility) {
		this();
		setUniqueName(uniqueName);
		setName(name);
		setType(type);
		setVisibility(visibility);
		if (uniqueName.equals("root")) {
		    setParentOfChild(this);
		}
    }

    @Override
    public void addChild(AbstractCombinedComponent child) {
	if (attached) {
	    child.setParentOfChild(this);
	    child.setAnalyzedModuleComponentPosition(children.size());
	}
	children.add(child);
    }

    @Override
    public void addChild(int index, AbstractCombinedComponent child) {
	if (attached) {
	    child.setParentOfChild(this);
	    child.setAnalyzedModuleComponentPosition(index);
	}
	children.add(index, child);
    }

    public void attach() {
    	attached = true;
    }

    public void detach() {
    	attached = false;
    }

    public void freeze() {
    	isfrozen = true;
    }

    @Override
    public ArrayList<AbstractCombinedComponent> getChildren() {
    	return children;
    }

    public String getVisibility() {
    	return visibility;
    }

    public boolean isIsfrozen() {
    	return isfrozen;
    }

    public boolean isMapped() {
    	return isfrozen;
    }

    public boolean isRemoved() {
    	return isRemoved;
    }

    public void registerchildrenSize() {
    	sizeOfChildren = getChildren().size();
    }

    @Override
    public void removeChild(AbstractCombinedComponent child) {
		for (AbstractCombinedComponent currentchild : children) {
		    if (currentchild.equals(child)) {
		    	children.remove(currentchild);
		    }
		}
    }

    public void removeChildFromParent() {
    	isRemoved = true;
    }

    @Override
    public void setChildren(ArrayList<AbstractCombinedComponent> children) {
		if (attached) {
		    for (AbstractCombinedComponent a : children) {
		    	a.setParentOfChild(this);
		    }
		}
		this.children = children;
    }

    public void setVisibility(String visibility) {
    	this.visibility = visibility;
    }

    public void unfreeze() {
    	isfrozen = false;
    }

	public boolean isAncestorsMapped() {
		boolean result = false;
		AnalyzedModuleComponent buffer =null;
		String type = this.getType().toLowerCase();
		buffer = (AnalyzedModuleComponent) this.getParentofChild();
		while (!type.equals("application")) {
			if (buffer.isMapped()) {
				result =true;
			    break;
			}
			else{	
				type=buffer.getParentofChild().getType().toLowerCase();
				buffer=buffer.getParentofChild();
			}
		}
		return result;
	}
}