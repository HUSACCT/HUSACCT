package husacct.control.task.help;

public class HelpTreeNode {
	private String filename;
	private String viewname;
	private String componentname;
	private String parent;
	private String fileType;
	
	public HelpTreeNode(String file, String viewname, String componentname, String filetype) {
		
		this.filename = file;
		setParent();
		this.viewname = viewname;
		this.componentname = componentname;
		this.fileType = filetype;
	}
	
	public String getFilename() {
		if(parent==null) {
			return filename;
		}
		else {
			return parent+filename;
		}
	}
	
	public String getViewName() {
		return viewname;
	}
	
	public String getComponentName() {
		return componentname;
	}
	
	public void setParent() {
		if(filename.indexOf("/") > -1) {
			this.parent = filename.substring(0, filename.indexOf("/"));
			this.filename = filename.substring(filename.indexOf("/"));
		}
		else {
			this.parent = null;
		}
	}
	
	public String getParent() {
		return this.parent;
	}
	
	public String getFileType() {
		return this.fileType;
	}
	
	@Override
	public String toString() {
		return this.viewname;
	}
}
