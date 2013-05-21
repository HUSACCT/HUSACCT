package husacct.control.task.help;

public class HelpTreeNode {
	String filename;
	String viewname;
	String componentname;
	
	public HelpTreeNode(String file, String viewname, String componentname) {
		this.filename = file;
		this.viewname = viewname;
		this.componentname = componentname;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String getviewname() {
		return viewname;
	}
	
	public String getcomponentname() {
		return componentname;
	}
	@Override
	public String toString() {
		return this.viewname;
	}
}
