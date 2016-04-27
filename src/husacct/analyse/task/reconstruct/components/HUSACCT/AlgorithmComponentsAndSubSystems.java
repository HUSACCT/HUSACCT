package husacct.analyse.task.reconstruct.components.HUSACCT;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.reconstruct.IAlgorithm;

public abstract class AlgorithmComponentsAndSubSystems extends IAlgorithm{

	protected AlgorithmComponentsAndSubSystems(IModelQueryService queryService) {
		super(queryService);
	}
	

}
