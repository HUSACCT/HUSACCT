package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IServiceListener;
import husacct.control.task.MainController;
import husacct.control.task.configuration.ConfigurationManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ActionLogPanel extends JPanel{

	private static final long serialVersionUID = 9105220354932171257L;
	private MainController mainController;
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public ActionLogPanel(MainController mainController){
		super();
		this.mainController = mainController;
		init();
	}
	
	private void init(){
		this.setFocusable(false);
		//this.setOpaque(false);
		this.setBackground(new Color(172, 181, 189));
		
		BorderLayout defaultBorderLayout = new BorderLayout();
		this.setLayout(defaultBorderLayout);
		Dimension preferredSize = new Dimension(250, 212);
		this.setPreferredSize(preferredSize);
		
		boolean showActionLog = ConfigurationManager.getProperty("ActionLogger").equals("true");
		this.setVisible(showActionLog);
		
		refreshActionLogPanel();
		
		localeService.addServiceListener(new IServiceListener() {
			public void update() {
				refreshActionLogPanel();
			}
		});
	}
	
	@SuppressWarnings("serial")
	private JScrollPane getActionsDialogScrollPaneContents(){
		DefaultTableModel logTableModel = new DefaultTableModel();
		JTable logTable = new JTable(logTableModel){
			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false;
			}
		};

		logTable.getTableHeader().setReorderingAllowed(false);
		logTable.getTableHeader().setResizingAllowed(false);
		logTable.setAutoCreateRowSorter(false);

		logTableModel.addColumn(localeService.getTranslatedString("ActionLog"));

		ArrayList<HashMap<String, String>> loggedActions = mainController.getActionLogController().getLoggedActionsArrayList();
		int i = 1;
		for(HashMap<String, String> loggedUserAction : loggedActions){
			logTableModel.addRow(new Object[]{i + ": " + loggedUserAction.get("message")});
			i++;
		}
		
		JScrollPane output = new JScrollPane(logTable);
		output.setOpaque(false);
		Dimension preferredSize = new Dimension(250, 212);
		output.setPreferredSize(preferredSize);
		
		return output;
	}
	
	public void refreshActionLogPanel(){
		this.removeAll();
		this.add(getActionsDialogScrollPaneContents(), BorderLayout.SOUTH);
		this.validate();
		this.repaint();
	}
	
	
}
