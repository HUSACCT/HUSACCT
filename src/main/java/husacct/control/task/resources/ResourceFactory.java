package husacct.control.task.resources;

import java.util.ArrayList;
import java.util.List;


public class ResourceFactory {

	private static IResource getDefault(){
		List<String> list = getAvailableResources();
		return get(list.get(0));
	}
	
	public static IResource get(String identifier){
		if(isAvailable(identifier)){
	        try {
	            ClassLoader myClassLoader = ResourceFactory.class.getClassLoader();
	            String classNameToBeLoaded = "husacct.control.task.resources." + identifier + "Resource";
	            Class<?> myClass = myClassLoader.loadClass(classNameToBeLoaded);
	            Object instance = myClass.newInstance();
	            return (IResource) instance;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		return getDefault();
	}
	
	public static List<String> getAvailableResources(){
		List<String> list = new ArrayList<>();
		list.add("Xml");
		return list;
	}
	
	private static boolean isAvailable(String identifier){
		List<String> list = getAvailableResources();
		for(String loader : list){
			if(loader.equals(identifier)){
				return true;
			}
		}
		return false;
	}

}
