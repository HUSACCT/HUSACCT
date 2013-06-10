package husacct.control.task;

import java.nio.file.Path;

public interface IFileChangeListener {
	public void updateFile(Path path);
	public void addFile(Path path);
	public void removeFile(Path path);
}
