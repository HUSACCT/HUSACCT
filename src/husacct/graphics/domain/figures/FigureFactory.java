package husacct.graphics.domain.figures;


import husacct.analyse.serviceinterface.dto.DependencyDTO;
import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.domain.decorators.ViolationsDecorator;

import java.awt.Color;

import org.apache.log4j.Logger;

public final class FigureFactory {
	protected Logger	logger				= Logger.getLogger(FigureFactory.class);
	private final String PROJECT_TYPE		= "Project";
	
	public RelationFigure createRelationFigure_Dependency(DependencyDTO[] dependencyDTOs) {
		if (dependencyDTOs.length <= 0) throw new RuntimeException("No dependencies received. Cannot create a dependency figure.");
		return new RelationFigure("Dependency from " + dependencyDTOs[0].from + " to " + dependencyDTOs[0].to, RelationType.DEPENDENCY, Integer.toString(dependencyDTOs.length));
	}
	
	public RelationFigure createRelationFigure_DependencyWithViolations(DependencyDTO[] dependencyDTOs, ViolationDTO[] violationDTOs) {
		RelationFigure violatedRelationFigure = new RelationFigure("Violated dependency from " + violationDTOs[0].fromClasspath
						+ " to " + violationDTOs[0].toClasspath, RelationType.VIOLATION, violationDTOs.length + "/" + dependencyDTOs.length);
		violatedRelationFigure.addDecorator(createViolationsDecorator());
		return violatedRelationFigure;
	}

	// May return null!
	public ModuleFigure createModuleFigure(AbstractDTO dto) {
		String type;
		String name;
		String uniqueName;
		if (dto != null) {
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
			} else {
				logger.error("Unimplemented dto type!");
				return null;
			}
		} else {
			logger.error("Null value for DTO!");
			return null;
		}
		return new ModuleFigure(name, uniqueName, type);
	}
	
	public ParentFigure createParentFigure(String parentUniqueName, String type) {
		return new ParentFigure(parentUniqueName, type);
	}
	
	public ViolationsDecorator createViolationsDecorator() {
		return new ViolationsDecorator(Color.RED);
	}
}
