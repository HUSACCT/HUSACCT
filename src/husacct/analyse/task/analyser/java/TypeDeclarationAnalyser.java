package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.Java7Parser;
import husacct.analyse.task.analyser.VisibilitySet;

import org.antlr.v4.runtime.tree.ParseTree;

class TypeDeclarationAnalyser extends JavaGenerator {

    private String name = "";
    private String uniqueName = "";
    private String belongsToPackage;
    private String belongsToClass = "";
    private boolean isAbstract = false;
    private String visibility = "";
    private String sourceFilePath;
    private int nrOfLinesOfCode;
    
    private boolean isNestedClass = false;
    private boolean isInterface = false;
    private boolean isEnumeration = false;

    public TypeDeclarationAnalyser(String uniquePackageName, String sourceFilePath, int linesOfCode, boolean isNestedClass, String parentClassName) {
        this.belongsToPackage = uniquePackageName;
        this.sourceFilePath = sourceFilePath;
        this.isNestedClass = isNestedClass;
        this.belongsToClass = parentClassName;
        if (isNestedClass) {
        	this.nrOfLinesOfCode = 0;
        } else {
            this.nrOfLinesOfCode = linesOfCode;
        }
    }

	@Override 
	public void enterClassOrInterfaceModifier(Java7Parser.ClassOrInterfaceModifierContext ctx) {
		if (ctx != null) {
			// Determine annotation
			if (ctx.annotation() != null) {
				new JavaAnnotationGenerator(ctx.annotation(), this.uniqueName);
			}
			
			// Determine this.isAbstract
			for (ParseTree child : ctx.children) {
				String modifier = child.getText();
				if (modifier.equals("abstract")) {
					this.isAbstract = true;
				}
			}
		}
			
		//Determine this.visibility
        if (ctx == null || ctx.getChildCount() < 1) {
            this.visibility = VisibilitySet.DEFAULT.toString();
        } else {
			for (ParseTree child : ctx.children) {
				String modifier = child.getText();
	            if (VisibilitySet.isValidVisibillity(modifier)) {
	            	this.visibility = modifier;
	            } else {
	                this.visibility = VisibilitySet.DEFAULT.toString();
	            }
			}
 		}
	}
	
	@Override 
	public void enterTypeDeclaration(Java7Parser.TypeDeclarationContext ctx) {
		if (ctx != null) {
			if (ctx.classDeclaration() != null) {
				this.name  = ctx.classDeclaration().Identifier().getText();
			} else if (ctx.interfaceDeclaration() != null) {
				isInterface = true;
				this.name  = ctx.interfaceDeclaration().Identifier().getText();
			} else if (ctx.enumDeclaration() != null) {
				isEnumeration = true;
				this.name  = ctx.enumDeclaration().Identifier().getText();
			} else if (ctx.annotationTypeDeclaration() != null) {
				isInterface = true; // Annotation is handled as interface
				this.name  = ctx.annotationTypeDeclaration().Identifier().getText();
			}
			// Determine uniqueName
			if (isNestedClass) {
	            this.uniqueName = belongsToClass + "." + name;
	        } else {
	            if (belongsToPackage.equals("")) {
	                this.uniqueName = name;
	            } else {
	                this.uniqueName = belongsToPackage + "." + name;
	            }
	            this.belongsToClass = "";
	        }

			// Add type to Model
	        if (!name.equals("") && !belongsToPackage.equals("")) {
	        	modelService.createClass(sourceFilePath, nrOfLinesOfCode, uniqueName, name, belongsToPackage, isAbstract, isNestedClass, belongsToClass, visibility, isInterface, isEnumeration);
	        }
		}
	}

	@Override 
	public void enterClassDeclaration(Java7Parser.ClassDeclarationContext ctx) {
		// Determine extends clause
		if (ctx.typeType() != null) {
			if(!ctx.typeType().getText().equals("")) {
			    String from = this.uniqueName;
			    String to = ctx.typeType().getText();
			    int lineNumber = ctx.typeType().start.getLine();
		        if (!SkippedTypes.isSkippable(to)) {
		            modelService.createInheritanceDefinition(from, to, lineNumber);
		        }
			}
		}
		
		// Determine implements clause
		if (ctx.typeList() != null) {
			for (ParseTree implementsType : ctx.typeList().children) {
			    String to = implementsType.getText();
				if(!implementsType.getText().equals("")) {
				    String from = this.uniqueName;
				    int lineNumber = ctx.typeList().start.getLine();
			        if (!SkippedTypes.isSkippable(to)) {
			            modelService.createImplementsDefinition(from, to, lineNumber);
			        }
				}
			}
		}

	}

	
	public String getUniqueNameOfType() {
        return uniqueName;
	}
	
}
