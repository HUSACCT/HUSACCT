package husacct.analyse.task.analyser.java;

import java.io.IOException;
import husacct.analyse.infrastructure.antlr.*;
import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.TreeAdaptor;

class JavaTreeParserBuilder{
	private CommonTokenStream commonTokenStream;
	
	public JavaParser buildTreeParser(String filePath) throws Exception {
        JavaParser javaParser = generateJavaParser(filePath);
        return javaParser;
    }

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
