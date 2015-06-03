package husacct.analyse.domain;

import org.jdom2.Element;

public interface IModelPersistencyService {

    // Used for the generic mechanism to save workspace data of all components; e.g. configuration settings  
	public Element getWorkspaceData();

    // Used for the generic mechanism to load workspace data of all components; e.g. configuration settings  
    public void loadWorkspaceData(Element analyseElement);

    public Element exportAnalysisModel();

    public void loadModel(Element analyseElement);
}
