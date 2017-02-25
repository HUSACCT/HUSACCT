package husacct.validate.task.imexporting.importing;

import husacct.common.dto.ViolationImExportDTO;
import husacct.common.imexport.XmlConversionUtils;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

public class ImportViolations {
	
    private Element violationsElement;

    public List<ViolationImExportDTO> importViolations(Element importElement) {
    	List<ViolationImExportDTO> imExportViolationDtoList = new ArrayList<>();
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

}