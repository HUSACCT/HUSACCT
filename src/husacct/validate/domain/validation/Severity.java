package husacct.validate.domain.validation;

import java.util.UUID;

public class Severity{
	
	private String defaultName;
	private String userName;
	private int value;
	private String color;
	private UUID id;
	
	public Severity() {
		id = UUID.randomUUID();
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
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}

	public UUID getId() {
		return id;
	}
}