package husacct.validate.domain.exception;

import java.io.File;

public class FileNotAccessibleException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FileNotAccessibleException(File file) {
		super("the file :" + file.getAbsolutePath() + "is not accessible, either run with admin or choose a different location");
	}

}
