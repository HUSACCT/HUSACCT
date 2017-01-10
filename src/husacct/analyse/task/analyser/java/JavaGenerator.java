package husacct.analyse.task.analyser.java;

import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.analyse.infrastructure.antlr.java.Java7BaseListener;

abstract class JavaGenerator extends Java7BaseListener{

    protected IModelCreationService modelService = new FamixCreationServiceImpl();
}
