package husacct.graphics.util;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ExternalSystemDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.RelationFigure;

import java.util.ArrayList;
import java.util.HashMap;

public class FigureMap {
	private final HashMap<String, BaseFigure> moduleFiguresByName = new HashMap<String, BaseFigure>();
	private final HashMap<BaseFigure, AbstractDTO> moduleFigureDTOMap = new HashMap<BaseFigure, AbstractDTO>();
	private final HashMap<RelationFigure, DependencyDTO[]> dependencyLineDTOMap = new HashMap<RelationFigure, DependencyDTO[]>();
	private final HashMap<RelationFigure, ViolationDTO[]> violationLineDTOMap = new HashMap<RelationFigure, ViolationDTO[]>();
	private final HashMap<BaseFigure, ViolationDTO[]> violatedFigureDTOMap = new HashMap<BaseFigure, ViolationDTO[]>();

	private int maxDependencies, maxViolations, maxAll;

	public FigureMap() {
		clearAll();
	}

	public void clearAll() {
		maxDependencies = 0;
		maxViolations = 0;
		maxAll = 0;
		moduleFigureDTOMap.clear();
		dependencyLineDTOMap.clear();
		clearAllViolations();
		moduleFiguresByName.clear();
	}

	public void clearAllViolations() {
		violationLineDTOMap.clear();
		violatedFigureDTOMap.clear();
	}

	public boolean containsModule(String path) {
		return moduleFiguresByName.containsKey(path);
	}

	public BaseFigure findModuleByPath(String path) {
		return moduleFiguresByName.get(path);
	}

	public DependencyDTO[] getDependencyDTOs(BaseFigure figure) {
		return dependencyLineDTOMap.get(figure);
	}

	public int getMaxAll() {
		return maxAll;
	}

	public int getMaxDependencies() {
		return maxDependencies;
	}

	public int getMaxViolations() {
		return maxViolations;
	}

	public AbstractDTO getModuleDTO(BaseFigure figure) {
		return moduleFigureDTOMap.get(figure);
	}

	public ViolationDTO[] getViolatedDTOs(BaseFigure figure) {
		return violatedFigureDTOMap.get(figure);
	}

	public ArrayList<BaseFigure> getViolatedFigures() {
		ArrayList<BaseFigure> figures = new ArrayList<BaseFigure>();
		for (BaseFigure figure : violatedFigureDTOMap.keySet())
			figures.add(figure);
		return figures;
	}

	public ViolationDTO[] getViolationDTOs(BaseFigure figure) {
		return violationLineDTOMap.get(figure);
	}

	public ArrayList<BaseFigure> getViolationLines() {
		ArrayList<BaseFigure> figures = new ArrayList<BaseFigure>();
		for (BaseFigure figure : violationLineDTOMap.keySet())
			figures.add(figure);
		return figures;
	}

	public boolean isDependencyLine(BaseFigure figure) {
		return dependencyLineDTOMap.containsKey(figure);
	}

	public boolean isViolatedFigure(BaseFigure figure) {
		return violatedFigureDTOMap.containsKey(figure);
	}

	public boolean isViolationLine(BaseFigure figure) {
		return violationLineDTOMap.containsKey(figure);
	}

	public void linkDependencies(RelationFigure figure, DependencyDTO[] dtos) {
		dependencyLineDTOMap.put(figure, dtos);
		setMaxDependencies(dtos.length);
	}

	public void linkModule(BaseFigure figure, AbstractDTO dto) {
		moduleFigureDTOMap.put(figure, dto);

		// TODO: Re-factor this into a more clean design. Perhaps turn into a
		// dictionary?
		// NOTE: There should not be a difference in performance between a
		// dictionary and a hashmap,
		// but them being the same thing, why on earth would it be better
		// design?
		if (dto instanceof ModuleDTO) {
			ModuleDTO md = (ModuleDTO) dto;
			moduleFiguresByName.put(md.logicalPath, figure);
		} else if (dto instanceof AnalysedModuleDTO) {
			AnalysedModuleDTO md = (AnalysedModuleDTO) dto;
			this.moduleFiguresByName.put(md.uniqueName, figure);
		} else if (dto instanceof ExternalSystemDTO) {
			ExternalSystemDTO es = (ExternalSystemDTO) dto;
			this.moduleFiguresByName.put(es.systemPackage, figure);
		} else if(dto instanceof ProjectDTO){
			ProjectDTO pd = (ProjectDTO) dto;
			this.moduleFiguresByName.put(pd.name, figure);
		}
	}

	public void linkViolatedModule(BaseFigure figure, ViolationDTO[] dtos) {
		violatedFigureDTOMap.put(figure, dtos);
	}

	public void linkViolations(RelationFigure figure, ViolationDTO[] dtos) {
		violationLineDTOMap.put(figure, dtos);
		setMaxViolations(dtos.length);
	}

	private void setMaxDependencies(int newMax) {
		if (newMax > maxDependencies)
			maxDependencies = newMax;
		if (newMax > maxAll)
			maxAll = newMax;
	}

	private void setMaxViolations(int newMax) {
		if (newMax > maxViolations)
			maxViolations = newMax;
		if (newMax > maxAll)
			maxAll = newMax;
	}
}
