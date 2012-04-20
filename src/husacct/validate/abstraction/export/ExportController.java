package husacct.validate.abstraction.export;

import husacct.validate.domain.validation.Violation;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

public class ExportController {
	
	private ExportFactory exportFactory;
	
	public ExportController() {
		exportFactory = new ExportFactory();
	}
	
	public Document exportViolationsXML(List<Violation> violations) throws ParserConfigurationException {
		return exportFactory.exportXML("violations", violations);
	}

}
