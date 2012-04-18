package husacct.graphics.presentation.decorators;

import org.jhotdraw.draw.Figure;

import husacct.common.dto.DependencyDTO;

public class DependenciesDecorator extends Decorator {
	private static final long serialVersionUID = 3445335414914791881L;
	private DependencyDTO[] dependencyDTOs;
	
	public DependenciesDecorator(Figure decorator, DependencyDTO[] dependencyDTOs) {
		super(decorator);
		this.dependencyDTOs = dependencyDTOs;
	}
	
	public DependencyDTO[] getDependencies() {
		return this.dependencyDTOs;
	}
}
