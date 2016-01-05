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

import husacct.control.presentation.codeviewer.CodeViewInternalFrame;

public class CSharpFileParser implements AbstractFileParser {

	
	private CodeViewInternalFrame view;
	
	private Style defaultStyle;
	private Style commentStyle;
	private Style keyStyle;
	
	private int lineNumber = 1;
	
	private String[] keywords = { "abstract", "as", "base", "bool", "break",
			"byte", "case", "catch", "char", "checked", "class", "const",
			"continue", "decimal", "default", "delegate", "do", "double",
			"else", "enum", "event", "explicit", "extern", "false", "finally",
			"fixed", "float", "for", "foreach", "goto", "if", "implicit", "in",
			"int", "interface", "internal", "is", "lock", "long", "namespace",
			"new", "null", "object", "operator", "out", "override", "params",
			"private", "protected", "public", "readonly", "ref", "return",
			"sbyte", "sealed", "short", "sizeof", "stackalloc", "static",
			"string", "struct", "switch", "this", "throws", "true", "try",
			"typeof", "uint", "ulong", "unchecked", "unsafe", "ushort",
			"using", "virtual", "volatile", "void", "while" };
	
	private ArrayList<String> keys = new ArrayList<String>(Arrays.asList(keywords));
	
	private boolean commentLine = false;
	private boolean commentBlock = false;
	
	public CSharpFileParser(CodeViewInternalFrame view) {
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
			e.printStackTrace();
		}
	}

	@Override
	public void parseLine(String line) {
		for(String word : line.split(" ")) {
			if(word.trim().startsWith("/*")) {
				commentBlock = true;
				view.addWord(word, commentStyle, lineNumber);
			} else if(word.trim().endsWith("*/")) {
				commentBlock = false;
				view.addWord(word, commentStyle, lineNumber);
			} else if(commentLine || commentBlock) {
				view.addWord(word, commentStyle, lineNumber);
			} else if(word.trim().startsWith("//")) {
				commentLine = true;
				view.addWord(word, commentStyle, lineNumber);
			} else if(keys.contains(word.trim())) {
				view.addWord(word, keyStyle, lineNumber);
			} else
				view.addWord(word, defaultStyle, lineNumber);
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
		commentStyle = context.addStyle("Blue", null);
		StyleConstants.setForeground(commentStyle, new Color(0, 149, 123));
		keyStyle = context.addStyle("Purple", null);
		StyleConstants.setForeground(keyStyle, new Color(85, 0, 255));
	}
}
