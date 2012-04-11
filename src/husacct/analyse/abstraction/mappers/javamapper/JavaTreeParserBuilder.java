package husacct.analyse.abstraction.mappers.javamapper;

import java.io.IOException;

import husacct.analyse.infrastructure.antlr.*;
import husacct.analyse.infrastructure.antlr.JavaParser.javaSource_return;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.runtime.tree.TreeAdaptor;

public class JavaTreeParserBuilder {
	private CommonTokenStream commonTokenStream;
	
	public JavaParser buildTreeParser(String filePath) throws Exception {
        JavaParser javaParser = generateJavaParser(filePath);
        //javaSource_return javaSource = javaParser.javaSource();
        //CommonTree commonTree = (CommonTree)javaSource.getTree();
        //JavaTreeParser javaTreeParser = generateJavaTreeParser(commonTree);
        return javaParser;
    }

//    private JavaTreeParser generateJavaTreeParser(CommonTree commonTree) throws RecognitionException {
//    	CommonTreeNodeStream commonTreeNodeStream = new CommonTreeNodeStream(commonTree);
//        commonTreeNodeStream.setTokenStream(commonTokenStream);
//        JavaTreeParser javaTreeParser = new JavaTreeParser(commonTreeNodeStream);
//        javaTreeParser.javaSource();
//        return javaTreeParser;
//	}

	private JavaParser generateJavaParser(String filePath) throws IOException {
    	CharStream charStream = new ANTLRFileStream(filePath,"UTF-8");
        Lexer javaLexer = new JavaLexer(charStream);
        commonTokenStream = new CommonTokenStream(javaLexer);
        JavaParser javaParser = new JavaParser(commonTokenStream);
        return javaParser;
	}

	static final TreeAdaptor adaptor = new CommonTreeAdaptor() {
        public Object create(Token payload) {
            return new CommonTree(payload);
        }
    };
}
