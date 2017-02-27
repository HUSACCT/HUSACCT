package husacct.analyse.task.reconstruct.algorithms.hu.externals;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.reconstruct.algorithms.Algorithm_SuperClass;

public abstract class AlgorithmExternal extends Algorithm_SuperClass{

	protected AlgorithmExternal(IModelQueryService queryService) {
		super(queryService);
	}
	
}
