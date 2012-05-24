package husacct.analyse.task.analyser.csharp;

import husacct.analyse.infrastructure.antlr.csharp.CSharpLexer;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.analyse.task.analyser.AbstractAnalyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;

public class CSharpAnalyser extends AbstractAnalyser{

	@Override
	public String getProgrammingLanguage(){
		return "C#";
	}

	@Override
	public String getFileExtension() {
		return ".cs";
	}

	private CSharpParser generateCSharpParser(String filePath) throws IOException {
		InputStreamReader inputStream = new InputStreamReader(new FileInputStream(new File(filePath)), "UTF-8");
		String stringStream = convertAnyCharsetToUnicode(inputStream);
		CharStream charStream = new ANTLRStringStream(stringStream);
		CSharpLexer cSharpLexer = new CSharpLexer(charStream);
		CommonTokenStream commonTokenStream = new CommonTokenStream(cSharpLexer);
		CSharpParser cSharpParser = new CSharpParser(commonTokenStream);
		return cSharpParser;
	}

	private String convertAnyCharsetToUnicode(InputStreamReader inputStream) throws IOException{
		BufferedReader in = new BufferedReader(inputStream);
		String stringStream = "";
		int firstChar = in.read();
		final int ZEROWIDTHNOBREAKSPACE = 65279;
		if (firstChar != ZEROWIDTHNOBREAKSPACE) {
			stringStream = Character.toString((char)firstChar);
		}
		String singleLine = "";
		while((singleLine = in.readLine()) != null) {
			stringStream += singleLine + System.getProperty("line.separator");
		}
		byte[] bytes = stringStream.getBytes("UTF-8");
		return new String(bytes, "UTF-8");
	}

	@Override
	public void generateModelFromSource(String sourceFilePath) {
		CSharpParser cSharpParser;
		CSharpTreeConvertController cSharpTreeParserDelegater = new CSharpTreeConvertController();
		try {
			cSharpParser = generateCSharpParser(sourceFilePath);
			cSharpTreeParserDelegater.delegateDomainObjectGenerators(cSharpParser);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}		

