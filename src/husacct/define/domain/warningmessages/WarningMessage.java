package husacct.define.domain.warningmessages;

import java.util.Observer;

public abstract class WarningMessage implements Observer {
	protected String description = "";
	protected String resource = "";
	protected String location = "";
	protected String type = "";

	public abstract void generateMessage();
public abstract Object[] getValue(); 
	
	public String getDescription() {

		return description;
	}

	public String getResource() {

		return resource;
	}

	public String getLocation() {

		return location;
	}

	public String getType() {

		return type;
	}
	
	
}