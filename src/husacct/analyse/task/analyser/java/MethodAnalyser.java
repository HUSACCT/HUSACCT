package husacct.analyse.task.analyser.java;

import java.util.List;

import husacct.analyse.infrastructure.antlr.java.Java7Parser;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.ConstructorDeclarationContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.FormalParameterContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.MethodDeclarationContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.ModifierContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.QualifiedNameContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.QualifiedNameListContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.TypeTypeContext;
import husacct.common.enums.DependencySubTypes;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.log4j.Logger;

class MethodAnalyser extends JavaGenerator {

    private boolean hasClassScope = false;
    private boolean isAbstract = false;
    private boolean isConstructor = false;
    private String belongsToClass;
    private String visibility = "public";
    private String declaredReturnType;
    private String parameterList = "()";
    public String name = "";
    public String uniqueName = "";
    private int lineNumber = 0;

    private Logger logger = Logger.getLogger(MethodAnalyser.class);

    public MethodAnalyser(String belongsToClass) {
        this.belongsToClass = belongsToClass;
    }
    public void analyseMethod(List<ModifierContext> modifierList, MethodDeclarationContext methodDeclaration) {
    	try {
	    	lineNumber = methodDeclaration.start.getLine();
	    	visibility = determineVisibility(modifierList);
	        hasClassScope = determineIsStatic(modifierList);
	        isAbstract = determineIsAbstract(modifierList);
	    	declaredReturnType = getReturnType(methodDeclaration.typeType());
	        name = methodDeclaration.Identifier().getText();
	        if (methodDeclaration.formalParameters() != null && methodDeclaration.formalParameters().formalParameterList() != null){
	            ParameterAnalyser javaParameterGenerator = new ParameterAnalyser();
	            parameterList = "(" + javaParameterGenerator.generateParameterObjects(methodDeclaration.formalParameters().formalParameterList(), name, belongsToClass) + ")";
	        } 
	        createMethodObject();
	        if (methodDeclaration.qualifiedNameList() != null) {
	        	delegateException(methodDeclaration.qualifiedNameList());
	        }
	        if (methodDeclaration.methodBody() != null && methodDeclaration.methodBody().block() != null) {
	        	new BlockAnalyser(methodDeclaration.methodBody().block(), belongsToClass, this.name + parameterList);
	        }
    	}
		catch (Exception e) {
			logger.warn(" Exception while processing: " + belongsToClass + e.getMessage());
		}
    }

    public void analyseConstructor(List<ModifierContext> modifierList, ConstructorDeclarationContext methodDeclaration) {
    	try {
            isConstructor = true;
            lineNumber = methodDeclaration.start.getLine();
	    	visibility = determineVisibility(modifierList);
	        hasClassScope = determineIsStatic(modifierList);
	        isAbstract = determineIsAbstract(modifierList);
	    	declaredReturnType =  "";
	        name = methodDeclaration.Identifier().getText();
	        if (methodDeclaration.formalParameters() != null && methodDeclaration.formalParameters().formalParameterList() != null){
	            ParameterAnalyser javaParameterGenerator = new ParameterAnalyser();
	            parameterList = "(" + javaParameterGenerator.generateParameterObjects(methodDeclaration.formalParameters().formalParameterList(), name, belongsToClass) + ")";
	        } 
	        createMethodObject();
	        if (methodDeclaration.qualifiedNameList() != null) {
	        	delegateException(methodDeclaration.qualifiedNameList());
	        }
	        if (methodDeclaration.constructorBody() != null && methodDeclaration.constructorBody().block() != null) {
	        	new BlockAnalyser(methodDeclaration.constructorBody().block(), belongsToClass, this.name + parameterList);
	        }
    	}
		catch (Exception e) {
			logger.warn(" Exception while processing: " + belongsToClass + e.getMessage());
		}
    }
    
    private String getClassOfUniqueName(String uniqueName) {
        String[] parts = uniqueName.split("\\.");
        return parts[parts.length - 1];
    }

    private void WalkThroughMethod() {
    	Tree tree = null;
        for (int childCount = 0; childCount < tree.getChildCount(); childCount++) {
            Tree child = tree.getChild(childCount);
	        boolean walkThroughChildren = true;
            int treeType = child.getType();
        	
        	/* Test helper
        	if (belongsToClass.contains("org.eclipse.ui.internal.views.markers.MarkerFieldFilterGroup")){
        		if (lineNumber == 522) {
        			int breakpoint = 1;
        		}
        	} */

            switch(treeType) {
	            case Java7Parser.ABSTRACT: 
	            	isAbstract = true;
	            	break;
	            case Java7Parser.AT:
	            	//AnnotationAnalyser annotationGenerator = new AnnotationAnalyser();
	                //annotationGenerator.generateToDomain((CommonTree) child, belongsToClass, "method");
	            	break;
	            case Java7Parser.STATIC: 
	            	hasClassScope = true; 
	            	break;
	            case Java7Parser.PUBLIC: 
	            	visibility = "public"; 
	            	break;
	            case Java7Parser.PRIVATE: 
	            	visibility = "private"; 
	            	break;
	            case Java7Parser.PROTECTED: 
	            	visibility = "protected"; 
	            	break;
/*	            case Java7Parser.TYPE: 
	            	getReturnType(child); 
		            walkThroughChildren = false;
	            	break;
*/	            case Java7Parser.Identifier: 
	            	name = child.getText(); 
	            	lineNumber = child.getLine();
	            	break;
	            case Java7Parser.THROW: 
	            	//delegateException(child); 
		            walkThroughChildren = false;
	            	break;
	            case Java7Parser.THROWS: 
	            	//delegateException(child); 
		            walkThroughChildren = false;
	            	break;
/*	            case Java7Parser.THROWS_CLAUSE: 
	            	delegateException(child); 
		            walkThroughChildren = false;
	            	break;
	            case Java7Parser.FORMAL_PARAM_LIST: 
	            	if (child.getChildCount() > 0) {
	                    JavaParameterGenerator javaParameterGenerator = new JavaParameterGenerator();
	                    signature = "(" + javaParameterGenerator.generateParameterObjects(child, name, belongsToClass) + ")";
	    	            walkThroughChildren = false;
	                }
	            	break;
	            case Java7Parser.BLOCK_SCOPE: {
	            	setSignature();
	                JavaBlockScopeGenerator javaBlockScopeGenerator = new JavaBlockScopeGenerator();
	                javaBlockScopeGenerator.walkThroughBlockScope((CommonTree) child, this.belongsToClass, this.name + signature);
		            walkThroughChildren = false;
	                break;
	            }
*/	            default: break;
            }
	        if (walkThroughChildren) {
	        	WalkThroughMethod();
	        }
        }
    }

    private void delegateException(QualifiedNameListContext nameList) {
    	for (QualifiedNameContext name : nameList.qualifiedName()) {
    		String exceptionType = name.getText();
    		int exceptionlineNr = name.start.getLine();
            if (!belongsToClass.equals("") && !exceptionType.equals("")) {
            	modelService.createException(belongsToClass, exceptionType, exceptionlineNr);
            }
    	}
    }

    private String getReturnType(TypeTypeContext returnTypeContext) {
    	String returnType;
    	if (returnTypeContext == null) {
    		returnType = "";
    	} else {
    		returnType = determineTypeOfTypeType(returnTypeContext, belongsToClass, DependencySubTypes.DECL_RETURN_TYPE);
    	}
    	return returnType;
    }

    private void createMethodObject() {
        uniqueName = belongsToClass + "." + this.name + parameterList;
		if(SkippedTypes.isSkippable(declaredReturnType)){
	        modelService.createMethodOnly(name, uniqueName, visibility, parameterList, declaredReturnType, belongsToClass, isConstructor, isAbstract, hasClassScope, lineNumber);
        } else {
            modelService.createMethod(name, uniqueName, visibility, parameterList, declaredReturnType, belongsToClass, isConstructor, isAbstract, hasClassScope, lineNumber);
        }
    }
}