package husacct.define.domain.module;

public class Layer extends Module {
	
	private int hierarchicalLevel;
	
	public Layer()
	{
		this("", "", -1);
	}

	public Layer(String name, int level)
	{
		this(name, "", level);
	}
	
	public Layer(String name, String description, int level)
	{
		super(name, description);
		super.type = "Layer";
		this.hierarchicalLevel = level;
	}
	
	public void setHierarchicalLevel(int hierarchicalLevel) {
		this.hierarchicalLevel = hierarchicalLevel;
	}

	public int getHierarchicalLevel() {
		return hierarchicalLevel;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
	        return true;
	    if (obj == null)
	        return false;
	    if (obj instanceof Layer){
	    	Layer l = (Layer)obj;
	    	if (l.id != this.id){
	    		return false;
	    	}
	    	return true;
	    }
	    return false;
	}
	
	@Override
	public int compareTo(Module compareModule) {
		int compareResult = 0;
		if(compareModule instanceof Layer || this.getId() < compareModule.getId()) {
			Layer compareLayer = (Layer) compareModule;
			if(this.getHierarchicalLevel() > compareLayer.getHierarchicalLevel()) {
				compareResult = 1;
			} else if(this.getHierarchicalLevel() < compareLayer.getHierarchicalLevel()) {
				compareResult = -1;
			}
		} else {
			compareResult = -1;
		}
		return compareResult;
	}
}
