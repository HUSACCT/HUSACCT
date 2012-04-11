package husacct.analyse.abstraction.mappers.javamapper;

import husacct.analyse.abstraction.mappers.codemapper.GenericMapper;
import husacct.analyse.domain.famix.FamixObject;
import husacct.analyse.infrastructure.antlr.JavaParser;

import java.util.ArrayList;
import java.util.List;

public class JavaMapper implements GenericMapper{
	public static String programmingLanguage = "Java";
	public List<MetaFile> pathsOfFiles;
	
	public List<FamixObject> analyseApplication(String workspacePath) {
		List<FamixObject> famixObject = new ArrayList<FamixObject>();
		WorkspaceAnalyser workspaceAnalyser = new WorkspaceAnalyser();
		JavaTreeParserBuilder javaTreeParserBuilder = new JavaTreeParserBuilder();
		JavaTreeParserDelegater javaTreeParserDelegater = new JavaTreeParserDelegater();
		try {
			pathsOfFiles = workspaceAnalyser.getFilePathsFromWorkspace(workspacePath);
			for (MetaFile file : pathsOfFiles) {
				JavaParser javaParser = javaTreeParserBuilder.buildTreeParser(file.getPath());
				famixObject = javaTreeParserDelegater.delegateFamixObjectGenerators(javaParser);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return famixObject;
	}	
}		

