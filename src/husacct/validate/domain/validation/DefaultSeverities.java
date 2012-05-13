package husacct.validate.domain.validation;

import java.awt.Color;

public enum DefaultSeverities {
	LOW("Low", Color.GREEN),
	MEDIUM("Medium", Color.ORANGE),
	HIGH("High", Color.RED),
	UNIDENTIFIED("Unidentified", Color.BLACK);
		
	private final String key;
	private final Color color;
	
	DefaultSeverities(String key, Color color){
		this.key = key;
		this.color = color;
	}
	
	public Color getColor(){
		return color;
	}
	
	@Override
	public String toString(){
		return key;
	}
}