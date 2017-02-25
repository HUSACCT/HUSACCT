package husacct.analyse.task.analyse.java;


import java.io.IOException;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.log4j.Logger;

import husacct.analyse.task.analyse.AbstractAnalyser;
import husacct.analyse.task.analyse.ParserExceptionHandler;
import husacct.analyse.task.analyse.java.analysing.CompilationUnitAnalyser;
import husacct.analyse.task.analyse.java.parsing.JavaLexer;
import husacct.analyse.task.analyse.java.parsing.JavaParser;
import husacct.analyse.task.analyse.java.parsing.JavaParser.CompilationUnitContext;

public class JavaAnalyser extends AbstractAnalyser {

	private CompilationUnitContext compilationUnit;
	private CharStream charStream;
    private Logger logger = Logger.getLogger(JavaAnalyser.class);

    @Override
    public void generateModelFromSourceFile(String sourceFilePath) {
        try {
            compilationUnit = null;
            charStream = null;
        	JavaParser javaParser = generateJavaParser(sourceFilePath);
            int nrOfLinesOfCode = determineNumberOfLinesOfCode();
            new CompilationUnitAnalyser(compilationUnit, sourceFilePath, nrOfLinesOfCode, javaParser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JavaParser generateJavaParser(String filePath) throws IOException {
        charStream = new ANTLRFileStream(filePath, "UTF-8");
        Lexer javaLexer = new JavaLexer(charStream);
		javaLexer.removeErrorListeners(); // Prevents messages in console
        CommonTokenStream tokens = new CommonTokenStream(javaLexer);
        JavaParser javaParser = new JavaParser(tokens);
		// Maximizing parser speed. Read Antlr v4 book, chapter 13.7. 
		javaParser.getInterpreter().setPredictionMode(PredictionMode.SLL); // try with simpler/faster SLL(*)
		javaParser.removeErrorListeners();
		javaParser.setErrorHandler(new BailErrorStrategy());
		javaParser.addErrorListener(new ParserExceptionHandler(this));
		try {
    		// logger.info(" SLL " + filePath);
			compilationUnit = javaParser.compilationUnit();
		}
    	catch (ParseCancellationException ex) { // Thrown by BailErrorStrategy
			// Maximizing parser speed. Read Antlr v4 book, chapter 13.7. 
			tokens.reset(); // rewind input stream
			javaParser.reset(); // Does not reset the error handler and listener
    		javaParser.getInterpreter().setPredictionMode(PredictionMode.LL); // try full LL(*)
    		try {
	    		logger.info(" LL " + filePath);
	    		compilationUnit = javaParser.compilationUnit();
    		} catch (RuntimeException e){
	    		logger.warn(e.getMessage() + " - in file: " + filePath);
    		}
    	}
		catch (RuntimeException ex) {
    		logger.warn(ex.getCause().toString() + " - in file: " + filePath);
    		//ex.printStackTrace();
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
    public String getFileExtension() {
        return ".java";
    }
}
