package husacct.control.presentation.util;

import java.io.File;

public class Filename {

    private File file;
    private char pathSeparator, extensionSeparator;

    public Filename(File file, char pathSeparator, char extensionSeparator) {
        this.file = file;
        this.pathSeparator = pathSeparator;
        this.extensionSeparator = extensionSeparator;
    }

    public String getFilenameWithoutExtension() {
        int extensionSeparatorPosition = file.getName().lastIndexOf(extensionSeparator);
        return file.getName().substring(0, extensionSeparatorPosition);
    }

    public String getExtension() {
        int extensionSeparatorPosition = file.getName().lastIndexOf(extensionSeparator);
        return file.getName().substring(extensionSeparatorPosition + 1);
    }

    public String getPath() {
        int pathSeparatorPosition = file.getAbsolutePath().lastIndexOf(pathSeparator);
        return file.getAbsolutePath().substring(0, pathSeparatorPosition + 1);
    }
}