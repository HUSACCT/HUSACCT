package husacct.analyse.task.analyser.java;

import husacct.analyse.infrastructure.antlr.java.Java7Parser;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.TypeDeclarationContext;
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

	public void analyseTypeDeclaration(TypeDeclarationContext typeDeclaration) {
		if (typeDeclaration != null) {
			if (typeDeclaration.classDeclaration() != null) {
				this.name  = typeDeclaration.classDeclaration().Identifier().getText();
				determineUniqueName();
				analyseClassDeclaration(typeDeclaration.classDeclaration());
			} else if (typeDeclaration.interfaceDeclaration() != null) {
				isInterface = true;
				this.name  = typeDeclaration.interfaceDeclaration().Identifier().getText();
				determineUniqueName();
				analyseInterfaceDeclaration(typeDeclaration.interfaceDeclaration());
			} else if (typeDeclaration.enumDeclaration() != null) {
				isEnumeration = true;
				this.name  = typeDeclaration.enumDeclaration().Identifier().getText();
				determineUniqueName();
				analyseEnumDeclaration(typeDeclaration.enumDeclaration());
			} else if (typeDeclaration.annotationTypeDeclaration() != null) {
				isInterface = true; // Annotation is handled as interface
				determineUniqueName();
				this.name  = typeDeclaration.annotationTypeDeclaration().Identifier().getText();
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
			
			processModifiers(typeDeclaration);

			// Add type to Model
	        if (!name.equals("") && !belongsToPackage.equals("")) {
	        	modelService.createClass(sourceFilePath, nrOfLinesOfCode, uniqueName, name, belongsToPackage, isAbstract, isNestedClass, belongsToClass, visibility, isInterface, isEnumeration);
	        }
		}
	}

	private void determineUniqueName() {
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
	}

	
	private void processModifiers(TypeDeclarationContext typeDeclaration) {
		if (typeDeclaration.classOrInterfaceModifier() != null) {
			// Detect and creates Annotation dependencies
	    	int size = typeDeclaration.classOrInterfaceModifier().size();
	    	for (int i = 0; i < size; i++) {
				if (typeDeclaration.classOrInterfaceModifier(i).annotation() != null) {
					new AnnotationAnalyser(typeDeclaration.classOrInterfaceModifier(i).annotation(), this.uniqueName);
				}
			}
		
			// Determine this.isAbstract
			for (ParseTree child : typeDeclaration.classOrInterfaceModifier()) {
				String modifier = child.getText();
				if (modifier.equals("abstract")) {
					this.isAbstract = true;
				}
			}
			
			//Determine this.visibility
	        this.visibility = VisibilitySet.DEFAULT.toString();
			for (ParseTree child : typeDeclaration.classOrInterfaceModifier()) {
				String modifier = child.getText();
	            if (VisibilitySet.isValidVisibillity(modifier)) {
	            	this.visibility = modifier;
	            }
			}
		}
	}

	private void analyseClassDeclaration(Java7Parser.ClassDeclarationContext ctx) {
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
		
		// Delegate the body
		if (ctx.classBody() != null) {
			new ClassBodyAnalyser(uniqueName, ctx.classBody());
		}
	}

	private void analyseInterfaceDeclaration(Java7Parser.InterfaceDeclarationContext ctx) {
		// Determine extends clause
		if (ctx.typeList() != null) {
			for (ParseTree extendsType : ctx.typeList().children) {
			    String to = extendsType.getText();
				if(!extendsType.getText().equals("")) {
				    String from = this.uniqueName;
				    int lineNumber = ctx.typeList().start.getLine();
			        if (!SkippedTypes.isSkippable(to)) {
			            modelService.createInheritanceDefinition(from, to, lineNumber);
			        }
				}
			}
		}
	}

	
	private void analyseEnumDeclaration(Java7Parser.EnumDeclarationContext ctx) {
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
