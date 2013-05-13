package husacct.analyse.task.analyser.csharp.generators;

import java.util.EnumSet;

enum SkippableTypes {

	STRING("String"),
	INT("int"),
	BOOLEAN("boolean"),
	OBJECT("Object"),
	BYTE("byte"),
	CHAR("char");
	
	
	private final String type;
	
	private SkippableTypes(String type){
		this.type = type;
	}
	
	@Override
	public String toString(){
		return type;
	}
	
	public static boolean isSkippable(String type){
		for(SkippableTypes skippedType : EnumSet.allOf(SkippableTypes.class)){
			if(skippedType.toString().equals(type)){
				return true;
			}
		}
		return false;
	}
}
