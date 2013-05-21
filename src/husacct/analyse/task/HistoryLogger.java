package husacct.analyse.task;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.domain.famix.FamixQueryServiceImpl;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ProjectDTO;

import java.io.File;
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

public class HistoryLogger {
	
	private IModelQueryService queryService;
	private ProjectDTO projectDTO;
	private ArrayList<ProjectDTO> projects;
	
	public void saveHistory(ApplicationDTO applicationDTO, String workspaceName) {
		this.queryService = new FamixQueryServiceImpl();
		
		projects = applicationDTO.projects;
		
		if(projects.size() > 0) {
			if(projects.size() == 1) {
				projectDTO = projects.get(0);
			}
			else {
				projectDTO = projects.get(0);
			}
		} else {
			projectDTO = new ProjectDTO(null, null, null, null, null, null);
		}
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			//Hussact root
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("hussact");
			doc.appendChild(rootElement);

			rootElement.setAttribute("version", projectDTO.version);

			//Workspace
			Element workspace = doc.createElement("workspace");
			rootElement.appendChild(workspace);

			workspace.setAttribute("name", workspaceName);

			//Application
			Element application = doc.createElement("application");
			workspace.appendChild(application);
			
			application.setAttribute("name", applicationDTO.name);

			//Project
			Element project = doc.createElement("project");
			application.appendChild(project);

			project.setAttribute("name", projectDTO.name);

			//analyse
			Element analyse = doc.createElement("analyse");
			project.appendChild(analyse);

			GregorianCalendar cal = new GregorianCalendar();
			long millis = cal.getTimeInMillis();
			
			analyse.setAttribute("timestamp", millis + "");

			//path
			String projectPath = "";
			if(projectDTO.paths.size() > 0) {
				if(projectDTO.paths.size() == 1) {
					projectPath = projectDTO.paths.get(0);
				}
				else {
					projectPath = projectDTO.paths.get(0);
				}
			}
			
			Element path = doc.createElement("path");
			path.appendChild(doc.createTextNode(projectPath));

			analyse.appendChild(path);

			//packages
			Element packages = doc.createElement("packages");
			packages.appendChild(doc.createTextNode(queryService.getAmountOfPackages() + ""));

			analyse.appendChild(packages);

			//classes
			Element classes = doc.createElement("classes");
			classes.appendChild(doc.createTextNode(queryService.getAmountOfClasses() + ""));

			analyse.appendChild(classes);

			//interfaces
			Element interfaces = doc.createElement("interfaces");
			interfaces.appendChild(doc.createTextNode(queryService.getAmountOfInterfaces() + ""));

			analyse.appendChild(interfaces);

			//dependencies
			Element dependencies = doc.createElement("dependencies");
			dependencies.appendChild(doc.createTextNode(queryService.getAmountOfDependencies() + ""));

			analyse.appendChild(dependencies);

			//violations
			Element violations = doc.createElement("violations");
			violations.appendChild(doc.createTextNode("0"));

			analyse.appendChild(violations);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			//StreamResult result = new StreamResult(new File("src/common/resources/applicationanalysishistory.xml"));
			//StreamResult result = new StreamResult(new File("C:\\Users\\Rick\\Desktop\\file.xml"));
			
			// Output to console for testing
			StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}
}
