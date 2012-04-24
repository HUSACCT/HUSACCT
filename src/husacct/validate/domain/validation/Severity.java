package husacct.validate.domain.validation;

import java.awt.Color;

public class Severity {

	private String defaultName;
	private String userName;
	private int value;
	private Color color;

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

	@Override
	public String toString() {
		if (userName != null && !userName.isEmpty()) {
			return userName;
		} else{
			return defaultName;
		}
	}
}