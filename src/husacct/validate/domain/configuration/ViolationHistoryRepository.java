package husacct.validate.domain.configuration;

import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.validation.ViolationHistory;

import java.util.ArrayList;
import java.util.List;

public class ViolationHistoryRepository {
	
	private List<ViolationHistory> violationHistory;
	private final ConfigurationServiceImpl configuration;
	
	public ViolationHistoryRepository(ConfigurationServiceImpl configuration) {
		this.configuration = configuration;
		violationHistory = new ArrayList<ViolationHistory>();
	}

	public List<ViolationHistory> getViolationHistory() {
		return violationHistory;
	}
	
	public void setViolationHistory(List<ViolationHistory> violationhistories){
		this.violationHistory = violationhistories;
	}

//	public void addNewHistoryPoint(String description){
//		configuration.
//	}
}
