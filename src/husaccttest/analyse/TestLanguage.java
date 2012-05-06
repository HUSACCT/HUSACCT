package husaccttest.analyse;

import husacct.analyse.AnalyseServiceImpl;

public class TestLanguage extends TestCaseExtended{

	private AnalyseServiceImpl service;
	
	public void setUp(){
		service = new AnalyseServiceImpl();
	}
	
	public void testGetAvailableLanguages(){
		int totalLanguagesExpected = 2;
		String[] availableLanguages = service.getAvailableLanguages();
		
		assertEquals(totalLanguagesExpected, availableLanguages.length);
		assertEquals(true, itemExistInArray("Java", availableLanguages));
		assertEquals(true, itemExistInArray("C#", availableLanguages));
	}
		
}

