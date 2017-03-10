package husacct.validate.task.imexporting.importing;

import husacct.common.dto.ViolationImExportDTO;
import husacct.common.imexport.XmlConversionUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Logger;
import org.jdom2.Element;

public class ImportViolations {
	
	private Logger logger = Logger.getLogger(ImportViolations.class);
    private Element importElement;
	private Element violationsElement;
	public ImportViolations(Element importElement) {
    	this.importElement = importElement;
    }

    public List<ViolationImExportDTO> importViolations() {
    	List<ViolationImExportDTO> imExportViolationDtoList = new ArrayList<ViolationImExportDTO>();
    	violationsElement = new Element("violations");
		for (Element rootElement : importElement.getChildren()) {
			String name = rootElement.getName();
			if (name.equals("violations")) {
				violationsElement = rootElement;
		    	for (Element element : violationsElement.getChildren()){
		    		if (element.getName().equals("violation")) {
		    			ViolationImExportDTO dto = new ViolationImExportDTO();
		    			dto = (ViolationImExportDTO) XmlConversionUtils.writeElementToDto(element, dto);
		    			imExportViolationDtoList.add(dto);
		    		}
		    	}
			} 
		}
    	return imExportViolationDtoList;
    }
    
    public Calendar getValidationDate() {
    	Calendar validationDate = Calendar.getInstance();
		for (Element rootElement : importElement.getChildren()) {
			String name = rootElement.getName();
			if (name.equals("violationsGeneratedOn")) {
				String validationDateString = rootElement.getText();
				if (validationDateString != null) {
					validationDate = getCalendar(validationDateString);
				}
			}
		}
    	return validationDate;
    }

	private Calendar getCalendar(String stringCalendar) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(stringCalendar).toGregorianCalendar();
		} catch (IllegalArgumentException e) {
			logger.error(String.format("%s is not a valid datetime, switching back to current datetime", stringCalendar));
		} catch (DatatypeConfigurationException e) {
			logger.error(e.getMessage());
		}
		return calendar;
	}

}