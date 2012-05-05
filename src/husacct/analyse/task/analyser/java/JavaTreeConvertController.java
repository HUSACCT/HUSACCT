package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;
import husacct.analyse.infrastructure.antlr.java.JavaParser.compilationUnit_return;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

class JavaTreeConvertController { 
    
	private String theClass = null; 
    private String thePackage = null;
    private String currentClass = null;
    private String parentClass = null;
    private int classCount = 0;
        
    public void delegateModelGenerators(JavaParser javaParser) throws RecognitionException { 
    	compilationUnit_return compilationUnit = javaParser.compilationUnit(); 
        CommonTree compilationUnitTree = (CommonTree) compilationUnit.getTree(); 
        createClassOrInterfaceInformation(compilationUnitTree);
        walkAST(compilationUnitTree);
    }
    
    private void createClassOrInterfaceInformation(CommonTree completeTree){
    	Tree packageTree = completeTree.getFirstChildWithType(JavaParser.PACKAGE);
    	if(hasPackageElement(completeTree)){
    		delegatePackage(packageTree);
    	} else this.thePackage = "";
    	Tree classTree = completeTree.getFirstChildWithType(JavaParser.CLASS);
    	if(classTree == null){
    		classTree = completeTree.getFirstChildWithType(JavaParser.INTERFACE);
    		this.theClass = this.currentClass = delegateInterface(classTree);
    	}else{
    		this.theClass = this.currentClass = delegateClass(classTree, false);
    	}
    	this.parentClass = theClass;
    }
        
    private void walkAST(CommonTree tree) {
    	if (tree != null) {
    		for (int i = 0; i < tree.getChildCount(); i++) {
    			Tree treeNode = tree.getChild(i);
    			int nodeType = treeNode.getType();
    			if(nodeType == JavaParser.CLASS){
    				if(classCount > 0) {
    					CommonTree innerClassTree = (CommonTree)treeNode;
    					this.parentClass = currentClass;
    					this.currentClass = delegateClass(innerClassTree, true);
    					walkAST(innerClassTree);
    				}else classCount++;
    			}else{
    				if(nodeType == JavaParser.IMPORT){
        				delegateImport((CommonTree)treeNode);
        				deleteTreeChild(treeNode);
  
        			}
    				if(nodeType == JavaParser.IMPLEMENTS_CLAUSE){
    					delegateImplementsDefinition((CommonTree)treeNode);
    					deleteTreeChild(treeNode);
    				}
    				if(nodeType == JavaParser.EXTENDS_CLAUSE ){
        				delegateInheritanceDefinition((CommonTree)treeNode);
        				deleteTreeChild(treeNode);

        			}
        			if(nodeType == JavaParser.VAR_DECLARATION ){
        				delegateAttribute(treeNode);
        				deleteTreeChild(treeNode);
        			}
        			if(nodeType == JavaParser.FUNCTION_METHOD_DECL || nodeType == JavaParser.CONSTRUCTOR_DECL || nodeType == JavaParser.VOID_METHOD_DECL){
        				delegateMethod(treeNode);
        				deleteTreeChild(treeNode);
        			}
        			if(nodeType == JavaParser.THROW || nodeType == JavaParser.CATCH || nodeType == JavaParser.THROWS){
        				delegateException(treeNode);
        				deleteTreeChild(treeNode);
        			}
    			}

    			walkAST((CommonTree) tree.getChild(i));

    		}
    	}
	}
    
    private void deleteTreeChild(Tree treeNode){
    	for (int child = 0 ; child < treeNode.getChildCount(); child++){
			treeNode.deleteChild(treeNode.getChild(child).getChildIndex());
		}
    }
        
    private void delegatePackage(Tree packageTree){ 
       	JavaPackageGenerator javaPackageGenerator = new JavaPackageGenerator(); 
        this.thePackage = javaPackageGenerator.generateModel((CommonTree)packageTree); 
	} 
        
    private String delegateClass(Tree classTree, boolean isInnerClass){ 
      	JavaClassGenerator javaClassGenerator = new JavaClassGenerator(thePackage); 
      	String analysedClass;
      	if(isInnerClass) analysedClass = javaClassGenerator.generateModel((CommonTree)classTree, parentClass);
      	else analysedClass = javaClassGenerator.generateModel((CommonTree)classTree);
        return analysedClass;
    } 
    
    private String delegateInterface(Tree interfaceTree){
    	JavaInterfaceGenerator javaInterfaceGenerator = new JavaInterfaceGenerator(thePackage);
    	return javaInterfaceGenerator.generateModel((CommonTree)interfaceTree);
    }
    
    private void delegateImplementsDefinition(CommonTree treeNode) {
		JavaImplementsDefinitionGenerator implementsGenerator = new JavaImplementsDefinitionGenerator();
		implementsGenerator.generateModelObject(treeNode, this.theClass);
	}
    
    private void delegateInheritanceDefinition(CommonTree treeNode) {
		JavaInheritanceDefinitionGenerator javaInheritanceDefinitionGenerator = new JavaInheritanceDefinitionGenerator();
		javaInheritanceDefinitionGenerator.generateModelObject(treeNode, this.theClass);
	}
        
    private void delegateImport(CommonTree importTree){ 
       	JavaImportGenerator javaImportGenerator = new JavaImportGenerator(); 
       	javaImportGenerator.generateFamixImport(importTree, this.currentClass); 
    }
    
    private void delegateAttribute(Tree attributeTree){
    	JavaAttributeGenerator javaAttributeGenerator = new JavaAttributeGenerator();
    	javaAttributeGenerator.generateModel(attributeTree, this.currentClass);
    }
    
    private void delegateMethod(Tree methodTree){
    	JavaMethodGenerator methodGenerator = new JavaMethodGenerator();
    	methodGenerator.generateModelObject((CommonTree)methodTree, this.currentClass);
    }
    
    private void delegateException(Tree exceptionTree){
    	JavaExceptionGenerator exceptionGenerator = new JavaExceptionGenerator();
    	exceptionGenerator.generateModel((CommonTree)exceptionTree, this.currentClass);
    }
    
    private boolean hasPackageElement(CommonTree tree){
    	return tree.getFirstChildWithType(JavaParser.PACKAGE) != null;
    }
}