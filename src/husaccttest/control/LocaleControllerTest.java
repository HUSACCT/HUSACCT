package husaccttest.control;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import husacct.control.ControlServiceImpl;
import husacct.control.ILocaleChangeListener;
import husacct.control.task.LocaleController;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

public class LocaleControllerTest {

	private ControlServiceImpl service;
	
	private String translatedString = "JUnitTestValue";
	private String stringIdentifier = "ControlJUnitTestKey";
	private String stringIdentifierCopy = "ControlJUnitTestKey";
	
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
	public void testGetExistingStringIdentifiers(){
		List<String> keys = service.getStringIdentifiers(translatedString);
		assertTrue(keys.contains(stringIdentifier));
		assertTrue(keys.contains(stringIdentifierCopy));
	}
	
	@Test
	public void testGetNonExistingStringIdentifiers(){
		String nonExistingString = UUID.randomUUID().toString();
		List<String> keys = service.getStringIdentifiers(nonExistingString);
		assertFalse(keys.contains(nonExistingString));
	}
	
}
