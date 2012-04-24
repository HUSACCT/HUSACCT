package husacct.analyse.task.analyser.csharp;

import husacct.analyse.task.analyser.AbstractAnalyser;

import java.util.ArrayList;
import java.util.List;

public class CSharpAnalyser extends AbstractAnalyser{
	
	public void generateModelFromSource(String sourceFilePath) {
		//TODO Implement CSharp Logic
	}

	@Override
	public String getProgrammingLanguage(){
		return "C#";
	}
	
	@Override
	public String getFileExtension() {
		return ".cs";
	}	
}		

