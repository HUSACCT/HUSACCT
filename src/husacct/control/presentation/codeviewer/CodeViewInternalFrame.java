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
		// Define style
		StyleContext context = new StyleContext();
		Style keyStyle = context.addStyle("default", null);
		StyleConstants.setForeground(keyStyle, new Color(0, 0, 0));
		StyleConstants.setBold(keyStyle, false);
		// Parse file into fileTextPane
		if (fileName != null) {
			// Insert file path
			String pathText = ServiceProvider.getInstance().getLocaleService().getTranslatedString("PathLabelShort");
			String fullText = pathText + ":	" + fileName;
			try {
				fileDocument.insertString(codeDocument.getLength(), fullText, keyStyle);
				fileDocument.insertString(codeDocument.getLength(), "\n", null);
			} catch (BadLocationException e) {
				logger.warn(" Exception: " + e.getMessage());
			}
			// Insert file name
			String fileNameText = fileName.substring(fileName.lastIndexOf("\\") + 1);
			String fileNameLabel = ServiceProvider.getInstance().getLocaleService().getTranslatedString("File");
			String fullfileNameText = fileNameLabel + ":	" + fileNameText;
			StyleConstants.setBold(keyStyle, true);
			try {
				fileDocument.insertString(codeDocument.getLength(), fullfileNameText, keyStyle);
			} catch (BadLocationException e) {
				logger.warn(" Exception: " + e.getMessage());
			}
			// Parse code into codeTextPane
			File file = new File(fileName);
			String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
			parser = getParser(extension);
			parser.parseFile(file);
			// Set the Carat at the line of the first error.
			codeTextPane.setSelectionStart(firstErrorPosition);
			codeTextPane.setSelectionEnd(firstErrorPosition);
		} else {
			addWord("File not found!", keyStyle, firstErrorPosition);
			logger.error(" File name == null");
		}
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
			logger.warn(" Exception: " + e.getMessage());
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

}
