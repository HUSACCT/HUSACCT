package husacct.common;

import java.net.URL;

import common.Logger;

public class Resource {
	private static Logger logger = Logger.getLogger(Resource.class);







	



	

	

	public static final String RESOURCE_PATH = "/husacct/common/resources/";
	public static final String LOCALE_PATH = RESOURCE_PATH + "locale/";
	public static final String IMAGE_PATH = RESOURCE_PATH + "image/";
	public static final String ICONSET_PATH = RESOURCE_PATH + "iconset/";
	
	public static final String HUSACCT_LOGO = IMAGE_PATH + "husacct.png";
	public static final String BLACKCAT = IMAGE_PATH + "blackcat.gif";
	
	public static final String ICON_BACK = ICONSET_PATH + "back.png";
	public static final String ICON_ANALYSED_ARCHITECTURE_DIAGRAM = ICONSET_PATH + "analysed-architecture-diagram.png";
	public static final String ICON_APPLICATION_OVERVIEW = ICONSET_PATH + "application-overview.png";
	public static final String ICON_APPLICATION_PROPERTIES = ICONSET_PATH + "application-properties.png";
	public static final String ICON_CLASS_PRIVATE = ICONSET_PATH + "class-private.png";
	public static final String ICON_CLASS_PUBLIC = ICONSET_PATH + "class-public.png";
	public static final String ICON_COMPONENT = ICONSET_PATH + "component.png";
	public static final String ICON_DEFINE_ARCHITECTURE = ICONSET_PATH + "define-architecture.png";
	public static final String ICON_DEFINE_ARCHITECTURE_DIAGRAM = ICONSET_PATH + "defined-architecture-diagram.png";
	public static final String ICON_DEPENDENCIES_ACTIVE = ICONSET_PATH + "dependencies-active.png";
	public static final String ICON_DEPENDENCIES_INACTIVE = ICONSET_PATH + "dependencies-inactive.png";
	public static final String ICON_ENUMERATION = ICONSET_PATH + "enumeration.png";
	public static final String ICON_FIGURES_HIDE = ICONSET_PATH + "figures-hide.png";
	public static final String ICON_FIGURES_SHOW = ICONSET_PATH + "figures-show.png";
	public static final String ICON_INTERFACE_PRIVATE = ICONSET_PATH + "interface-private.png";
	public static final String ICON_INTERFACE_PUBLIC = ICONSET_PATH + "interface-public.png";
	public static final String ICON_LAYER = ICONSET_PATH + "define-layer.png";
	public static final String ICON_LIBRARY = ICONSET_PATH + "library.png";
	public static final String ICON_MODULE = ICONSET_PATH + "module.png";
	public static final String ICON_NEW = ICONSET_PATH + "new.png";
	public static final String ICON_OPEN = ICONSET_PATH + "open.png";
	public static final String ICON_OPTIONS = ICONSET_PATH + "options.png";
	public static final String ICON_OUTOFDATE = ICONSET_PATH + "outofdate.png";
	public static final String ICON_PACKAGE = ICONSET_PATH + "package.png";
	public static final String ICON_REFRESH = ICONSET_PATH + "refresh.png";
	public static final String ICON_SAVE = ICONSET_PATH + "save.png";
	public static final String ICON_SOFTWARE_ARCHITECTURE = ICONSET_PATH + "software-architecture.png";
	public static final String ICON_SOURCE = ICONSET_PATH + "source.png";
	public static final String ICON_SUBSYSTEM = ICONSET_PATH + "subsystem.png";
	public static final String ICON_VALIDATE = ICONSET_PATH + "validate.png";
	public static final String ICON_VIOLATIONS_ACTIVE = ICONSET_PATH + "violations-active.png";
	public static final String ICON_VIOLATIONS_INACTIVE = ICONSET_PATH + "violations-inactive.png";
	public static final String ICON_ZOOM = ICONSET_PATH + "zoom.png";
	public static final String ICON_EXTERNALLIB=ICONSET_PATH+"define_externalLib.png";
	public static final String ICON_SUBSYSTEMJAVA=ICONSET_PATH+"define-subsystem.png";
	public static final String CONTROL_TEST_WORKSPACE = RESOURCE_PATH + "control/testworkspace.xml";
	public static final String ICON_ROOT=ICONSET_PATH+"define-root.png";
	public static final String ICON_PACKAGE_EMPTY=ICONSET_PATH+"define-emptypackage.png";
	public static String ICON_FACADE=ICONSET_PATH+"define-facade.png";;
	
	public static URL get(String path){
		URL resourceUrl = null;
		try {
			resourceUrl = Resource.class.getResource(path);
		} catch (Exception exception){
			logger.error(String.format("Unable to find resource %s: %s", path, exception.getMessage()));
		}
		return resourceUrl;
	}
}