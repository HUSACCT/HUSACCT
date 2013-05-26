package husacct.common;

public class OSDetector {
	public enum OS {
		WINDOWS,
		LINUX,
		MAC
	};
	
	private static OS operatingSystem;

    static
    {
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win"))
        	operatingSystem = OS.WINDOWS;
        else if(os.contains("nux") || os.contains("nix"))
        	operatingSystem = OS.LINUX;
        else if(os.contains("mac"))
        	operatingSystem = OS.MAC;
    }

    public static OS getOS() {
    	return operatingSystem;
    }
    
    
}
