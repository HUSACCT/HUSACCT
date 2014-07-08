package husacct.analyse.task.analyser.csharp.generators;

import husacct.analyse.domain.IModelCreationService;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;

abstract class CSharpGenerator {

    protected IModelCreationService modelService = new FamixCreationServiceImpl();
}