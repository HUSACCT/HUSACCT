package husacct.graphics.presentation.figures;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.presentation.decorators.DependenciesDecorator;
import husacct.graphics.presentation.decorators.ViolationsDecorator;

public final class FigureFactory {

	public BaseFigure createFigure(DependencyDTO[] dtos) {
		if(dtos.length>0){
			RelationFigure relationFigure = this.createFigure(dtos[0]);
			DependenciesDecorator dependenciesDecorator = new DependenciesDecorator(relationFigure, dtos);
			return dependenciesDecorator;
		}
		else{
			throw new RuntimeException("No dependencies received. Cannot create a dependency figure.");
		}
	}

	private RelationFigure createFigure(DependencyDTO dependencyDTO) {
		return new RelationFigure("Dependency from " + dependencyDTO.from + " to " + dependencyDTO.to);
	}

	public BaseFigure createFigure(ViolationDTO[] violationDTOs) {
		RelationFigure relationFigure = this.createFigure(violationDTOs[0]);
		ViolationsDecorator violationsDecorator = new ViolationsDecorator(relationFigure, violationDTOs);
		return violationsDecorator;
	}

	private RelationFigure createFigure(ViolationDTO violationDTO) {
		return new RelationFigure("Violated dependency from " + violationDTO.getFromClasspath() + " to "
				+ violationDTO.getToClasspath());
	}

	public BaseFigure createFigure(AbstractDTO dto) {
		BaseFigure createdFigure = null;

		if ((dto instanceof ModuleDTO) || (dto instanceof AnalysedModuleDTO)) {
			createdFigure = createModuleFigure(dto);
		}

		if (null==createdFigure) {
			throw new RuntimeException("Unimplemented dto type '" + dto.getClass().getSimpleName()
					+ "' passed to FigureFactory");
		}

		return createdFigure;
	}

	public BaseFigure createFigure(AbstractDTO dto, ViolationDTO[] violationDTOs) {
		BaseFigure createdFigure = this.createFigure(dto);

		if (violationDTOs.length > 0) {
			createdFigure = new ViolationsDecorator(createdFigure, violationDTOs);
		}

		return createdFigure;
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
			throw new RuntimeException("dto type '" + dto.getClass().getSimpleName()
					+ "' is not recognized as a module dto");
		}

		if(type.toLowerCase().equals("layer")) {
			return new LayerFigure(name);
		}else if(type.toLowerCase().equals("class")) {
			return new ClassFigure(name);
		}else if(type.toLowerCase().equals("abstract")) {				
			//TODO Abstract class
			return new ClassFigure(name);
		}else if(type.toLowerCase().equals("interface")) {	
			//TODO Interface obj
			return new ClassFigure(name);
		}else if(type.toLowerCase().equals("package")) {
			return new PackageFigure(name);
		}else{
			throw new RuntimeException("module dto type '" + type + "' not implemented");
		}
	}
}
