package husacct.control.domain;

import java.util.Date;

public class Workspace {
	private boolean saved;
	private Date lastSave;
	private String path;
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Date getLastSave() {
		return lastSave;
	}
	public void setLastSave(Date lastSave) {
		this.lastSave = lastSave;
	}
	public boolean isSaved() {
		return saved;
	}
	public void setSaved(boolean saved) {
		this.saved = saved;
	}
}
