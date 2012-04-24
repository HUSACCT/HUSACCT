package husacct.validate.domain.configuration;

import husacct.validate.domain.validation.Severity;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class SeverityConfigRepository {
	private List<Severity> severities;

	public SeverityConfigRepository(){
		this.severities = new ArrayList<Severity>();
		Severity s1 = new Severity();
		s1.setColor(Color.GREEN);
		s1.setDefaultName("Low");

		Severity s2 = new Severity();
		s2.setColor(Color.YELLOW);
		s2.setDefaultName("Medium");

		Severity s3 = new Severity();
		s3.setColor(Color.RED);
		s3.setDefaultName("High");

		severities.add(s1);
		severities.add(s2);
		severities.add(s3);
	}

	public List<Severity> getAllSeverities(){
		return severities;
	}

	public void addSeverities(List<Severity> severities){
		this.severities = severities;
	}
}