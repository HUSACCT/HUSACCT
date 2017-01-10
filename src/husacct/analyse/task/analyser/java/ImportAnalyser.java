package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.Java7Parser;

import org.antlr.v4.runtime.tree.ParseTree;

class ImportAnalyser extends JavaGenerator {

    private String importingType;
    private String importedType;
    private String completeImportDeclaration;
    private boolean isCompletePackageImport = false;
    private int lineNumber = 0;

    public ImportAnalyser(String importingType){
        this.importingType = importingType;
    }
    
	@Override 
	public void enterImportDeclaration(Java7Parser.ImportDeclarationContext ctx) {
		this.importedType = ctx.qualifiedName().getText();
		this.completeImportDeclaration = importedType;
        this.lineNumber = ctx.start.getLine();
        for (ParseTree child : ctx.children) {
        	String t = child.getText();
        	if (t.equals("*")) {
        		this.isCompletePackageImport = true;
        		this.completeImportDeclaration = importedType + ".*";
        	}
        }
        if (importingType != null && !importingType.isEmpty() && importedType != null && !importedType.isEmpty()) {
            modelService.createImport(importingType, importedType, lineNumber, completeImportDeclaration, isCompletePackageImport);
        }
	}
    
}
