package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.JavaParser;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.apache.log4j.Logger;

class JavaMethodGenerator extends JavaGenerator{
	
	private boolean hasClassScope;
	private boolean isAbstract = false;
	private boolean isConstructor;
	private String belongsToClass;

	private String accessControlQualifier;
	private String signature;
	private boolean isPureAccessor;  //TODO Fille isPureAccessor
	private String declaredReturnType;
	private String declareReturnClass; //TODO fill declareReturnClass if necessary

	public String name;
	public String uniqueName;
	private Logger logger = Logger.getLogger(JavaMethodGenerator.class);

	public void generateModelObject(CommonTree methodTree, String className) {
		this.belongsToClass = className;
		fillMethodObject(methodTree);
		createMethodObject();		
	}

	private void fillMethodObject(CommonTree methodTree) {
		if (methodTree == null) {
			return;
		}
		
		if(methodTree.getType() == JavaParser.CONSTRUCTOR_DECL){
			createMethodDetails(methodTree);
			isConstructor = true;
			declaredReturnType = "";
			uniqueName = belongsToClass + "." + signature;
		} else {
			createMethodDetails(methodTree);
			isConstructor = false;
			uniqueName = belongsToClass + "." + signature;
		}
	}

	private void createMethodDetails(Tree tree) {
		if (tree == null) {
			return;
		}
		for (int i = 0; i < tree.getChildCount(); i++) {
			CommonTree childTree = (CommonTree) tree.getChild(i);
			
			switch(childTree.getType()){
				case JavaParser.IDENT:
					name = childTree.getText();
					break;
				case JavaParser.TYPE:
					declaredReturnType = retrieveMethodReturnType(childTree);
					break;
				case JavaParser.MODIFIER_LIST:
					parseScopeProperties(childTree);
					break;
			}						
		}
		fillMethodSignature(tree, 1);
	}

	private String retrieveMethodReturnType(Tree returnTypetree){
		String type = "";
		CommonTree myTree = (CommonTree) returnTypetree;
		try {
			CommonTree typeWrapperTree = (CommonTree) myTree.getFirstChildWithType(JavaParser.QUALIFIED_TYPE_IDENT);
			type = typeWrapperTree.getFirstChildWithType(JavaParser.IDENT).toString();
		} catch (Exception e){
			
		}
		return type;
	}
	
	private boolean parseScopeProperties(CommonTree scopeTree){
		int scopeTreeChildCount = scopeTree.getChildCount();
		
		for(int currentChild = 0; currentChild < scopeTreeChildCount; currentChild++){
			CommonTree currentElement = (CommonTree) scopeTree.getChild(currentChild);
			switch(currentElement.getType()){
				case JavaParser.PUBLIC:
					accessControlQualifier = "public";
					break;
				case JavaParser.PROTECTED:
					accessControlQualifier = "protected";
					break;
				case JavaParser.PRIVATE:
					accessControlQualifier = "private";
					break;
				case JavaParser.ABSTRACT:
					isAbstract = true;
					break;
				case JavaParser.STATIC:
					hasClassScope = true;
					break;
				case JavaParser.AT:
					JavaAnnotationGenerator annotationGenerator = new JavaAnnotationGenerator(this.belongsToClass);
					annotationGenerator.generateMethod(currentElement);
					break;
				default:
					logger.warn("Method type unknown (like private public static abstract) ["+ currentElement.getType() +"]");
					break;
			}
		}	
		
		if (scopeTreeChildCount == 0 || hasClassScope && accessControlQualifier == null){
			accessControlQualifier = "package-private";
		}
		
		return true;
	}

	
	public void fillMethodSignature(Tree tree, int i)
	{	
		CommonTree totalTree = (CommonTree) tree;
		CommonTree methodNameTree = (CommonTree) totalTree.getFirstChildWithType(JavaParser.IDENT);
		
		String methodSignature = "";
		
		if(tree.getType() == JavaParser.CONSTRUCTOR_DECL){
			methodSignature = belongsToClass;
		} else {
			methodSignature = methodNameTree != null ? methodNameTree.toString() : "";
		}
		
		String functionParameterTypes = "";
		
		CommonTree paramListTree = (CommonTree) totalTree.getFirstChildWithType(JavaParser.FORMAL_PARAM_LIST);
		
		if(paramListTree != null && paramListTree.getChildCount() > 0){
			int paramListCount = paramListTree.getChildCount();
			for(int param = 0; param < paramListCount; param++){
				CommonTree declarationTree = (CommonTree) paramListTree.getChild(param);
				CommonTree typeTree = (CommonTree) declarationTree.getFirstChildWithType(JavaParser.TYPE);
				
				CommonTree valueTree = (CommonTree) typeTree.getChild(0);
				switch(valueTree.getType()){
					case JavaParser.QUALIFIED_TYPE_IDENT:
						CommonTree qualifiedTypeTree = (CommonTree) typeTree.getFirstChildWithType(JavaParser.QUALIFIED_TYPE_IDENT);
						functionParameterTypes += ", " + qualifiedTypeTree.getFirstChildWithType(JavaParser.IDENT).toString();
						break;
					case JavaParser.INT:
					case JavaParser.FLOAT:
					case JavaParser.BOOLEAN:
					case JavaParser.DOUBLE:
					case JavaParser.SHORT:
					case JavaParser.LONG:
					case JavaParser.CHAR:
					case JavaParser.BYTE:
						functionParameterTypes += ", " + typeTree.getChild(0).toString();
						break;
					default:
						logger.warn("Cant parse a attribute for methods, unknown property [" + valueTree.getType() + "]");
						break;
				}
			}
		}
		
		if(!methodSignature.equals("")){
			functionParameterTypes = functionParameterTypes != "" ? functionParameterTypes.substring(2) : "";
			methodSignature = methodSignature + "(" + functionParameterTypes + ")";
			signature = methodSignature;
		}
		
		if(tree.getChild(i).getType() == JavaParser.THROWS_CLAUSE){ //156
			JavaExceptionGenerator exceptionGenerator = new JavaExceptionGenerator();
			CommonTree exceptionTree = (CommonTree) tree.getChild(i);
			exceptionGenerator.generateModel(exceptionTree, this.belongsToClass);
		}
		
		
	}

	private void createMethodObject(){
		modelService.createMethod(name, uniqueName, accessControlQualifier, signature, isPureAccessor, declaredReturnType, belongsToClass, isConstructor, isAbstract, hasClassScope);
	}	
}