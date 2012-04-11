package husacct.analyse.abstraction.mappers.codemapper;

import husacct.analyse.abstraction.mappers.csharpmapper.CSharpMapper;
import husacct.analyse.abstraction.mappers.javamapper.JavaMapper;

public class MapperBuilder{
	
	public GenericMapper getMapper(String language){
		GenericMapper mapper;
		if(language.toLowerCase().equals(JavaMapper.programmingLanguage.toLowerCase())){
			mapper = new JavaMapper();
		}
		else{
			mapper = new CSharpMapper();
		}
		return mapper;
	}
}
