package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.Java7Parser;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.CompilationUnitContext;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.log4j.Logger;

class JavaTreeConvertController {

	private CompilationUnitContext compilationUnit;
	private ParseTreeWalker walker;
	private String sourceFilePath = "";
    private int numberOfLinesOfCode = 0;
    private String theClass = null;
    private String thePackage = null;
    private String currentClass = null;
    private String parentClass = null;
    private int classCount = 0;
    private Logger logger = Logger.getLogger(JavaTreeConvertController.class);
    private PackageAnalyser packageAnalyser;
    private TypeDeclarationAnalyser typeDeclarationAnalyser;
    private JavaMethodGeneratorController methodGenerator;
    private JavaAttributeAndLocalVariableGenerator javaAttributeGenerator;

    public JavaTreeConvertController(CompilationUnitContext compilationUnit) {
    	this.compilationUnit = compilationUnit;
        methodGenerator = new JavaMethodGeneratorController();
        javaAttributeGenerator = new JavaAttributeAndLocalVariableGenerator();
    }

    public void delegateASTToGenerators(String sourceFilePath, int nrOfLinesOfCode, Java7Parser java7Parser) throws RecognitionException {
    	this.sourceFilePath = sourceFilePath;
    	this.numberOfLinesOfCode = nrOfLinesOfCode;
    	try {
    		// Test and Debug
    		if (sourceFilePath.contains("AnnotationDependency")) {
    		} //
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
        packageAnalyser = new PackageAnalyser();
        walker = new ParseTreeWalker();
        walker.walk(packageAnalyser, compilationUnit);
        this.thePackage = packageAnalyser.getPackage();
    }
    
    private void analyseTypeDeclaration() {
    	typeDeclarationAnalyser = new TypeDeclarationAnalyser(thePackage, sourceFilePath, numberOfLinesOfCode, false, "");
        walker.walk(typeDeclarationAnalyser, compilationUnit);
        this.theClass = typeDeclarationAnalyser.getUniqueNameOfType();
    }
    
    private void analyseImports() {
    	ImportAnalyser importAnalyser = new ImportAnalyser(this.theClass);
        walker.walk(importAnalyser, compilationUnit);
    }
    
    private void delegateASTToGenerators(CommonTree tree) {
        if (tree != null) {
        	CommonTree childNode;
            for (int i = 0; i < tree.getChildCount(); i++) {
                childNode = (CommonTree) tree.getChild(i);
                int nodeType = childNode.getType();
                switch (nodeType) {
                    case Java7Parser.CLASS: case Java7Parser.ENUM: case Java7Parser.INTERFACE:	
                        if (classCount > 0) {
                            CommonTree innerClassTree = childNode;
                            this.parentClass = currentClass;
                            if (nodeType == Java7Parser.INTERFACE) {
                            	this.currentClass = delegateClass(innerClassTree, true, true, false);
                            } else if (nodeType == Java7Parser.ENUM){
                            	this.currentClass = delegateClass(innerClassTree, true, false, true);
                            } else {
                            	this.currentClass = delegateClass(innerClassTree, true, false, false);
                            }
                            delegateASTToGenerators(innerClassTree);
                            this.currentClass = parentClass;
                        } else {
                            classCount++;
                            delegateASTToGenerators(childNode);
                        }
                        break;
                    case Java7Parser.IMPORT:
                        //delegateImport(childNode);
                        break;
                    case Java7Parser.AT:
                        delegateAnnotation(childNode);
                        break;
                    /*
                   case Java7Parser.VAR_DECLARATION:
                        delegateAttribute(childNode);
                        break;
                    case Java7Parser.FUNCTION_METHOD_DECL:
                    case Java7Parser.CONSTRUCTOR_DECL:
                    case Java7Parser.VOID_METHOD_DECL:
                        delegateMethod(childNode);
                        break;
                    */
    	            default: 
                        delegateASTToGenerators(childNode);
                        break;
                }
            }
        }
    }

    private String delegateClass(CommonTree classTree, boolean isInnerClass, boolean isInterface, boolean isEnumeration) {
        String analysedClass = null;
        if (isInnerClass) {
            //analysedClass = javaClassGenerator.generateToDomain(sourceFilePath, numberOfLinesOfCode, classTree, true, parentClass, isInterface, isEnumeration);
        } else {
            //analysedClass = javaClassGenerator.generateToDomain(sourceFilePath, numberOfLinesOfCode, classTree, false, "", isInterface, isEnumeration);
        }
        return analysedClass;
    }

    private void delegateAnnotation(CommonTree annotationTree) {
        //javaAnnotationGenerator.generateToDomain(annotationTree, this.currentClass, "class");
    }

    private void delegateAttribute(CommonTree attributeTree) {
        javaAttributeGenerator.generateAttributeToDomain(attributeTree, this.currentClass);
    }

    private void delegateMethod(CommonTree methodTree) {
        methodGenerator.delegateMethodBlock(methodTree, this.currentClass);
    }
}