package husaccttest.control;

import static org.junit.Assert.assertEquals;
import husacct.control.ControlServiceImpl;
import husacct.control.ILocaleChangeListener;
import husacct.control.task.LocaleController;

import java.util.Locale;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class LocaleControllerTest {

	private ControlServiceImpl service;
	
	private String translatedString = "JUnitTestValue";
	private String stringIdentifier = "ControlJUnitTestKey";
	
	@Before
	public void setup(){
		 service = new ControlServiceImpl();
	}

	@Test
	public void testLocaleObserver(){
		service.addLocaleChangeListener(new ILocaleChangeListener() {
			@Override
			public void update(Locale newLocale) {
				assertEquals(newLocale.getLanguage(), "en");
			}
		});
		LocaleController localeController = service.getMainController().getLocaleController();
		localeController.setLocale(LocaleController.english);
	}

	@Test
	public void testDefaultLocale(){
		assertEquals(service.getLocale(), Locale.ENGLISH);
	}
	
	@Test
	public void testGetExistingTranslatedString(){
		String translated = service.getTranslatedString(stringIdentifier);
		assertEquals(translated, translatedString);
	}
	
	@Test
	public void testGetNonExistingTranslatedString(){
		String nonExistingKey = UUID.randomUUID().toString();
		String translatedString = service.getTranslatedString(nonExistingKey);
		assertEquals(translatedString, nonExistingKey);
	}

	@Test
	public void testConcurrentModification(){
		service.addLocaleChangeListener(new ILocaleChangeListener() {
			@Override
			public void update(Locale newLocale) {
				
				// Adding another listener while being notified should not raise a ConcurrentModificatinException
				service.addLocaleChangeListener(new ILocaleChangeListener() {
					@Override
					public void update(Locale newLocale) {
					}
				});
			}
		});
		LocaleController localeController = service.getMainController().getLocaleController();
		localeController.setLocale(LocaleController.dutch);
	}
}
