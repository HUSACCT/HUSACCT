package husacct.analyse.task.analyser.csharp;

import husacct.analyse.infrastructure.antlr.csharp.CSharpLexer;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.analyse.task.analyser.AbstractAnalyser;

import java.io.IOException;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;

public class CSharpAnalyser extends AbstractAnalyser{
	
	private CommonTokenStream commonTokenStream;
	@Override
	public String getProgrammingLanguage(){
		return "C#";
	}
	
	@Override
	public String getFileExtension() {
		return ".cs";
	}

	
	private CSharpParser generateJavaParser(String filePath) throws IOException {
		System.out.println(filePath);
    	CharStream charStream = new ANTLRFileStream(filePath,"UTF-8");
        Lexer cSharpLexer = new CSharpLexer(charStream);
        commonTokenStream = new CommonTokenStream(cSharpLexer);
        CSharpParser cSharpParser = new CSharpParser(commonTokenStream);
        return cSharpParser;
	}

	@Override
	public void generateModelFromSource(String sourceFilePath) {
		CSharpParser cSharpParser;
		CSharpTreeConvertController cSharpTreeParserDelegater = new CSharpTreeConvertController();
		try {
			cSharpParser = generateJavaParser(sourceFilePath);
			cSharpTreeParserDelegater.delegateDomainObjectGenerators(cSharpParser);
		} catch (Exception e) {
			
		}		
	}
	
}		

