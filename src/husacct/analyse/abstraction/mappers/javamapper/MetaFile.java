package husacct.analyse.abstraction.mappers.javamapper;

public class MetaFile {
	private String path;
	private int lineNumber;
	public MetaFile(String metaFile, int lineNumber) {
		this.path = metaFile;
		this.lineNumber = lineNumber;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
