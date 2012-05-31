package husacct.validate.task.fetch.xml;

import husacct.validate.domain.validation.Severity;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.jdom2.Element;

public class ImportSeverities {

	private Logger logger = Logger.getLogger(ImportSeverities.class);

	public List<Severity> importSeverities(Element element) {
		List<Severity> severities = new ArrayList<Severity>();

		for (Element severityElement : element.getChildren()) {
			UUID id = UUID.fromString(severityElement.getChildText("id"));

			String defaultname = severityElement.getChildText("defaultName");
			String username = severityElement.getChildText("userName");			

			final String rgbColor = severityElement.getChildText("color");
			Color color = getColor(rgbColor);

			Severity severity = new Severity(id, defaultname, username, color);
			severities.add(severity);
		}
		return severities;
	}

	private Color getColor(String rgbColor){
		Color color = null;
		try{
			color = new Color(Integer.parseInt(rgbColor));
		}catch(NumberFormatException e){
			logger.warn(String.format("No valid RGB color found for value: %s", rgbColor));
			color = Color.WHITE;
		}
		return color;
	}
}