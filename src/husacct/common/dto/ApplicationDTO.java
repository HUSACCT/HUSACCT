package husacct.common.dto;

public class ApplicationDTO extends AbstractDTO {

    public String name;
    public String[] paths;
    public String programmingLanguage;
    public String version;

    public ApplicationDTO(String name, String[] paths,
                          String programmingLanguage, String version) {
        super();
        this.name = name;
        this.paths = paths;
        this.programmingLanguage = programmingLanguage;
        this.version = version;
    }
}
