package husacct.graphics.presentation.figures;

import husacct.common.dto.*;
import husacct.graphics.presentation.decorators.DTODecorator;
import husacct.graphics.presentation.decorators.DependenciesDecorator;

public final class FigureFactory {
	
	public BaseFigure createFigure(DependencyDTO[] dtos) {
		RelationFigure relationFigure = this.createFigure(dtos[0]);
		DependenciesDecorator dependenciesDecorator = new DependenciesDecorator(relationFigure, dtos);
		return dependenciesDecorator;
	}

	public BaseFigure createFigure(AbstractDTO dto) {
		BaseFigure retVal = null;

		if ((dto instanceof ModuleDTO) || (dto instanceof AnalysedModuleDTO)) {
			retVal = createModuleFigure(dto);
		}
		
		if(retVal == null)
		{
			throw new RuntimeException("Unimplemented dto type '"
					+ dto.getClass().getSimpleName() + "' passed to FigureFactory");
		}

		// TODO: Use a DTODecorator to store the DTO along side with the newly
		// created Figure.
		// TODO: Determine whether it's Figure -> DTODecorator or DTODecorator
		// -> Figure.
		DTODecorator decorator = new DTODecorator(retVal, dto);
		return decorator;
	}

	private BaseFigure createModuleFigure(AbstractDTO dto) {
		String type;
		String name;

		if (dto instanceof ModuleDTO) {
			type = ((ModuleDTO) dto).type;
			name = ((ModuleDTO) dto).logicalPath;
		} else if (dto instanceof AnalysedModuleDTO) {
			type = ((AnalysedModuleDTO) dto).type;
			name = ((AnalysedModuleDTO) dto).name;
		} else {
			throw new RuntimeException("dto type '"
					+ dto.getClass().getSimpleName()
					+ "' is not recognized as a module dto");
		}

		switch (type.toLowerCase()) {
		case "layer":
			return new LayerFigure(name);

		case "class":
			return new ClassFigure(name);

		case "package":
			return new PackageFigure(name);

		default:
			throw new RuntimeException("module dto type '"+type+"' not implemented");
		}
	}

	private RelationFigure createFigure(DependencyDTO dependencyDTO) {
		return new RelationFigure("Dependency from "+dependencyDTO.from+" to "+dependencyDTO.to);
	}
}
