package husacct.graphics.util;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
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
		this.clearAll();
	}

	public void clearAll() {
		this.maxDependencies = 0;
		this.maxViolations = 0;
		this.maxAll = 0;
		this.moduleFigureDTOMap.clear();
		this.dependencyLineDTOMap.clear();
		this.clearAllViolations();
		this.moduleFiguresByName.clear();
	}

	public void clearAllViolations() {
		this.violationLineDTOMap.clear();
		this.violatedFigureDTOMap.clear();
	}

	public boolean containsModule(String path) {
		return this.moduleFiguresByName.containsKey(path);
	}

	public BaseFigure findModuleByPath(String path) {
		return this.moduleFiguresByName.get(path);
	}

	public DependencyDTO[] getDependencyDTOs(BaseFigure figure) {
		return this.dependencyLineDTOMap.get(figure);
	}

	public int getMaxAll() {
		return this.maxAll;
	}

	public int getMaxDependencies() {
		return this.maxDependencies;
	}

	public int getMaxViolations() {
		return this.maxViolations;
	}

	public AbstractDTO getModuleDTO(BaseFigure figure) {
		return this.moduleFigureDTOMap.get(figure);
	}

	public ViolationDTO[] getViolatedDTOs(BaseFigure figure) {
		return this.violatedFigureDTOMap.get(figure);
	}

	public ArrayList<BaseFigure> getViolatedFigures() {
		ArrayList<BaseFigure> figures = new ArrayList<BaseFigure>();
		for (BaseFigure figure : this.violatedFigureDTOMap.keySet()) {
			figures.add(figure);
		}
		return figures;
	}

	public ViolationDTO[] getViolationDTOs(BaseFigure figure) {
		return this.violationLineDTOMap.get(figure);
	}

	public ArrayList<BaseFigure> getViolationLines() {
		ArrayList<BaseFigure> figures = new ArrayList<BaseFigure>();
		for (BaseFigure figure : this.violationLineDTOMap.keySet()) {
			figures.add(figure);
		}
		return figures;
	}

	public boolean isDependencyLine(BaseFigure figure) {
		return this.dependencyLineDTOMap.containsKey(figure);
	}

	public boolean isViolatedFigure(BaseFigure figure) {
		return this.violatedFigureDTOMap.containsKey(figure);
	}

	public boolean isViolationLine(BaseFigure figure) {
		return this.violationLineDTOMap.containsKey(figure);
	}

	public void linkDependencies(RelationFigure figure, DependencyDTO[] dtos) {
		this.dependencyLineDTOMap.put(figure, dtos);
		this.setMaxDependencies(dtos.length);
	}

	public void linkModule(BaseFigure figure, AbstractDTO dto) {
		this.moduleFigureDTOMap.put(figure, dto);

		// TODO: Re-factor this into a more clean design. Perhaps turn into a
		// dictionary?
		// NOTE: There should not be a difference in performance between a
		// dictionary and a hashmap,
		// but them being the same thing, why on earth would it be better
		// design?
		if (dto instanceof ModuleDTO) {
			ModuleDTO md = (ModuleDTO) dto;
			this.moduleFiguresByName.put(md.logicalPath, figure);
		} else if (dto instanceof AnalysedModuleDTO) {
			AnalysedModuleDTO md = (AnalysedModuleDTO) dto;
			this.moduleFiguresByName.put(md.uniqueName, figure);
		}
		//TODO!!! instanceof ProjectDTO
	}

	public void linkViolatedModule(BaseFigure figure, ViolationDTO[] dtos) {
		this.violatedFigureDTOMap.put(figure, dtos);
	}

	public void linkViolations(RelationFigure figure, ViolationDTO[] dtos) {
		this.violationLineDTOMap.put(figure, dtos);
		this.setMaxViolations(dtos.length);
	}

	private void setMaxDependencies(int newMax) {
		if (newMax > this.maxDependencies) {
			this.maxDependencies = newMax;
		}
		if (newMax > this.maxAll) {
			this.maxAll = newMax;
		}
	}

	private void setMaxViolations(int newMax) {
		if (newMax > this.maxViolations) {
			this.maxViolations = newMax;
		}
		if (newMax > this.maxAll) {
			this.maxAll = newMax;
		}
	}
}
