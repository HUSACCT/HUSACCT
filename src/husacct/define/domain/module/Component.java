package husacct.define.domain.module;

public class Component extends Module {
	
	public Component(){
		super();
		super.type = "Component";
	}
	
	public Component(String name, String description){
		super(name, description);
		super.type = "Component";
	}

}
