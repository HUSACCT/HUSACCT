package husaccttest.analyse.java.benchmark.domain;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import husacct.common.dto.DependencyDTO;
import husaccttest.analyse.java.benchmark.BenchmarkExtended;

public class LocationbasedFoursquare extends BenchmarkExtended{

	@Test 
	public void testEvent(){
		boolean AccessPropertyOrFieldFound = false;
		boolean importFound = false;
		boolean declerationFound = false;
		DependencyDTO[] dependencies = super.getDependenciesFrom("domain.locationbased.foursquare.Event");
		for (DependencyDTO dependency : dependencies){
			if(dependency.type.equals("Import")){
				importFound = true;
				assertEquals(3, dependency.lineNumber);
				assertEquals("infrastructure.socialmedia.locationbased.foursquare.EventDAO", dependency.to);
			}
			else if(dependency.type.equals("AccessPropertyOrField")){
				AccessPropertyOrFieldFound = true;
				assertEquals(13, dependency.lineNumber);
				assertEquals("infrastructure.socialmedia.locationbased.foursquare.EventDAO", dependency.to);
			}
			else if(dependency.type.equals("Declaration")){
				declerationFound = true;
				assertEquals(9, dependency.lineNumber);
				assertEquals("infrastructure.socialmedia.locationbased.foursquare.EventDAO", dependency.to);
			}
		}
		
		assertEquals(true, declerationFound);
		assertEquals(true, importFound);
		assertEquals(true, AccessPropertyOrFieldFound);
		
	}
}
