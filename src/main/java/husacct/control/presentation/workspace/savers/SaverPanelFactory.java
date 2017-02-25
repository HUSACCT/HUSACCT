package husacct.control.presentation.workspace.savers;


public class SaverPanelFactory {

	public static SaverPanel get(String identifier){
        try {
            ClassLoader myClassLoader = SaverPanelFactory.class.getClassLoader();
            String classNameToBeLoaded = "husacct.control.presentation.workspace.savers." + identifier + "SavePanel";
			Class<?> myClass = myClassLoader.loadClass(classNameToBeLoaded);
            Object instance = myClass.newInstance();
            return (SaverPanel) instance;
        } catch (SecurityException | IllegalAccessException | InstantiationException | ClassNotFoundException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        return new XmlSavePanel();
	}
}
