package husacct.common.dto;

public class PhysicalPathDTO extends AbstractDTO {
	public final String path;
	public final String type;
	
	public PhysicalPathDTO(String path, String type) {
		super();
		this.path = path;
		this.type = type;
	}
	
	@Override
	public String toString(){
		String result = "";
		result += "Path: " + path + "\n";
		result += "Type: " + type + "\n";
		return result;
	}
}
