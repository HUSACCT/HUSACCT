package husaccttest.analyse.java;

import husacct.analyse.AnalyseServiceImpl;
import husacct.analyse.domain.famix.FamixCreationServiceImpl;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.*;

import static org.junit.Assert.*;

public abstract class TestCaseExtended{
	
	public String DECLARATION = "Declaration";
	public String EXTENDSCONCRETE = "ExtendsConcrete";
	public String EXTENDSABSTRACT = "ExtendsAbstract";
	public String EXTENDSINTERFACE = "ExtendsInterface";
	public String IMPLEMENTS = "Implements";
	public String IMPORT = "Import";
	public String INVOCCONSTRUCTOR = "InvocConstructor";
	public String INVOCMETHOD = "InvocMethod";
	public String EXCEPTION = "Exception";
	public String ANNOTATION = "Annotation";
	
	public String CLASS = "class";
	public String INTERFACE = "interface";
	public String PACKAGE = "package";
	
	
	private ArrayList<String> enabledDependencyTypes;
	private boolean isIndirectEnabled;
	
	

	protected AnalyseServiceImpl service;
	private FamixCreationServiceImpl famix;
	
	@Before
	public void setUp(){
		service = new AnalyseServiceImpl();
		famix = new FamixCreationServiceImpl();
			
		fillFamixAsStub();
	}
	
	@After
	public void tearDown(){
		famix.clearModel();
	}
	
	
	
	
	
	public void setConfig(){
		enabledDependencyTypes = new ArrayList<String>();
		enabledDependencyTypes.add(DECLARATION);
		enabledDependencyTypes.add(EXTENDSABSTRACT);
		enabledDependencyTypes.add(EXTENDSCONCRETE);
		enabledDependencyTypes.add(EXTENDSINTERFACE);
		enabledDependencyTypes.add(IMPLEMENTS);
		enabledDependencyTypes.add(IMPORT);
		enabledDependencyTypes.add(INVOCCONSTRUCTOR);
		enabledDependencyTypes.add(INVOCMETHOD);
		enabledDependencyTypes.add(EXCEPTION);
		enabledDependencyTypes.add(ANNOTATION);
		isIndirectEnabled = false;
	}
	
	public boolean MayDependencyBeChecked(DependencyDTO d){
		if(enabledDependencyTypes.contains(d.type)){
			return true;
		}
		if((d.isIndirect == true) && this.isIndirectEnabled){
			return true;
		}
		return false;		
	}
	
	public ArrayList<DependencyDTO> getDirectDependencies(DependencyDTO[] dependencies){
		ArrayList<DependencyDTO> directDependencies = new ArrayList<DependencyDTO>();
		for(DependencyDTO d : dependencies){
			if(isIndirectEnabled && d.isIndirect){
				directDependencies.add(d);
			} else {
				directDependencies.add(d);
			}
		}
		return directDependencies;
	}
	
	
	public ArrayList<DependencyDTO> getDependenciesByLinenumber(DependencyDTO currentDependency, ArrayList<DependencyDTO> filterTheseDependencies){
		ArrayList<DependencyDTO> filtered = new ArrayList<DependencyDTO>();
		for(DependencyDTO d : filterTheseDependencies){
			if(d.lineNumber == currentDependency.lineNumber){
				filtered.add(d);
			}
		}
		return filtered;
	}

	public ArrayList<DependencyDTO> getDependenciesByType(DependencyDTO currentDependency, ArrayList<DependencyDTO> filterTheseDependencies){
		ArrayList<DependencyDTO> filtered = new ArrayList<DependencyDTO>();
		for(DependencyDTO d : filterTheseDependencies){
			if(d.type.equals(currentDependency.type)){
				filtered.add(d);
			}
		}
		return filtered;
	}
	
	public ArrayList<DependencyDTO> getDependenciesByTo(DependencyDTO currentDependency, ArrayList<DependencyDTO> filterTheseDependencies){
		ArrayList<DependencyDTO> filtered = new ArrayList<DependencyDTO>();
		for(DependencyDTO d : filterTheseDependencies){
			if(d.to.equals(currentDependency.to)){
				filtered.add(d);
			}
		}
		return filtered;
	}
	
	public ArrayList<DependencyDTO> getDependenciesByFrom(DependencyDTO currentDependency, ArrayList<DependencyDTO> filterTheseDependencies){
		ArrayList<DependencyDTO> filtered = new ArrayList<DependencyDTO>();
		for(DependencyDTO d : filterTheseDependencies){
			if(d.from.equals(currentDependency.from)){
				filtered.add(d);
			}
		}
		return filtered;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public boolean itemExistInArray(Object value, Object[] items){
		for(Object o : items){
			if(o.toString().equals(value.toString())){
				return true;
			}
		}
		return false;
	}
	
	
	public HashMap<String, Object> createImportHashmap(String from, String to, int linenumber){
		String fromImportExpected = from;
		String toImportExpected = to;
		String typeImportExpected = this.IMPORT;
		int linenumberImportExpected = linenumber;
		
		HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
				fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
		
		return dependencyImportExpected;
	}
	
	
	public HashMap<String, Object> createDependencyHashmap(String from, String to, String type, int linenumber){
		HashMap<String, Object> dependencyHashMap = new HashMap<String, Object>();
		
		dependencyHashMap.put("from", from);
		dependencyHashMap.put("to", to);
		dependencyHashMap.put("type", type);
		dependencyHashMap.put("lineNumber", linenumber);
		
		return dependencyHashMap;
	}
	
	public HashMap<String, Object> createDependencyHashmap(String from, String to, String type, int linenumber, boolean indirect){
		HashMap<String, Object> dependencyHashMap = new HashMap<String, Object>();
		
		dependencyHashMap.put("from", from);
		dependencyHashMap.put("to", to);
		dependencyHashMap.put("type", type);
		dependencyHashMap.put("lineNumber", linenumber);
		dependencyHashMap.put("isIndirect", indirect);
		
		return dependencyHashMap;
	}
	
	
	public HashMap<String, Object> createModuleHashmap(String name, String uniqueName, int totalSubmodules, String type){
		HashMap<String, Object> moduleHashMap = new HashMap<String, Object>();
			
		moduleHashMap.put("name", name);
		moduleHashMap.put("uniqueName", uniqueName);
		moduleHashMap.put("subModules", totalSubmodules);
		moduleHashMap.put("type", type);
		
		return moduleHashMap;
	}
	
	public DependencyDTO[] getOnlyDirectDependencies(DependencyDTO[] dependencies){
		ArrayList<DependencyDTO> directDependencies = new ArrayList<DependencyDTO>();
		for(DependencyDTO d : dependencies){
			if(!d.isIndirect){
				directDependencies.add(d);
			}
		}
		
		DependencyDTO[] direct = new DependencyDTO[directDependencies.size()];
		int iterator = 0;
		for(DependencyDTO d : directDependencies){
			direct[iterator] = d;
			iterator++;
		}
		
		return direct;
	}
	
	
	public DependencyDTO[] getDependenciesFrom(String from){
		service = new AnalyseServiceImpl();
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
			            dependencies = this.getOnlyDirectDependencies(dependencies);
		return dependencies;
	}
	
	public boolean compaireDTOWithValues(Object o, Object[] allDependencies){

		@SuppressWarnings("unchecked")
		HashMap<String, Object> findingProperties = (HashMap<String, Object>) o;
		
		dependencyloop : for(Object currentDependency : allDependencies){
			for(String currentKey : findingProperties.keySet()){
								
				try {
					Class<? extends Object> objectPropertyClass = currentDependency.getClass();
					Field objectPropertyField = objectPropertyClass.getDeclaredField(currentKey);
														
					Object objectPropertyFieldValueObject = (Object) objectPropertyField.get(currentDependency);
					Object objectPropertyFieldValue;
					
					if(objectPropertyField.getType().getSimpleName().matches("List|Array")){
						if(objectPropertyFieldValueObject == null){
							objectPropertyFieldValue = (Integer) 0;
						} else {
								objectPropertyFieldValue = ((List<?>) objectPropertyFieldValueObject).size();	
						}
					} else {
						objectPropertyFieldValue = objectPropertyFieldValueObject.toString();
					}
					Object checkingObject = (Object) findingProperties.get(currentKey);
					Object checkingObjectValue = checkingObject.toString();
					
					//System.out.println(objectPropertyField.getName()+ " - " + objectPropertyFieldValue.toString() + " - " + checkingObjectValue.toString());
					
					if(!objectPropertyFieldValue.toString().equals(checkingObjectValue.toString())){
						continue dependencyloop;
					}
					
				} catch (Exception e) {
					return false;
				}

			}
			return true;
		}		
		return false;
	}	
	
	public boolean foundModulesNames(String[] search, AnalysedModuleDTO[] moduleList){
		nextSearch : for(String s : search){
			for(AnalysedModuleDTO d : moduleList){
				if(d.name.equals(s)){
					continue nextSearch;
				}
			}
			assertTrue(s + " not found", false);
			return false;
		}
		return true;
	}
	
	public AnalysedModuleDTO getModuleByName(String name, AnalysedModuleDTO[] modulelist){
		for(AnalysedModuleDTO d : modulelist){
			if(d.name.equals(name)){
				return d;
			}
		}
		return null;
	}
	
	private void createIndirectExtendsExtendsModel(){
		famix.createPackage("domain", "", "domain");
		famix.createPackage("domain.indirect", "domain", "direct");
		famix.createPackage("domain.indirect.violatingfrom", "domain.indirect", "violatingfrom");
		famix.createPackage("domain.indirect.intermediate", "domain.indirect", "intermediate");
		famix.createPackage("domain.indirect.indirectto", "domain.indirect", "indirectto");
		
		famix.createClass("domain.indirect.violatingfrom.InheritanceExtendsExtendsIndirect", 
					"InheritanceExtendsExtendsIndirect", 
					"domain.indirect.violatingfrom", false, false);
		famix.createClass("domain.indirect.intermediate.MapsService", "MapsService", 
					"domain.indirect.intermediate", false, false);
		famix.createClass("domain.indirect.indirectto.POI", "POI", 
					"domain.indirectindirectto", true, false);
		
		famix.createImport("domain.indirect.violatingfrom.InheritanceExtendsExtendsIndirect", "domain.indirect.intermediate.MapsService", 3,
					"domain.indirect.intermediate.MapsService", false);
		famix.createImport("domain.indirect.intermediate.MapsService", "domain.indirect.indirectto.POI", 3,
					"domain.indirect.indirectto.POI", false);
		
		famix.createInheritanceDefinition("domain.indirect.violatingfrom.InheritanceExtendsExtendsIndirect", 
					"domain.indirect.intermediate.MapsService", 5);
		famix.createInheritanceDefinition("domain.indirect.intermediate.MapsService", 
				"domain.indirect.indirectto.POI", 5);
		
		
		famix.createClass("abc.a", "a", "abc", false, false);
		famix.createClass("abc.b", "b", "abc", true, false);
		famix.createClass("abc.c", "c", "abc", false, false);
//		famix.createInheritanceDefinition("abc.c", "abc.b", 1);
		famix.createInheritanceDefinition("abc.b", "abc.a", 2);
		famix.createImplementsDefinition("abc.c", "abc.b", 2);
	}
	
	private void fillFamixAsStub(){
		createIndirectExtendsExtendsModel();
		famix.createPackage("domain", "", "domain");
		famix.createPackage("domain.locationbased", "domain", "locationbased");
		famix.createPackage("domain.locationbased.latitude", "domain.locationbased", "latitude");
		famix.createPackage("domain.locationbased.foursquare", "domain.locationbased", "foursquare");
		famix.createPackage("infrastructure", "", "infrastructure");
		famix.createPackage("infrastructure.socialmedia", "infrastructure", "socialmedia");
		famix.createPackage("infrastructure.socialmedia.locationbased", "infrastructure.socialmedia", "locationbased");
		famix.createPackage("infrastructure.socialmedia.locationbased.latitude", "infrastructure.socialmedia.locationbased", "latitude");
		famix.createPackage("infrastructure.socialmedia.locationbased.foursquare", "infrastructure.socialmedia.locationbased", "foursquare");
		
		famix.createPackage("indirect", "", "indirect");
		famix.createPackage("indirect.houses", "indirect", "houses");
		famix.createPackage("indirect.houses.type", "indirect.houses", "type");
		famix.createPackage("indirect.houses.attributes", "indirect.houses", "attributes");
				
		famix.createClass("domain.locationbased.foursquare.Account", "Account", "domain.locationbased.foursquare", false, false);
		famix.createClass("domain.locationbased.foursquare.Friends", "Friends", "domain.locationbased.foursquare", false, false);
		famix.createClass("domain.locationbased.foursquare.Map", "Map", "domain.locationbased.foursquare", false, false);
		famix.createClass("domain.locationbased.foursquare.History", "History", "domain.locationbased.foursquare", false, false);
		
		famix.createClass("domain.locationbased.latitude.Account", "Account", "domain.locationbased.latitude", false, false);
		famix.createClass("domain.locationbased.latitude.Friends", "Friends", "domain.locationbased.latitude", false, false);
		famix.createClass("domain.locationbased.latitude.Map", "Map", "domain.locationbased.latitude", false, false);
		
		famix.createClass("infrastructure.socialmedia.locationbased.foursquare.AccountDAO", "AccountDAO", "infrastructure.socialmedia.locationbased.foursquare", false, false);
		famix.createClass("infrastructure.socialmedia.locationbased.foursquare.FriendsDAO", "FriendsDAO", "infrastructure.socialmedia.locationbased.foursquare", true, false);
		famix.createClass("infrastructure.socialmedia.locationbased.foursquare.HistoryDAO", "HistoryDAO", "infrastructure.socialmedia.locationbased.foursquare", false, false);
		famix.createClass("infrastructure.socialmedia.locationbased.foursquare.IMap", "IMap", "infrastructure.socialmedia.locationbased.foursquare", false, false);
		
		famix.createClass("infrastructure.socialmedia.locationbased.latitude.AccountDAO", "AccountDAO", "infrastructure.socialmedia.locationbased.latitude", false, false);
		famix.createClass("infrastructure.socialmedia.locationbased.latitude.FriendsDAO", "FriendsDAO", "infrastructure.socialmedia.locationbased.latitude", true, false);
		famix.createClass("infrastructure.socialmedia.locationbased.latitude.IMap", "IMap", "infrastructure.socialmedia.locationbased.latitude", false, false);
		famix.createClass("infrastructure.socialmedia.locationbased.latitude.IMapp", "IMapp", "infrastructure.socialmedia.locationbased.latitude", false, false);
		
		famix.createClass("indirect.houses.type.IType", "IType", "indirect.houses.type", false, false, "", "default", true);
		famix.createClass("indirect.houses.type.IGlobal", "IGlobal", "indirect.houses.type", false, false, "", "default", true);
		famix.createClass("indirect.houses.type.RowHouse", "RowHouse", "indirect.houses.type", false, false);
		
		famix.createClass("indirect.houses.attributes.IAttribute", "IAttribute", "indirect.houses.attributes.Door", false, false, "", "default", true);
		famix.createClass("indirect.houses.attributes.Door", "Door", "indirect.houses.attributes", false, false);
		
		famix.createConstructorInvocation("domain.locationbased.foursquare.Account", "infrastructure.socialmedia.locationbased.foursquare.AccountDAO", 10, "Account", "AccountDAO()", "");
		famix.createInheritanceDefinition("domain.locationbased.foursquare.Friends", "infrastructure.socialmedia.locationbased.foursquare.FriendsDAO", 10);
		famix.createImplementsDefinition("domain.locationbased.foursquare.Map", "infrastructure.socialmedia.locationbased.foursquare.IMap", 10);
		famix.createInheritanceDefinition("domain.locationbased.foursquare.History", "infrastructure.socialmedia.locationbased.foursquare.HistoryDAO", 10);
		famix.createConstructorInvocation("domain.locationbased.latitude.Account", "infrastructure.socialmedia.locationbased.latitude.AccountDAO", 11, "Account", "AccountDAO()", "");
		famix.createInheritanceDefinition("domain.locationbased.latitude.Friends", "infrastructure.socialmedia.locationbased.latitude.FriendsDAO", 10);
		famix.createImplementsDefinition("domain.locationbased.latitude.Map", "infrastructure.socialmedia.locationbased.latitude.IMap", 10);
		famix.createImplementsDefinition("domain.locationbased.latitude.Mapp", "infrastructure.socialmedia.locationbased.latitude.IMap", 10);
		
		famix.createImport("domain.locationbased.foursquare.Account", "infrastructure.socialmedia.locationbased.foursquare.AccountDAO", 3, "infrastructure.socialmedia.locationbased.foursquare.AccountDAO", false);
		famix.createImport("domain.locationbased.foursquare.Friends", "infrastructure.socialmedia.locationbased.foursquare.FriendsDAO", 3, "infrastructure.socialmedia.locationbased.foursquare.FriendsDAO", false);
		famix.createImport("domain.locationbased.foursquare.Map", "infrastructure.socialmedia.locationbased.foursquare.IMap", 3, "infrastructure.socialmedia.locationbased.foursquare.IMap", false);
		famix.createImport("domain.locationbased.foursquare.History", "infrastructure.socialmedia.locationbased.foursquare.HistoryDAO", 3, "infrastructure.socialmedia.locationbased.foursquare.HistoryDAO", false);
		famix.createImport("domain.locationbased.latitude.Account", "infrastructure.socialmedia.locationbased.latitude.AccountDAO", 3, "infrastructure.socialmedia.locationbased.latitude.AccountDAO", false);
		famix.createImport("domain.locationbased.latitude.Friends", "infrastructure.socialmedia.locationbased.latitude.FriendsDAO", 3, "infrastructure.socialmedia.locationbased.latitude.FriendsDAO", false);
		famix.createImport("domain.locationbased.latitude.Map", "infrastructure.socialmedia.locationbased.latitude.IMap", 3, "infrastructure.socialmedia.locationbased.latitude.IMap", false);
		famix.createImport("domain.locationbased.latitude.Friends",  "System.Date.Calendar", 4, "System.Date.Calendar", false);
		
		famix.createImplementsDefinition("indirect.houses.type.RowHouse", "indirect.houses.type.IType", 3);
		famix.createInheritanceDefinition("indirect.houses.type.IType", "indirect.houses.type.IGlobal", 3);
		
		famix.createImplementsDefinition("indirect.houses.attributes.Door", "indirect.houses.attributes.IAttribute", 3);
		famix.createImport("indirect.houses.attributes.IAttribute", "indirect.houses.type.IType", 3, "indirect.houses.type.IType", false);
		famix.createInheritanceDefinition("indirect.houses.attributes.IAttribute", "indirect.houses.type.IType", 5);
		
		
		famix.executePostProcesses();
	}
	
	public void printDependencies(DependencyDTO[] dependencies){
		for(DependencyDTO d : dependencies){
			String indirect = d.isIndirect ? "indirect" : "direct";
			
			System.out.println(d.from + " -> " + d.to + " ( " + d.type + " | " + d.lineNumber + " | " + indirect +" )" );
		}
	}
	
}
