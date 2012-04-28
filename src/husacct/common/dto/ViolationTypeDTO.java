package husacct.common.dto;

public class ViolationTypeDTO extends AbstractDTO {
	public final String key;
	public final String descriptionKey;
	public final boolean isDefault;
	

	public ViolationTypeDTO(String key, String descriptionKey, boolean isDefault) {
		this.key = key;
		this.descriptionKey = descriptionKey;
		this.isDefault = isDefault;
	}
}