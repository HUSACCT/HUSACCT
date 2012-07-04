package husacct.validate.domain.exception;

import husacct.ServiceProvider;

import java.io.File;

public class FileNotAccessibleException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FileNotAccessibleException(File file) {
		super(String.format(ServiceProvider.getInstance().getLocaleService().getTranslatedString("FileNotAccessibleExceptionMessage"), file.getAbsolutePath()));
	}
}