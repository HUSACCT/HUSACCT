package husacct.control.domain;

import husacct.common.dto.ApplicationDTO;

public class Workspace {
	
	private String name = "Workspace";
	private ApplicationDTO appData;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		if (name != null && (name.trim().length() >= 1)) {
			this.name = name;
		}
	}
	public ApplicationDTO getApplicationData() {
		return appData;
	}
	public void setApplicationData(ApplicationDTO appData) {
		this.appData = appData;
	}
	
    @Override
	public String toString() {
        String representation = "";
        representation += "\nName: " + name;
        representation += "\n";
        return representation;
    }
	
	
}
