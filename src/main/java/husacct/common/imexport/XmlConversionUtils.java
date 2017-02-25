package husacct.common.imexport;

import husacct.common.dto.AbstractDTO;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;
import org.jdom2.Element;

public class XmlConversionUtils {
	
    private static Logger logger = Logger.getLogger(XmlConversionUtils.class);

	
    public static Element writeDtoToXml(String elementName, AbstractDTO dto) {
    	Element dtoElement = new Element(elementName);
    	Class<?> d = dto.getClass();
    	try {
			String propertyName = "";
    		String valueString = "";
    		Field[] fields = d.getDeclaredFields();
    		for( Field field : fields ){
    			propertyName = field.getName();
    			Object value = field.get(dto);
    			if (value != null) {
    				valueString = field.get(dto).toString();
    			}
    			dtoElement.addContent(new Element(propertyName).setText(valueString));
    		 }
		} catch (IllegalAccessException e) {
			logger.warn("Could not write to xml for element: " + elementName + " Exception: " + e.getMessage());
			//e.printStackTrace();
		} catch (Exception e) {
			logger.warn("Could not write to xml for element: " + elementName + " Exception: " + e.getMessage());
			//e.printStackTrace();
		}
        return dtoElement;
    }
    

    
    public static AbstractDTO writeElementToDto(Element element, AbstractDTO dto) {
    	Class<?> d = dto.getClass();
    	try {
			String propertyName;
			Class<?> propertyType;
    		String valueString = "";
    		boolean valueBoolean = false;
    		int valueInt = 0;
    		Field[] fields = d.getDeclaredFields();
    		for( Field field : fields ){
    			propertyName = field.getName();
    			if (element.getChildText(propertyName) != null) {
	    			propertyType = field.getType();
	        		if (propertyType == String.class) {
		    			valueString = element.getChildText(propertyName);
		        		field.set(dto, valueString);
	        		} else {
	        			if (propertyType == boolean.class) {
		        			valueBoolean = Boolean.parseBoolean(element.getChildText(propertyName));
			        		field.set(dto, valueBoolean);
		        		} else if (propertyType == int.class) {
		        			valueInt = Integer.parseInt(element.getChildText(propertyName));
			        		field.set(dto, valueInt);
		        		}
	        		}
    			}
    		}
		} catch (IllegalAccessException | IllegalArgumentException e) {
            logger.warn("Could not read from xml for class: " + dto.getClass().getName() + " Exception: " + e.getMessage());
			//e.printStackTrace();
		}
        return dto;
    }


}
