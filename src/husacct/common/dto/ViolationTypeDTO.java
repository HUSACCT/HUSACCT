package husacct.common.dto;

public class ViolationTypeDTO extends AbstractDTO {
	public String key = "";
	public String descriptionKey = ""; 
	public boolean isDefault = false;	

	public ViolationTypeDTO(String key, String descriptionKey, boolean isDefault) {
		this.key = key;
		this.descriptionKey = descriptionKey;
		this.isDefault = isDefault;
	}


	public String getKey() {
		return key;
	}


	public String getDescriptionKey() {
		return descriptionKey;
	}


	public boolean isDefault() {
		return isDefault;
	}
}