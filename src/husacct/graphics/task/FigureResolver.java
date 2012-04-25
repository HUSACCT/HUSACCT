package husacct.graphics.task;

import org.jhotdraw.draw.Figure;

import husacct.common.dto.AbstractDTO;
import husacct.graphics.presentation.decorators.DTODecorator;
import husacct.graphics.presentation.decorators.Decorator;
import husacct.graphics.presentation.figures.BaseFigure;

public final class FigureResolver {

	private FigureResolver() {
	}

	public static BaseFigure resolveFigure(Figure figure) {

		while (figure instanceof Decorator) {

			Decorator decorator = (Decorator) figure;
			figure = decorator.getDecorator();
		}

		return (BaseFigure) figure;
	}

	public static AbstractDTO resolveDTO(Figure figure) {

		while (figure != null) {

			if (figure instanceof DTODecorator) {

				DTODecorator decorator = (DTODecorator) figure;
				return (AbstractDTO) decorator.getDTO();

			} else {

				figure = ((Decorator) figure).getDecorator();
			}
		}

		throw new RuntimeException("Figure doesn't contain a DTO");
	}
}
