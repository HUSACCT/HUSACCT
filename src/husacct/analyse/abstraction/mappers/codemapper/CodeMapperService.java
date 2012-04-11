package husacct.analyse.abstraction.mappers.codemapper;

import husacct.analyse.domain.famix.FamixObject;

import java.util.List;

public interface CodeMapperService {
	
	public List<FamixObject> analyseApplication(String workspacePath);
	public String[] getAvailableLanguages();
	
}
