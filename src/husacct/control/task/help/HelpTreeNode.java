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
	
	public String getViewName() {
		return viewname;
	}
	
	public String getComponentName() {
		return componentname;
	}
	@Override
	public String toString() {
		return this.viewname;
	}
}
