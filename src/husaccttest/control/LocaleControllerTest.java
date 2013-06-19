package husaccttest.control;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import husacct.common.locale.ILocaleService;
import husacct.common.locale.LocaleServiceImpl;
import husacct.common.services.IServiceListener;

import java.util.Locale;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class LocaleControllerTest {

	private ILocaleService localeService;
	
	private String translatedString = "JUnitTestValue";
	private String stringIdentifier = "ControlJUnitTestKey";
	
	@Before
	public void setup(){
		 localeService = new LocaleServiceImpl();
	}

	@Test
	public void testLocaleObserver(){
		localeService.addServiceListener(new IServiceListener() {
			@Override
			public void update() {
				assertEquals(true, true);
			}
		});
		localeService.notifyServiceListeners();
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
		assertEquals(translatedString, nonExistingKey);
	}

	@Test
	public void testConcurrentModification(){
		localeService.addServiceListener(new IServiceListener() {
			@Override
			public void update() {
				
				// Adding another listener while being notified should not raise a ConcurrentModificatinException
				localeService.addServiceListener(new IServiceListener() {
					@Override
					public void update() {
					}
				});
			}
		});
		localeService.notifyServiceListeners();
	}
}
