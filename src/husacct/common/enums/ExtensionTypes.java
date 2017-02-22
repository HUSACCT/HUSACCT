package husacct.common.enums;

//Owner: Validate

public enum ExtensionTypes {
	XLS("xls"),
	HTML("html"),
	PDF("pdf"),
	XML("xml");
	
	private String extension;

	ExtensionTypes(String extension) {
		this.extension = extension;
	}

	public static String[] getExtensionTypes() {
		String[] extensionTypes = new String[ExtensionTypes.values().length];
		int extensionTypesIndex = 0;
		for (ExtensionTypes extensionType : ExtensionTypes.values()) {
			extensionTypes[extensionTypesIndex] = extensionType.getExtension();
			extensionTypesIndex++;
		}
		return extensionTypes;
	}
	
	public String getExtension() {
		return extension;
	}
}