package husaccttest.control;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import husacct.common.locale.ILocaleService;
import husacct.common.locale.LocaleServiceImpl;
import husacct.common.services.IServiceListener;
import husacct.control.task.configuration.ConfigurationManager;
import java.net.URL;
import java.util.Locale;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

public class LocaleControllerTest {

	private ILocaleService localeService;
	private String translatedString = "JUnitTestValue";
	private String stringIdentifier = "ControlJUnitTestKey";
	private static Logger logger;
	
	@Before
	public void setup(){
		localeService = new LocaleServiceImpl();
		setLog4jConfiguration();
	}

	private static void setLog4jConfiguration() {
		URL propertiesFile = Class.class.getResource("/husacct/common/resources/log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
		logger = Logger.getLogger(LocaleControllerTest.class);
	}

	@Test
	public void testLocaleObserver(){
		localeService.addServiceListener(() -> assertEquals(true, true));
		localeService.notifyServiceListeners();
	}
	
	@Test
	public void testConfigLocale() {
		Locale configLocale = new Locale(ConfigurationManager.getProperty("Language"), ConfigurationManager.getProperty("Language"));
		Locale currentLocale = localeService.getLocale();
		assertEquals(configLocale, currentLocale);
	}
	
	@Test
	public void testSetExistingLocale(){
		Locale testLocale = new Locale("nl", "NL");
		localeService.setLocale(testLocale);
		Locale newLocale = localeService.getLocale();
		assertSame(testLocale, newLocale);
	}
	
	@Test
	public void testSetNonExistingLocale(){
		logger.info("Executing test on Locale service");
		Locale oldLocale = localeService.getLocale(); 
		Locale testLocale = new Locale("xx", "xx");
		localeService.setLocale(testLocale);
		Locale newLocale = localeService.getLocale();
		assertSame(oldLocale, newLocale);
	}
	
	@Test
	public void testGetExistingTranslatedString(){
		String translated = localeService.getTranslatedString(stringIdentifier);
		assertEquals(translated, translatedString);
	}
	
	@Test
	public void testGetNonExistingTranslatedString(){
		String nonExistingKey = UUID.randomUUID().toString();
		String translatedString = localeService.getTranslatedString(nonExistingKey);
		String expectedResult = nonExistingKey + " (Missing resource in Locale service)";
		assertEquals(translatedString, expectedResult);
	}

	@Test
	public void testConcurrentModification(){
		localeService.addServiceListener(() -> {

            // Adding another listener while being notified should not raise a ConcurrentModificatinException
            localeService.addServiceListener(() -> {
});
        });
		localeService.notifyServiceListeners();
	}
}
