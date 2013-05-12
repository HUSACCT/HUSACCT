package husacct.define.domain.warningmessages;

import husacct.define.task.components.AnalyzedModuleComponent;
import husaccttest.control.ObservableServiceTest;

public abstract class WarningMessage {
protected String description="";
protected String resource="";
protected String location="";
protected String type="";


public abstract void generateMessage();


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