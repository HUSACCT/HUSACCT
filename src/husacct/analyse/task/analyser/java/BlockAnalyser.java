package husacct.analyse.task.analyser.java;

import java.util.List;

import husacct.analyse.infrastructure.antlr.java.Java7Parser;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.BlockContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.BlockStatementContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.CatchClauseContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.ExpressionContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.ResourceContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.StatementContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.SwitchBlockStatementGroupContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.SwitchLabelContext;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.log4j.Logger;

public class BlockAnalyser extends JavaGenerator {

    private String belongsToClass;
    private String belongsToMethod;
    private String to = "";
    private int lineNumber;

    private Logger logger = Logger.getLogger(BlockAnalyser.class);

    public BlockAnalyser(BlockContext block, String belongsToClass, String belongsToMethod) {
        this.belongsToClass = belongsToClass;
        this.belongsToMethod = belongsToMethod;
    	try {
	        for (BlockStatementContext blockStatement : block.blockStatement()) {
	            
	            // Test helpers
	        	if (belongsToClass.equals("domain.direct.violating.CallConstructor")) {
    					boolean breakpoint = true;
	        	} //
	            
	        	if (blockStatement.localVariableDeclarationStatement() != null && 
	        			blockStatement.localVariableDeclarationStatement().localVariableDeclaration() != null) {
	    			VariableAnalyser variableAnalyser = new VariableAnalyser(this.belongsToClass);
	    			variableAnalyser.analyseLocalVariable(blockStatement.localVariableDeclarationStatement().localVariableDeclaration(), this.belongsToMethod);
	        	}
	        	if (blockStatement.typeDeclaration() != null) {
	        		TypeDeclarationAnalyser typeDeclarationAnalyser = new TypeDeclarationAnalyser();
	            	typeDeclarationAnalyser.analyseNestedTypeDeclaration(blockStatement.typeDeclaration(), belongsToClass);
	        	}
	        	if (blockStatement.statement() != null) {
	        		analyseStatement(blockStatement.statement());
	        	}
	        }
    	}
		catch (Exception e) {
			logger.warn(" Exception while processing: " + belongsToClass + " " + e.getMessage());
		}
    }
    
    public void analyseStatement(StatementContext statement) {
		String rs = statement.getText();
		if (statement.block() != null) {
			new BlockAnalyser(statement.block(), this.belongsToClass, this.belongsToMethod);
		} 
		if (!statement.statement().isEmpty()) {
			for (StatementContext subStatement : statement.statement()) {
				analyseStatement(subStatement);
			}
		}
		if (!statement.expression().isEmpty()) {
			for (ExpressionContext subExpression : statement.expression()) {
				analyseExpression(subExpression);
			}
		}
		if ((statement.statementExpression() != null) && (statement.statementExpression().expression() != null)) {
			analyseExpression(statement.statementExpression().expression());
		}
		if ((statement.parExpression() != null) && (statement.parExpression().expression() != null)) {
			analyseExpression(statement.parExpression().expression());
		}
		if (statement.forControl() != null) {
			if (statement.forControl().expression() != null) {
				analyseExpression(statement.forControl().expression());
			} else if (statement.forControl().enhancedForControl() != null) {
				if (statement.forControl().enhancedForControl().typeType() != null) {
	    			VariableAnalyser variableAnalyser = new VariableAnalyser(this.belongsToClass);
	    			variableAnalyser.analyseForControlVariable(statement.forControl().enhancedForControl(), this.belongsToMethod);
				}
				if (statement.forControl().enhancedForControl().expression() != null) {
					analyseExpression(statement.forControl().enhancedForControl().expression());
				}
			}
		}
		if (!statement.catchClause().isEmpty()) {
			for (CatchClauseContext catchClause : statement.catchClause()) {
    			VariableAnalyser variableAnalyser = new VariableAnalyser(this.belongsToClass);
    			variableAnalyser.analyseCatchClauseVariable(catchClause, this.belongsToMethod);
    			if (catchClause.block() != null) {
    				new BlockAnalyser(catchClause.block(), this.belongsToClass, this.belongsToMethod);
    			}
			}
		}
		if (statement.finallyBlock() != null) {
			if (statement.finallyBlock().block() != null){
				new BlockAnalyser(statement.finallyBlock().block(), this.belongsToClass, this.belongsToMethod);
			}
		}
		if ((statement.resourceSpecification() != null) && !statement.resourceSpecification().isEmpty()) {
			for (ResourceContext resource : statement.resourceSpecification().resources().resource()) {
    			VariableAnalyser variableAnalyser = new VariableAnalyser(this.belongsToClass);
    			variableAnalyser.analyseResourceVariable(resource, this.belongsToMethod);
				if (resource.expression() != null) {
					analyseExpression(resource.expression());
				}
			}
		}
		if (!statement.switchBlockStatementGroup().isEmpty()) {
			for (SwitchBlockStatementGroupContext switchBlockStatementGroup : statement.switchBlockStatementGroup()) {
				if (switchBlockStatementGroup.switchLabel() != null) {
					analyseSwitchLabel(switchBlockStatementGroup.switchLabel());
				}
			}
		}
		if (!statement.switchLabel().isEmpty()) {
			analyseSwitchLabel(statement.switchLabel());
		}
		if (statement.Identifier() != null) {
			this.lineNumber = statement.start.getLine();
			this.to = statement.Identifier().getText();
			addAssociationToModel(); 
		}
    }

    private void analyseExpression(ExpressionContext expression) {
		ExpressionAnalyser expressionAnalyser = new ExpressionAnalyser(belongsToClass, belongsToMethod);
		expressionAnalyser.analyseExpression(expression);
    }


    private void addAssociationToModel() {
        if ((to != null) && !to.trim().equals("") && !SkippedTypes.isSkippable(to)) {
            modelService.createVariableInvocation(belongsToClass, to, lineNumber, belongsToMethod);
        }
        to = "";
        lineNumber = 0;
    }
    
    private void analyseSwitchLabel(List<SwitchLabelContext> switchLabelList) {
    	for(SwitchLabelContext switchLabel : switchLabelList) {
    		if((switchLabel.constantExpression() != null) && (switchLabel.constantExpression().expression() != null)) {
    			analyseExpression(switchLabel.constantExpression().expression());
    		}
    		if((switchLabel.enumConstantName() != null) && (switchLabel.enumConstantName().Identifier() != null)) {
    			this.lineNumber = switchLabel.enumConstantName().start.getLine();
    			this.to = switchLabel.enumConstantName().Identifier().getText();
    			addAssociationToModel(); 
    		}
    	}
    }
	    


    
    // Verwijder na afronding J7
    VariableAnalyser javaLocalVariableGenerator= new VariableAnalyser(belongsToClass);
    CommonTree tree;

    private void walkThroughBlockScope(Tree tree) {
	    for (int i = 0; i < tree.getChildCount(); i++) {
	    	CommonTree child = (CommonTree) tree.getChild(i);
	        int treeType = child.getType();
	        boolean walkThroughChildren = true;

	        /* Test helper
	       	if (this.belongsToClass.contains("org.eclipse.ui.internal.views.markers.MarkerFieldFilterGroup")){
	    		if (child.getLine() == 522) {
	    				boolean breakpoint1 = true;
	    		}
	    	} */ 

/*	        switch(treeType) {
	        case Java7Parser.CLASS_CONSTRUCTOR_CALL: case Java7Parser.SUPER_CONSTRUCTOR_CALL:
            	detectAndProcessAnonymousClass(child);
	            delegateInvocation(child, "invocConstructor");
	            walkThroughChildren = false;
	            break;
	        case Java7Parser.RETURN: case Java7Parser.CAST_EXPR: case Java7Parser.ASSIGN: case Java7Parser.NOTEQUAL: case Java7Parser.EQUAL: case Java7Parser.GE: 
        	case Java7Parser.LE: case Java7Parser.LT: case Java7Parser.GT:
	            delegateInvocation(child, "AccessVariable");
	            walkThroughChildren = false;
	            break;
	        case Java7Parser.METHOD_CALL: 
            	detectAndProcessAnonymousClass(child);
	            delegateInvocation(child, "invocMethod");
	            walkThroughChildren = false;
	            break;
	        case Java7Parser.THROW: case Java7Parser.CATCH: case Java7Parser.THROWS:
	            delegateException(child);
	            walkThroughChildren = false;
	        	break;
	        case Java7Parser.FOR_EACH: case Java7Parser.FOR: case Java7Parser.WHILE:
	            delegateLoop(child);
	            walkThroughChildren = false;
	            break;
		    }
	        if (walkThroughChildren) {
	        	walkThroughBlockScope(child);
	        } else {
	        	int ttype = child.getType();
	        	if ((ttype != Java7Parser.FOR_EACH) && (ttype != Java7Parser.FOR) && (ttype != Java7Parser.WHILE)) {
		        	Tree descendant = child.getFirstChildWithType(Java7Parser.BLOCK_SCOPE);
			        if ((descendant != null) || (child.getType() == Java7Parser.BLOCK_SCOPE)) {
			        	walkThroughBlockScope(child);
			        }
	        	}
	        }
*/	    }
    }


    private void delegateInvocation(Tree treeNode, String type) {
        StatementAnalyser javaInvocationGenerator = new StatementAnalyser(this.belongsToClass);
        if (type.equals("invocConstructor")) {
            javaInvocationGenerator.generateConstructorInvocToDomain((CommonTree) treeNode, this.belongsToMethod);
        } else if (type.equals("invocMethod")) {
            javaInvocationGenerator.generateMethodInvocToDomain((CommonTree) treeNode, this.belongsToMethod);
        } else if (type.equals("AccessVariable")) {
            //javaInvocationGenerator.generatePropertyOrFieldInvocToDomain((CommonTree) treeNode, this.belongsToMethod);
        }
    }

    private void delegateException(Tree exceptionTree) {
        JavaExceptionGenerator exceptionGenerator = new JavaExceptionGenerator();
        exceptionGenerator.generateToDomain((CommonTree) exceptionTree, this.belongsToClass);
    }

    private void delegateLoop(Tree tree) {
        JavaLoopGenerator forEachLoopGenerator = new JavaLoopGenerator();
        forEachLoopGenerator.generateToDomainFromLoop((CommonTree) tree, this.belongsToClass, this.belongsToMethod);
    }
    
    private void detectAndProcessAnonymousClass(Tree tree) {
    	int treeType;
    	for (int i = 0; i < tree.getChildCount(); i++) {
	    	CommonTree child = (CommonTree) tree.getChild(i);
	        treeType = child.getType();
        	if (treeType == Java7Parser.VOID) { //CLASS_TOP_LEVEL_SCOPE) {
        		walkThroughBlockScope(child);
        		break;
        	}
        	detectAndProcessAnonymousClass(child);
	    }
    }

}