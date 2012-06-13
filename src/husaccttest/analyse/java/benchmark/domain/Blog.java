package husaccttest.analyse.java.benchmark.domain;

import java.util.HashMap;

import org.junit.Test;

import static org.junit.Assert.*;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.TestObject;
import husaccttest.analyse.java.benchmark.BenchmarkExtended;



public class Blog extends BenchmarkExtended{

	@Test
	public void testNavigation(){
		String from = "domain.blog";
		
		int expectedchildmodules = 1;
		AnalysedModuleDTO[] childModules = service.getChildModulesInModule(from);
		assertEquals(expectedchildmodules, childModules.length);
		
		from += ".wordpress";
		int expectedchildsClasses = 4;
		AnalysedModuleDTO[] childClasses = service.getChildModulesInModule(from);
		assertEquals(expectedchildsClasses, childClasses.length);
		
		HashMap<String, Object> myblahExpectedModule = createModuleHashmap(
				"MyBlah", 
				from +  ".MyBlah",
				0,
				super.CLASS);
		
		HashMap<String, Object> myblogExpectedModule = createModuleHashmap(
				"MyBlog", 
				from +  ".MyBlog",
				0,
				super.CLASS);
		
		HashMap<String, Object> mycommentExpectedModule = createModuleHashmap(
				"MyComment", 
				from +  ".MyComment",
				0,
				super.CLASS);
		
		HashMap<String, Object> mystoryExpectedModule = createModuleHashmap(
				"MyStory", 
				from +  ".MyStory",
				0,
				super.CLASS);
		
		boolean foundmyblah = compaireDTOWithValues(myblahExpectedModule, childClasses);
		boolean foundmyblog = compaireDTOWithValues(myblogExpectedModule, childClasses);
		boolean foundmycomment = compaireDTOWithValues(mycommentExpectedModule, childClasses);
		boolean foundmystory = compaireDTOWithValues(mystoryExpectedModule, childClasses);
		
		assertEquals(true, foundmyblah);
		assertEquals(true, foundmyblog);
		assertEquals(true, foundmycomment);
		assertEquals(true, foundmystory);
	}
	
	
	@Test
	public void testDomainBlogWordpressMyBlah(){
		TestObject testobject = new TestObject("domain.blog.wordpress.MyBlah");
		testobject.addDependency(new DependencyDTO("", "infrastructure.blog.ILocation", super.IMPORT, 3));
		testobject.addDependency(new DependencyDTO("", "infrastructure.blog.ILocation", super.IMPLEMENTS, 10));
		boolean result = super.testDependencyObject(testobject);
		assertTrue(testobject.getLastError(), result);
	}
	

	@Test
	public void testDomainBlogWordpressMyBlog(){
		TestObject testobject = new TestObject("domain.blog.wordpress.MyBlog");
		testobject.addDependency(new DependencyDTO("", "infrastructure.blog.Blog", super.IMPORT, 3));
		testobject.addDependency(new DependencyDTO("", "infrastructure.blog.ILocation", super.IMPORT, 4));
		testobject.addDependency(new DependencyDTO("", "infrastructure.blog.Blog", super.EXTENDSABSTRACT, 11));
		testobject.addDependency(new DependencyDTO("", "infrastructure.blog.ILocation", super.IMPLEMENTS, 11));
		boolean result = super.testDependencyObject(testobject);
		assertTrue(testobject.getLastError(), result);
	}	
	
	@Test
	public void testDomainBlogWordpressMyComment(){
		TestObject testobject = new TestObject("domain.blog.wordpress.MyComment");
		testobject.addDependency(new DependencyDTO("", "infrastructure.blog.Blog", super.IMPORT, 3));
		testobject.addDependency(new DependencyDTO("", "infrastructure.blog.ILocation", super.IMPORT, 4));
		testobject.addDependency(new DependencyDTO("", "infrastructure.blog.Blog", super.EXTENDSABSTRACT, 11));
		testobject.addDependency(new DependencyDTO("", "infrastructure.blog.ILocation", super.IMPLEMENTS, 11));
		boolean result = super.testDependencyObject(testobject);
		assertTrue(testobject.getLastError(), result);
	}	
	
	@Test
	public void testDomainBlogWordpressMyStory(){
		TestObject testobject = new TestObject("domain.blog.wordpress.MyStory");
		testobject.addDependency(new DependencyDTO("", "infrastructure.blog.Blog", super.IMPORT, 4));
		testobject.addDependency(new DependencyDTO("", "infrastructure.blog.ILocation", super.IMPORT, 5));
		testobject.addDependency(new DependencyDTO("", "infrastructure.blog.Blog", super.EXTENDSABSTRACT, 12));
		testobject.addDependency(new DependencyDTO("", "infrastructure.blog.ILocation", super.IMPLEMENTS, 12));
		boolean result = super.testDependencyObject(testobject);
		assertTrue(testobject.getLastError(), result);
	}	
	
	
}
