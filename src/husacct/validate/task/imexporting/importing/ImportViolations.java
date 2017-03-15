package husacct.validate.task.imexporting.importing;

import husacct.common.dto.ViolationImExportDTO;
import husacct.common.imexport.XmlConversionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

public class ImportViolations {
	
	private Logger logger = Logger.getLogger(ImportViolations.class);
    private Element importElement;
	public ImportViolations(Document previousViolations) {
		if (previousViolations != null) {
	    	this.importElement = previousViolations.getRootElement();
		}
    }

    public List<ViolationImExportDTO> importViolations() {
    	List<ViolationImExportDTO> imExportViolationDtoList = new ArrayList<ViolationImExportDTO>();
    	if (importElement != null) {
			for (Element childOfRootElement : importElement.getChildren()) {
				String name = childOfRootElement.getName();
				if (name.equals("violations")) {
			    	for (Element element : childOfRootElement.getChildren()){
			    		if (element.getName().equals("violation")) {
			    			ViolationImExportDTO dto = new ViolationImExportDTO();
			    			dto = (ViolationImExportDTO) XmlConversionUtils.writeElementToDto(element, dto);
			    			imExportViolationDtoList.add(dto);
			    		}
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
				break;
			}
		}
    	return validationDate;
    }

	private Calendar getCalendar(String stringCalendar){
		Calendar calendar = Calendar.getInstance();
		try {
			stringCalendar.trim();
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
			calendar.setTime(sdf1.parse(stringCalendar));
		} catch (IllegalArgumentException | ParseException e) {
			logger.warn(String.format("%s is not a valid datetime for violationsGeneratedOn; using current datetime", stringCalendar));
		}
		return calendar;
	}

}