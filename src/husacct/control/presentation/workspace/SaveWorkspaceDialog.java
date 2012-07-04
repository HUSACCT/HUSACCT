package husacct.control.presentation.workspace;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.control.presentation.workspace.savers.SaverPanel;
import husacct.control.presentation.workspace.savers.SaverPanelFactory;
import husacct.control.task.MainController;
import husacct.control.task.resources.ResourceFactory;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class SaveWorkspaceDialog extends JDialog{

	private MainController mainController;
	private JList saverList;
	private List<String> saverListData;
	private JButton saveButton, cancelButton;
	
	private JPanel saverPanelContainer;
	private JPanel savePanel;
	private SaverPanel selectedSaverPanel;
	
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public SaveWorkspaceDialog(MainController mainController){
		super(mainController.getMainGui(), true);
		this.setTitle(localeService.getTranslatedString("SaveWorkspace"));
		this.mainController = mainController;
		this.setup();
		this.setSavers();
		this.addComponents();
		this.setListeners();
		this.setResizable(true);
		this.setVisible(true);
	}
	
	private void setup(){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(500, 380));
		this.setLocationRelativeTo(getRootPane());
	}
	
	private void setSavers(){
		List<String> savers = ResourceFactory.getAvailableResources();
		saverListData = savers;
	}
	
	private void addComponents(){
		
		saverList = new JList(saverListData.toArray());
		saverList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		saverList.setLayoutOrientation(JList.VERTICAL);
		saverList.setVisibleRowCount(-1);
		JScrollPane listScrollPane = new JScrollPane(saverList);
		listScrollPane.setAlignmentX(LEFT_ALIGNMENT);
		
		savePanel = new JPanel();
		savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.Y_AXIS));
		
		saverPanelContainer = new JPanel();
		saverPanelContainer.setPreferredSize(new Dimension(350, 300));
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		saveButton = new JButton(localeService.getTranslatedString("SaveButton"));
		cancelButton = new JButton(localeService.getTranslatedString("CancelButton"));
		
		saveButton.setEnabled(false);
		getRootPane().setDefaultButton(saveButton);
		
		buttonsPanel.add(saveButton);
		buttonsPanel.add(cancelButton);
		
		savePanel.add(saverPanelContainer);
		savePanel.add(buttonsPanel);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, savePanel);
		splitPane.setDividerLocation(150);
		splitPane.setEnabled(false);
		add(splitPane);
	}
	
	private void setListeners(){
		saverList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				loadSelectedOpenMethodPanel();
				saveButton.setEnabled(true);
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedSaverPanel.validateData() && saveWorkspace()){
					dispose();
				}
			}
		});
	}
	
	private boolean saveWorkspace(){
		String selectedLoader = (String) saverList.getSelectedValue();
		HashMap<String, Object> data = selectedSaverPanel.getData();
		return mainController.getWorkspaceController().saveWorkspace(selectedLoader, data);
	}
	
	private void loadSelectedOpenMethodPanel(){
		String selectedLoader = (String) saverList.getSelectedValue();
		selectedSaverPanel = SaverPanelFactory.get(selectedLoader);
		saverPanelContainer.removeAll();
		saverPanelContainer.add(selectedSaverPanel);
		validate();
	}
}
