package husacct.define.presentation.jpanel;

import husacct.ServiceProvider;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.common.services.IServiceListener;
import husacct.control.presentation.util.DialogUtils;
import husacct.define.domain.services.DomainGateway;
import husacct.define.presentation.jdialog.AppliedRuleJDialog;
import husacct.define.presentation.tables.JTableAppliedRule;
import husacct.define.presentation.tables.JTableTableModel;
import husacct.define.presentation.utils.DataHelper;
import husacct.define.presentation.utils.JPanelStatus;
import husacct.define.presentation.utils.UiDialogs;
import husacct.define.task.DefinitionController;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

public class AppliedRulesJPanel extends HelpableJPanel implements ActionListener,
		Observer, IServiceListener {

	private static final long serialVersionUID = -2052083182258803790L;

	private JButton addRuleButton;
	private JMenuItem addRuleItem = new JMenuItem();
	private JScrollPane appliedRulesPane;
	private JTableAppliedRule appliedRulesTable;
	private JButton editRuleButton;
	private JMenuItem editRuleItem = new JMenuItem();

	private JPopupMenu popupMenu = new JPopupMenu();
	private JButton removeRuleButton;
	private JMenuItem removeRuleItem = new JMenuItem();

	public AppliedRulesJPanel() {
		super();
	}

	/**
	 * Handling ActionPerformed
	 */
	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == addRuleButton
				|| action.getSource() == addRuleItem) {
			addRule();
		} else if (action.getSource() == editRuleButton
				|| action.getSource() == editRuleItem) {
			editRule();
		} else if (action.getSource() == removeRuleButton
				|| action.getSource() == removeRuleItem) {
			removeRules();
		}
	}

	private JScrollPane addAppliedRulesTable() {
		appliedRulesPane = new JScrollPane();
		appliedRulesTable = new JTableAppliedRule();
		appliedRulesTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				createPopup(event);
				setButtonEnableState();
				if (event.getClickCount() == 2) {
					editRule();
				}
			}

			@Override
			public void mouseEntered(MouseEvent event) {
				createPopup(event);
				setButtonEnableState();
			}

			@Override
			public void mousePressed(MouseEvent event) {
				createPopup(event);
				setButtonEnableState();
			}
		});
		appliedRulesPane.setViewportView(appliedRulesTable);
		return appliedRulesPane;
	}

	protected JPanel addButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(createButtonPanelLayout());
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 4));
		buttonPanel.setPreferredSize(new java.awt.Dimension(90, 156));

		addRuleButton = new JButton();
		buttonPanel.add(addRuleButton, new GridBagConstraints(0, 0, 1, 1, 0.0,
				0.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		addRuleButton.addActionListener(this);

		editRuleButton = new JButton();
		buttonPanel.add(editRuleButton, new GridBagConstraints(0, 1, 1, 1, 0.0,
				0.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		editRuleButton.addActionListener(this);

		removeRuleButton = new JButton();
		buttonPanel.add(removeRuleButton, new GridBagConstraints(0, 2, 1, 1,
				0.0, 0.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		removeRuleButton.addActionListener(this);

		setButtonTexts();
		return buttonPanel;
	}

	private void addRule() {
		long moduleId = DomainGateway.getInstance().getSelectedModuleId();
		if (moduleId != -1) {
			AppliedRuleJDialog appliedRuleFrame = new AppliedRuleJDialog(
					moduleId, -1L);
			DialogUtils.alignCenter(appliedRuleFrame);
			appliedRuleFrame.setVisible(true);

		} else {
			// TODO Test popup
			JOptionPane.showMessageDialog(this,
					ServiceProvider.getInstance().getLocaleService()
							.getTranslatedString("ModuleSelectionError"),
					ServiceProvider.getInstance().getLocaleService()
							.getTranslatedString("WrongSelectionTitle"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private GridBagLayout createButtonPanelLayout() {
		GridBagLayout buttonPanelLayout = new GridBagLayout();
		buttonPanelLayout.rowWeights = new double[] { 0.0, 0.0, 0.1 };
		buttonPanelLayout.rowHeights = new int[] { 0, 11, 7 };
		buttonPanelLayout.columnWeights = new double[] { 0.1 };
		buttonPanelLayout.columnWidths = new int[] { 7 };
		return buttonPanelLayout;
	}

	private void createPopup(MouseEvent event) {
		if (SwingUtilities.isRightMouseButton(event)) {
			int row = appliedRulesTable.rowAtPoint(event.getPoint());
			int column = appliedRulesTable.columnAtPoint(event.getPoint());
			if (!appliedRulesTable.isRowSelected(row)) {
				appliedRulesTable.changeSelection(row, column, false, false);
			}
			popupMenu.show(event.getComponent(), event.getX(), event.getY());
		}
	}

	private void createPopupMenu() {
		addRuleItem = new JMenuItem(ServiceProvider.getInstance()
				.getLocaleService().getTranslatedString("Add"));
		addRuleItem.addActionListener(this);
		editRuleItem = new JMenuItem(ServiceProvider.getInstance()
				.getLocaleService().getTranslatedString("Edit"));
		editRuleItem.addActionListener(this);
		removeRuleItem = new JMenuItem(ServiceProvider.getInstance()
				.getLocaleService().getTranslatedString("Remove"));
		removeRuleItem.addActionListener(this);

		popupMenu.add(addRuleItem);
		popupMenu.add(editRuleItem);
		popupMenu.add(removeRuleItem);
	}

	private void disableButtons() {
		addRuleButton.setEnabled(false);
		addRuleItem.setEnabled(false);

		editRuleButton.setEnabled(false);
		editRuleItem.setEnabled(false);

		removeRuleButton.setEnabled(false);
		removeRuleItem.setEnabled(false);
	}

	private void editRule() {
		long moduleId = DefinitionController.getInstance()
				.getSelectedModuleId();
		long selectedAppliedRuleId = getSelectedAppliedRuleId();
		if (selectedAppliedRuleId != -1) {
			AppliedRuleJDialog appliedRuleFrame = new AppliedRuleJDialog(
					moduleId, selectedAppliedRuleId);
			DialogUtils.alignCenter(appliedRuleFrame);
			appliedRuleFrame.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(this,
					ServiceProvider.getInstance().getLocaleService()
							.getTranslatedString("RuleSelectionError"),
					ServiceProvider.getInstance().getLocaleService()
							.getTranslatedString("WrongSelectionTitle"),
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void enableAddDisableEditRemoveButtons() {
		addRuleButton.setEnabled(true);
		addRuleItem.setEnabled(true);

		editRuleButton.setEnabled(false);
		editRuleItem.setEnabled(false);

		removeRuleButton.setEnabled(false);
		removeRuleItem.setEnabled(false);
	}

	private void enableButtons() {
		addRuleButton.setEnabled(true);
		addRuleItem.setEnabled(true);

		editRuleButton.setEnabled(true);
		editRuleItem.setEnabled(true);

		removeRuleButton.setEnabled(true);
		removeRuleItem.setEnabled(true);
	}

	public TableModel getModel() {
		return appliedRulesTable.getModel();
	}

	private long getSelectedAppliedRuleId() {
		long selectedAppliedRuleId = -1;
		try {
			Object o = appliedRulesTable.getValueAt(getSelectedRow(),
					appliedRulesTable.getRuleTypeColumnIndex());
			if (o instanceof DataHelper) {
				DataHelper datahelper = (DataHelper) o;
				selectedAppliedRuleId = datahelper.getId();
			}
		} catch (Exception e) {
		}
		return selectedAppliedRuleId;
	}

	public int getSelectedRow() {
		return appliedRulesTable.getSelectedRow();
	}

	/**
	 * Creating Gui
	 */
	public void initGui() {
		DefinitionController.getInstance().addObserver(this);
		BorderLayout appliedRulesPanelLayout = new BorderLayout();
		setLayout(appliedRulesPanelLayout);
		setBorder(BorderFactory.createTitledBorder(ServiceProvider
				.getInstance().getLocaleService().getTranslatedString("Rules")));
		this.add(addAppliedRulesTable(), BorderLayout.CENTER);
		this.add(addButtonPanel(), BorderLayout.EAST);
		createPopupMenu();
		setButtonEnableState();
		ServiceProvider.getInstance().getLocaleService().addServiceListener(this);
	}

	private void removeRules() {
		if (getSelectedRow() != -1) {
			List<Long> selectedRules = new ArrayList<Long>();

			for (int selectedRow : appliedRulesTable.getSelectedRows()) {
				Object o = appliedRulesTable.getValueAt(selectedRow, 0);
				if (o instanceof DataHelper) {
					DataHelper datahelper = (DataHelper) o;
					selectedRules.add(datahelper.getId());
				}
			}
			DomainGateway.getInstance().removeRules(selectedRules);
			DefinitionController.getInstance().removeRules(selectedRules);
		}
	}

	private void setButtonEnableState() {
		if (DefinitionController.getInstance().getSelectedModuleId() == -1) {
			disableButtons();
		} else if (appliedRulesTable.getSelectedRowCount() == 0
				|| getSelectedRow() == -1) {
			enableAddDisableEditRemoveButtons();
		} else {
			enableButtons();
		}
	}

	private void setButtonTexts() {
		addRuleButton.setText(ServiceProvider.getInstance().getLocaleService()
				.getTranslatedString("Add"));
		editRuleButton.setText(ServiceProvider.getInstance().getLocaleService()
				.getTranslatedString("Edit"));
		removeRuleButton.setText(ServiceProvider.getInstance()
				.getLocaleService().getTranslatedString("Remove"));
	}

	@Override
	public void update() {
		setButtonTexts();
		setBorder(BorderFactory.createTitledBorder(ServiceProvider
				.getInstance().getLocaleService().getTranslatedString("Rules")));
		appliedRulesTable.changeColumnHeaders();
	}

	/**
	 * Observer
	 */
	@Override
	public void update(Observable o, Object arg) {
		updateAppliedRuleTable();
		setButtonEnableState();
	}

	public void updateAppliedRuleTable() {
		try {
			JTableTableModel atm = (JTableTableModel) appliedRulesTable.getModel();
			atm.getDataVector().removeAllElements();

			long moduleId = DefinitionController.getInstance().getSelectedModuleId();
			JPanelStatus.getInstance(ServiceProvider.getInstance().getLocaleService().getTranslatedString("UpdatingRules")).start();
			if (moduleId != -1) {

				// Get all appliedRuleIds from the service
				ArrayList<Long> appliedRulesIds = DefinitionController.getInstance().getAppliedRuleIdsBySelectedModule();

				if (appliedRulesIds != null) {
					for (long appliedRuleId : appliedRulesIds) {

						HashMap<String, Object> ruleDetails = DefinitionController.getInstance().getRuleDetailsByAppliedRuleId(appliedRuleId);
						String ruleTypeKey = (String) ruleDetails.get("ruleTypeKey");
						// SetDataHelper to help retrieve the applied Rule id
						// through the ruleTypeKey
						DataHelper datahelperRuleType = new DataHelper();
						datahelperRuleType.setId(appliedRuleId);
						datahelperRuleType.setValue(ruleTypeKey);

						String moduleToName = (String) ruleDetails.get("moduleToName");
						boolean appliedRuleIsEnabled = (Boolean) ruleDetails.get("enabled");
						String enabled = "Off";
						if (appliedRuleIsEnabled) {
							enabled = "On";
						}
						int numberofexceptions = (Integer) ruleDetails.get("numberofexceptions");

						Object rowdata[] = { datahelperRuleType, moduleToName, enabled, numberofexceptions };

						atm.addRow(rowdata);
					}
				}
			}
			atm.fireTableDataChanged();
		} catch (Exception e) {
			UiDialogs.errorDialog(this, e.getMessage());
		} finally {
			JPanelStatus.getInstance().stop();
		}
	}
}
