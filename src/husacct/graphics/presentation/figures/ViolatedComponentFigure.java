package husacct.graphics.presentation.figures;

import java.awt.Color;
import java.awt.geom.Rectangle2D.Double;

import husacct.common.dto.ViolationDTO;

@SuppressWarnings("serial")
public class ViolatedComponentFigure extends ComponentFigure implements
		IViolatedFigure
{
	private ViolationDTO[] violations;
	

	public ViolatedComponentFigure(Double rect, String name, ViolationDTO[] violations)
	{
		super(rect, name);
		
		this.violations = violations;
	}
	
	protected void initializeComponents()
	{
		super.initializeComponents();

		this.setLineColor(Color.RED);
		//this.setLineThickness(2.0);
	}

	@Override
	public ViolationDTO[] getViolations()
	{
		return this.violations;
	}

}
