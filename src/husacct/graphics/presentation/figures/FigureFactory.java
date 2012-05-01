package husacct.graphics.presentation.figures;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.presentation.decorators.ViolationsDecorator;

public final class FigureFactory {

	public RelationFigure createFigure(DependencyDTO[] dependencyDTOs) {
		if(dependencyDTOs.length <= 0){
			throw new RuntimeException("No dependencies received. Cannot create a dependency figure.");
		}
		
		return new RelationFigure("Dependency from " + dependencyDTOs[0].from 
				+ " to " + dependencyDTOs[0].to, false, dependencyDTOs.length);
	}

	public RelationFigure createFigure(ViolationDTO[] violationDTOs) {
		if(violationDTOs.length <= 0) {
			throw new RuntimeException("No violations received. Cannot create a violation figure.");
		}
		
		RelationFigure violatedRelationFigure = new RelationFigure("Violated dependency from " 
				+ violationDTOs[0].getFromClasspath() + " to " + violationDTOs[0].getToClasspath(), 
				true, violationDTOs.length);
		
		return violatedRelationFigure;
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
