package husacct.control.presentation.codeviewer;

import husacct.ServiceProvider;
import husacct.common.help.presentation.HelpableJInternalFrame;
import husacct.control.presentation.codeviewer.parser.AbstractFileParser;
import husacct.control.presentation.codeviewer.parser.CSharpFileParser;
import husacct.control.presentation.codeviewer.parser.JavaFileParser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.text.Position;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import org.apache.log4j.Logger;


@SuppressWarnings("serial")
public class CodeViewInternalFrame extends HelpableJInternalFrame  {
	
	private static Logger logger = Logger.getLogger(CodeViewInternalFrame.class);
	private AbstractFileParser parser;
	
	private JTextPane fileTextPane, codeTextPane;
	private JScrollPane fileScrollPane, codeScrollPane;

	private StyledDocument fileDocument, codeDocument;
	private TextLineNumber codeLineNumber;

	private ArrayList<Error> errors = new ArrayList<Error>();
	private int firstErrorPosition = 0;
	
	public CodeViewInternalFrame() {
		this.setLayout(new BorderLayout(0,5));
		initComponents();
	}
	
	public void reset() {
		this.remove(fileScrollPane);
		this.remove(codeScrollPane);
		initComponents();
	}
	
	private void initComponents() {
		fileTextPane = new JTextPane();
		fileTextPane.setEditable(false);
		fileScrollPane = new JScrollPane(fileTextPane);
        fileDocument = fileTextPane.getStyledDocument();
        this.add(fileScrollPane, BorderLayout.PAGE_START);

		codeTextPane = new JTextPane();
		codeTextPane.setEditable(false);
		codeLineNumber = new TextLineNumber(codeTextPane);
		codeScrollPane = new JScrollPane(codeTextPane);
		codeScrollPane.setRowHeaderView(codeLineNumber);
		codeDocument = codeTextPane.getStyledDocument();
		codeDocument.putProperty(PlainDocument.tabSizeAttribute, 4);
		this.add(codeScrollPane, BorderLayout.CENTER);

		errors = new ArrayList<Error>();
	}
	
	public void setErrorLines(ArrayList<Integer> errorLines) {
		for(int lineNumber : errorLines) {
			Error error = new Error(lineNumber, new Color(255, 0, 25));
			errors.add(error);
		}
	}
	
	public void setErrors(ArrayList<Error> errors) {
		this.errors = errors;
	}
	
	public void parseFile(String fileName) {
		firstErrorPosition = 0;
		// Parse file into fileTextPane
		try {
			// Define style
			StyleContext context = new StyleContext();
			Style keyStyle = context.addStyle("default", null);
			StyleConstants.setForeground(keyStyle, new Color(0, 0, 0));
			StyleConstants.setBold(keyStyle, false);
			// Insert file path
			String pathText = ServiceProvider.getInstance().getLocaleService().getTranslatedString("PathLabelShort");
			String fullText = pathText + ":	" + fileName;
			fileDocument.insertString(codeDocument.getLength(), fullText, keyStyle);
			fileDocument.insertString(codeDocument.getLength(), "\n", null);
			// Insert file name
			String fileNameText = fileName.substring(fileName.lastIndexOf("\\") + 1);
			String fileNameLabel = ServiceProvider.getInstance().getLocaleService().getTranslatedString("File");
			String fullfileNameText = fileNameLabel + ":	" + fileNameText;
			StyleConstants.setBold(keyStyle, true);
			fileDocument.insertString(codeDocument.getLength(), fullfileNameText, keyStyle);
		} catch (BadLocationException e) {
			logger.warn(" Exception: " + e.getMessage());
		}
		// Parse code into codeTextPane
		File file = new File(fileName);
		parser = getParser(fileName.substring(fileName.lastIndexOf(".") + 1));
		parser.parseFile(file);
		// Set the Carat at the line of the first error.
		codeTextPane.setSelectionStart(firstErrorPosition);
		codeTextPane.setSelectionEnd(firstErrorPosition);
	}
	
	public void addWord(String word, Style style, int lineNumber) {
		try {
			Error error;
			if((error = getError(lineNumber)) != null) {
				StyleContext context = new StyleContext();
				Style errorStyle = context.addStyle("default", style);
				StyleConstants.setBackground(errorStyle, error.color);
				codeDocument.insertString(codeDocument.getLength(), word + " ", errorStyle);
				if (firstErrorPosition == 0) {
					firstErrorPosition = codeDocument.getLength();
				}
			} else
				codeDocument.insertString(codeDocument.getLength(), word + " ", style);
		} catch (BadLocationException e) {
			logger.warn(" Exception: " + e.getMessage());
		}
	}
	
	public Error getError(int lineNumber) {
		for(Error error : errors) {
			if(error.line == lineNumber)
				return error;
		}
		return null;
	}
	
	public void setNewLine() {
		try {
			codeDocument.insertString(codeDocument.getLength(), "\n", null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	private AbstractFileParser getParser(String extension) {
		switch(extension.toLowerCase()) {
			case "java":
				return new JavaFileParser(this);
			case "cs":
				return new CSharpFileParser(this);
			default:
				return new JavaFileParser(this);
		}
	}
	
	// Old code (version 2.0) that caused problems
	/*
	public void setErrorLine(int lineNumber) {
		Element map = codeDocument.getDefaultRootElement();
	    if (lineNumber >= 0 && lineNumber < map.getElementCount()) {
	        Element element = map.getElement(lineNumber);
	        codeDocument.setParagraphAttributes(element.getStartOffset(), element.getEndOffset() - element.getStartOffset(), error, false);
	    }
	}
	
	public int getLineOfOffset(int offset) throws BadLocationException {
	    if (offset < 0) {
	        throw new BadLocationException("Can't translate offset to line", -1);
	    } else if (offset > codeDocument.getLength()) {
	        throw new BadLocationException("Can't translate offset to line", codeDocument.getLength() + 1);
	    } else {
	        Element map = codeDocument.getDefaultRootElement();
	        return map.getElementIndex(offset);
	    }
	}

	public int getLineStartOffset(int line) throws BadLocationException {
	    Element map = codeDocument.getDefaultRootElement();
	    if (line < 0) {
	        throw new BadLocationException("Negative line", -1);
	    } else if (line >= map.getElementCount()) {
	        throw new BadLocationException("No such line", codeDocument.getLength() + 1);
	    } else {
	        Element lineElem = map.getElement(line);
	        return lineElem.getStartOffset();
	    }
	}
	*/
}
