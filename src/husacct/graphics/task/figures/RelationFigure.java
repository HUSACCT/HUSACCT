package husacct.graphics.task.figures;

import husacct.common.dto.DependencyDTO;

public class RelationFigure extends AbstractFigure
{
	private DependencyDTO dependencyDTO;
	
	public RelationFigure(DependencyDTO dependencyDTO)
	{
		this.dependencyDTO = dependencyDTO;
	}
	
	public DependencyDTO getDependencyDTO()
	{
		return this.dependencyDTO;
	}
}
