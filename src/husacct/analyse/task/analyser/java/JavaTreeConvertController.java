package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.JavaParser; 
import husacct.analyse.infrastructure.antlr.JavaParser.compilationUnit_return;
import org.antlr.runtime.RecognitionException; 
import org.antlr.runtime.tree.CommonTree; 
import org.antlr.runtime.tree.Tree;

class JavaTreeConvertController { 
        
	private String theClass = null; 
    private String thePackage = null; 
        
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
    	delegateClass(classTree);
    }
        
    private void walkAST(CommonTree tree) {
    	if (tree != null) {
    		for (int i = 0; i < tree.getChildCount(); i++) {
    			if(tree.getChild(i).getType() == JavaParser.IMPORT){
    				delegateImport((CommonTree)tree.getChild(i));
    			}
    			if(tree.getChild(i).getType() == JavaParser.VAR_DECLARATION ){
    				delegateAttribute(tree.getChild(i));
    			}
    			walkAST((CommonTree) tree.getChild(i));
    		}
    	}
	}
        
	public void delegatePackage(Tree packageTree){ 
       	JavaPackageGenerator javaPackageGenerator = new JavaPackageGenerator(); 
        this.thePackage = javaPackageGenerator.generateModel((CommonTree)packageTree); 
	} 
        
    public void delegateClass(Tree classTree){ 
      	JavaClassGenerator javaClassGenerator = new JavaClassGenerator(thePackage); 
       	String analysedClass = javaClassGenerator.generateModel((CommonTree)classTree); 
        this.theClass = analysedClass; 
    } 
        
    public void delegateImport(CommonTree importTree){ 
       	JavaImportGenerator javaImportGenerator = new JavaImportGenerator(); 
       	javaImportGenerator.generateFamixImport(importTree, this.theClass); 
    }
    
    public void delegateAttribute(Tree attributeTree){
    	JavaAttributeGenerator javaAttributeGenerator = new JavaAttributeGenerator();
    	javaAttributeGenerator.generateModel(attributeTree, theClass);
    }

    public void delegateTopLevelScopeTree(Tree scopeTree){ 
       	System.out.println(scopeTree.getChildCount()); 
    }       
}