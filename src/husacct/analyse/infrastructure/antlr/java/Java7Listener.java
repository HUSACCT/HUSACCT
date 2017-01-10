// Generated from Java7.g4 by ANTLR 4.6

package husacct.analyse.infrastructure.antlr.java;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link Java7Parser}.
 */
public interface Java7Listener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link Java7Parser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void enterCompilationUnit(Java7Parser.CompilationUnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#compilationUnit}.
	 * @param ctx the parse tree
	 */
	void exitCompilationUnit(Java7Parser.CompilationUnitContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#packageDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterPackageDeclaration(Java7Parser.PackageDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#packageDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitPackageDeclaration(Java7Parser.PackageDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#importDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterImportDeclaration(Java7Parser.ImportDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#importDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitImportDeclaration(Java7Parser.ImportDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterTypeDeclaration(Java7Parser.TypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#typeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitTypeDeclaration(Java7Parser.TypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#modifier}.
	 * @param ctx the parse tree
	 */
	void enterModifier(Java7Parser.ModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#modifier}.
	 * @param ctx the parse tree
	 */
	void exitModifier(Java7Parser.ModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#classOrInterfaceModifier}.
	 * @param ctx the parse tree
	 */
	void enterClassOrInterfaceModifier(Java7Parser.ClassOrInterfaceModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#classOrInterfaceModifier}.
	 * @param ctx the parse tree
	 */
	void exitClassOrInterfaceModifier(Java7Parser.ClassOrInterfaceModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#variableModifier}.
	 * @param ctx the parse tree
	 */
	void enterVariableModifier(Java7Parser.VariableModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#variableModifier}.
	 * @param ctx the parse tree
	 */
	void exitVariableModifier(Java7Parser.VariableModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassDeclaration(Java7Parser.ClassDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassDeclaration(Java7Parser.ClassDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#typeParameters}.
	 * @param ctx the parse tree
	 */
	void enterTypeParameters(Java7Parser.TypeParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#typeParameters}.
	 * @param ctx the parse tree
	 */
	void exitTypeParameters(Java7Parser.TypeParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#typeParameter}.
	 * @param ctx the parse tree
	 */
	void enterTypeParameter(Java7Parser.TypeParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#typeParameter}.
	 * @param ctx the parse tree
	 */
	void exitTypeParameter(Java7Parser.TypeParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#typeBound}.
	 * @param ctx the parse tree
	 */
	void enterTypeBound(Java7Parser.TypeBoundContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#typeBound}.
	 * @param ctx the parse tree
	 */
	void exitTypeBound(Java7Parser.TypeBoundContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#enumDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterEnumDeclaration(Java7Parser.EnumDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#enumDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitEnumDeclaration(Java7Parser.EnumDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#enumConstants}.
	 * @param ctx the parse tree
	 */
	void enterEnumConstants(Java7Parser.EnumConstantsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#enumConstants}.
	 * @param ctx the parse tree
	 */
	void exitEnumConstants(Java7Parser.EnumConstantsContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#enumConstant}.
	 * @param ctx the parse tree
	 */
	void enterEnumConstant(Java7Parser.EnumConstantContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#enumConstant}.
	 * @param ctx the parse tree
	 */
	void exitEnumConstant(Java7Parser.EnumConstantContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#enumBodyDeclarations}.
	 * @param ctx the parse tree
	 */
	void enterEnumBodyDeclarations(Java7Parser.EnumBodyDeclarationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#enumBodyDeclarations}.
	 * @param ctx the parse tree
	 */
	void exitEnumBodyDeclarations(Java7Parser.EnumBodyDeclarationsContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#interfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceDeclaration(Java7Parser.InterfaceDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#interfaceDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceDeclaration(Java7Parser.InterfaceDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#typeList}.
	 * @param ctx the parse tree
	 */
	void enterTypeList(Java7Parser.TypeListContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#typeList}.
	 * @param ctx the parse tree
	 */
	void exitTypeList(Java7Parser.TypeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#classBody}.
	 * @param ctx the parse tree
	 */
	void enterClassBody(Java7Parser.ClassBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#classBody}.
	 * @param ctx the parse tree
	 */
	void exitClassBody(Java7Parser.ClassBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#interfaceBody}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceBody(Java7Parser.InterfaceBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#interfaceBody}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceBody(Java7Parser.InterfaceBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#classBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassBodyDeclaration(Java7Parser.ClassBodyDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#classBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassBodyDeclaration(Java7Parser.ClassBodyDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#memberDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMemberDeclaration(Java7Parser.MemberDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#memberDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMemberDeclaration(Java7Parser.MemberDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMethodDeclaration(Java7Parser.MethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMethodDeclaration(Java7Parser.MethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#genericMethodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterGenericMethodDeclaration(Java7Parser.GenericMethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#genericMethodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitGenericMethodDeclaration(Java7Parser.GenericMethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#constructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterConstructorDeclaration(Java7Parser.ConstructorDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#constructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitConstructorDeclaration(Java7Parser.ConstructorDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#genericConstructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterGenericConstructorDeclaration(Java7Parser.GenericConstructorDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#genericConstructorDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitGenericConstructorDeclaration(Java7Parser.GenericConstructorDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFieldDeclaration(Java7Parser.FieldDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#fieldDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFieldDeclaration(Java7Parser.FieldDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#interfaceBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceBodyDeclaration(Java7Parser.InterfaceBodyDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#interfaceBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceBodyDeclaration(Java7Parser.InterfaceBodyDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#interfaceMemberDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceMemberDeclaration(Java7Parser.InterfaceMemberDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#interfaceMemberDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceMemberDeclaration(Java7Parser.InterfaceMemberDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#constDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterConstDeclaration(Java7Parser.ConstDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#constDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitConstDeclaration(Java7Parser.ConstDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#constantDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterConstantDeclarator(Java7Parser.ConstantDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#constantDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitConstantDeclarator(Java7Parser.ConstantDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#interfaceMethodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterInterfaceMethodDeclaration(Java7Parser.InterfaceMethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#interfaceMethodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitInterfaceMethodDeclaration(Java7Parser.InterfaceMethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#genericInterfaceMethodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterGenericInterfaceMethodDeclaration(Java7Parser.GenericInterfaceMethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#genericInterfaceMethodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitGenericInterfaceMethodDeclaration(Java7Parser.GenericInterfaceMethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#variableDeclarators}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarators(Java7Parser.VariableDeclaratorsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#variableDeclarators}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarators(Java7Parser.VariableDeclaratorsContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#variableDeclarator}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclarator(Java7Parser.VariableDeclaratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#variableDeclarator}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclarator(Java7Parser.VariableDeclaratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaratorId(Java7Parser.VariableDeclaratorIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaratorId(Java7Parser.VariableDeclaratorIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#variableInitializer}.
	 * @param ctx the parse tree
	 */
	void enterVariableInitializer(Java7Parser.VariableInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#variableInitializer}.
	 * @param ctx the parse tree
	 */
	void exitVariableInitializer(Java7Parser.VariableInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#arrayInitializer}.
	 * @param ctx the parse tree
	 */
	void enterArrayInitializer(Java7Parser.ArrayInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#arrayInitializer}.
	 * @param ctx the parse tree
	 */
	void exitArrayInitializer(Java7Parser.ArrayInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#enumConstantName}.
	 * @param ctx the parse tree
	 */
	void enterEnumConstantName(Java7Parser.EnumConstantNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#enumConstantName}.
	 * @param ctx the parse tree
	 */
	void exitEnumConstantName(Java7Parser.EnumConstantNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#typeType}.
	 * @param ctx the parse tree
	 */
	void enterTypeType(Java7Parser.TypeTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#typeType}.
	 * @param ctx the parse tree
	 */
	void exitTypeType(Java7Parser.TypeTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#classOrInterfaceType}.
	 * @param ctx the parse tree
	 */
	void enterClassOrInterfaceType(Java7Parser.ClassOrInterfaceTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#classOrInterfaceType}.
	 * @param ctx the parse tree
	 */
	void exitClassOrInterfaceType(Java7Parser.ClassOrInterfaceTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveType(Java7Parser.PrimitiveTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveType(Java7Parser.PrimitiveTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#typeArguments}.
	 * @param ctx the parse tree
	 */
	void enterTypeArguments(Java7Parser.TypeArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#typeArguments}.
	 * @param ctx the parse tree
	 */
	void exitTypeArguments(Java7Parser.TypeArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#typeArgument}.
	 * @param ctx the parse tree
	 */
	void enterTypeArgument(Java7Parser.TypeArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#typeArgument}.
	 * @param ctx the parse tree
	 */
	void exitTypeArgument(Java7Parser.TypeArgumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#qualifiedNameList}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedNameList(Java7Parser.QualifiedNameListContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#qualifiedNameList}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedNameList(Java7Parser.QualifiedNameListContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#formalParameters}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameters(Java7Parser.FormalParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#formalParameters}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameters(Java7Parser.FormalParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#formalParameterList}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameterList(Java7Parser.FormalParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#formalParameterList}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameterList(Java7Parser.FormalParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#formalParameter}.
	 * @param ctx the parse tree
	 */
	void enterFormalParameter(Java7Parser.FormalParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#formalParameter}.
	 * @param ctx the parse tree
	 */
	void exitFormalParameter(Java7Parser.FormalParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#lastFormalParameter}.
	 * @param ctx the parse tree
	 */
	void enterLastFormalParameter(Java7Parser.LastFormalParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#lastFormalParameter}.
	 * @param ctx the parse tree
	 */
	void exitLastFormalParameter(Java7Parser.LastFormalParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#methodBody}.
	 * @param ctx the parse tree
	 */
	void enterMethodBody(Java7Parser.MethodBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#methodBody}.
	 * @param ctx the parse tree
	 */
	void exitMethodBody(Java7Parser.MethodBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#constructorBody}.
	 * @param ctx the parse tree
	 */
	void enterConstructorBody(Java7Parser.ConstructorBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#constructorBody}.
	 * @param ctx the parse tree
	 */
	void exitConstructorBody(Java7Parser.ConstructorBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void enterQualifiedName(Java7Parser.QualifiedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#qualifiedName}.
	 * @param ctx the parse tree
	 */
	void exitQualifiedName(Java7Parser.QualifiedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(Java7Parser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(Java7Parser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#annotation}.
	 * @param ctx the parse tree
	 */
	void enterAnnotation(Java7Parser.AnnotationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#annotation}.
	 * @param ctx the parse tree
	 */
	void exitAnnotation(Java7Parser.AnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#annotationName}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationName(Java7Parser.AnnotationNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#annotationName}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationName(Java7Parser.AnnotationNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#elementValuePairs}.
	 * @param ctx the parse tree
	 */
	void enterElementValuePairs(Java7Parser.ElementValuePairsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#elementValuePairs}.
	 * @param ctx the parse tree
	 */
	void exitElementValuePairs(Java7Parser.ElementValuePairsContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#elementValuePair}.
	 * @param ctx the parse tree
	 */
	void enterElementValuePair(Java7Parser.ElementValuePairContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#elementValuePair}.
	 * @param ctx the parse tree
	 */
	void exitElementValuePair(Java7Parser.ElementValuePairContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#elementValue}.
	 * @param ctx the parse tree
	 */
	void enterElementValue(Java7Parser.ElementValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#elementValue}.
	 * @param ctx the parse tree
	 */
	void exitElementValue(Java7Parser.ElementValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#elementValueArrayInitializer}.
	 * @param ctx the parse tree
	 */
	void enterElementValueArrayInitializer(Java7Parser.ElementValueArrayInitializerContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#elementValueArrayInitializer}.
	 * @param ctx the parse tree
	 */
	void exitElementValueArrayInitializer(Java7Parser.ElementValueArrayInitializerContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#annotationTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationTypeDeclaration(Java7Parser.AnnotationTypeDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#annotationTypeDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationTypeDeclaration(Java7Parser.AnnotationTypeDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#annotationTypeBody}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationTypeBody(Java7Parser.AnnotationTypeBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#annotationTypeBody}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationTypeBody(Java7Parser.AnnotationTypeBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#annotationTypeElementDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationTypeElementDeclaration(Java7Parser.AnnotationTypeElementDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#annotationTypeElementDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationTypeElementDeclaration(Java7Parser.AnnotationTypeElementDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#annotationTypeElementRest}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationTypeElementRest(Java7Parser.AnnotationTypeElementRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#annotationTypeElementRest}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationTypeElementRest(Java7Parser.AnnotationTypeElementRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#annotationMethodOrConstantRest}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationMethodOrConstantRest(Java7Parser.AnnotationMethodOrConstantRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#annotationMethodOrConstantRest}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationMethodOrConstantRest(Java7Parser.AnnotationMethodOrConstantRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#annotationMethodRest}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationMethodRest(Java7Parser.AnnotationMethodRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#annotationMethodRest}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationMethodRest(Java7Parser.AnnotationMethodRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#annotationConstantRest}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationConstantRest(Java7Parser.AnnotationConstantRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#annotationConstantRest}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationConstantRest(Java7Parser.AnnotationConstantRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#defaultValue}.
	 * @param ctx the parse tree
	 */
	void enterDefaultValue(Java7Parser.DefaultValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#defaultValue}.
	 * @param ctx the parse tree
	 */
	void exitDefaultValue(Java7Parser.DefaultValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(Java7Parser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(Java7Parser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(Java7Parser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(Java7Parser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#localVariableDeclarationStatement}.
	 * @param ctx the parse tree
	 */
	void enterLocalVariableDeclarationStatement(Java7Parser.LocalVariableDeclarationStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#localVariableDeclarationStatement}.
	 * @param ctx the parse tree
	 */
	void exitLocalVariableDeclarationStatement(Java7Parser.LocalVariableDeclarationStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterLocalVariableDeclaration(Java7Parser.LocalVariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitLocalVariableDeclaration(Java7Parser.LocalVariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(Java7Parser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(Java7Parser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#catchClause}.
	 * @param ctx the parse tree
	 */
	void enterCatchClause(Java7Parser.CatchClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#catchClause}.
	 * @param ctx the parse tree
	 */
	void exitCatchClause(Java7Parser.CatchClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#catchType}.
	 * @param ctx the parse tree
	 */
	void enterCatchType(Java7Parser.CatchTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#catchType}.
	 * @param ctx the parse tree
	 */
	void exitCatchType(Java7Parser.CatchTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#finallyBlock}.
	 * @param ctx the parse tree
	 */
	void enterFinallyBlock(Java7Parser.FinallyBlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#finallyBlock}.
	 * @param ctx the parse tree
	 */
	void exitFinallyBlock(Java7Parser.FinallyBlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#resourceSpecification}.
	 * @param ctx the parse tree
	 */
	void enterResourceSpecification(Java7Parser.ResourceSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#resourceSpecification}.
	 * @param ctx the parse tree
	 */
	void exitResourceSpecification(Java7Parser.ResourceSpecificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#resources}.
	 * @param ctx the parse tree
	 */
	void enterResources(Java7Parser.ResourcesContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#resources}.
	 * @param ctx the parse tree
	 */
	void exitResources(Java7Parser.ResourcesContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#resource}.
	 * @param ctx the parse tree
	 */
	void enterResource(Java7Parser.ResourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#resource}.
	 * @param ctx the parse tree
	 */
	void exitResource(Java7Parser.ResourceContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 */
	void enterSwitchBlockStatementGroup(Java7Parser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 */
	void exitSwitchBlockStatementGroup(Java7Parser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#switchLabel}.
	 * @param ctx the parse tree
	 */
	void enterSwitchLabel(Java7Parser.SwitchLabelContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#switchLabel}.
	 * @param ctx the parse tree
	 */
	void exitSwitchLabel(Java7Parser.SwitchLabelContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#forControl}.
	 * @param ctx the parse tree
	 */
	void enterForControl(Java7Parser.ForControlContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#forControl}.
	 * @param ctx the parse tree
	 */
	void exitForControl(Java7Parser.ForControlContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#forInit}.
	 * @param ctx the parse tree
	 */
	void enterForInit(Java7Parser.ForInitContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#forInit}.
	 * @param ctx the parse tree
	 */
	void exitForInit(Java7Parser.ForInitContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#enhancedForControl}.
	 * @param ctx the parse tree
	 */
	void enterEnhancedForControl(Java7Parser.EnhancedForControlContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#enhancedForControl}.
	 * @param ctx the parse tree
	 */
	void exitEnhancedForControl(Java7Parser.EnhancedForControlContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#forUpdate}.
	 * @param ctx the parse tree
	 */
	void enterForUpdate(Java7Parser.ForUpdateContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#forUpdate}.
	 * @param ctx the parse tree
	 */
	void exitForUpdate(Java7Parser.ForUpdateContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#parExpression}.
	 * @param ctx the parse tree
	 */
	void enterParExpression(Java7Parser.ParExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#parExpression}.
	 * @param ctx the parse tree
	 */
	void exitParExpression(Java7Parser.ParExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(Java7Parser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(Java7Parser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#statementExpression}.
	 * @param ctx the parse tree
	 */
	void enterStatementExpression(Java7Parser.StatementExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#statementExpression}.
	 * @param ctx the parse tree
	 */
	void exitStatementExpression(Java7Parser.StatementExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#constantExpression}.
	 * @param ctx the parse tree
	 */
	void enterConstantExpression(Java7Parser.ConstantExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#constantExpression}.
	 * @param ctx the parse tree
	 */
	void exitConstantExpression(Java7Parser.ConstantExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(Java7Parser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(Java7Parser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(Java7Parser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(Java7Parser.PrimaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#creator}.
	 * @param ctx the parse tree
	 */
	void enterCreator(Java7Parser.CreatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#creator}.
	 * @param ctx the parse tree
	 */
	void exitCreator(Java7Parser.CreatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#createdName}.
	 * @param ctx the parse tree
	 */
	void enterCreatedName(Java7Parser.CreatedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#createdName}.
	 * @param ctx the parse tree
	 */
	void exitCreatedName(Java7Parser.CreatedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#innerCreator}.
	 * @param ctx the parse tree
	 */
	void enterInnerCreator(Java7Parser.InnerCreatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#innerCreator}.
	 * @param ctx the parse tree
	 */
	void exitInnerCreator(Java7Parser.InnerCreatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#arrayCreatorRest}.
	 * @param ctx the parse tree
	 */
	void enterArrayCreatorRest(Java7Parser.ArrayCreatorRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#arrayCreatorRest}.
	 * @param ctx the parse tree
	 */
	void exitArrayCreatorRest(Java7Parser.ArrayCreatorRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#classCreatorRest}.
	 * @param ctx the parse tree
	 */
	void enterClassCreatorRest(Java7Parser.ClassCreatorRestContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#classCreatorRest}.
	 * @param ctx the parse tree
	 */
	void exitClassCreatorRest(Java7Parser.ClassCreatorRestContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#explicitGenericInvocation}.
	 * @param ctx the parse tree
	 */
	void enterExplicitGenericInvocation(Java7Parser.ExplicitGenericInvocationContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#explicitGenericInvocation}.
	 * @param ctx the parse tree
	 */
	void exitExplicitGenericInvocation(Java7Parser.ExplicitGenericInvocationContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#nonWildcardTypeArguments}.
	 * @param ctx the parse tree
	 */
	void enterNonWildcardTypeArguments(Java7Parser.NonWildcardTypeArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#nonWildcardTypeArguments}.
	 * @param ctx the parse tree
	 */
	void exitNonWildcardTypeArguments(Java7Parser.NonWildcardTypeArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#typeArgumentsOrDiamond}.
	 * @param ctx the parse tree
	 */
	void enterTypeArgumentsOrDiamond(Java7Parser.TypeArgumentsOrDiamondContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#typeArgumentsOrDiamond}.
	 * @param ctx the parse tree
	 */
	void exitTypeArgumentsOrDiamond(Java7Parser.TypeArgumentsOrDiamondContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#nonWildcardTypeArgumentsOrDiamond}.
	 * @param ctx the parse tree
	 */
	void enterNonWildcardTypeArgumentsOrDiamond(Java7Parser.NonWildcardTypeArgumentsOrDiamondContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#nonWildcardTypeArgumentsOrDiamond}.
	 * @param ctx the parse tree
	 */
	void exitNonWildcardTypeArgumentsOrDiamond(Java7Parser.NonWildcardTypeArgumentsOrDiamondContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#superSuffix}.
	 * @param ctx the parse tree
	 */
	void enterSuperSuffix(Java7Parser.SuperSuffixContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#superSuffix}.
	 * @param ctx the parse tree
	 */
	void exitSuperSuffix(Java7Parser.SuperSuffixContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#explicitGenericInvocationSuffix}.
	 * @param ctx the parse tree
	 */
	void enterExplicitGenericInvocationSuffix(Java7Parser.ExplicitGenericInvocationSuffixContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#explicitGenericInvocationSuffix}.
	 * @param ctx the parse tree
	 */
	void exitExplicitGenericInvocationSuffix(Java7Parser.ExplicitGenericInvocationSuffixContext ctx);
	/**
	 * Enter a parse tree produced by {@link Java7Parser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(Java7Parser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link Java7Parser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(Java7Parser.ArgumentsContext ctx);
}