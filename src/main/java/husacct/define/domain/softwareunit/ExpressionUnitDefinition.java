package husacct.define.domain.softwareunit;

import java.util.ArrayList;

public class ExpressionUnitDefinition extends SoftwareUnitDefinition {

	private ArrayList<SoftwareUnitDefinition> container = new ArrayList<>();

	public ExpressionUnitDefinition(String name, Type type) {
		super(name, type);

	}

	public void addSoftwareUnit(SoftwareUnitDefinition unit) {
		this.container.add(unit);

	}

	public void addSoftwareUnits(ArrayList<SoftwareUnitDefinition> units) {

		switch (container.size()) {
		case 0:
			container = units;
			break;

		default:
			for (SoftwareUnitDefinition su : units) {
				container.add(su);
			}

			break;
		}

	}
	
	public ArrayList<SoftwareUnitDefinition> getExpressionValues()
	{
		
	return container;	
	}


}
