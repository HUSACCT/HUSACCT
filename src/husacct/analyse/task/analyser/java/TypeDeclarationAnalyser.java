package husacct.analyse.task.analyser.java;

import java.util.List;

import husacct.analyse.infrastructure.antlr.java.Java7Parser.AnnotationTypeDeclarationContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.ClassDeclarationContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.ClassOrInterfaceModifierContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.EnumDeclarationContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.InterfaceDeclarationContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.ModifierContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.TypeDeclarationContext;
import husacct.analyse.task.analyser.VisibilitySet;

import org.antlr.v4.runtime.tree.ParseTree;

class TypeDeclarationAnalyser extends JavaGenerator {

    private String name = "";
    private String uniqueName = "";
    private String belongsToPackage = "";
    private String belongsToClass = "";
    private boolean isAbstract = false;
    private String visibility = "";
    private String sourceFilePath = "";
    private int nrOfLinesOfCode = 0;  // The top level class gets the total nr of lines of code of the file; inner classes zero.
    
    private boolean isNestedClass = false;
    private boolean isInterface = false;
    private boolean isEnumeration = false;

    public TypeDeclarationAnalyser() {
    	this.sourceFilePath = CompilationUnitAnalyser.getSourceFilePath();
    	this.belongsToPackage = CompilationUnitAnalyser.getPackage();
    }
    
    /**
     * To be used for top level type (class, interface, enum or annotation) within the file.
     * @param uniquePackageName
     * @param typeDeclaration
     * @param sourceFilePath
     * @param linesOfCode
     * @return
     */
    public String analyseTypeDeclaration(TypeDeclarationContext typeDeclaration, int linesOfCode) {
        this.nrOfLinesOfCode = linesOfCode;
        // Determine this.isAbstract and this.visibility
        if (typeDeclaration.classOrInterfaceModifier() != null) {
	        this.visibility = VisibilitySet.DEFAULT.toString();
			for (ClassOrInterfaceModifierContext modifier : typeDeclaration.classOrInterfaceModifier()) {
	            if (VisibilitySet.isValidVisibillity(modifier.getText())) {
	            	this.visibility = modifier.getText();
	            } else if (modifier.getText().equals("abstract")) {
					this.isAbstract = true;
				}
			}
		}
        // Delegate specific types
		if (typeDeclaration != null) {
			if (typeDeclaration.classDeclaration() != null) {
				processClassDeclaration(typeDeclaration.classDeclaration());
			} else if (typeDeclaration.interfaceDeclaration() != null) {
				processInterfaceDeclaration(typeDeclaration.interfaceDeclaration());
			} else if (typeDeclaration.enumDeclaration() != null) {
				processEnumDeclaration(typeDeclaration.enumDeclaration());
			} else if (typeDeclaration.annotationTypeDeclaration() != null) {
				processAnnotationTypeDeclaration(typeDeclaration.annotationTypeDeclaration());
				isInterface = true; // Annotation is handled as interface
				this.name  = typeDeclaration.annotationTypeDeclaration().Identifier().getText();
				determineUniqueName();
		        addTypeToModel();
			}
		}
		// Detect and creates Annotation dependencies
        if (typeDeclaration.classOrInterfaceModifier() != null) {
			for (ClassOrInterfaceModifierContext modifier : typeDeclaration.classOrInterfaceModifier()) {
				if (modifier.annotation() != null) {
					new AnnotationAnalyser(modifier.annotation(), this.uniqueName);
				}
			}
        }
		return uniqueName;
	}

	private void processClassDeclaration(ClassDeclarationContext classDeclaration){
		if (classDeclaration.Identifier() != null) {
			this.name  = classDeclaration.Identifier().getText();
			determineUniqueName();
	        addTypeToModel();
			// Determine extends clause
			if (classDeclaration.typeType() != null) {
				if(!classDeclaration.typeType().getText().equals("")) {
				    String from = this.uniqueName;
				    String to = classDeclaration.typeType().getText();
				    int lineNumber = classDeclaration.typeType().start.getLine();
			        if (!SkippedTypes.isSkippable(to)) {
			            modelService.createInheritanceDefinition(from, to, lineNumber);
			        }
				}
			}
			// Determine implements clause
			if (classDeclaration.typeList() != null) {
				for (ParseTree implementsType : classDeclaration.typeList().children) {
				    String to = implementsType.getText();
					if(!implementsType.getText().equals("") && !implementsType.getText().equals(",")) {
					    String from = this.uniqueName;
					    int lineNumber = classDeclaration.typeList().start.getLine();
				        if (!SkippedTypes.isSkippable(to)) {
				            modelService.createImplementsDefinition(from, to, lineNumber);
				        }
					}
				}
			}
			// Delegate the body
			if (classDeclaration.classBody() != null) {
				TypeBodyAnalyser typeBodyAnalyser = new TypeBodyAnalyser(uniqueName);
				typeBodyAnalyser.analyseClassBody(classDeclaration.classBody());
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
	    }
	}
	
	private void processInterfaceDeclaration(InterfaceDeclarationContext interfaceDeclaration) {
		isInterface = true;
		this.name  = interfaceDeclaration.Identifier().getText();
		determineUniqueName();
        addTypeToModel();
		// Determine extends clause
		if (interfaceDeclaration.typeList() != null) {
			for (ParseTree extendsType : interfaceDeclaration.typeList().children) {
			    String to = extendsType.getText();
				if(!extendsType.getText().equals("")) {
				    String from = this.uniqueName;
				    int lineNumber = interfaceDeclaration.typeList().start.getLine();
			        if (!SkippedTypes.isSkippable(to)) {
			            modelService.createInheritanceDefinition(from, to, lineNumber);
			        }
				}
			}
		}
		// Delegate the body
		if ( interfaceDeclaration.interfaceBody() != null) {
			TypeBodyAnalyser typeBodyAnalyser = new TypeBodyAnalyser(uniqueName);
			typeBodyAnalyser.analyseInterfaceBody(interfaceDeclaration.interfaceBody());
		}
	}

	private void processEnumDeclaration(EnumDeclarationContext enumDeclaration) {
		isEnumeration = true;
		this.name  = enumDeclaration.Identifier().getText();
		determineUniqueName();
        addTypeToModel();
		// Determine implements clause
		if (enumDeclaration.typeList() != null) {
			for (ParseTree implementsType : enumDeclaration.typeList().children) {
			    String to = implementsType.getText();
				if(!implementsType.getText().equals("")) {
				    String from = this.uniqueName;
				    int lineNumber = enumDeclaration.typeList().start.getLine();
			        if (!SkippedTypes.isSkippable(to)) {
			            modelService.createImplementsDefinition(from, to, lineNumber);
			        }
				}
			}
		}
		// Delegate the body
		if ( enumDeclaration.enumBodyDeclarations() != null) {
			TypeBodyAnalyser typeBodyAnalyser = new TypeBodyAnalyser(uniqueName);
			typeBodyAnalyser.analyseEnumBody(enumDeclaration.enumConstants(), enumDeclaration.enumBodyDeclarations());
		}
	}

	private void processAnnotationTypeDeclaration(AnnotationTypeDeclarationContext annotationTypeDeclaration) {
		isInterface = true; // Annotation is handled as interface
		this.name  = annotationTypeDeclaration.Identifier().getText();
		determineUniqueName();
	    addTypeToModel();
		// Delegate the body
		if ( annotationTypeDeclaration.annotationTypeBody() != null) {
			TypeBodyAnalyser typeBodyAnalyser = new TypeBodyAnalyser(uniqueName);
			typeBodyAnalyser.analyseAnnotationBody(annotationTypeDeclaration.annotationTypeBody());
		}
	}

	
	/** 
	 * @param typeDeclaration
	 * @param parentClassName
	 */
	public void analyseNestedTypeDeclaration(TypeDeclarationContext typeDeclaration, String parentClassName) {
        this.isNestedClass = true;
        this.belongsToClass = parentClassName;
        analyseTypeDeclaration(typeDeclaration, 0);
	}

	
	/** 
	 * @param modifierList
	 * @param classDeclaration
	 * @param parentClassName
	 */
	public void analyseNestedClassDeclaration(List<ModifierContext> modifierList, ClassDeclarationContext classDeclaration, String parentClassName) {
        this.isNestedClass = true;
        this.belongsToClass = parentClassName;
        this.visibility = determineVisibility(modifierList);
        this.isAbstract = determineIsAbstract(modifierList);
        if (classDeclaration != null) {
        	processClassDeclaration(classDeclaration);
        }
	}

	/** 
	 * To be used for nested interface.
	 * @param modifierList
	 * @param interfaceDeclaration
	 * @param parentClassName
	 */
	public void analyseNestedInterfaceDeclaration(List<ModifierContext> modifierList, InterfaceDeclarationContext interfaceDeclaration, String parentClassName) {
        this.isNestedClass = true;
        this.belongsToClass = parentClassName;
        this.visibility = determineVisibility(modifierList);
        this.isAbstract = determineIsAbstract(modifierList);
        if (interfaceDeclaration != null) {
        	processInterfaceDeclaration(interfaceDeclaration);
        }
	}

	/** 
	 * @param modifierList
	 * @param enumDeclaration
	 * @param parentClassName
	 */
	public void analyseNestedEnumDeclaration(List<ModifierContext> modifierList, EnumDeclarationContext enumDeclaration, String parentClassName) {
        this.isNestedClass = true;
        this.belongsToClass = parentClassName;
        this.visibility = determineVisibility(modifierList);
        this.isAbstract = determineIsAbstract(modifierList);
        if (enumDeclaration != null) {
        	processEnumDeclaration(enumDeclaration);
        }
	}

	/** 
	 * @param modifierList
	 * @param annotationTypeDeclaration
	 * @param parentClassName
	 */
	public void analyseNestedAnnotationTypeDeclaration(List<ModifierContext> modifierList, AnnotationTypeDeclarationContext annotationTypeDeclaration, String parentClassName) {
        this.isNestedClass = true;
        this.belongsToClass = parentClassName;
        this.visibility = determineVisibility(modifierList);
        this.isAbstract = determineIsAbstract(modifierList);
        if (annotationTypeDeclaration != null) {
        	processAnnotationTypeDeclaration(annotationTypeDeclaration);
        }
	}

	private void addTypeToModel() {
	    if (!name.equals("") && !belongsToPackage.equals("")) {
	    	modelService.createClass(this.sourceFilePath, this.nrOfLinesOfCode, uniqueName, name, belongsToPackage, isAbstract, isNestedClass, belongsToClass, visibility, isInterface, isEnumeration);
	    }
	}

	
}
