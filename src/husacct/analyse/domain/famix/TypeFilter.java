package husacct.analyse.domain.famix;

import husacct.common.dto.DependencyDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class TypeFilter {	
	private static final Map<String, List<String>> TYPE_DICTIONARY = new HashMap<>();
	private static final List<String> DICTIONARY_KEYS;
    private static final Logger logger = Logger.getLogger(TypeFilter.class);

	
	static {
		TYPE_DICTIONARY.put("Inheritance", Arrays.asList(new String[] {"Extends", "Implements", "Inheritance"}));
		TYPE_DICTIONARY.put("Import", Arrays.asList(new String[] {"Import"} ));
		TYPE_DICTIONARY.put("Call", Arrays.asList(new String[] {"Invoc", "Call"} ));
		TYPE_DICTIONARY.put("Annotation", Arrays.asList(new String[] { "Annotation" }));
		TYPE_DICTIONARY.put("Access", Arrays.asList(new String[] { "Access" }));
		TYPE_DICTIONARY.put("Declaration", Arrays.asList(new String[] { "Exception", "Declaration" }));
		DICTIONARY_KEYS = new ArrayList<>(TYPE_DICTIONARY.keySet());
	}

	public static DependencyDTO[] filterDependencies(DependencyDTO[] dtos) {
		for (int dto = 0; dto < dtos.length; dto++)
			dtos[dto] = editDTO(dtos[dto]);
		return dtos;
	}
	
	private static DependencyDTO editDTO(DependencyDTO dto) {
		try {
			dto.type = getSimpleType(dto.type);
		} catch (NoSimpleTypeFoundException e) {
	        logger.warn(new Date().toString() + " Exception: " + e);
	        //e.printStackTrace();
		}
		return dto;
	}

	public static String getSimpleType(String complexType) throws NoSimpleTypeFoundException {
		for (String simpleTypeAsKey : DICTIONARY_KEYS)
			for (String subcat : TYPE_DICTIONARY.get(simpleTypeAsKey))
				if (complexType.startsWith(subcat))
					return simpleTypeAsKey;
		throw new NoSimpleTypeFoundException("No simple type found for " + complexType);
	}
	
	private static class NoSimpleTypeFoundException extends Exception {
		private static final long serialVersionUID = 1L;

		public NoSimpleTypeFoundException(String message) {
			super(message);
		}
	}
}
