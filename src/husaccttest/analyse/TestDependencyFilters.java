package husaccttest.analyse;

import java.util.HashMap;

import husacct.analyse.AnalyseServiceImpl;
import husacct.common.dto.DependencyDTO;

public class TestDependencyFilters extends TestCaseExtended{

	private AnalyseServiceImpl service;
	
	public void setUp(){
		service = new AnalyseServiceImpl();
	}

	public void testDependenciesFromToTypeInterfaces(){
		String from = "domain";
		String to   = "infrastructure";
		String[] dependencyFilter = {"interface"};
		int expectedDependencies = 2;
		DependencyDTO[] dependencies = service.getDependencies(from, to, dependencyFilter);
				
		assertEquals(expectedDependencies, dependencies.length);
		
		String foursquareMapFrom = "domain.locationbased.foursquare.Map";
		String foursquareMapTo = "infrastructure.socialmedia.locationbased.foursquare.IMap";
		String foursquareMapType = "Extends";
		int foursquareMapLine = 10;
		
		String latitudeMapFrom = "domain.locationbased.latitude.Map";
		String latitudeMapTo = "infrastructure.socialmedia.locationbased.latitude.IMap";
		String latitudeMapType = "Implements";
		int latitudeMapLine = 10;
		
		HashMap<String, Object> foursquareMapDependency = createDependencyHashmap(foursquareMapFrom, foursquareMapTo, foursquareMapType, foursquareMapLine);
		HashMap<String, Object> latitudeMapDependency = createDependencyHashmap(latitudeMapFrom, latitudeMapTo, latitudeMapType, latitudeMapLine);
		
		boolean foundFoursquareMap = compaireDTOWithValues(foursquareMapDependency, dependencies);
		boolean foundLatitudeMap = compaireDTOWithValues(latitudeMapDependency, dependencies);
		
		assertEquals(true, foundFoursquareMap);
		assertEquals(true, foundLatitudeMap);
	}
	
	public void testDependenciesFromTypeInterfaces(){
		String from = "domain";
		String[] dependencyFilter = {"interface"};
		int expectedDependencies = 2;
		DependencyDTO[] dependencies = service.getDependenciesFrom(from, dependencyFilter);

		assertEquals(expectedDependencies, dependencies.length);
		
		String foursquareMapFrom = "domain.locationbased.foursquare.Map";
		String foursquareMapTo = "infrastructure.socialmedia.locationbased.foursquare.IMap";
		String foursquareMapType = "Extends";
		int foursquareMapLine = 10;
		
		String latitudeMapFrom = "domain.locationbased.latitude.Map";
		String latitudeMapTo = "infrastructure.socialmedia.locationbased.latitude.IMap";
		String latitudeMapType = "Implements";
		int latitudeMapLine = 10;
		
		HashMap<String, Object> foursquareMapDependency = createDependencyHashmap(foursquareMapFrom, foursquareMapTo, foursquareMapType, foursquareMapLine);
		HashMap<String, Object> latitudeMapDependency = createDependencyHashmap(latitudeMapFrom, latitudeMapTo, latitudeMapType, latitudeMapLine);
		
		boolean foundFoursquareMap = compaireDTOWithValues(foursquareMapDependency, dependencies);
		boolean foundLatitudeMap = compaireDTOWithValues(latitudeMapDependency, dependencies);
		
		assertEquals(true, foundFoursquareMap);
		assertEquals(true, foundLatitudeMap);
	}
	
	public void testDependenciesToTypeInterfaces(){
		String to   = "infrastructure";
		String[] dependencyFilter = {"interface"};
		int expectedDependencies = 2;
		DependencyDTO[] dependencies = service.getDependenciesTo(to, dependencyFilter);
		
		assertEquals(expectedDependencies, dependencies.length);
		
		String foursquareMapFrom = "domain.locationbased.foursquare.Map";
		String foursquareMapTo = "infrastructure.socialmedia.locationbased.foursquare.IMap";
		String foursquareMapType = "Extends";
		int foursquareMapLine = 10;
		
		String latitudeMapFrom = "domain.locationbased.latitude.Map";
		String latitudeMapTo = "infrastructure.socialmedia.locationbased.latitude.IMap";
		String latitudeMapType = "Implements";
		int latitudeMapLine = 10;
		
		HashMap<String, Object> foursquareMapDependency = createDependencyHashmap(foursquareMapFrom, foursquareMapTo, foursquareMapType, foursquareMapLine);
		HashMap<String, Object> latitudeMapDependency = createDependencyHashmap(latitudeMapFrom, latitudeMapTo, latitudeMapType, latitudeMapLine);
		
		boolean foundFoursquareMap = compaireDTOWithValues(foursquareMapDependency, dependencies);
		boolean foundLatitudeMap = compaireDTOWithValues(latitudeMapDependency, dependencies);
		
		assertEquals(true, foundFoursquareMap);
		assertEquals(true, foundLatitudeMap);
	}
	
}

