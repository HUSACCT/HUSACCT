package husacct.validate.task.extensiontypes;

public class ExtensionTypes {

    public enum ExtensionType {

        PDF("pdf"),
        HTML("html"),
        XML("xml");
        private String extension;

        ExtensionType(String extension) {
            this.extension = extension;
        }

        public String getExtension() {
            return extension;
        }
    }

    public String[] getExtensionTypes() {
        String[] extensionTypes = new String[ExtensionType.values().length];
        int extensionTypesIndex = 0;
        for (ExtensionType extensionType : ExtensionType.values()) {
            extensionTypes[extensionTypesIndex] = extensionType.getExtension();
            extensionTypesIndex++;
        }
        return extensionTypes;
    }
}