package husacct.graphics.presentation.decorators;

import husacct.common.dto.AbstractDTO;

public class DTODecorator extends Decorator {

	private static final long serialVersionUID = -7724638299537442673L;
	private AbstractDTO containedDTO;

	public DTODecorator(AbstractDTO dto) {
		containedDTO = dto;
	}
	
	public AbstractDTO getDTO() {
		return containedDTO;
	}
}
