package husacct.build;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BuildWrapper {
	
	private String initialHeapSize = "64m";
	private String maxHeapSize = "256m";
	private String jarFile = "HUSACCT.jar";
	
	private String outputString;
	
	public BuildWrapper() {
		System.out.println("Trying to run husacct with increased heapsize");
		String commandString = String.format("java -Xms%s -Xmx%s -jar %s", initialHeapSize, maxHeapSize, jarFile);
		System.out.println(commandString);
		try {
			Process process = Runtime.getRuntime().exec(commandString);
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((outputString = input.readLine()) != null) {
				System.out.println(outputString);
			}
			input.close();
		} catch (IOException e) {
			System.out.println("Unable to run command");
			e.printStackTrace();
		} 
	}
	
	public static void main(String[] consoleArguments) {
		new BuildWrapper();
	}
}
