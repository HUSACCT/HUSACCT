package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.Java7Parser;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.CompilationUnitContext;
import org.antlr.runtime.RecognitionException;
import org.apache.log4j.Logger;

class JavaTreeConvertController {

	private CompilationUnitContext compilationUnit;
	private String sourceFilePath = "";
    private int numberOfLinesOfCode = 0;
    private String theClass = null;
    private String thePackage = null;
    private Logger logger = Logger.getLogger(JavaTreeConvertController.class);
    private TypeDeclarationAnalyser typeDeclarationAnalyser;

    public JavaTreeConvertController(CompilationUnitContext compilationUnit) {

    	this.compilationUnit = compilationUnit;
    }

    public void delegateASTToGenerators(String sourceFilePath, int nrOfLinesOfCode, Java7Parser java7Parser) throws RecognitionException {
    	this.sourceFilePath = sourceFilePath;
    	this.numberOfLinesOfCode = nrOfLinesOfCode;
    	try {
    		/* Test and Debug
    		if (sourceFilePath.contains("AnnotationDependency")) {
    		} */
        	analysePackage();
        	analyseTypeDeclaration();
        	analyseImports();
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
        this.thePackage = packageAnalyser.getPackageUniqueName();
    }
    
    private void analyseTypeDeclaration() {
    	int size = compilationUnit.typeDeclaration().size();
    	for (int i = 0; i < size; i++) {
        	typeDeclarationAnalyser = new TypeDeclarationAnalyser(thePackage, sourceFilePath, numberOfLinesOfCode, false, "");
        	typeDeclarationAnalyser.analyseTypeDeclaration(compilationUnit.typeDeclaration(i));
        	if (i == 0) {
                this.theClass = typeDeclarationAnalyser.getUniqueNameOfType();
        	}
    	}
    }
    
    private void analyseImports() {
    	new ImportAnalyser(this.theClass, compilationUnit.importDeclaration());
    }
}