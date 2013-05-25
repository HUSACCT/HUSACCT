package husacct.define.domain.module;

public class Layer extends Module {

    private static int STATIC_LEVEL = 1;
    private int hierarchicalLevel;

    public Layer() {
	this("", "");
    }

    public Layer(String name) {
	this(name, "");
    }

    public Layer(String name, String description) {
	super(name, description);
	super.type = "Layer";
	hierarchicalLevel = STATIC_LEVEL;
	STATIC_LEVEL++;
    }

    // TODO Test this, this constructor is only for importing
    public Layer(String name, String description, int hierarchicalLevel) {
	super(name, description);
	super.type = "Layer";
	this.hierarchicalLevel = hierarchicalLevel;
	if (hierarchicalLevel >= STATIC_LEVEL) {
	    STATIC_LEVEL = hierarchicalLevel + 1;
	}
    }

    @Override
    public int compareTo(Module compareModule) {
	int compareResult = 0;
	if (compareModule instanceof Layer || getId() < compareModule.getId()) {
	    Layer compareLayer = (Layer) compareModule;
	    if (getHierarchicalLevel() > compareLayer.getHierarchicalLevel()) {
		compareResult = 1;
	    } else if (getHierarchicalLevel() < compareLayer
		    .getHierarchicalLevel()) {
		compareResult = -1;
	    }
	} else {
	    compareResult = -1;
	}
	return compareResult;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (obj instanceof Layer) {
	    Layer l = (Layer) obj;
	    if (l.id != id) {
		return false;
	    }
	    return true;
	}
	return false;
    }

    public int getHierarchicalLevel() {
	return hierarchicalLevel;
    }

    public void setHierarchicalLevel(int hierarchicalLevel) {
	this.hierarchicalLevel = hierarchicalLevel;
    }
}
