package husaccttest.analyse.benchmarkLeo.domain;

import java.util.HashMap;

import static org.junit.Assert.*;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.benchmarkLeo.BenchmarkExtended;



public class Flickr extends BenchmarkExtended{

	public void testNavigation(){
		String from = "domain.flickr";
		
		int expectedchildmodules = 3;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
		
		HashMap<String, Object> flickrExpectedModule = createModuleHashmap(
				"Flickr", 
				from +  ".Flickr",
				0,
				super.CLASS);
		
		HashMap<String, Object> flickrpictureExpectedModule = createModuleHashmap(
				"FlickrPicture", 
				from +  ".FlickrPicture",
				0,
				super.CLASS);
		
		HashMap<String, Object> tagExpectedModule = createModuleHashmap(
				"Tag", 
				from +  ".Tag",
				0,
				super.CLASS);
		
		boolean foundflickr = compaireDTOWithValues(flickrExpectedModule, childModules);
		boolean foundflickrpicture = compaireDTOWithValues(flickrpictureExpectedModule, childModules);
		boolean foundtag = compaireDTOWithValues(tagExpectedModule, childModules);
		
		assertEquals(true, foundflickr);
		assertEquals(true, foundflickrpicture);
		assertEquals(true, foundtag);
	}
	
	public void testDomainFlickrFlickrPicture(){
		String from = "domain.flickr.FlickrPicture";
		int expectedDependencies = 8;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		super.printDependencies(dependencies);
		assertEquals(expectedDependencies, dependencies.length);

//		String fromImportExpected = from;
//		String toImportExpected = "infrastructure.blog.ILocation";
//		String typeImportExpected = super.IMPORT;
//		int linenumberImportExpected = 3;
//		
//		String fromImplementsExpected = from;
//		String toImplementsExpected = "infrastructure.blog.ILocation";
//		String typeImplementsExpected = super.IMPLEMENTS;
//		int linenumberImplementsExpected = 10;
//		
//		HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
//				fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
//		HashMap<String, Object> dependencyImplementsExpected = createDependencyHashmap(
//				fromImplementsExpected, toImplementsExpected, typeImplementsExpected, linenumberImplementsExpected);
//		
//		boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
//		boolean foundImplementsDependency = compaireDTOWithValues(dependencyImplementsExpected, dependencies);
//		
//		assertEquals(true, foundImportDependency);
//		assertEquals(true, foundImplementsDependency);
	}
}
