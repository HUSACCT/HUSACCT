package husacct.analyse.task.analyse.java.analysing;

import java.util.List;

import husacct.analyse.task.analyse.java.parsing.JavaParser.ImportDeclarationContext;

class ImportAnalyser extends JavaGenerator {

    private String importedType = "";
    private String completeImportDeclaration = "";
    private boolean isCompletePackageImport = false;
    private int lineNumber = 0;

    public ImportAnalyser(String importingType, List<ImportDeclarationContext> importList){
        for (ImportDeclarationContext importDeclaration : importList) {
        	if (importDeclaration.qualifiedName() != null) {
				this.importedType = importDeclaration.qualifiedName().getText();
				this.completeImportDeclaration = importedType;
        	}
	        this.lineNumber = importDeclaration.start.getLine();
	        for (ParseTree child : importDeclaration.children) {
	        	String t = child.getText();
	        	if (t.equals("*")) {
	        		this.isCompletePackageImport = true;
	        		this.completeImportDeclaration = importedType + ".*";
	        	}
	        }
	        if (importingType != null && !importingType.isEmpty() && importedType != null && !importedType.isEmpty()) {
	            modelService.createImport(importingType, importedType, lineNumber, completeImportDeclaration, isCompletePackageImport);
	        }
	        importedType = "";
	        completeImportDeclaration = "";
	        isCompletePackageImport = false;
	        lineNumber = 0;
        }
	}
    
}
