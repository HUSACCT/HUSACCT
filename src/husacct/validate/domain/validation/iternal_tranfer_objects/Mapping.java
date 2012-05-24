package husacct.validate.domain.validation.iternal_tranfer_objects;

import java.util.Arrays;
import java.util.List;

public class Mapping {
	private final String logicalPath;
	private final String physicalPath;
	private final String logicalPathType;
	private String[] violationTypes;

	public Mapping(String logicalPath, String logicalPathType, String physicalPath, String[] violationTypes){
		this.logicalPath = logicalPath;
		this.physicalPath = physicalPath;
		this.logicalPathType = logicalPathType;
		this.violationTypes = violationTypes;
	}

	public String getLogicalPath() {
		return logicalPath;
	}

	public String getPhysicalPath() {
		return physicalPath;
	}

	public String getLogicalPathType() {
		return logicalPathType;
	}

	public String[] getViolationTypes() {
		return violationTypes;
	}

	public void removeViolationType(String violationType){
		List<String> violationTypeList = Arrays.asList(violationTypes);
		for(int i = 0; i < violationTypeList.size(); i++){
			if(violationTypeList.get(i).toLowerCase().equals(violationType.toLowerCase())){
				violationTypeList.remove(i);
				i--;
			}
		}
		violationTypes = violationTypeList.toArray(new String[]{});
	}
}