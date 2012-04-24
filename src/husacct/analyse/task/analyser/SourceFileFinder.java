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
		File[] list = root.listFiles();
		
		List<MetaFile> paths = new ArrayList<MetaFile>();
		for (File f : list) {
			if (f.getAbsoluteFile().isDirectory()) {
				paths.addAll(walk(f.getAbsolutePath()));
			}
			else {
				if (getSourceFiles(f.getAbsolutePath())){
					LineNumberReader  lnr = new LineNumberReader(new FileReader(f));
					lnr.skip(Long.MAX_VALUE);
					paths.add(new MetaFile(f.getAbsolutePath(),lnr.getLineNumber()));
				}
			}
		}
		return paths;
	}
	
	private boolean getSourceFiles(String filepath) {
		int extensionIndex = filepath.lastIndexOf(".");
		String extension = filepath.substring(extensionIndex, filepath.length());
		return extension.equals(requiredFileExtension);
	}
}
