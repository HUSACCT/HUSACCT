package husacct.validate.abstraction.extensiontypes;

import java.util.ArrayList;
import java.util.List;

public class ExtensionTypes {
	public enum ExtensionType {
		PDF("pdf"), HTML("html"), XML("xml");
		private String extension;
		ExtensionType(String extension) {
			this.extension = extension;
		}
		public String getExtension() {
			return extension;
		}
	}
	public List<String> getExtensionTypes() {
		List<String> extensionTypes = new ArrayList<String>();
		for(ExtensionType extensionType : ExtensionType.values()) {
			extensionTypes.add(extensionType.getExtension());
		}
		return extensionTypes;
	}
}
