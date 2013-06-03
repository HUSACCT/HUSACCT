package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.control.task.MainController;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class UserActionLogPanel extends JPanel{

	private static final long serialVersionUID = 9105220354932171257L;
	private MainController mainController;
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public UserActionLogPanel(MainController mainController){
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
		
		this.setVisible(true);
		
		refreshUserActionsPanel();
	}
	
	@SuppressWarnings("serial")
	private JScrollPane getUserActionsDialogScrollPaneContents(){
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

		ArrayList<HashMap<String, String>> loggedUserActions = mainController.getUserActionLogController().getLoggedUserActionsArrayList();
		int i = 1;
		for(HashMap<String, String> loggedUserAction : loggedUserActions){
			logTableModel.addRow(new Object[]{i + ": " + loggedUserAction.get("message")});
			i++;
		}
		
		JScrollPane output = new JScrollPane(logTable);
		output.setOpaque(false);
		Dimension preferredSize = new Dimension(250, 212);
		output.setPreferredSize(preferredSize);
		
		return output;
	}
	
	/*private JPanel getUserActionsDialogButtonsPanel(){
		JPanel buttonsPanel = new JPanel();
		
		JButton undoButton = new JButton(localeService.getTranslatedString("Undo"));
		undoButton.setEnabled(false);
		buttonsPanel.add(undoButton);
		
		JButton redoButton = new JButton(localeService.getTranslatedString("Redo"));
		redoButton.setEnabled(false);
		buttonsPanel.add(redoButton);
		
		return buttonsPanel;
	}*/
	
	public void refreshUserActionsPanel(){
		this.removeAll();
		this.add(getUserActionsDialogScrollPaneContents(), BorderLayout.SOUTH);
		//this.add(getUserActionsDialogButtonsPanel(), BorderLayout.SOUTH);
		this.validate();
		this.repaint();
	}
}
