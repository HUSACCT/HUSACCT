package husacct.analyse.task.analyse.java.analysing;

import husacct.analyse.task.analyse.java.parsing.JavaParser;
import husacct.analyse.task.analyse.java.parsing.JavaParser.CompilationUnitContext;

import org.apache.log4j.Logger;

public class CompilationUnitAnalyser {
	
	private static String sourceFilePath = "";
    private static String thePackage = "";
	private CompilationUnitContext compilationUnit;
    private String theClass = null;
    private Logger logger = Logger.getLogger(CompilationUnitAnalyser.class);

    public static String getPackage() {
    	return thePackage;
    }
    
    public static String getSourceFilePath() {
    	return sourceFilePath;
    }
    
    public CompilationUnitAnalyser(CompilationUnitContext compilationUnit, String sourceFileLocation, JavaParser java7Parser) {
    	this.compilationUnit = compilationUnit;
    	sourceFilePath = sourceFileLocation;
    	try {
    		/* Test and Debug
    		if (sourceFilePath.contains("RESTService")) {
    			int i = 1;
    		} */
    		if (compilationUnit != null) {
	        	analysePackage();
	        	analyseTypeDeclaration();
	        	// analyseImports() is invoked per TypeDeclaration within analyseTypeDeclaration()
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
    		int numberOfLinesOfCode = 0;
    		int previousStopLine = 0;
	    	for (int i = 0; i < size; i++) {
	    		if (i == 0) {
	    			numberOfLinesOfCode = compilationUnit.typeDeclaration(i).getStop().getLine();
	    		} else {
	    			int stopLine = compilationUnit.typeDeclaration(i).getStop().getLine();
	    			numberOfLinesOfCode = stopLine - previousStopLine;
	    		}
	    		TypeDeclarationAnalyser typeDeclarationAnalyser = new TypeDeclarationAnalyser();
	        	String className = typeDeclarationAnalyser.analyseTypeDeclaration(compilationUnit.typeDeclaration(i), numberOfLinesOfCode);
	        	this.theClass = className;
	        	analyseImports();
    			previousStopLine = numberOfLinesOfCode;
	    	}
    	}
    }
    
    private void analyseImports() {
    	if (compilationUnit.importDeclaration() != null) {
    		new ImportAnalyser(this.theClass, compilationUnit.importDeclaration());
    	}
    }
}