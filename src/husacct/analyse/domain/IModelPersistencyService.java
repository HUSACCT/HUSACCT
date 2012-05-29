package husacct.analyse.domain;

import org.jdom2.Element;

public interface IModelPersistencyService{

	public Element saveModel();
	public void loadModel(Element analyseElement);
	public void exportToFile();
}
