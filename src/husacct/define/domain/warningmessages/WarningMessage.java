package husacct.define.domain.warningmessages;

public abstract class WarningMessage {
    protected String description = "";
    protected String location = "";
    protected String resource = "";
    protected String type = "";

    public abstract void generateMessage();

    public String getDescription() {
	return description;
    }

    public String getLocation() {
	return location;
    }

    public String getResource() {
	return resource;
    }

    public String getType() {
	return type;
    }
}