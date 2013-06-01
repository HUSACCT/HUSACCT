package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.common.locale.ILocaleService;
import husacct.control.presentation.util.DialogUtils;
import husacct.control.task.ApplicationController;
import husacct.control.task.MainController;
import husacct.control.task.help.HelpTreeModelLoader;
import husacct.control.task.help.HelpTreeNode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;

import org.apache.log4j.Logger;

public class HelpDialog extends JDialog {

	private static final long serialVersionUID = 652971000948178977L;

	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private Logger logger = Logger.getLogger(HelpDialog.class);

	private JTree tree;
	private JScrollPane treeView;
	private DefaultMutableTreeNode selectedNode;
	private JEditorPane editorpane;
	private JScrollPane scroller;
	private Component component;
	private MainController mainController;
	private HelpTreeModelLoader hpl;

	public HelpDialog(MainController mainController, Component component) {
		super(mainController.mainGUI, true);
		this.mainController = mainController;
		this.component = component;
		this.hpl = new HelpTreeModelLoader();
		this.setTitle(localeService.getTranslatedString("Help"));
		setup();
		addComponents();
		setListeners();
		this.setVisible(true);

	}

	private void setup() {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(600, 675));
		this.setLayout(new FlowLayout(0, 0, 0));
		this.setResizable(false);
		this.getContentPane().setBackground(Color.WHITE);

		DialogUtils.alignCenter(this);

	}

	private void addComponents() {
		JSplitPane rootPanel = new JSplitPane();
		rootPanel.setDividerLocation(200);

		rootPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		tree = new JTree(hpl.getTreeModel());
		treeView = new JScrollPane(tree);
		tree.setRootVisible(false);

		ImageIcon leafIcon = new ImageIcon("src/"
				+ Resource.ICON_DEFINE_ARCHITECTURE_DIAGRAM);
		if (leafIcon != null) {
			DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
			renderer.setLeafIcon(leafIcon);
			tree.setCellRenderer(renderer);
		}

		rootPanel.add(treeView, JSplitPane.LEFT);


		editorpane = new JEditorPane("text/html", hpl.getContent(component));
		Color color = new Color(255, 255, 200);
		editorpane.setBackground(color);
		editorpane.setEditable(false);
		editorpane.setCaretPosition(0);
		scroller = new JScrollPane(editorpane);

		rootPanel.add(scroller, JSplitPane.RIGHT);
		rootPanel.setPreferredSize(this.getSize());
		getContentPane().add(rootPanel, BorderLayout.SOUTH);

	}

	private void setListeners() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setVisible(false);
				dispose();
			}
		});

		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				selectedNode = (DefaultMutableTreeNode) tree
						.getLastSelectedPathComponent();
				try {
					File f = new File(
							new File(".").getCanonicalPath()
							+ "\\src\\husacct\\common\\resources\\help\\pages\\"
							+ ((HelpTreeNode) selectedNode
									.getUserObject()).getFilename());
					editorpane.setText(hpl.getContent(f));

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		});

		editorpane.addHyperlinkListener(new HyperlinkListener() {

			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					String id =  e.getDescription().replaceAll("#", "");
					int position = editorpane.getText().indexOf(("id=\"" + id));
					if(position==-1) {
						position = editorpane.getText().indexOf("id = \"" + id);
						
					}
					System.out.println(editorpane.getText());
					System.out.println(position);
					if(position > 0) {
						editorpane.setCaretPosition(position);
					}
				}
			}


		});
		
		mainController.getMainGui().addUserActionLogDialogWindowFocusListener(this);
	}
}