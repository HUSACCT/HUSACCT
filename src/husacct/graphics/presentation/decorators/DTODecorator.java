package husacct.graphics.presentation.decorators;

import org.jhotdraw.draw.Figure;

import husacct.common.dto.AbstractDTO;

public class DTODecorator extends Decorator {

	private static final long serialVersionUID = -7724638299537442673L;
	private AbstractDTO containedDTO;

	public DTODecorator(Figure decorator, AbstractDTO dto) {	
		super(decorator);
		containedDTO = dto;
	}
	
	public AbstractDTO getDTO() {
		return containedDTO;
	}
}
