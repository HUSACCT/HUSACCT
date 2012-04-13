package husacct.graphics.task.figures;

import java.awt.Color;
import java.awt.geom.Rectangle2D.Double;

import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ViolationDTO;

@SuppressWarnings("serial")
public class ViolatedComponentFigure extends ComponentFigure implements
		IViolatedFigure
{
	private ViolationDTO[] violations;
	

	public ViolatedComponentFigure(Double rect, ModuleDTO moduleDTO, ViolationDTO[] violations)
	{
		super(rect, moduleDTO);
		
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
