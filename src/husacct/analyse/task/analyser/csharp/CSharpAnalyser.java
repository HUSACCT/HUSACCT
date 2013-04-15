package husacct.analyse.task.analyser.csharp;

import antlr.Token;
import husacct.analyse.infrastructure.antlr.csharp.CSharpLexer;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.analyse.task.analyser.AbstractAnalyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.antlr.runtime.ANTLRFileStream;

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
		ANTLRFileStream stream = new ANTLRFileStream(filePath);
		CSharpLexer cSharpLexer = new CSharpLexer(stream);
		CommonTokenStream commonTokenStream = new CommonTokenStream(cSharpLexer);
		CSharpParser cSharpParser = new CSharpParser(commonTokenStream);
               	return cSharpParser;
	}

	@Override
	public void generateModelFromSource(String sourceFilePath) {
		try {
			CSharpTreeConvertController cSharpTreeParserDelegater = new CSharpTreeConvertController();
			CSharpParser cSharpParser = generateCSharpParser(sourceFilePath);
			cSharpTreeParserDelegater.delegateDomainObjectGenerators(cSharpParser);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}

