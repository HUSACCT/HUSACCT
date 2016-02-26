package husacct.graphics.domain;

import husacct.analyse.serviceinterface.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.domain.figures.BaseFigure;
import husacct.graphics.domain.figures.RelationFigure;

import java.util.ArrayList;
import java.util.HashMap;

public class FigureMap {
	private final HashMap<RelationFigure, DependencyDTO[]>	dependencyLineDTOMap	= new HashMap<RelationFigure, DependencyDTO[]>();
	private final HashMap<RelationFigure, ViolationDTO[]>	violationLineDTOMap		= new HashMap<RelationFigure, ViolationDTO[]>();
	private int	maxDependencies, maxViolations, maxAll;
	
	public FigureMap() {
		clearAll();
	}
	
	public void clearAll() {
		maxDependencies = 0;
		maxViolations = 0;
		maxAll = 0;
		dependencyLineDTOMap.clear();
		clearAllViolations();
	}
	
	public void clearAllViolations() {
		violationLineDTOMap.clear();
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
	
	public boolean isViolationLine(BaseFigure figure) {
		return violationLineDTOMap.containsKey(figure);
	}
	
	public void linkDependencies(RelationFigure figure, DependencyDTO[] dtos) {
		dependencyLineDTOMap.put(figure, dtos);
		setMaxDependencies(dtos.length);
	}
	
	public void linkViolations(RelationFigure figure, ViolationDTO[] dtos) {
		violationLineDTOMap.put(figure, dtos);
		setMaxViolations(dtos.length);
	}
	
	private void setMaxDependencies(int newMax) {
		if (newMax > maxDependencies) maxDependencies = newMax;
		if (newMax > maxAll) maxAll = newMax;
	}
	
	private void setMaxViolations(int newMax) {
		if (newMax > maxViolations) maxViolations = newMax;
		if (newMax > maxAll) maxAll = newMax;
	}
}
