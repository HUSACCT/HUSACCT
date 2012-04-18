package husacct.graphics.presentation.figures;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.ViolationDTO;

import java.awt.Color;

public class ViolatedComponentFigure extends ModuleFigure implements
		IViolatedFigure {

	private static final long serialVersionUID = 6886808516854527764L;
	private ViolationDTO[] violations;

	public ViolatedComponentFigure(AbstractDTO dto, ViolationDTO[] violations)
	{
		super(dto);

		this.violations = violations;
		
		this.setStrokeColor(Color.RED);
	}

	@Override
	public ViolationDTO[] getViolations() {
		return this.violations;
	}

}
