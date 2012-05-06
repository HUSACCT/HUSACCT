package husacct.control.presentation.workspace;

import husacct.control.presentation.workspace.loaders.ILoaderFrame;
import husacct.control.presentation.workspace.loaders.LoaderFrameFactory;
import husacct.control.task.MainController;
import husacct.control.task.resources.ResourceFactory;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class OpenWorkspaceFrame extends JFrame{

	private MainController mainController;
	private JList loaderList;
	private List<String> loaderListData;
	private JButton next, cancel;
	
	public OpenWorkspaceFrame(MainController mainController){
		super("Open workspace");
		this.mainController = mainController;
		this.setup();
		this.setLoaders();
		this.addComponents();
		this.setListeners();
		this.setVisible(true);
	}
	
	private void setup(){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLayout(new FlowLayout());
		this.setSize(new Dimension(300, 400));
		this.setLocationRelativeTo(getRootPane());
	}
	
	private void setLoaders(){
		List<String> loaders = ResourceFactory.getAvailableResources();
		loaderListData = loaders;
	}
	
	private void addComponents(){
		loaderList = new JList(loaderListData.toArray());
		loaderList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		loaderList.setLayoutOrientation(JList.VERTICAL);
		loaderList.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(loaderList);
		listScroller.setPreferredSize(new Dimension(250, 80));
		listScroller.setAlignmentX(LEFT_ALIGNMENT);
		this.getContentPane().add(listScroller);
		
		next = new JButton("Next");
		next.setEnabled(false);
		this.getContentPane().add(next);
		
		cancel = new JButton("Cancel");
		this.getContentPane().add(cancel);
	}
	
	private void setListeners(){
		loaderList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				next.setEnabled(true);				
			}
		});
		
		loaderList.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		        if (e.getClickCount() == 2) {
		        	next.doClick();
		        }
		    }
		});
		
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();				
			}
		});
		
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				openLoaderFrame();
			}
		});
	}
	
	private void openLoaderFrame(){
		String selectedLoader = (String) loaderList.getSelectedValue();
		ILoaderFrame loaderFrame = LoaderFrameFactory.get(selectedLoader);
		loaderFrame.setWorkspaceController(mainController.getWorkspaceController());
		loaderFrame.setVisible(true);
	}
}
