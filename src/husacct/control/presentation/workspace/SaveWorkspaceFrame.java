package husacct.control.presentation.workspace;

import husacct.control.presentation.workspace.savers.ISaverFrame;
import husacct.control.presentation.workspace.savers.SaverFrameFactory;
import husacct.control.task.WorkspaceController;
import husacct.control.task.workspace.ResourceFactory;

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
public class SaveWorkspaceFrame extends JFrame{

	private WorkspaceController workspaceController;
	private JList saverList;
	private List<String> saverListData;
	private JButton next, cancel;
	
	public SaveWorkspaceFrame(WorkspaceController workspaceController){
		super("Save workspace");
		this.workspaceController = workspaceController;
		this.setup();
		this.setSavers();
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
	
	private void setSavers(){
		List<String> workspaceResources = ResourceFactory.getAvailableResources();
		saverListData = workspaceResources;
	}
	
	private void addComponents(){
		saverList = new JList(saverListData.toArray());
		saverList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		saverList.setLayoutOrientation(JList.VERTICAL);
		saverList.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(saverList);
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
		saverList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				next.setEnabled(true);				
			}
		});
		
		saverList.addMouseListener(new MouseAdapter() {
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
		String selectedSaver = (String) saverList.getSelectedValue();
		ISaverFrame saverFrame = SaverFrameFactory.get(selectedSaver);
		saverFrame.setWorkspaceController(workspaceController);
		saverFrame.setVisible(true);
	}
}
