package husacct.control.presentation.workspace.savers;

import java.io.File;
import java.util.HashMap;

import javax.swing.JPanel;

public abstract class SaverPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	abstract public boolean validateData();
	abstract public HashMap<String, Object> getData();
	
	public File getDirectoryFromFile(File file){
		File output = new File("");
		
		if(file != null){
			String pathWithSelectedFile = file.getAbsolutePath();
			String pathToSelectedFile = pathWithSelectedFile.substring(0, pathWithSelectedFile.lastIndexOf('\\') + 1);
			output = new File(pathToSelectedFile);
		}
		
		return output;
	}
}