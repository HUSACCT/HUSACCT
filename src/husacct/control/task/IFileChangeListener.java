package husacct.control.task;

import java.nio.file.Path;

public interface IFileChangeListener {
	public void onUpdate(Path path);
	public void onCreate(Path path);
	public void onRemove(Path path);
}
