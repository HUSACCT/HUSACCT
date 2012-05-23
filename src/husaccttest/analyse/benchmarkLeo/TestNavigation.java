package husaccttest.analyse.benchmarkLeo;

import husacct.common.dto.AnalysedModuleDTO;

public class TestNavigation extends BenchmarkExtended{
	
	public void testRootModules(){
		String[] namesExpected = {"declarations", "domain", "exception", "extendsconcrete", "generics", "husacct", "infrastructure", "inpackage", "interfaces", "overpackages", "parentmoduletest", "presentation"};			
		AnalysedModuleDTO[] rootModules = service.getRootModules();
		
		assertEquals(namesExpected.length, rootModules.length);
		this.foundModulesNames(namesExpected, rootModules);
	}
	
	public void testNavigation(){
		String rootElement = "presentation";
		
		String[] expectedName = {"annotations", "exception", "gui", "legal", "post", "upload"};
		AnalysedModuleDTO[] childmodules1 = service.getChildModulesInModule(rootElement);
		assertEquals(expectedName.length, childmodules1.length);
		this.foundModulesNames(expectedName, childmodules1);
		
		String uniquenameChildModules2 = childmodules1[0].uniqueName;
		String[] expectedNames2 = {"flickr", "pinterest", "Upload"};
		AnalysedModuleDTO[] childmodules2 = service.getChildModulesInModule(uniquenameChildModules2);
		assertEquals(expectedNames2.length, childmodules2.length);
		this.foundModulesNames(expectedNames2, childmodules2);

		String uniquenameChildModules3 = this.getModuleByName("flickr", childmodules2).uniqueName;
		String[] expectedNamesChildModule3 = {"IFlickrUpload", "UploadFlickrPicture"};
		AnalysedModuleDTO[] childmodules3 = service.getChildModulesInModule(uniquenameChildModules3);
		assertEquals(expectedNamesChildModule3.length, childmodules3.length);
		this.foundModulesNames(expectedNamesChildModule3, childmodules3);
	}

}
