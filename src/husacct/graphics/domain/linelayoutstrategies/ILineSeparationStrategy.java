package husacct.graphics.domain.linelayoutstrategies;

import husacct.graphics.domain.figures.RelationFigure;

import java.util.HashSet;

public interface ILineSeparationStrategy {
	public void separateLines(HashSet<RelationFigure> overlappingLineFigures);
}
