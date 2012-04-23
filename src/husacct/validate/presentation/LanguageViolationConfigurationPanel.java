package husacct.validate.presentation;

import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.task.TableModels.ComboBoxTableModel;
import husacct.validate.task.TaskServiceImpl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.TableColumnModel;

public class LanguageViolationConfigurationPanel extends JPanel {

	private final ComboBoxTableModel ruletypeModel;
	private final ComboBoxTableModel violationtypeModel;
	private final String language;
	private final List<RuleType> ruletypes;
	private final TaskServiceImpl ts;

    private JButton applyViolationTypes;
    private JScrollPane ruletypePanel, violationtypePanel;
    private JTable ruletypeTable, violationtypeTable;

    public LanguageViolationConfigurationPanel(String language, List<RuleType> ruletypes, List<Severity> severityNames, TaskServiceImpl ts) {
		this.language = language;
		this.ruletypes = ruletypes;
		this.ts = ts;
		String[] ruletypeColumnNames = {"Ruletype", "Severity"};
		ruletypeModel = new ComboBoxTableModel(ruletypeColumnNames, 0,severityNames);
		ruletypeModel.setTypes(new Class[]{java.lang.String.class, java.lang.String.class});
		ruletypeModel.setCanEdit(new Boolean[]{false, true});

		String[] violationtypeModelHeaders = {"Violationtype", "Severity", "Active"};
		violationtypeModel = new ComboBoxTableModel(violationtypeModelHeaders, 0, severityNames);
		violationtypeModel.setTypes(new Class[]{String.class, String.class, Boolean.class});
		violationtypeModel.setCanEdit(new Boolean[]{false, true, true});

		initComponents();

		TableColumnModel tcm = ruletypeTable.getColumnModel();
		tcm.getColumn(1).setCellEditor(ruletypeModel.getEditor());

		LoadRuleTypes();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        ruletypePanel = new JScrollPane();
        ruletypeTable = new JTable();
        violationtypePanel = new JScrollPane();
        violationtypeTable = new JTable();
        applyViolationTypes = new JButton();

        ruletypeTable.setModel(ruletypeModel);
        ruletypeTable.setFillsViewportHeight(true);
        ruletypeTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked() {
                ruletypeTableMouseClicked();
            }
        });
        ruletypeTable.addKeyListener(new KeyAdapter() {
            public void keyPressed() {
                ruletypeTableKeyPressed();
            }
        });
        ruletypePanel.setViewportView(ruletypeTable);

        violationtypeTable.setModel(violationtypeModel);
        violationtypeTable.setFillsViewportHeight(true);
        violationtypeTable.getTableHeader().setReorderingAllowed(false);
        violationtypePanel.setViewportView(violationtypeTable);

        applyViolationTypes.setText("Apply");
        applyViolationTypes.addActionListener(new ActionListener() {
			@Override
            public void actionPerformed(ActionEvent evt) {
                applyViolationTypesActionPerformed();
            }
        });

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(ruletypePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(violationtypePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(applyViolationTypes)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(violationtypePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(applyViolationTypes)
                .addContainerGap())
            .addComponent(ruletypePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
    }

	private void ruletypeTableMouseClicked() {
		if (ruletypeTable.getSelectedRow() > -1) {
			System.out.print("test");
			loadViolationType((String) ruletypeModel.getValueAt(ruletypeTable.
					getSelectedRow(), 0));
		}
	}

	private void ruletypeTableKeyPressed() {
		if (ruletypeTable.getSelectedRow() > -1) {
			loadViolationType((String) ruletypeModel.getValueAt(ruletypeTable.
					getSelectedRow(), 0));
		}
	}

	private void applyViolationTypesActionPerformed() {
		//TODO: Fix the fetching of the ruletypes en put them in a list to return to the reposetory
		ts.UpdateRuletype(ruletypeModel, violationtypeModel, language);
	}

	private void LoadRuleTypes() {
		for (RuleType ruletype : ruletypes) {
			ruletypeModel.addRow(new Object[]{ruletype.getKey(), "Low"});
		}
	}

	private void loadViolationType(String ruletypeKey) {
		for (RuleType ruletype : ruletypes) {
			if (ruletype.getKey().equals(ruletypeKey)) {
				clearModel(violationtypeModel);
				for (ViolationType violationtype : ruletype.getViolationTypes()) {
					violationtypeModel.addRow(new Object[]{violationtype.
								getViolationtypeKey(), 1,
														   violationtype.
								isActive()});
				}
			}
		}
	}

	private void clearModel(ComboBoxTableModel model) {
		int rows = model.getRowCount();
		while (0 < rows) {
			model.removeRow(0);
			rows--;
		}
	}
}
