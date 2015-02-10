package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;
import husacct.analyse.infrastructure.antlr.java.JavaParser.compilationUnit_return;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.log4j.Logger;

class JavaTreeConvertController {
	private String sourceFilePath = "";
    private int numberOfLinesOfCode = 0;
    private String theClass = null;
    private String thePackage = null;
    private String currentClass = null;
    private String parentClass = null;
    private int classCount = 0;
    private Logger logger = Logger.getLogger(JavaTreeConvertController.class);
    private JavaPackageGenerator javaPackageGenerator;
    private JavaClassGenerator javaClassGenerator;
    private JavaAnnotationGenerator javaAnnotationGenerator;
    private JavaInheritanceDefinitionGenerator javaInheritanceDefinitionGenerator;
    private JavaImplementsDefinitionGenerator implementsGenerator;
    private JavaImportGenerator javaImportGenerator;
    private JavaMethodGeneratorController methodGenerator;
    private JavaAttributeAndLocalVariableGenerator javaAttributeGenerator;

    public JavaTreeConvertController() {
        javaPackageGenerator = new JavaPackageGenerator();
        methodGenerator = new JavaMethodGeneratorController();
        javaAttributeGenerator = new JavaAttributeAndLocalVariableGenerator();
        javaImportGenerator = new JavaImportGenerator();
        javaInheritanceDefinitionGenerator = new JavaInheritanceDefinitionGenerator();
        implementsGenerator = new JavaImplementsDefinitionGenerator();
        javaAnnotationGenerator = new JavaAnnotationGenerator();
    }

    public void delegateASTToGenerators(String sourceFilePath, int nrOfLinesOfCode, JavaParser javaParser) throws RecognitionException {
    	compilationUnit_return compilationUnit;
    	CommonTree compilationUnitTree;
    	try {
    		this.sourceFilePath = sourceFilePath;
        	this.numberOfLinesOfCode = nrOfLinesOfCode;
	        compilationUnit = javaParser.compilationUnit();
	        if (compilationUnit != null) {
	        	compilationUnitTree = (CommonTree) compilationUnit.getTree();
	        	createClassInformation(compilationUnitTree);
		        if ((this.theClass != null) && !this.theClass.equals("")) {
		        	delegateASTToGenerators(compilationUnitTree);
		        }
	        } 
    	}
    	catch (Exception e) {
    		logger.error("Exception: "+ e);
    		//e.printStackTrace();
    	}
    }

    private void createClassInformation(CommonTree completeTree) {
        CommonTree packageTree = (CommonTree) completeTree.getFirstChildWithType(JavaParser.PACKAGE);
        if (hasPackageElement(completeTree)) {
            this.thePackage = javaPackageGenerator.generateModel(packageTree);
        } else {
    		this.thePackage = javaPackageGenerator.generateNoPackage_Package();
        }
        javaClassGenerator = new JavaClassGenerator(thePackage);

        CommonTree classTree = (CommonTree) completeTree.getFirstChildWithType(JavaParser.CLASS);
        if (!isTreeExisting(classTree)) {
            classTree = (CommonTree) completeTree.getFirstChildWithType(JavaParser.INTERFACE);
        }
        if (!isTreeExisting(classTree)) {
            classTree = (CommonTree) completeTree.getFirstChildWithType(JavaParser.ENUM);
        }
        if (isTreeExisting(classTree)) {
            int classType = classTree.getType();
            switch (classType) {
                case JavaParser.CLASS:
                    this.theClass = this.currentClass = delegateClass(classTree, false, false);
                    break;
                case JavaParser.INTERFACE:
                    this.theClass = this.currentClass = delegateClass(classTree, false, true);
                    break;
                case JavaParser.ENUM:
                    this.theClass = this.currentClass = delegateClass(classTree, false, false);
                    break;
                default:
                    this.warnNotSupportedClassType(classType, classTree);
            }
        } else {
            int typeid = 0;
            CommonTree warnTree = (CommonTree) completeTree.getChild(2);
            if (isTreeExisting(warnTree)) {
                typeid = warnTree.getType();
            }
            this.warnNotSupportedClassType(typeid, classTree);
        }
    }
    
    private void warnNotSupportedClassType(int typeId, CommonTree classTree){
    	String warnMessage = "Detected a non-supported type";
    	if(typeId != 0)
    		warnMessage += " [Probably type id " + typeId + " ]";
    	if(classTree != null)
    		warnMessage += " Info: " + classTree.toString();
    	//logger.warn(warnMessage); //Deactivated 2012-02-10, since warnings were generated for annotation expressions like @interface.
    }

    private void delegateASTToGenerators(CommonTree tree) {
        if (isTreeExisting(tree)) {
        	CommonTree childNode;
            for (int i = 0; i < tree.getChildCount(); i++) {
                childNode = (CommonTree) tree.getChild(i);
                int nodeType = childNode.getType();

                switch (nodeType) {
                    case JavaParser.CLASS: case JavaParser.ENUM: case JavaParser.INTERFACE:	
                        if (classCount > 0) {
                            CommonTree innerClassTree = childNode;
                            this.parentClass = currentClass;
                            if (nodeType == JavaParser.INTERFACE) {
                            	this.currentClass = delegateClass(innerClassTree, true, true);
                            } else {
                            	this.currentClass = delegateClass(innerClassTree, true, false);
                            }
                            delegateASTToGenerators(innerClassTree);
                            this.currentClass = parentClass;
                        } else {
                            classCount++;
                            delegateASTToGenerators(childNode);
                        }
                        break;
                    case JavaParser.IMPORT:
                        delegateImport(childNode);
                        break;
                    case JavaParser.AT:
                        delegateAnnotation(childNode);
                        break;
                    case JavaParser.IMPLEMENTS_CLAUSE:
                        delegateImplementsDefinition(childNode);
                        break;
                    case JavaParser.EXTENDS_CLAUSE:
                        delegateInheritanceDefinition(childNode);
                        break;
                    case JavaParser.VAR_DECLARATION:
                        delegateAttribute(childNode);
                        break;
                    case JavaParser.FUNCTION_METHOD_DECL:
                    case JavaParser.CONSTRUCTOR_DECL:
                    case JavaParser.VOID_METHOD_DECL:
                        delegateMethod(childNode);
                        break;
    	            default: 
                        delegateASTToGenerators(childNode);
                        break;
                }
            }
        }
    }

    private String delegateClass(CommonTree classTree, boolean isInnerClass, boolean isInterface) {
        String analysedClass = null;
        if (isInnerClass) {
            analysedClass = javaClassGenerator.generateToDomain(sourceFilePath, numberOfLinesOfCode, classTree, true, parentClass, isInterface);
        } else {
            analysedClass = javaClassGenerator.generateToDomain(sourceFilePath, numberOfLinesOfCode, classTree, false, "", isInterface);
        }
        return analysedClass;
    }

    private void delegateAnnotation(CommonTree annotationTree) {
        javaAnnotationGenerator.generateToDomain(annotationTree, this.currentClass);
    }

    private void delegateImplementsDefinition(CommonTree treeNode) {
        implementsGenerator.generateToDomain(treeNode, this.currentClass);
    }

    private void delegateInheritanceDefinition(CommonTree treeNode) {
    	javaInheritanceDefinitionGenerator.generateToDomain(treeNode, this.currentClass);
    }

    private void delegateImport(CommonTree importTree) {
        javaImportGenerator.generateToDomain(importTree, this.currentClass);
    }

    private void delegateAttribute(CommonTree attributeTree) {
        javaAttributeGenerator.generateAttributeToDomain(attributeTree, this.currentClass);
    }

    private void delegateMethod(CommonTree methodTree) {
        methodGenerator.delegateMethodBlock(methodTree, this.currentClass);
    }

    private boolean hasPackageElement(CommonTree tree) {
        return tree.getFirstChildWithType(JavaParser.PACKAGE) != null;
    }

    private boolean isTreeExisting(Tree tree) {
    	return tree != null;
    }
}