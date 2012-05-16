package husacct.graphics.presentation.figures;

import org.jhotdraw.draw.AttributeKeys;

public class AbstractClassFigure extends ClassFigure {
	private static final long serialVersionUID = 6814136333544958104L;

	public AbstractClassFigure(String name) {
		super(name);
		getClassNameText().set(AttributeKeys.FONT_ITALIC, true);
	}

}
