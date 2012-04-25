package husacct.graphics.presentation.decorators;

import husacct.common.dto.DependencyDTO;
import husacct.graphics.presentation.figures.BaseFigure;

public class DependenciesDecorator extends Decorator {
	private static final long serialVersionUID = 3445335414914791881L;
	private DependencyDTO[] dependencyDTOs;
	
	public DependenciesDecorator(BaseFigure decorator, DependencyDTO[] dependencyDTOs) {
		super(decorator);
		this.dependencyDTOs = dependencyDTOs;
	}
	
	public DependencyDTO[] getDependencies() {
		return this.dependencyDTOs;
	}
}
