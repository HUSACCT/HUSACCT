package husacct.analyse.task.analyser.java;

import java.io.IOException;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;

import husacct.analyse.infrastructure.antlr.java.JavaLexer;
import husacct.analyse.infrastructure.antlr.java.JavaParser;
import husacct.analyse.task.analyser.AbstractAnalyser;

public class JavaAnalyser extends AbstractAnalyser {

    private CharStream charStream;

    public void generateModelFromSource(String sourceFilePath) {
        JavaTreeConvertController javaTreeParserDelegater = new JavaTreeConvertController();
        JavaParser javaParser;
        try {
            javaParser = generateJavaParser(sourceFilePath);
            int nrOfLinesOfCode = determineNumberOfLinesOfCode();
            javaTreeParserDelegater.delegateASTToGenerators(sourceFilePath, nrOfLinesOfCode, javaParser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JavaParser generateJavaParser(String filePath) throws IOException {
        charStream = new ANTLRFileStream(filePath, "UTF-8");
        Lexer javaLexer = new JavaLexer(charStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(javaLexer);
        JavaParser javaParser = new JavaParser(commonTokenStream);
        return javaParser;
    }

    private int determineNumberOfLinesOfCode() throws IOException {
        int linesOfCode = 0;
    	if (charStream != null) {
    		int size = charStream.size();
    		int position = 0;
    		linesOfCode = 1;
    		while (position < size) {
     			String s = charStream.substring(position, position);
	        	if (s.equals("\n")) {
	        		linesOfCode ++;
	            }
    			position ++;
    		}
        }
    	return linesOfCode;
    }
    
    @Override
    public String getProgrammingLanguage() {
        return "Java";
    }

    @Override
    public String getFileExtension() {
        return ".java";
    }
}
