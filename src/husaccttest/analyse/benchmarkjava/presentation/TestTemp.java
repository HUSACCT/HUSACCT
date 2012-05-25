package husaccttest.analyse.benchmarkjava.presentation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.benchmarkjava.BenchmarkExtended;
import husaccttest.analyse.recognitiontest.RecognationExtended;

public class TestTemp extends BenchmarkExtended{

	
	@Test
	public void testDomainBlogWordpressMyBlog(){
		String from = "presentation.gui.observer.hyves.StartHyvesMediaGUI";
		int expectedDependencies = 6;

		DependencyDTO[] dependencies = service.getDependenciesFrom(from);
		super.printDependencies(dependencies);
		assertEquals(expectedDependencies, dependencies.length);

	}
	
}
