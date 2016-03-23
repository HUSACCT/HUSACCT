package husacct.analyse.infrastructure.antlr.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.antlr.runtime.BitSet;
import org.antlr.runtime.EarlyExitException;
import org.antlr.runtime.MismatchedSetException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.TreeNodeStream;
import org.antlr.runtime.tree.TreeParser;

/**
 * For more information see the head comment within the 'java.g' grammar file
 * that defines the input for this tree grammar.
 *
 * BSD licence
 *
 * Copyright (c) 2007-2008 by HABELITZ Software Developments
 *
 * All rights reserved.
 *
 * http://www.habelitz.com
 *
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. 2. Redistributions in
 * binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. 3. The name of the author may not
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY HABELITZ SOFTWARE DEVELOPMENTS ('HSD') ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL 'HSD' BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
public class JavaTreeParser extends TreeParser {

    public static final String[] tokenNames = new String[]{
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "AND", "AND_ASSIGN", "ASSIGN", "AT", "BIT_SHIFT_RIGHT", "BIT_SHIFT_RIGHT_ASSIGN", "COLON", "COMMA", "DEC", "DIV", "DIV_ASSIGN", "DOT", "DOTSTAR", "ELLIPSIS", "EQUAL", "GREATER_OR_EQUAL", "GREATER_THAN", "INC", "LBRACK", "LCURLY", "LESS_OR_EQUAL", "LESS_THAN", "LOGICAL_AND", "LOGICAL_NOT", "LOGICAL_OR", "LPAREN", "MINUS", "MINUS_ASSIGN", "MOD", "MOD_ASSIGN", "NOT", "NOT_EQUAL", "OR", "OR_ASSIGN", "PLUS", "PLUS_ASSIGN", "QUESTION", "RBRACK", "RCURLY", "RPAREN", "SEMI", "SHIFT_LEFT", "SHIFT_LEFT_ASSIGN", "SHIFT_RIGHT", "SHIFT_RIGHT_ASSIGN", "STAR", "STAR_ASSIGN", "XOR", "XOR_ASSIGN", "ABSTRACT", "ASSERT", "BOOLEAN", "BREAK", "BYTE", "CASE", "CATCH", "CHAR", "CLASS", "CONTINUE", "DEFAULT", "DO", "DOUBLE", "ELSE", "ENUM", "EXTENDS", "FALSE", "FINAL", "FINALLY", "FLOAT", "FOR", "IF", "IMPLEMENTS", "INSTANCEOF", "INTERFACE", "IMPORT", "INT", "LONG", "NATIVE", "NEW", "NULL", "PACKAGE", "PRIVATE", "PROTECTED", "PUBLIC", "RETURN", "SHORT", "STATIC", "STRICTFP", "SUPER", "SWITCH", "SYNCHRONIZED", "THIS", "THROW", "THROWS", "TRANSIENT", "TRUE", "TRY", "VOID", "VOLATILE", "WHILE", "ANNOTATION_INIT_ARRAY_ELEMENT", "ANNOTATION_INIT_BLOCK", "ANNOTATION_INIT_DEFAULT_KEY", "ANNOTATION_INIT_KEY_LIST", "ANNOTATION_LIST", "ANNOTATION_METHOD_DECL", "ANNOTATION_SCOPE", "ANNOTATION_TOP_LEVEL_SCOPE", "ARGUMENT_LIST", "ARRAY_DECLARATOR", "ARRAY_DECLARATOR_LIST", "ARRAY_ELEMENT_ACCESS", "ARRAY_INITIALIZER", "BLOCK_SCOPE", "CAST_EXPR", "CATCH_CLAUSE_LIST", "CLASS_CONSTRUCTOR_CALL", "CLASS_INSTANCE_INITIALIZER", "CLASS_STATIC_INITIALIZER", "CLASS_TOP_LEVEL_SCOPE", "CONSTRUCTOR_DECL", "ENUM_TOP_LEVEL_SCOPE", "EXPR", "EXTENDS_BOUND_LIST", "EXTENDS_CLAUSE", "FOR_CONDITION", "FOR_EACH", "FOR_INIT", "FOR_UPDATE", "FORMAL_PARAM_LIST", "FORMAL_PARAM_STD_DECL", "FORMAL_PARAM_VARARG_DECL", "FUNCTION_METHOD_DECL", "GENERIC_TYPE_ARG_LIST", "GENERIC_TYPE_PARAM_LIST", "INTERFACE_TOP_LEVEL_SCOPE", "IMPLEMENTS_CLAUSE", "LABELED_STATEMENT", "LOCAL_MODIFIER_LIST", "JAVA_SOURCE", "METHOD_CALL", "MODIFIER_LIST", "PARENTESIZED_EXPR", "POST_DEC", "POST_INC", "PRE_DEC", "PRE_INC", "QUALIFIED_TYPE_IDENT", "STATIC_ARRAY_CREATOR", "SUPER_CONSTRUCTOR_CALL", "SWITCH_BLOCK_LABEL_LIST", "THIS_CONSTRUCTOR_CALL", "THROWS_CLAUSE", "TYPE", "UNARY_MINUS", "UNARY_PLUS", "VAR_DECLARATION", "VAR_DECLARATOR", "VAR_DECLARATOR_LIST", "VOID_METHOD_DECL", "IDENT", "HEX_LITERAL", "OCTAL_LITERAL", "DECIMAL_LITERAL", "FLOATING_POINT_LITERAL", "CHARACTER_LITERAL", "STRING_LITERAL", "HEX_DIGIT", "INTEGER_TYPE_SUFFIX", "EXPONENT", "FLOAT_TYPE_SUFFIX", "ESCAPE_SEQUENCE", "UNICODE_ESCAPE", "OCTAL_ESCAPE", "JAVA_ID_START", "JAVA_ID_PART", "WS", "COMMENT", "LINE_COMMENT"
    };
    public static final int PACKAGE = 84;
    public static final int EXPONENT = 173;
    public static final int STAR = 49;
    public static final int WHILE = 103;
    public static final int MOD = 32;
    public static final int MOD_ASSIGN = 33;
    public static final int CASE = 58;
    public static final int CHAR = 60;
    public static final int NEW = 82;
    public static final int DO = 64;
    public static final int GENERIC_TYPE_PARAM_LIST = 138;
    public static final int CLASS_INSTANCE_INITIALIZER = 121;
    public static final int ARRAY_ELEMENT_ACCESS = 115;
    public static final int FOR_CONDITION = 129;
    public static final int NOT = 34;
    public static final int VAR_DECLARATION = 160;
    public static final int ANNOTATION_METHOD_DECL = 109;
    public static final int EOF = -1;
    public static final int DIV_ASSIGN = 14;
    public static final int BREAK = 56;
    public static final int LOGICAL_AND = 26;
    public static final int BIT_SHIFT_RIGHT_ASSIGN = 9;
    public static final int UNARY_PLUS = 159;
    public static final int TYPE = 157;
    public static final int FINAL = 70;
    public static final int INC = 21;
    public static final int RPAREN = 43;
    public static final int IMPORT = 78;
    public static final int STRING_LITERAL = 170;
    public static final int FOR_UPDATE = 132;
    public static final int FLOATING_POINT_LITERAL = 168;
    public static final int CAST_EXPR = 118;
    public static final int NOT_EQUAL = 35;
    public static final int VOID_METHOD_DECL = 163;
    public static final int RETURN = 88;
    public static final int THIS = 95;
    public static final int DOUBLE = 65;
    public static final int VOID = 101;
    public static final int ENUM_TOP_LEVEL_SCOPE = 125;
    public static final int SUPER = 92;
    public static final int COMMENT = 181;
    public static final int ANNOTATION_INIT_KEY_LIST = 107;
    public static final int JAVA_ID_START = 178;
    public static final int FLOAT_TYPE_SUFFIX = 174;
    public static final int PRE_DEC = 149;
    public static final int RBRACK = 41;
    public static final int IMPLEMENTS_CLAUSE = 140;
    public static final int SWITCH_BLOCK_LABEL_LIST = 154;
    public static final int LINE_COMMENT = 182;
    public static final int PRIVATE = 85;
    public static final int STATIC = 90;
    public static final int BLOCK_SCOPE = 117;
    public static final int ANNOTATION_INIT_DEFAULT_KEY = 106;
    public static final int SWITCH = 93;
    public static final int NULL = 83;
    public static final int VAR_DECLARATOR = 161;
    public static final int MINUS_ASSIGN = 31;
    public static final int ELSE = 66;
    public static final int STRICTFP = 91;
    public static final int CHARACTER_LITERAL = 169;
    public static final int PRE_INC = 150;
    public static final int ANNOTATION_LIST = 108;
    public static final int ELLIPSIS = 17;
    public static final int NATIVE = 81;
    public static final int OCTAL_ESCAPE = 177;
    public static final int UNARY_MINUS = 158;
    public static final int THROWS = 97;
    public static final int LCURLY = 23;
    public static final int INT = 79;
    public static final int FORMAL_PARAM_VARARG_DECL = 135;
    public static final int METHOD_CALL = 144;
    public static final int ASSERT = 54;
    public static final int TRY = 100;
    public static final int INTERFACE_TOP_LEVEL_SCOPE = 139;
    public static final int SHIFT_LEFT = 45;
    public static final int WS = 180;
    public static final int SHIFT_RIGHT = 47;
    public static final int FORMAL_PARAM_STD_DECL = 134;
    public static final int LOCAL_MODIFIER_LIST = 142;
    public static final int OR = 36;
    public static final int LESS_THAN = 25;
    public static final int SHIFT_RIGHT_ASSIGN = 48;
    public static final int EXTENDS_BOUND_LIST = 127;
    public static final int JAVA_SOURCE = 143;
    public static final int CATCH = 59;
    public static final int FALSE = 69;
    public static final int INTEGER_TYPE_SUFFIX = 172;
    public static final int DECIMAL_LITERAL = 167;
    public static final int THROW = 96;
    public static final int FOR_INIT = 131;
    public static final int PROTECTED = 86;
    public static final int DEC = 12;
    public static final int CLASS = 61;
    public static final int LBRACK = 22;
    public static final int BIT_SHIFT_RIGHT = 8;
    public static final int THROWS_CLAUSE = 156;
    public static final int GREATER_OR_EQUAL = 19;
    public static final int FOR = 73;
    public static final int LOGICAL_NOT = 27;
    public static final int THIS_CONSTRUCTOR_CALL = 155;
    public static final int FLOAT = 72;
    public static final int ABSTRACT = 53;
    public static final int AND = 4;
    public static final int POST_DEC = 147;
    public static final int AND_ASSIGN = 5;
    public static final int ANNOTATION_SCOPE = 110;
    public static final int MODIFIER_LIST = 145;
    public static final int STATIC_ARRAY_CREATOR = 152;
    public static final int LPAREN = 29;
    public static final int IF = 74;
    public static final int AT = 7;
    public static final int CONSTRUCTOR_DECL = 124;
    public static final int ESCAPE_SEQUENCE = 175;
    public static final int LABELED_STATEMENT = 141;
    public static final int UNICODE_ESCAPE = 176;
    public static final int BOOLEAN = 55;
    public static final int SYNCHRONIZED = 94;
    public static final int EXPR = 126;
    public static final int CLASS_TOP_LEVEL_SCOPE = 123;
    public static final int IMPLEMENTS = 75;
    public static final int CONTINUE = 62;
    public static final int COMMA = 11;
    public static final int TRANSIENT = 98;
    public static final int XOR_ASSIGN = 52;
    public static final int EQUAL = 18;
    public static final int LOGICAL_OR = 28;
    public static final int ARGUMENT_LIST = 112;
    public static final int QUALIFIED_TYPE_IDENT = 151;
    public static final int IDENT = 164;
    public static final int PLUS = 38;
    public static final int ANNOTATION_INIT_BLOCK = 105;
    public static final int HEX_LITERAL = 165;
    public static final int DOT = 15;
    public static final int SHIFT_LEFT_ASSIGN = 46;
    public static final int FORMAL_PARAM_LIST = 133;
    public static final int GENERIC_TYPE_ARG_LIST = 137;
    public static final int DOTSTAR = 16;
    public static final int ANNOTATION_TOP_LEVEL_SCOPE = 111;
    public static final int BYTE = 57;
    public static final int XOR = 51;
    public static final int JAVA_ID_PART = 179;
    public static final int GREATER_THAN = 20;
    public static final int VOLATILE = 102;
    public static final int PARENTESIZED_EXPR = 146;
    public static final int LESS_OR_EQUAL = 24;
    public static final int ARRAY_DECLARATOR_LIST = 114;
    public static final int CLASS_STATIC_INITIALIZER = 122;
    public static final int DEFAULT = 63;
    public static final int OCTAL_LITERAL = 166;
    public static final int HEX_DIGIT = 171;
    public static final int SHORT = 89;
    public static final int INSTANCEOF = 76;
    public static final int MINUS = 30;
    public static final int SEMI = 44;
    public static final int TRUE = 99;
    public static final int EXTENDS_CLAUSE = 128;
    public static final int STAR_ASSIGN = 50;
    public static final int VAR_DECLARATOR_LIST = 162;
    public static final int COLON = 10;
    public static final int ARRAY_DECLARATOR = 113;
    public static final int OR_ASSIGN = 37;
    public static final int ENUM = 67;
    public static final int QUESTION = 40;
    public static final int FINALLY = 71;
    public static final int RCURLY = 42;
    public static final int ASSIGN = 6;
    public static final int PLUS_ASSIGN = 39;
    public static final int ANNOTATION_INIT_ARRAY_ELEMENT = 104;
    public static final int FUNCTION_METHOD_DECL = 136;
    public static final int INTERFACE = 77;
    public static final int DIV = 13;
    public static final int POST_INC = 148;
    public static final int LONG = 80;
    public static final int CLASS_CONSTRUCTOR_CALL = 120;
    public static final int PUBLIC = 87;
    public static final int EXTENDS = 68;
    public static final int FOR_EACH = 130;
    public static final int ARRAY_INITIALIZER = 116;
    public static final int CATCH_CLAUSE_LIST = 119;
    public static final int SUPER_CONSTRUCTOR_CALL = 153;

    // delegates
    // delegators
    public JavaTreeParser(TreeNodeStream input) {
        this(input, new RecognizerSharedState());
    }

    public JavaTreeParser(TreeNodeStream input, RecognizerSharedState state) {
        super(input, state);
        this.state.ruleMemo = new HashMap[288 + 1];


    }

    public String[] getTokenNames() {
        return JavaTreeParser.tokenNames;
    }

    public String getGrammarFileName() {
        return "/Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g";
    }
    boolean mMessageCollectionEnabled = false;
    private boolean mHasErrors = false;
    List<String> mMessages;

    /**
     * Switches error message collection on or of.
     *
     * The standard destination for parser error messages is
     * <code>System.err</code>. However, if
     * <code>true</code> gets passed to this method this default behaviour will
     * be switched off and all error messages will be collected instead of
     * written to anywhere.
     *
     * The default value is
     * <code>false</code>.
     *
     * @param pNewState  <code>true</code> if error messages should be collected.
     */
    public void enableErrorMessageCollection(boolean pNewState) {
        mMessageCollectionEnabled = pNewState;
        if (mMessages == null && mMessageCollectionEnabled) {
            mMessages = new ArrayList<String>();
        }
    }

    /**
     * Collects an error message or passes the error message to
     * <code>
     *  super.emitErrorMessage(...)</code>.
     *
     * The actual behaviour depends on whether collecting error messages has
     * been enabled or not.
     *
     * @param pMessage The error message.
     */
    @Override
    public void emitErrorMessage(String pMessage) {
        if (mMessageCollectionEnabled) {
            mMessages.add(pMessage);
        } else {
            super.emitErrorMessage(pMessage);
        }
    }

    /**
     * Returns collected error messages.
     *
     * @return A list holding collected error messages or <code>null</code> if
     * collecting error messages hasn't been enabled. Of course, this list may
     * be empty if no error message has been emited.
     */
    public List<String> getMessages() {
        return mMessages;
    }

    /**
     * Tells if parsing a Java source has caused any error messages.
     *
     * @return  <code>true</code> if parsing a Java source has caused at least
     * one error message.
     */
    public boolean hasErrors() {
        return mHasErrors;
    }

    // $ANTLR start "javaSource"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:117:1: javaSource : ^( JAVA_SOURCE annotationList ( packageDeclaration )? ( importDeclaration )* ( typeDeclaration )* ) ;
    public final void javaSource() throws RecognitionException {
        int javaSource_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 1)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:118:5: ( ^( JAVA_SOURCE annotationList ( packageDeclaration )? ( importDeclaration )* ( typeDeclaration )* ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:118:9: ^( JAVA_SOURCE annotationList ( packageDeclaration )? ( importDeclaration )* ( typeDeclaration )* )
            {
                match(input, JAVA_SOURCE, FOLLOW_JAVA_SOURCE_in_javaSource91);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_annotationList_in_javaSource93);
                annotationList();

                state._fsp--;
                if (state.failed) {
                    return;
                }
                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:118:38: ( packageDeclaration )?
                int alt1 = 2;
                int LA1_0 = input.LA(1);

                if ((LA1_0 == PACKAGE)) {
                    alt1 = 1;
                }
                switch (alt1) {
                    case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: packageDeclaration
                    {
                        pushFollow(FOLLOW_packageDeclaration_in_javaSource95);
                        packageDeclaration();

                        state._fsp--;
                        if (state.failed) {
                            return;
                        }

                    }
                    break;

                }

                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:118:58: ( importDeclaration )*
                loop2:
                do {
                    int alt2 = 2;
                    int LA2_0 = input.LA(1);

                    if ((LA2_0 == IMPORT)) {
                        alt2 = 1;
                    }


                    switch (alt2) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: importDeclaration
                        {
                            pushFollow(FOLLOW_importDeclaration_in_javaSource98);
                            importDeclaration();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                        default:
                            break loop2;
                    }
                } while (true);

                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:118:77: ( typeDeclaration )*
                loop3:
                do {
                    int alt3 = 2;
                    int LA3_0 = input.LA(1);

                    if ((LA3_0 == AT || LA3_0 == CLASS || LA3_0 == ENUM || LA3_0 == INTERFACE)) {
                        alt3 = 1;
                    }


                    switch (alt3) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: typeDeclaration
                        {
                            pushFollow(FOLLOW_typeDeclaration_in_javaSource101);
                            typeDeclaration();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                        default:
                            break loop3;
                    }
                } while (true);


                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 1, javaSource_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "javaSource"

    // $ANTLR start "packageDeclaration"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:121:1: packageDeclaration : ^( PACKAGE qualifiedIdentifier ) ;
    public final void packageDeclaration() throws RecognitionException {
        int packageDeclaration_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 2)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:122:5: ( ^( PACKAGE qualifiedIdentifier ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:122:9: ^( PACKAGE qualifiedIdentifier )
            {
                match(input, PACKAGE, FOLLOW_PACKAGE_in_packageDeclaration123);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_qualifiedIdentifier_in_packageDeclaration125);
                qualifiedIdentifier();

                state._fsp--;
                if (state.failed) {
                    return;
                }

                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 2, packageDeclaration_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "packageDeclaration"

    // $ANTLR start "importDeclaration"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:125:1: importDeclaration : ^( IMPORT ( STATIC )? qualifiedIdentifier ( DOTSTAR )? ) ;
    public final void importDeclaration() throws RecognitionException {
        int importDeclaration_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 3)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:126:5: ( ^( IMPORT ( STATIC )? qualifiedIdentifier ( DOTSTAR )? ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:126:9: ^( IMPORT ( STATIC )? qualifiedIdentifier ( DOTSTAR )? )
            {
                match(input, IMPORT, FOLLOW_IMPORT_in_importDeclaration152);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:126:18: ( STATIC )?
                int alt4 = 2;
                int LA4_0 = input.LA(1);

                if ((LA4_0 == STATIC)) {
                    alt4 = 1;
                }
                switch (alt4) {
                    case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: STATIC
                    {
                        match(input, STATIC, FOLLOW_STATIC_in_importDeclaration154);
                        if (state.failed) {
                            return;
                        }

                    }
                    break;

                }

                pushFollow(FOLLOW_qualifiedIdentifier_in_importDeclaration157);
                qualifiedIdentifier();

                state._fsp--;
                if (state.failed) {
                    return;
                }
                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:126:46: ( DOTSTAR )?
                int alt5 = 2;
                int LA5_0 = input.LA(1);

                if ((LA5_0 == DOTSTAR)) {
                    alt5 = 1;
                }
                switch (alt5) {
                    case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: DOTSTAR
                    {
                        match(input, DOTSTAR, FOLLOW_DOTSTAR_in_importDeclaration159);
                        if (state.failed) {
                            return;
                        }

                    }
                    break;

                }


                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 3, importDeclaration_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "importDeclaration"

    // $ANTLR start "typeDeclaration"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:129:1: typeDeclaration : ( ^( CLASS modifierList IDENT ( genericTypeParameterList )? ( extendsClause )? ( implementsClause )? classTopLevelScope ) | ^( INTERFACE modifierList IDENT ( genericTypeParameterList )? ( extendsClause )? interfaceTopLevelScope ) | ^( ENUM modifierList IDENT ( implementsClause )? enumTopLevelScope ) | ^( AT modifierList IDENT annotationTopLevelScope ) );
    public final void typeDeclaration() throws RecognitionException {
        int typeDeclaration_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 4)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:130:5: ( ^( CLASS modifierList IDENT ( genericTypeParameterList )? ( extendsClause )? ( implementsClause )? classTopLevelScope ) | ^( INTERFACE modifierList IDENT ( genericTypeParameterList )? ( extendsClause )? interfaceTopLevelScope ) | ^( ENUM modifierList IDENT ( implementsClause )? enumTopLevelScope ) | ^( AT modifierList IDENT annotationTopLevelScope ) )
            int alt12 = 4;
            switch (input.LA(1)) {
                case CLASS: {
                    alt12 = 1;
                }
                break;
                case INTERFACE: {
                    alt12 = 2;
                }
                break;
                case ENUM: {
                    alt12 = 3;
                }
                break;
                case AT: {
                    alt12 = 4;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 12, 0, input);

                    throw nvae;
            }

            switch (alt12) {
                case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:130:9: ^( CLASS modifierList IDENT ( genericTypeParameterList )? ( extendsClause )? ( implementsClause )? classTopLevelScope )
                {
                    match(input, CLASS, FOLLOW_CLASS_in_typeDeclaration185);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_modifierList_in_typeDeclaration187);
                    modifierList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    match(input, IDENT, FOLLOW_IDENT_in_typeDeclaration189);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:130:36: ( genericTypeParameterList )?
                    int alt6 = 2;
                    int LA6_0 = input.LA(1);

                    if ((LA6_0 == GENERIC_TYPE_PARAM_LIST)) {
                        alt6 = 1;
                    }
                    switch (alt6) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: genericTypeParameterList
                        {
                            pushFollow(FOLLOW_genericTypeParameterList_in_typeDeclaration191);
                            genericTypeParameterList();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:130:62: ( extendsClause )?
                    int alt7 = 2;
                    int LA7_0 = input.LA(1);

                    if ((LA7_0 == EXTENDS_CLAUSE)) {
                        alt7 = 1;
                    }
                    switch (alt7) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: extendsClause
                        {
                            pushFollow(FOLLOW_extendsClause_in_typeDeclaration194);
                            extendsClause();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:130:77: ( implementsClause )?
                    int alt8 = 2;
                    int LA8_0 = input.LA(1);

                    if ((LA8_0 == IMPLEMENTS_CLAUSE)) {
                        alt8 = 1;
                    }
                    switch (alt8) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: implementsClause
                        {
                            pushFollow(FOLLOW_implementsClause_in_typeDeclaration197);
                            implementsClause();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    pushFollow(FOLLOW_classTopLevelScope_in_typeDeclaration200);
                    classTopLevelScope();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:131:9: ^( INTERFACE modifierList IDENT ( genericTypeParameterList )? ( extendsClause )? interfaceTopLevelScope )
                {
                    match(input, INTERFACE, FOLLOW_INTERFACE_in_typeDeclaration212);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_modifierList_in_typeDeclaration214);
                    modifierList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    match(input, IDENT, FOLLOW_IDENT_in_typeDeclaration216);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:131:40: ( genericTypeParameterList )?
                    int alt9 = 2;
                    int LA9_0 = input.LA(1);

                    if ((LA9_0 == GENERIC_TYPE_PARAM_LIST)) {
                        alt9 = 1;
                    }
                    switch (alt9) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: genericTypeParameterList
                        {
                            pushFollow(FOLLOW_genericTypeParameterList_in_typeDeclaration218);
                            genericTypeParameterList();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:131:66: ( extendsClause )?
                    int alt10 = 2;
                    int LA10_0 = input.LA(1);

                    if ((LA10_0 == EXTENDS_CLAUSE)) {
                        alt10 = 1;
                    }
                    switch (alt10) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: extendsClause
                        {
                            pushFollow(FOLLOW_extendsClause_in_typeDeclaration221);
                            extendsClause();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    pushFollow(FOLLOW_interfaceTopLevelScope_in_typeDeclaration224);
                    interfaceTopLevelScope();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 3: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:132:9: ^( ENUM modifierList IDENT ( implementsClause )? enumTopLevelScope )
                {
                    match(input, ENUM, FOLLOW_ENUM_in_typeDeclaration236);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_modifierList_in_typeDeclaration238);
                    modifierList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    match(input, IDENT, FOLLOW_IDENT_in_typeDeclaration240);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:132:35: ( implementsClause )?
                    int alt11 = 2;
                    int LA11_0 = input.LA(1);

                    if ((LA11_0 == IMPLEMENTS_CLAUSE)) {
                        alt11 = 1;
                    }
                    switch (alt11) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: implementsClause
                        {
                            pushFollow(FOLLOW_implementsClause_in_typeDeclaration242);
                            implementsClause();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    pushFollow(FOLLOW_enumTopLevelScope_in_typeDeclaration245);
                    enumTopLevelScope();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 4: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:133:9: ^( AT modifierList IDENT annotationTopLevelScope )
                {
                    match(input, AT, FOLLOW_AT_in_typeDeclaration257);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_modifierList_in_typeDeclaration259);
                    modifierList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    match(input, IDENT, FOLLOW_IDENT_in_typeDeclaration261);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_annotationTopLevelScope_in_typeDeclaration263);
                    annotationTopLevelScope();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 4, typeDeclaration_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "typeDeclaration"

    // $ANTLR start "extendsClause"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:136:1: extendsClause : ^( EXTENDS_CLAUSE ( type )+ ) ;
    public final void extendsClause() throws RecognitionException {
        int extendsClause_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 5)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:138:5: ( ^( EXTENDS_CLAUSE ( type )+ ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:138:9: ^( EXTENDS_CLAUSE ( type )+ )
            {
                match(input, EXTENDS_CLAUSE, FOLLOW_EXTENDS_CLAUSE_in_extendsClause300);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:138:26: ( type )+
                int cnt13 = 0;
                loop13:
                do {
                    int alt13 = 2;
                    int LA13_0 = input.LA(1);

                    if ((LA13_0 == TYPE)) {
                        alt13 = 1;
                    }


                    switch (alt13) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: type
                        {
                            pushFollow(FOLLOW_type_in_extendsClause302);
                            type();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                        default:
                            if (cnt13 >= 1) {
                                break loop13;
                            }
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            EarlyExitException eee =
                                    new EarlyExitException(13, input);
                            throw eee;
                    }
                    cnt13++;
                } while (true);


                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 5, extendsClause_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "extendsClause"

    // $ANTLR start "implementsClause"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:141:1: implementsClause : ^( IMPLEMENTS_CLAUSE ( type )+ ) ;
    public final void implementsClause() throws RecognitionException {
        int implementsClause_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 6)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:142:5: ( ^( IMPLEMENTS_CLAUSE ( type )+ ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:142:9: ^( IMPLEMENTS_CLAUSE ( type )+ )
            {
                match(input, IMPLEMENTS_CLAUSE, FOLLOW_IMPLEMENTS_CLAUSE_in_implementsClause331);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:142:29: ( type )+
                int cnt14 = 0;
                loop14:
                do {
                    int alt14 = 2;
                    int LA14_0 = input.LA(1);

                    if ((LA14_0 == TYPE)) {
                        alt14 = 1;
                    }


                    switch (alt14) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: type
                        {
                            pushFollow(FOLLOW_type_in_implementsClause333);
                            type();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                        default:
                            if (cnt14 >= 1) {
                                break loop14;
                            }
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            EarlyExitException eee =
                                    new EarlyExitException(14, input);
                            throw eee;
                    }
                    cnt14++;
                } while (true);


                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 6, implementsClause_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "implementsClause"

    // $ANTLR start "genericTypeParameterList"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:145:1: genericTypeParameterList : ^( GENERIC_TYPE_PARAM_LIST ( genericTypeParameter )+ ) ;
    public final void genericTypeParameterList() throws RecognitionException {
        int genericTypeParameterList_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 7)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:146:5: ( ^( GENERIC_TYPE_PARAM_LIST ( genericTypeParameter )+ ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:146:9: ^( GENERIC_TYPE_PARAM_LIST ( genericTypeParameter )+ )
            {
                match(input, GENERIC_TYPE_PARAM_LIST, FOLLOW_GENERIC_TYPE_PARAM_LIST_in_genericTypeParameterList363);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:146:35: ( genericTypeParameter )+
                int cnt15 = 0;
                loop15:
                do {
                    int alt15 = 2;
                    int LA15_0 = input.LA(1);

                    if ((LA15_0 == IDENT)) {
                        alt15 = 1;
                    }


                    switch (alt15) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: genericTypeParameter
                        {
                            pushFollow(FOLLOW_genericTypeParameter_in_genericTypeParameterList365);
                            genericTypeParameter();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                        default:
                            if (cnt15 >= 1) {
                                break loop15;
                            }
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            EarlyExitException eee =
                                    new EarlyExitException(15, input);
                            throw eee;
                    }
                    cnt15++;
                } while (true);


                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 7, genericTypeParameterList_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "genericTypeParameterList"

    // $ANTLR start "genericTypeParameter"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:149:1: genericTypeParameter : ^( IDENT ( bound )? ) ;
    public final void genericTypeParameter() throws RecognitionException {
        int genericTypeParameter_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 8)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:150:5: ( ^( IDENT ( bound )? ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:150:9: ^( IDENT ( bound )? )
            {
                match(input, IDENT, FOLLOW_IDENT_in_genericTypeParameter387);
                if (state.failed) {
                    return;
                }

                if (input.LA(1) == Token.DOWN) {
                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:150:17: ( bound )?
                    int alt16 = 2;
                    int LA16_0 = input.LA(1);

                    if ((LA16_0 == EXTENDS_BOUND_LIST)) {
                        alt16 = 1;
                    }
                    switch (alt16) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: bound
                        {
                            pushFollow(FOLLOW_bound_in_genericTypeParameter389);
                            bound();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 8, genericTypeParameter_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "genericTypeParameter"

    // $ANTLR start "bound"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:153:1: bound : ^( EXTENDS_BOUND_LIST ( type )+ ) ;
    public final void bound() throws RecognitionException {
        int bound_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 9)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:154:5: ( ^( EXTENDS_BOUND_LIST ( type )+ ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:154:9: ^( EXTENDS_BOUND_LIST ( type )+ )
            {
                match(input, EXTENDS_BOUND_LIST, FOLLOW_EXTENDS_BOUND_LIST_in_bound419);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:154:30: ( type )+
                int cnt17 = 0;
                loop17:
                do {
                    int alt17 = 2;
                    int LA17_0 = input.LA(1);

                    if ((LA17_0 == TYPE)) {
                        alt17 = 1;
                    }


                    switch (alt17) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: type
                        {
                            pushFollow(FOLLOW_type_in_bound421);
                            type();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                        default:
                            if (cnt17 >= 1) {
                                break loop17;
                            }
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            EarlyExitException eee =
                                    new EarlyExitException(17, input);
                            throw eee;
                    }
                    cnt17++;
                } while (true);


                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 9, bound_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "bound"

    // $ANTLR start "enumTopLevelScope"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:157:1: enumTopLevelScope : ^( ENUM_TOP_LEVEL_SCOPE ( enumConstant )+ ( classTopLevelScope )? ) ;
    public final void enumTopLevelScope() throws RecognitionException {
        int enumTopLevelScope_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 10)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:158:5: ( ^( ENUM_TOP_LEVEL_SCOPE ( enumConstant )+ ( classTopLevelScope )? ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:158:9: ^( ENUM_TOP_LEVEL_SCOPE ( enumConstant )+ ( classTopLevelScope )? )
            {
                match(input, ENUM_TOP_LEVEL_SCOPE, FOLLOW_ENUM_TOP_LEVEL_SCOPE_in_enumTopLevelScope443);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:158:32: ( enumConstant )+
                int cnt18 = 0;
                loop18:
                do {
                    int alt18 = 2;
                    int LA18_0 = input.LA(1);

                    if ((LA18_0 == IDENT)) {
                        alt18 = 1;
                    }


                    switch (alt18) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: enumConstant
                        {
                            pushFollow(FOLLOW_enumConstant_in_enumTopLevelScope445);
                            enumConstant();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                        default:
                            if (cnt18 >= 1) {
                                break loop18;
                            }
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            EarlyExitException eee =
                                    new EarlyExitException(18, input);
                            throw eee;
                    }
                    cnt18++;
                } while (true);

                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:158:46: ( classTopLevelScope )?
                int alt19 = 2;
                int LA19_0 = input.LA(1);

                if ((LA19_0 == CLASS_TOP_LEVEL_SCOPE)) {
                    alt19 = 1;
                }
                switch (alt19) {
                    case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: classTopLevelScope
                    {
                        pushFollow(FOLLOW_classTopLevelScope_in_enumTopLevelScope448);
                        classTopLevelScope();

                        state._fsp--;
                        if (state.failed) {
                            return;
                        }

                    }
                    break;

                }


                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 10, enumTopLevelScope_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "enumTopLevelScope"

    // $ANTLR start "enumConstant"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:161:1: enumConstant : ^( IDENT annotationList ( arguments )? ( classTopLevelScope )? ) ;
    public final void enumConstant() throws RecognitionException {
        int enumConstant_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 11)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:162:5: ( ^( IDENT annotationList ( arguments )? ( classTopLevelScope )? ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:162:9: ^( IDENT annotationList ( arguments )? ( classTopLevelScope )? )
            {
                match(input, IDENT, FOLLOW_IDENT_in_enumConstant474);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_annotationList_in_enumConstant476);
                annotationList();

                state._fsp--;
                if (state.failed) {
                    return;
                }
                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:162:32: ( arguments )?
                int alt20 = 2;
                int LA20_0 = input.LA(1);

                if ((LA20_0 == ARGUMENT_LIST)) {
                    alt20 = 1;
                }
                switch (alt20) {
                    case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: arguments
                    {
                        pushFollow(FOLLOW_arguments_in_enumConstant478);
                        arguments();

                        state._fsp--;
                        if (state.failed) {
                            return;
                        }

                    }
                    break;

                }

                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:162:43: ( classTopLevelScope )?
                int alt21 = 2;
                int LA21_0 = input.LA(1);

                if ((LA21_0 == CLASS_TOP_LEVEL_SCOPE)) {
                    alt21 = 1;
                }
                switch (alt21) {
                    case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: classTopLevelScope
                    {
                        pushFollow(FOLLOW_classTopLevelScope_in_enumConstant481);
                        classTopLevelScope();

                        state._fsp--;
                        if (state.failed) {
                            return;
                        }

                    }
                    break;

                }


                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 11, enumConstant_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "enumConstant"

    // $ANTLR start "classTopLevelScope"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:166:1: classTopLevelScope : ^( CLASS_TOP_LEVEL_SCOPE ( classScopeDeclarations )* ) ;
    public final void classTopLevelScope() throws RecognitionException {
        int classTopLevelScope_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 12)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:167:5: ( ^( CLASS_TOP_LEVEL_SCOPE ( classScopeDeclarations )* ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:167:9: ^( CLASS_TOP_LEVEL_SCOPE ( classScopeDeclarations )* )
            {
                match(input, CLASS_TOP_LEVEL_SCOPE, FOLLOW_CLASS_TOP_LEVEL_SCOPE_in_classTopLevelScope512);
                if (state.failed) {
                    return;
                }

                if (input.LA(1) == Token.DOWN) {
                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:167:33: ( classScopeDeclarations )*
                    loop22:
                    do {
                        int alt22 = 2;
                        int LA22_0 = input.LA(1);

                        if ((LA22_0 == AT || LA22_0 == CLASS || LA22_0 == ENUM || LA22_0 == INTERFACE || (LA22_0 >= CLASS_INSTANCE_INITIALIZER && LA22_0 <= CLASS_STATIC_INITIALIZER) || LA22_0 == CONSTRUCTOR_DECL || LA22_0 == FUNCTION_METHOD_DECL || LA22_0 == VAR_DECLARATION || LA22_0 == VOID_METHOD_DECL)) {
                            alt22 = 1;
                        }


                        switch (alt22) {
                            case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: classScopeDeclarations
                            {
                                pushFollow(FOLLOW_classScopeDeclarations_in_classTopLevelScope514);
                                classScopeDeclarations();

                                state._fsp--;
                                if (state.failed) {
                                    return;
                                }

                            }
                            break;

                            default:
                                break loop22;
                        }
                    } while (true);


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 12, classTopLevelScope_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "classTopLevelScope"

    // $ANTLR start "classScopeDeclarations"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:170:1: classScopeDeclarations : ( ^( CLASS_INSTANCE_INITIALIZER block ) | ^( CLASS_STATIC_INITIALIZER block ) | ^( FUNCTION_METHOD_DECL modifierList ( genericTypeParameterList )? type IDENT formalParameterList ( arrayDeclaratorList )? ( throwsClause )? ( block )? ) | ^( VOID_METHOD_DECL modifierList ( genericTypeParameterList )? IDENT formalParameterList ( throwsClause )? ( block )? ) | ^( VAR_DECLARATION modifierList type variableDeclaratorList ) | ^( CONSTRUCTOR_DECL modifierList ( genericTypeParameterList )? formalParameterList ( throwsClause )? block ) | typeDeclaration );
    public final void classScopeDeclarations() throws RecognitionException {
        int classScopeDeclarations_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 13)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:171:5: ( ^( CLASS_INSTANCE_INITIALIZER block ) | ^( CLASS_STATIC_INITIALIZER block ) | ^( FUNCTION_METHOD_DECL modifierList ( genericTypeParameterList )? type IDENT formalParameterList ( arrayDeclaratorList )? ( throwsClause )? ( block )? ) | ^( VOID_METHOD_DECL modifierList ( genericTypeParameterList )? IDENT formalParameterList ( throwsClause )? ( block )? ) | ^( VAR_DECLARATION modifierList type variableDeclaratorList ) | ^( CONSTRUCTOR_DECL modifierList ( genericTypeParameterList )? formalParameterList ( throwsClause )? block ) | typeDeclaration )
            int alt32 = 7;
            switch (input.LA(1)) {
                case CLASS_INSTANCE_INITIALIZER: {
                    alt32 = 1;
                }
                break;
                case CLASS_STATIC_INITIALIZER: {
                    alt32 = 2;
                }
                break;
                case FUNCTION_METHOD_DECL: {
                    alt32 = 3;
                }
                break;
                case VOID_METHOD_DECL: {
                    alt32 = 4;
                }
                break;
                case VAR_DECLARATION: {
                    alt32 = 5;
                }
                break;
                case CONSTRUCTOR_DECL: {
                    alt32 = 6;
                }
                break;
                case AT:
                case CLASS:
                case ENUM:
                case INTERFACE: {
                    alt32 = 7;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 32, 0, input);

                    throw nvae;
            }

            switch (alt32) {
                case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:171:9: ^( CLASS_INSTANCE_INITIALIZER block )
                {
                    match(input, CLASS_INSTANCE_INITIALIZER, FOLLOW_CLASS_INSTANCE_INITIALIZER_in_classScopeDeclarations540);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_block_in_classScopeDeclarations542);
                    block();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:172:9: ^( CLASS_STATIC_INITIALIZER block )
                {
                    match(input, CLASS_STATIC_INITIALIZER, FOLLOW_CLASS_STATIC_INITIALIZER_in_classScopeDeclarations554);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_block_in_classScopeDeclarations556);
                    block();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 3: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:173:9: ^( FUNCTION_METHOD_DECL modifierList ( genericTypeParameterList )? type IDENT formalParameterList ( arrayDeclaratorList )? ( throwsClause )? ( block )? )
                {
                    match(input, FUNCTION_METHOD_DECL, FOLLOW_FUNCTION_METHOD_DECL_in_classScopeDeclarations568);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_modifierList_in_classScopeDeclarations570);
                    modifierList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:173:45: ( genericTypeParameterList )?
                    int alt23 = 2;
                    int LA23_0 = input.LA(1);

                    if ((LA23_0 == GENERIC_TYPE_PARAM_LIST)) {
                        alt23 = 1;
                    }
                    switch (alt23) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: genericTypeParameterList
                        {
                            pushFollow(FOLLOW_genericTypeParameterList_in_classScopeDeclarations572);
                            genericTypeParameterList();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    pushFollow(FOLLOW_type_in_classScopeDeclarations575);
                    type();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    match(input, IDENT, FOLLOW_IDENT_in_classScopeDeclarations577);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_formalParameterList_in_classScopeDeclarations579);
                    formalParameterList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:173:102: ( arrayDeclaratorList )?
                    int alt24 = 2;
                    int LA24_0 = input.LA(1);

                    if ((LA24_0 == ARRAY_DECLARATOR_LIST)) {
                        alt24 = 1;
                    }
                    switch (alt24) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: arrayDeclaratorList
                        {
                            pushFollow(FOLLOW_arrayDeclaratorList_in_classScopeDeclarations581);
                            arrayDeclaratorList();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:173:123: ( throwsClause )?
                    int alt25 = 2;
                    int LA25_0 = input.LA(1);

                    if ((LA25_0 == THROWS_CLAUSE)) {
                        alt25 = 1;
                    }
                    switch (alt25) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: throwsClause
                        {
                            pushFollow(FOLLOW_throwsClause_in_classScopeDeclarations584);
                            throwsClause();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:173:137: ( block )?
                    int alt26 = 2;
                    int LA26_0 = input.LA(1);

                    if ((LA26_0 == BLOCK_SCOPE)) {
                        alt26 = 1;
                    }
                    switch (alt26) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: block
                        {
                            pushFollow(FOLLOW_block_in_classScopeDeclarations587);
                            block();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 4: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:174:9: ^( VOID_METHOD_DECL modifierList ( genericTypeParameterList )? IDENT formalParameterList ( throwsClause )? ( block )? )
                {
                    match(input, VOID_METHOD_DECL, FOLLOW_VOID_METHOD_DECL_in_classScopeDeclarations600);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_modifierList_in_classScopeDeclarations602);
                    modifierList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:174:41: ( genericTypeParameterList )?
                    int alt27 = 2;
                    int LA27_0 = input.LA(1);

                    if ((LA27_0 == GENERIC_TYPE_PARAM_LIST)) {
                        alt27 = 1;
                    }
                    switch (alt27) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: genericTypeParameterList
                        {
                            pushFollow(FOLLOW_genericTypeParameterList_in_classScopeDeclarations604);
                            genericTypeParameterList();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    match(input, IDENT, FOLLOW_IDENT_in_classScopeDeclarations607);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_formalParameterList_in_classScopeDeclarations609);
                    formalParameterList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:174:93: ( throwsClause )?
                    int alt28 = 2;
                    int LA28_0 = input.LA(1);

                    if ((LA28_0 == THROWS_CLAUSE)) {
                        alt28 = 1;
                    }
                    switch (alt28) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: throwsClause
                        {
                            pushFollow(FOLLOW_throwsClause_in_classScopeDeclarations611);
                            throwsClause();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:174:107: ( block )?
                    int alt29 = 2;
                    int LA29_0 = input.LA(1);

                    if ((LA29_0 == BLOCK_SCOPE)) {
                        alt29 = 1;
                    }
                    switch (alt29) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: block
                        {
                            pushFollow(FOLLOW_block_in_classScopeDeclarations614);
                            block();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 5: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:175:9: ^( VAR_DECLARATION modifierList type variableDeclaratorList )
                {
                    match(input, VAR_DECLARATION, FOLLOW_VAR_DECLARATION_in_classScopeDeclarations627);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_modifierList_in_classScopeDeclarations629);
                    modifierList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_type_in_classScopeDeclarations631);
                    type();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_variableDeclaratorList_in_classScopeDeclarations633);
                    variableDeclaratorList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 6: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:176:9: ^( CONSTRUCTOR_DECL modifierList ( genericTypeParameterList )? formalParameterList ( throwsClause )? block )
                {
                    match(input, CONSTRUCTOR_DECL, FOLLOW_CONSTRUCTOR_DECL_in_classScopeDeclarations645);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_modifierList_in_classScopeDeclarations647);
                    modifierList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:176:41: ( genericTypeParameterList )?
                    int alt30 = 2;
                    int LA30_0 = input.LA(1);

                    if ((LA30_0 == GENERIC_TYPE_PARAM_LIST)) {
                        alt30 = 1;
                    }
                    switch (alt30) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: genericTypeParameterList
                        {
                            pushFollow(FOLLOW_genericTypeParameterList_in_classScopeDeclarations649);
                            genericTypeParameterList();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    pushFollow(FOLLOW_formalParameterList_in_classScopeDeclarations652);
                    formalParameterList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:176:87: ( throwsClause )?
                    int alt31 = 2;
                    int LA31_0 = input.LA(1);

                    if ((LA31_0 == THROWS_CLAUSE)) {
                        alt31 = 1;
                    }
                    switch (alt31) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: throwsClause
                        {
                            pushFollow(FOLLOW_throwsClause_in_classScopeDeclarations654);
                            throwsClause();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    pushFollow(FOLLOW_block_in_classScopeDeclarations657);
                    block();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 7: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:177:9: typeDeclaration
                {
                    pushFollow(FOLLOW_typeDeclaration_in_classScopeDeclarations668);
                    typeDeclaration();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 13, classScopeDeclarations_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "classScopeDeclarations"

    // $ANTLR start "interfaceTopLevelScope"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:180:1: interfaceTopLevelScope : ^( INTERFACE_TOP_LEVEL_SCOPE ( interfaceScopeDeclarations )* ) ;
    public final void interfaceTopLevelScope() throws RecognitionException {
        int interfaceTopLevelScope_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 14)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:181:5: ( ^( INTERFACE_TOP_LEVEL_SCOPE ( interfaceScopeDeclarations )* ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:181:9: ^( INTERFACE_TOP_LEVEL_SCOPE ( interfaceScopeDeclarations )* )
            {
                match(input, INTERFACE_TOP_LEVEL_SCOPE, FOLLOW_INTERFACE_TOP_LEVEL_SCOPE_in_interfaceTopLevelScope692);
                if (state.failed) {
                    return;
                }

                if (input.LA(1) == Token.DOWN) {
                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:181:37: ( interfaceScopeDeclarations )*
                    loop33:
                    do {
                        int alt33 = 2;
                        int LA33_0 = input.LA(1);

                        if ((LA33_0 == AT || LA33_0 == CLASS || LA33_0 == ENUM || LA33_0 == INTERFACE || LA33_0 == FUNCTION_METHOD_DECL || LA33_0 == VAR_DECLARATION || LA33_0 == VOID_METHOD_DECL)) {
                            alt33 = 1;
                        }


                        switch (alt33) {
                            case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: interfaceScopeDeclarations
                            {
                                pushFollow(FOLLOW_interfaceScopeDeclarations_in_interfaceTopLevelScope694);
                                interfaceScopeDeclarations();

                                state._fsp--;
                                if (state.failed) {
                                    return;
                                }

                            }
                            break;

                            default:
                                break loop33;
                        }
                    } while (true);


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 14, interfaceTopLevelScope_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "interfaceTopLevelScope"

    // $ANTLR start "interfaceScopeDeclarations"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:184:1: interfaceScopeDeclarations : ( ^( FUNCTION_METHOD_DECL modifierList ( genericTypeParameterList )? type IDENT formalParameterList ( arrayDeclaratorList )? ( throwsClause )? ) | ^( VOID_METHOD_DECL modifierList ( genericTypeParameterList )? IDENT formalParameterList ( throwsClause )? ) | ^( VAR_DECLARATION modifierList type variableDeclaratorList ) | typeDeclaration );
    public final void interfaceScopeDeclarations() throws RecognitionException {
        int interfaceScopeDeclarations_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 15)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:185:5: ( ^( FUNCTION_METHOD_DECL modifierList ( genericTypeParameterList )? type IDENT formalParameterList ( arrayDeclaratorList )? ( throwsClause )? ) | ^( VOID_METHOD_DECL modifierList ( genericTypeParameterList )? IDENT formalParameterList ( throwsClause )? ) | ^( VAR_DECLARATION modifierList type variableDeclaratorList ) | typeDeclaration )
            int alt39 = 4;
            switch (input.LA(1)) {
                case FUNCTION_METHOD_DECL: {
                    alt39 = 1;
                }
                break;
                case VOID_METHOD_DECL: {
                    alt39 = 2;
                }
                break;
                case VAR_DECLARATION: {
                    alt39 = 3;
                }
                break;
                case AT:
                case CLASS:
                case ENUM:
                case INTERFACE: {
                    alt39 = 4;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 39, 0, input);

                    throw nvae;
            }

            switch (alt39) {
                case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:185:9: ^( FUNCTION_METHOD_DECL modifierList ( genericTypeParameterList )? type IDENT formalParameterList ( arrayDeclaratorList )? ( throwsClause )? )
                {
                    match(input, FUNCTION_METHOD_DECL, FOLLOW_FUNCTION_METHOD_DECL_in_interfaceScopeDeclarations720);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_modifierList_in_interfaceScopeDeclarations722);
                    modifierList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:185:45: ( genericTypeParameterList )?
                    int alt34 = 2;
                    int LA34_0 = input.LA(1);

                    if ((LA34_0 == GENERIC_TYPE_PARAM_LIST)) {
                        alt34 = 1;
                    }
                    switch (alt34) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: genericTypeParameterList
                        {
                            pushFollow(FOLLOW_genericTypeParameterList_in_interfaceScopeDeclarations724);
                            genericTypeParameterList();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    pushFollow(FOLLOW_type_in_interfaceScopeDeclarations727);
                    type();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    match(input, IDENT, FOLLOW_IDENT_in_interfaceScopeDeclarations729);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_formalParameterList_in_interfaceScopeDeclarations731);
                    formalParameterList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:185:102: ( arrayDeclaratorList )?
                    int alt35 = 2;
                    int LA35_0 = input.LA(1);

                    if ((LA35_0 == ARRAY_DECLARATOR_LIST)) {
                        alt35 = 1;
                    }
                    switch (alt35) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: arrayDeclaratorList
                        {
                            pushFollow(FOLLOW_arrayDeclaratorList_in_interfaceScopeDeclarations733);
                            arrayDeclaratorList();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:185:123: ( throwsClause )?
                    int alt36 = 2;
                    int LA36_0 = input.LA(1);

                    if ((LA36_0 == THROWS_CLAUSE)) {
                        alt36 = 1;
                    }
                    switch (alt36) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: throwsClause
                        {
                            pushFollow(FOLLOW_throwsClause_in_interfaceScopeDeclarations736);
                            throwsClause();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:186:9: ^( VOID_METHOD_DECL modifierList ( genericTypeParameterList )? IDENT formalParameterList ( throwsClause )? )
                {
                    match(input, VOID_METHOD_DECL, FOLLOW_VOID_METHOD_DECL_in_interfaceScopeDeclarations749);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_modifierList_in_interfaceScopeDeclarations751);
                    modifierList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:186:41: ( genericTypeParameterList )?
                    int alt37 = 2;
                    int LA37_0 = input.LA(1);

                    if ((LA37_0 == GENERIC_TYPE_PARAM_LIST)) {
                        alt37 = 1;
                    }
                    switch (alt37) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: genericTypeParameterList
                        {
                            pushFollow(FOLLOW_genericTypeParameterList_in_interfaceScopeDeclarations753);
                            genericTypeParameterList();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    match(input, IDENT, FOLLOW_IDENT_in_interfaceScopeDeclarations756);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_formalParameterList_in_interfaceScopeDeclarations758);
                    formalParameterList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:186:93: ( throwsClause )?
                    int alt38 = 2;
                    int LA38_0 = input.LA(1);

                    if ((LA38_0 == THROWS_CLAUSE)) {
                        alt38 = 1;
                    }
                    switch (alt38) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: throwsClause
                        {
                            pushFollow(FOLLOW_throwsClause_in_interfaceScopeDeclarations760);
                            throwsClause();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 3: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:190:9: ^( VAR_DECLARATION modifierList type variableDeclaratorList )
                {
                    match(input, VAR_DECLARATION, FOLLOW_VAR_DECLARATION_in_interfaceScopeDeclarations851);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_modifierList_in_interfaceScopeDeclarations853);
                    modifierList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_type_in_interfaceScopeDeclarations855);
                    type();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_variableDeclaratorList_in_interfaceScopeDeclarations857);
                    variableDeclaratorList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 4: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:191:9: typeDeclaration
                {
                    pushFollow(FOLLOW_typeDeclaration_in_interfaceScopeDeclarations868);
                    typeDeclaration();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 15, interfaceScopeDeclarations_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "interfaceScopeDeclarations"

    // $ANTLR start "variableDeclaratorList"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:194:1: variableDeclaratorList : ^( VAR_DECLARATOR_LIST ( variableDeclarator )+ ) ;
    public final void variableDeclaratorList() throws RecognitionException {
        int variableDeclaratorList_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 16)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:195:5: ( ^( VAR_DECLARATOR_LIST ( variableDeclarator )+ ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:195:9: ^( VAR_DECLARATOR_LIST ( variableDeclarator )+ )
            {
                match(input, VAR_DECLARATOR_LIST, FOLLOW_VAR_DECLARATOR_LIST_in_variableDeclaratorList888);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:195:31: ( variableDeclarator )+
                int cnt40 = 0;
                loop40:
                do {
                    int alt40 = 2;
                    int LA40_0 = input.LA(1);

                    if ((LA40_0 == VAR_DECLARATOR)) {
                        alt40 = 1;
                    }


                    switch (alt40) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: variableDeclarator
                        {
                            pushFollow(FOLLOW_variableDeclarator_in_variableDeclaratorList890);
                            variableDeclarator();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                        default:
                            if (cnt40 >= 1) {
                                break loop40;
                            }
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            EarlyExitException eee =
                                    new EarlyExitException(40, input);
                            throw eee;
                    }
                    cnt40++;
                } while (true);


                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 16, variableDeclaratorList_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "variableDeclaratorList"

    // $ANTLR start "variableDeclarator"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:198:1: variableDeclarator : ^( VAR_DECLARATOR variableDeclaratorId ( variableInitializer )? ) ;
    public final void variableDeclarator() throws RecognitionException {
        int variableDeclarator_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 17)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:199:5: ( ^( VAR_DECLARATOR variableDeclaratorId ( variableInitializer )? ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:199:9: ^( VAR_DECLARATOR variableDeclaratorId ( variableInitializer )? )
            {
                match(input, VAR_DECLARATOR, FOLLOW_VAR_DECLARATOR_in_variableDeclarator912);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_variableDeclaratorId_in_variableDeclarator914);
                variableDeclaratorId();

                state._fsp--;
                if (state.failed) {
                    return;
                }
                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:199:47: ( variableInitializer )?
                int alt41 = 2;
                int LA41_0 = input.LA(1);

                if ((LA41_0 == ARRAY_INITIALIZER || LA41_0 == EXPR)) {
                    alt41 = 1;
                }
                switch (alt41) {
                    case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: variableInitializer
                    {
                        pushFollow(FOLLOW_variableInitializer_in_variableDeclarator916);
                        variableInitializer();

                        state._fsp--;
                        if (state.failed) {
                            return;
                        }

                    }
                    break;

                }


                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 17, variableDeclarator_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "variableDeclarator"

    // $ANTLR start "variableDeclaratorId"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:202:1: variableDeclaratorId : ^( IDENT ( arrayDeclaratorList )? ) ;
    public final void variableDeclaratorId() throws RecognitionException {
        int variableDeclaratorId_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 18)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:203:5: ( ^( IDENT ( arrayDeclaratorList )? ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:203:9: ^( IDENT ( arrayDeclaratorList )? )
            {
                match(input, IDENT, FOLLOW_IDENT_in_variableDeclaratorId942);
                if (state.failed) {
                    return;
                }

                if (input.LA(1) == Token.DOWN) {
                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:203:17: ( arrayDeclaratorList )?
                    int alt42 = 2;
                    int LA42_0 = input.LA(1);

                    if ((LA42_0 == ARRAY_DECLARATOR_LIST)) {
                        alt42 = 1;
                    }
                    switch (alt42) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: arrayDeclaratorList
                        {
                            pushFollow(FOLLOW_arrayDeclaratorList_in_variableDeclaratorId944);
                            arrayDeclaratorList();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 18, variableDeclaratorId_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "variableDeclaratorId"

    // $ANTLR start "variableInitializer"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:206:1: variableInitializer : ( arrayInitializer | expression );
    public final void variableInitializer() throws RecognitionException {
        int variableInitializer_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 19)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:207:5: ( arrayInitializer | expression )
            int alt43 = 2;
            int LA43_0 = input.LA(1);

            if ((LA43_0 == ARRAY_INITIALIZER)) {
                alt43 = 1;
            } else if ((LA43_0 == EXPR)) {
                alt43 = 2;
            } else {
                if (state.backtracking > 0) {
                    state.failed = true;
                    return;
                }
                NoViableAltException nvae =
                        new NoViableAltException("", 43, 0, input);

                throw nvae;
            }
            switch (alt43) {
                case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:207:9: arrayInitializer
                {
                    pushFollow(FOLLOW_arrayInitializer_in_variableInitializer965);
                    arrayInitializer();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:208:9: expression
                {
                    pushFollow(FOLLOW_expression_in_variableInitializer975);
                    expression();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 19, variableInitializer_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "variableInitializer"

    // $ANTLR start "arrayDeclarator"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:211:1: arrayDeclarator : LBRACK RBRACK ;
    public final void arrayDeclarator() throws RecognitionException {
        int arrayDeclarator_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 20)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:212:5: ( LBRACK RBRACK )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:212:9: LBRACK RBRACK
            {
                match(input, LBRACK, FOLLOW_LBRACK_in_arrayDeclarator994);
                if (state.failed) {
                    return;
                }
                match(input, RBRACK, FOLLOW_RBRACK_in_arrayDeclarator996);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 20, arrayDeclarator_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "arrayDeclarator"

    // $ANTLR start "arrayDeclaratorList"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:215:1: arrayDeclaratorList : ^( ARRAY_DECLARATOR_LIST ( ARRAY_DECLARATOR )* ) ;
    public final void arrayDeclaratorList() throws RecognitionException {
        int arrayDeclaratorList_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 21)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:216:5: ( ^( ARRAY_DECLARATOR_LIST ( ARRAY_DECLARATOR )* ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:216:9: ^( ARRAY_DECLARATOR_LIST ( ARRAY_DECLARATOR )* )
            {
                match(input, ARRAY_DECLARATOR_LIST, FOLLOW_ARRAY_DECLARATOR_LIST_in_arrayDeclaratorList1016);
                if (state.failed) {
                    return;
                }

                if (input.LA(1) == Token.DOWN) {
                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:216:33: ( ARRAY_DECLARATOR )*
                    loop44:
                    do {
                        int alt44 = 2;
                        int LA44_0 = input.LA(1);

                        if ((LA44_0 == ARRAY_DECLARATOR)) {
                            alt44 = 1;
                        }


                        switch (alt44) {
                            case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: ARRAY_DECLARATOR
                            {
                                match(input, ARRAY_DECLARATOR, FOLLOW_ARRAY_DECLARATOR_in_arrayDeclaratorList1018);
                                if (state.failed) {
                                    return;
                                }

                            }
                            break;

                            default:
                                break loop44;
                        }
                    } while (true);


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 21, arrayDeclaratorList_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "arrayDeclaratorList"

    // $ANTLR start "arrayInitializer"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:219:1: arrayInitializer : ^( ARRAY_INITIALIZER ( variableInitializer )* ) ;
    public final void arrayInitializer() throws RecognitionException {
        int arrayInitializer_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 22)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:220:5: ( ^( ARRAY_INITIALIZER ( variableInitializer )* ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:220:9: ^( ARRAY_INITIALIZER ( variableInitializer )* )
            {
                match(input, ARRAY_INITIALIZER, FOLLOW_ARRAY_INITIALIZER_in_arrayInitializer1046);
                if (state.failed) {
                    return;
                }

                if (input.LA(1) == Token.DOWN) {
                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:220:29: ( variableInitializer )*
                    loop45:
                    do {
                        int alt45 = 2;
                        int LA45_0 = input.LA(1);

                        if ((LA45_0 == ARRAY_INITIALIZER || LA45_0 == EXPR)) {
                            alt45 = 1;
                        }


                        switch (alt45) {
                            case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: variableInitializer
                            {
                                pushFollow(FOLLOW_variableInitializer_in_arrayInitializer1048);
                                variableInitializer();

                                state._fsp--;
                                if (state.failed) {
                                    return;
                                }

                            }
                            break;

                            default:
                                break loop45;
                        }
                    } while (true);


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 22, arrayInitializer_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "arrayInitializer"

    // $ANTLR start "throwsClause"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:223:1: throwsClause : ^( THROWS_CLAUSE ( qualifiedIdentifier )+ ) ;
    public final void throwsClause() throws RecognitionException {
        int throwsClause_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 23)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:224:5: ( ^( THROWS_CLAUSE ( qualifiedIdentifier )+ ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:224:9: ^( THROWS_CLAUSE ( qualifiedIdentifier )+ )
            {
                match(input, THROWS_CLAUSE, FOLLOW_THROWS_CLAUSE_in_throwsClause1070);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:224:25: ( qualifiedIdentifier )+
                int cnt46 = 0;
                loop46:
                do {
                    int alt46 = 2;
                    int LA46_0 = input.LA(1);

                    if ((LA46_0 == DOT || LA46_0 == IDENT)) {
                        alt46 = 1;
                    }


                    switch (alt46) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: qualifiedIdentifier
                        {
                            pushFollow(FOLLOW_qualifiedIdentifier_in_throwsClause1072);
                            qualifiedIdentifier();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                        default:
                            if (cnt46 >= 1) {
                                break loop46;
                            }
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            EarlyExitException eee =
                                    new EarlyExitException(46, input);
                            throw eee;
                    }
                    cnt46++;
                } while (true);


                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 23, throwsClause_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "throwsClause"

    // $ANTLR start "modifierList"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:227:1: modifierList : ^( MODIFIER_LIST ( modifier )* ) ;
    public final void modifierList() throws RecognitionException {
        int modifierList_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 24)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:228:5: ( ^( MODIFIER_LIST ( modifier )* ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:228:9: ^( MODIFIER_LIST ( modifier )* )
            {
                match(input, MODIFIER_LIST, FOLLOW_MODIFIER_LIST_in_modifierList1094);
                if (state.failed) {
                    return;
                }

                if (input.LA(1) == Token.DOWN) {
                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:228:25: ( modifier )*
                    loop47:
                    do {
                        int alt47 = 2;
                        int LA47_0 = input.LA(1);

                        if ((LA47_0 == AT || LA47_0 == ABSTRACT || LA47_0 == FINAL || LA47_0 == NATIVE || (LA47_0 >= PRIVATE && LA47_0 <= PUBLIC) || (LA47_0 >= STATIC && LA47_0 <= STRICTFP) || LA47_0 == SYNCHRONIZED || LA47_0 == TRANSIENT || LA47_0 == VOLATILE)) {
                            alt47 = 1;
                        }


                        switch (alt47) {
                            case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: modifier
                            {
                                pushFollow(FOLLOW_modifier_in_modifierList1096);
                                modifier();

                                state._fsp--;
                                if (state.failed) {
                                    return;
                                }

                            }
                            break;

                            default:
                                break loop47;
                        }
                    } while (true);


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 24, modifierList_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "modifierList"

    // $ANTLR start "modifier"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:231:1: modifier : ( PUBLIC | PROTECTED | PRIVATE | STATIC | ABSTRACT | NATIVE | SYNCHRONIZED | TRANSIENT | VOLATILE | STRICTFP | localModifier );
    public final void modifier() throws RecognitionException {
        int modifier_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 25)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:232:5: ( PUBLIC | PROTECTED | PRIVATE | STATIC | ABSTRACT | NATIVE | SYNCHRONIZED | TRANSIENT | VOLATILE | STRICTFP | localModifier )
            int alt48 = 11;
            switch (input.LA(1)) {
                case PUBLIC: {
                    alt48 = 1;
                }
                break;
                case PROTECTED: {
                    alt48 = 2;
                }
                break;
                case PRIVATE: {
                    alt48 = 3;
                }
                break;
                case STATIC: {
                    alt48 = 4;
                }
                break;
                case ABSTRACT: {
                    alt48 = 5;
                }
                break;
                case NATIVE: {
                    alt48 = 6;
                }
                break;
                case SYNCHRONIZED: {
                    alt48 = 7;
                }
                break;
                case TRANSIENT: {
                    alt48 = 8;
                }
                break;
                case VOLATILE: {
                    alt48 = 9;
                }
                break;
                case STRICTFP: {
                    alt48 = 10;
                }
                break;
                case AT:
                case FINAL: {
                    alt48 = 11;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 48, 0, input);

                    throw nvae;
            }

            switch (alt48) {
                case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:232:9: PUBLIC
                {
                    match(input, PUBLIC, FOLLOW_PUBLIC_in_modifier1117);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:233:9: PROTECTED
                {
                    match(input, PROTECTED, FOLLOW_PROTECTED_in_modifier1127);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 3: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:234:9: PRIVATE
                {
                    match(input, PRIVATE, FOLLOW_PRIVATE_in_modifier1137);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 4: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:235:9: STATIC
                {
                    match(input, STATIC, FOLLOW_STATIC_in_modifier1147);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 5: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:236:9: ABSTRACT
                {
                    match(input, ABSTRACT, FOLLOW_ABSTRACT_in_modifier1157);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 6: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:237:9: NATIVE
                {
                    match(input, NATIVE, FOLLOW_NATIVE_in_modifier1167);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 7: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:238:9: SYNCHRONIZED
                {
                    match(input, SYNCHRONIZED, FOLLOW_SYNCHRONIZED_in_modifier1177);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 8: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:239:9: TRANSIENT
                {
                    match(input, TRANSIENT, FOLLOW_TRANSIENT_in_modifier1187);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 9: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:240:9: VOLATILE
                {
                    match(input, VOLATILE, FOLLOW_VOLATILE_in_modifier1197);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 10: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:241:9: STRICTFP
                {
                    match(input, STRICTFP, FOLLOW_STRICTFP_in_modifier1207);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 11: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:242:9: localModifier
                {
                    pushFollow(FOLLOW_localModifier_in_modifier1217);
                    localModifier();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 25, modifier_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "modifier"

    // $ANTLR start "localModifierList"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:245:1: localModifierList : ^( LOCAL_MODIFIER_LIST ( localModifier )* ) ;
    public final void localModifierList() throws RecognitionException {
        int localModifierList_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 26)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:246:5: ( ^( LOCAL_MODIFIER_LIST ( localModifier )* ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:246:9: ^( LOCAL_MODIFIER_LIST ( localModifier )* )
            {
                match(input, LOCAL_MODIFIER_LIST, FOLLOW_LOCAL_MODIFIER_LIST_in_localModifierList1237);
                if (state.failed) {
                    return;
                }

                if (input.LA(1) == Token.DOWN) {
                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:246:31: ( localModifier )*
                    loop49:
                    do {
                        int alt49 = 2;
                        int LA49_0 = input.LA(1);

                        if ((LA49_0 == AT || LA49_0 == FINAL)) {
                            alt49 = 1;
                        }


                        switch (alt49) {
                            case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: localModifier
                            {
                                pushFollow(FOLLOW_localModifier_in_localModifierList1239);
                                localModifier();

                                state._fsp--;
                                if (state.failed) {
                                    return;
                                }

                            }
                            break;

                            default:
                                break loop49;
                        }
                    } while (true);


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 26, localModifierList_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "localModifierList"

    // $ANTLR start "localModifier"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:249:1: localModifier : ( FINAL | annotation );
    public final void localModifier() throws RecognitionException {
        int localModifier_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 27)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:250:5: ( FINAL | annotation )
            int alt50 = 2;
            int LA50_0 = input.LA(1);

            if ((LA50_0 == FINAL)) {
                alt50 = 1;
            } else if ((LA50_0 == AT)) {
                alt50 = 2;
            } else {
                if (state.backtracking > 0) {
                    state.failed = true;
                    return;
                }
                NoViableAltException nvae =
                        new NoViableAltException("", 50, 0, input);

                throw nvae;
            }
            switch (alt50) {
                case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:250:9: FINAL
                {
                    match(input, FINAL, FOLLOW_FINAL_in_localModifier1260);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:251:9: annotation
                {
                    pushFollow(FOLLOW_annotation_in_localModifier1270);
                    annotation();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 27, localModifier_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "localModifier"

    // $ANTLR start "type"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:254:1: type : ^( TYPE ( primitiveType | qualifiedTypeIdent ) ( arrayDeclaratorList )? ) ;
    public final void type() throws RecognitionException {
        int type_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 28)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:255:5: ( ^( TYPE ( primitiveType | qualifiedTypeIdent ) ( arrayDeclaratorList )? ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:255:9: ^( TYPE ( primitiveType | qualifiedTypeIdent ) ( arrayDeclaratorList )? )
            {
                match(input, TYPE, FOLLOW_TYPE_in_type1290);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:255:16: ( primitiveType | qualifiedTypeIdent )
                int alt51 = 2;
                int LA51_0 = input.LA(1);

                if ((LA51_0 == BOOLEAN || LA51_0 == BYTE || LA51_0 == CHAR || LA51_0 == DOUBLE || LA51_0 == FLOAT || (LA51_0 >= INT && LA51_0 <= LONG) || LA51_0 == SHORT)) {
                    alt51 = 1;
                } else if ((LA51_0 == QUALIFIED_TYPE_IDENT)) {
                    alt51 = 2;
                } else {
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 51, 0, input);

                    throw nvae;
                }
                switch (alt51) {
                    case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:255:17: primitiveType
                    {
                        pushFollow(FOLLOW_primitiveType_in_type1293);
                        primitiveType();

                        state._fsp--;
                        if (state.failed) {
                            return;
                        }

                    }
                    break;
                    case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:255:33: qualifiedTypeIdent
                    {
                        pushFollow(FOLLOW_qualifiedTypeIdent_in_type1297);
                        qualifiedTypeIdent();

                        state._fsp--;
                        if (state.failed) {
                            return;
                        }

                    }
                    break;

                }

                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:255:53: ( arrayDeclaratorList )?
                int alt52 = 2;
                int LA52_0 = input.LA(1);

                if ((LA52_0 == ARRAY_DECLARATOR_LIST)) {
                    alt52 = 1;
                }
                switch (alt52) {
                    case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: arrayDeclaratorList
                    {
                        pushFollow(FOLLOW_arrayDeclaratorList_in_type1300);
                        arrayDeclaratorList();

                        state._fsp--;
                        if (state.failed) {
                            return;
                        }

                    }
                    break;

                }


                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 28, type_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "type"

    // $ANTLR start "qualifiedTypeIdent"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:258:1: qualifiedTypeIdent : ^( QUALIFIED_TYPE_IDENT ( typeIdent )+ ) ;
    public final void qualifiedTypeIdent() throws RecognitionException {
        int qualifiedTypeIdent_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 29)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:259:5: ( ^( QUALIFIED_TYPE_IDENT ( typeIdent )+ ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:259:9: ^( QUALIFIED_TYPE_IDENT ( typeIdent )+ )
            {
                match(input, QUALIFIED_TYPE_IDENT, FOLLOW_QUALIFIED_TYPE_IDENT_in_qualifiedTypeIdent1322);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:259:32: ( typeIdent )+
                int cnt53 = 0;
                loop53:
                do {
                    int alt53 = 2;
                    int LA53_0 = input.LA(1);

                    if ((LA53_0 == IDENT)) {
                        alt53 = 1;
                    }


                    switch (alt53) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: typeIdent
                        {
                            pushFollow(FOLLOW_typeIdent_in_qualifiedTypeIdent1324);
                            typeIdent();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                        default:
                            if (cnt53 >= 1) {
                                break loop53;
                            }
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            EarlyExitException eee =
                                    new EarlyExitException(53, input);
                            throw eee;
                    }
                    cnt53++;
                } while (true);


                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 29, qualifiedTypeIdent_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "qualifiedTypeIdent"

    // $ANTLR start "typeIdent"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:262:1: typeIdent : ^( IDENT ( genericTypeArgumentList )? ) ;
    public final void typeIdent() throws RecognitionException {
        int typeIdent_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 30)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:263:5: ( ^( IDENT ( genericTypeArgumentList )? ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:263:9: ^( IDENT ( genericTypeArgumentList )? )
            {
                match(input, IDENT, FOLLOW_IDENT_in_typeIdent1347);
                if (state.failed) {
                    return;
                }

                if (input.LA(1) == Token.DOWN) {
                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:263:17: ( genericTypeArgumentList )?
                    int alt54 = 2;
                    int LA54_0 = input.LA(1);

                    if ((LA54_0 == GENERIC_TYPE_ARG_LIST)) {
                        alt54 = 1;
                    }
                    switch (alt54) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: genericTypeArgumentList
                        {
                            pushFollow(FOLLOW_genericTypeArgumentList_in_typeIdent1349);
                            genericTypeArgumentList();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 30, typeIdent_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "typeIdent"

    // $ANTLR start "primitiveType"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:266:1: primitiveType : ( BOOLEAN | CHAR | BYTE | SHORT | INT | LONG | FLOAT | DOUBLE );
    public final void primitiveType() throws RecognitionException {
        int primitiveType_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 31)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:267:5: ( BOOLEAN | CHAR | BYTE | SHORT | INT | LONG | FLOAT | DOUBLE )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:
            {
                if (input.LA(1) == BOOLEAN || input.LA(1) == BYTE || input.LA(1) == CHAR || input.LA(1) == DOUBLE || input.LA(1) == FLOAT || (input.LA(1) >= INT && input.LA(1) <= LONG) || input.LA(1) == SHORT) {
                    input.consume();
                    state.errorRecovery = false;
                    state.failed = false;
                } else {
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    throw mse;
                }


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 31, primitiveType_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "primitiveType"

    // $ANTLR start "genericTypeArgumentList"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:277:1: genericTypeArgumentList : ^( GENERIC_TYPE_ARG_LIST ( genericTypeArgument )+ ) ;
    public final void genericTypeArgumentList() throws RecognitionException {
        int genericTypeArgumentList_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 32)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:278:5: ( ^( GENERIC_TYPE_ARG_LIST ( genericTypeArgument )+ ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:278:9: ^( GENERIC_TYPE_ARG_LIST ( genericTypeArgument )+ )
            {
                match(input, GENERIC_TYPE_ARG_LIST, FOLLOW_GENERIC_TYPE_ARG_LIST_in_genericTypeArgumentList1460);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:278:33: ( genericTypeArgument )+
                int cnt55 = 0;
                loop55:
                do {
                    int alt55 = 2;
                    int LA55_0 = input.LA(1);

                    if ((LA55_0 == QUESTION || LA55_0 == TYPE)) {
                        alt55 = 1;
                    }


                    switch (alt55) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: genericTypeArgument
                        {
                            pushFollow(FOLLOW_genericTypeArgument_in_genericTypeArgumentList1462);
                            genericTypeArgument();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                        default:
                            if (cnt55 >= 1) {
                                break loop55;
                            }
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            EarlyExitException eee =
                                    new EarlyExitException(55, input);
                            throw eee;
                    }
                    cnt55++;
                } while (true);


                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 32, genericTypeArgumentList_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "genericTypeArgumentList"

    // $ANTLR start "genericTypeArgument"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:281:1: genericTypeArgument : ( type | ^( QUESTION ( genericWildcardBoundType )? ) );
    public final void genericTypeArgument() throws RecognitionException {
        int genericTypeArgument_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 33)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:282:5: ( type | ^( QUESTION ( genericWildcardBoundType )? ) )
            int alt57 = 2;
            int LA57_0 = input.LA(1);

            if ((LA57_0 == TYPE)) {
                alt57 = 1;
            } else if ((LA57_0 == QUESTION)) {
                alt57 = 2;
            } else {
                if (state.backtracking > 0) {
                    state.failed = true;
                    return;
                }
                NoViableAltException nvae =
                        new NoViableAltException("", 57, 0, input);

                throw nvae;
            }
            switch (alt57) {
                case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:282:9: type
                {
                    pushFollow(FOLLOW_type_in_genericTypeArgument1487);
                    type();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:283:9: ^( QUESTION ( genericWildcardBoundType )? )
                {
                    match(input, QUESTION, FOLLOW_QUESTION_in_genericTypeArgument1498);
                    if (state.failed) {
                        return;
                    }

                    if (input.LA(1) == Token.DOWN) {
                        match(input, Token.DOWN, null);
                        if (state.failed) {
                            return;
                        }
                        // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:283:20: ( genericWildcardBoundType )?
                        int alt56 = 2;
                        int LA56_0 = input.LA(1);

                        if ((LA56_0 == EXTENDS || LA56_0 == SUPER)) {
                            alt56 = 1;
                        }
                        switch (alt56) {
                            case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: genericWildcardBoundType
                            {
                                pushFollow(FOLLOW_genericWildcardBoundType_in_genericTypeArgument1500);
                                genericWildcardBoundType();

                                state._fsp--;
                                if (state.failed) {
                                    return;
                                }

                            }
                            break;

                        }


                        match(input, Token.UP, null);
                        if (state.failed) {
                            return;
                        }
                    }

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 33, genericTypeArgument_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "genericTypeArgument"

    // $ANTLR start "genericWildcardBoundType"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:286:1: genericWildcardBoundType : ( ^( EXTENDS type ) | ^( SUPER type ) );
    public final void genericWildcardBoundType() throws RecognitionException {
        int genericWildcardBoundType_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 34)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:287:5: ( ^( EXTENDS type ) | ^( SUPER type ) )
            int alt58 = 2;
            int LA58_0 = input.LA(1);

            if ((LA58_0 == EXTENDS)) {
                alt58 = 1;
            } else if ((LA58_0 == SUPER)) {
                alt58 = 2;
            } else {
                if (state.backtracking > 0) {
                    state.failed = true;
                    return;
                }
                NoViableAltException nvae =
                        new NoViableAltException("", 58, 0, input);

                throw nvae;
            }
            switch (alt58) {
                case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:287:9: ^( EXTENDS type )
                {
                    match(input, EXTENDS, FOLLOW_EXTENDS_in_genericWildcardBoundType1640);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_type_in_genericWildcardBoundType1642);
                    type();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:288:9: ^( SUPER type )
                {
                    match(input, SUPER, FOLLOW_SUPER_in_genericWildcardBoundType1654);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_type_in_genericWildcardBoundType1656);
                    type();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 34, genericWildcardBoundType_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "genericWildcardBoundType"

    // $ANTLR start "formalParameterList"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:291:1: formalParameterList : ^( FORMAL_PARAM_LIST ( formalParameterStandardDecl )* ( formalParameterVarargDecl )? ) ;
    public final void formalParameterList() throws RecognitionException {
        int formalParameterList_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 35)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:292:5: ( ^( FORMAL_PARAM_LIST ( formalParameterStandardDecl )* ( formalParameterVarargDecl )? ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:292:9: ^( FORMAL_PARAM_LIST ( formalParameterStandardDecl )* ( formalParameterVarargDecl )? )
            {
                match(input, FORMAL_PARAM_LIST, FOLLOW_FORMAL_PARAM_LIST_in_formalParameterList1677);
                if (state.failed) {
                    return;
                }

                if (input.LA(1) == Token.DOWN) {
                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:292:29: ( formalParameterStandardDecl )*
                    loop59:
                    do {
                        int alt59 = 2;
                        int LA59_0 = input.LA(1);

                        if ((LA59_0 == FORMAL_PARAM_STD_DECL)) {
                            alt59 = 1;
                        }


                        switch (alt59) {
                            case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: formalParameterStandardDecl
                            {
                                pushFollow(FOLLOW_formalParameterStandardDecl_in_formalParameterList1679);
                                formalParameterStandardDecl();

                                state._fsp--;
                                if (state.failed) {
                                    return;
                                }

                            }
                            break;

                            default:
                                break loop59;
                        }
                    } while (true);

                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:292:58: ( formalParameterVarargDecl )?
                    int alt60 = 2;
                    int LA60_0 = input.LA(1);

                    if ((LA60_0 == FORMAL_PARAM_VARARG_DECL)) {
                        alt60 = 1;
                    }
                    switch (alt60) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: formalParameterVarargDecl
                        {
                            pushFollow(FOLLOW_formalParameterVarargDecl_in_formalParameterList1682);
                            formalParameterVarargDecl();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 35, formalParameterList_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "formalParameterList"

    // $ANTLR start "formalParameterStandardDecl"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:295:1: formalParameterStandardDecl : ^( FORMAL_PARAM_STD_DECL localModifierList type variableDeclaratorId ) ;
    public final void formalParameterStandardDecl() throws RecognitionException {
        int formalParameterStandardDecl_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 36)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:296:5: ( ^( FORMAL_PARAM_STD_DECL localModifierList type variableDeclaratorId ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:296:9: ^( FORMAL_PARAM_STD_DECL localModifierList type variableDeclaratorId )
            {
                match(input, FORMAL_PARAM_STD_DECL, FOLLOW_FORMAL_PARAM_STD_DECL_in_formalParameterStandardDecl1709);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_localModifierList_in_formalParameterStandardDecl1711);
                localModifierList();

                state._fsp--;
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_type_in_formalParameterStandardDecl1713);
                type();

                state._fsp--;
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_variableDeclaratorId_in_formalParameterStandardDecl1715);
                variableDeclaratorId();

                state._fsp--;
                if (state.failed) {
                    return;
                }

                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 36, formalParameterStandardDecl_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "formalParameterStandardDecl"

    // $ANTLR start "formalParameterVarargDecl"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:299:1: formalParameterVarargDecl : ^( FORMAL_PARAM_VARARG_DECL localModifierList type variableDeclaratorId ) ;
    public final void formalParameterVarargDecl() throws RecognitionException {
        int formalParameterVarargDecl_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 37)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:300:5: ( ^( FORMAL_PARAM_VARARG_DECL localModifierList type variableDeclaratorId ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:300:9: ^( FORMAL_PARAM_VARARG_DECL localModifierList type variableDeclaratorId )
            {
                match(input, FORMAL_PARAM_VARARG_DECL, FOLLOW_FORMAL_PARAM_VARARG_DECL_in_formalParameterVarargDecl1740);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_localModifierList_in_formalParameterVarargDecl1742);
                localModifierList();

                state._fsp--;
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_type_in_formalParameterVarargDecl1744);
                type();

                state._fsp--;
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_variableDeclaratorId_in_formalParameterVarargDecl1746);
                variableDeclaratorId();

                state._fsp--;
                if (state.failed) {
                    return;
                }

                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 37, formalParameterVarargDecl_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "formalParameterVarargDecl"

    // $ANTLR start "qualifiedIdentifier"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:303:1: qualifiedIdentifier : ( IDENT | ^( DOT qualifiedIdentifier IDENT ) );
    public final void qualifiedIdentifier() throws RecognitionException {
        int qualifiedIdentifier_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 38)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:304:5: ( IDENT | ^( DOT qualifiedIdentifier IDENT ) )
            int alt61 = 2;
            int LA61_0 = input.LA(1);

            if ((LA61_0 == IDENT)) {
                alt61 = 1;
            } else if ((LA61_0 == DOT)) {
                alt61 = 2;
            } else {
                if (state.backtracking > 0) {
                    state.failed = true;
                    return;
                }
                NoViableAltException nvae =
                        new NoViableAltException("", 61, 0, input);

                throw nvae;
            }
            switch (alt61) {
                case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:304:9: IDENT
                {
                    match(input, IDENT, FOLLOW_IDENT_in_qualifiedIdentifier1770);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:305:9: ^( DOT qualifiedIdentifier IDENT )
                {
                    match(input, DOT, FOLLOW_DOT_in_qualifiedIdentifier1781);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_qualifiedIdentifier_in_qualifiedIdentifier1783);
                    qualifiedIdentifier();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    match(input, IDENT, FOLLOW_IDENT_in_qualifiedIdentifier1785);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 38, qualifiedIdentifier_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "qualifiedIdentifier"

    // $ANTLR start "annotationList"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:310:1: annotationList : ^( ANNOTATION_LIST ( annotation )* ) ;
    public final void annotationList() throws RecognitionException {
        int annotationList_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 39)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:311:5: ( ^( ANNOTATION_LIST ( annotation )* ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:311:9: ^( ANNOTATION_LIST ( annotation )* )
            {
                match(input, ANNOTATION_LIST, FOLLOW_ANNOTATION_LIST_in_annotationList1812);
                if (state.failed) {
                    return;
                }

                if (input.LA(1) == Token.DOWN) {
                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:311:27: ( annotation )*
                    loop62:
                    do {
                        int alt62 = 2;
                        int LA62_0 = input.LA(1);

                        if ((LA62_0 == AT)) {
                            alt62 = 1;
                        }


                        switch (alt62) {
                            case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: annotation
                            {
                                pushFollow(FOLLOW_annotation_in_annotationList1814);
                                annotation();

                                state._fsp--;
                                if (state.failed) {
                                    return;
                                }

                            }
                            break;

                            default:
                                break loop62;
                        }
                    } while (true);


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 39, annotationList_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "annotationList"

    // $ANTLR start "annotation"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:314:1: annotation : ^( AT qualifiedIdentifier ( annotationInit )? ) ;
    public final void annotation() throws RecognitionException {
        int annotation_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 40)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:315:5: ( ^( AT qualifiedIdentifier ( annotationInit )? ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:315:9: ^( AT qualifiedIdentifier ( annotationInit )? )
            {
                match(input, AT, FOLLOW_AT_in_annotation1836);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_qualifiedIdentifier_in_annotation1838);
                qualifiedIdentifier();

                state._fsp--;
                if (state.failed) {
                    return;
                }
                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:315:34: ( annotationInit )?
                int alt63 = 2;
                int LA63_0 = input.LA(1);

                if ((LA63_0 == ANNOTATION_INIT_BLOCK)) {
                    alt63 = 1;
                }
                switch (alt63) {
                    case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: annotationInit
                    {
                        pushFollow(FOLLOW_annotationInit_in_annotation1840);
                        annotationInit();

                        state._fsp--;
                        if (state.failed) {
                            return;
                        }

                    }
                    break;

                }


                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 40, annotation_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "annotation"

    // $ANTLR start "annotationInit"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:318:1: annotationInit : ^( ANNOTATION_INIT_BLOCK annotationInitializers ) ;
    public final void annotationInit() throws RecognitionException {
        int annotationInit_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 41)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:319:5: ( ^( ANNOTATION_INIT_BLOCK annotationInitializers ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:319:9: ^( ANNOTATION_INIT_BLOCK annotationInitializers )
            {
                match(input, ANNOTATION_INIT_BLOCK, FOLLOW_ANNOTATION_INIT_BLOCK_in_annotationInit1866);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_annotationInitializers_in_annotationInit1868);
                annotationInitializers();

                state._fsp--;
                if (state.failed) {
                    return;
                }

                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 41, annotationInit_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "annotationInit"

    // $ANTLR start "annotationInitializers"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:322:1: annotationInitializers : ( ^( ANNOTATION_INIT_KEY_LIST ( annotationInitializer )+ ) | ^( ANNOTATION_INIT_DEFAULT_KEY annotationElementValue ) );
    public final void annotationInitializers() throws RecognitionException {
        int annotationInitializers_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 42)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:323:5: ( ^( ANNOTATION_INIT_KEY_LIST ( annotationInitializer )+ ) | ^( ANNOTATION_INIT_DEFAULT_KEY annotationElementValue ) )
            int alt65 = 2;
            int LA65_0 = input.LA(1);

            if ((LA65_0 == ANNOTATION_INIT_KEY_LIST)) {
                alt65 = 1;
            } else if ((LA65_0 == ANNOTATION_INIT_DEFAULT_KEY)) {
                alt65 = 2;
            } else {
                if (state.backtracking > 0) {
                    state.failed = true;
                    return;
                }
                NoViableAltException nvae =
                        new NoViableAltException("", 65, 0, input);

                throw nvae;
            }
            switch (alt65) {
                case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:323:9: ^( ANNOTATION_INIT_KEY_LIST ( annotationInitializer )+ )
                {
                    match(input, ANNOTATION_INIT_KEY_LIST, FOLLOW_ANNOTATION_INIT_KEY_LIST_in_annotationInitializers1889);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:323:36: ( annotationInitializer )+
                    int cnt64 = 0;
                    loop64:
                    do {
                        int alt64 = 2;
                        int LA64_0 = input.LA(1);

                        if ((LA64_0 == IDENT)) {
                            alt64 = 1;
                        }


                        switch (alt64) {
                            case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: annotationInitializer
                            {
                                pushFollow(FOLLOW_annotationInitializer_in_annotationInitializers1891);
                                annotationInitializer();

                                state._fsp--;
                                if (state.failed) {
                                    return;
                                }

                            }
                            break;

                            default:
                                if (cnt64 >= 1) {
                                    break loop64;
                                }
                                if (state.backtracking > 0) {
                                    state.failed = true;
                                    return;
                                }
                                EarlyExitException eee =
                                        new EarlyExitException(64, input);
                                throw eee;
                        }
                        cnt64++;
                    } while (true);


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:324:9: ^( ANNOTATION_INIT_DEFAULT_KEY annotationElementValue )
                {
                    match(input, ANNOTATION_INIT_DEFAULT_KEY, FOLLOW_ANNOTATION_INIT_DEFAULT_KEY_in_annotationInitializers1904);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_annotationElementValue_in_annotationInitializers1906);
                    annotationElementValue();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 42, annotationInitializers_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "annotationInitializers"

    // $ANTLR start "annotationInitializer"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:327:1: annotationInitializer : ^( IDENT annotationElementValue ) ;
    public final void annotationInitializer() throws RecognitionException {
        int annotationInitializer_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 43)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:328:5: ( ^( IDENT annotationElementValue ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:328:9: ^( IDENT annotationElementValue )
            {
                match(input, IDENT, FOLLOW_IDENT_in_annotationInitializer1931);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_annotationElementValue_in_annotationInitializer1933);
                annotationElementValue();

                state._fsp--;
                if (state.failed) {
                    return;
                }

                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 43, annotationInitializer_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "annotationInitializer"

    // $ANTLR start "annotationElementValue"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:331:1: annotationElementValue : ( ^( ANNOTATION_INIT_ARRAY_ELEMENT ( annotationElementValue )* ) | annotation | expression );
    public final void annotationElementValue() throws RecognitionException {
        int annotationElementValue_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 44)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:332:5: ( ^( ANNOTATION_INIT_ARRAY_ELEMENT ( annotationElementValue )* ) | annotation | expression )
            int alt67 = 3;
            switch (input.LA(1)) {
                case ANNOTATION_INIT_ARRAY_ELEMENT: {
                    alt67 = 1;
                }
                break;
                case AT: {
                    alt67 = 2;
                }
                break;
                case EXPR: {
                    alt67 = 3;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 67, 0, input);

                    throw nvae;
            }

            switch (alt67) {
                case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:332:9: ^( ANNOTATION_INIT_ARRAY_ELEMENT ( annotationElementValue )* )
                {
                    match(input, ANNOTATION_INIT_ARRAY_ELEMENT, FOLLOW_ANNOTATION_INIT_ARRAY_ELEMENT_in_annotationElementValue1958);
                    if (state.failed) {
                        return;
                    }

                    if (input.LA(1) == Token.DOWN) {
                        match(input, Token.DOWN, null);
                        if (state.failed) {
                            return;
                        }
                        // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:332:41: ( annotationElementValue )*
                        loop66:
                        do {
                            int alt66 = 2;
                            int LA66_0 = input.LA(1);

                            if ((LA66_0 == AT || LA66_0 == ANNOTATION_INIT_ARRAY_ELEMENT || LA66_0 == EXPR)) {
                                alt66 = 1;
                            }


                            switch (alt66) {
                                case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: annotationElementValue
                                {
                                    pushFollow(FOLLOW_annotationElementValue_in_annotationElementValue1960);
                                    annotationElementValue();

                                    state._fsp--;
                                    if (state.failed) {
                                        return;
                                    }

                                }
                                break;

                                default:
                                    break loop66;
                            }
                        } while (true);


                        match(input, Token.UP, null);
                        if (state.failed) {
                            return;
                        }
                    }

                }
                break;
                case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:333:9: annotation
                {
                    pushFollow(FOLLOW_annotation_in_annotationElementValue1972);
                    annotation();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 3: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:334:9: expression
                {
                    pushFollow(FOLLOW_expression_in_annotationElementValue1982);
                    expression();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 44, annotationElementValue_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "annotationElementValue"

    // $ANTLR start "annotationTopLevelScope"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:337:1: annotationTopLevelScope : ^( ANNOTATION_TOP_LEVEL_SCOPE ( annotationScopeDeclarations )* ) ;
    public final void annotationTopLevelScope() throws RecognitionException {
        int annotationTopLevelScope_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 45)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:338:5: ( ^( ANNOTATION_TOP_LEVEL_SCOPE ( annotationScopeDeclarations )* ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:338:9: ^( ANNOTATION_TOP_LEVEL_SCOPE ( annotationScopeDeclarations )* )
            {
                match(input, ANNOTATION_TOP_LEVEL_SCOPE, FOLLOW_ANNOTATION_TOP_LEVEL_SCOPE_in_annotationTopLevelScope2006);
                if (state.failed) {
                    return;
                }

                if (input.LA(1) == Token.DOWN) {
                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:338:38: ( annotationScopeDeclarations )*
                    loop68:
                    do {
                        int alt68 = 2;
                        int LA68_0 = input.LA(1);

                        if ((LA68_0 == AT || LA68_0 == CLASS || LA68_0 == ENUM || LA68_0 == INTERFACE || LA68_0 == ANNOTATION_METHOD_DECL || LA68_0 == VAR_DECLARATION)) {
                            alt68 = 1;
                        }


                        switch (alt68) {
                            case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: annotationScopeDeclarations
                            {
                                pushFollow(FOLLOW_annotationScopeDeclarations_in_annotationTopLevelScope2008);
                                annotationScopeDeclarations();

                                state._fsp--;
                                if (state.failed) {
                                    return;
                                }

                            }
                            break;

                            default:
                                break loop68;
                        }
                    } while (true);


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 45, annotationTopLevelScope_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "annotationTopLevelScope"

    // $ANTLR start "annotationScopeDeclarations"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:341:1: annotationScopeDeclarations : ( ^( ANNOTATION_METHOD_DECL modifierList type IDENT ( annotationDefaultValue )? ) | ^( VAR_DECLARATION modifierList type variableDeclaratorList ) | typeDeclaration );
    public final void annotationScopeDeclarations() throws RecognitionException {
        int annotationScopeDeclarations_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 46)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:342:5: ( ^( ANNOTATION_METHOD_DECL modifierList type IDENT ( annotationDefaultValue )? ) | ^( VAR_DECLARATION modifierList type variableDeclaratorList ) | typeDeclaration )
            int alt70 = 3;
            switch (input.LA(1)) {
                case ANNOTATION_METHOD_DECL: {
                    alt70 = 1;
                }
                break;
                case VAR_DECLARATION: {
                    alt70 = 2;
                }
                break;
                case AT:
                case CLASS:
                case ENUM:
                case INTERFACE: {
                    alt70 = 3;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 70, 0, input);

                    throw nvae;
            }

            switch (alt70) {
                case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:342:9: ^( ANNOTATION_METHOD_DECL modifierList type IDENT ( annotationDefaultValue )? )
                {
                    match(input, ANNOTATION_METHOD_DECL, FOLLOW_ANNOTATION_METHOD_DECL_in_annotationScopeDeclarations2034);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_modifierList_in_annotationScopeDeclarations2036);
                    modifierList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_type_in_annotationScopeDeclarations2038);
                    type();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    match(input, IDENT, FOLLOW_IDENT_in_annotationScopeDeclarations2040);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:342:58: ( annotationDefaultValue )?
                    int alt69 = 2;
                    int LA69_0 = input.LA(1);

                    if ((LA69_0 == DEFAULT)) {
                        alt69 = 1;
                    }
                    switch (alt69) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: annotationDefaultValue
                        {
                            pushFollow(FOLLOW_annotationDefaultValue_in_annotationScopeDeclarations2042);
                            annotationDefaultValue();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:343:9: ^( VAR_DECLARATION modifierList type variableDeclaratorList )
                {
                    match(input, VAR_DECLARATION, FOLLOW_VAR_DECLARATION_in_annotationScopeDeclarations2055);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_modifierList_in_annotationScopeDeclarations2057);
                    modifierList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_type_in_annotationScopeDeclarations2059);
                    type();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_variableDeclaratorList_in_annotationScopeDeclarations2061);
                    variableDeclaratorList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 3: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:344:9: typeDeclaration
                {
                    pushFollow(FOLLOW_typeDeclaration_in_annotationScopeDeclarations2072);
                    typeDeclaration();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 46, annotationScopeDeclarations_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "annotationScopeDeclarations"

    // $ANTLR start "annotationDefaultValue"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:347:1: annotationDefaultValue : ^( DEFAULT annotationElementValue ) ;
    public final void annotationDefaultValue() throws RecognitionException {
        int annotationDefaultValue_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 47)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:348:5: ( ^( DEFAULT annotationElementValue ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:348:9: ^( DEFAULT annotationElementValue )
            {
                match(input, DEFAULT, FOLLOW_DEFAULT_in_annotationDefaultValue2096);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_annotationElementValue_in_annotationDefaultValue2098);
                annotationElementValue();

                state._fsp--;
                if (state.failed) {
                    return;
                }

                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 47, annotationDefaultValue_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "annotationDefaultValue"

    // $ANTLR start "block"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:353:1: block : ^( BLOCK_SCOPE ( blockStatement )* ) ;
    public final void block() throws RecognitionException {
        int block_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 48)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:354:5: ( ^( BLOCK_SCOPE ( blockStatement )* ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:354:9: ^( BLOCK_SCOPE ( blockStatement )* )
            {
                match(input, BLOCK_SCOPE, FOLLOW_BLOCK_SCOPE_in_block2121);
                if (state.failed) {
                    return;
                }

                if (input.LA(1) == Token.DOWN) {
                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:354:23: ( blockStatement )*
                    loop71:
                    do {
                        int alt71 = 2;
                        int LA71_0 = input.LA(1);

                        if ((LA71_0 == AT || LA71_0 == SEMI || LA71_0 == ASSERT || LA71_0 == BREAK || (LA71_0 >= CLASS && LA71_0 <= CONTINUE) || LA71_0 == DO || LA71_0 == ENUM || (LA71_0 >= FOR && LA71_0 <= IF) || LA71_0 == INTERFACE || LA71_0 == RETURN || (LA71_0 >= SWITCH && LA71_0 <= SYNCHRONIZED) || LA71_0 == THROW || LA71_0 == TRY || LA71_0 == WHILE || LA71_0 == BLOCK_SCOPE || LA71_0 == EXPR || LA71_0 == FOR_EACH || LA71_0 == LABELED_STATEMENT || LA71_0 == VAR_DECLARATION)) {
                            alt71 = 1;
                        }


                        switch (alt71) {
                            case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: blockStatement
                            {
                                pushFollow(FOLLOW_blockStatement_in_block2123);
                                blockStatement();

                                state._fsp--;
                                if (state.failed) {
                                    return;
                                }

                            }
                            break;

                            default:
                                break loop71;
                        }
                    } while (true);


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 48, block_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "block"

    // $ANTLR start "blockStatement"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:357:1: blockStatement : ( localVariableDeclaration | typeDeclaration | statement );
    public final void blockStatement() throws RecognitionException {
        int blockStatement_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 49)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:358:5: ( localVariableDeclaration | typeDeclaration | statement )
            int alt72 = 3;
            switch (input.LA(1)) {
                case VAR_DECLARATION: {
                    alt72 = 1;
                }
                break;
                case AT:
                case CLASS:
                case ENUM:
                case INTERFACE: {
                    alt72 = 2;
                }
                break;
                case SEMI:
                case ASSERT:
                case BREAK:
                case CONTINUE:
                case DO:
                case FOR:
                case IF:
                case RETURN:
                case SWITCH:
                case SYNCHRONIZED:
                case THROW:
                case TRY:
                case WHILE:
                case BLOCK_SCOPE:
                case EXPR:
                case FOR_EACH:
                case LABELED_STATEMENT: {
                    alt72 = 3;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 72, 0, input);

                    throw nvae;
            }

            switch (alt72) {
                case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:358:9: localVariableDeclaration
                {
                    pushFollow(FOLLOW_localVariableDeclaration_in_blockStatement2148);
                    localVariableDeclaration();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:359:9: typeDeclaration
                {
                    pushFollow(FOLLOW_typeDeclaration_in_blockStatement2158);
                    typeDeclaration();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 3: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:360:9: statement
                {
                    pushFollow(FOLLOW_statement_in_blockStatement2168);
                    statement();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 49, blockStatement_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "blockStatement"

    // $ANTLR start "localVariableDeclaration"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:363:1: localVariableDeclaration : ^( VAR_DECLARATION localModifierList type variableDeclaratorList ) ;
    public final void localVariableDeclaration() throws RecognitionException {
        int localVariableDeclaration_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 50)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:364:5: ( ^( VAR_DECLARATION localModifierList type variableDeclaratorList ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:364:9: ^( VAR_DECLARATION localModifierList type variableDeclaratorList )
            {
                match(input, VAR_DECLARATION, FOLLOW_VAR_DECLARATION_in_localVariableDeclaration2192);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_localModifierList_in_localVariableDeclaration2194);
                localModifierList();

                state._fsp--;
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_type_in_localVariableDeclaration2196);
                type();

                state._fsp--;
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_variableDeclaratorList_in_localVariableDeclaration2198);
                variableDeclaratorList();

                state._fsp--;
                if (state.failed) {
                    return;
                }

                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 50, localVariableDeclaration_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "localVariableDeclaration"

    // $ANTLR start "statement"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:368:1: statement : ( block | ^( ASSERT expression ( expression )? ) | ^( IF parenthesizedExpression statement ( statement )? ) | ^( FOR forInit forCondition forUpdater statement ) | ^( FOR_EACH localModifierList type IDENT expression statement ) | ^( WHILE parenthesizedExpression statement ) | ^( DO statement parenthesizedExpression ) | ^( TRY block ( catches )? ( block )? ) | ^( SWITCH parenthesizedExpression switchBlockLabels ) | ^( SYNCHRONIZED parenthesizedExpression block ) | ^( RETURN ( expression )? ) | ^( THROW expression ) | ^( BREAK ( IDENT )? ) | ^( CONTINUE ( IDENT )? ) | ^( LABELED_STATEMENT IDENT statement ) | expression | SEMI );
    public final void statement() throws RecognitionException {
        int statement_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 51)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:369:5: ( block | ^( ASSERT expression ( expression )? ) | ^( IF parenthesizedExpression statement ( statement )? ) | ^( FOR forInit forCondition forUpdater statement ) | ^( FOR_EACH localModifierList type IDENT expression statement ) | ^( WHILE parenthesizedExpression statement ) | ^( DO statement parenthesizedExpression ) | ^( TRY block ( catches )? ( block )? ) | ^( SWITCH parenthesizedExpression switchBlockLabels ) | ^( SYNCHRONIZED parenthesizedExpression block ) | ^( RETURN ( expression )? ) | ^( THROW expression ) | ^( BREAK ( IDENT )? ) | ^( CONTINUE ( IDENT )? ) | ^( LABELED_STATEMENT IDENT statement ) | expression | SEMI )
            int alt80 = 17;
            switch (input.LA(1)) {
                case BLOCK_SCOPE: {
                    alt80 = 1;
                }
                break;
                case ASSERT: {
                    alt80 = 2;
                }
                break;
                case IF: {
                    alt80 = 3;
                }
                break;
                case FOR: {
                    alt80 = 4;
                }
                break;
                case FOR_EACH: {
                    alt80 = 5;
                }
                break;
                case WHILE: {
                    alt80 = 6;
                }
                break;
                case DO: {
                    alt80 = 7;
                }
                break;
                case TRY: {
                    alt80 = 8;
                }
                break;
                case SWITCH: {
                    alt80 = 9;
                }
                break;
                case SYNCHRONIZED: {
                    alt80 = 10;
                }
                break;
                case RETURN: {
                    alt80 = 11;
                }
                break;
                case THROW: {
                    alt80 = 12;
                }
                break;
                case BREAK: {
                    alt80 = 13;
                }
                break;
                case CONTINUE: {
                    alt80 = 14;
                }
                break;
                case LABELED_STATEMENT: {
                    alt80 = 15;
                }
                break;
                case EXPR: {
                    alt80 = 16;
                }
                break;
                case SEMI: {
                    alt80 = 17;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 80, 0, input);

                    throw nvae;
            }

            switch (alt80) {
                case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:369:9: block
                {
                    pushFollow(FOLLOW_block_in_statement2231);
                    block();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:370:9: ^( ASSERT expression ( expression )? )
                {
                    match(input, ASSERT, FOLLOW_ASSERT_in_statement2242);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expression_in_statement2244);
                    expression();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:370:29: ( expression )?
                    int alt73 = 2;
                    int LA73_0 = input.LA(1);

                    if ((LA73_0 == EXPR)) {
                        alt73 = 1;
                    }
                    switch (alt73) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: expression
                        {
                            pushFollow(FOLLOW_expression_in_statement2246);
                            expression();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 3: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:371:9: ^( IF parenthesizedExpression statement ( statement )? )
                {
                    match(input, IF, FOLLOW_IF_in_statement2259);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_parenthesizedExpression_in_statement2261);
                    parenthesizedExpression();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_statement_in_statement2263);
                    statement();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:371:48: ( statement )?
                    int alt74 = 2;
                    int LA74_0 = input.LA(1);

                    if ((LA74_0 == SEMI || LA74_0 == ASSERT || LA74_0 == BREAK || LA74_0 == CONTINUE || LA74_0 == DO || (LA74_0 >= FOR && LA74_0 <= IF) || LA74_0 == RETURN || (LA74_0 >= SWITCH && LA74_0 <= SYNCHRONIZED) || LA74_0 == THROW || LA74_0 == TRY || LA74_0 == WHILE || LA74_0 == BLOCK_SCOPE || LA74_0 == EXPR || LA74_0 == FOR_EACH || LA74_0 == LABELED_STATEMENT)) {
                        alt74 = 1;
                    }
                    switch (alt74) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: statement
                        {
                            pushFollow(FOLLOW_statement_in_statement2265);
                            statement();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 4: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:372:9: ^( FOR forInit forCondition forUpdater statement )
                {
                    match(input, FOR, FOLLOW_FOR_in_statement2278);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_forInit_in_statement2280);
                    forInit();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_forCondition_in_statement2282);
                    forCondition();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_forUpdater_in_statement2284);
                    forUpdater();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_statement_in_statement2286);
                    statement();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 5: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:373:9: ^( FOR_EACH localModifierList type IDENT expression statement )
                {
                    match(input, FOR_EACH, FOLLOW_FOR_EACH_in_statement2298);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_localModifierList_in_statement2300);
                    localModifierList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_type_in_statement2302);
                    type();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    match(input, IDENT, FOLLOW_IDENT_in_statement2304);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expression_in_statement2306);
                    expression();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_statement_in_statement2308);
                    statement();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 6: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:374:9: ^( WHILE parenthesizedExpression statement )
                {
                    match(input, WHILE, FOLLOW_WHILE_in_statement2321);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_parenthesizedExpression_in_statement2323);
                    parenthesizedExpression();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_statement_in_statement2325);
                    statement();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 7: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:375:9: ^( DO statement parenthesizedExpression )
                {
                    match(input, DO, FOLLOW_DO_in_statement2337);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_statement_in_statement2339);
                    statement();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_parenthesizedExpression_in_statement2341);
                    parenthesizedExpression();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 8: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:376:9: ^( TRY block ( catches )? ( block )? )
                {
                    match(input, TRY, FOLLOW_TRY_in_statement2353);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_block_in_statement2355);
                    block();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:376:21: ( catches )?
                    int alt75 = 2;
                    int LA75_0 = input.LA(1);

                    if ((LA75_0 == CATCH_CLAUSE_LIST)) {
                        alt75 = 1;
                    }
                    switch (alt75) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: catches
                        {
                            pushFollow(FOLLOW_catches_in_statement2357);
                            catches();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:376:30: ( block )?
                    int alt76 = 2;
                    int LA76_0 = input.LA(1);

                    if ((LA76_0 == BLOCK_SCOPE)) {
                        alt76 = 1;
                    }
                    switch (alt76) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: block
                        {
                            pushFollow(FOLLOW_block_in_statement2360);
                            block();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 9: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:377:9: ^( SWITCH parenthesizedExpression switchBlockLabels )
                {
                    match(input, SWITCH, FOLLOW_SWITCH_in_statement2375);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_parenthesizedExpression_in_statement2377);
                    parenthesizedExpression();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_switchBlockLabels_in_statement2379);
                    switchBlockLabels();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 10: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:378:9: ^( SYNCHRONIZED parenthesizedExpression block )
                {
                    match(input, SYNCHRONIZED, FOLLOW_SYNCHRONIZED_in_statement2391);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_parenthesizedExpression_in_statement2393);
                    parenthesizedExpression();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_block_in_statement2395);
                    block();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 11: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:379:9: ^( RETURN ( expression )? )
                {
                    match(input, RETURN, FOLLOW_RETURN_in_statement2407);
                    if (state.failed) {
                        return;
                    }

                    if (input.LA(1) == Token.DOWN) {
                        match(input, Token.DOWN, null);
                        if (state.failed) {
                            return;
                        }
                        // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:379:18: ( expression )?
                        int alt77 = 2;
                        int LA77_0 = input.LA(1);

                        if ((LA77_0 == EXPR)) {
                            alt77 = 1;
                        }
                        switch (alt77) {
                            case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: expression
                            {
                                pushFollow(FOLLOW_expression_in_statement2409);
                                expression();

                                state._fsp--;
                                if (state.failed) {
                                    return;
                                }

                            }
                            break;

                        }


                        match(input, Token.UP, null);
                        if (state.failed) {
                            return;
                        }
                    }

                }
                break;
                case 12: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:380:9: ^( THROW expression )
                {
                    match(input, THROW, FOLLOW_THROW_in_statement2422);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expression_in_statement2424);
                    expression();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 13: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:381:9: ^( BREAK ( IDENT )? )
                {
                    match(input, BREAK, FOLLOW_BREAK_in_statement2436);
                    if (state.failed) {
                        return;
                    }

                    if (input.LA(1) == Token.DOWN) {
                        match(input, Token.DOWN, null);
                        if (state.failed) {
                            return;
                        }
                        // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:381:17: ( IDENT )?
                        int alt78 = 2;
                        int LA78_0 = input.LA(1);

                        if ((LA78_0 == IDENT)) {
                            alt78 = 1;
                        }
                        switch (alt78) {
                            case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: IDENT
                            {
                                match(input, IDENT, FOLLOW_IDENT_in_statement2438);
                                if (state.failed) {
                                    return;
                                }

                            }
                            break;

                        }


                        match(input, Token.UP, null);
                        if (state.failed) {
                            return;
                        }
                    }

                }
                break;
                case 14: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:382:9: ^( CONTINUE ( IDENT )? )
                {
                    match(input, CONTINUE, FOLLOW_CONTINUE_in_statement2451);
                    if (state.failed) {
                        return;
                    }

                    if (input.LA(1) == Token.DOWN) {
                        match(input, Token.DOWN, null);
                        if (state.failed) {
                            return;
                        }
                        // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:382:20: ( IDENT )?
                        int alt79 = 2;
                        int LA79_0 = input.LA(1);

                        if ((LA79_0 == IDENT)) {
                            alt79 = 1;
                        }
                        switch (alt79) {
                            case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: IDENT
                            {
                                match(input, IDENT, FOLLOW_IDENT_in_statement2453);
                                if (state.failed) {
                                    return;
                                }

                            }
                            break;

                        }


                        match(input, Token.UP, null);
                        if (state.failed) {
                            return;
                        }
                    }

                }
                break;
                case 15: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:383:9: ^( LABELED_STATEMENT IDENT statement )
                {
                    match(input, LABELED_STATEMENT, FOLLOW_LABELED_STATEMENT_in_statement2466);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    match(input, IDENT, FOLLOW_IDENT_in_statement2468);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_statement_in_statement2470);
                    statement();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 16: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:384:9: expression
                {
                    pushFollow(FOLLOW_expression_in_statement2481);
                    expression();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 17: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:385:9: SEMI
                {
                    match(input, SEMI, FOLLOW_SEMI_in_statement2491);
                    if (state.failed) {
                        return;
                    }

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 51, statement_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "statement"

    // $ANTLR start "catches"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:388:1: catches : ^( CATCH_CLAUSE_LIST ( catchClause )+ ) ;
    public final void catches() throws RecognitionException {
        int catches_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 52)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:389:5: ( ^( CATCH_CLAUSE_LIST ( catchClause )+ ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:389:9: ^( CATCH_CLAUSE_LIST ( catchClause )+ )
            {
                match(input, CATCH_CLAUSE_LIST, FOLLOW_CATCH_CLAUSE_LIST_in_catches2520);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:389:29: ( catchClause )+
                int cnt81 = 0;
                loop81:
                do {
                    int alt81 = 2;
                    int LA81_0 = input.LA(1);

                    if ((LA81_0 == CATCH)) {
                        alt81 = 1;
                    }


                    switch (alt81) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: catchClause
                        {
                            pushFollow(FOLLOW_catchClause_in_catches2522);
                            catchClause();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                        default:
                            if (cnt81 >= 1) {
                                break loop81;
                            }
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            EarlyExitException eee =
                                    new EarlyExitException(81, input);
                            throw eee;
                    }
                    cnt81++;
                } while (true);


                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 52, catches_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "catches"

    // $ANTLR start "catchClause"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:392:1: catchClause : ^( CATCH formalParameterStandardDecl block ) ;
    public final void catchClause() throws RecognitionException {
        int catchClause_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 53)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:393:5: ( ^( CATCH formalParameterStandardDecl block ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:393:9: ^( CATCH formalParameterStandardDecl block )
            {
                match(input, CATCH, FOLLOW_CATCH_in_catchClause2548);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_formalParameterStandardDecl_in_catchClause2550);
                formalParameterStandardDecl();

                state._fsp--;
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_block_in_catchClause2552);
                block();

                state._fsp--;
                if (state.failed) {
                    return;
                }

                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 53, catchClause_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "catchClause"

    // $ANTLR start "switchBlockLabels"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:396:1: switchBlockLabels : ^( SWITCH_BLOCK_LABEL_LIST ( switchCaseLabel )* ( switchDefaultLabel )? ( switchCaseLabel )* ) ;
    public final void switchBlockLabels() throws RecognitionException {
        int switchBlockLabels_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 54)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:397:5: ( ^( SWITCH_BLOCK_LABEL_LIST ( switchCaseLabel )* ( switchDefaultLabel )? ( switchCaseLabel )* ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:397:9: ^( SWITCH_BLOCK_LABEL_LIST ( switchCaseLabel )* ( switchDefaultLabel )? ( switchCaseLabel )* )
            {
                match(input, SWITCH_BLOCK_LABEL_LIST, FOLLOW_SWITCH_BLOCK_LABEL_LIST_in_switchBlockLabels2573);
                if (state.failed) {
                    return;
                }

                if (input.LA(1) == Token.DOWN) {
                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:397:35: ( switchCaseLabel )*
                    loop82:
                    do {
                        int alt82 = 2;
                        int LA82_0 = input.LA(1);

                        if ((LA82_0 == CASE)) {
                            input.LA(2);

                            if ((synpred125_JavaTreeParser())) {
                                alt82 = 1;
                            }


                        }


                        switch (alt82) {
                            case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: switchCaseLabel
                            {
                                pushFollow(FOLLOW_switchCaseLabel_in_switchBlockLabels2575);
                                switchCaseLabel();

                                state._fsp--;
                                if (state.failed) {
                                    return;
                                }

                            }
                            break;

                            default:
                                break loop82;
                        }
                    } while (true);

                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:397:52: ( switchDefaultLabel )?
                    int alt83 = 2;
                    int LA83_0 = input.LA(1);

                    if ((LA83_0 == DEFAULT)) {
                        alt83 = 1;
                    }
                    switch (alt83) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: switchDefaultLabel
                        {
                            pushFollow(FOLLOW_switchDefaultLabel_in_switchBlockLabels2578);
                            switchDefaultLabel();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:397:72: ( switchCaseLabel )*
                    loop84:
                    do {
                        int alt84 = 2;
                        int LA84_0 = input.LA(1);

                        if ((LA84_0 == CASE)) {
                            alt84 = 1;
                        }


                        switch (alt84) {
                            case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: switchCaseLabel
                            {
                                pushFollow(FOLLOW_switchCaseLabel_in_switchBlockLabels2581);
                                switchCaseLabel();

                                state._fsp--;
                                if (state.failed) {
                                    return;
                                }

                            }
                            break;

                            default:
                                break loop84;
                        }
                    } while (true);


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 54, switchBlockLabels_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "switchBlockLabels"

    // $ANTLR start "switchCaseLabel"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:400:1: switchCaseLabel : ^( CASE expression ( blockStatement )* ) ;
    public final void switchCaseLabel() throws RecognitionException {
        int switchCaseLabel_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 55)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:401:5: ( ^( CASE expression ( blockStatement )* ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:401:9: ^( CASE expression ( blockStatement )* )
            {
                match(input, CASE, FOLLOW_CASE_in_switchCaseLabel2611);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_expression_in_switchCaseLabel2613);
                expression();

                state._fsp--;
                if (state.failed) {
                    return;
                }
                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:401:27: ( blockStatement )*
                loop85:
                do {
                    int alt85 = 2;
                    int LA85_0 = input.LA(1);

                    if ((LA85_0 == AT || LA85_0 == SEMI || LA85_0 == ASSERT || LA85_0 == BREAK || (LA85_0 >= CLASS && LA85_0 <= CONTINUE) || LA85_0 == DO || LA85_0 == ENUM || (LA85_0 >= FOR && LA85_0 <= IF) || LA85_0 == INTERFACE || LA85_0 == RETURN || (LA85_0 >= SWITCH && LA85_0 <= SYNCHRONIZED) || LA85_0 == THROW || LA85_0 == TRY || LA85_0 == WHILE || LA85_0 == BLOCK_SCOPE || LA85_0 == EXPR || LA85_0 == FOR_EACH || LA85_0 == LABELED_STATEMENT || LA85_0 == VAR_DECLARATION)) {
                        alt85 = 1;
                    }


                    switch (alt85) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: blockStatement
                        {
                            pushFollow(FOLLOW_blockStatement_in_switchCaseLabel2615);
                            blockStatement();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                        default:
                            break loop85;
                    }
                } while (true);


                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 55, switchCaseLabel_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "switchCaseLabel"

    // $ANTLR start "switchDefaultLabel"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:404:1: switchDefaultLabel : ^( DEFAULT ( blockStatement )* ) ;
    public final void switchDefaultLabel() throws RecognitionException {
        int switchDefaultLabel_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 56)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:405:5: ( ^( DEFAULT ( blockStatement )* ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:405:9: ^( DEFAULT ( blockStatement )* )
            {
                match(input, DEFAULT, FOLLOW_DEFAULT_in_switchDefaultLabel2641);
                if (state.failed) {
                    return;
                }

                if (input.LA(1) == Token.DOWN) {
                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:405:19: ( blockStatement )*
                    loop86:
                    do {
                        int alt86 = 2;
                        int LA86_0 = input.LA(1);

                        if ((LA86_0 == AT || LA86_0 == SEMI || LA86_0 == ASSERT || LA86_0 == BREAK || (LA86_0 >= CLASS && LA86_0 <= CONTINUE) || LA86_0 == DO || LA86_0 == ENUM || (LA86_0 >= FOR && LA86_0 <= IF) || LA86_0 == INTERFACE || LA86_0 == RETURN || (LA86_0 >= SWITCH && LA86_0 <= SYNCHRONIZED) || LA86_0 == THROW || LA86_0 == TRY || LA86_0 == WHILE || LA86_0 == BLOCK_SCOPE || LA86_0 == EXPR || LA86_0 == FOR_EACH || LA86_0 == LABELED_STATEMENT || LA86_0 == VAR_DECLARATION)) {
                            alt86 = 1;
                        }


                        switch (alt86) {
                            case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: blockStatement
                            {
                                pushFollow(FOLLOW_blockStatement_in_switchDefaultLabel2643);
                                blockStatement();

                                state._fsp--;
                                if (state.failed) {
                                    return;
                                }

                            }
                            break;

                            default:
                                break loop86;
                        }
                    } while (true);


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 56, switchDefaultLabel_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "switchDefaultLabel"

    // $ANTLR start "forInit"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:408:1: forInit : ^( FOR_INIT ( localVariableDeclaration | ( expression )* )? ) ;
    public final void forInit() throws RecognitionException {
        int forInit_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 57)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:409:5: ( ^( FOR_INIT ( localVariableDeclaration | ( expression )* )? ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:409:9: ^( FOR_INIT ( localVariableDeclaration | ( expression )* )? )
            {
                match(input, FOR_INIT, FOLLOW_FOR_INIT_in_forInit2669);
                if (state.failed) {
                    return;
                }

                if (input.LA(1) == Token.DOWN) {
                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:409:20: ( localVariableDeclaration | ( expression )* )?
                    int alt88 = 3;
                    switch (input.LA(1)) {
                        case VAR_DECLARATION: {
                            alt88 = 1;
                        }
                        break;
                        case EXPR: {
                            alt88 = 2;
                        }

                        break;
                        case UP: {
                            input.LA(2);

                            if ((synpred132_JavaTreeParser())) {
                                alt88 = 2;
                            }
                        }
                        break;
                    }

                    switch (alt88) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:409:21: localVariableDeclaration
                        {
                            pushFollow(FOLLOW_localVariableDeclaration_in_forInit2672);
                            localVariableDeclaration();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;
                        case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:409:48: ( expression )*
                        {
                            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:409:48: ( expression )*
                            loop87:
                            do {
                                int alt87 = 2;
                                int LA87_0 = input.LA(1);

                                if ((LA87_0 == EXPR)) {
                                    alt87 = 1;
                                }


                                switch (alt87) {
                                    case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: expression
                                    {
                                        pushFollow(FOLLOW_expression_in_forInit2676);
                                        expression();

                                        state._fsp--;
                                        if (state.failed) {
                                            return;
                                        }

                                    }
                                    break;

                                    default:
                                        break loop87;
                                }
                            } while (true);


                        }
                        break;

                    }


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 57, forInit_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "forInit"

    // $ANTLR start "forCondition"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:412:1: forCondition : ^( FOR_CONDITION ( expression )? ) ;
    public final void forCondition() throws RecognitionException {
        int forCondition_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 58)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:413:5: ( ^( FOR_CONDITION ( expression )? ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:413:9: ^( FOR_CONDITION ( expression )? )
            {
                match(input, FOR_CONDITION, FOLLOW_FOR_CONDITION_in_forCondition2704);
                if (state.failed) {
                    return;
                }

                if (input.LA(1) == Token.DOWN) {
                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:413:25: ( expression )?
                    int alt89 = 2;
                    int LA89_0 = input.LA(1);

                    if ((LA89_0 == EXPR)) {
                        alt89 = 1;
                    }
                    switch (alt89) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: expression
                        {
                            pushFollow(FOLLOW_expression_in_forCondition2706);
                            expression();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 58, forCondition_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "forCondition"

    // $ANTLR start "forUpdater"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:416:1: forUpdater : ^( FOR_UPDATE ( expression )* ) ;
    public final void forUpdater() throws RecognitionException {
        int forUpdater_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 59)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:417:5: ( ^( FOR_UPDATE ( expression )* ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:417:9: ^( FOR_UPDATE ( expression )* )
            {
                match(input, FOR_UPDATE, FOLLOW_FOR_UPDATE_in_forUpdater2732);
                if (state.failed) {
                    return;
                }

                if (input.LA(1) == Token.DOWN) {
                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:417:22: ( expression )*
                    loop90:
                    do {
                        int alt90 = 2;
                        int LA90_0 = input.LA(1);

                        if ((LA90_0 == EXPR)) {
                            alt90 = 1;
                        }


                        switch (alt90) {
                            case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: expression
                            {
                                pushFollow(FOLLOW_expression_in_forUpdater2734);
                                expression();

                                state._fsp--;
                                if (state.failed) {
                                    return;
                                }

                            }
                            break;

                            default:
                                break loop90;
                        }
                    } while (true);


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 59, forUpdater_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "forUpdater"

    // $ANTLR start "parenthesizedExpression"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:422:1: parenthesizedExpression : ^( PARENTESIZED_EXPR expression ) ;
    public final void parenthesizedExpression() throws RecognitionException {
        int parenthesizedExpression_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 60)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:423:5: ( ^( PARENTESIZED_EXPR expression ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:423:9: ^( PARENTESIZED_EXPR expression )
            {
                match(input, PARENTESIZED_EXPR, FOLLOW_PARENTESIZED_EXPR_in_parenthesizedExpression2762);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_expression_in_parenthesizedExpression2764);
                expression();

                state._fsp--;
                if (state.failed) {
                    return;
                }

                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 60, parenthesizedExpression_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "parenthesizedExpression"

    // $ANTLR start "expression"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:426:1: expression : ^( EXPR expr ) ;
    public final void expression() throws RecognitionException {
        int expression_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 61)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:427:5: ( ^( EXPR expr ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:427:9: ^( EXPR expr )
            {
                match(input, EXPR, FOLLOW_EXPR_in_expression2789);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_expr_in_expression2791);
                expr();

                state._fsp--;
                if (state.failed) {
                    return;
                }

                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 61, expression_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "expression"

    // $ANTLR start "expr"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:430:1: expr : ( ^( ASSIGN expr expr ) | ^( PLUS_ASSIGN expr expr ) | ^( MINUS_ASSIGN expr expr ) | ^( STAR_ASSIGN expr expr ) | ^( DIV_ASSIGN expr expr ) | ^( AND_ASSIGN expr expr ) | ^( OR_ASSIGN expr expr ) | ^( XOR_ASSIGN expr expr ) | ^( MOD_ASSIGN expr expr ) | ^( BIT_SHIFT_RIGHT_ASSIGN expr expr ) | ^( SHIFT_RIGHT_ASSIGN expr expr ) | ^( SHIFT_LEFT_ASSIGN expr expr ) | ^( QUESTION expr expr expr ) | ^( LOGICAL_OR expr expr ) | ^( LOGICAL_AND expr expr ) | ^( OR expr expr ) | ^( XOR expr expr ) | ^( AND expr expr ) | ^( EQUAL expr expr ) | ^( NOT_EQUAL expr expr ) | ^( INSTANCEOF expr type ) | ^( LESS_OR_EQUAL expr expr ) | ^( GREATER_OR_EQUAL expr expr ) | ^( BIT_SHIFT_RIGHT expr expr ) | ^( SHIFT_RIGHT expr expr ) | ^( GREATER_THAN expr expr ) | ^( SHIFT_LEFT expr expr ) | ^( LESS_THAN expr expr ) | ^( PLUS expr expr ) | ^( MINUS expr expr ) | ^( STAR expr expr ) | ^( DIV expr expr ) | ^( MOD expr expr ) | ^( UNARY_PLUS expr ) | ^( UNARY_MINUS expr ) | ^( PRE_INC expr ) | ^( PRE_DEC expr ) | ^( POST_INC expr ) | ^( POST_DEC expr ) | ^( NOT expr ) | ^( LOGICAL_NOT expr ) | ^( CAST_EXPR type expr ) | primaryExpression );
    public final void expr() throws RecognitionException {
        int expr_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 62)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:431:5: ( ^( ASSIGN expr expr ) | ^( PLUS_ASSIGN expr expr ) | ^( MINUS_ASSIGN expr expr ) | ^( STAR_ASSIGN expr expr ) | ^( DIV_ASSIGN expr expr ) | ^( AND_ASSIGN expr expr ) | ^( OR_ASSIGN expr expr ) | ^( XOR_ASSIGN expr expr ) | ^( MOD_ASSIGN expr expr ) | ^( BIT_SHIFT_RIGHT_ASSIGN expr expr ) | ^( SHIFT_RIGHT_ASSIGN expr expr ) | ^( SHIFT_LEFT_ASSIGN expr expr ) | ^( QUESTION expr expr expr ) | ^( LOGICAL_OR expr expr ) | ^( LOGICAL_AND expr expr ) | ^( OR expr expr ) | ^( XOR expr expr ) | ^( AND expr expr ) | ^( EQUAL expr expr ) | ^( NOT_EQUAL expr expr ) | ^( INSTANCEOF expr type ) | ^( LESS_OR_EQUAL expr expr ) | ^( GREATER_OR_EQUAL expr expr ) | ^( BIT_SHIFT_RIGHT expr expr ) | ^( SHIFT_RIGHT expr expr ) | ^( GREATER_THAN expr expr ) | ^( SHIFT_LEFT expr expr ) | ^( LESS_THAN expr expr ) | ^( PLUS expr expr ) | ^( MINUS expr expr ) | ^( STAR expr expr ) | ^( DIV expr expr ) | ^( MOD expr expr ) | ^( UNARY_PLUS expr ) | ^( UNARY_MINUS expr ) | ^( PRE_INC expr ) | ^( PRE_DEC expr ) | ^( POST_INC expr ) | ^( POST_DEC expr ) | ^( NOT expr ) | ^( LOGICAL_NOT expr ) | ^( CAST_EXPR type expr ) | primaryExpression )
            int alt91 = 43;
            switch (input.LA(1)) {
                case ASSIGN: {
                    alt91 = 1;
                }
                break;
                case PLUS_ASSIGN: {
                    alt91 = 2;
                }
                break;
                case MINUS_ASSIGN: {
                    alt91 = 3;
                }
                break;
                case STAR_ASSIGN: {
                    alt91 = 4;
                }
                break;
                case DIV_ASSIGN: {
                    alt91 = 5;
                }
                break;
                case AND_ASSIGN: {
                    alt91 = 6;
                }
                break;
                case OR_ASSIGN: {
                    alt91 = 7;
                }
                break;
                case XOR_ASSIGN: {
                    alt91 = 8;
                }
                break;
                case MOD_ASSIGN: {
                    alt91 = 9;
                }
                break;
                case BIT_SHIFT_RIGHT_ASSIGN: {
                    alt91 = 10;
                }
                break;
                case SHIFT_RIGHT_ASSIGN: {
                    alt91 = 11;
                }
                break;
                case SHIFT_LEFT_ASSIGN: {
                    alt91 = 12;
                }
                break;
                case QUESTION: {
                    alt91 = 13;
                }
                break;
                case LOGICAL_OR: {
                    alt91 = 14;
                }
                break;
                case LOGICAL_AND: {
                    alt91 = 15;
                }
                break;
                case OR: {
                    alt91 = 16;
                }
                break;
                case XOR: {
                    alt91 = 17;
                }
                break;
                case AND: {
                    alt91 = 18;
                }
                break;
                case EQUAL: {
                    alt91 = 19;
                }
                break;
                case NOT_EQUAL: {
                    alt91 = 20;
                }
                break;
                case INSTANCEOF: {
                    alt91 = 21;
                }
                break;
                case LESS_OR_EQUAL: {
                    alt91 = 22;
                }
                break;
                case GREATER_OR_EQUAL: {
                    alt91 = 23;
                }
                break;
                case BIT_SHIFT_RIGHT: {
                    alt91 = 24;
                }
                break;
                case SHIFT_RIGHT: {
                    alt91 = 25;
                }
                break;
                case GREATER_THAN: {
                    alt91 = 26;
                }
                break;
                case SHIFT_LEFT: {
                    alt91 = 27;
                }
                break;
                case LESS_THAN: {
                    alt91 = 28;
                }
                break;
                case PLUS: {
                    alt91 = 29;
                }
                break;
                case MINUS: {
                    alt91 = 30;
                }
                break;
                case STAR: {
                    alt91 = 31;
                }
                break;
                case DIV: {
                    alt91 = 32;
                }
                break;
                case MOD: {
                    alt91 = 33;
                }
                break;
                case UNARY_PLUS: {
                    alt91 = 34;
                }
                break;
                case UNARY_MINUS: {
                    alt91 = 35;
                }
                break;
                case PRE_INC: {
                    alt91 = 36;
                }
                break;
                case PRE_DEC: {
                    alt91 = 37;
                }
                break;
                case POST_INC: {
                    alt91 = 38;
                }
                break;
                case POST_DEC: {
                    alt91 = 39;
                }
                break;
                case NOT: {
                    alt91 = 40;
                }
                break;
                case LOGICAL_NOT: {
                    alt91 = 41;
                }
                break;
                case CAST_EXPR: {
                    alt91 = 42;
                }
                break;
                case DOT:
                case FALSE:
                case NULL:
                case SUPER:
                case THIS:
                case TRUE:
                case ARRAY_DECLARATOR:
                case ARRAY_ELEMENT_ACCESS:
                case CLASS_CONSTRUCTOR_CALL:
                case METHOD_CALL:
                case PARENTESIZED_EXPR:
                case STATIC_ARRAY_CREATOR:
                case SUPER_CONSTRUCTOR_CALL:
                case THIS_CONSTRUCTOR_CALL:
                case IDENT:
                case HEX_LITERAL:
                case OCTAL_LITERAL:
                case DECIMAL_LITERAL:
                case FLOATING_POINT_LITERAL:
                case CHARACTER_LITERAL:
                case STRING_LITERAL: {
                    alt91 = 43;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 91, 0, input);

                    throw nvae;
            }

            switch (alt91) {
                case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:431:9: ^( ASSIGN expr expr )
                {
                    match(input, ASSIGN, FOLLOW_ASSIGN_in_expr2812);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2814);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2816);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:432:9: ^( PLUS_ASSIGN expr expr )
                {
                    match(input, PLUS_ASSIGN, FOLLOW_PLUS_ASSIGN_in_expr2828);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2830);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2832);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 3: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:433:9: ^( MINUS_ASSIGN expr expr )
                {
                    match(input, MINUS_ASSIGN, FOLLOW_MINUS_ASSIGN_in_expr2844);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2846);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2848);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 4: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:434:9: ^( STAR_ASSIGN expr expr )
                {
                    match(input, STAR_ASSIGN, FOLLOW_STAR_ASSIGN_in_expr2860);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2862);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2864);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 5: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:435:9: ^( DIV_ASSIGN expr expr )
                {
                    match(input, DIV_ASSIGN, FOLLOW_DIV_ASSIGN_in_expr2876);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2878);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2880);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 6: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:436:9: ^( AND_ASSIGN expr expr )
                {
                    match(input, AND_ASSIGN, FOLLOW_AND_ASSIGN_in_expr2892);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2894);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2896);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 7: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:437:9: ^( OR_ASSIGN expr expr )
                {
                    match(input, OR_ASSIGN, FOLLOW_OR_ASSIGN_in_expr2908);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2910);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2912);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 8: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:438:9: ^( XOR_ASSIGN expr expr )
                {
                    match(input, XOR_ASSIGN, FOLLOW_XOR_ASSIGN_in_expr2924);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2926);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2928);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 9: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:439:9: ^( MOD_ASSIGN expr expr )
                {
                    match(input, MOD_ASSIGN, FOLLOW_MOD_ASSIGN_in_expr2940);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2942);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2944);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 10: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:440:9: ^( BIT_SHIFT_RIGHT_ASSIGN expr expr )
                {
                    match(input, BIT_SHIFT_RIGHT_ASSIGN, FOLLOW_BIT_SHIFT_RIGHT_ASSIGN_in_expr2956);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2958);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2960);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 11: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:441:9: ^( SHIFT_RIGHT_ASSIGN expr expr )
                {
                    match(input, SHIFT_RIGHT_ASSIGN, FOLLOW_SHIFT_RIGHT_ASSIGN_in_expr2972);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2974);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2976);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 12: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:442:9: ^( SHIFT_LEFT_ASSIGN expr expr )
                {
                    match(input, SHIFT_LEFT_ASSIGN, FOLLOW_SHIFT_LEFT_ASSIGN_in_expr2988);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2990);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr2992);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 13: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:443:9: ^( QUESTION expr expr expr )
                {
                    match(input, QUESTION, FOLLOW_QUESTION_in_expr3004);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3006);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3008);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3010);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 14: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:444:9: ^( LOGICAL_OR expr expr )
                {
                    match(input, LOGICAL_OR, FOLLOW_LOGICAL_OR_in_expr3022);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3024);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3026);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 15: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:445:9: ^( LOGICAL_AND expr expr )
                {
                    match(input, LOGICAL_AND, FOLLOW_LOGICAL_AND_in_expr3038);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3040);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3042);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 16: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:446:9: ^( OR expr expr )
                {
                    match(input, OR, FOLLOW_OR_in_expr3054);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3056);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3058);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 17: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:447:9: ^( XOR expr expr )
                {
                    match(input, XOR, FOLLOW_XOR_in_expr3070);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3072);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3074);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 18: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:448:9: ^( AND expr expr )
                {
                    match(input, AND, FOLLOW_AND_in_expr3086);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3088);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3090);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 19: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:449:9: ^( EQUAL expr expr )
                {
                    match(input, EQUAL, FOLLOW_EQUAL_in_expr3102);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3104);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3106);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 20: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:450:9: ^( NOT_EQUAL expr expr )
                {
                    match(input, NOT_EQUAL, FOLLOW_NOT_EQUAL_in_expr3118);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3120);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3122);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 21: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:451:9: ^( INSTANCEOF expr type )
                {
                    match(input, INSTANCEOF, FOLLOW_INSTANCEOF_in_expr3134);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3136);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_type_in_expr3138);
                    type();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 22: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:452:9: ^( LESS_OR_EQUAL expr expr )
                {
                    match(input, LESS_OR_EQUAL, FOLLOW_LESS_OR_EQUAL_in_expr3150);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3152);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3154);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 23: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:453:9: ^( GREATER_OR_EQUAL expr expr )
                {
                    match(input, GREATER_OR_EQUAL, FOLLOW_GREATER_OR_EQUAL_in_expr3166);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3168);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3170);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 24: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:454:9: ^( BIT_SHIFT_RIGHT expr expr )
                {
                    match(input, BIT_SHIFT_RIGHT, FOLLOW_BIT_SHIFT_RIGHT_in_expr3182);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3184);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3186);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 25: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:455:9: ^( SHIFT_RIGHT expr expr )
                {
                    match(input, SHIFT_RIGHT, FOLLOW_SHIFT_RIGHT_in_expr3198);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3200);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3202);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 26: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:456:9: ^( GREATER_THAN expr expr )
                {
                    match(input, GREATER_THAN, FOLLOW_GREATER_THAN_in_expr3214);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3216);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3218);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 27: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:457:9: ^( SHIFT_LEFT expr expr )
                {
                    match(input, SHIFT_LEFT, FOLLOW_SHIFT_LEFT_in_expr3230);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3232);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3234);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 28: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:458:9: ^( LESS_THAN expr expr )
                {
                    match(input, LESS_THAN, FOLLOW_LESS_THAN_in_expr3246);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3248);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3250);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 29: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:459:9: ^( PLUS expr expr )
                {
                    match(input, PLUS, FOLLOW_PLUS_in_expr3262);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3264);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3266);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 30: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:460:9: ^( MINUS expr expr )
                {
                    match(input, MINUS, FOLLOW_MINUS_in_expr3278);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3280);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3282);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 31: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:461:9: ^( STAR expr expr )
                {
                    match(input, STAR, FOLLOW_STAR_in_expr3294);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3296);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3298);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 32: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:462:9: ^( DIV expr expr )
                {
                    match(input, DIV, FOLLOW_DIV_in_expr3310);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3312);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3314);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 33: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:463:9: ^( MOD expr expr )
                {
                    match(input, MOD, FOLLOW_MOD_in_expr3326);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3328);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3330);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 34: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:464:9: ^( UNARY_PLUS expr )
                {
                    match(input, UNARY_PLUS, FOLLOW_UNARY_PLUS_in_expr3342);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3344);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 35: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:465:9: ^( UNARY_MINUS expr )
                {
                    match(input, UNARY_MINUS, FOLLOW_UNARY_MINUS_in_expr3356);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3358);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 36: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:466:9: ^( PRE_INC expr )
                {
                    match(input, PRE_INC, FOLLOW_PRE_INC_in_expr3370);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3372);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 37: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:467:9: ^( PRE_DEC expr )
                {
                    match(input, PRE_DEC, FOLLOW_PRE_DEC_in_expr3384);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3386);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 38: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:468:9: ^( POST_INC expr )
                {
                    match(input, POST_INC, FOLLOW_POST_INC_in_expr3398);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3400);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 39: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:469:9: ^( POST_DEC expr )
                {
                    match(input, POST_DEC, FOLLOW_POST_DEC_in_expr3412);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3414);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 40: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:470:9: ^( NOT expr )
                {
                    match(input, NOT, FOLLOW_NOT_in_expr3426);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3428);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 41: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:471:9: ^( LOGICAL_NOT expr )
                {
                    match(input, LOGICAL_NOT, FOLLOW_LOGICAL_NOT_in_expr3440);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3442);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 42: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:472:9: ^( CAST_EXPR type expr )
                {
                    match(input, CAST_EXPR, FOLLOW_CAST_EXPR_in_expr3454);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_type_in_expr3456);
                    type();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expr_in_expr3458);
                    expr();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 43: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:473:9: primaryExpression
                {
                    pushFollow(FOLLOW_primaryExpression_in_expr3469);
                    primaryExpression();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 62, expr_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "expr"

    // $ANTLR start "primaryExpression"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:476:1: primaryExpression : ( ^( DOT ( primaryExpression ( IDENT | THIS | SUPER | innerNewExpression | CLASS ) | primitiveType CLASS | VOID CLASS ) ) | parenthesizedExpression | IDENT | ^( METHOD_CALL primaryExpression ( genericTypeArgumentList )? arguments ) | explicitConstructorCall | ^( ARRAY_ELEMENT_ACCESS primaryExpression expression ) | literal | newExpression | THIS | arrayTypeDeclarator | SUPER );
    public final void primaryExpression() throws RecognitionException {
        int primaryExpression_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 63)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:477:5: ( ^( DOT ( primaryExpression ( IDENT | THIS | SUPER | innerNewExpression | CLASS ) | primitiveType CLASS | VOID CLASS ) ) | parenthesizedExpression | IDENT | ^( METHOD_CALL primaryExpression ( genericTypeArgumentList )? arguments ) | explicitConstructorCall | ^( ARRAY_ELEMENT_ACCESS primaryExpression expression ) | literal | newExpression | THIS | arrayTypeDeclarator | SUPER )
            int alt95 = 11;
            switch (input.LA(1)) {
                case DOT: {
                    alt95 = 1;
                }
                break;
                case PARENTESIZED_EXPR: {
                    alt95 = 2;
                }
                break;
                case IDENT: {
                    alt95 = 3;
                }
                break;
                case METHOD_CALL: {
                    alt95 = 4;
                }
                break;
                case SUPER_CONSTRUCTOR_CALL:
                case THIS_CONSTRUCTOR_CALL: {
                    alt95 = 5;
                }
                break;
                case ARRAY_ELEMENT_ACCESS: {
                    alt95 = 6;
                }
                break;
                case FALSE:
                case NULL:
                case TRUE:
                case HEX_LITERAL:
                case OCTAL_LITERAL:
                case DECIMAL_LITERAL:
                case FLOATING_POINT_LITERAL:
                case CHARACTER_LITERAL:
                case STRING_LITERAL: {
                    alt95 = 7;
                }
                break;
                case CLASS_CONSTRUCTOR_CALL:
                case STATIC_ARRAY_CREATOR: {
                    alt95 = 8;
                }
                break;
                case THIS: {
                    alt95 = 9;
                }
                break;
                case ARRAY_DECLARATOR: {
                    alt95 = 10;
                }
                break;
                case SUPER: {
                    alt95 = 11;
                }
                break;
                default:
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    NoViableAltException nvae =
                            new NoViableAltException("", 95, 0, input);

                    throw nvae;
            }

            switch (alt95) {
                case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:477:9: ^( DOT ( primaryExpression ( IDENT | THIS | SUPER | innerNewExpression | CLASS ) | primitiveType CLASS | VOID CLASS ) )
                {
                    match(input, DOT, FOLLOW_DOT_in_primaryExpression3495);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:478:13: ( primaryExpression ( IDENT | THIS | SUPER | innerNewExpression | CLASS ) | primitiveType CLASS | VOID CLASS )
                    int alt93 = 3;
                    switch (input.LA(1)) {
                        case DOT:
                        case FALSE:
                        case NULL:
                        case SUPER:
                        case THIS:
                        case TRUE:
                        case ARRAY_DECLARATOR:
                        case ARRAY_ELEMENT_ACCESS:
                        case CLASS_CONSTRUCTOR_CALL:
                        case METHOD_CALL:
                        case PARENTESIZED_EXPR:
                        case STATIC_ARRAY_CREATOR:
                        case SUPER_CONSTRUCTOR_CALL:
                        case THIS_CONSTRUCTOR_CALL:
                        case IDENT:
                        case HEX_LITERAL:
                        case OCTAL_LITERAL:
                        case DECIMAL_LITERAL:
                        case FLOATING_POINT_LITERAL:
                        case CHARACTER_LITERAL:
                        case STRING_LITERAL: {
                            alt93 = 1;
                        }
                        break;
                        case BOOLEAN:
                        case BYTE:
                        case CHAR:
                        case DOUBLE:
                        case FLOAT:
                        case INT:
                        case LONG:
                        case SHORT: {
                            alt93 = 2;
                        }
                        break;
                        case VOID: {
                            alt93 = 3;
                        }
                        break;
                        default:
                            if (state.backtracking > 0) {
                                state.failed = true;
                                return;
                            }
                            NoViableAltException nvae =
                                    new NoViableAltException("", 93, 0, input);

                            throw nvae;
                    }

                    switch (alt93) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:478:17: primaryExpression ( IDENT | THIS | SUPER | innerNewExpression | CLASS )
                        {
                            pushFollow(FOLLOW_primaryExpression_in_primaryExpression3513);
                            primaryExpression();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }
                            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:479:17: ( IDENT | THIS | SUPER | innerNewExpression | CLASS )
                            int alt92 = 5;
                            switch (input.LA(1)) {
                                case IDENT: {
                                    alt92 = 1;
                                }
                                break;
                                case THIS: {
                                    alt92 = 2;
                                }
                                break;
                                case SUPER: {
                                    alt92 = 3;
                                }
                                break;
                                case CLASS_CONSTRUCTOR_CALL: {
                                    alt92 = 4;
                                }
                                break;
                                case CLASS: {
                                    alt92 = 5;
                                }
                                break;
                                default:
                                    if (state.backtracking > 0) {
                                        state.failed = true;
                                        return;
                                    }
                                    NoViableAltException nvae =
                                            new NoViableAltException("", 92, 0, input);

                                    throw nvae;
                            }

                            switch (alt92) {
                                case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:479:21: IDENT
                                {
                                    match(input, IDENT, FOLLOW_IDENT_in_primaryExpression3535);
                                    if (state.failed) {
                                        return;
                                    }

                                }
                                break;
                                case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:480:21: THIS
                                {
                                    match(input, THIS, FOLLOW_THIS_in_primaryExpression3557);
                                    if (state.failed) {
                                        return;
                                    }

                                }
                                break;
                                case 3: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:481:21: SUPER
                                {
                                    match(input, SUPER, FOLLOW_SUPER_in_primaryExpression3579);
                                    if (state.failed) {
                                        return;
                                    }

                                }
                                break;
                                case 4: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:482:21: innerNewExpression
                                {
                                    pushFollow(FOLLOW_innerNewExpression_in_primaryExpression3601);
                                    innerNewExpression();

                                    state._fsp--;
                                    if (state.failed) {
                                        return;
                                    }

                                }
                                break;
                                case 5: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:483:21: CLASS
                                {
                                    match(input, CLASS, FOLLOW_CLASS_in_primaryExpression3623);
                                    if (state.failed) {
                                        return;
                                    }

                                }
                                break;

                            }


                        }
                        break;
                        case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:485:17: primitiveType CLASS
                        {
                            pushFollow(FOLLOW_primitiveType_in_primaryExpression3659);
                            primitiveType();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }
                            match(input, CLASS, FOLLOW_CLASS_in_primaryExpression3661);
                            if (state.failed) {
                                return;
                            }

                        }
                        break;
                        case 3: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:486:17: VOID CLASS
                        {
                            match(input, VOID, FOLLOW_VOID_in_primaryExpression3679);
                            if (state.failed) {
                                return;
                            }
                            match(input, CLASS, FOLLOW_CLASS_in_primaryExpression3681);
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:489:9: parenthesizedExpression
                {
                    pushFollow(FOLLOW_parenthesizedExpression_in_primaryExpression3715);
                    parenthesizedExpression();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 3: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:490:9: IDENT
                {
                    match(input, IDENT, FOLLOW_IDENT_in_primaryExpression3725);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 4: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:491:9: ^( METHOD_CALL primaryExpression ( genericTypeArgumentList )? arguments )
                {
                    match(input, METHOD_CALL, FOLLOW_METHOD_CALL_in_primaryExpression3736);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_primaryExpression_in_primaryExpression3738);
                    primaryExpression();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:491:41: ( genericTypeArgumentList )?
                    int alt94 = 2;
                    int LA94_0 = input.LA(1);

                    if ((LA94_0 == GENERIC_TYPE_ARG_LIST)) {
                        alt94 = 1;
                    }
                    switch (alt94) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: genericTypeArgumentList
                        {
                            pushFollow(FOLLOW_genericTypeArgumentList_in_primaryExpression3740);
                            genericTypeArgumentList();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    pushFollow(FOLLOW_arguments_in_primaryExpression3743);
                    arguments();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 5: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:492:9: explicitConstructorCall
                {
                    pushFollow(FOLLOW_explicitConstructorCall_in_primaryExpression3754);
                    explicitConstructorCall();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 6: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:493:9: ^( ARRAY_ELEMENT_ACCESS primaryExpression expression )
                {
                    match(input, ARRAY_ELEMENT_ACCESS, FOLLOW_ARRAY_ELEMENT_ACCESS_in_primaryExpression3765);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_primaryExpression_in_primaryExpression3767);
                    primaryExpression();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_expression_in_primaryExpression3769);
                    expression();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 7: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:494:9: literal
                {
                    pushFollow(FOLLOW_literal_in_primaryExpression3780);
                    literal();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 8: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:495:9: newExpression
                {
                    pushFollow(FOLLOW_newExpression_in_primaryExpression3790);
                    newExpression();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 9: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:496:9: THIS
                {
                    match(input, THIS, FOLLOW_THIS_in_primaryExpression3800);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 10: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:497:9: arrayTypeDeclarator
                {
                    pushFollow(FOLLOW_arrayTypeDeclarator_in_primaryExpression3810);
                    arrayTypeDeclarator();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 11: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:498:9: SUPER
                {
                    match(input, SUPER, FOLLOW_SUPER_in_primaryExpression3820);
                    if (state.failed) {
                        return;
                    }

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 63, primaryExpression_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "primaryExpression"

    // $ANTLR start "explicitConstructorCall"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:501:1: explicitConstructorCall : ( ^( THIS_CONSTRUCTOR_CALL ( genericTypeArgumentList )? arguments ) | ^( SUPER_CONSTRUCTOR_CALL ( primaryExpression )? ( genericTypeArgumentList )? arguments ) );
    public final void explicitConstructorCall() throws RecognitionException {
        int explicitConstructorCall_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 64)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:502:5: ( ^( THIS_CONSTRUCTOR_CALL ( genericTypeArgumentList )? arguments ) | ^( SUPER_CONSTRUCTOR_CALL ( primaryExpression )? ( genericTypeArgumentList )? arguments ) )
            int alt99 = 2;
            int LA99_0 = input.LA(1);

            if ((LA99_0 == THIS_CONSTRUCTOR_CALL)) {
                alt99 = 1;
            } else if ((LA99_0 == SUPER_CONSTRUCTOR_CALL)) {
                alt99 = 2;
            } else {
                if (state.backtracking > 0) {
                    state.failed = true;
                    return;
                }
                NoViableAltException nvae =
                        new NoViableAltException("", 99, 0, input);

                throw nvae;
            }
            switch (alt99) {
                case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:502:9: ^( THIS_CONSTRUCTOR_CALL ( genericTypeArgumentList )? arguments )
                {
                    match(input, THIS_CONSTRUCTOR_CALL, FOLLOW_THIS_CONSTRUCTOR_CALL_in_explicitConstructorCall3844);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:502:33: ( genericTypeArgumentList )?
                    int alt96 = 2;
                    int LA96_0 = input.LA(1);

                    if ((LA96_0 == GENERIC_TYPE_ARG_LIST)) {
                        alt96 = 1;
                    }
                    switch (alt96) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: genericTypeArgumentList
                        {
                            pushFollow(FOLLOW_genericTypeArgumentList_in_explicitConstructorCall3846);
                            genericTypeArgumentList();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    pushFollow(FOLLOW_arguments_in_explicitConstructorCall3849);
                    arguments();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:503:9: ^( SUPER_CONSTRUCTOR_CALL ( primaryExpression )? ( genericTypeArgumentList )? arguments )
                {
                    match(input, SUPER_CONSTRUCTOR_CALL, FOLLOW_SUPER_CONSTRUCTOR_CALL_in_explicitConstructorCall3861);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:503:34: ( primaryExpression )?
                    int alt97 = 2;
                    int LA97_0 = input.LA(1);

                    if ((LA97_0 == DOT || LA97_0 == FALSE || LA97_0 == NULL || LA97_0 == SUPER || LA97_0 == THIS || LA97_0 == TRUE || LA97_0 == ARRAY_DECLARATOR || LA97_0 == ARRAY_ELEMENT_ACCESS || LA97_0 == CLASS_CONSTRUCTOR_CALL || LA97_0 == METHOD_CALL || LA97_0 == PARENTESIZED_EXPR || (LA97_0 >= STATIC_ARRAY_CREATOR && LA97_0 <= SUPER_CONSTRUCTOR_CALL) || LA97_0 == THIS_CONSTRUCTOR_CALL || (LA97_0 >= IDENT && LA97_0 <= STRING_LITERAL))) {
                        alt97 = 1;
                    }
                    switch (alt97) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: primaryExpression
                        {
                            pushFollow(FOLLOW_primaryExpression_in_explicitConstructorCall3863);
                            primaryExpression();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:503:53: ( genericTypeArgumentList )?
                    int alt98 = 2;
                    int LA98_0 = input.LA(1);

                    if ((LA98_0 == GENERIC_TYPE_ARG_LIST)) {
                        alt98 = 1;
                    }
                    switch (alt98) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: genericTypeArgumentList
                        {
                            pushFollow(FOLLOW_genericTypeArgumentList_in_explicitConstructorCall3866);
                            genericTypeArgumentList();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    pushFollow(FOLLOW_arguments_in_explicitConstructorCall3869);
                    arguments();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 64, explicitConstructorCall_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "explicitConstructorCall"

    // $ANTLR start "arrayTypeDeclarator"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:506:1: arrayTypeDeclarator : ^( ARRAY_DECLARATOR ( arrayTypeDeclarator | qualifiedIdentifier | primitiveType ) ) ;
    public final void arrayTypeDeclarator() throws RecognitionException {
        int arrayTypeDeclarator_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 65)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:507:5: ( ^( ARRAY_DECLARATOR ( arrayTypeDeclarator | qualifiedIdentifier | primitiveType ) ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:507:9: ^( ARRAY_DECLARATOR ( arrayTypeDeclarator | qualifiedIdentifier | primitiveType ) )
            {
                match(input, ARRAY_DECLARATOR, FOLLOW_ARRAY_DECLARATOR_in_arrayTypeDeclarator3890);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:507:28: ( arrayTypeDeclarator | qualifiedIdentifier | primitiveType )
                int alt100 = 3;
                switch (input.LA(1)) {
                    case ARRAY_DECLARATOR: {
                        alt100 = 1;
                    }
                    break;
                    case DOT:
                    case IDENT: {
                        alt100 = 2;
                    }
                    break;
                    case BOOLEAN:
                    case BYTE:
                    case CHAR:
                    case DOUBLE:
                    case FLOAT:
                    case INT:
                    case LONG:
                    case SHORT: {
                        alt100 = 3;
                    }
                    break;
                    default:
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 100, 0, input);

                        throw nvae;
                }

                switch (alt100) {
                    case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:507:29: arrayTypeDeclarator
                    {
                        pushFollow(FOLLOW_arrayTypeDeclarator_in_arrayTypeDeclarator3893);
                        arrayTypeDeclarator();

                        state._fsp--;
                        if (state.failed) {
                            return;
                        }

                    }
                    break;
                    case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:507:51: qualifiedIdentifier
                    {
                        pushFollow(FOLLOW_qualifiedIdentifier_in_arrayTypeDeclarator3897);
                        qualifiedIdentifier();

                        state._fsp--;
                        if (state.failed) {
                            return;
                        }

                    }
                    break;
                    case 3: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:507:73: primitiveType
                    {
                        pushFollow(FOLLOW_primitiveType_in_arrayTypeDeclarator3901);
                        primitiveType();

                        state._fsp--;
                        if (state.failed) {
                            return;
                        }

                    }
                    break;

                }


                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 65, arrayTypeDeclarator_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "arrayTypeDeclarator"

    // $ANTLR start "newExpression"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:510:1: newExpression : ( ^( STATIC_ARRAY_CREATOR ( primitiveType newArrayConstruction | ( genericTypeArgumentList )? qualifiedTypeIdent newArrayConstruction ) ) | ^( CLASS_CONSTRUCTOR_CALL ( genericTypeArgumentList )? qualifiedTypeIdent arguments ( classTopLevelScope )? ) );
    public final void newExpression() throws RecognitionException {
        int newExpression_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 66)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:511:5: ( ^( STATIC_ARRAY_CREATOR ( primitiveType newArrayConstruction | ( genericTypeArgumentList )? qualifiedTypeIdent newArrayConstruction ) ) | ^( CLASS_CONSTRUCTOR_CALL ( genericTypeArgumentList )? qualifiedTypeIdent arguments ( classTopLevelScope )? ) )
            int alt105 = 2;
            int LA105_0 = input.LA(1);

            if ((LA105_0 == STATIC_ARRAY_CREATOR)) {
                alt105 = 1;
            } else if ((LA105_0 == CLASS_CONSTRUCTOR_CALL)) {
                alt105 = 2;
            } else {
                if (state.backtracking > 0) {
                    state.failed = true;
                    return;
                }
                NoViableAltException nvae =
                        new NoViableAltException("", 105, 0, input);

                throw nvae;
            }
            switch (alt105) {
                case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:511:9: ^( STATIC_ARRAY_CREATOR ( primitiveType newArrayConstruction | ( genericTypeArgumentList )? qualifiedTypeIdent newArrayConstruction ) )
                {
                    match(input, STATIC_ARRAY_CREATOR, FOLLOW_STATIC_ARRAY_CREATOR_in_newExpression3925);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:512:13: ( primitiveType newArrayConstruction | ( genericTypeArgumentList )? qualifiedTypeIdent newArrayConstruction )
                    int alt102 = 2;
                    int LA102_0 = input.LA(1);

                    if ((LA102_0 == BOOLEAN || LA102_0 == BYTE || LA102_0 == CHAR || LA102_0 == DOUBLE || LA102_0 == FLOAT || (LA102_0 >= INT && LA102_0 <= LONG) || LA102_0 == SHORT)) {
                        alt102 = 1;
                    } else if ((LA102_0 == GENERIC_TYPE_ARG_LIST || LA102_0 == QUALIFIED_TYPE_IDENT)) {
                        alt102 = 2;
                    } else {
                        if (state.backtracking > 0) {
                            state.failed = true;
                            return;
                        }
                        NoViableAltException nvae =
                                new NoViableAltException("", 102, 0, input);

                        throw nvae;
                    }
                    switch (alt102) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:512:17: primitiveType newArrayConstruction
                        {
                            pushFollow(FOLLOW_primitiveType_in_newExpression3943);
                            primitiveType();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }
                            pushFollow(FOLLOW_newArrayConstruction_in_newExpression3945);
                            newArrayConstruction();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;
                        case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:513:17: ( genericTypeArgumentList )? qualifiedTypeIdent newArrayConstruction
                        {
                            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:513:17: ( genericTypeArgumentList )?
                            int alt101 = 2;
                            int LA101_0 = input.LA(1);

                            if ((LA101_0 == GENERIC_TYPE_ARG_LIST)) {
                                alt101 = 1;
                            }
                            switch (alt101) {
                                case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: genericTypeArgumentList
                                {
                                    pushFollow(FOLLOW_genericTypeArgumentList_in_newExpression3963);
                                    genericTypeArgumentList();

                                    state._fsp--;
                                    if (state.failed) {
                                        return;
                                    }

                                }
                                break;

                            }

                            pushFollow(FOLLOW_qualifiedTypeIdent_in_newExpression3966);
                            qualifiedTypeIdent();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }
                            pushFollow(FOLLOW_newArrayConstruction_in_newExpression3968);
                            newArrayConstruction();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:516:9: ^( CLASS_CONSTRUCTOR_CALL ( genericTypeArgumentList )? qualifiedTypeIdent arguments ( classTopLevelScope )? )
                {
                    match(input, CLASS_CONSTRUCTOR_CALL, FOLLOW_CLASS_CONSTRUCTOR_CALL_in_newExpression4003);
                    if (state.failed) {
                        return;
                    }

                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:516:34: ( genericTypeArgumentList )?
                    int alt103 = 2;
                    int LA103_0 = input.LA(1);

                    if ((LA103_0 == GENERIC_TYPE_ARG_LIST)) {
                        alt103 = 1;
                    }
                    switch (alt103) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: genericTypeArgumentList
                        {
                            pushFollow(FOLLOW_genericTypeArgumentList_in_newExpression4005);
                            genericTypeArgumentList();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }

                    pushFollow(FOLLOW_qualifiedTypeIdent_in_newExpression4008);
                    qualifiedTypeIdent();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_arguments_in_newExpression4010);
                    arguments();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:516:88: ( classTopLevelScope )?
                    int alt104 = 2;
                    int LA104_0 = input.LA(1);

                    if ((LA104_0 == CLASS_TOP_LEVEL_SCOPE)) {
                        alt104 = 1;
                    }
                    switch (alt104) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: classTopLevelScope
                        {
                            pushFollow(FOLLOW_classTopLevelScope_in_newExpression4012);
                            classTopLevelScope();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }

                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 66, newExpression_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "newExpression"

    // $ANTLR start "innerNewExpression"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:519:1: innerNewExpression : ^( CLASS_CONSTRUCTOR_CALL ( genericTypeArgumentList )? IDENT arguments ( classTopLevelScope )? ) ;
    public final void innerNewExpression() throws RecognitionException {
        int innerNewExpression_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 67)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:520:5: ( ^( CLASS_CONSTRUCTOR_CALL ( genericTypeArgumentList )? IDENT arguments ( classTopLevelScope )? ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:520:9: ^( CLASS_CONSTRUCTOR_CALL ( genericTypeArgumentList )? IDENT arguments ( classTopLevelScope )? )
            {
                match(input, CLASS_CONSTRUCTOR_CALL, FOLLOW_CLASS_CONSTRUCTOR_CALL_in_innerNewExpression4035);
                if (state.failed) {
                    return;
                }

                match(input, Token.DOWN, null);
                if (state.failed) {
                    return;
                }
                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:520:34: ( genericTypeArgumentList )?
                int alt106 = 2;
                int LA106_0 = input.LA(1);

                if ((LA106_0 == GENERIC_TYPE_ARG_LIST)) {
                    alt106 = 1;
                }
                switch (alt106) {
                    case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: genericTypeArgumentList
                    {
                        pushFollow(FOLLOW_genericTypeArgumentList_in_innerNewExpression4037);
                        genericTypeArgumentList();

                        state._fsp--;
                        if (state.failed) {
                            return;
                        }

                    }
                    break;

                }

                match(input, IDENT, FOLLOW_IDENT_in_innerNewExpression4040);
                if (state.failed) {
                    return;
                }
                pushFollow(FOLLOW_arguments_in_innerNewExpression4042);
                arguments();

                state._fsp--;
                if (state.failed) {
                    return;
                }
                // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:520:75: ( classTopLevelScope )?
                int alt107 = 2;
                int LA107_0 = input.LA(1);

                if ((LA107_0 == CLASS_TOP_LEVEL_SCOPE)) {
                    alt107 = 1;
                }
                switch (alt107) {
                    case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: classTopLevelScope
                    {
                        pushFollow(FOLLOW_classTopLevelScope_in_innerNewExpression4044);
                        classTopLevelScope();

                        state._fsp--;
                        if (state.failed) {
                            return;
                        }

                    }
                    break;

                }


                match(input, Token.UP, null);
                if (state.failed) {
                    return;
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 67, innerNewExpression_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "innerNewExpression"

    // $ANTLR start "newArrayConstruction"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:523:1: newArrayConstruction : ( arrayDeclaratorList arrayInitializer | ( expression )+ ( arrayDeclaratorList )? );
    public final void newArrayConstruction() throws RecognitionException {
        int newArrayConstruction_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 68)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:524:5: ( arrayDeclaratorList arrayInitializer | ( expression )+ ( arrayDeclaratorList )? )
            int alt110 = 2;
            int LA110_0 = input.LA(1);

            if ((LA110_0 == ARRAY_DECLARATOR_LIST)) {
                alt110 = 1;
            } else if ((LA110_0 == EXPR)) {
                alt110 = 2;
            } else {
                if (state.backtracking > 0) {
                    state.failed = true;
                    return;
                }
                NoViableAltException nvae =
                        new NoViableAltException("", 110, 0, input);

                throw nvae;
            }
            switch (alt110) {
                case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:524:9: arrayDeclaratorList arrayInitializer
                {
                    pushFollow(FOLLOW_arrayDeclaratorList_in_newArrayConstruction4069);
                    arrayDeclaratorList();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }
                    pushFollow(FOLLOW_arrayInitializer_in_newArrayConstruction4071);
                    arrayInitializer();

                    state._fsp--;
                    if (state.failed) {
                        return;
                    }

                }
                break;
                case 2: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:525:9: ( expression )+ ( arrayDeclaratorList )?
                {
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:525:9: ( expression )+
                    int cnt108 = 0;
                    loop108:
                    do {
                        int alt108 = 2;
                        int LA108_0 = input.LA(1);

                        if ((LA108_0 == EXPR)) {
                            alt108 = 1;
                        }


                        switch (alt108) {
                            case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: expression
                            {
                                pushFollow(FOLLOW_expression_in_newArrayConstruction4081);
                                expression();

                                state._fsp--;
                                if (state.failed) {
                                    return;
                                }

                            }
                            break;

                            default:
                                if (cnt108 >= 1) {
                                    break loop108;
                                }
                                if (state.backtracking > 0) {
                                    state.failed = true;
                                    return;
                                }
                                EarlyExitException eee =
                                        new EarlyExitException(108, input);
                                throw eee;
                        }
                        cnt108++;
                    } while (true);

                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:525:21: ( arrayDeclaratorList )?
                    int alt109 = 2;
                    int LA109_0 = input.LA(1);

                    if ((LA109_0 == ARRAY_DECLARATOR_LIST)) {
                        alt109 = 1;
                    }
                    switch (alt109) {
                        case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: arrayDeclaratorList
                        {
                            pushFollow(FOLLOW_arrayDeclaratorList_in_newArrayConstruction4084);
                            arrayDeclaratorList();

                            state._fsp--;
                            if (state.failed) {
                                return;
                            }

                        }
                        break;

                    }


                }
                break;

            }
        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 68, newArrayConstruction_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "newArrayConstruction"

    // $ANTLR start "arguments"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:528:1: arguments : ^( ARGUMENT_LIST ( expression )* ) ;
    public final void arguments() throws RecognitionException {
        int arguments_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 69)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:529:5: ( ^( ARGUMENT_LIST ( expression )* ) )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:529:9: ^( ARGUMENT_LIST ( expression )* )
            {
                match(input, ARGUMENT_LIST, FOLLOW_ARGUMENT_LIST_in_arguments4105);
                if (state.failed) {
                    return;
                }

                if (input.LA(1) == Token.DOWN) {
                    match(input, Token.DOWN, null);
                    if (state.failed) {
                        return;
                    }
                    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:529:25: ( expression )*
                    loop111:
                    do {
                        int alt111 = 2;
                        int LA111_0 = input.LA(1);

                        if ((LA111_0 == EXPR)) {
                            alt111 = 1;
                        }


                        switch (alt111) {
                            case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: expression
                            {
                                pushFollow(FOLLOW_expression_in_arguments4107);
                                expression();

                                state._fsp--;
                                if (state.failed) {
                                    return;
                                }

                            }
                            break;

                            default:
                                break loop111;
                        }
                    } while (true);


                    match(input, Token.UP, null);
                    if (state.failed) {
                        return;
                    }
                }

            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 69, arguments_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "arguments"

    // $ANTLR start "literal"
    // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:532:1: literal : ( HEX_LITERAL | OCTAL_LITERAL | DECIMAL_LITERAL | FLOATING_POINT_LITERAL | CHARACTER_LITERAL | STRING_LITERAL | TRUE | FALSE | NULL );
    public final void literal() throws RecognitionException {
        int literal_StartIndex = input.index();
        try {
            if (state.backtracking > 0 && alreadyParsedRule(input, 70)) {
                return;
            }
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:533:5: ( HEX_LITERAL | OCTAL_LITERAL | DECIMAL_LITERAL | FLOATING_POINT_LITERAL | CHARACTER_LITERAL | STRING_LITERAL | TRUE | FALSE | NULL )
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:
            {
                if (input.LA(1) == FALSE || input.LA(1) == NULL || input.LA(1) == TRUE || (input.LA(1) >= HEX_LITERAL && input.LA(1) <= STRING_LITERAL)) {
                    input.consume();
                    state.errorRecovery = false;
                    state.failed = false;
                } else {
                    if (state.backtracking > 0) {
                        state.failed = true;
                        return;
                    }
                    MismatchedSetException mse = new MismatchedSetException(null, input);
                    throw mse;
                }


            }

        } catch (RecognitionException re) {
            reportError(re);
            recover(input, re);
        } finally {
            if (state.backtracking > 0) {
                memoize(input, 70, literal_StartIndex);
            }
        }
        return;
    }
    // $ANTLR end "literal"

    // $ANTLR start synpred125_JavaTreeParser
    public final void synpred125_JavaTreeParser_fragment() throws RecognitionException {
        // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:397:35: ( switchCaseLabel )
        // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:397:35: switchCaseLabel
        {
            pushFollow(FOLLOW_switchCaseLabel_in_synpred125_JavaTreeParser2575);
            switchCaseLabel();

            state._fsp--;
            if (state.failed) {
                return;
            }

        }
    }
    // $ANTLR end synpred125_JavaTreeParser

    // $ANTLR start synpred132_JavaTreeParser
    public final void synpred132_JavaTreeParser_fragment() throws RecognitionException {
        // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:409:48: ( ( expression )* )
        // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:409:48: ( expression )*
        {
            // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:409:48: ( expression )*
            loop142:
            do {
                int alt142 = 2;
                int LA142_0 = input.LA(1);

                if ((LA142_0 == EXPR)) {
                    alt142 = 1;
                }


                switch (alt142) {
                    case 1: // /Users/Thijmen/Documents/project/husacct_final/src/husacct/analyse/infrastructure/grammars/java/JavaTreeParser.g:0:0: expression
                    {
                        pushFollow(FOLLOW_expression_in_synpred132_JavaTreeParser2676);
                        expression();

                        state._fsp--;
                        if (state.failed) {
                            return;
                        }

                    }
                    break;

                    default:
                        break loop142;
                }
            } while (true);


        }
    }
    // $ANTLR end synpred132_JavaTreeParser

    // Delegated rules
    public final boolean synpred132_JavaTreeParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred132_JavaTreeParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }

    public final boolean synpred125_JavaTreeParser() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred125_JavaTreeParser_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: " + re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed = false;
        return success;
    }
    public static final BitSet FOLLOW_JAVA_SOURCE_in_javaSource91 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_annotationList_in_javaSource93 = new BitSet(new long[]{0x2000000000000088L, 0x0000000000106008L});
    public static final BitSet FOLLOW_packageDeclaration_in_javaSource95 = new BitSet(new long[]{0x2000000000000088L, 0x0000000000006008L});
    public static final BitSet FOLLOW_importDeclaration_in_javaSource98 = new BitSet(new long[]{0x2000000000000088L, 0x0000000000006008L});
    public static final BitSet FOLLOW_typeDeclaration_in_javaSource101 = new BitSet(new long[]{0x2000000000000088L, 0x0000000000002008L});
    public static final BitSet FOLLOW_PACKAGE_in_packageDeclaration123 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_qualifiedIdentifier_in_packageDeclaration125 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IMPORT_in_importDeclaration152 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_STATIC_in_importDeclaration154 = new BitSet(new long[]{0x0000000000008000L, 0x0000000000000000L, 0x0000001000000000L});
    public static final BitSet FOLLOW_qualifiedIdentifier_in_importDeclaration157 = new BitSet(new long[]{0x0000000000010008L});
    public static final BitSet FOLLOW_DOTSTAR_in_importDeclaration159 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CLASS_in_typeDeclaration185 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_modifierList_in_typeDeclaration187 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000001000000000L});
    public static final BitSet FOLLOW_IDENT_in_typeDeclaration189 = new BitSet(new long[]{0x0000000000000000L, 0x0800000000000000L, 0x0000000000001401L});
    public static final BitSet FOLLOW_genericTypeParameterList_in_typeDeclaration191 = new BitSet(new long[]{0x0000000000000000L, 0x0800000000000000L, 0x0000000000001401L});
    public static final BitSet FOLLOW_extendsClause_in_typeDeclaration194 = new BitSet(new long[]{0x0000000000000000L, 0x0800000000000000L, 0x0000000000001401L});
    public static final BitSet FOLLOW_implementsClause_in_typeDeclaration197 = new BitSet(new long[]{0x0000000000000000L, 0x0800000000000000L, 0x0000000000001401L});
    public static final BitSet FOLLOW_classTopLevelScope_in_typeDeclaration200 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_INTERFACE_in_typeDeclaration212 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_modifierList_in_typeDeclaration214 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000001000000000L});
    public static final BitSet FOLLOW_IDENT_in_typeDeclaration216 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000000000000C01L});
    public static final BitSet FOLLOW_genericTypeParameterList_in_typeDeclaration218 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000000000000C01L});
    public static final BitSet FOLLOW_extendsClause_in_typeDeclaration221 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000000000000C01L});
    public static final BitSet FOLLOW_interfaceTopLevelScope_in_typeDeclaration224 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ENUM_in_typeDeclaration236 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_modifierList_in_typeDeclaration238 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000001000000000L});
    public static final BitSet FOLLOW_IDENT_in_typeDeclaration240 = new BitSet(new long[]{0x0000000000000000L, 0x2000000000000000L, 0x0000000000001000L});
    public static final BitSet FOLLOW_implementsClause_in_typeDeclaration242 = new BitSet(new long[]{0x0000000000000000L, 0x2000000000000000L, 0x0000000000001000L});
    public static final BitSet FOLLOW_enumTopLevelScope_in_typeDeclaration245 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_AT_in_typeDeclaration257 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_modifierList_in_typeDeclaration259 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000001000000000L});
    public static final BitSet FOLLOW_IDENT_in_typeDeclaration261 = new BitSet(new long[]{0x0000000000000000L, 0x0000800000000000L});
    public static final BitSet FOLLOW_annotationTopLevelScope_in_typeDeclaration263 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EXTENDS_CLAUSE_in_extendsClause300 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_type_in_extendsClause302 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x0000000020000000L});
    public static final BitSet FOLLOW_IMPLEMENTS_CLAUSE_in_implementsClause331 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_type_in_implementsClause333 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x0000000020000000L});
    public static final BitSet FOLLOW_GENERIC_TYPE_PARAM_LIST_in_genericTypeParameterList363 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_genericTypeParameter_in_genericTypeParameterList365 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x0000001000000000L});
    public static final BitSet FOLLOW_IDENT_in_genericTypeParameter387 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_bound_in_genericTypeParameter389 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EXTENDS_BOUND_LIST_in_bound419 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_type_in_bound421 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x0000000020000000L});
    public static final BitSet FOLLOW_ENUM_TOP_LEVEL_SCOPE_in_enumTopLevelScope443 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_enumConstant_in_enumTopLevelScope445 = new BitSet(new long[]{0x0000000000000008L, 0x0800000000000000L, 0x0000001000001401L});
    public static final BitSet FOLLOW_classTopLevelScope_in_enumTopLevelScope448 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IDENT_in_enumConstant474 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_annotationList_in_enumConstant476 = new BitSet(new long[]{0x0000000000000008L, 0x0801000000000000L, 0x0000000000001401L});
    public static final BitSet FOLLOW_arguments_in_enumConstant478 = new BitSet(new long[]{0x0000000000000008L, 0x0800000000000000L, 0x0000000000001401L});
    public static final BitSet FOLLOW_classTopLevelScope_in_enumConstant481 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CLASS_TOP_LEVEL_SCOPE_in_classTopLevelScope512 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_classScopeDeclarations_in_classTopLevelScope514 = new BitSet(new long[]{0x2000000000000088L, 0x1600000000002008L, 0x0000000900000100L});
    public static final BitSet FOLLOW_CLASS_INSTANCE_INITIALIZER_in_classScopeDeclarations540 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_block_in_classScopeDeclarations542 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CLASS_STATIC_INITIALIZER_in_classScopeDeclarations554 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_block_in_classScopeDeclarations556 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_FUNCTION_METHOD_DECL_in_classScopeDeclarations568 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_modifierList_in_classScopeDeclarations570 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x0000000020000400L});
    public static final BitSet FOLLOW_genericTypeParameterList_in_classScopeDeclarations572 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x0000000020000000L});
    public static final BitSet FOLLOW_type_in_classScopeDeclarations575 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000001000000000L});
    public static final BitSet FOLLOW_IDENT_in_classScopeDeclarations577 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000000000000020L});
    public static final BitSet FOLLOW_formalParameterList_in_classScopeDeclarations579 = new BitSet(new long[]{0x0000000000000008L, 0x0024000000000000L, 0x0000000010000000L});
    public static final BitSet FOLLOW_arrayDeclaratorList_in_classScopeDeclarations581 = new BitSet(new long[]{0x0000000000000008L, 0x0020000000000000L, 0x0000000010000000L});
    public static final BitSet FOLLOW_throwsClause_in_classScopeDeclarations584 = new BitSet(new long[]{0x0000000000000008L, 0x0020000000000000L});
    public static final BitSet FOLLOW_block_in_classScopeDeclarations587 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VOID_METHOD_DECL_in_classScopeDeclarations600 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_modifierList_in_classScopeDeclarations602 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000001000000400L});
    public static final BitSet FOLLOW_genericTypeParameterList_in_classScopeDeclarations604 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000001000000000L});
    public static final BitSet FOLLOW_IDENT_in_classScopeDeclarations607 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000000000000020L});
    public static final BitSet FOLLOW_formalParameterList_in_classScopeDeclarations609 = new BitSet(new long[]{0x0000000000000008L, 0x0020000000000000L, 0x0000000010000000L});
    public static final BitSet FOLLOW_throwsClause_in_classScopeDeclarations611 = new BitSet(new long[]{0x0000000000000008L, 0x0020000000000000L});
    public static final BitSet FOLLOW_block_in_classScopeDeclarations614 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VAR_DECLARATION_in_classScopeDeclarations627 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_modifierList_in_classScopeDeclarations629 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x0000000020000000L});
    public static final BitSet FOLLOW_type_in_classScopeDeclarations631 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000000400000000L});
    public static final BitSet FOLLOW_variableDeclaratorList_in_classScopeDeclarations633 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CONSTRUCTOR_DECL_in_classScopeDeclarations645 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_modifierList_in_classScopeDeclarations647 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000000000000420L});
    public static final BitSet FOLLOW_genericTypeParameterList_in_classScopeDeclarations649 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000000000000020L});
    public static final BitSet FOLLOW_formalParameterList_in_classScopeDeclarations652 = new BitSet(new long[]{0x0000000000000000L, 0x0020000000000000L, 0x0000000010000000L});
    public static final BitSet FOLLOW_throwsClause_in_classScopeDeclarations654 = new BitSet(new long[]{0x0000000000000000L, 0x0020000000000000L});
    public static final BitSet FOLLOW_block_in_classScopeDeclarations657 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_typeDeclaration_in_classScopeDeclarations668 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INTERFACE_TOP_LEVEL_SCOPE_in_interfaceTopLevelScope692 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_interfaceScopeDeclarations_in_interfaceTopLevelScope694 = new BitSet(new long[]{0x2000000000000088L, 0x0000000000002008L, 0x0000000900000100L});
    public static final BitSet FOLLOW_FUNCTION_METHOD_DECL_in_interfaceScopeDeclarations720 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_modifierList_in_interfaceScopeDeclarations722 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x0000000020000400L});
    public static final BitSet FOLLOW_genericTypeParameterList_in_interfaceScopeDeclarations724 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x0000000020000000L});
    public static final BitSet FOLLOW_type_in_interfaceScopeDeclarations727 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000001000000000L});
    public static final BitSet FOLLOW_IDENT_in_interfaceScopeDeclarations729 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000000000000020L});
    public static final BitSet FOLLOW_formalParameterList_in_interfaceScopeDeclarations731 = new BitSet(new long[]{0x0000000000000008L, 0x0004000000000000L, 0x0000000010000000L});
    public static final BitSet FOLLOW_arrayDeclaratorList_in_interfaceScopeDeclarations733 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x0000000010000000L});
    public static final BitSet FOLLOW_throwsClause_in_interfaceScopeDeclarations736 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VOID_METHOD_DECL_in_interfaceScopeDeclarations749 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_modifierList_in_interfaceScopeDeclarations751 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000001000000400L});
    public static final BitSet FOLLOW_genericTypeParameterList_in_interfaceScopeDeclarations753 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000001000000000L});
    public static final BitSet FOLLOW_IDENT_in_interfaceScopeDeclarations756 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000000000000020L});
    public static final BitSet FOLLOW_formalParameterList_in_interfaceScopeDeclarations758 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x0000000010000000L});
    public static final BitSet FOLLOW_throwsClause_in_interfaceScopeDeclarations760 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VAR_DECLARATION_in_interfaceScopeDeclarations851 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_modifierList_in_interfaceScopeDeclarations853 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x0000000020000000L});
    public static final BitSet FOLLOW_type_in_interfaceScopeDeclarations855 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000000400000000L});
    public static final BitSet FOLLOW_variableDeclaratorList_in_interfaceScopeDeclarations857 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_typeDeclaration_in_interfaceScopeDeclarations868 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_DECLARATOR_LIST_in_variableDeclaratorList888 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_variableDeclarator_in_variableDeclaratorList890 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x0000000200000000L});
    public static final BitSet FOLLOW_VAR_DECLARATOR_in_variableDeclarator912 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_variableDeclaratorId_in_variableDeclarator914 = new BitSet(new long[]{0x0000000000000008L, 0x4010000000000000L});
    public static final BitSet FOLLOW_variableInitializer_in_variableDeclarator916 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IDENT_in_variableDeclaratorId942 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_arrayDeclaratorList_in_variableDeclaratorId944 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_arrayInitializer_in_variableInitializer965 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_variableInitializer975 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACK_in_arrayDeclarator994 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_RBRACK_in_arrayDeclarator996 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ARRAY_DECLARATOR_LIST_in_arrayDeclaratorList1016 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_ARRAY_DECLARATOR_in_arrayDeclaratorList1018 = new BitSet(new long[]{0x0000000000000008L, 0x0002000000000000L});
    public static final BitSet FOLLOW_ARRAY_INITIALIZER_in_arrayInitializer1046 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_variableInitializer_in_arrayInitializer1048 = new BitSet(new long[]{0x0000000000000008L, 0x4010000000000000L});
    public static final BitSet FOLLOW_THROWS_CLAUSE_in_throwsClause1070 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_qualifiedIdentifier_in_throwsClause1072 = new BitSet(new long[]{0x0000000000008008L, 0x0000000000000000L, 0x0000001000000000L});
    public static final BitSet FOLLOW_MODIFIER_LIST_in_modifierList1094 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_modifier_in_modifierList1096 = new BitSet(new long[]{0x0020000000000088L, 0x000000444CE20040L});
    public static final BitSet FOLLOW_PUBLIC_in_modifier1117 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PROTECTED_in_modifier1127 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PRIVATE_in_modifier1137 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STATIC_in_modifier1147 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ABSTRACT_in_modifier1157 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NATIVE_in_modifier1167 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SYNCHRONIZED_in_modifier1177 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRANSIENT_in_modifier1187 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VOLATILE_in_modifier1197 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRICTFP_in_modifier1207 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localModifier_in_modifier1217 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LOCAL_MODIFIER_LIST_in_localModifierList1237 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_localModifier_in_localModifierList1239 = new BitSet(new long[]{0x0020000000000088L, 0x000000444CE20040L});
    public static final BitSet FOLLOW_FINAL_in_localModifier1260 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_localModifier1270 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TYPE_in_type1290 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_primitiveType_in_type1293 = new BitSet(new long[]{0x0000000000000008L, 0x0004000000000000L});
    public static final BitSet FOLLOW_qualifiedTypeIdent_in_type1297 = new BitSet(new long[]{0x0000000000000008L, 0x0004000000000000L});
    public static final BitSet FOLLOW_arrayDeclaratorList_in_type1300 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_QUALIFIED_TYPE_IDENT_in_qualifiedTypeIdent1322 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_typeIdent_in_qualifiedTypeIdent1324 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x0000001000000000L});
    public static final BitSet FOLLOW_IDENT_in_typeIdent1347 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_genericTypeArgumentList_in_typeIdent1349 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_set_in_primitiveType0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GENERIC_TYPE_ARG_LIST_in_genericTypeArgumentList1460 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_genericTypeArgument_in_genericTypeArgumentList1462 = new BitSet(new long[]{0x0000010000000008L, 0x0000000000000000L, 0x0000000020000000L});
    public static final BitSet FOLLOW_type_in_genericTypeArgument1487 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUESTION_in_genericTypeArgument1498 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_genericWildcardBoundType_in_genericTypeArgument1500 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EXTENDS_in_genericWildcardBoundType1640 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_type_in_genericWildcardBoundType1642 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SUPER_in_genericWildcardBoundType1654 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_type_in_genericWildcardBoundType1656 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_FORMAL_PARAM_LIST_in_formalParameterList1677 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_formalParameterStandardDecl_in_formalParameterList1679 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x00000000000000C0L});
    public static final BitSet FOLLOW_formalParameterVarargDecl_in_formalParameterList1682 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_FORMAL_PARAM_STD_DECL_in_formalParameterStandardDecl1709 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_localModifierList_in_formalParameterStandardDecl1711 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x0000000020000000L});
    public static final BitSet FOLLOW_type_in_formalParameterStandardDecl1713 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000001000000000L});
    public static final BitSet FOLLOW_variableDeclaratorId_in_formalParameterStandardDecl1715 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_FORMAL_PARAM_VARARG_DECL_in_formalParameterVarargDecl1740 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_localModifierList_in_formalParameterVarargDecl1742 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x0000000020000000L});
    public static final BitSet FOLLOW_type_in_formalParameterVarargDecl1744 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000001000000000L});
    public static final BitSet FOLLOW_variableDeclaratorId_in_formalParameterVarargDecl1746 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IDENT_in_qualifiedIdentifier1770 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_qualifiedIdentifier1781 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_qualifiedIdentifier_in_qualifiedIdentifier1783 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000001000000000L});
    public static final BitSet FOLLOW_IDENT_in_qualifiedIdentifier1785 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ANNOTATION_LIST_in_annotationList1812 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_annotation_in_annotationList1814 = new BitSet(new long[]{0x0020000000000088L, 0x000000444CE20040L});
    public static final BitSet FOLLOW_AT_in_annotation1836 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_qualifiedIdentifier_in_annotation1838 = new BitSet(new long[]{0x0000000000000008L, 0x0000020000000000L});
    public static final BitSet FOLLOW_annotationInit_in_annotation1840 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ANNOTATION_INIT_BLOCK_in_annotationInit1866 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_annotationInitializers_in_annotationInit1868 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ANNOTATION_INIT_KEY_LIST_in_annotationInitializers1889 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_annotationInitializer_in_annotationInitializers1891 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x0000001000000000L});
    public static final BitSet FOLLOW_ANNOTATION_INIT_DEFAULT_KEY_in_annotationInitializers1904 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_annotationElementValue_in_annotationInitializers1906 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IDENT_in_annotationInitializer1931 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_annotationElementValue_in_annotationInitializer1933 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ANNOTATION_INIT_ARRAY_ELEMENT_in_annotationElementValue1958 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_annotationElementValue_in_annotationElementValue1960 = new BitSet(new long[]{0x0020000000000088L, 0x401001444CE20040L});
    public static final BitSet FOLLOW_annotation_in_annotationElementValue1972 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_annotationElementValue1982 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ANNOTATION_TOP_LEVEL_SCOPE_in_annotationTopLevelScope2006 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_annotationScopeDeclarations_in_annotationTopLevelScope2008 = new BitSet(new long[]{0x2000000000000088L, 0x0000200000002008L, 0x0000000100000000L});
    public static final BitSet FOLLOW_ANNOTATION_METHOD_DECL_in_annotationScopeDeclarations2034 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_modifierList_in_annotationScopeDeclarations2036 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x0000000020000000L});
    public static final BitSet FOLLOW_type_in_annotationScopeDeclarations2038 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000001000000000L});
    public static final BitSet FOLLOW_IDENT_in_annotationScopeDeclarations2040 = new BitSet(new long[]{0x8000000000000008L});
    public static final BitSet FOLLOW_annotationDefaultValue_in_annotationScopeDeclarations2042 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VAR_DECLARATION_in_annotationScopeDeclarations2055 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_modifierList_in_annotationScopeDeclarations2057 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x0000000020000000L});
    public static final BitSet FOLLOW_type_in_annotationScopeDeclarations2059 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000000400000000L});
    public static final BitSet FOLLOW_variableDeclaratorList_in_annotationScopeDeclarations2061 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_typeDeclaration_in_annotationScopeDeclarations2072 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DEFAULT_in_annotationDefaultValue2096 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_annotationElementValue_in_annotationDefaultValue2098 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_BLOCK_SCOPE_in_block2121 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_blockStatement_in_block2123 = new BitSet(new long[]{0x6140100000000088L, 0x4030009161002609L, 0x0000000100002004L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_blockStatement2148 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeDeclaration_in_blockStatement2158 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_blockStatement2168 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VAR_DECLARATION_in_localVariableDeclaration2192 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_localModifierList_in_localVariableDeclaration2194 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x0000000020000000L});
    public static final BitSet FOLLOW_type_in_localVariableDeclaration2196 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000000400000000L});
    public static final BitSet FOLLOW_variableDeclaratorList_in_localVariableDeclaration2198 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_block_in_statement2231 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASSERT_in_statement2242 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_statement2244 = new BitSet(new long[]{0x0000000000000008L, 0x4010000000000000L});
    public static final BitSet FOLLOW_expression_in_statement2246 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_IF_in_statement2259 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_parenthesizedExpression_in_statement2261 = new BitSet(new long[]{0x6140100000000088L, 0x4030009161002609L, 0x0000000100002004L});
    public static final BitSet FOLLOW_statement_in_statement2263 = new BitSet(new long[]{0x6140100000000088L, 0x4030009161002609L, 0x0000000100002004L});
    public static final BitSet FOLLOW_statement_in_statement2265 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_FOR_in_statement2278 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_forInit_in_statement2280 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000000000000002L});
    public static final BitSet FOLLOW_forCondition_in_statement2282 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000000000000010L});
    public static final BitSet FOLLOW_forUpdater_in_statement2284 = new BitSet(new long[]{0x6140100000000088L, 0x4030009161002609L, 0x0000000100002004L});
    public static final BitSet FOLLOW_statement_in_statement2286 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_FOR_EACH_in_statement2298 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_localModifierList_in_statement2300 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x0000000020000000L});
    public static final BitSet FOLLOW_type_in_statement2302 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000001000000000L});
    public static final BitSet FOLLOW_IDENT_in_statement2304 = new BitSet(new long[]{0x0000000000000000L, 0x4010000000000000L});
    public static final BitSet FOLLOW_expression_in_statement2306 = new BitSet(new long[]{0x6140100000000088L, 0x4030009161002609L, 0x0000000100002004L});
    public static final BitSet FOLLOW_statement_in_statement2308 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_WHILE_in_statement2321 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_parenthesizedExpression_in_statement2323 = new BitSet(new long[]{0x6140100000000088L, 0x4030009161002609L, 0x0000000100002004L});
    public static final BitSet FOLLOW_statement_in_statement2325 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DO_in_statement2337 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_statement_in_statement2339 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000000000040000L});
    public static final BitSet FOLLOW_parenthesizedExpression_in_statement2341 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_TRY_in_statement2353 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_block_in_statement2355 = new BitSet(new long[]{0x0000000000000008L, 0x00A0000000000000L});
    public static final BitSet FOLLOW_catches_in_statement2357 = new BitSet(new long[]{0x0000000000000008L, 0x0020000000000000L});
    public static final BitSet FOLLOW_block_in_statement2360 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SWITCH_in_statement2375 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_parenthesizedExpression_in_statement2377 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000000004000000L});
    public static final BitSet FOLLOW_switchBlockLabels_in_statement2379 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SYNCHRONIZED_in_statement2391 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_parenthesizedExpression_in_statement2393 = new BitSet(new long[]{0x0000000000000000L, 0x0020000000000000L});
    public static final BitSet FOLLOW_block_in_statement2395 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_RETURN_in_statement2407 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_statement2409 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_THROW_in_statement2422 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_statement2424 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_BREAK_in_statement2436 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_statement2438 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CONTINUE_in_statement2451 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_statement2453 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LABELED_STATEMENT_in_statement2466 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_IDENT_in_statement2468 = new BitSet(new long[]{0x6140100000000088L, 0x4030009161002609L, 0x0000000100002004L});
    public static final BitSet FOLLOW_statement_in_statement2470 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_expression_in_statement2481 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_statement2491 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CATCH_CLAUSE_LIST_in_catches2520 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_catchClause_in_catches2522 = new BitSet(new long[]{0x0800000000000008L});
    public static final BitSet FOLLOW_CATCH_in_catchClause2548 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_formalParameterStandardDecl_in_catchClause2550 = new BitSet(new long[]{0x0000000000000000L, 0x0020000000000000L});
    public static final BitSet FOLLOW_block_in_catchClause2552 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SWITCH_BLOCK_LABEL_LIST_in_switchBlockLabels2573 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_switchCaseLabel_in_switchBlockLabels2575 = new BitSet(new long[]{0x8400000000000008L});
    public static final BitSet FOLLOW_switchDefaultLabel_in_switchBlockLabels2578 = new BitSet(new long[]{0x0400000000000008L});
    public static final BitSet FOLLOW_switchCaseLabel_in_switchBlockLabels2581 = new BitSet(new long[]{0x0400000000000008L});
    public static final BitSet FOLLOW_CASE_in_switchCaseLabel2611 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_switchCaseLabel2613 = new BitSet(new long[]{0x6140100000000088L, 0x4030009161002609L, 0x0000000100002004L});
    public static final BitSet FOLLOW_blockStatement_in_switchCaseLabel2615 = new BitSet(new long[]{0x6140100000000088L, 0x4030009161002609L, 0x0000000100002004L});
    public static final BitSet FOLLOW_DEFAULT_in_switchDefaultLabel2641 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_blockStatement_in_switchDefaultLabel2643 = new BitSet(new long[]{0x6140100000000088L, 0x4030009161002609L, 0x0000000100002004L});
    public static final BitSet FOLLOW_FOR_INIT_in_forInit2669 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_forInit2672 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_expression_in_forInit2676 = new BitSet(new long[]{0x0000000000000008L, 0x4010000000000000L});
    public static final BitSet FOLLOW_FOR_CONDITION_in_forCondition2704 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_forCondition2706 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_FOR_UPDATE_in_forUpdater2732 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_forUpdater2734 = new BitSet(new long[]{0x0000000000000008L, 0x4010000000000000L});
    public static final BitSet FOLLOW_PARENTESIZED_EXPR_in_parenthesizedExpression2762 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_parenthesizedExpression2764 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EXPR_in_expression2789 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expression2791 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ASSIGN_in_expr2812 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr2814 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr2816 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PLUS_ASSIGN_in_expr2828 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr2830 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr2832 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MINUS_ASSIGN_in_expr2844 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr2846 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr2848 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_STAR_ASSIGN_in_expr2860 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr2862 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr2864 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DIV_ASSIGN_in_expr2876 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr2878 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr2880 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_AND_ASSIGN_in_expr2892 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr2894 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr2896 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_OR_ASSIGN_in_expr2908 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr2910 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr2912 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_XOR_ASSIGN_in_expr2924 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr2926 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr2928 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MOD_ASSIGN_in_expr2940 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr2942 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr2944 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_BIT_SHIFT_RIGHT_ASSIGN_in_expr2956 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr2958 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr2960 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SHIFT_RIGHT_ASSIGN_in_expr2972 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr2974 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr2976 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SHIFT_LEFT_ASSIGN_in_expr2988 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr2990 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr2992 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_QUESTION_in_expr3004 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3006 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr3008 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr3010 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LOGICAL_OR_in_expr3022 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3024 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr3026 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LOGICAL_AND_in_expr3038 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3040 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr3042 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_OR_in_expr3054 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3056 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr3058 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_XOR_in_expr3070 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3072 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr3074 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_AND_in_expr3086 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3088 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr3090 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_EQUAL_in_expr3102 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3104 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr3106 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NOT_EQUAL_in_expr3118 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3120 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr3122 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_INSTANCEOF_in_expr3134 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3136 = new BitSet(new long[]{0x0000000000000008L, 0x0000000000000000L, 0x0000000020000000L});
    public static final BitSet FOLLOW_type_in_expr3138 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LESS_OR_EQUAL_in_expr3150 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3152 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr3154 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_GREATER_OR_EQUAL_in_expr3166 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3168 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr3170 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_BIT_SHIFT_RIGHT_in_expr3182 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3184 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr3186 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SHIFT_RIGHT_in_expr3198 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3200 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr3202 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_GREATER_THAN_in_expr3214 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3216 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr3218 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SHIFT_LEFT_in_expr3230 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3232 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr3234 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LESS_THAN_in_expr3246 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3248 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr3250 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PLUS_in_expr3262 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3264 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr3266 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MINUS_in_expr3278 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3280 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr3282 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_STAR_in_expr3294 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3296 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr3298 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_DIV_in_expr3310 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3312 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr3314 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_MOD_in_expr3326 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3328 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr3330 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_UNARY_PLUS_in_expr3342 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3344 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_UNARY_MINUS_in_expr3356 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3358 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PRE_INC_in_expr3370 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3372 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_PRE_DEC_in_expr3384 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3386 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_POST_INC_in_expr3398 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3400 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_POST_DEC_in_expr3412 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3414 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_NOT_in_expr3426 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3428 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_LOGICAL_NOT_in_expr3440 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expr_in_expr3442 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CAST_EXPR_in_expr3454 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_type_in_expr3456 = new BitSet(new long[]{0x001FE1FFDF1CE370L, 0x014A000890081020L, 0x000007F0CB7D0000L});
    public static final BitSet FOLLOW_expr_in_expr3458 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_primaryExpression_in_expr3469 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_primaryExpression3495 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_primaryExpression_in_primaryExpression3513 = new BitSet(new long[]{0x2000000000000000L, 0x0100000090000000L, 0x0000001000000000L});
    public static final BitSet FOLLOW_IDENT_in_primaryExpression3535 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_THIS_in_primaryExpression3557 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SUPER_in_primaryExpression3579 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_innerNewExpression_in_primaryExpression3601 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CLASS_in_primaryExpression3623 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_primitiveType_in_primaryExpression3659 = new BitSet(new long[]{0x2000000000000000L});
    public static final BitSet FOLLOW_CLASS_in_primaryExpression3661 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_VOID_in_primaryExpression3679 = new BitSet(new long[]{0x2000000000000000L});
    public static final BitSet FOLLOW_CLASS_in_primaryExpression3681 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_parenthesizedExpression_in_primaryExpression3715 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_primaryExpression3725 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_METHOD_CALL_in_primaryExpression3736 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_primaryExpression_in_primaryExpression3738 = new BitSet(new long[]{0x0000000000000000L, 0x0001000000000000L, 0x0000000000000200L});
    public static final BitSet FOLLOW_genericTypeArgumentList_in_primaryExpression3740 = new BitSet(new long[]{0x0000000000000000L, 0x0001000000000000L});
    public static final BitSet FOLLOW_arguments_in_primaryExpression3743 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_explicitConstructorCall_in_primaryExpression3754 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ARRAY_ELEMENT_ACCESS_in_primaryExpression3765 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_primaryExpression_in_primaryExpression3767 = new BitSet(new long[]{0x0000000000000000L, 0x4010000000000000L});
    public static final BitSet FOLLOW_expression_in_primaryExpression3769 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_literal_in_primaryExpression3780 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_newExpression_in_primaryExpression3790 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THIS_in_primaryExpression3800 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arrayTypeDeclarator_in_primaryExpression3810 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUPER_in_primaryExpression3820 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THIS_CONSTRUCTOR_CALL_in_explicitConstructorCall3844 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_genericTypeArgumentList_in_explicitConstructorCall3846 = new BitSet(new long[]{0x0000000000000000L, 0x0001000000000000L});
    public static final BitSet FOLLOW_arguments_in_explicitConstructorCall3849 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_SUPER_CONSTRUCTOR_CALL_in_explicitConstructorCall3861 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_primaryExpression_in_explicitConstructorCall3863 = new BitSet(new long[]{0x0000000000000000L, 0x0001000000000000L, 0x0000000000000200L});
    public static final BitSet FOLLOW_genericTypeArgumentList_in_explicitConstructorCall3866 = new BitSet(new long[]{0x0000000000000000L, 0x0001000000000000L});
    public static final BitSet FOLLOW_arguments_in_explicitConstructorCall3869 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_ARRAY_DECLARATOR_in_arrayTypeDeclarator3890 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_arrayTypeDeclarator_in_arrayTypeDeclarator3893 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_qualifiedIdentifier_in_arrayTypeDeclarator3897 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_primitiveType_in_arrayTypeDeclarator3901 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_STATIC_ARRAY_CREATOR_in_newExpression3925 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_primitiveType_in_newExpression3943 = new BitSet(new long[]{0x0000000000000000L, 0x4014000000000000L});
    public static final BitSet FOLLOW_newArrayConstruction_in_newExpression3945 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_genericTypeArgumentList_in_newExpression3963 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000000000800000L});
    public static final BitSet FOLLOW_qualifiedTypeIdent_in_newExpression3966 = new BitSet(new long[]{0x0000000000000000L, 0x4014000000000000L});
    public static final BitSet FOLLOW_newArrayConstruction_in_newExpression3968 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CLASS_CONSTRUCTOR_CALL_in_newExpression4003 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_genericTypeArgumentList_in_newExpression4005 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000000000800000L});
    public static final BitSet FOLLOW_qualifiedTypeIdent_in_newExpression4008 = new BitSet(new long[]{0x0000000000000000L, 0x0001000000000000L});
    public static final BitSet FOLLOW_arguments_in_newExpression4010 = new BitSet(new long[]{0x0000000000000008L, 0x0800000000000000L, 0x0000000000001401L});
    public static final BitSet FOLLOW_classTopLevelScope_in_newExpression4012 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_CLASS_CONSTRUCTOR_CALL_in_innerNewExpression4035 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_genericTypeArgumentList_in_innerNewExpression4037 = new BitSet(new long[]{0x0000000000000000L, 0x0000000000000000L, 0x0000001000000000L});
    public static final BitSet FOLLOW_IDENT_in_innerNewExpression4040 = new BitSet(new long[]{0x0000000000000000L, 0x0001000000000000L});
    public static final BitSet FOLLOW_arguments_in_innerNewExpression4042 = new BitSet(new long[]{0x0000000000000008L, 0x0800000000000000L, 0x0000000000001401L});
    public static final BitSet FOLLOW_classTopLevelScope_in_innerNewExpression4044 = new BitSet(new long[]{0x0000000000000008L});
    public static final BitSet FOLLOW_arrayDeclaratorList_in_newArrayConstruction4069 = new BitSet(new long[]{0x0000000000000000L, 0x0010000000000000L});
    public static final BitSet FOLLOW_arrayInitializer_in_newArrayConstruction4071 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_newArrayConstruction4081 = new BitSet(new long[]{0x0000000000000002L, 0x4014000000000000L});
    public static final BitSet FOLLOW_arrayDeclaratorList_in_newArrayConstruction4084 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ARGUMENT_LIST_in_arguments4105 = new BitSet(new long[]{0x0000000000000004L});
    public static final BitSet FOLLOW_expression_in_arguments4107 = new BitSet(new long[]{0x0000000000000008L, 0x4010000000000000L});
    public static final BitSet FOLLOW_set_in_literal0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_switchCaseLabel_in_synpred125_JavaTreeParser2575 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred132_JavaTreeParser2676 = new BitSet(new long[]{0x0000000000000002L, 0x4010000000000000L});
}