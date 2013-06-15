package husacct.control.presentation.workspace.loaders;


public class LoaderPanelFactory {

	public static LoaderPanel get(String identifier){
        try {
            ClassLoader myClassLoader = LoaderPanelFactory.class.getClassLoader();
            String classNameToBeLoaded = "husacct.control.presentation.workspace.loaders." + identifier + "LoadPanel";
			Class<?> myClass = myClassLoader.loadClass(classNameToBeLoaded);
            Object instance = myClass.newInstance();
            return (LoaderPanel) instance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return new HusacctLoadPanel();
	}
}
