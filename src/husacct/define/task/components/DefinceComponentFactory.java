package husacct.define.task.components;

public class DefinceComponentFactory {
	
	public static AbstractDefineComponent getDefineComponent(String type) {
		if(type == "layer") {
			return new LayerComponent();
		} else if(type == "softwareArchitecture") {
			return new SoftwareArchitectureComponent();
		} else {
			// #TODO:: throw Exception?
			return null;
		}
	}
}
