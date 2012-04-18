package husacct.graphics.presentation.figures;

import husacct.common.dto.ViolationDTO;

import java.awt.Color;
import java.awt.geom.Rectangle2D.Double;

public class ViolatedComponentFigure extends BaseFigure implements
		IViolatedFigure {

	private static final long serialVersionUID = 6886808516854527764L;
	private ViolationDTO[] violations;

	public ViolatedComponentFigure(Double rect, String name,
			ViolationDTO[] violations) {
		super(rect, name);

		this.violations = violations;
	}

	protected void initializeComponents() {
		super.initializeComponents();

		this.setStrokeColor(Color.RED);
		// this.setLineThickness(2.0);
	}

	@Override
	public ViolationDTO[] getViolations() {
		return this.violations;
	}

}
