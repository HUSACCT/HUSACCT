// Generated from Java7.g4 by ANTLR 4.6

package husacct.analyse.infrastructure.antlr.java;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link Java7Parser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface Java7Visitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link Java7Parser#compilationUnit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompilationUnit(Java7Parser.CompilationUnitContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#packageDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackageDeclaration(Java7Parser.PackageDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#importDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportDeclaration(Java7Parser.ImportDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#typeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeDeclaration(Java7Parser.TypeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#modifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitModifier(Java7Parser.ModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#classOrInterfaceModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassOrInterfaceModifier(Java7Parser.ClassOrInterfaceModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#variableModifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableModifier(Java7Parser.VariableModifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#classDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassDeclaration(Java7Parser.ClassDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#typeParameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeParameters(Java7Parser.TypeParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#typeParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeParameter(Java7Parser.TypeParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#typeBound}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeBound(Java7Parser.TypeBoundContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#enumDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumDeclaration(Java7Parser.EnumDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#enumConstants}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumConstants(Java7Parser.EnumConstantsContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#enumConstant}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumConstant(Java7Parser.EnumConstantContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#enumBodyDeclarations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumBodyDeclarations(Java7Parser.EnumBodyDeclarationsContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#interfaceDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceDeclaration(Java7Parser.InterfaceDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#typeList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeList(Java7Parser.TypeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#classBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassBody(Java7Parser.ClassBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#interfaceBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceBody(Java7Parser.InterfaceBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#classBodyDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassBodyDeclaration(Java7Parser.ClassBodyDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#memberDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMemberDeclaration(Java7Parser.MemberDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#methodDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodDeclaration(Java7Parser.MethodDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#genericMethodDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericMethodDeclaration(Java7Parser.GenericMethodDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#constructorDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorDeclaration(Java7Parser.ConstructorDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#genericConstructorDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericConstructorDeclaration(Java7Parser.GenericConstructorDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#fieldDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldDeclaration(Java7Parser.FieldDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#interfaceBodyDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceBodyDeclaration(Java7Parser.InterfaceBodyDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#interfaceMemberDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceMemberDeclaration(Java7Parser.InterfaceMemberDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#constDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstDeclaration(Java7Parser.ConstDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#constantDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantDeclarator(Java7Parser.ConstantDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#interfaceMethodDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInterfaceMethodDeclaration(Java7Parser.InterfaceMethodDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#genericInterfaceMethodDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGenericInterfaceMethodDeclaration(Java7Parser.GenericInterfaceMethodDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#variableDeclarators}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclarators(Java7Parser.VariableDeclaratorsContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#variableDeclarator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclarator(Java7Parser.VariableDeclaratorContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#variableDeclaratorId}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaratorId(Java7Parser.VariableDeclaratorIdContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#variableInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableInitializer(Java7Parser.VariableInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#arrayInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayInitializer(Java7Parser.ArrayInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#enumConstantName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnumConstantName(Java7Parser.EnumConstantNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#typeType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeType(Java7Parser.TypeTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#classOrInterfaceType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassOrInterfaceType(Java7Parser.ClassOrInterfaceTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#primitiveType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimitiveType(Java7Parser.PrimitiveTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#typeArguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeArguments(Java7Parser.TypeArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#typeArgument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeArgument(Java7Parser.TypeArgumentContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#qualifiedNameList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedNameList(Java7Parser.QualifiedNameListContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#formalParameters}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameters(Java7Parser.FormalParametersContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#formalParameterList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameterList(Java7Parser.FormalParameterListContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#formalParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFormalParameter(Java7Parser.FormalParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#lastFormalParameter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLastFormalParameter(Java7Parser.LastFormalParameterContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#methodBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMethodBody(Java7Parser.MethodBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#constructorBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstructorBody(Java7Parser.ConstructorBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#qualifiedName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQualifiedName(Java7Parser.QualifiedNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(Java7Parser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#annotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotation(Java7Parser.AnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#annotationName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationName(Java7Parser.AnnotationNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#elementValuePairs}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementValuePairs(Java7Parser.ElementValuePairsContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#elementValuePair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementValuePair(Java7Parser.ElementValuePairContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#elementValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementValue(Java7Parser.ElementValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#elementValueArrayInitializer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElementValueArrayInitializer(Java7Parser.ElementValueArrayInitializerContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#annotationTypeDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationTypeDeclaration(Java7Parser.AnnotationTypeDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#annotationTypeBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationTypeBody(Java7Parser.AnnotationTypeBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#annotationTypeElementDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationTypeElementDeclaration(Java7Parser.AnnotationTypeElementDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#annotationTypeElementRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationTypeElementRest(Java7Parser.AnnotationTypeElementRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#annotationMethodOrConstantRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationMethodOrConstantRest(Java7Parser.AnnotationMethodOrConstantRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#annotationMethodRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationMethodRest(Java7Parser.AnnotationMethodRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#annotationConstantRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationConstantRest(Java7Parser.AnnotationConstantRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#defaultValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDefaultValue(Java7Parser.DefaultValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#block}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlock(Java7Parser.BlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#blockStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBlockStatement(Java7Parser.BlockStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#localVariableDeclarationStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocalVariableDeclarationStatement(Java7Parser.LocalVariableDeclarationStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#localVariableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocalVariableDeclaration(Java7Parser.LocalVariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(Java7Parser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#catchClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatchClause(Java7Parser.CatchClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#catchType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCatchType(Java7Parser.CatchTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#finallyBlock}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFinallyBlock(Java7Parser.FinallyBlockContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#resourceSpecification}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResourceSpecification(Java7Parser.ResourceSpecificationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#resources}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResources(Java7Parser.ResourcesContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#resource}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResource(Java7Parser.ResourceContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#switchBlockStatementGroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchBlockStatementGroup(Java7Parser.SwitchBlockStatementGroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#switchLabel}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSwitchLabel(Java7Parser.SwitchLabelContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#forControl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForControl(Java7Parser.ForControlContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#forInit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForInit(Java7Parser.ForInitContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#enhancedForControl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEnhancedForControl(Java7Parser.EnhancedForControlContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#forUpdate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitForUpdate(Java7Parser.ForUpdateContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#parExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParExpression(Java7Parser.ParExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#expressionList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionList(Java7Parser.ExpressionListContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#statementExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatementExpression(Java7Parser.StatementExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#constantExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantExpression(Java7Parser.ConstantExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(Java7Parser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#primary}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrimary(Java7Parser.PrimaryContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#creator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreator(Java7Parser.CreatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#createdName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCreatedName(Java7Parser.CreatedNameContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#innerCreator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInnerCreator(Java7Parser.InnerCreatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#arrayCreatorRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayCreatorRest(Java7Parser.ArrayCreatorRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#classCreatorRest}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassCreatorRest(Java7Parser.ClassCreatorRestContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#explicitGenericInvocation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplicitGenericInvocation(Java7Parser.ExplicitGenericInvocationContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#nonWildcardTypeArguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonWildcardTypeArguments(Java7Parser.NonWildcardTypeArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#typeArgumentsOrDiamond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeArgumentsOrDiamond(Java7Parser.TypeArgumentsOrDiamondContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#nonWildcardTypeArgumentsOrDiamond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonWildcardTypeArgumentsOrDiamond(Java7Parser.NonWildcardTypeArgumentsOrDiamondContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#superSuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSuperSuffix(Java7Parser.SuperSuffixContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#explicitGenericInvocationSuffix}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExplicitGenericInvocationSuffix(Java7Parser.ExplicitGenericInvocationSuffixContext ctx);
	/**
	 * Visit a parse tree produced by {@link Java7Parser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(Java7Parser.ArgumentsContext ctx);
}