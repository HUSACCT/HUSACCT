package husacct.validate.presentation;

import husacct.ServiceProvider;
import husacct.validate.abstraction.language.ValidateTranslator;
import husacct.validate.domain.factory.message.Messagebuilder;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.presentation.tableModels.ViolationHistoryListModel;
import husacct.validate.task.TaskServiceImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class ViolationHistoryGUI extends JInternalFrame {
	private JTable violationTable;
	private JList listDates;
	private DefaultTableModel violationModel;
	private final TaskServiceImpl taskServiceImpl;
	private ViolationHistoryListModel calendarListModel;
	private JTextArea textAreaDescription;
	private ViolationHistory selectedViolationHistory;

	public ViolationHistoryGUI(TaskServiceImpl taskServiceImpl) {
		init();
		loadModels();
		updateList();
		this.taskServiceImpl = taskServiceImpl;
	}

	private void updateList() {
		Calendar[] dates = ServiceProvider.getInstance().getValidateService().getViolationHistoryDates();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
		calendarListModel = new ViolationHistoryListModel(dates, taskServiceImpl);
		listDates.setModel(calendarListModel);
	}

	private void loadModels(){
		String[] columnNames = {

				ValidateTranslator.getValue("Source"),
				ValidateTranslator.getValue("DependencyKind"),
				ValidateTranslator.getValue("Target"),
				ValidateTranslator.getValue("Severity")};

		violationModel = new DefaultTableModel(columnNames, 0) {
			private static final long serialVersionUID = -6892927200143239311L;
			Class<?>[] types = new Class[]{
					String.class, String.class, String.class, String.class
			};
			boolean[] canEdit = new boolean[]{
					false, false, false, false
			};

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		};
		violationTable.setModel(violationModel);
		//violationTable.getRowSorter().toggleSortOrder(4);
		//	violationTable.getRowSorter().toggleSortOrder(4);
	}

	private void setViolations(Calendar date) {
		loadModels();
		selectedViolationHistory = taskServiceImpl.getViolationHistoryByDate(date);
		textAreaDescription.setText(selectedViolationHistory.getDescription());
		for (Violation violation : selectedViolationHistory.getViolations()) {
			violationModel.addRow(new Object[]{violation.getClassPathFrom(), ValidateTranslator.getValue(violation.getViolationtypeKey()), violation.getClassPathTo(), violation.getSeverity().toString()});

		}
	}


	private void init() {
		setResizable(true);
		setIconifiable(true);
		setMaximizable(true);
		setClosable(true);
		setTitle("Violation history");
		JScrollPane scrollPane = new JScrollPane();
		JScrollPane scrollPane_1 = new JScrollPane();
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				taskServiceImpl.removeViolationHistory(calendarListModel.getDate(listDates.getSelectedIndex()));
			}
		});
		JScrollPane scrollPane_2 = new JScrollPane();
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Appleflap", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(btnDelete, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))
								.addGap(6)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup()
												.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
														.addGroup(groupLayout.createSequentialGroup()
																.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 612, Short.MAX_VALUE)
																.addGap(2))
																.addGroup(groupLayout.createSequentialGroup()
																		.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 612, Short.MAX_VALUE)
																		.addGap(2)))
																		.addGap(0))
																		.addGroup(groupLayout.createSequentialGroup()
																				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
																				.addContainerGap())))
				);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addGroup(groupLayout.createSequentialGroup()
										.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(panel, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
										.addGap(5))
										.addGroup(groupLayout.createSequentialGroup()
												.addGap(3)
												.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)))
												.addGap(1)
												.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
														.addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
														.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
																.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
																.addGap(1)))
																.addContainerGap())
				);

		JLabel lblNewLabel = new JLabel("Line number");

		JLabel lblLogicalModule = new JLabel("Logical module");

		JLabel lblMessage = new JLabel("Message");

		final JLabel labelLineNumberValue = new JLabel("");

		final JLabel labelLogicalModuleValue = new JLabel("");

		final JLabel labelMessageValue = new JLabel("");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel)
								.addComponent(lblLogicalModule)
								.addComponent(lblMessage))
								.addGap(31)
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(labelMessageValue)
										.addComponent(labelLogicalModuleValue)
										.addComponent(labelLineNumberValue))
										.addContainerGap(436, Short.MAX_VALUE))
				);
		gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel)
								.addComponent(labelLineNumberValue))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblLogicalModule)
										.addComponent(labelLogicalModuleValue))
										.addGap(13)
										.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
												.addComponent(lblMessage)
												.addComponent(labelMessageValue))
												.addContainerGap(20, Short.MAX_VALUE))
				);
		panel.setLayout(gl_panel);

		textAreaDescription = new JTextArea();
		scrollPane_2.setViewportView(textAreaDescription);

		violationTable = new JTable();
		violationTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if(arg0.getValueIsAdjusting()) {
					return;
				}
				Violation violation = selectedViolationHistory.getViolations().get(violationTable.getSelectedRow());
				String message = new Messagebuilder().createMessage(violation.getMessage());
				labelMessageValue.setText(message);
				labelLogicalModuleValue.setText(violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath());
				labelLineNumberValue.setText("" + violation.getLinenumber());
			}

		});
		scrollPane_1.setViewportView(violationTable);

		listDates = new JList();
		listDates.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if(arg0.getValueIsAdjusting()) {
					return;
				}
				setViolations(calendarListModel.getDate(listDates.getSelectedIndex()));

			}
		});
		scrollPane.setViewportView(listDates);
		getContentPane().setLayout(groupLayout);
	}
}
