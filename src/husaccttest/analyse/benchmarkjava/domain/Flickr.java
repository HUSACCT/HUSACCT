package husaccttest.analyse.benchmarkjava.domain;

import java.util.HashMap;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.benchmarkjava.BenchmarkExtended;



public class Flickr extends BenchmarkExtended{

	@Test
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
	
	@Ignore ("Needed dependencies are not detected yet")
	@Test 
	public void testDomainFlickrFlickrPicture(){
		String from = "domain.flickr.FlickrPicture";
		int expectedDependencies = 8;
		
		//DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = super.getDependenciesFrom(from);
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
	
	
	@Test
	public void testDomainFlickrTag(){
		String from = "domain.flickr.Tag";
		int expectedDependencies = 2;
		
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);
		
		String fromImportExpected = from;
		String toImportExpected = "infrastructure.socialmedia.SocialNetwork";
		String typeImportExpected = super.IMPORT;
		int linenumberImportExpected = 3;
		
		String fromExtendsExpected = from;
		String toExtendsExpected = "infrastructure.socialmedia.SocialNetwork";
		String typeExtendsExpected = super.EXTENDSCONCRETE;
		int linenumberExtendsExpected = 7;
			
		HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
				fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
		HashMap<String, Object> dependencyExtendsExpected = createDependencyHashmap(
				fromExtendsExpected, toExtendsExpected, typeExtendsExpected, linenumberExtendsExpected);
		
		boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
		boolean foundExtendsDependency = compaireDTOWithValues(dependencyExtendsExpected, dependencies);
		assertEquals(true, foundImportDependency);
		assertEquals(true, foundExtendsDependency);
		
		
		HashMap<String, Object> indirectStringExpected = createDependencyHashmap(
				from,
				"String",
				super.DECLARATION,
				4,
				true);
		
		HashMap<String, Object> indirectObjectExpected = createDependencyHashmap(
				from,
				"String",
				super.DECLARATION,
				4,
				true);
		
		boolean foundindirectString = compaireDTOWithValues(indirectStringExpected, dependencies);
		boolean foundindirectObject = compaireDTOWithValues(indirectObjectExpected, dependencies);
//		assertEquals(true, foundindirectString);
//		assertEquals(true, foundindirectObject);
		
		
		
	}
	
}
