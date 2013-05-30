package husacct.analyse.task.analyser.java;

import java.util.ArrayList;

import husacct.analyse.infrastructure.antlr.java.JavaParser;
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
    private ArrayList<String> declaredReturnType;
    private String signature = "";
    public String name;
    public String uniqueName;
    private int lineNumber;
    private Logger logger = Logger.getLogger(JavaMethodGeneratorController.class);
    JavaAttributeAndLocalVariableGenerator javaLocalVariableGenerator = new JavaAttributeAndLocalVariableGenerator();

    public void delegateMethodBlock(CommonTree methodTree, String className) {
        this.belongsToClass = className;
        lineNumber = methodTree.getLine();
        checkMethodType(methodTree);
        WalkThroughMethod(methodTree);
        createMethodObject();
    }

    private void checkMethodType(CommonTree methodTree) {
        declaredReturnType = new ArrayList<String>();
        if (methodTree.getType() == JavaParser.CONSTRUCTOR_DECL) {
            declaredReturnType.add("");
            isConstructor = true;
            name = getClassOfUniqueName(belongsToClass);

        } else if (methodTree.getType() == JavaParser.VOID_METHOD_DECL) {
            declaredReturnType.add("");
            isConstructor = false;
        } else if (methodTree.getType() == JavaParser.FUNCTION_METHOD_DECL) {
            isConstructor = false;
        } else {
            logger.warn("MethodGenerator couldn't find a valid function type");
        }
    }

    private String getClassOfUniqueName(String uniqueName) {
        String[] parts = uniqueName.split("\\.");
        return parts[parts.length - 1];
    }

    private void WalkThroughMethod(Tree tree) {
        for (int childCount = 0; childCount < tree.getChildCount(); childCount++) {
            Tree child = tree.getChild(childCount);
            int treeType = child.getType();
            switch(treeType) {
            case JavaParser.ABSTRACT: isAbstract = true; break;
            case JavaParser.FINAL: hasClassScope = true; break;
            case JavaParser.PUBLIC: accessControlQualifier = "public"; break;
            case JavaParser.PRIVATE: accessControlQualifier = "private"; break;
            case JavaParser.PROTECTED: accessControlQualifier = "protected"; break;
            case JavaParser.TYPE: getReturnType(child); deleteTreeChild(child); break;
            case JavaParser.IDENT: name = child.getText(); break;
            case JavaParser.THROW: delegateException(child); deleteTreeChild(child); break;
            case JavaParser.THROWS: delegateException(child); deleteTreeChild(child); break;
            case JavaParser.THROWS_CLAUSE: delegateException(child); deleteTreeChild(child); break;
            case JavaParser.FORMAL_PARAM_LIST: 
            	if (child.getChildCount() > 0) {
                    JavaParameterGenerator javaParameterGenerator = new JavaParameterGenerator();
                    signature = "(" + javaParameterGenerator.generateParameterObjects(child, name, belongsToClass) + ")";
                    deleteTreeChild(child);
                }
            	break;
            case JavaParser.BLOCK_SCOPE: {
            	setSignature();
                JavaBlockScopeGenerator javaBlockScopeGenerator = new JavaBlockScopeGenerator();
                javaBlockScopeGenerator.walkThroughBlockScope((CommonTree) child, this.belongsToClass, this.name + signature);
                deleteTreeChild(child);
                break;
            }
            default: break;
            }
            

            WalkThroughMethod(child);
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
        declaredReturnType = javaLocalVariableGenerator.generateMethodReturnType(tree, belongsToClass);
    }

    private void deleteTreeChild(Tree treeNode) {
        for (int child = 0; child < treeNode.getChildCount();) {
            treeNode.deleteChild(treeNode.getChild(child).getChildIndex());
        }
    }

    private void createMethodObject() {
        uniqueName = belongsToClass + "." + this.name + signature;
        ArrayList<String> returnTypes = new ArrayList<String>();
        for (String s : declaredReturnType) {
            if (!SkippedTypes.isSkippable(s)) {
                returnTypes.add(s);
            }
        }


        modelService.createMethod(name, uniqueName, accessControlQualifier, signature, isPureAccessor, returnTypes, belongsToClass, isConstructor, isAbstract, hasClassScope, lineNumber);
    }
}