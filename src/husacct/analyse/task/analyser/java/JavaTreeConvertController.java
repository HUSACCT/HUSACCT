package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.JavaParser;
import husacct.analyse.infrastructure.antlr.JavaParser.compilationUnit_return;
import java.util.ArrayList;
import java.util.List;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.BaseTree;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

class JavaTreeConvertController {
	
	private String theClass = null;
	private String thePackage = null;
	private Tree packageTree;
	private Tree classTree;
	private Tree classTopLevelScopeTree;
	
	public void delegateModelGenerators(JavaParser javaParser) throws RecognitionException {
		
		compilationUnit_return compilationUnit = javaParser.compilationUnit();
		CommonTree compilationUnitTree = (CommonTree) compilationUnit.getTree();
		
		packageTree = compilationUnitTree.getFirstChildWithType(JavaParser.PACKAGE);
		classTree = compilationUnitTree.getFirstChildWithType(JavaParser.CLASS);
		
		if(packageTree != null) delegatePackage(packageTree);
		if(classTree != null) delegateClass(classTree);
		
		//TODO : skipping interface and annotations, need to added later
		if(classTree != null && hasType(compilationUnitTree, JavaParser.IMPORT)){
			List<CommonTree> importTrees = this.getAllImportTrees(compilationUnitTree);
			for(CommonTree importTree : importTrees){
				delegateImport(importTree);
			}
		}
		
		//TODO Werkt nog niet helemaal zoals het hoort: nullPointers? Herschrijven?
//		if (hasMethods(classTree)){
//			Tree methodTree = classTree.getChild(2).getChild(1);
//			if(methodTree != null)delegateMethodTree(methodTree);
////			delegateMethodTree(classTree.getChild(2).getChild(1));
//		}
		
//		if(classTopLevelScopeTree != null) delegateTopLevelScopeTree(classTopLevelScopeTree);
//      if (methodTree != null) delegateMethodTree(methodTree);
	}
	
	private List<CommonTree> getAllImportTrees(CommonTree baseTree){
		List<CommonTree> importTrees = new ArrayList<CommonTree>();
		CommonTree importTree = (CommonTree)baseTree.getFirstChildWithType(JavaParser.IMPORT);
		if(importTree != null){
			importTrees.add(importTree);
			baseTree.deleteChild(importTree.childIndex);
			if(hasType(baseTree, JavaParser.IMPORT)){
				List<CommonTree> otherImportTrees = getAllImportTrees(baseTree);
				for(CommonTree importNode: otherImportTrees){
					importTrees.add(importNode);
				}
			}
		}
		return importTrees;
	}
	
	public void delegatePackage(Tree packageTree){
		JavaPackageGenerator javaPackageGenerator = new JavaPackageGenerator();
		this.thePackage = javaPackageGenerator.generateModel((CommonTree)packageTree);
	}
	
	public void delegateClass(Tree classTree){
		JavaClassGenerator javaClassGenerator = new JavaClassGenerator(thePackage);
		String analysedClass = javaClassGenerator.generateModel((CommonTree)classTree);
		if(this.theClass == null) this.theClass = analysedClass;
		Tree classTopLevelScopeTreeChild = ((BaseTree) classTree).getFirstChildWithType(JavaParser.CLASS_TOP_LEVEL_SCOPE);
		if(classTopLevelScopeTree != null){
			classTopLevelScopeTree = classTopLevelScopeTreeChild;
		}
	}
	
	public void delegateImport(CommonTree importTree){
		JavaImportGenerator javaImportGenerator = new JavaImportGenerator();
		javaImportGenerator.generateFamixImport(importTree, this.theClass);
	}

//	private void delegateMethodTree(Tree methodTree) {
//		JavaMethodGenerator javaMethodGenerator = new JavaMethodGenerator();
//		javaMethodGenerator.generateFamix((CommonTree) methodTree, theClass);
//	}

//	public void delegateAttribute(Tree scopeTree, FamixClass classObject){
//		JavaAttributeGenerator javaAttributeGenerator = new JavaAttributeGenerator();
//	}

	public void delegateTopLevelScopeTree(Tree scopeTree){
		System.out.println(scopeTree.getChildCount());
	}	
	
	private boolean hasType(CommonTree completeTree, int nodeType){
		return completeTree.getFirstChildWithType(nodeType) != null;
	}
	
	private boolean hasMethods(Tree classTree) {
		if (classTree != null) {
			if (classTree.getChildCount() == 3) {
				if(classTree.getChild(2).getChildCount() > 0) return true;
			}
		}
		return false;
	}
}
