package husacct.control.task.help;

import husacct.common.Resource;
import husacct.control.task.MainController;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class HelpTreeModelLoader {

	private Logger logger = Logger.getLogger(MainController.class);
	List<HelpTreeNode> HelpTreeNodeList = new ArrayList<HelpTreeNode>();

	public Document getXmlDocument() {
		String manualXmlPath = Resource.HELP_PATH + "manuals.xml";
		logger.debug(manualXmlPath);

		Reader reader = new InputStreamReader(Resource.getStream(manualXmlPath));
		SAXBuilder sax = new SAXBuilder();
		Document doc = new Document();
		try {			
			doc = sax.build(reader);
		}
		catch (Exception ex) {
			logger.error(ex.getMessage());
		}

		return doc;
	}



	public DefaultMutableTreeNode getTreeModel() {


		Element root = getXmlDocument().getRootElement();

		DefaultMutableTreeNode parent = new DefaultMutableTreeNode(root.getName());
		for(Element child : root.getChildren()) {
			HelpTreeNode HelpNode = new HelpTreeNode(child.getAttributeValue("filename"), child.getAttributeValue("viewname"), child.getName(), "html");
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(HelpNode);
			if(HelpNode.getParent() == null) {
				parent.add(childNode);
			}
			else {
				HelpTreeNode parentHelpNode = new HelpTreeNode(HelpNode.getParent(),HelpNode.getParent(),HelpNode.getParent(), "folder");
				DefaultMutableTreeNode parentNode = findNode(parent, parentHelpNode);
				parentNode.add(childNode);
				parent.add(parentNode);
			}
			HelpTreeNodeList.add(HelpNode);
		}
		return parent;
	}
	
	private DefaultMutableTreeNode findNode(DefaultMutableTreeNode root, HelpTreeNode node) {
		List<DefaultMutableTreeNode> Children = Collections.list(root.children());
		for(int i = 0; i < Children.size(); i++) {
			if(((HelpTreeNode)Children.get(i).getUserObject()).getFilename().equals(node.getFilename())) {
				return Children.get(i);
			}
		}
		return new DefaultMutableTreeNode(node);
		
	}

	public String getContent(InputStream stream) {		
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		String line;
		String html = "";
		try {
			while((line = br.readLine()) != null) {
				html+=line;
			}	
		}
		catch (Exception ex) {
			
		}
		html = setPath(html);
		return html;
	}
	
	public String setPath(String html) {
		while(html.contains("RESOURCE.GET")) {
			int startIndex = html.indexOf("RESOURCE.GET");
			int startFileNameIndex = startIndex += "RESOURCE.GET".length() + 1;
			int endFileNameIndex = startFileNameIndex;
			if(startIndex > -1) {				
				while(html.charAt(endFileNameIndex) != ')') {
					endFileNameIndex++;
				}
				String filename = html.substring(startFileNameIndex, endFileNameIndex);
				URL pathname = Resource.get(Resource.HELP_IMAGE_PATH + filename);
				html = html.replace("RESOURCE.GET(" + filename + ")", pathname+"");
				
			}
			else {
				break;
			}
		}
		return html;
	}



	public String getContent(Component component) {
		for(HelpTreeNode htn : HelpTreeNodeList) {
			if(htn.getComponentName().equals(component.getClass().getName().substring(component.getClass().getName().lastIndexOf('.')+1))) {
				
				String HelpPagePath = Resource.HELP_PAGES_PATH + htn.getFilename();
				InputStream stream = Resource.getStream(HelpPagePath);

				String html = getContent(stream);
				
				if(html.equals("") || html==null){
					HelpPagePath = Resource.HELP_PAGES_PATH + "home.html";
					stream = Resource.getStream(HelpPagePath);
					return getContent(stream);
				}
				else {
					return html;
					
				}



			}
		}
		String HelpPagePath = Resource.HELP_PAGES_PATH + "home.html";
		InputStream stream = Resource.getStream(HelpPagePath);
		return getContent(stream);
	}
}
