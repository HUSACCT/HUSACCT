package husacct.define.presentation.utils;

import husacct.ServiceProvider;



public class DefaultMessages {
	/**
	 * Add default messages / tooltips starting with MSG_ or TIP_
	 */
	public static final String MSG = "";
	public static final String TIP_MODULE = ServiceProvider.getInstance().getLocaleService().getTranslatedString("NameOfTheModule");
	public static final String TIP_MODULEDESCRIPTION = ServiceProvider.getInstance().getLocaleService().getTranslatedString("DescriptionOfTheModule");
	public static final String TIP_FACADE = ServiceProvider.getInstance().getLocaleService().getTranslatedString("FacadeAccessible");
	public static final String TIP_REGEXLANGUAGE = ServiceProvider.getInstance().getLocaleService().getTranslatedString("FacadeAccessible");
	
	public static final String MSG_NOARCHITECTURENAME = ServiceProvider.getInstance().getLocaleService().getTranslatedString("NoArchitectureName");


}
