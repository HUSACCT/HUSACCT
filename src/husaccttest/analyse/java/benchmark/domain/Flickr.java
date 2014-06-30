package husaccttest.analyse.java.benchmark.domain;

import java.util.HashMap;

import org.junit.Test;

import static org.junit.Assert.*;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.java.TestObject;
import husaccttest.analyse.java.benchmark.BenchmarkExtended;



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
	
	@Test 
	public void testDomainFlickrFlickr(){
		TestObject testobject = new TestObject("domain.flickr.Flickr");
		boolean result = super.testDependencyObject(testobject);
		assertTrue(testobject.getLastError(), result);
	}
	
	@Test 
	public void testDomainFlickrFlickrPicture(){
		TestObject testobject = new TestObject("domain.flickr.FlickrPicture");
		testobject.addDependency(new DependencyDTO("", "", "java.util.List", "java.util.List", super.IMPORT, 3));
		testobject.addDependency(new DependencyDTO("", "", "domain.flickr.Tag", "domain.flickr.Flickr", super.EXTENDSABSTRACT, 5));
		testobject.addDependency(new DependencyDTO("", "", "java.util.List", "java.util.List", super.DECLARATION, 7));
		testobject.addDependency(new DependencyDTO("", "", "domain.flickr.Tag", "domain.flickr.Tag", super.DECLARATION, 7));
		testobject.addDependency(new DependencyDTO("", "", "java.util.List", "java.util.List", super.DECLARATION, 9));
		testobject.addDependency(new DependencyDTO("", "", "domain.flickr.Tag", "domain.flickr.Tag", super.DECLARATION, 9));
		testobject.addDependency(new DependencyDTO("", "", "java.util.List", "java.util.List", super.DECLARATION, 13));
		testobject.addDependency(new DependencyDTO("", "", "domain.flickr.Tag", "domain.flickr.Tag", super.DECLARATION, 13));
		boolean result = super.testDependencyObject(testobject);
		assertTrue(testobject.getLastError(), result);
	}
	
	
	@Test
	public void testDomainFlickrTag(){
		TestObject testobject = new TestObject("domain.flickr.Tag");
		testobject.addDependency(new DependencyDTO("", "", "infrastructure.socialmedia.SocialNetwork", "infrastructure.socialmedia.SocialNetwork", super.IMPORT, 3));
		testobject.addDependency(new DependencyDTO("", "", "infrastructure.socialmedia.SocialNetwork", "infrastructure.socialmedia.SocialNetwork", super.EXTENDSCONCRETE, 7));
		boolean result = super.testDependencyObject(testobject);
		assertTrue(testobject.getLastError(), result);
	}
	
}
