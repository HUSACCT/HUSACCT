package husacct.control.task.help;

import java.io.File;

public class HelpTreeNode {
	String filename;
	String viewname;
	String componentname;
	String parent;
	String filetype;
	
	public HelpTreeNode(String file, String viewname, String componentname, String filetype) {
		setFile(file);
		//this.filename = file;
		this.viewname = viewname;
		this.componentname = componentname;
		this.filetype = filetype;
	}
	
	public void setFile(String file) {
		if(file.indexOf('/') > -1 ) {
			this.parent = file.substring(0, file.indexOf('/'));
			this.filename = file.substring(file.indexOf('/')+1);
		}
		else {
			this.filename = file;
		}
		
	}
	
	public String getParent() {
		return parent;
	}
	
	public String getFilename() {
		if(parent == null) {
			return filename;
		}
		else {
			return parent +"/" + filename;
		}
	}
	
	public String getViewName() {
		return viewname;
	}
	
	public String getComponentName() {
		return componentname;
	}
	
	public String getFileType() {
		return filetype;
	}
	@Override
	public String toString() {
		return this.viewname;
	}
}
