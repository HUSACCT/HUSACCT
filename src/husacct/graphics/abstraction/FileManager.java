package husacct.graphics.abstraction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {
	private File file;
	private FileOutputStream outputstream;

	public FileManager() {
		createTemporaryFile();
	}

	public void createTemporaryFile() {
		new File(".");
	}

	public File getFile() {
		return file;
	}

	public void setFile(File selectedFile) {
		file = selectedFile;
		addFileExtensionIfMissing();
	}

	public FileOutputStream createOutputStream() throws FileNotFoundException {
		outputstream = new FileOutputStream(file);
		return outputstream;
	}

	public FileOutputStream getOutputStream() {
		return outputstream;
	}

	public void closeOutputStream() throws IOException {
		outputstream.close();
	}

	private void addFileExtensionIfMissing() {
		String absolutePathName = file.getAbsolutePath();
		String extension = absolutePathName.substring(
				absolutePathName.length() - 4, absolutePathName.length());
		if (!extension.equalsIgnoreCase(".png")) {
			file = new File(absolutePathName + ".png");
		}
	}
}
