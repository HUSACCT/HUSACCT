package husacct.analyse.task.analyse.java.analysing;

import java.util.List;

import husacct.analyse.task.analyse.java.parsing.JavaParser.ConstructorDeclarationContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.GenericConstructorDeclarationContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.GenericInterfaceMethodDeclarationContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.GenericMethodDeclarationContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.InterfaceMethodDeclarationContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.MethodDeclarationContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.ModifierContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.QualifiedNameContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.QualifiedNameListContext;
import husacct.analyse.task.analyse.java.parsing.JavaParser.TypeTypeContext;

import org.antlr.v4.runtime.tree.TerminalNode;
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
        /* Test helpers
    	if (belongsToClass.contains("org.apache.tools.ant.util.WorkerAnt")) {
    		if (methodDeclaration.start.getLine() == 80) {
    				boolean breakpoint = true;
    		}
    	} */

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
			dispatchAnnotationsOfMember(modifierList, belongsToClass);
	        if (methodDeclaration.qualifiedNameList() != null) {
	        	delegateException(methodDeclaration.qualifiedNameList());
	        }
	        if (methodDeclaration.methodBody() != null && methodDeclaration.methodBody().block() != null) {
	        	new BlockAnalyser(methodDeclaration.methodBody().block(), belongsToClass, this.name + parameterList);
	        }
    	}
		catch (Exception e) {
			logger.warn(" Exception while processing: " + belongsToClass + " line : " + methodDeclaration.start.getLine() + " " + e.getMessage());
		}
    }

    public void analysegenericMethod(List<ModifierContext> modifierList, GenericMethodDeclarationContext genericMethod) {
    	try {
    		if (genericMethod.methodDeclaration() != null) {
    			analyseMethod(modifierList, genericMethod.methodDeclaration());
    		}
    		// Currently no analysis of typeParameters()
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
			dispatchAnnotationsOfMember(modifierList, belongsToClass);
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
    
    public void analyseGenericConstructor(List<ModifierContext> modifierList, GenericConstructorDeclarationContext genericConstructor) {
    	try {
    		if (genericConstructor.constructorDeclaration() != null) {
    			analyseConstructor(modifierList, genericConstructor.constructorDeclaration());
    		}
    		// Currently no analysis of typeParameters()
    	}
		catch (Exception e) {
			logger.warn(" Exception while processing: " + belongsToClass + e.getMessage());
		}
    }
    
    public void analyseInterfaceMethod(List<ModifierContext> modifierList, InterfaceMethodDeclarationContext methodDeclaration) {
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
			dispatchAnnotationsOfMember(modifierList, belongsToClass);
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

    public void analyseGenericInterfaceMethod(List<ModifierContext> modifierList, GenericInterfaceMethodDeclarationContext genericMethod) {
    	try {
    		if (genericMethod.interfaceMethodDeclaration() != null) {
    			analyseInterfaceMethod(modifierList, genericMethod.interfaceMethodDeclaration());
    		}
    		// Currently no analysis of typeParameters()
    	}
		catch (Exception e) {
			logger.warn(" Exception while processing: " + belongsToClass + e.getMessage());
		}
    }
    
    public void analyseAnnotationMethod(List<ModifierContext> modifierList, TypeTypeContext typeType, TerminalNode identifier) {
    	try {
	    	lineNumber = typeType.start.getLine();
	    	visibility = determineVisibility(modifierList);
	        hasClassScope = determineIsStatic(modifierList);
	        isAbstract = determineIsAbstract(modifierList);
	    	declaredReturnType = getReturnType(typeType);
	        name = identifier.getText();
	        createMethodObject();
			dispatchAnnotationsOfMember(modifierList, belongsToClass);
    	}
		catch (Exception e) {
			logger.warn(" Exception while processing: " + belongsToClass + e.getMessage());
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
    		returnType = determineTypeOfTypeType(returnTypeContext, belongsToClass);
    	}
    	return returnType;
    }

    private void createMethodObject() {
        uniqueName = belongsToClass + "." + this.name + parameterList;
		if(SkippedJavaTypes.isSkippable(declaredReturnType)){
	        modelService.createMethodOnly(name, uniqueName, visibility, parameterList, declaredReturnType, belongsToClass, isConstructor, isAbstract, hasClassScope, lineNumber);
        } else {
            modelService.createMethod(name, uniqueName, visibility, parameterList, declaredReturnType, belongsToClass, isConstructor, isAbstract, hasClassScope, lineNumber);
        }
    }
}