package husacct.graphics.domain.decorators;

import husacct.graphics.domain.figures.BaseFigure;

public interface Decorator {
	public void decorate(BaseFigure f);
	
	public void deDecorate(BaseFigure f);
}
