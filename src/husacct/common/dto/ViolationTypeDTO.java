package husacct.common.dto;

public class ViolationTypeDTO extends AbstractDTO {
	private String key;
	private String descriptionKey;
	private Boolean isDefault;
	

	public ViolationTypeDTO(String key, String descriptionKey, Boolean isDefault) {
		this.key = key;
		this.descriptionKey = descriptionKey;
		this.isDefault = isDefault;
	}

	public String getKey() {
		return key;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public String getDescriptionKey() {
		return descriptionKey;
	}
}