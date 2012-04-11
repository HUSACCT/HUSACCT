package husacct.validate.domain.factory;

import husacct.validate.domain.violationtype.ViolationType;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public abstract class AbstractViolationType {
	public abstract List<ViolationType> createViolationTypesByRule(String key);
	public abstract ViolationType createViolationType(String violationKey);

	protected List<ViolationType> generateViolationTypes(EnumSet<?> enums){
		List<ViolationType> violationtypes = new ArrayList<ViolationType>();
		for(Enum<?> enumValue : enums){
			ViolationType violationtype = generateViolationType(enumValue);
			violationtypes.add(violationtype);
		}
		return violationtypes;
	}
	
	private ViolationType generateViolationType(Enum<?> enumValue){
		return new ViolationType(enumValue.toString());
	}
}