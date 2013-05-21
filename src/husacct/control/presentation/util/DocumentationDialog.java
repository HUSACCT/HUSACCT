package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.common.locale.ILocaleService;
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

public class DocumentationDialog extends JDialog{

	private static final long serialVersionUID = 1L;

	private JTree tree;
	private JScrollPane treeView;
	private JButton openButton;
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private MainController mainController;
	private DefaultMutableTreeNode selectedNode;
	public DocumentationDialog(MainController mainController) {

		super(mainController.getMainGui(), true);
		this.mainController = mainController;
		setTitle(localeService.getTranslatedString("Documentation"));
		setup();
		addComponents();
		setListeners();
		this.setVisible(true);
	}

	private void setup(){


		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(420, 350));
		this.setLayout(new FlowLayout(0,0,0));
		this.setResizable(false);
		DialogUtils.alignCenter(this);
		try {
			File root = new File(new File(".").getCanonicalPath() + "\\doc");
			tree = new JTree(getTreeModel(root));
			treeView = new JScrollPane(tree);
			treeView.setPreferredSize(new Dimension(420,280));
			
			
			add(treeView);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		JPanel buttonPanel = new JPanel();
		openButton = new JButton("Open");
		buttonPanel.add(openButton);
		add(buttonPanel);
		
		
	}

	private MutableTreeNode getTreeModel(File root) {
		DefaultMutableTreeNode parent = new DefaultMutableTreeNode(root.getName());
		if(root.list() != null) {
			for(int i = 0; i < root.list().length ; i++ ) {
				parent.add(getTreeModel(new File(root.getAbsolutePath() + "\\"+root.list()[i])));
			}
		}
		return parent;
	}

	private void addComponents(){

	}

	private void setListeners(){
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				selectedNode = (DefaultMutableTreeNode)
                tree.getLastSelectedPathComponent();	
				
				
			}
			
		});
		openButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(selectedNode!= null) {
					String path = "\\" + selectedNode.getUserObject();
					while((selectedNode = (DefaultMutableTreeNode) selectedNode.getParent()) != null) {
						path = "\\" + (String)selectedNode.getUserObject() + path;
					}
					
					try {
						Desktop dt = Desktop.getDesktop();
						dt.open(new File(new File(".").getCanonicalPath()+ path));
						
					}
					catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				
			}
			
		});

	}



	private GridBagConstraints getConstraint(int gridx, int gridy){

		return null;
	}



}

