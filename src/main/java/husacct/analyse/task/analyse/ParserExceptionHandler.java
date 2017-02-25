// HUSACCT-specific exception handler

package husacct.analyse.task.analyse;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

public class ParserExceptionHandler extends BaseErrorListener {
	private AbstractAnalyser analyser;
    @SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(ParserExceptionHandler.class);
	private Object offendingSymbol;
	private int line;
	private int charPositionInLine;
	private String msg;
	private RecognitionException recExc;
	private List<String> ruleStack;
	
	public ParserExceptionHandler(AbstractAnalyser analyser){
		this.analyser = analyser;
	}
	
	@Override
    public void syntaxError(Recognizer<?, ?> recognizer,Object offendingSymbol,int line,
    		 int charPositionInLine, String msg, RecognitionException e)
    {
        this.offendingSymbol = offendingSymbol;
        this.line = line;
        this.charPositionInLine = charPositionInLine;
    	this.msg = msg;
    	this.recExc = e;
    	this.ruleStack = ((Parser)recognizer).getRuleInvocationStack();
        Collections.reverse(ruleStack);
        //logger.warn(" Syntax Error in file: " + analyser.getSourceFilePath() + " Rule stack: "+ruleStack);
        //logger.warn("Line "+line+":"+charPositionInLine+" At " + offendingSymbol + ": " + msg);
        analyser.raiseNumberOfSyntaxErrors();
        analyser.addFileToFilesWithSyntaxErrorsStack();
    }

	public Object getOffendingSymbol() {
		return offendingSymbol;
	}

	public int getLine() {
		return line;
	}

	public int getCharPositionInLine() {
		return charPositionInLine;
	}

	public String getMsg() {
		return msg;
	}

	public RecognitionException getRecExc() {
		return recExc;
	}

	public List<String> getStack() {
		return ruleStack;
	}
}



