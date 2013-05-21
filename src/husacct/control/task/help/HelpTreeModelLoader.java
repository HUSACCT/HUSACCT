package husacct.control.task.help;

import husacct.common.Resource;

import java.awt.Component;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class HelpTreeModelLoader {
	List<HelpTreeNode> HelpTreeNodeList = new ArrayList<HelpTreeNode>();

	public Document getXmlDocument() {
		File manualDescriptionFile = new File("src\\husacct\\common\\resources\\help\\manuals.xml");

		SAXBuilder sax = new SAXBuilder();
		Document doc = new Document();
		try {

			doc = sax.build(manualDescriptionFile);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return doc;
	}



	public DefaultMutableTreeNode getTreeModel() {


		Element root = getXmlDocument().getRootElement();
		
		DefaultMutableTreeNode parent = new DefaultMutableTreeNode(root.getName());
		for(Element child : root.getChildren()) {
			HelpTreeNode HelpNode = new HelpTreeNode(child.getAttributeValue("filename"), child.getAttributeValue("viewname"), child.getName());
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(HelpNode);
			parent.add(childNode);
			HelpTreeNodeList.add(HelpNode);
		}
		
		return parent;




	}



	public String getContent(File f) throws FileNotFoundException {
	//	File f = new File("src\\common\resources\\help\\pages\\" + userObject.getFilename());
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line;
		String html = "";
		try {
			while((line = br.readLine()) != null) {
				html+=line;
			}	
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return html;


	}



	public String getContent(Component component) {
		for(HelpTreeNode htn : HelpTreeNodeList) {
			if(htn.getcomponentname().equals(component.getClass().getName().substring(component.getClass().getName().lastIndexOf('.')+1))) {
				try {
					File f = new File(new File(".").getCanonicalPath()+ "\\src\\husacct\\common\\resources\\help\\pages\\" + htn.getFilename());
					if(f.exists()) {
						return getContent(f);
					}
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
		try {
			File defaultFile = new File(new File(".").getCanonicalPath() + "\\src\\husacct\\common\\resources\\help\\pages\\home.html");
			return getContent(defaultFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
