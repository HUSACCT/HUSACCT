package husacct.analyse.task.analyser.java;

import java.util.List;

import husacct.analyse.infrastructure.antlr.java.Java7Parser.ClassBodyContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.MemberDeclarationContext;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.ModifierContext;

class ClassBodyAnalyser extends JavaGenerator {

    private String belongsToClass;
 
    public ClassBodyAnalyser(String belongsToClass, ClassBodyContext classBody) {
        this.belongsToClass = belongsToClass;
        int size = classBody.classBodyDeclaration().size();
        for (int i=0 ; i < size ; i++) {
        	if (classBody.classBodyDeclaration(i).block() != null) {
        		// new BlockAnalyser(belongsToClass, ctx.classBodyDeclaration(i));
        	} else if (classBody.classBodyDeclaration(i).memberDeclaration() != null) {
        		analyseMemberDeclaration(classBody.classBodyDeclaration(i).modifier(), classBody.classBodyDeclaration(i).memberDeclaration());
        	}
        }
    }
    
	private void analyseMemberDeclaration(List<ModifierContext> modifierList, MemberDeclarationContext member) {
		if (member.fieldDeclaration() != null) {
			VariableAnalyser variableAnalyser = new VariableAnalyser(belongsToClass);
			variableAnalyser.generateAttributeToDomain(modifierList, member.fieldDeclaration());
		} else if (member.methodDeclaration() != null) {
			MethodAnalyser methodAnalyser = new MethodAnalyser();
			//methodAnalyser.AnalyseMethod( classDeclaration
		} else if (member.classDeclaration() != null) {
			TypeDeclarationAnalyser nestedTypeAnalyser = new TypeDeclarationAnalyser();
			nestedTypeAnalyser.analyseNestedClassDeclaration(modifierList, member.classDeclaration(), belongsToClass);
		} else if (member.interfaceDeclaration() != null) {
			TypeDeclarationAnalyser nestedTypeAnalyser = new TypeDeclarationAnalyser();
			nestedTypeAnalyser.analyseNestedInterfaceDeclaration(modifierList, member.interfaceDeclaration(), belongsToClass);
		} else if (member.enumDeclaration() != null) {
			TypeDeclarationAnalyser nestedTypeAnalyser = new TypeDeclarationAnalyser();
			nestedTypeAnalyser.analyseNestedEnumDeclaration(modifierList, member.enumDeclaration(), belongsToClass);
		} else if (member.annotationTypeDeclaration() != null) {
			TypeDeclarationAnalyser nestedTypeAnalyser = new TypeDeclarationAnalyser();
			nestedTypeAnalyser.analyseNestedAnnotationTypeDeclaration(modifierList, member.annotationTypeDeclaration(), belongsToClass);
		}

	}
    
}
