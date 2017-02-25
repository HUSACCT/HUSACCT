package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.common.locale.ILocaleService;
import husacct.control.presentation.util.DialogUtils;
import husacct.control.task.MainController;
import husacct.control.task.help.HelpTreeModelLoader;
import husacct.control.task.help.HelpTreeNode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.apache.log4j.Logger;

public class HelpDialog extends JDialog {

	private static final long serialVersionUID = 652971000948178977L;

	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private Logger logger = Logger.getLogger(HelpDialog.class);
	private JLabel pictureLabel;
	private JTree tree;
	private JScrollPane treeView;
	private DefaultMutableTreeNode selectedNode;
	private JEditorPane editorpane;
	private JScrollPane scroller;
	private Component component;
	private HelpTreeModelLoader hpl;

	public HelpDialog(MainController mainController, Component component) {
		super(mainController.mainGUI, true);
		
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
		this.setSize(new Dimension(1000, 675));
		this.setLayout(new FlowLayout(0, 0, 0));
		this.setResizable(false);
		this.getContentPane().setBackground(Color.WHITE);

		DialogUtils.alignCenter(this);

	}

	public JPanel getYouTubePanel() {
		JPanel youTubePanel = new JPanel();
		youTubePanel.setPreferredSize(new Dimension (this.getSize().width,30));		
		Image logo = Toolkit.getDefaultToolkit().getImage(Resource.get(Resource.SMALL_YOUTUBE_LOGO));
		logo = logo.getScaledInstance(95/2, 40/2, Image.SCALE_DEFAULT);
		pictureLabel = new JLabel(new ImageIcon(logo));

		pictureLabel.setBounds(0,0,256,256);
		youTubePanel.add(new JLabel(localeService.getTranslatedString("WatchOnlineTutorialsOn")));
		youTubePanel.add(pictureLabel);
		return youTubePanel;
	}

	private void addComponents() {
		JSplitPane rootPanel = new JSplitPane();
		rootPanel.setDividerLocation(200);

		rootPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

		tree = new JTree(hpl.getTreeModel());
		treeView = new JScrollPane(tree);
		tree.setRootVisible(false);

		ImageIcon leafIcon = new ImageIcon(Resource.get(Resource.ICON_DEFINE_ARCHITECTURE_DIAGRAM));
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
		rootPanel.setPreferredSize(new Dimension(this.getSize().width-5, this.getSize().height-55));
		getContentPane().add(rootPanel, BorderLayout.NORTH);


		getContentPane().add(getYouTubePanel());

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
					if(((HelpTreeNode)selectedNode.getUserObject()).getFileType().equals("html")) {
						String filename = ((HelpTreeNode)selectedNode.getUserObject()).getFilename();					
						InputStream stream = Resource.getStream(Resource.HELP_PAGES_PATH + filename);
						editorpane.setText(hpl.getContent(stream));
						editorpane.setCaretPosition(0);
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		pictureLabel.addMouseListener(new MouseListener() {
			@Override

			public void mouseClicked(MouseEvent e) {		
				try {
					Desktop.getDesktop().browse(new URI("http://www.youtube.com/user/HUSACCT"));
				} catch (IOException e1) {
					logger.error("IO Exception on opening browser with URL http://www.youtube.com/user/HUSACCT");
				} catch (URISyntaxException e1) {
					logger.error("URISyntaxException with URL http://www.youtube.com/user/HUSACCT");
				}
			}		
			@Override
			public void mouseEntered(MouseEvent e) {setCursor (Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));}
			@Override
			public void mouseExited(MouseEvent e) {setCursor (Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		});	
	}

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



}
