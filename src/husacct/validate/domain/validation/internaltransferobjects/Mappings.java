package husacct.validate.domain.validation.internaltransferobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Mappings {

	private final List<Mapping> mappingFrom;
	private final List<Mapping> mappingTo;

	public Mappings(List<Mapping> mappingFrom, List<Mapping> mappingTo) {
		HashMap<String, Mapping> fromMap = new HashMap<String, Mapping>();
		for(Mapping from : mappingFrom){
			fromMap.put(from.getPhysicalPath(), from);
		}
		this.mappingFrom = new ArrayList<Mapping>(fromMap.values());
		
		HashMap<String, Mapping> toMap = new HashMap<String, Mapping>();
		for(Mapping to : mappingTo){
			toMap.put(to.getPhysicalPath(), to);
		}
		this.mappingTo = new ArrayList<Mapping>(toMap.values());
	}

	public List<Mapping> getMappingFrom() {
		return mappingFrom;
	}

	public List<Mapping> getMappingTo() {
		return mappingTo;
	}
}