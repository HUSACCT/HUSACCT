package husacct.analyse.task.analyser.csharp;

import husacct.analyse.domain.ModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;

public abstract class CSharpGenerator {
	protected final int USING = 18;
	protected final int SEMICOLON = 25;
	protected final int BACKWARDBRACKET = 26;
	protected final int FINAL = 45;
	protected final int NAMESPACE = 61;
	protected final int FORWARDCURLYBRACKET = 62;
	protected final int BACKWARDCURLYBRACKET = 63;
	protected final int NEW	= 68;
	protected final int PUBLIC = 69;
	protected final int PROTECTED = 70;
	protected final int PRIVATE = 72;
	protected final int ABSTRACT = 74;
	protected final int VOID = 82;
	protected final int FORWARDBRACKET = 88;
	protected final int CLASS = 155;
	protected final int INTERFACE = 172;
	protected final int IDENTIFIER = 4;
	protected final int STRUCT = 169;
	protected ModelCreationService modelService = new FamixCreationServiceImpl();

	
}
