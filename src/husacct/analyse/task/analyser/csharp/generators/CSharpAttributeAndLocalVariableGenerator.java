package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.analyse.infrastructure.antlr.java.JavaParser;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CSharpAttributeAndLocalVariableGenerator extends CSharpGenerator{

	private boolean hasClassScope;
	private boolean isFinal;
	private String name;
	private String accessControlQualifier;
	private String packageAndClassName; 
	private String declareType;  //int, string, CustomClass etc
	private int lineNumber;
	private String belongsToMethod; //alleen voor local variables

	public void generateLocalVariableToDomain(Tree treeNode, String packageAndClassName, String belongsToMethod) {
		this.declareType = "";
		this.packageAndClassName = packageAndClassName;
		this.belongsToMethod = belongsToMethod;
		setMembers(treeNode);
		treeNodeTypeFilter(treeNode);
		createLocalVariableObject();
	}

	public void generateAttributeToDomain(Tree treeNode, String packageAndClassName) {
		this.declareType = "";
		this.belongsToMethod = "";
		this.packageAndClassName = packageAndClassName;
		setMembers(treeNode);
		treeNodeTypeFilter(treeNode);
		createAttributeObject();
	}

    public void generateLocalVariableForEachLoopToDomain(String packageAndClassName, String belongsToMethod, String name, String type, int line) {
        this.packageAndClassName = packageAndClassName;
        this.belongsToMethod = belongsToMethod;
        this.name = name;
        this.declareType = type;
        this.lineNumber = line;
        createLocalVariableObject();
    }
	private void createAttributeObject() {
		if ((declareType != null) && !declareType.equals("")) {
			if(SkippableTypes.isSkippable(declareType)){
				modelService.createAttributeOnly(hasClassScope, isFinal, accessControlQualifier, packageAndClassName, declareType, name, packageAndClassName + "." + name, lineNumber);
	        } else {
	        	modelService.createAttribute(hasClassScope, isFinal, accessControlQualifier, packageAndClassName, declareType, name, packageAndClassName + "." + name, lineNumber);
			}
		}
		declareType = "";
	}

	private void createLocalVariableObject() {
		if ((declareType != null) && !declareType.equals("")) {
			if(SkippableTypes.isSkippable(declareType)){
				modelService.createLocalVariableOnly(packageAndClassName, declareType, name, packageAndClassName + "." + belongsToMethod + "." + name, lineNumber, belongsToMethod);
	        } else {
				modelService.createLocalVariable(packageAndClassName, declareType, name, packageAndClassName + "." + belongsToMethod + "." + name, lineNumber, belongsToMethod);
	        }
		}
		declareType = "";
	}


	private void setMembers(Tree treeNode) { //startFiltering in Java Implementation
		CommonTree currentTree = (CommonTree) treeNode;
		CommonTree identifierTree = (CommonTree) currentTree.getFirstChildWithType(CSharpParser.IDENTIFIER);
		if(identifierTree != null){
			this.name = identifierTree.getText();
			this.lineNumber = identifierTree.getLine();
		}
	}

	private void treeNodeTypeFilter(Tree treeNode) {
		for(int i = 0; i < treeNode.getChildCount(); i++){
            Tree child = treeNode.getChild(i);
            boolean walkThroughChildren = true;
			switch(child.getType()){
			case CSharpParser.MODIFIERS:
				setModifiers(child);
				break;
			case CSharpParser.TYPE:
				setDeclareType(child);		
	            walkThroughChildren = false;
				break;
			case CSharpParser.UNARY_EXPRESSION:
				delegateInvocation(child);
	            walkThroughChildren = false;
				break;
			} 
	        if (walkThroughChildren) {
	        	treeNodeTypeFilter(child);
	        }
		}
	}
	
	private void delegateInvocation(Tree tree) {
		CSharpInvocationGenerator csharpInvocationGenerator = new CSharpInvocationGenerator(this.packageAndClassName);
		csharpInvocationGenerator.generateInvocationToDomain((CommonTree) tree, this.belongsToMethod);
	}

	private void setDeclareType(Tree typeNode) {
		CommonTree typeTree = (CommonTree) typeNode;
		CSharpInvocationGenerator csharpInvocationGenerator = new CSharpInvocationGenerator(this.packageAndClassName);
    	String foundType = csharpInvocationGenerator.getCompleteToString(typeTree);
        if ((foundType != null) && !foundType.equals("")) {
            this.declareType = foundType;
        } 
	}

    private void setModifiers(Tree ModifierList) {
    	accessControlQualifier = "package";
    	hasClassScope = false;
    	isFinal = false;
        for (int i = 0; i < ModifierList.getChildCount(); i++) {
            int treeType = ModifierList.getChild(i).getType();
            switch(treeType)
            {
	            case JavaParser.PRIVATE:
	            	accessControlQualifier = "private";
	            	break;
	            case JavaParser.PUBLIC:
	            	accessControlQualifier = "public";
	            	break;
	            case JavaParser.PROTECTED:
	            	accessControlQualifier = "protected";
	            	break;
	            case JavaParser.STATIC:
	            	hasClassScope = true;
	            	break;
	            case JavaParser.FINAL:
	                isFinal = true;
	            	break;
        	}
        }
    }


}
