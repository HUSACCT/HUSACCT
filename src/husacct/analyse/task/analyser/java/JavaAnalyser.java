package husacct.analyse.task.analyser.java;


import java.io.IOException;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.misc.Interval;
import org.apache.log4j.Logger;

import husacct.analyse.infrastructure.antlr.java.Java7Lexer;
import husacct.analyse.infrastructure.antlr.java.Java7Parser;
import husacct.analyse.infrastructure.antlr.java.Java7Parser.CompilationUnitContext;
import husacct.analyse.task.analyser.AbstractAnalyser;

public class JavaAnalyser extends AbstractAnalyser {

	private CompilationUnitContext compilationUnit;
	private CharStream charStream;
    private Logger logger = Logger.getLogger(JavaAnalyser.class);

    public void generateModelFromSourceFile(String sourceFilePath) {
        try {
        	Java7Parser javaParser = generateJavaParser(sourceFilePath);
            int nrOfLinesOfCode = determineNumberOfLinesOfCode();
            CompilationUnitAnalyser javaTreeParserDelegater = new CompilationUnitAnalyser(compilationUnit, sourceFilePath, nrOfLinesOfCode, javaParser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Java7Parser generateJavaParser(String filePath) throws IOException {
        charStream = new ANTLRFileStream(filePath, "UTF-8");
        Lexer javaLexer = new Java7Lexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(javaLexer);
        Java7Parser javaParser = new Java7Parser(tokens);
		// Maximizing parser speed. Read Antlr v4 book, chapter 13.7. 
		javaParser.getInterpreter().setPredictionMode(PredictionMode.SLL); // try with simpler/faster SLL(*)
		javaParser.removeErrorListeners();
		javaParser.setErrorHandler(new BailErrorStrategy());
		try {
			compilationUnit = javaParser.compilationUnit();
			int nrOfChildren = compilationUnit.getChildCount();
    		//logger.info(" SLL " + filePath + " NrOfChildren: " + nrOfChildren);
		}
    	catch (RuntimeException ex) {
    		if (ex.getClass() == RuntimeException.class && ex.getCause() instanceof RecognitionException) {
    			// Maximizing parser speed. Read Antlr v4 book, chapter 13.7. 
    			tokens.reset(); // rewind input stream
	    		javaParser.addErrorListener(ConsoleErrorListener.INSTANCE);
	    		javaParser.setErrorHandler(new DefaultErrorStrategy());
	    		javaParser.getInterpreter().setPredictionMode(PredictionMode.LL); // try full LL(*)
	    		try {
		    		compilationUnit = javaParser.compilationUnit();
		    		logger.info(" LL " + filePath);
	    		} catch (RuntimeException e){
		    		logger.warn(e.getMessage() + " - in file: " + filePath);
	    		}
			} else {
	    		logger.warn(ex.getMessage() + " - in file: " + filePath);
	    		//e.printStackTrace();
			}
    	}
        return javaParser;
    }

    private int determineNumberOfLinesOfCode() throws IOException {
        int linesOfCode = 0;
    	if (charStream != null) {
    		int size = charStream.size();
    		int position = 0;
    		linesOfCode = 1;
    		while (position < size) {
     			String s = charStream.getText(new Interval(position, position));
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
