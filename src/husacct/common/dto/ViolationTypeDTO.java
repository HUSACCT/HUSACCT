package husacct.common.dto;

public class ViolationTypeDTO extends AbstractDTO {
	private final String key;
	private final String descriptionKey;
	private final boolean isDefault;
	

	public ViolationTypeDTO(String key, String descriptionKey, boolean isDefault) {
		this.key = key;
		this.descriptionKey = descriptionKey;
		this.isDefault = isDefault;
	}

	public String getKey() {
		return key;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public String getDescriptionKey() {
		return descriptionKey;
	}
}