package husacct.analyse.task.analyser.java;

import java.util.List;

import husacct.analyse.infrastructure.antlr.java.Java7Parser.BlockContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.BlockStatementContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.CatchClauseContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.ExpressionContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.ResourceContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.StatementContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.SwitchBlockStatementGroupContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.SwitchLabelContext;

import org.apache.log4j.Logger;

public class BlockAnalyser extends JavaGenerator {

    private String belongsToClass;
    private String belongsToMethod;
    private String to = "";
    private int lineNumber;

    private Logger logger = Logger.getLogger(BlockAnalyser.class);

    // Analyses a BlockContext, in order of the definition of "block" in Java7.g4.
    public BlockAnalyser(BlockContext block, String belongsToClass, String belongsToMethod) {
        this.belongsToClass = belongsToClass;
        this.belongsToMethod = belongsToMethod;
    	try {
	        for (BlockStatementContext blockStatement : block.blockStatement()) {
	            /* Test helper
	        	if (belongsToClass.equals("domain.direct.violating.DeclarationReturnType")) {
    					boolean breakpoint = true;
	        	} */
	            
	        	if (blockStatement.localVariableDeclarationStatement() != null && 
	        			blockStatement.localVariableDeclarationStatement().localVariableDeclaration() != null) {
	    			VariableAnalyser variableAnalyser = new VariableAnalyser(this.belongsToClass);
	    			variableAnalyser.analyseLocalVariable(blockStatement.localVariableDeclarationStatement().localVariableDeclaration(), this.belongsToMethod);
	        	} else if (blockStatement.statement() != null) {
	        		analyseStatement(blockStatement.statement());
	        	} else if (blockStatement.typeDeclaration() != null) {
	        		TypeDeclarationAnalyser typeDeclarationAnalyser = new TypeDeclarationAnalyser();
	            	typeDeclarationAnalyser.analyseNestedTypeDeclaration(blockStatement.typeDeclaration(), belongsToClass);
	        	}
	        }
    	}
		catch (Exception e) {
			logger.warn(" Exception while processing: " + belongsToClass + " Line: " + block.start.getLine() + " " + e.getMessage());
		}
    }
    
    // Analyses a StatementContext, in order of the definition of "statement" in Java7.g4.
    private void analyseStatement(StatementContext statement) {
		/* Test helper
		String rs = statement.getText();
    	if (belongsToClass.equals("husacct.define.presentation.utils.ReportToHTML") &&
    			(statement.start.getLine() == 34)) {
				boolean breakpoint = true;
    	} */
		
		if (statement.block() != null) {
			new BlockAnalyser(statement.block(), this.belongsToClass, this.belongsToMethod);
		} 
		if ((statement.statement() != null) && !statement.statement().isEmpty()) {
			for (StatementContext subStatement : statement.statement()) {
				analyseStatement(subStatement);
			}
		}
		if ((statement.expression() != null) && !statement.expression().isEmpty()) {
			for (ExpressionContext subExpression : statement.expression()) {
				new ExpressionAnalyser(belongsToClass, belongsToMethod, subExpression);
			}
		}
		if ((statement.statementExpression() != null) && (statement.statementExpression().expression() != null)) {
			new ExpressionAnalyser(belongsToClass, belongsToMethod, statement.statementExpression().expression());
		}
		if ((statement.parExpression() != null) && (statement.parExpression().expression() != null)) {
			new ExpressionAnalyser(belongsToClass, belongsToMethod, statement.parExpression().expression());
		}
		if (statement.forControl() != null) {
			if (statement.forControl().expression() != null) {
				new ExpressionAnalyser(belongsToClass, belongsToMethod, statement.forControl().expression());
				if (statement.forControl().forInit() != null) {
					// To do!
				}
				if (statement.forControl().forUpdate() != null) {
					// To do!
				}
			} else if (statement.forControl().enhancedForControl() != null) {
				if (statement.forControl().enhancedForControl().typeType() != null) {
	    			VariableAnalyser variableAnalyser = new VariableAnalyser(this.belongsToClass);
	    			variableAnalyser.analyseForControlVariable(statement.forControl().enhancedForControl(), this.belongsToMethod);
				}
				if (statement.forControl().enhancedForControl().expression() != null) {
					new ExpressionAnalyser(belongsToClass, belongsToMethod, statement.forControl().enhancedForControl().expression());
				}
			}
		}
		if ((statement.catchClause() != null) && !statement.catchClause().isEmpty()) {
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
		if ((statement.resourceSpecification() != null) && (statement.resourceSpecification().resources() != null) &&
				(statement.resourceSpecification().resources().resource() != null) &&
				!statement.resourceSpecification().resources().resource().isEmpty()) {
			for (ResourceContext resource : statement.resourceSpecification().resources().resource()) {
    			VariableAnalyser variableAnalyser = new VariableAnalyser(this.belongsToClass);
    			variableAnalyser.analyseResourceVariable(resource, this.belongsToMethod);
				if (resource.expression() != null) {
					new ExpressionAnalyser(belongsToClass, belongsToMethod, resource.expression());
				}
			}
		}
		if ((statement.switchBlockStatementGroup() != null) && !statement.switchBlockStatementGroup().isEmpty()) {
			for (SwitchBlockStatementGroupContext switchBlockStatementGroup : statement.switchBlockStatementGroup()) {
				if ((switchBlockStatementGroup.switchLabel() != null) && !statement.switchLabel().isEmpty()) {
					analyseSwitchLabel(switchBlockStatementGroup.switchLabel());
				}
			}
		}
		if ((statement.switchLabel() != null) && !statement.switchLabel().isEmpty()) {
			analyseSwitchLabel(statement.switchLabel());
		}
		if (statement.Identifier() != null) {
			this.lineNumber = statement.start.getLine();
			this.to = statement.Identifier().getText();
			addAssociationToModel(); 
		}
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
    			new ExpressionAnalyser(belongsToClass, belongsToMethod, switchLabel.constantExpression().expression());
    		}
    		if((switchLabel.enumConstantName() != null) && (switchLabel.enumConstantName().Identifier() != null)) {
    			this.lineNumber = switchLabel.enumConstantName().start.getLine();
    			this.to = switchLabel.enumConstantName().Identifier().getText();
    			addAssociationToModel(); 
    		}
    	}
    }
	    
}