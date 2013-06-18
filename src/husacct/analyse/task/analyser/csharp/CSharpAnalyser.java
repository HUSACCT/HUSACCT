package husacct.analyse.task.analyser.csharp;

import husacct.analyse.infrastructure.antlr.csharp.CSharpLexer;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.analyse.task.analyser.AbstractAnalyser;
import husacct.analyse.task.analyser.csharp.generators.buffers.BufferService;

import java.io.IOException;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;

public class CSharpAnalyser extends AbstractAnalyser {

    @Override
    public String getProgrammingLanguage() {
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
    
    @Override
    public void clearLambdaBuffers() {
    	BufferService.getInstance().clear();
    }
}
