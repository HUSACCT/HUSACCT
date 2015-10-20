package husacct.graphics.presentation.figures;


import husacct.common.dto.AbstractDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.presentation.decorators.ViolationsDecorator;

import java.awt.Color;

import org.apache.log4j.Logger;

public final class FigureFactory {
	protected Logger	logger				= Logger.getLogger(FigureFactory.class);
	private String		PROJECT_TYPE		= "Project";
	
	public BaseFigure createFigure(AbstractDTO dto) {
		BaseFigure createdFigure = createModuleFigure(dto);
		
		if (createdFigure == null) throw new RuntimeException("Unimplemented dto type '"
				+ (dto == null ? "DTO=null" : dto.getClass()
						.getSimpleName()) + "' passed to FigureFactory");
		return createdFigure;
	}
	
	public RelationFigure createRelationFigure(DependencyDTO[] dependencyDTOs) {
		if (dependencyDTOs.length <= 0) throw new RuntimeException("No dependencies received. Cannot create a dependency figure.");
		return new RelationFigure("Dependency from " + dependencyDTOs[0].from + " to " + dependencyDTOs[0].to, false, Integer.toString(dependencyDTOs.length));
	}
	
	public RelationFigure createRelationFigureWithViolations(DependencyDTO[] dependencyDTOs, ViolationDTO[] violationDTOs) {
		RelationFigure violatedRelationFigure = new RelationFigure("Violated dependency from " + violationDTOs[0].fromClasspath
						+ " to " + violationDTOs[0].toClasspath, true, violationDTOs.length + "/" + dependencyDTOs.length);
		violatedRelationFigure.addDecorator(createViolationsDecorator());
		return violatedRelationFigure;
	}

	private BaseFigure createModuleFigure(AbstractDTO dto) {
		String type;
		String name;
		String uniqueName;
		
		if (dto instanceof ModuleDTO) {
			type = ((ModuleDTO) dto).type;
			name = ((ModuleDTO) dto).name;
			uniqueName = ((ModuleDTO) dto).logicalPath;
		} else if (dto instanceof SoftwareUnitDTO) {
			type = ((SoftwareUnitDTO) dto).type;
			name = ((SoftwareUnitDTO) dto).name;
			uniqueName = ((SoftwareUnitDTO) dto).uniqueName;
		} else if (dto instanceof ProjectDTO) {
			type = PROJECT_TYPE;
			name = ((ProjectDTO) dto).name;
			uniqueName = name;
		} else
			return null;
		
		// TODO check these values with the define team
		if (type.toLowerCase().equals("project")) return new ProjectFigure(name, uniqueName);
		else {return new ModuleFigure(name, uniqueName, type);
		}
	}
	
	public ParentFigure createParentFigure(String parentUniqueName, String type) {
		return new ParentFigure(parentUniqueName, type);
	}
	
	public ViolationsDecorator createViolationsDecorator() {
		return new ViolationsDecorator(Color.RED);
	}
}
