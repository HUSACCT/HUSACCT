package husacct.graphics.presentation.decorators;

import husacct.graphics.presentation.figures.BaseFigure;

public interface Decorator {

    public void decorate(BaseFigure f);

    public void deDecorate(BaseFigure f);
}
