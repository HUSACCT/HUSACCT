package husacct.graphics.presentation.linelayoutstrategies;

import husacct.graphics.presentation.figures.RelationFigure;

import java.util.HashSet;

import org.jhotdraw.draw.connector.Connector;

public class ConnectorLineSeparationStrategy implements ILineSeparationStrategy {
	public final int RELATIONS_DISTANCE = 200;

	@Override
	public void separateLines(HashSet<RelationFigure> overlappingLineFigures) {

		double start = 0 - (overlappingLineFigures.size() - 1)
				* RELATIONS_DISTANCE / 2;

		for (RelationFigure figure : overlappingLineFigures) {

			Connector newStartConnector = new DistantiatedChopRectangleConnector(
					figure.getStartFigure(), start);
			figure.setStartConnector(newStartConnector);

			Connector newEndConnector = new DistantiatedChopRectangleConnector(
					figure.getEndFigure(), start);
			figure.setEndConnector(newEndConnector);

			start += RELATIONS_DISTANCE;
		}

	}

}
