package husacct.control.presentation.codeviewer;

import husacct.control.presentation.codeviewer.parser.AbstractFileParser;
import husacct.control.presentation.codeviewer.parser.CSharpFileParser;
import husacct.control.presentation.codeviewer.parser.JavaFileParser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

@SuppressWarnings("serial")
public class CodeViewInternalFrame extends JFrame {
	
	private AbstractFileParser parser;
	
	private JTextPane textPane;
	private JScrollPane scrollPane;

	private StyledDocument styledDocument;
	private TextLineNumber textLineNumber;

	private ArrayList<Error> errors = new ArrayList<Error>();
	
	public CodeViewInternalFrame() {
		this.setLayout(new BorderLayout());
		this.setSize(800, 600);
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		textLineNumber = new TextLineNumber(textPane);
		scrollPane = new JScrollPane(textPane);
		scrollPane.setRowHeaderView(textLineNumber);
		this.add(scrollPane);
		
		styledDocument = textPane.getStyledDocument();
		styledDocument.putProperty(PlainDocument.tabSizeAttribute, 4);

		errors = new ArrayList<Error>();
	}
	
	public void reset() {
		this.remove(scrollPane);
		textPane = new JTextPane();
		textPane.setEditable(false);
		textLineNumber = new TextLineNumber(textPane);
		scrollPane = new JScrollPane(textPane);
		scrollPane.setRowHeaderView(textLineNumber);
		this.add(scrollPane);
		
		styledDocument = textPane.getStyledDocument();
		styledDocument.putProperty(PlainDocument.tabSizeAttribute, 4);

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
		this.setTitle(fileName.substring(fileName.lastIndexOf("\\") + 1));
		File file = new File(fileName);
		parser = getParser(fileName.substring(fileName.lastIndexOf(".") + 1));
		parser.parseFile(file);
	}
	
	public Error getError(int lineNumber) {
		for(Error error : errors) {
			if(error.line == lineNumber)
				return error;
		}
		return null;
	}
	
	public void addWord(String word, Style style, int lineNumber) {
		try {
			Error error;
			if((error = getError(lineNumber)) != null) {
				StyleContext context = new StyleContext();
				Style errorStyle = context.addStyle("default", style);
				StyleConstants.setBackground(errorStyle, error.color);
				styledDocument.insertString(styledDocument.getLength(), word + " ", errorStyle);
			} else
				styledDocument.insertString(styledDocument.getLength(), word + " ", style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void setNewLine() {
		try {
			styledDocument.insertString(styledDocument.getLength(), "\n", null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void setErrorLine(int lineNumber) {
		/*
		Element map = styledDocument.getDefaultRootElement();
	    if (lineNumber >= 0 && lineNumber < map.getElementCount()) {
	        Element element = map.getElement(lineNumber);
	        styledDocument.setParagraphAttributes(element.getStartOffset(), element.getEndOffset() - element.getStartOffset(), error, false);
	    }
	    */
	}
	
	public AbstractFileParser getParser(String extension) {
		switch(extension.toLowerCase()) {
			case "java":
				return new JavaFileParser(this);
			case "cs":
				return new CSharpFileParser(this);
			default:
				return new JavaFileParser(this);
		}
	}
	
	public int getLineOfOffset(int offset) throws BadLocationException {
	    if (offset < 0) {
	        throw new BadLocationException("Can't translate offset to line", -1);
	    } else if (offset > styledDocument.getLength()) {
	        throw new BadLocationException("Can't translate offset to line", styledDocument.getLength() + 1);
	    } else {
	        Element map = styledDocument.getDefaultRootElement();
	        return map.getElementIndex(offset);
	    }
	}

	public int getLineStartOffset(int line) throws BadLocationException {
	    Element map = styledDocument.getDefaultRootElement();
	    if (line < 0) {
	        throw new BadLocationException("Negative line", -1);
	    } else if (line >= map.getElementCount()) {
	        throw new BadLocationException("No such line", styledDocument.getLength() + 1);
	    } else {
	        Element lineElem = map.getElement(line);
	        return lineElem.getStartOffset();
	    }
	}
}
