package husacct.validate.domain.validation;

import java.awt.Color;
import java.util.UUID;

public class Severity {

	private String defaultName;
	private String userName;
	private Color color;
	private UUID id;
	
	public Severity() {
		this.id = UUID.randomUUID();
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
			return defaultName;
		}
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
}