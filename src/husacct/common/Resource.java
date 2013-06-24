package husacct.common;

import java.io.InputStream;
import java.net.URL;

import common.Logger;

public class Resource {
	private static Logger logger = Logger.getLogger(Resource.class);

	public static final String RESOURCE_PATH = "/husacct/common/resources/";
	public static final String LOCALE_PATH = RESOURCE_PATH + "locale/";
	public static final String LOGGING_PATH = RESOURCE_PATH + "logging/";
	public static final String IMAGE_PATH = RESOURCE_PATH + "image/";
	public static final String ICONSET_PATH = RESOURCE_PATH + "iconset/";
	public static final String CREDITS_PATH = RESOURCE_PATH +"credits/";
	public static final String HELP_PATH = RESOURCE_PATH +"help/";
	public static final String HELP_PAGES_PATH = HELP_PATH +"pages/";
	public static final String HELP_IMAGE_PATH = HELP_PATH +"image/";
	public static final String test_page = HELP_PAGES_PATH + "about.html";

	public static final String HUSACCT_LOGO = IMAGE_PATH + "husacct.png";
	public static final String BLACKCAT = IMAGE_PATH + "blackcat.gif";
	public static final String DEFINE_WORKFLOW = IMAGE_PATH + "define_workflow.png";
	public static final String BIG_YOUTUBE_LOGO = IMAGE_PATH + "yt-brand-standard-logo-630px.png";

	public static final String GIT_FORK_1 = IMAGE_PATH +"fork_1.png";
	public static final String GIT_FORK_2 = IMAGE_PATH +"fork_2.png";
	public static final String GIT_FORK_3 = IMAGE_PATH +"fork_3.png";
	public static final String GIT_FORK_4 = IMAGE_PATH +"fork_4.png";

	public static final String SMALL_YOUTUBE_LOGO = IMAGE_PATH + "yt-brand-standard-logo-95px.png";
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
	public static final String ICON_ZOOMCONTEXT = ICONSET_PATH + "zoom-in.png";
	public static final String ICON_EXTERNALLIB=ICONSET_PATH+"define_externalLib.png";
	public static final String ICON_SUBSYSTEMJAVA=ICONSET_PATH+"define-subsystem.png";
	public static final String CONTROL_TEST_WORKSPACE = RESOURCE_PATH + "control/testworkspace.xml";
	public static final String ICON_ROOT=ICONSET_PATH+"define-root.png";
	public static final String ICON_PACKAGE_EMPTY=ICONSET_PATH+"define-emptypackage.png";
	public static final String ICON_REGEX = ICONSET_PATH+"define-regex.png";
	public static final String ICON_REGEXSEARCH =ICONSET_PATH+"define-search.png";
	public static final String ICON_QUESTIONMARK =ICONSET_PATH+"question_mark.png";
	public static final String ICON_EXTERNALLIB2 =ICONSET_PATH+"define-externallib.png";
	public static final String ICON_CLASS_PUBLIC_GRAY = ICONSET_PATH + "class-public-gray.png";
	public static final String ICON_INTERFACE_PUBLIC_GRAY = ICONSET_PATH + "interface-public-gray.png";
	public static final String ICON_ENUMERATION_GRAY = ICONSET_PATH + "enumeration-gray.png";
	public static final String ICON_EXTERNALLIB_GRAY =ICONSET_PATH+"define_externalLib-gray.png";
	public static final String ICON_SUBSYSTEMJAVA_GRAY = ICONSET_PATH+"define-subsystem-gray.png";
	public static final String ICON_EXTERNALLIB2_GRAY = ICONSET_PATH+"define-externallib-gray.png";
	public static final String ICON_PAN_TOOL = ICONSET_PATH
			+ "icon-hand-clean-hi.png";
	public static final String ICON_SELECT_TOOL = ICONSET_PATH
			+ "mouse-cursor-icon.jpg";
	public static String ICON_FACADE = ICONSET_PATH + "define-facade.png";
	public static String WINDOW = IMAGE_PATH + "window.wav";

	public static URL get(String path){
		URL resourceUrl = null;
		try {
			resourceUrl = Resource.class.getResource(path);
		} catch (Exception exception){
			path = path.substring(1);
			resourceUrl = Resource.class.getResource(path);
			logger.error(String.format("Unable to find resource %s: %s", path, exception.getMessage()));
		}
		return resourceUrl;
	}
	
	public static InputStream getStream(String path) {
		
		return Resource.class.getResourceAsStream(path);
	}

}
