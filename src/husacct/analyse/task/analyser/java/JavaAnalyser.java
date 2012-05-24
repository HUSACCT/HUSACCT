package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;
import husacct.analyse.task.analyser.AbstractAnalyser;

public class JavaAnalyser extends AbstractAnalyser{
	
	public void generateModelFromSource(String sourceFilePath) {
		JavaTreeParserBuilder javaTreeParserBuilder = new JavaTreeParserBuilder();
		JavaTreeConvertController javaTreeParserDelegater = new JavaTreeConvertController();
		JavaParser javaParser;
		try {
			javaParser = javaTreeParserBuilder.buildTreeParser(sourceFilePath);
			javaTreeParserDelegater.delegateModelGenerators(javaParser);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String getProgrammingLanguage(){
		return "Java";
	}

	@Override
	public String getFileExtension() {
		return ".java";
	}	
}		

