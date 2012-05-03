package husacct.graphics.abstraction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {
	private File file;
	private FileOutputStream outputstream;
	
	public FileManager(){
		createTemporaryFile();
	}
	
	public void createTemporaryFile(){
		new File(".");
	}
	
	public File getFile(){
		return this.file;
	}
	
	public void setFile(File file){
		this.file = file;
	}
	
	public FileOutputStream createOutputStream() throws FileNotFoundException{
		this.outputstream = new FileOutputStream(file);
		return this.outputstream;
	}

	public FileOutputStream getOutputStream() {
		return this.outputstream;
	}

	public void closeOutputStream() throws IOException {
		this.outputstream.close();
	}
}
