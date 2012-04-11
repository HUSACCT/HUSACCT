package husacct.define.presentation.helper;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class XmlFileFilter extends FileFilter {
	public static final String XML = "xml";

	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String extension = getExtension(f);
		if (extension != null) {
			return extension.equals(XML);
		}
		return false;
	}

	@Override
	public String getDescription() {
		return "*." + XML;
	}

	public String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

}
