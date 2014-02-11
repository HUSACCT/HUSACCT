package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.common.locale.ILocaleService;
import husacct.control.ControlServiceImpl;
import husacct.control.task.MainController;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import org.apache.log4j.Logger;

public class DocumentationDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(DocumentationDialog.class);
	private JTree tree;
	private JScrollPane treeView;
	private JButton openButton;
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private MainController mainController;
	private JPanel mainPanel;
	private DefaultMutableTreeNode selectedNode;
	private JLabel hyperLinkLabel;
	
	public DocumentationDialog(MainController mainController) {
		super(mainController.getMainGui(), true);
		this.mainController = mainController;
		setTitle(localeService.getTranslatedString("Documentation"));
		setup();
		addComponents();
		this.setVisible(true);
	}

	private void setup(){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(420, 150));
		this.setLayout(new FlowLayout(0,0,0));
		this.setResizable(false);
		DialogUtils.alignCenter(this);
		
	}
	
	private void openBrowser(String link) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
            	URI uri = new URI(link);
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
	
	private void addComponents() {
		
		JLabel textLabel = new JLabel(localeService.getTranslatedString("clickLinkForDocumentation"));
		hyperLinkLabel = new JLabel("http://husacct.github.io/HUSACCT/#user_documentation");
		hyperLinkLabel.setForeground(Color.BLUE);
		
		hyperLinkLabel.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseReleased(MouseEvent e)  
		    {  
		    	openBrowser(hyperLinkLabel.getText());
		    	
		    }  
		}); 
		
		add(textLabel);
		add(hyperLinkLabel);
	}

	
}