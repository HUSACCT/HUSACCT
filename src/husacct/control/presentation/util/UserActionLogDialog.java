package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.control.task.MainController;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class UserActionLogDialog extends JDialog{

	private static final long serialVersionUID = 9105220354932171257L;
	private MainController mainController;
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public UserActionLogDialog(Frame owner, MainController mainController){
		super(owner);
		this.mainController = mainController;
		init();
	}
	
	private void init(){
		this.setUndecorated(true);
		this.setAlwaysOnTop(true);
		this.setFocusableWindowState(false);
		this.setFocusable(false);
		this.setTitle(localeService.getTranslatedString("ActionLog"));
		this.setVisible(true);
		
		int dialogWidth = 300;
		int dialogHeight = 212;
		this.setSize(dialogWidth, dialogHeight);

		//Absolute positioning: Right bottom of screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int marginRight = 5;
		int marginBottom = 40;
		int dialogLocationX = (int)dim.getWidth()-dialogWidth-marginRight;
		int dialogLocationY = (int)dim.getHeight()-dialogHeight-marginBottom;
		this.setLocation(dialogLocationX, dialogLocationY);
		
		refreshUserActionsDialog();
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
		
		return new JScrollPane(logTable);
	}
	
	private JPanel getUserActionsDialogButtonsPanel(){
		JPanel buttonsPanel = new JPanel();
		
		JButton undoButton = new JButton(localeService.getTranslatedString("Undo"));
		undoButton.setEnabled(false);
		buttonsPanel.add(undoButton);
		
		JButton redoButton = new JButton(localeService.getTranslatedString("Redo"));
		redoButton.setEnabled(false);
		buttonsPanel.add(redoButton);
		
		return buttonsPanel;
	}
	
	public void refreshUserActionsDialog(){
		this.getContentPane().removeAll();
		this.add(getUserActionsDialogScrollPaneContents());
		this.add(getUserActionsDialogButtonsPanel(), BorderLayout.SOUTH);
		this.validate();
		this.repaint();
	}
}
