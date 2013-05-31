package husacct.analyse.task;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ProjectDTO;
import husacct.control.task.configuration.ConfigurationManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class HistoryLogger {

	private IAnalyseService service;
	private ArrayList<ProjectDTO> projects;

	private Document doc;
	private Element rootElement;
	private String xmlFile = ConfigurationManager.getProperty("PlatformIndependentAppDataFolder", "") + ConfigurationManager.getProperty("ApplicationHistoryXMLFilename", "");


	public void logHistory(ApplicationDTO applicationDTO, String workspaceName) {
		this.service = ServiceProvider.getInstance().getAnalyseService();
		File file = new File(xmlFile); 
		if(file.exists()) {
			addToExistingXml(applicationDTO, workspaceName);
		} else {
			if(saveHistory(applicationDTO, workspaceName)) {
				createXML(doc, rootElement);
			}
		}
	}

	private boolean saveHistory(ApplicationDTO adto, String workspaceName) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			//Husacct root
			doc = docBuilder.newDocument();
			rootElement = doc.createElement("hussact");
			doc.appendChild(rootElement);

			rootElement.setAttribute("version", adto.version);

			//Workspace
			Element workspace = getWorkspace(workspaceName);
			rootElement.appendChild(workspace);

			//Application
			Element application = getApplicationElement(adto);
			workspace.appendChild(application);
			
			for(Element project : getProjectElement(adto)) {
				application.appendChild(project);
			}
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}	
		return true;
	}

	private boolean addToExistingXml(ApplicationDTO adto, String workspaceName) {

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.parse(xmlFile);

			Node root = doc.getFirstChild();
			NodeList workspaces = root.getChildNodes();
			
			
			ArrayList<String> workspaceList = new ArrayList<String>();
			for(int i = 0; i < workspaces.getLength(); i++) {
				workspaceList.add(workspaces.item(i).getAttributes().getNamedItem("name").getNodeValue());
			}
			
			if(workspaceList.contains(workspaceName)) {
				if(workspaces.item(workspaceList.indexOf(workspaceName)).getAttributes().getNamedItem("name").getNodeValue().equals(workspaceName)) {
					Node application = workspaces.item(workspaceList.indexOf(workspaceName)).getFirstChild();
					if(application.getAttributes().getNamedItem("name").getNodeValue().equals(adto.name)) {
						NodeList projects = application.getChildNodes();

						for(int i = 0; i < projects.getLength(); i++) {
							Node p = projects.item(i);

							if(p.getAttributes().getNamedItem("name").getNodeValue().equals(adto.projects.get(i).name)) {
								p.appendChild(getAnalyseElement((Element) p, adto.projects.get(i)));
							} else {
								for(Element project : getProjectElement(adto)) {
									p.appendChild(project);
								}
							}
						}
					} else {
						Node workspace = workspaces.item(workspaceList.indexOf(workspaceName)).getFirstChild();
						Node tempApplication = getApplicationElement(adto);
						for(Element project : getProjectElement(adto)) {
							tempApplication.appendChild(project);
						}
						workspace.appendChild(tempApplication);
					}
				}
			} else {
				Node workspace = getWorkspace(workspaceName);
				Node tempApplication = getApplicationElement(adto);
				for(Element project : getProjectElement(adto)) {
					tempApplication.appendChild(project);
				}
				workspace.appendChild(tempApplication);
				root.appendChild(workspace);
			}

			createXML(doc, (Element)root);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException saxe) {
			saxe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return true;
	}

	private void createXML(Document doc, Element rootElement) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(ConfigurationManager.getProperty("PlatformIndependentAppDataFolder", "") + ConfigurationManager.getProperty("ApplicationHistoryXMLFilename", "")));

			// Output to console for testing
			//StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
	
	private Element getWorkspace(String workspaceName) {
		Element workspace = doc.createElement("workspace");
		workspace.setAttribute("name", workspaceName);

		return workspace;
	}
	
	private Element getApplicationElement(ApplicationDTO adto) {
		Element application = doc.createElement("application");
		application.setAttribute("name", adto.name);
		
		return application;
	}

	private ArrayList<Element> getProjectElement(ApplicationDTO adto) {
		projects = adto.projects;
		ArrayList <Element> projectList = new ArrayList<Element>();
		
		for(ProjectDTO pdto : projects) {
			Element project = null;
			project = doc.createElement("project");
			project.setAttribute("name", pdto.name);
			project.appendChild(getAnalyseElement(project, pdto));
			projectList.add(project);
		}
		return projectList;
	}

	private Element getAnalyseElement(Element project, ProjectDTO pdto) {
		Element analyse = doc.createElement("analyse");

		GregorianCalendar cal = new GregorianCalendar();
		long millis = cal.getTimeInMillis();

		analyse.setAttribute("timestamp", millis + "");

		String projectPath = "";
		projectPath = pdto.paths.get(0);

		Element path = doc.createElement("path");
		path.appendChild(doc.createTextNode(projectPath));

		analyse.appendChild(path);

		//packages
		Element packages = doc.createElement("packages");
		packages.appendChild(doc.createTextNode(service.getAmountOfPackages() + ""));

		analyse.appendChild(packages);

		//classes
		Element classes = doc.createElement("classes");
		classes.appendChild(doc.createTextNode(service.getAmountOfClasses() + ""));

		analyse.appendChild(classes);

		//interfaces
		Element interfaces = doc.createElement("interfaces");
		interfaces.appendChild(doc.createTextNode(service.getAmountOfInterfaces() + ""));

		analyse.appendChild(interfaces);

		//dependencies
		Element dependencies = doc.createElement("dependencies");
		dependencies.appendChild(doc.createTextNode(service.getAmountOfDependencies() + ""));

		analyse.appendChild(dependencies);

		//violations
		Element violations = doc.createElement("violations");
		violations.appendChild(doc.createTextNode("0"));

		analyse.appendChild(violations);
		
		return analyse;
	}
}
