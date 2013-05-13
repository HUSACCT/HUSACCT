package husacct.validate.task.export.xml;

import husacct.validate.domain.validation.Violation;

import java.util.AbstractMap.SimpleEntry;
import java.util.Calendar;
import java.util.List;

import org.jdom2.Element;

public class ExportViolations {

	
	public Element getAllViolations(SimpleEntry<Calendar, List<Violation>> allViolations) {
		Element root = new Element("violations");
		
		for(int i = 0; i < allViolations.getValue().size(); i++) {
			System.out.println(allViolations.getValue().get(i).toString());
		}
		
		
		
		
		return root;
	}
}
