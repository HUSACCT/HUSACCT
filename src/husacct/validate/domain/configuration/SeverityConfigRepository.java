package husacct.validate.domain.configuration;

import husacct.validate.domain.validation.Severity;
import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

public class SeverityConfigRepository {
	private List<Severity> severities;

	public SeverityConfigRepository(){
		this.severities = new ArrayList<Severity>();
	}

	public List<Severity> getAllSeverities(){
		return severities;
	}

	public void addSeverities(List<Severity> severities) {
// TODO ask Jorik about old code and how to use it and or fix it
//		this.severities.removeAll(null);
//		for(int i = 0; i < severities.size(); i++){
//			if(this.severities.get(i) != null){
//				this.severities.set(i, severities.get(i));
//			} else{
//				this.severities.add(severities.get(i));
//			}
//
//		}
//		reorderSeverityValues();
		this.severities = severities;
	}

	private void reorderSeverityValues(){
		for(int i = 0; i < severities.size(); i++){
			severities.get(i).setValue(i+1);
		}
	}
}