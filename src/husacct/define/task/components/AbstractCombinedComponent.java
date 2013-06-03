package husacct.define.task.components;

import java.awt.Component;
import java.util.ArrayList;

public abstract class AbstractCombinedComponent extends Component implements
	Comparable<AbstractCombinedComponent> {
    private static final long serialVersionUID = 1L;
    private int analyzedModuleComponentPosition = 0;
    protected ArrayList<AbstractCombinedComponent> children;
    private AnalyzedModuleComponent parent;
    protected int sizeOfChildren;
    protected String type = "";
    protected String uniqueName = "";

    public AbstractCombinedComponent() {
	super();
	children = new ArrayList<AbstractCombinedComponent>();
    }

    public abstract void addChild(AbstractCombinedComponent child);

    public abstract void addChild(int index, AbstractCombinedComponent child);

    @Override
    public int compareTo(AbstractCombinedComponent left) {

	if (left.getType().toLowerCase().equals("externalpackage")) {
	    return -1;
	} else if (getType().toLowerCase().equals("externalpackage")) {
	    return 1;
	}

	else {

	    return getUniqueName().toUpperCase().compareTo(
		    left.getUniqueName().toUpperCase());
	}
    }

    public int getAnalyzedModuleComponentPosition() {
	return analyzedModuleComponentPosition;
    }

    public abstract ArrayList<AbstractCombinedComponent> getChildren();

    public AnalyzedModuleComponent getParentofChild() {

	return parent;
    }

    public String getType() {
	return type;
    }

    public String getUniqueName() {
	return uniqueName;
    }

    public abstract void removeChild(AbstractCombinedComponent child);

    public void setAnalyzedModuleComponentPosition(
	    int analyzedModuleComponentPosition) {
	this.analyzedModuleComponentPosition = analyzedModuleComponentPosition;
    }

    public abstract void setChildren(
	    ArrayList<AbstractCombinedComponent> children);

    public void setParentOfChild(AbstractCombinedComponent parentofchild) {

	parent = (AnalyzedModuleComponent) parentofchild;
    }

    public void setType(String type) {
	this.type = type.toUpperCase();
    }

    public void setUniqueName(String uniqueName) {
	this.uniqueName = uniqueName;
    }

    public void updateChilderenPosition() {
	for (int i = 0; i < children.size(); i++) {
	    int newPosition = i;
	    children.get(i).setAnalyzedModuleComponentPosition(newPosition);

	}

    }

}
