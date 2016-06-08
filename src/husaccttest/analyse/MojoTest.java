package husaccttest.analyse;

import static org.junit.Assert.*;

import org.junit.BeforeClass;

import husacct.analyse.task.reconstruct.mojo.MoJo;
import husaccttest.TestResourceFinder;

import org.junit.Test;

public class MojoTest {
	private static MoJo mojoTest;
	@BeforeClass
	public static void createObjects(){
		MoJo mojo = new MoJo();
		mojoTest = mojo;
	}
	
	@Test
	public void MojoUnitTest() {
		//get the 2 files
		String goldenPath = TestResourceFinder.findMojoTestFile("mojo", "MojoTestGoldenStandard.rsf");
		String comparePath = TestResourceFinder.findMojoTestFile("mojo", "MojoTestToCompare.rsf");
		//use them in mojo
		double result = mojoTest.executeMojo(new String[]{goldenPath, comparePath, "-fm"});
		double expected = 71.43;
		double maxDifference = 0;
		assertEquals(expected, result, maxDifference);
	}

}
