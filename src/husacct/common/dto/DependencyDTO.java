package husacct.common.dto;

public class DependencyDTO extends AbstractDTO{
	
	public String from;
	public String to;
	public String type;
	public int lineNumber;
	
	public DependencyDTO(String from, String to, String type, int lineNumber){
		this.from = from;
		this.to = to;
		this.type = type;
		this.lineNumber = lineNumber;
	}
	
	public boolean equals(DependencyDTO other){
		boolean result = true;
		result = result && (this.from == other.from);
		result = result && (this.to == other.to);
		result = result && (this.type == other.type);
		result = result && (this.lineNumber == other.lineNumber);
		return result;
	}
}
