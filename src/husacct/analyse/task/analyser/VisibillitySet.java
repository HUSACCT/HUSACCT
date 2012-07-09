package husacct.analyse.task.analyser;

import java.util.EnumSet;

public enum VisibillitySet {

	PUBLIC("public"),
	PRIVATE("private"),
	DEFAULT("default"),
	PROTECTED("protected");
	
	private final String visibillity;
	
	private VisibillitySet(String visibillity){
		this.visibillity = visibillity;
	}
	
	@Override
	public String toString(){
		return visibillity;
	}
	
	public static boolean isValidVisibillity(String visibillty){
		for(VisibillitySet option : EnumSet.allOf(VisibillitySet.class)){
			if(option.toString().equals(visibillty)){
				return true;
			}
		}
		return false;
	}
	
}
