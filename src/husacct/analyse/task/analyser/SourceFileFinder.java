package husacct.analyse.task.analyser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

class SourceFileFinder {
	private String requiredFileExtension;
	
	List<MetaFile> getFileInfoFromProject(String projectPath, String fileExtension) throws Exception {
		requiredFileExtension = fileExtension;
		List<MetaFile> paths = walk(projectPath);
		return paths;
	}
	
	private List<MetaFile> walk(String path) throws IOException {
		File root = new File(path);
		File[] listFiles = root.listFiles();
		List<MetaFile> filePaths = new ArrayList<MetaFile>();
		for (File file : listFiles) {
			if (file.getAbsoluteFile().isDirectory()) {
				filePaths.addAll(walk(file.getAbsolutePath()));
			}
			else {
				if (getSourceFiles(file.getAbsolutePath())){
					LineNumberReader  lineNumberReader = new LineNumberReader(new FileReader(file));
					lineNumberReader.skip(Long.MAX_VALUE);
					filePaths.add(new MetaFile(file.getAbsolutePath(),lineNumberReader.getLineNumber()));
				}
			}
		}
		return filePaths;
	}
	
	private boolean getSourceFiles(String filepath) {
		int extensionIndex = filepath.lastIndexOf(".");
		String extension = filepath.substring(extensionIndex, filepath.length());
		return extension.equals(requiredFileExtension);
	}
}
