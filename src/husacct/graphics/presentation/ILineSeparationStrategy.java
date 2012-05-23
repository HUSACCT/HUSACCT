package husacct.graphics.presentation;

import husacct.graphics.presentation.figures.RelationFigure;

import java.util.HashSet;

public interface ILineSeparationStrategy {
	public void separateLines(HashSet<RelationFigure> overlappingLineFigures);
}
