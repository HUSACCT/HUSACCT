package husacct.analyse.task.analyser.csharp;

import husacct.analyse.domain.ModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;

public abstract class CSharpGenerator {

	protected final int ABSTRACT = 74;
	protected final int CLASS = 155;
	protected final int FORWARDCURLYBRACKET = 62;
	protected final int BACKWARDCURLYBRACKET = 63;
	protected final int USING = 18;
	protected final int NAMESPACE = 61;
	protected final int SEMICOLON = 25;
	protected ModelCreationService modelService = new FamixCreationServiceImpl();

}
