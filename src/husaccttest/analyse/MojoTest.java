package husaccttest.analyse;

import static org.junit.Assert.*;

import org.junit.BeforeClass;

import husacct.analyse.task.reconstruct.mojo.MoJo;
import husaccttest.TestResourceFinder;

import org.junit.Test;

public class MojoTest {
	private static MoJo mojoInstance;
	@BeforeClass
	public static void createObjects(){
		MoJo mojo = new MoJo();
		mojoInstance = mojo;
	}
	
	@Test
	public void MojoUnitTest() {
		//get the 2 files
		String goldenPath = TestResourceFinder.findMojoTestFile("mojo", "MojoTestGoldenStandard.rsf");
		String comparePath = TestResourceFinder.findMojoTestFile("mojo", "MojoTestToCompare.rsf");
		//use them in mojo
		double result = mojoInstance.executeMojo(new String[]{goldenPath, comparePath, "-fm"});
		double expected = 71.43;
		double maxDifference = 0;
		assertEquals(expected, result, maxDifference);
	}

	@Test
	public void MoJoTestFromMojoAuthor() {
		/*
		 * From README in the mojo.tar downloaded from http://www.cs.yorku.ca/~bil/downloads/ :
		 * The files distra.rsf and distrb.rsf are for testing purposes. 
		 * Aftersuccessful installation you should be able to give:
		 *   java mojo.MoJo distra.rsf distrb.rsf
		 * This should create the following output: 383
		 * This is the one-way MoJo distance from decomposition distra.rsf to distrb.rsf.
		 * There are also a number of options that allow ...
		 */
		//get the 2 files
		String goldenPath = TestResourceFinder.findMojoTestFile("mojo", "distra.rsf");
		String comparePath = TestResourceFinder.findMojoTestFile("mojo", "distrb.rsf");
		//use them in mojo
		
		double result = mojoInstance.executeMojo(new String[]{goldenPath, comparePath});
		double expected = 383;
		double maxDifference = 0;
		assertEquals(expected, result, maxDifference);
	}
	
	@Test
	public void MoJoFMTestFromMojoAuthor() {
		/*
		 * MoJoFM test based on the test files within the mojo.tar author.
		 */
		//get the 2 files
		String goldenPath = TestResourceFinder.findMojoTestFile("mojo", "distra.rsf");
		String comparePath = TestResourceFinder.findMojoTestFile("mojo", "distrb.rsf");
		//use them in mojo
		
		double result = mojoInstance.executeMojo(new String[]{goldenPath, comparePath, "-fm"});
		double expected = 58;
		double maxDifference = 0;
		assertEquals(expected, result, maxDifference);
	}

}
