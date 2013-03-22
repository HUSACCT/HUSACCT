package husacct.validate.presentation.languageSeverityConfiguration;

import husacct.ServiceProvider;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.ViolationType;
import husacct.validate.presentation.DataLanguageHelper;
import husacct.validate.presentation.tableModels.ComboBoxTableModel;
import husacct.validate.task.TaskServiceImpl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import org.apache.log4j.Logger;

class ViolationTypeSeverityPanel extends JPanel {

    private static final long serialVersionUID = 1283848062887016417L;
    private static Logger logger = Logger.getLogger(ViolationTypeSeverityPanel.class);
    private ComboBoxTableModel violationtypeModel;
    private TaskServiceImpl taskServiceImpl;
    private JButton apply, restore, restoreAll;
    private JList category;
    private JScrollPane categoryScrollpane, violationtypeScrollpane;
    private JTable violationtypeTable;
    private final DefaultListModel categoryModel;
    private final String language;
    private final Map<String, List<ViolationType>> violationTypes;
    private List<Severity> severities;

    ViolationTypeSeverityPanel(TaskServiceImpl taskServiceImpl, ConfigurationViolationTypeDTO configurationViolationTypeDTO) {

        categoryModel = new DefaultListModel();
        this.taskServiceImpl = taskServiceImpl;
        this.language = configurationViolationTypeDTO.getLanguage();
        this.violationTypes = configurationViolationTypeDTO.getViolationtypes();
        this.severities = configurationViolationTypeDTO.getSeverities();

        initComponents();
        loadModel();
        setText();
        loadViolationTypeCategories();
    }

    private void initComponents() {

        categoryScrollpane = new JScrollPane();
        category = new JList();
        violationtypeScrollpane = new JScrollPane();
        violationtypeTable = new JTable();
        restore = new JButton();
        restoreAll = new JButton();
        apply = new JButton();

        category.setModel(categoryModel);
        category.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        category.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent evt) {
                if (!evt.getValueIsAdjusting() && category.getSelectedIndex() > -1) {
                    categoryValueChanged();
                }
            }
        });
        categoryScrollpane.setViewportView(category);

        violationtypeTable.setFillsViewportHeight(true);
        violationtypeTable.getTableHeader().setReorderingAllowed(false);
        violationtypeTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        violationtypeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }
                checkRestoreButtonEnabled();
            }
        });

        violationtypeScrollpane.setViewportView(violationtypeTable);

        restore.setEnabled(false);
        restore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (violationtypeTable.getSelectedRow() > -1) {
                    restoreActionPerformed();
                } else {
                    ServiceProvider.getInstance().getControlService().showInfoMessage((ServiceProvider.getInstance().getLocaleService().getTranslatedString("RowNotSelected")));
                }
            }
        });

        restoreAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                restoreAllActionPerformed();
            }
        });

        apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                applyActionPerformed();
            }
        });

        createLayout();
    }

    private void createLayout() {
        GroupLayout violationtypeSeverityLayout = new GroupLayout(this);

        GroupLayout.ParallelGroup horizontalButtonGroup = violationtypeSeverityLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false);
        horizontalButtonGroup.addComponent(restore, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        horizontalButtonGroup.addComponent(restoreAll, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        horizontalButtonGroup.addComponent(apply, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);

        GroupLayout.SequentialGroup horizontalPaneGroup = violationtypeSeverityLayout.createSequentialGroup();
        horizontalPaneGroup.addComponent(categoryScrollpane);
        horizontalPaneGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        horizontalPaneGroup.addComponent(violationtypeScrollpane);
        horizontalPaneGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        horizontalPaneGroup.addGroup(horizontalButtonGroup);
        horizontalPaneGroup.addContainerGap();

        violationtypeSeverityLayout.setHorizontalGroup(horizontalPaneGroup);

        GroupLayout.SequentialGroup verticalButtonGroup = violationtypeSeverityLayout.createSequentialGroup();
        verticalButtonGroup.addContainerGap();
        verticalButtonGroup.addComponent(restore);
        verticalButtonGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        verticalButtonGroup.addComponent(restoreAll);
        verticalButtonGroup.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        verticalButtonGroup.addComponent(apply);
        verticalButtonGroup.addContainerGap();

        GroupLayout.ParallelGroup verticalPaneGroup = violationtypeSeverityLayout.createParallelGroup(GroupLayout.Alignment.TRAILING);
        verticalPaneGroup.addComponent(categoryScrollpane);
        verticalPaneGroup.addComponent(violationtypeScrollpane);
        verticalPaneGroup.addGroup(verticalButtonGroup);

        violationtypeSeverityLayout.setVerticalGroup(verticalPaneGroup);

        setLayout(violationtypeSeverityLayout);
    }

    void loadAfterChange() {
        setText();
        loadModel();
    }

    private void setText() {
        category.setBorder(BorderFactory.createTitledBorder(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Category")));
        restore.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("RestoreToDefault"));
        restoreAll.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("RestoreAllToDefault"));
        apply.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Apply"));
    }

    void setSeverities(List<Severity> severities) {
        this.severities = severities;
    }

    final void loadModel() {
        String[] violationtypeModelHeaders = {ServiceProvider.getInstance().getLocaleService().getTranslatedString("Violationtype"), ServiceProvider.getInstance().getLocaleService().getTranslatedString("Severity")};
        violationtypeModel = new ComboBoxTableModel(violationtypeModelHeaders, 0, severities);
        violationtypeModel.setTypes(new Class[]{String.class, Severity.class});
        violationtypeModel.setCanEdit(new Boolean[]{false, true});

        violationtypeTable.setModel(violationtypeModel);

        TableColumnModel tcm2 = violationtypeTable.getColumnModel();
        tcm2.getColumn(1).setCellEditor(violationtypeModel.getEditor());
    }

    private void restoreActionPerformed() {
        taskServiceImpl.restoreKeyToDefaultSeverity(language, ((DataLanguageHelper) violationtypeModel.getValueAt(violationtypeTable.getSelectedRow(), 0)).key);
        categoryValueChanged();
    }

    private void restoreAllActionPerformed() {
        taskServiceImpl.restoreAllKeysToDefaultSeverities(language);
        categoryValueChanged();
        ServiceProvider.getInstance().getControlService().showInfoMessage((ServiceProvider.getInstance().getLocaleService().getTranslatedString("AllRestored")));
    }

    private void applyActionPerformed() {
        checkRestoreButtonEnabled();
        updateViolationtypeSeverities();
    }

    private void categoryValueChanged() {
        checkRestoreButtonEnabled();
        loadViolationType(((DataLanguageHelper) category.getSelectedValue()).key);
    }

    private void updateViolationtypeSeverities() {
        HashMap<String, Severity> map = new HashMap<String, Severity>();

        for (int i = 0; i < violationtypeModel.getRowCount(); i++) {
            String key = ((DataLanguageHelper) violationtypeModel.getValueAt(i, 0)).key;
            map.put(key, (Severity) violationtypeModel.getValueAt(i, 1));

        }

        taskServiceImpl.updateSeverityPerType(map, language);
    }

    private void loadViolationTypeCategories() {
        categoryModel.clear();
        for (String categoryString : violationTypes.keySet()) {
            categoryModel.addElement(new DataLanguageHelper(categoryString));
        }
        if (!categoryModel.isEmpty()) {
            category.setSelectedIndex(0);
        }
    }

    private void loadViolationType(String category) {
        if (violationtypeModel != null) {
        }
        violationtypeModel.clear();
        for (String categoryString : violationTypes.keySet()) {
            if (categoryString.equals(category)) {
                List<ViolationType> violationtypes = violationTypes.get(categoryString);
                for (ViolationType violationtype : violationtypes) {
                    Severity severity;
                    try {
                        severity = taskServiceImpl.getSeverityFromKey(language, violationtype.getViolationtypeKey());
                    } catch (Exception e) {
                        logger.error(e);
                        severity = taskServiceImpl.getAllSeverities().get(0);
                    }
                    violationtypeModel.addRow(new Object[]{new DataLanguageHelper(violationtype.getViolationtypeKey()), severity});
                }
            }

        }
        violationtypeModel.checkValuesAreValid();
    }

    private void checkRestoreButtonEnabled() {
        if (violationtypeTable.getSelectedRow() > -1) {
            restore.setEnabled(true);
        } else {
            restore.setEnabled(false);
        }
    }

    public void clearSelection() {
        violationtypeTable.getSelectionModel().clearSelection();
        category.getSelectionModel().clearSelection();
        selectFirstIndexOfCategory();
    }

    public void selectFirstIndexOfCategory() {
        category.setSelectedIndex(0);
    }
}