package husacct.analyse.task.analyser.csharp.generators;

import java.util.EnumSet;

enum SkippableTypes {

	STRING1("string"),
	STRING2("String"),
	INT("int"),
	BOOL("bool"),
	OBJECT("object"),
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
			String s= skippedType.toString();
			if(s.equals(type)){
				return true;
			}
		}
		return false;
	}
}
