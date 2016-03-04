package husacct.analyse.domain;

import org.jdom2.Element;

public interface IModelPersistencyService {

    /** Used for the generic mechanism to save workspace data of all components; e.g. configuration settings
     */
	public Element getWorkspaceData();

    /** Used for the generic mechanism to load workspace data of all components; e.g. configuration settings
     */
    public void loadWorkspaceData(Element analyseElement);

    /** Creates an xml-Element that contains the data of all packages, classes, libraries and dependencies.
     */
    public Element exportAnalysisModel();

    /** First, clears the model. 
     * Second, adds the packages, classes, libraries and dependencies within the xml-Element  
     */
    public void importAnalysisModel(Element analyseElement);
}
