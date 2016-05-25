package husacct.analyse.task.analyser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class SourceFileFinder {

    private String requiredFileExtension;

    List<MetaFile> getFileInfoFromProject(String projectPath, String fileExtension) throws Exception {
        requiredFileExtension = fileExtension;
        List<MetaFile> paths = walk(projectPath);
        return paths;
    }

    private List<MetaFile> walk(String path) {
        path = path.replace('\\','/');
        File root = new File(path);
        File[] listFiles = root.listFiles();
        if (listFiles == null) {
            listFiles = new File[]{root};
        }
        List<MetaFile> filePaths = new ArrayList<MetaFile>();
        for (File file : listFiles) {
            checkIfFileOrDirectory(file, filePaths);
        }
        return filePaths;
    }

    private void checkIfFileOrDirectory(File file, List<MetaFile> filePaths) {
        if (file.getAbsoluteFile().isDirectory()) {
            filePaths.addAll(walk(file.getAbsolutePath()));
        } else if (getSourceFiles(file.getAbsolutePath())) {
            filePaths.add(new MetaFile(file.getAbsolutePath()));
        }
    }

    private boolean getSourceFiles(String filepath) {
        int extensionIndex = filepath.lastIndexOf(".");
        if (extensionIndex == -1) {
            return false;
        }
        String extension = filepath.substring(extensionIndex, filepath.length());
        return extension.equals(requiredFileExtension);
    }
}
