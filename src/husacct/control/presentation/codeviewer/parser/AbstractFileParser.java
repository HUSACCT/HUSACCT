package husacct.control.presentation.codeviewer.parser;

import java.io.File;

public interface AbstractFileParser {
	void initialiseStyles();
	public void parseFile(File file);
	public void parseLine(String line);
}
