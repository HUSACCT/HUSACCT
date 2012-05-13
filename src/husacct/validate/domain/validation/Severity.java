package husacct.validate.domain.validation;

import husacct.validate.abstraction.language.ValidateTranslator;
import java.awt.Color;
import java.util.UUID;

public class Severity implements Cloneable {
	private final UUID id;
	private String defaultName;
	private String userName;
	private Color color;

	public Severity(String defaultName, String userName, Color color){
		this.id = UUID.randomUUID();
		this.defaultName = defaultName;
		this.userName = userName;
		this.color = color;
	}

	public Severity(UUID id, String defaultName, String userName, Color color){
		this.id = id;
		this.defaultName = defaultName;
		this.userName = userName;
		this.color = color;
	}

	public Severity(String userName, Color color){
		this.id = UUID.randomUUID();
		this.defaultName = "";
		this.userName = userName;
		this.color = color;
	}

	public Severity(UUID id, String userName, Color color){
		this.id = id;
		this.defaultName = "";
		this.userName = userName;
		this.color = color;
	}

	public void setDefaultName(String defaultName) {
		this.defaultName = defaultName;
	}

	public String getDefaultName() {
		return defaultName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public String toString() {
		if (userName != null && !userName.isEmpty()) {
			return userName;
		} else{
			return ValidateTranslator.getValue(defaultName);
		}
	}

	public UUID getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Severity other = (Severity) obj;
		if ((this.defaultName == null) ? (other.defaultName != null)
				: !this.defaultName.equals(other.defaultName)) {
			return false;
		}
		if ((this.userName == null) ? (other.userName != null)
				: !this.userName.equals(other.userName)) {
			return false;
		}
		if (this.color != other.color &&
				(this.color == null || !this.color.equals(other.color))) {
			return false;
		}
		if (this.id != other.id &&
				(this.id == null || !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	public Severity clone() throws CloneNotSupportedException {
		Severity clone = (Severity)super.clone();		
		return clone;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 73 * hash + (this.defaultName != null ? this.defaultName.hashCode() : 0);
		hash = 73 * hash + (this.userName != null ? this.userName.hashCode() : 0);
		hash = 73 * hash + (this.color != null ? this.color.hashCode() : 0);
		hash = 73 * hash + (this.id != null ? this.id.hashCode() : 0);
		return hash;
	}
}