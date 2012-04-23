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
		s1.setValue(1);

		Severity s2 = new Severity();
		s2.setColor(Color.YELLOW);
		s2.setDefaultName("Medium");
		s2.setValue(2);

		Severity s3 = new Severity();
		s3.setColor(Color.RED);
		s3.setDefaultName("High");
		s3.setValue(3);

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