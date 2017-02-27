package husacct.analyse.task.analyse.java.analysing;

import husacct.analyse.task.analyse.java.parsing.JavaParser;
import husacct.analyse.task.analyse.java.parsing.JavaParser.CompilationUnitContext;

import org.apache.log4j.Logger;

public class CompilationUnitAnalyser {
	
	private static String sourceFilePath = "";
    private static String thePackage = "";
	private CompilationUnitContext compilationUnit;
    private int numberOfLinesOfCode = 0;
    private String theClass = null;
    private Logger logger = Logger.getLogger(CompilationUnitAnalyser.class);

    public static String getPackage() {
    	return thePackage;
    }
    
    public static String getSourceFilePath() {
    	return sourceFilePath;
    }
    
    public CompilationUnitAnalyser(CompilationUnitContext compilationUnit, String sourceFileLocation, int nrOfLinesOfCode, JavaParser java7Parser) {
    	this.compilationUnit = compilationUnit;
    	sourceFilePath = sourceFileLocation;
    	this.numberOfLinesOfCode = nrOfLinesOfCode;
    	try {
    		/* Test and Debug
    		if (sourceFilePath.contains("testcases\\taskdefs\\apt\\AptExample.java")) {
    			int i = 1;
    		} */
    		if (compilationUnit != null) {
	        	analysePackage();
	        	analyseTypeDeclaration();
	        	analyseImports();
    		}
    	}
    	catch (Exception e) {
    		String location;
    		if ((thePackage != null) && (theClass != null)) {
    			location = thePackage + "." + theClass;
    		} else {
    			location = sourceFilePath;
    		}
    		logger.warn(e.getMessage() + " - in file: " + location);
    		//e.printStackTrace();
    	}
    }

    private void analysePackage() {
        PackageAnalyser packageAnalyser;
        packageAnalyser = new PackageAnalyser(compilationUnit.packageDeclaration());
        thePackage = packageAnalyser.getPackageUniqueName();
    }
    
    private void analyseTypeDeclaration() {
    	if (compilationUnit.typeDeclaration() != null) {
	    	int size = compilationUnit.typeDeclaration().size();
	    	for (int i = 0; i < size; i++) {
	    		TypeDeclarationAnalyser typeDeclarationAnalyser = new TypeDeclarationAnalyser();
	        	String className = typeDeclarationAnalyser.analyseTypeDeclaration(compilationUnit.typeDeclaration(i), numberOfLinesOfCode);
	        	if (i == 0) {
	                this.theClass = className;
	        	}
	    	}
    	}
    }
    
    private void analyseImports() {
    	if (compilationUnit.importDeclaration() != null) {
    		new ImportAnalyser(this.theClass, compilationUnit.importDeclaration());
    	}
    }
}