package husacct.graphics.domain.linelayoutstrategies;

import husacct.graphics.domain.figures.ElbowLiner;
import husacct.graphics.domain.figures.RelationFigure;

import java.util.HashSet;

public class ElbowLineSeparationStrategy implements ILineSeparationStrategy {
	public final int RELATIONS_DISTANCE = 50;

	@Override
	public void separateLines(HashSet<RelationFigure> overlappingLineFigures) {

		double start = 0 - ((overlappingLineFigures.size() - 1) * RELATIONS_DISTANCE / 2);

		for (RelationFigure figure : overlappingLineFigures) {

			ElbowLiner lineType = new ElbowLiner(start);
			figure.setLiner(lineType);
			
			start += RELATIONS_DISTANCE;
		}

	}

}
