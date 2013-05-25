package husacct.define.domain.module;

public class Facade extends Module {

    public Facade() {
	this("", "");
	super.type = "Facade";
    }

    public Facade(String name, String description) {
	super(name, description);
	super.type = "Facade";
    }
}
