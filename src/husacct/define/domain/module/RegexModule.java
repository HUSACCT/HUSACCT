package husacct.define.domain.module;

public class RegexModule extends Module {
    public RegexModule() {
	super();
	super.type = "Regex";
    }

    public RegexModule(String name, String description) {
	super(name, description);
	super.type = "Facade";
    }
}
