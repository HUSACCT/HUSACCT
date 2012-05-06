package husacct.analyse.task.analyser.java;

import husacct.analyse.domain.ModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;

abstract class JavaGenerator {
	
	protected ModelCreationService modelService = new FamixCreationServiceImpl();

}
