package husacct.graphics.presentation.figures;

import java.awt.Color;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;

public class ViolatedRelationFigure extends RelationFigure implements
		IViolatedFigure
{
	private static final long serialVersionUID = -2679119740061602305L;
	private ViolationDTO[] violationDTOs;

	public ViolatedRelationFigure(DependencyDTO dependencyDTO, ViolationDTO[] violationDTOs)
	{
		super(dependencyDTO);
		
		this.violationDTOs = violationDTOs;
		
		//this.setLineThickness(1.0);
		this.setLineColor(Color.RED);
	}

	@Override
	public ViolationDTO[] getViolations()
	{
		return this.violationDTOs;
	}

}
