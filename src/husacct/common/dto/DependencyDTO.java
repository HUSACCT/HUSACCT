package husacct.common.dto;

public class DependencyDTO extends AbstractDTO{
	
	public String from;
	public String to;
	public String via;
	public String type;
	public int lineNumber;
	public boolean isIndirect;
	
	public DependencyDTO(String from, String to, String type, int lineNumber){
		this.from = from;
		this.to = to;
		this.type = type;
		this.lineNumber = lineNumber;
		this.isIndirect = false;
	}
	
	public DependencyDTO(String from, String to, String type, boolean indirect, int lineNumber){
		this.from = from;
		this.to = to;
		this.type = type;
		this.lineNumber = lineNumber;
		this.isIndirect = indirect;
	}
	
	public boolean equals(DependencyDTO other){
		boolean result = true;
		result = result && (this.from == other.from);
		result = result && (this.to == other.to);
		result = result && (this.type == other.type);
		result = result && (this.lineNumber == other.lineNumber);
		result = result && (this.isIndirect == other.isIndirect);
		return result;
	}
	
	public String toString(){
		String result = "";
		result += "From: " + from + ", ";
		result += "To: " + to + ", ";
		result += "Type: " + type + " ,";
		result += "Line: " + lineNumber + ".";
		result += "Indirect : " + isIndirect + ".";
		return result;
	}
}
