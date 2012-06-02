package husaccttest.analyse.benchmarkjava.domain;

import java.util.HashMap;

import org.junit.Test;

import static org.junit.Assert.*;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.benchmarkjava.BenchmarkExtended;



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
		String from = "domain.blog.wordpress.MyBlah";
		int expectedDependencies = 2;
		
		//DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);

		String fromImportExpected = from;
		String toImportExpected = "infrastructure.blog.ILocation";
		String typeImportExpected = super.IMPORT;
		int linenumberImportExpected = 3;
		
		String fromImplementsExpected = from;
		String toImplementsExpected = "infrastructure.blog.ILocation";
		String typeImplementsExpected = super.IMPLEMENTS;
		int linenumberImplementsExpected = 10;
		
		HashMap<String, Object> dependencyImportExpected = createDependencyHashmap(
				fromImportExpected, toImportExpected, typeImportExpected, linenumberImportExpected);
		HashMap<String, Object> dependencyImplementsExpected = createDependencyHashmap(
				fromImplementsExpected, toImplementsExpected, typeImplementsExpected, linenumberImplementsExpected);
		
		boolean foundImportDependency = compaireDTOWithValues(dependencyImportExpected, dependencies);
		boolean foundImplementsDependency = compaireDTOWithValues(dependencyImplementsExpected, dependencies);
		
		assertEquals(true, foundImportDependency);
		assertEquals(true, foundImplementsDependency);
	}
	

	@Test
	public void testDomainBlogWordpressMyBlog(){
		String from = "domain.blog.wordpress.MyBlog";
		int expectedDependencies = 4;

		//DependencyDTO[] dependencies = getOnlyDirectDependencies(service.getDependenciesFrom(from));
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);

		HashMap<String, Object> dependencyImportBlogExpected = createDependencyHashmap(
				from,
				"infrastructure.blog.Blog",
				super.IMPORT,
				3);
		
		HashMap<String, Object> dependencyImportILocationExpected = createDependencyHashmap(
				from,
				"infrastructure.blog.ILocation",
				super.IMPORT,
				4);
		
		HashMap<String, Object> dependencyExtendsBlogExpected = createDependencyHashmap(
				from,
				"infrastructure.blog.Blog",
				super.EXTENDSABSTRACT,
				11);

		HashMap<String, Object> dependencyImplementsIlocationExpected = createDependencyHashmap(
				from,
				"infrastructure.blog.ILocation",
				super.IMPLEMENTS,
				11);
		
		
		boolean foundImportBlogExpected = compaireDTOWithValues(dependencyImportBlogExpected, dependencies);
		boolean foundImportILocationExpected = compaireDTOWithValues(dependencyImportILocationExpected, dependencies);
		boolean foundExtendsBlogExpected = compaireDTOWithValues(dependencyExtendsBlogExpected, dependencies);
		boolean foundImplementsIlocationExpected = compaireDTOWithValues(dependencyImplementsIlocationExpected, dependencies);
		
		assertEquals(true, foundImportBlogExpected);
		assertEquals(true, foundImportILocationExpected);
		assertEquals(true, foundExtendsBlogExpected);
		assertEquals(true, foundImplementsIlocationExpected);
	}	
	
	@Test
	public void testDomainBlogWordpressMyComment(){
		String from = "domain.blog.wordpress.MyComment";
		int expectedDependencies = 4;

		//DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);

		HashMap<String, Object> dependencyImportBlogExpected = createDependencyHashmap(
				from,
				"infrastructure.blog.Blog",
				super.IMPORT,
				3);
		
		HashMap<String, Object> dependencyImportILocationExpected = createDependencyHashmap(
				from,
				"infrastructure.blog.ILocation",
				super.IMPORT,
				4);
		
		HashMap<String, Object> dependencyExtendsBlogExpected = createDependencyHashmap(
				from,
				"infrastructure.blog.Blog",
				super.EXTENDSABSTRACT,
				11);

		HashMap<String, Object> dependencyImplementsIlocationExpected = createDependencyHashmap(
				from,
				"infrastructure.blog.ILocation",
				super.IMPLEMENTS,
				11);
		
		HashMap<String, Object> indirectMyitemExpected = createDependencyHashmap(
				from,
				"infrastructure.blog.MyItem",
				super.EXTENDSABSTRACT,
				3,
				true);
		
		boolean foundImportBlogExpected = compaireDTOWithValues(dependencyImportBlogExpected, dependencies);
		boolean foundImportILocationExpected = compaireDTOWithValues(dependencyImportILocationExpected, dependencies);
		boolean foundExtendsBlogExpected = compaireDTOWithValues(dependencyExtendsBlogExpected, dependencies);
		boolean foundImplementsIlocationExpected = compaireDTOWithValues(dependencyImplementsIlocationExpected, dependencies);
		
		assertEquals(true, foundImportBlogExpected);
		assertEquals(true, foundImportILocationExpected);
		assertEquals(true, foundExtendsBlogExpected);
		assertEquals(true, foundImplementsIlocationExpected);
	}	
	
	@Test
	public void testDomainBlogWordpressMyStory(){
		String from = "domain.blog.wordpress.MyStory";
		int expectedDependencies = 4;

		//DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		assertEquals(expectedDependencies, dependencies.length);

		HashMap<String, Object> dependencyImportBlogExpected = createDependencyHashmap(
				from,
				"infrastructure.blog.Blog",
				super.IMPORT,
				4);
		
		HashMap<String, Object> dependencyImportILocationExpected = createDependencyHashmap(
				from,
				"infrastructure.blog.ILocation",
				super.IMPORT,
				5);
		
		HashMap<String, Object> dependencyExtendsBlogExpected = createDependencyHashmap(
				from,
				"infrastructure.blog.Blog",
				super.EXTENDSABSTRACT,
				12);

		HashMap<String, Object> dependencyImplementsIlocationExpected = createDependencyHashmap(
				from,
				"infrastructure.blog.ILocation",
				super.IMPLEMENTS,
				12);
		
		HashMap<String, Object> indirectMyitemExpected = createDependencyHashmap(
				from,
				"infrastructure.blog.MyItem",
				super.EXTENDSABSTRACT,
				3,
				true);
		
		boolean foundImportBlogExpected = compaireDTOWithValues(dependencyImportBlogExpected, dependencies);
		boolean foundImportILocationExpected = compaireDTOWithValues(dependencyImportILocationExpected, dependencies);
		boolean foundExtendsBlogExpected = compaireDTOWithValues(dependencyExtendsBlogExpected, dependencies);
		boolean foundImplementsIlocationExpected = compaireDTOWithValues(dependencyImplementsIlocationExpected, dependencies);
		boolean foundIndirectMyitemExpected = compaireDTOWithValues(indirectMyitemExpected, dependencies);
		
		assertEquals(true, foundImportBlogExpected);
		assertEquals(true, foundImportILocationExpected);
		assertEquals(true, foundExtendsBlogExpected);
		assertEquals(true, foundImplementsIlocationExpected);
//		assertEquals(true, foundIndirectMyitemExpected);
	}	
	
	
}
