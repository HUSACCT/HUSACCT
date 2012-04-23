package husacct.analyse.task.analyser.java;

import husacct.analyse.domain.ModelService;
import husacct.analyse.domain.famix.FamixModelServiceImpl;

abstract class JavaGenerator {
	
	protected ModelService modelService = new FamixModelServiceImpl();

}
