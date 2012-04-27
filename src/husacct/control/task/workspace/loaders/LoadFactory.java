package husacct.control.task.workspace.loaders;


import java.util.ArrayList;
import java.util.List;


public class LoadFactory {

	public static ILoadWorkspace getDefault(){
		List<String> list = getAvailableLoaders();
		return get(list.get(0));
	}
	
	public static ILoadWorkspace get(String identifier){
		if(isAvailable(identifier)){
	        try {
	            ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();
	            String classNameToBeLoaded = "husacct.control.task.workspace.loaders." + identifier + "Loader";
	            Class<?> myClass = myClassLoader.loadClass(classNameToBeLoaded);
	            Object instance = myClass.newInstance();
	            return (ILoadWorkspace) instance;
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
		}
		return getDefault();
	}
	
	public static List<String> getAvailableLoaders(){
		List<String> list = new ArrayList<String>();
		list.add("Xml");
		return list;
	}
	
	private static boolean isAvailable(String identifier){
		List<String> list = getAvailableLoaders();
		for(String loader : list){
			if(loader.equals(identifier)){
				return true;
			}
		}
		return false;
	}

}
