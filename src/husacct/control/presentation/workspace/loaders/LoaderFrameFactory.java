package husacct.control.presentation.workspace.loaders;


public class LoaderFrameFactory {

	public static ILoaderFrame get(String identifier){
        try {
            ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();
            String classNameToBeLoaded = "husacct.control.presentation.workspace.loaders." + identifier + "LoadFrame";
            Class<?> myClass = myClassLoader.loadClass(classNameToBeLoaded);
            Object instance = myClass.newInstance();
            return (ILoaderFrame) instance;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        
        return new XmlLoadFrame();
	}
}
