package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.Java7Parser;
import husacct.common.enums.DependencySubTypes;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.log4j.Logger;

class JavaMethodGeneratorController extends JavaGenerator {

    private boolean hasClassScope = false;
    private boolean isAbstract = false;
    private boolean isConstructor;
    private String belongsToClass;
    private String accessControlQualifier = "package-private";
    private boolean isPureAccessor;
    private String declaredReturnType;
    private String signature = "";
    public String name;
    public String uniqueName;
    private int lineNumber;
    private Logger logger = Logger.getLogger(JavaMethodGeneratorController.class);
    JavaAttributeAndLocalVariableGenerator javaLocalVariableGenerator = new JavaAttributeAndLocalVariableGenerator();

    public void delegateMethodBlock(CommonTree methodTree, String className) {
        this.belongsToClass = className;
        lineNumber = methodTree.getLine();
        hasClassScope = false;
        isAbstract = false;
        isConstructor = false;
        accessControlQualifier = "package-private";
        isPureAccessor = false;
        declaredReturnType = "";
        signature = "";
        name = "";
        uniqueName  = "";
        checkMethodType(methodTree);
        WalkThroughMethod(methodTree);
        createMethodObject();
    }

    private void checkMethodType(CommonTree methodTree) {
        int methodTreeType = methodTree.getType();
        switch(methodTreeType) {
        case Java7Parser.CONSTRUCTOR_DECL: 
            declaredReturnType = "";
            isConstructor = true;
            name = getClassOfUniqueName(belongsToClass);
            break;
        case Java7Parser.VOID_METHOD_DECL:
            declaredReturnType = "";
            isConstructor = false;
        	break;
        case Java7Parser.FUNCTION_METHOD_DECL:
            isConstructor = false;
        	break;
        default:
        	logger.warn("MethodGenerator couldn't find a valid function type");
        	break;
        	}
        }
        
    private String getClassOfUniqueName(String uniqueName) {
        String[] parts = uniqueName.split("\\.");
        return parts[parts.length - 1];
    }

    private void WalkThroughMethod(Tree tree) {
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
	            	JavaAnnotationGenerator annotationGenerator = new JavaAnnotationGenerator();
	                annotationGenerator.generateToDomain((CommonTree) child, belongsToClass, "method");
	            	break;
	            case Java7Parser.STATIC: 
	            	hasClassScope = true; 
	            	break;
	            case Java7Parser.PUBLIC: 
	            	accessControlQualifier = "public"; 
	            	break;
	            case Java7Parser.PRIVATE: 
	            	accessControlQualifier = "private"; 
	            	break;
	            case Java7Parser.PROTECTED: 
	            	accessControlQualifier = "protected"; 
	            	break;
	            case Java7Parser.TYPE: 
	            	getReturnType(child); 
		            walkThroughChildren = false;
	            	break;
	            case Java7Parser.Identifier: 
	            	name = child.getText(); 
	            	lineNumber = child.getLine();
	            	break;
	            case Java7Parser.THROW: 
	            	delegateException(child); 
		            walkThroughChildren = false;
	            	break;
	            case Java7Parser.THROWS: 
	            	delegateException(child); 
		            walkThroughChildren = false;
	            	break;
	            case Java7Parser.THROWS_CLAUSE: 
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
	            default: break;
            }
	        if (walkThroughChildren) {
	        	WalkThroughMethod(child);
	        }
        }
    }

    private void setSignature() {
        if (signature.equals("")) {
            signature = "()";
        }
    }

    private void delegateException(Tree exceptionTree) {
        JavaExceptionGenerator exceptionGenerator = new JavaExceptionGenerator();
        exceptionGenerator.generateToDomain((CommonTree) exceptionTree, this.belongsToClass);
    }

    private void getReturnType(Tree tree) {
    	JavaInvocationGenerator javaInvocationGenerator = new JavaInvocationGenerator(this.belongsToClass);
    	String foundType = javaInvocationGenerator.getCompleteToString((CommonTree) tree, belongsToClass, DependencySubTypes.DECL_RETURN_TYPE);
        if (foundType != null) {
            this.declaredReturnType = foundType;
        } else {
        	this.declaredReturnType = "";
       	}
    }

    private void createMethodObject() {
        uniqueName = belongsToClass + "." + this.name + signature;
		if(SkippedTypes.isSkippable(declaredReturnType)){
	        modelService.createMethodOnly(name, uniqueName, accessControlQualifier, signature, isPureAccessor, declaredReturnType, belongsToClass, isConstructor, isAbstract, hasClassScope, lineNumber);
        } else {
            modelService.createMethod(name, uniqueName, accessControlQualifier, signature, isPureAccessor, declaredReturnType, belongsToClass, isConstructor, isAbstract, hasClassScope, lineNumber);
        }
    }
}