package husacct.control.task.workspace;



import java.util.ArrayList;
import java.util.List;


public class ResourceFactory {

	public static IWorkspaceResource getDefault(){
		List<String> list = getAvailableResources();
		return get(list.get(0));
	}
	
	public static IWorkspaceResource get(String identifier){
		if(isAvailable(identifier)){
	        try {
	            ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();
	            String classNameToBeLoaded = "husacct.control.task.workspace.resources." + identifier + "WorkspaceResource";
	            Class<?> myClass = myClassLoader.loadClass(classNameToBeLoaded);
	            Object instance = myClass.newInstance();
	            return (IWorkspaceResource) instance;
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
	
	public static List<String> getAvailableResources(){
		List<String> list = new ArrayList<String>();
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
