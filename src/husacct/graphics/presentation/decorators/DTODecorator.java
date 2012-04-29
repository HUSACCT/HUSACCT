package husacct.graphics.presentation.decorators;

import husacct.common.dto.AbstractDTO;
import husacct.graphics.presentation.figures.BaseFigure;

public class DTODecorator extends Decorator {

	private static final long serialVersionUID = -7724638299537442673L;
	private AbstractDTO containedDTO;

	public DTODecorator(BaseFigure decorator, AbstractDTO dto) {	
		super(decorator);
		containedDTO = dto;
	}
	
	public AbstractDTO getDTO() {
		return containedDTO;
	}

	@Override
	public boolean isModule() {
		return true;
	}

	@Override
	public boolean isLine() {
		return false;
	}
}
