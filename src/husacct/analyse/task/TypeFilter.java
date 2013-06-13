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
		TYPE_DICTIONARY.put("Inheritance", Arrays.asList(new String[] {
				"ExtendsConcreteExtendsAbstract",
				"ExtendsAbstractExtendsAbstract",
				"ExtendsAbstractExtendsConcrete",
				"ExtendsConcreteExtendsConcrete",
				"ExtendsConcreteImplements",
				"ImplementsExtendsInterface",
				"ExtendsConcrete",
				"ExtendsAbstract",
				"ExtendsLibrary",
				"ExtendsInterface",
				"ExtendsConcreteInvocMethod",
				"ExtendsConcreteImport",
				"ExtendsConcreteDeclarationInstanceVariable",
				"ExtendsConcreteDeclarationVariableWithinMethod",
				"ExtendsConcreteDeclarationReturnType",
				"ExtendsConcreteException",
				"Implements",
				"Extends"
		}) );
		TYPE_DICTIONARY.put("Import", Arrays.asList(new String[] {
				"Import"
		} ));
		TYPE_DICTIONARY.put("Call", Arrays.asList(new String[] {
				"InvocMethod",
				"InvocConstructor"
		}));
		TYPE_DICTIONARY.put("Annotation", Arrays.asList(new String[] {
				"Annotation"
		}));
		TYPE_DICTIONARY.put("Access", Arrays.asList(new String[] {
				"AccessPropertyOrField",
				"AccessClassVariableInterface"	
		}));
		TYPE_DICTIONARY.put("Declaration", Arrays.asList(new String[] {
				"Declaration",
				"Declaration1",
				"Declaration3",
				"DeclarationLocalVariable",
				"Declaration6",
				"DeclarationReturnType",
				"DeclarationParameter",
				"DeclarationInstanceVariable",
				"DeclarationVariableWithinMethod",
				"Exception"
		}));
		DICTIONARY_KEYS = new ArrayList<String>(TYPE_DICTIONARY.keySet());
	}

	public static DependencyDTO[] filterDependencies(DependencyDTO[] dtos) {
		for (int i = 0; i < dtos.length; i++)
			dtos[i] = editDTO(dtos[i]);
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
		for (int i = 0; i < dtos.length; i++)
			dtos[i] = editExternalSystemDTO(dtos[i]);
		return dtos;
	}

	private static ExternalSystemDTO editExternalSystemDTO(ExternalSystemDTO externalSystemDTO) {
		externalSystemDTO.fromDependencies = filterDependenciesInExternalSystemDTO(externalSystemDTO.fromDependencies);
		return externalSystemDTO;
	}

	private static DependencyDTO editDTO(DependencyDTO dto) {
		dto.type = getSimpleType(dto.type);
		return dto;
	}

	private static String getSimpleType(String type) {
		for (String simplenameAsKey : DICTIONARY_KEYS)
			if (TYPE_DICTIONARY.get(simplenameAsKey).contains(type))
				return simplenameAsKey;
		return null;
	}
}
