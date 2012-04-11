package husacct.analyse.abstraction.mappers.javamapper;

import husacct.analyse.abstraction.mappers.javamapper.famixObjectGenerators.JavaAttributeGenerator;
import husacct.analyse.abstraction.mappers.javamapper.famixObjectGenerators.JavaClassGenerator;
import husacct.analyse.abstraction.mappers.javamapper.famixObjectGenerators.JavaImportGenerator;
import husacct.analyse.abstraction.mappers.javamapper.famixObjectGenerators.JavaMethodGenerator;
import husacct.analyse.abstraction.mappers.javamapper.famixObjectGenerators.JavaPackageGenerator;
import husacct.analyse.domain.famix.FamixClass;
import husacct.analyse.domain.famix.FamixMethod;
import husacct.analyse.domain.famix.FamixObject;
import husacct.analyse.domain.famix.FamixPackage;
import husacct.analyse.infrastructure.antlr.JavaParser;
import husacct.analyse.infrastructure.antlr.JavaParser.compilationUnit_return;
import java.util.ArrayList;
import java.util.List;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.BaseTree;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class JavaTreeParserDelegater {
	
	FamixPackage  famixPackageObject;
	FamixClass famixClassObject;
	FamixMethod famixMethodObject;
	List<FamixObject> famixObjects = new ArrayList<FamixObject>();
	
	Tree packageTree;
	Tree classTree;
	Tree importTree;
	Tree classTopLevelScopeTree;
	Tree methodTree;
	
	public List<FamixObject> delegateFamixObjectGenerators(JavaParser javaParser) throws RecognitionException {
		compilationUnit_return compilationUnit = javaParser.compilationUnit();
		CommonTree compilationUnitTree = (CommonTree) compilationUnit.getTree();

		packageTree = compilationUnitTree.getFirstChildWithType(JavaParser.PACKAGE);
		classTree = compilationUnitTree.getFirstChildWithType(JavaParser.CLASS);

		if (hasMethodes(classTree)){
			methodTree = classTree.getChild(2).getChild(1);
		}

		if(packageTree != null){
			delegatePackage(packageTree);
		}

		if(classTree != null){
			delegateClass(classTree);
		}
		
		if(classTopLevelScopeTree != null){
			delegateTopLevelScopeTree(classTopLevelScopeTree);
		}
	
                if (methodTree != null){
                        delegateMethodTree(methodTree);
                }
	
		//TODO : skipping interface and annotations, need to added later
		if(classTree != null && hasType(compilationUnitTree, JavaParser.IMPORT)){
			List<CommonTree> importTrees = this.getAllImportTrees(compilationUnitTree);
			for(CommonTree importTree : importTrees){
				delegateImport(importTree, famixClassObject.getUniqueName());
			}
		}
		
		return famixObjects;
	}
	
	private void createPackageObject(Tree ast){
		if(ast != null)	delegatePackage(ast);
	}
	
	private void createClassObject(Tree ast){
		if(ast != null) delegateClass(ast); 
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
		
	private boolean hasMethodes(Tree classTree) {
		if (classTree != null) {
			if (classTree.getChildCount() == 3) {
				if (classTree.getChild(2).getChildCount() > 0){
					return true;
				}
			}
		}
		return false;
	}

	private void delegateMethodTree(Tree methodTree) {
		JavaMethodGenerator javaMethodGenerator = new JavaMethodGenerator();
		javaMethodGenerator.setFamixClassObject(famixClassObject);
		famixMethodObject = javaMethodGenerator.generateFamix((CommonTree) methodTree);
		
		famixObjects.add(famixMethodObject);

	}

	public void delegatePackage(Tree packageTree){
		JavaPackageGenerator javaPackageGenerator = new JavaPackageGenerator();
		famixPackageObject = javaPackageGenerator.generateFamix((CommonTree) packageTree);
		famixObjects.add(famixPackageObject);
	}
	
	public void delegateClass(Tree classTree){
		JavaClassGenerator javaClassGenerator = new JavaClassGenerator();

		javaClassGenerator.setPackageNameAndUniqueName(famixPackageObject.getName(), famixPackageObject.getUniqueName());
		famixClassObject = javaClassGenerator.generateFamix((CommonTree) classTree);
		famixObjects.add(famixClassObject);
		Tree classTopLevelScopeTreeChild = ((BaseTree) classTree).getFirstChildWithType(JavaParser.CLASS_TOP_LEVEL_SCOPE);
		if(classTopLevelScopeTree != null){
			classTopLevelScopeTree = classTopLevelScopeTreeChild;
		}
	}
	
	public void delegateImport(CommonTree importTree, String belongsToClass){
		JavaImportGenerator javaImportGenerator = new JavaImportGenerator();
		FamixObject importObject = javaImportGenerator.generateFamixImport(importTree, belongsToClass);
		famixObjects.add(importObject);
	}
	

	public void delegateAttribute(Tree scopeTree, FamixClass classObject){
		@SuppressWarnings("unused")
		JavaAttributeGenerator javaAttributeGenerator = new JavaAttributeGenerator();
		//TODO Implement Attribute-generator
		//javaAttributeGenerator.generateFamix(scopeTree);
	}

	public void delegateTopLevelScopeTree(Tree scopeTree){
		System.out.println(scopeTree.getChildCount());
	}	
	
	private boolean hasType(CommonTree completeTree, int nodeType){
		return completeTree.getFirstChildWithType(nodeType) != null;
	}
}
