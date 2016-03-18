package husacct.control.presentation.codeviewer.parser;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.apache.log4j.Logger;

import husacct.control.presentation.codeviewer.CodeViewInternalFrame;

public class JavaFileParser implements AbstractFileParser {

	private static Logger logger = Logger.getLogger(JavaFileParser.class);
	private CodeViewInternalFrame view;
	private int lineNumber = 1;
	
	// Style variables
	private Style defaultStyle;
	private Style commentStyle;
	private Style keyStyle;
	
	// Variables needed to assign styles to text.
	private boolean commentLine = false; // True if the text is part of a comment
	private boolean commentBlock = false;
	private String[] keywords = { "abstract", "boolean", "break", "byte",
			"case", "catch", "char", "class", "continue", "default", "do",
			"double", "else", "extends", "false", "final", "finally", "float",
			"for", "if", "implements", "import", "instanceof", "int",
			"interface", "long", "native", "new", "null", "package", "private",
			"protected", "public", "return", "short", "static", "super",
			"switch", "synchronized", "this", "throw", "throws", "transient",
			"true", "try", "void", "volatile", "while" };
	private ArrayList<String> keys = new ArrayList<String>(Arrays.asList(keywords));
	
	public JavaFileParser(CodeViewInternalFrame view) {
		this.view = view;
		initialiseStyles();
	}
	
	@Override
	public void parseFile(File file) {
		FileReader fileReader;
		try {
			fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while((line = bufferedReader.readLine()) != null) {
				parseLine(line);
			}
			bufferedReader.close();
		} catch (IOException e) {
			view.addWord("File not found!", defaultStyle, lineNumber);
			logger.error(" Exception: " + e.getMessage());
		}
	}
	
	/* Determines the types of text.
	 * Assigns styles to different types of text. 
	 * */
	@Override
	public void parseLine(String line) {
		for(String word : line.split(" ")) {
			if(word.trim().startsWith("/*")) {
				commentBlock = true;
				view.addWord(word, commentStyle, lineNumber);
			} else if(word.trim().endsWith("*/")) {
				commentBlock = false;
				view.addWord(word, commentStyle, lineNumber);
			} else if(word.trim().startsWith("//")) {
				commentLine = true;
				view.addWord(word, commentStyle, lineNumber);
			} else if(commentLine || commentBlock) {
				view.addWord(word, commentStyle, lineNumber);
			} else if(keys.contains(word.trim())) {
				view.addWord(word, keyStyle, lineNumber);
			} else {
				view.addWord(word, defaultStyle, lineNumber);
			}
		}
		commentLine = false;
		view.setNewLine();
		lineNumber++;
	}
	@Override
	public void initialiseStyles() {
		StyleContext context = new StyleContext();
		defaultStyle = context.addStyle("default", null);
		StyleConstants.setForeground(defaultStyle, new Color(0, 0, 0));
		commentStyle = context.addStyle("Green", null);
		StyleConstants.setForeground(commentStyle, new Color(63, 127, 95));
		keyStyle = context.addStyle("Purple", null);
		StyleConstants.setForeground(keyStyle, new Color(127, 0, 85));
		StyleConstants.setBold(keyStyle, true);
	}
}
