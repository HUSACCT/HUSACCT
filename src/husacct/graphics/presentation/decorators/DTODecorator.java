package husacct.graphics.presentation.decorators;

import java.awt.Color;

import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.Figure;

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
	
	@Override 
	public void setDecorator(Figure newDecorator) {
		Figure oldDecorator = getDecorator();
		if (oldDecorator != null)
			oldDecorator.set(AttributeKeys.STROKE_COLOR, Color.BLACK);
		
		super.setDecorator(newDecorator);
		getDecorator().set(AttributeKeys.STROKE_COLOR, Color.RED);
	}	
}
