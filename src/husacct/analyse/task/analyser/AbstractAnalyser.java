package husacct.analyse.task.analyser;

public abstract class AbstractAnalyser {
	
	public void analyseApplication(String sourceFilePath){
		generateModelFromSource(sourceFilePath);
	}
	
	public abstract void generateModelFromSource(String sourceFilePath);
	public abstract String getProgrammingLanguage();
	public abstract String getFileExtension();
}
