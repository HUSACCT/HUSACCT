package husacct.graphics.presentation.linelayoutstrategies;

import husacct.graphics.presentation.figures.RelationFigure;

import java.util.HashSet;

public interface ILineSeparationStrategy {
	public void separateLines(HashSet<RelationFigure> overlappingLineFigures);
}
