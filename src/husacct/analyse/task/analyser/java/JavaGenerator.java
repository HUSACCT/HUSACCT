package husacct.analyse.task.analyser.java;

import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;

abstract class JavaGenerator {

    protected IModelCreationService modelService = new FamixCreationServiceImpl();
}
