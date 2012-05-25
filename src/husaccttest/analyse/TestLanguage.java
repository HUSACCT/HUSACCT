package husaccttest.analyse;

public class TestLanguage extends TestCaseExtended{
	
	public void testGetAvailableLanguages(){
		int totalLanguagesExpected = 2;
		String[] availableLanguages = service.getAvailableLanguages();
		
		assertEquals(totalLanguagesExpected, availableLanguages.length);
		assertEquals(true, itemExistInArray("Java", availableLanguages));
		assertEquals(true, itemExistInArray("C#", availableLanguages));
	}
		
}

