package husacct.control.presentation.workspace.savers;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import husacct.ServiceProvider;
import husacct.common.savechain.ISaveable;

public class resourceGatherer implements Runnable {

	private XmlSavePanel saverPanel;
	public resourceGatherer(XmlSavePanel p) {
		this.saverPanel = p;
	}

	@Override
	public void run() {
		Element root = new Element("root");
		for(ISaveable service : getSaveableServices()){
			String serviceName = service.getClass().getName();

			Element container = new Element(serviceName);
			Element serviceData = service.getWorkspaceData();
			container.addContent(serviceData);
			root.addContent(container);
		}
		saverPanel.setRequiredSpace(calculateNewNodeSize(new Document(root)));
	}
	private int calculateNewNodeSize(Document d) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		XMLOutputter xout;
		if((boolean)saverPanel.getConfig().get("doCompress")) {
			xout = new XMLOutputter(Format.getRawFormat());
		}
		else {
			xout = new XMLOutputter(Format.getPrettyFormat());
		}
		try {
			xout.output(d, os);
			os.close();
			return(os.size());

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}


	private List<ISaveable> getSaveableServices() {
		List<ISaveable> saveableServices = new ArrayList<ISaveable>();

		if(ServiceProvider.getInstance().getControlService() instanceof ISaveable){
			saveableServices.add((ISaveable) ServiceProvider.getInstance().getControlService());
		}

		if(ServiceProvider.getInstance().getDefineService() instanceof ISaveable){
			saveableServices.add((ISaveable) ServiceProvider.getInstance().getDefineService());
		}

		if(ServiceProvider.getInstance().getAnalyseService() instanceof ISaveable){
			saveableServices.add((ISaveable) ServiceProvider.getInstance().getAnalyseService());
		}

		if(ServiceProvider.getInstance().getValidateService() instanceof ISaveable){
			saveableServices.add((ISaveable) ServiceProvider.getInstance().getValidateService());
		}

		if(ServiceProvider.getInstance().getGraphicsService() instanceof ISaveable){
			saveableServices.add((ISaveable) ServiceProvider.getInstance().getGraphicsService());
		}

		return saveableServices;
	}
}
