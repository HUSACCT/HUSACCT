package husaccttest.analyse;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestLanguage extends TestCaseExtended{
	
	@Test
	public void testGetAvailableLanguages(){
		int totalLanguagesExpected = 2;
		String[] availableLanguages = service.getAvailableLanguages();
		
		assertEquals(totalLanguagesExpected, availableLanguages.length);
		assertEquals(true, itemExistInArray("Java", availableLanguages));
		assertEquals(true, itemExistInArray("C#", availableLanguages));
	}
		
}

