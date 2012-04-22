package husacct.validate.domain.validation;

import java.awt.Color;
import java.util.UUID;

public class Severity{

	private String defaultName;
	private String userName;
	private int value;
	private Color color;

	public Severity() {
//		id = UUID.randomUUID();
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
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}

//	public UUID getId() {
//		return id;
	}
//}