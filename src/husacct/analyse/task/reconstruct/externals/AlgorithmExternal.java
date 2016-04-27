package husacct.analyse.task.reconstruct.externals;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.reconstruct.IAlgorithm;

public abstract class AlgorithmExternal extends IAlgorithm{

	protected AlgorithmExternal(IModelQueryService queryService) {
		super(queryService);
	}
	
}
