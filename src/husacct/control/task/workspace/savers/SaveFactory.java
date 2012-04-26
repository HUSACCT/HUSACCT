package husacct.control.task.workspace.savers;


import java.util.ArrayList;
import java.util.List;


public class SaveFactory {

	public static ISaveWorkspace getDefault(){
		List<String> list = getAvailableSavers();
		return get(list.get(0));
	}
	
	public static ISaveWorkspace get(String identifyer){
		if(isAvailable(identifyer)){
	        try {
	            ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();
	            String classNameToBeLoaded = "husacct.control.task.workspace.savers." + identifyer + "Saver";
	            Class<?> myClass = myClassLoader.loadClass(classNameToBeLoaded);
	            Object instance = myClass.newInstance();
	            return (ISaveWorkspace) instance;
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
	
	public static List<String> getAvailableSavers(){
		List<String> list = new ArrayList<String>();
		list.add("Xml");
		list.add("OracleDB");
		return list;
	}
	
	private static boolean isAvailable(String identifyer){
		List<String> list = getAvailableSavers();
		for(String loader : list){
			if(loader.equals(identifyer)){
				return true;
			}
		}
		return false;
	}

}
