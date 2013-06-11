package husacct.control.task;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;

public class FileController {

	private WatchService watcher;
	private HashMap<WatchKey,Path> keys;
	private ArrayList<IFileChangeListener> listeners;
	
	@SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }
	
	public FileController(MainController mainController) {
		try {
			watcher = FileSystems.getDefault().newWatchService();
			keys = new HashMap<WatchKey,Path>();
			listeners = new ArrayList<IFileChangeListener>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addProject(String folder) throws IOException {
		Path project = Paths.get(folder);
		Files.walkFileTree(project, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path directory, BasicFileAttributes attrs) throws IOException
            {
            	addDirectory(directory);
                return FileVisitResult.CONTINUE;
            }
        });
	}
	
	public void addFileChangeListener(IFileChangeListener listener) {
		listeners.add(listener);
	}
	
	public void processEvents() throws IOException {
        for (;;) {
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path directory = keys.get(key);
            if (directory == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event: key.pollEvents()) {
                Kind<?> kind = event.kind();

                WatchEvent<Path> castedEvent = cast(event);
                Path relativePath = castedEvent.context();
                Path file = directory.resolve(relativePath);

                System.out.format("%s: %s\n", kind.name(), file);

                if ((kind == ENTRY_CREATE)) {
                    if (Files.isDirectory(file, NOFOLLOW_LINKS)) {
                        addDirectory(file);
                    } else {
                    	for(IFileChangeListener listener : listeners)
                    		listener.addFile(file);
                    }
                } else if((kind == ENTRY_MODIFY)) {
                	if (!Files.isDirectory(file, NOFOLLOW_LINKS)) {
                		for(IFileChangeListener listener : listeners)
                			listener.updateFile(file);
                	}
                } else if ((kind == ENTRY_DELETE)) {
                    if (Files.isDirectory(file, NOFOLLOW_LINKS)) {
                        //TODO Directory removed
                    } else {
                    	for(IFileChangeListener listener : listeners)
                    		listener.removeFile(file);
                    }
                }
            }

            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                if (keys.isEmpty()) {
                    break;
                }
            }
        }
	}
	
	private void addDirectory(Path directory) throws IOException {
		WatchKey key = directory.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        keys.put(key, directory);
	}
}
