package husacct.control.presentation.workspace.savers;


public class SaverPanelFactory {

	public static SaverPanel get(String identifier){
        try {
            ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();
            String classNameToBeLoaded = "husacct.control.presentation.workspace.savers." + identifier + "SavePanel";
			Class<?> myClass = myClassLoader.loadClass(classNameToBeLoaded);
            Object instance = myClass.newInstance();
            return (SaverPanel) instance;
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
        
        return new XmlSavePanel();
	}
}
