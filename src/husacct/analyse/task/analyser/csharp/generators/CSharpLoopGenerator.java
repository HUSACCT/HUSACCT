package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.analyse.infrastructure.antlr.java.JavaParser;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

public class CSharpLoopGenerator extends CSharpGenerator {

    private String packageAndClassName;
    private String belongsToMethod;
    private String variableTypeForLoop;
    
    CSharpAttributeAndLocalVariableGenerator csAttribueteAndLocalVariableGenerator = new CSharpAttributeAndLocalVariableGenerator();
	CSharpInvocationGenerator csharpInvocationGenerator = new CSharpInvocationGenerator(this.packageAndClassName);
    CSharpBlockScopeGenerator csBlockScopeGenerator;

    public void generateToDomainFromLoop(CommonTree loopTree, String belongsToClass, String belongsToMethod) {
        packageAndClassName = belongsToClass;
        this.belongsToMethod = belongsToMethod;
        variableTypeForLoop = "";
        
        switch(loopTree.getType()){
            case CSharpParser.FOR:
            case CSharpParser.WHILE:
			case CSharpParser.DO:
                walkForOrWhileTree(loopTree);
                break;
            case CSharpParser.FOREACH:
                walkForEachTree(loopTree);
                break;
            default:
                System.err.println(this.getClass().getName() + " no supported loop");
        }
    }

    private void walkForOrWhileTree(CommonTree loopTree) {
        for(int i = 0; i < loopTree.getChildCount(); i++){
            CommonTree child = (CommonTree) loopTree.getChild(i);
            boolean walkThroughChildren = true;
            
            switch(child.getType()){
                case CSharpParser.LOCAL_VARIABLE_DECLARATOR:
                	delegateLocalVariable(child);
                    csAttribueteAndLocalVariableGenerator.generateLocalVariableToDomain(child, packageAndClassName, this.belongsToMethod);
		            walkThroughChildren = false;
                    break;
                case CSharpParser.UNARY_EXPRESSION:
                	delegateInvocation(child);
		            walkThroughChildren = false;
                    break;
                case CSharpParser.BLOCK:
                	delegateBlockScope(child);
		            walkThroughChildren = false;
                    break;
            }
	        if (walkThroughChildren) {
	        	walkForEachTree(child);
	        }
        }
    }

   private void delegateLocalVariable(CommonTree child) {
		if (child.toStringTree().contains("= >")) {
			CSharpLamdaGenerator csLamdaGenerator = new CSharpLamdaGenerator();
			csLamdaGenerator.delegateLambdaToBuffer((CommonTree)child, packageAndClassName, belongsToMethod);
		} else {
			CSharpAttributeAndLocalVariableGenerator csharpLocalVariableGenerator = new CSharpAttributeAndLocalVariableGenerator();
			csharpLocalVariableGenerator.generateLocalVariableToDomain(child, this.packageAndClassName, this.belongsToMethod);
		}
	}

	private void delegateInvocation(Tree tree) {
		csharpInvocationGenerator.generateInvocationToDomain((CommonTree) tree, this.belongsToMethod);
	}

   private void delegateBlockScope(CommonTree child) {
	   csBlockScopeGenerator = new CSharpBlockScopeGenerator();
     csBlockScopeGenerator.walkThroughBlockScope(child, this.packageAndClassName, this.belongsToMethod);
	}

	private void walkForEachTree(CommonTree loopTree) {
        for(int childCount = 0; childCount < loopTree.getChildCount(); childCount++){
			CommonTree child = (CommonTree) loopTree.getChild(childCount);
            boolean walkThroughChildren = true;
			
			switch (child.getType()) {
	            case CSharpParser.TYPE:
	            	String foundType = csharpInvocationGenerator.getCompleteToString((CommonTree) child);
	                if (foundType != null) {
	                    this.variableTypeForLoop = foundType;
	                }
		            walkThroughChildren = false;
	                break;
	            case CSharpParser.IDENTIFIER:
	            	String foundName = csharpInvocationGenerator.getCompleteToString((CommonTree) child);
	            	int lineNumber = 0;
	                if (foundName != null) {
	                    lineNumber = child.getLine();
	                } else {
	                	foundName = "";
	               	}
	                if (!foundName.trim().equals("") && !variableTypeForLoop.trim().equals("")) {
	                	csAttribueteAndLocalVariableGenerator.generateLocalVariableForEachLoopToDomain(packageAndClassName, belongsToMethod, foundName, variableTypeForLoop, lineNumber);
	                	variableTypeForLoop = "";
	                }
		            walkThroughChildren = false;
	                break;
				case CSharpParser.UNARY_EXPRESSION: case CSharpParser.CAST_EXPRESSION:
					delegateInvocation(child);
		            walkThroughChildren = false;
					break;
				case CSharpParser.BLOCK:
					delegateBlockScope(child);
		            walkThroughChildren = false;
					break;
			}
	        if (walkThroughChildren) {
	        	walkForEachTree(child);
	        }
		}
    }

}
