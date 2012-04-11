package husacct.validate.domain.factory.java;

enum JavaAccessTypes{
	PUBLIC("public"),
	PROTECTED("protected"),
	DEFAULT("default"),
	PRIVATE("private");

	private final String key;

	JavaAccessTypes(String key){
		this.key = key;
	}

	@Override
	public String toString(){
		return key;
	}
}