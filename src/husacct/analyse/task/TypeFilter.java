package husacct.analyse.task;

import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ExternalSystemDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TypeFilter {	
	private static final Map<String, List<String>> TYPE_DICTIONARY = new HashMap<>();
	private static final List<String> DICTIONARY_KEYS;
	
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
	
	private static ArrayList<DependencyDTO> filterDependenciesInExternalSystemDTO(ArrayList<DependencyDTO> dtos) {
		ArrayList<DependencyDTO> newdtos = new ArrayList<>();
		Iterator<DependencyDTO> iterator = dtos.iterator();
		while(iterator.hasNext())
			newdtos.add(editDTO(iterator.next()));
		return newdtos;
	}
	
	public static ExternalSystemDTO[] filterExternalSystems(ExternalSystemDTO[] dtos) {
		for (int dto = 0; dto < dtos.length; dto++)
			dtos[dto] = editExternalSystemDTO(dtos[dto]);
		return dtos;
	}

	private static ExternalSystemDTO editExternalSystemDTO(ExternalSystemDTO externalSystemDTO) {
		externalSystemDTO.fromDependencies = filterDependenciesInExternalSystemDTO(externalSystemDTO.fromDependencies);
		return externalSystemDTO;
	}

	private static DependencyDTO editDTO(DependencyDTO dto) {
		try {
			dto.type = getSimpleType(dto.type);
		} catch (NoSimpleTypeFoundException e) {
			e.printStackTrace();
		}
		return dto;
	}

	private static String getSimpleType(String complexType) throws NoSimpleTypeFoundException {
		complexType = complexType.toLowerCase();
		for (String simpleTypeAsKey : DICTIONARY_KEYS){
			for (String subcat : TYPE_DICTIONARY.get(simpleTypeAsKey)){
				if (complexType.startsWith(subcat.toLowerCase())){
					return simpleTypeAsKey;
				}
			}
		}
		throw new NoSimpleTypeFoundException("No simple type found for " + complexType);
	}
	
	private static class NoSimpleTypeFoundException extends Exception {
		private static final long serialVersionUID = 1L;

		public NoSimpleTypeFoundException(String message) {
			super(message);
		}
	}
}
