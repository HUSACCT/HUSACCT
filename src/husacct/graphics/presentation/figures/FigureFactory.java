package husacct.graphics.presentation.figures;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.presentation.decorators.ViolationsDecorator;

import java.awt.Color;

import org.apache.log4j.Logger;

public final class FigureFactory {
	protected Logger logger = Logger.getLogger(FigureFactory.class);

	public RelationFigure createFigure(DependencyDTO[] dependencyDTOs) {
		if (dependencyDTOs.length <= 0) {
			throw new RuntimeException("No dependencies received. Cannot create a dependency figure.");
		}
		return new RelationFigure("Dependency from " + dependencyDTOs[0].from + " to " + dependencyDTOs[0].to, false, dependencyDTOs.length);
	}

	public ViolationsDecorator createViolationsDecorator(ViolationDTO[] violationDTOs) {
		if (violationDTOs.length <= 0) {
			throw new RuntimeException("No violations received. Cannot create a violation figure.");
		}

		int highestSeverity = -1;
		Color highestColor = null;
		for (ViolationDTO dto : violationDTOs) {
			if (dto.severityValue > highestSeverity) {
				highestSeverity = dto.severityValue;
				highestColor = dto.severityColor;
			}
		}

		if (null == highestColor) {
			logger.warn("No violation severity color found! Resetting to the default 'Color.RED'.");
			highestColor = Color.RED;
		}
		return new ViolationsDecorator(highestColor);
	}

	// FIXME: Patrick: We've decided to NOT store DTOs inside decorators.
	// The code below shows that DTOs are stored inside the decorator. Please
	// fix the code and remove this comment.
	public RelationFigure createFigure(ViolationDTO[] violationDTOs) {
		if (violationDTOs.length == 0) {
			throw new RuntimeException("No violations received. Cannot create a violation figure.");
		}

		RelationFigure violatedRelationFigure = new RelationFigure("Violated dependency from " + violationDTOs[0].fromClasspath + " to " + violationDTOs[0].toClasspath, true, violationDTOs.length);
		violatedRelationFigure.addDecorator(createViolationsDecorator(violationDTOs));
		return violatedRelationFigure;
	}

	public BaseFigure createFigure(AbstractDTO dto) {
		BaseFigure createdFigure = null;

		if ((dto instanceof ModuleDTO) || (dto instanceof AnalysedModuleDTO)) {
			createdFigure = createModuleFigure(dto);
		}

		if (null == createdFigure) {
			throw new RuntimeException("Unimplemented dto type '" + (null == dto ? "DTO=null" : dto.getClass().getSimpleName()) + "' passed to FigureFactory");
		}
		return createdFigure;
	}

	public BaseFigure createFigure(AbstractDTO dto, ViolationDTO[] violationDTOs) {
		BaseFigure createdFigure = createFigure(dto);
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
			throw new RuntimeException("DTO type '" + dto.getClass().getSimpleName() + "' is not recognized as a module dto.");
		}

		// TODO check these values with the define team
		if (type.toLowerCase().equals("layer")) {
			return new LayerFigure(name);
		} else if (type.toLowerCase().equals("component")) {
			return new ComponentFigure(name);
		} else if (type.toLowerCase().equals("class")) {
			return new ClassFigure(name);
		} else if (type.toLowerCase().equals("abstract")) {
			return new AbstractClassFigure(name);
		} else if (type.toLowerCase().equals("interface")) {
			return new InterfaceFigure(name);
		} else if (type.toLowerCase().equals("package")) {
			return new PackageFigure(name);
		} else if (type.toLowerCase().equals("subsystem")) {
			return new SubsystemFigure(name);
		} else {
			logger.debug("Type " + type.toLowerCase() + " is not supported. Created a ModuleFigure instead.");
			return new ModuleFigure(name, type);
		}
	}

	public ParentFigure createParentFigure(String parentName) {
		return new ParentFigure(parentName);
	}
}
