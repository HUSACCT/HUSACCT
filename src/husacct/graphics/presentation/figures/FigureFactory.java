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
	
	public BaseFigure createFigure(AbstractDTO dto, ViolationDTO[] violationDTOs) {
		BaseFigure createdFigure = createFigure(dto);
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

	public RelationFigure createFigure(ViolationDTO[] violationDTOs) {
		if (violationDTOs.length == 0) throw new RuntimeException(
				"No violations received. Cannot create a violation figure.");
		
		RelationFigure violatedRelationFigure = new RelationFigure("Violated dependency from " + violationDTOs[0].fromClasspath
						+ " to " + violationDTOs[0].toClasspath, true, Integer.toString(violationDTOs.length));
		violatedRelationFigure.addDecorator(createViolationsDecorator());
		return violatedRelationFigure;
	}
	
	private BaseFigure createModuleFigure(AbstractDTO dto) {
		String type;
		String name;
		
		if (dto instanceof ModuleDTO) {
			type = ((ModuleDTO) dto).type;
			name = ((ModuleDTO) dto).logicalPath;
		} else if (dto instanceof SoftwareUnitDTO) {
			type = ((SoftwareUnitDTO) dto).type;
			name = ((SoftwareUnitDTO) dto).name;
		} else if (dto instanceof ProjectDTO) {
			type = PROJECT_TYPE;
			name = ((ProjectDTO) dto).name;
		} else
			return null;
		
		// TODO check these values with the define team
		if (type.toLowerCase().equals("project")) return new ProjectFigure(name);
		//else if (type.toLowerCase().equals("layer")) return new LayerFigure(name);
		//else if (type.toLowerCase().equals("component")) return new ComponentFigure(name);
		else if (type.toLowerCase().equals("class")) return new ClassFigure(name);
		else if (type.toLowerCase().equals("abstract")) return new AbstractClassFigure(name);
		else if (type.toLowerCase().equals("interface")) return new InterfaceFigure(name);
		else if (type.toLowerCase().equals("package")) return new PackageFigure(name);
		//else if (type.toLowerCase().equals("subsystem")) return new SubsystemFigure(name);
		else if (type.toLowerCase().equals("library")) return new ModuleFigure(name, type);
		else {			
			//logger.info("Type " + type.toLowerCase() + " is not specifically supported yet; a ModuleFigure is created instead.");
			return new ModuleFigure(name, type);
		}
	}
	
	public ParentFigure createParentFigure(String parentName) {
		return new ParentFigure(parentName);
	}
	
	public ViolationsDecorator createViolationsDecorator() {
		return new ViolationsDecorator(Color.RED);
	}
}
