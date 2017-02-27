package husacct.analyse.task.analyse;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

class SourceFileFinder {

    private String requiredFileExtension;

    List<MetaFile> getFileInfoFromProject(String projectPath, String fileExtension) throws Exception {
        requiredFileExtension = fileExtension;
        List<MetaFile> files = new ArrayList<>();
        try {
            Files.walkFileTree(Paths.get(projectPath), new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    String fileName = file.toAbsolutePath().toString();
                    if(fileName.endsWith(requiredFileExtension)) {
                        files.add(new MetaFile(fileName));
                    }
                    return FileVisitResult.CONTINUE;
                }
            } );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return files;
    }

}
