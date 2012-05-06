package husaccttest.control;

import static org.junit.Assert.assertEquals;
import husacct.control.ControlServiceImpl;
import husacct.control.ILocaleChangeListener;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class ControllerTest {

	ControlServiceImpl service;

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
		service.notifyLocaleListeners(Locale.ENGLISH);
	}

	@Test
	public void testDefaultLocale(){
		assertEquals(service.getLocale(), Locale.ENGLISH);
	}
	
}
