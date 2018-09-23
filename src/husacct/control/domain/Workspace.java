package husacct.control.domain;

import husacct.common.dto.ApplicationDTO;

import java.util.Objects;

public class Workspace {
	
	private String name;
	private ApplicationDTO appData;
	private boolean dirty;

	public String getName() {
		return name;
	}
	public void setName(String name) {
    dirty |= !Objects.equals(this.name, name);
		this.name = name;
	}
	public ApplicationDTO getApplicationData() {
		return appData;
	}
	public void setApplicationData(ApplicationDTO appData) {
    dirty |= !Objects.deepEquals(this.appData, appData);
		this.appData = appData;
	}

	public boolean isDirty() {
		return dirty;
	}

	@Override
	public String toString() {
    String representation = "";
    representation += "\nName: " + name;
    representation += "\n";
    return representation;
	}
}
