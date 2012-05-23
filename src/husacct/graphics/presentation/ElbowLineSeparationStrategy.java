package husacct.graphics.presentation;

import husacct.graphics.presentation.figures.RelationFigure;
import husacct.graphics.presentation.figures.ElbowLiner;

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
