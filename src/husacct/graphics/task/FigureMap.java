package husacct.graphics.task;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.RelationFigure;

import java.util.ArrayList;
import java.util.HashMap;

public class FigureMap {

	// Normal figures
	private HashMap<BaseFigure, AbstractDTO> moduleFigureDTOMap = new HashMap<BaseFigure, AbstractDTO>();
	// Dependency lines
	private HashMap<RelationFigure, DependencyDTO[]> dependencyLineDTOMap = new HashMap<RelationFigure, DependencyDTO[]>();
	// Violation lines
	private HashMap<RelationFigure, ViolationDTO[]> violationLineDTOMap = new HashMap<RelationFigure, ViolationDTO[]>();
	// Normal figures with internal violations
	private HashMap<BaseFigure, ViolationDTO[]> violatedFigureDTOMap = new HashMap<BaseFigure, ViolationDTO[]>();

	public FigureMap() {

	}

	// Clearing

	public void clearAll() {
		this.moduleFigureDTOMap.clear();
		this.dependencyLineDTOMap.clear();
		this.violationLineDTOMap.clear();
		this.violatedFigureDTOMap.clear();
	}

	public void clearAllViolations() {
		this.violationLineDTOMap.clear();
		this.violatedFigureDTOMap.clear();
	}

	// Get figures

	public ArrayList<BaseFigure> getViolationLines() {
		ArrayList<BaseFigure> figures = new ArrayList<BaseFigure>();
		for (BaseFigure figure : violationLineDTOMap.keySet()) {
			figures.add(figure);
		}
		return figures;
	}

	public ArrayList<BaseFigure> getViolatedFigures() {
		ArrayList<BaseFigure> figures = new ArrayList<BaseFigure>();
		for (BaseFigure figure : violatedFigureDTOMap.keySet()) {
			figures.add(figure);
		}
		return figures;
	}

	// Get DTOs

	public AbstractDTO getModuleDTO(BaseFigure figure) {
		return moduleFigureDTOMap.get(figure);
	}

	public DependencyDTO[] getDependencyDTOs(BaseFigure figure) {
		return dependencyLineDTOMap.get(figure);
	}

	public ViolationDTO[] getViolationDTOs(BaseFigure figure) {
		return violationLineDTOMap.get(figure);
	}

	public ViolationDTO[] getViolatedDTOs(BaseFigure figure) {
		return violatedFigureDTOMap.get(figure);
	}

	// Linking

	public void linkModule(BaseFigure figure, AbstractDTO dto) {
		moduleFigureDTOMap.put(figure, dto);
	}

	public void linkViolatedModule(BaseFigure figure, ViolationDTO[] dtos) {
		violatedFigureDTOMap.put(figure, dtos);
	}

	public void linkDependencies(RelationFigure figure, DependencyDTO[] dtos) {
		dependencyLineDTOMap.put(figure, dtos);
	}

	public void linkViolations(RelationFigure figure, ViolationDTO[] dtos) {
		violationLineDTOMap.put(figure, dtos);
	}

	// Inquiries about figures

	public boolean isDependencyLine(BaseFigure figure) {
		return dependencyLineDTOMap.containsKey(figure);
	}

	public boolean isViolationLine(BaseFigure figure) {
		return violationLineDTOMap.containsKey(figure);
	}

	public boolean isViolatedFigure(BaseFigure figure) {
		return violatedFigureDTOMap.containsKey(figure);
	}

}
