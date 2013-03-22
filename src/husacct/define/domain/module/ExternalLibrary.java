package husacct.define.domain.module;

public class ExternalLibrary extends Module {

    public ExternalLibrary() {
        this("", "");
    }

    public ExternalLibrary(String name, String description) {
        super(name, description);
        super.type = "ExternalLibrary";
    }
}
