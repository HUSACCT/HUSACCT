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
        createClassInformation(compilationUnitTree);
        walkAST(compilationUnitTree);
    }
    
    private void createClassInformation(CommonTree completeTree){
    	Tree packageTree = completeTree.getFirstChildWithType(JavaParser.PACKAGE);
    	delegatePackage(packageTree);
    	Tree classTree = completeTree.getFirstChildWithType(JavaParser.CLASS);
    	this.theClass = this.currentClass = delegateClass(classTree, false);
    	this.parentClass = theClass;
    }
        
    private void walkAST(CommonTree tree) {
    	if (tree != null) {
    		for (int i = 0; i < tree.getChildCount(); i++) {
    			if(tree.getChild(i).getType() == JavaParser.CLASS){
    				if(classCount > 0) {
    					CommonTree innerClassTree = (CommonTree)tree.getChild(i);
    					this.parentClass = currentClass;
    					this.currentClass = delegateClass(innerClassTree, true);
    					walkAST(innerClassTree);
    				}else classCount++;
    			}else{
    				if(tree.getChild(i).getType() == JavaParser.IMPORT){
        				delegateImport((CommonTree)tree.getChild(i));
        			}
        			if(tree.getChild(i).getType() == JavaParser.VAR_DECLARATION ){
        				delegateAttribute(tree.getChild(i));
        				tree.deleteChild(tree.getChild(i).getChildIndex());
        			}
    			}
    			walkAST((CommonTree) tree.getChild(i));
    		}
    	}
	}
        
	public void delegatePackage(Tree packageTree){ 
       	JavaPackageGenerator javaPackageGenerator = new JavaPackageGenerator(); 
        this.thePackage = javaPackageGenerator.generateModel((CommonTree)packageTree); 
	} 
        
    public String delegateClass(Tree classTree, boolean isInnerClass){ 
      	JavaClassGenerator javaClassGenerator = new JavaClassGenerator(thePackage); 
      	String analysedClass;
      	if(isInnerClass) analysedClass = javaClassGenerator.generateModel((CommonTree)classTree, parentClass);
      	else analysedClass = javaClassGenerator.generateModel((CommonTree)classTree);
        return analysedClass;
    } 
        
    public void delegateImport(CommonTree importTree){ 
       	JavaImportGenerator javaImportGenerator = new JavaImportGenerator(); 
       	javaImportGenerator.generateFamixImport(importTree, this.currentClass); 
    }
    
    public void delegateAttribute(Tree attributeTree){
    	JavaAttributeGenerator javaAttributeGenerator = new JavaAttributeGenerator();
    	javaAttributeGenerator.generateModel(attributeTree, this.currentClass);
    }
}