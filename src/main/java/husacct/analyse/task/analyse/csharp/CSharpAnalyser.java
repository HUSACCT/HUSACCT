package husacct.analyse.task.analyse.csharp;

import husacct.analyse.infrastructure.antlr.csharp.CSharpLexer;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser;
import husacct.analyse.task.analyse.AbstractAnalyser;
import husacct.analyse.task.analyse.csharp.generators.buffers.BufferService;

import java.io.IOException;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;

public class CSharpAnalyser extends AbstractAnalyser {

    private ANTLRFileStream charStream;

    @Override
    public void generateModelFromSourceFile(String sourceFilePath) {
        try {
            CSharpTreeConvertController cSharpTreeParserDelegater = new CSharpTreeConvertController(this);
            CSharpParser cSharpParser = generateCSharpParser(sourceFilePath);
            int nrOfLinesOfCode = determineNumberOfLinesOfCode();
            cSharpTreeParserDelegater.delegateDomainObjectGenerators(cSharpParser, sourceFilePath, nrOfLinesOfCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private CSharpParser generateCSharpParser(String filePath) throws IOException {
        charStream = new ANTLRFileStream(filePath);
        CSharpLexer cSharpLexer = new CSharpLexer(charStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(cSharpLexer);
        CSharpParser cSharpParser = new CSharpParser(commonTokenStream);
        return cSharpParser;
    }

    private int determineNumberOfLinesOfCode() throws IOException {
        int linesOfCode = 0;
    	if (charStream != null) {
    		int size = charStream.size();
    		int position = 0;
    		linesOfCode = 1;
    		while (position < size) {
     			String s = charStream.substring(position, position);
	        	if (s.equals("\n")) {
	        		linesOfCode ++;
	            }
    			position ++;
    		}
        }
    	return linesOfCode;
    }
    
    @Override
    public String getFileExtension() {
        return ".cs";
    }

    @Override
    public void clearLambdaBuffers() {
    	BufferService.getInstance().clear();
    }
}
