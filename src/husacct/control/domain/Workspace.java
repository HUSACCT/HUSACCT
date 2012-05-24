package husacct.control.domain;

import husacct.common.dto.ApplicationDTO;

public class Workspace {
	
	private String name;
	private ApplicationDTO appData;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ApplicationDTO getApplicationData() {
		return appData;
	}
	public void setApplicationData(ApplicationDTO appData) {
		this.appData = appData;
	}
	
}
