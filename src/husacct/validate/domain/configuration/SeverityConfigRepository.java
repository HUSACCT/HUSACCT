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
		for(int i = 0; i < severities.size(); i++){
			if(this.severities.get(i) != null){
				this.severities.set(i, severities.get(i));
			} else{
				this.severities.add(severities.get(i));
			}

		}
		reorderSeverityValues();
	}

	private void reorderSeverityValues(){
		for(int i = 0; i < severities.size(); i++){
			severities.get(i).setValue(i+1);
		}
	}
}