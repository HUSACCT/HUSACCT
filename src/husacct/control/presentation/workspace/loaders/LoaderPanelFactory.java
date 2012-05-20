package husacct.control.presentation.workspace.loaders;


public class LoaderPanelFactory {

	public static LoaderPanel get(String identifier){
        try {
            ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();
            String classNameToBeLoaded = "husacct.control.presentation.workspace.loaders." + identifier + "LoadPanel";
			Class<?> myClass = myClassLoader.loadClass(classNameToBeLoaded);
            Object instance = myClass.newInstance();
            return (LoaderPanel) instance;
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
        
        return new XmlLoadPanel();
	}
}
