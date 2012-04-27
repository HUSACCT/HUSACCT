package husacct.validate.domain.validation.iternal_tranfer_objects;

import java.util.List;

public class Mappings {
	private final List<Mapping> mappingFrom;
	private final List<Mapping> mappingTo;

	public Mappings(List<Mapping> mappingFrom, List<Mapping> mappingTo){
		this.mappingFrom = mappingFrom;
		this.mappingTo = mappingTo;
	}

	public List<Mapping> getMappingFrom() {
		return mappingFrom;
	}

	public List<Mapping> getMappingTo() {
		return mappingTo;
	}
}