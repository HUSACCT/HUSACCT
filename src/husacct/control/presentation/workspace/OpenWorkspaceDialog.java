package husacct.control.presentation.workspace;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.control.presentation.workspace.loaders.LoaderPanel;
import husacct.control.presentation.workspace.loaders.LoaderPanelFactory;
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
public class OpenWorkspaceDialog extends JDialog{

	private String selectedLoader = "Xml";
	private MainController mainController;
	private JButton openButton, cancelButton;
	
	private JPanel loaderPanelContainer;
	private JPanel openPanel;
	private LoaderPanel selectedLoaderPanel;
	
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public OpenWorkspaceDialog(MainController mainController){
		super(mainController.getMainGui(), true);
		this.setTitle(localeService.getTranslatedString("OpenWorkspace"));
		this.mainController = mainController;
		this.setup();
		this.addComponents();
		this.setListeners();
		this.setResizable(true);
		this.setVisible(true);
	}
	
	private void setup(){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(400, 250));
		this.setLocationRelativeTo(getRootPane());
	}
	
	private void addComponents(){
		openPanel = new JPanel();
		openPanel.setLayout(new BoxLayout(openPanel, BoxLayout.Y_AXIS));
		
		loaderPanelContainer = new JPanel();
		loaderPanelContainer.setPreferredSize(new Dimension(350, 200));
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		openButton = new JButton(localeService.getTranslatedString("OpenButton"));
		cancelButton = new JButton(localeService.getTranslatedString("CancelButton"));
		
		openButton.setEnabled(true);
		getRootPane().setDefaultButton(openButton);
		
		buttonsPanel.add(openButton);
		buttonsPanel.add(cancelButton);
		
		openPanel.add(loaderPanelContainer);
		openPanel.add(buttonsPanel);
		add(openPanel);
		
		selectedLoaderPanel = LoaderPanelFactory.get(selectedLoader);
		loaderPanelContainer.removeAll();
		loaderPanelContainer.add(selectedLoaderPanel);
	}
	
	private void setListeners(){
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		openButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(selectedLoaderPanel.validateData() && loadWorkspace()){
					dispose();
					mainController.getViewController().showDefineArchitecture();
				}
			}
		});
	}
	
	private boolean loadWorkspace(){
		HashMap<String, Object> data = selectedLoaderPanel.getData();
		return mainController.getWorkspaceController().loadWorkspace(selectedLoader, data);
	}
}
