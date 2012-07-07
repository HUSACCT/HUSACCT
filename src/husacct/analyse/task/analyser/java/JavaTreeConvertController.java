package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser; 
import husacct.analyse.infrastructure.antlr.java.JavaParser.compilationUnit_return;

import org.antlr.runtime.RecognitionException; 
import org.antlr.runtime.tree.CommonTree; 
import org.antlr.runtime.tree.Tree;
import org.apache.log4j.Logger;

class JavaTreeConvertController { 

	private String theClass = null; 
	private String thePackage = null; 
	private String currentClass = null; 
	private String parentClass = null; 
	private int classCount = 0; 
	private Logger logger = Logger.getLogger(JavaTreeConvertController.class);
	
	private JavaPackageGenerator javaPackageGenerator;
	private JavaClassGenerator javaClassGenerator;
	private JavaInterfaceGenerator javaInterfaceGenerator;
	private JavaAnnotationGenerator javaAnnotationGenerator;
	private JavaInheritanceDefinitionGenerator javaInheritanceDefinitionGenerator;
	private JavaImplementsDefinitionGenerator implementsGenerator;
	private JavaImportGenerator javaImportGenerator;
	private JavaMethodGeneratorController methodGenerator;
	private JavaAttributeAndLocalVariableGenerator javaAttributeGenerator;
	
	public JavaTreeConvertController(){
		javaPackageGenerator = new JavaPackageGenerator(); 
		methodGenerator = new JavaMethodGeneratorController();
		javaAttributeGenerator = new JavaAttributeAndLocalVariableGenerator();
		javaImportGenerator = new JavaImportGenerator(); 
		javaInheritanceDefinitionGenerator = new JavaInheritanceDefinitionGenerator();
		implementsGenerator = new JavaImplementsDefinitionGenerator();
	}

	public void delegateASTToGenerators(JavaParser javaParser) throws RecognitionException { 
		compilationUnit_return compilationUnit = javaParser.compilationUnit(); 
		CommonTree compilationUnitTree = (CommonTree) compilationUnit.getTree();
		createClassInformation(compilationUnitTree); 

		if(this.theClass != null){
			delegateASTToGenerators(compilationUnitTree); 
		}
	} 

	private void createClassInformation(CommonTree completeTree){ 
		CommonTree packageTree = (CommonTree) completeTree.getFirstChildWithType(JavaParser.PACKAGE); 
		if(hasPackageElement(completeTree)){ 
			delegatePackage(packageTree); 
		} else {
			this.thePackage = ""; 
		}

		CommonTree classTree = (CommonTree) completeTree.getFirstChildWithType(JavaParser.CLASS);
		if(!isTreeAvailable(classTree)){
			classTree = (CommonTree) completeTree.getFirstChildWithType(JavaParser.INTERFACE);
		}
		if(!isTreeAvailable(classTree)){
			classTree = (CommonTree) completeTree.getFirstChildWithType(JavaParser.AT);
		}
		if(!isTreeAvailable(classTree)){
			classTree = (CommonTree) completeTree.getFirstChildWithType(JavaParser.ENUM);
		}

		
		if(isTreeAvailable(classTree)){
			int classType = classTree.getType();
			switch(classType){
				case JavaParser.CLASS:
					this.theClass = this.currentClass = delegateClass(classTree, false);
					break;
				case JavaParser.INTERFACE:
					this.theClass = this.currentClass = delegateInterface(classTree);
					break;
				case JavaParser.AT:
					this.theClass = this.currentClass = delegateAnnotation(classTree);
					break;
				case JavaParser.ENUM:
					this.theClass = this.currentClass = delegateClass(classTree, false);
					break;
				default:
					this.warnNotSupportedClassType(classType);
			}
		} else {
			int typeid = 0;
			CommonTree warnTree = (CommonTree) completeTree.getChild(2);
			if(isTreeAvailable(warnTree)){
				typeid = warnTree.getType();
			}
			this.warnNotSupportedClassType(typeid);
		}
	} 
	
	private void warnNotSupportedClassType(int typeId){
		String warnMessage = "Detected a not supported type";
		if(typeId != 0){
			warnMessage += " [Probably type id " + typeId + " ]";
		}
		logger.warn(warnMessage);
	}

	private void delegateASTToGenerators(CommonTree tree) { 
		if (isTreeAvailable(tree)) { 
			for (int i = 0; i < tree.getChildCount(); i++) { 
				CommonTree treeNode = (CommonTree) tree.getChild(i); 
				int nodeType = treeNode.getType(); 
				
				switch(nodeType){
					case JavaParser.CLASS:
						if(classCount > 0) { 
							CommonTree innerClassTree = (CommonTree)treeNode; 
							this.parentClass = currentClass; 
							this.currentClass = delegateClass(innerClassTree, true);
							delegateASTToGenerators(innerClassTree); 
							this.currentClass = parentClass;
						} else { 
							classCount++; 
						}
						break;
					case JavaParser.IMPORT:
						if(nodeType == JavaParser.IMPORT){ 
							delegateImport((CommonTree)treeNode); 
							deleteTreeChild(treeNode);
						}
						break;
					case JavaParser.IMPLEMENTS_CLAUSE:
							delegateImplementsDefinition((CommonTree)treeNode); 
							deleteTreeChild(treeNode); 
						break;
					case JavaParser.EXTENDS_CLAUSE:
							delegateInheritanceDefinition((CommonTree)treeNode); 
							deleteTreeChild(treeNode);
						break;
					case JavaParser.VAR_DECLARATION:
							delegateAttribute(treeNode); 
							deleteTreeChild(treeNode); 
						break;
					case JavaParser.FUNCTION_METHOD_DECL:
					case JavaParser.CONSTRUCTOR_DECL:
					case JavaParser.VOID_METHOD_DECL:
							delegateMethod(treeNode);
							deleteTreeChild(treeNode); 
						break;
						
				}
				delegateASTToGenerators((CommonTree) tree.getChild(i));
			} 
		} 
	} 

	private void deleteTreeChild(Tree treeNode){ 
		for (int child = 0 ; child < treeNode.getChildCount();){ 
			treeNode.deleteChild(treeNode.getChild(child).getChildIndex()); 
		} 
	} 

	private void delegatePackage(CommonTree packageTree){ 
		this.thePackage = javaPackageGenerator.generateModel(packageTree);
		javaClassGenerator = new JavaClassGenerator(thePackage);
		javaInterfaceGenerator = new JavaInterfaceGenerator(thePackage);
		javaAnnotationGenerator = new JavaAnnotationGenerator(thePackage);
	} 

	private String delegateClass(CommonTree classTree, boolean isInnerClass){ 
		String analysedClass; 
		if(isInnerClass){
			analysedClass = javaClassGenerator.generateToModel(classTree, parentClass); 
		} else {
			analysedClass = javaClassGenerator.generateToDomain(classTree); 
		}
		return analysedClass; 
	} 

	private String delegateInterface(CommonTree interfaceTree){  
		return javaInterfaceGenerator.generateToDomain(interfaceTree); 
	} 

	private String delegateAnnotation(CommonTree annotationTree){
		return javaAnnotationGenerator.generateToDomain(annotationTree);
	}

	private void delegateImplementsDefinition(CommonTree treeNode) {
		implementsGenerator.generateToDomain(treeNode, this.theClass); 
	} 

	private void delegateInheritanceDefinition(CommonTree treeNode) {
		javaInheritanceDefinitionGenerator.generateToDomain(treeNode, this.theClass);
	} 
	
	private void delegateImport(CommonTree importTree){ 
		javaImportGenerator.generateToDomain(importTree, this.currentClass); 
	}

	private void delegateAttribute(CommonTree attributeTree){ 
		javaAttributeGenerator.generateAttributeToDomain(attributeTree, this.currentClass); 
	} 

	private void delegateMethod(CommonTree methodTree){ 
		methodGenerator.delegateMethodBlock(methodTree, this.currentClass); 
	} 

	private boolean hasPackageElement(CommonTree tree){ 
		return tree.getFirstChildWithType(JavaParser.PACKAGE) != null; 
	}
	
	private boolean isTreeAvailable(Tree tree){
		if(tree != null) return true; 
		return false;
	}
	
}