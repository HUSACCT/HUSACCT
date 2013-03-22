package husacct.define.presentation.jdialog;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.control.ControlServiceImpl;
import husacct.define.presentation.jpanel.ruledetails.AbstractDetailsJPanel;
import husacct.define.presentation.jpanel.ruledetails.FactoryDetails;
import husacct.define.presentation.utils.KeyValueComboBox;
import husacct.define.presentation.utils.UiDialogs;
import husacct.define.task.AppliedRuleController;
import husacct.define.task.PopUpController;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class ExceptionRuleJDialog extends JDialog implements KeyListener, ActionListener, ItemListener {

    private static final long serialVersionUID = -3491664038962722000L;
    private AppliedRuleController appliedRuleController;
    private FactoryDetails factoryDetails;
    public AbstractDetailsJPanel ruleDetailsJPanel;
    public KeyValueComboBox exceptionRuleKeyValueComboBox;
    private JPanel mainPanel;
    public JButton cancelButton;
    public JButton saveButton;
    private AppliedRuleJDialog appliedRuleFrame;

    /**
     * Constructor
     */
    public ExceptionRuleJDialog(AppliedRuleController appliedRulesController, AppliedRuleJDialog appliedRuleFrame) {
        super(((ControlServiceImpl) ServiceProvider.getInstance().getControlService()).getMainController().getMainGui(), true);
        this.appliedRuleController = appliedRulesController;
        this.appliedRuleFrame = appliedRuleFrame;
        this.factoryDetails = new FactoryDetails();

        this.initGUI();
        this.setTextures();
    }

    private void setTextures() {
        if (this.appliedRuleController.getAction().equals(PopUpController.ACTION_NEW)) {
            this.saveButton.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("CreateException"));
            this.setTitle(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ExceptionRuleTitle"));
        } else if (this.appliedRuleController.getAction().equals(PopUpController.ACTION_EDIT)) {
            this.saveButton.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Save"));
            this.setTitle(ServiceProvider.getInstance().getLocaleService().getTranslatedString("EditExceptionRuleTitle"));
        }
    }

    /**
     * Creating Gui
     */
    private void initGUI() {
        try {
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            setTitle(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ExceptionRuleTitle"));
            setIconImage(new ImageIcon(Resource.get(Resource.HUSACCT_LOGO)).getImage());

            getContentPane().add(this.createMainPanel(), BorderLayout.CENTER);
            getContentPane().add(this.createButtonPanel(), BorderLayout.SOUTH);
//			this.setResizable(false);
            this.pack();
            this.setSize(820, 620);
        } catch (Exception e) {
            // add your error handling code here
            e.printStackTrace();
        }
    }

    private JPanel createMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(this.createMainPanelLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

        mainPanel.add(new JLabel(ServiceProvider.getInstance().getLocaleService().getTranslatedString("RuleType")), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        this.createAppliedRuleKeyValueComboBox();
        mainPanel.add(this.exceptionRuleKeyValueComboBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        this.createRuleDetailPanel();
        mainPanel.add(this.ruleDetailsJPanel, new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        return mainPanel;
    }

    private GridBagLayout createMainPanelLayout() {
        GridBagLayout mainPanelLayout = new GridBagLayout();
        mainPanelLayout.rowWeights = new double[]{0.0, 0.0};
        mainPanelLayout.rowHeights = new int[]{30, 420};
        mainPanelLayout.columnWeights = new double[]{0.0, 0.0};
        mainPanelLayout.columnWidths = new int[]{130, 660};
        return mainPanelLayout;
    }

    private void createAppliedRuleKeyValueComboBox() {
        this.exceptionRuleKeyValueComboBox = new KeyValueComboBox();
        this.appliedRuleController.fillRuleTypeComboBoxWithExceptions(this.exceptionRuleKeyValueComboBox);
        this.exceptionRuleKeyValueComboBox.addItemListener(this);
    }

    private void createRuleDetailPanel() {
        String ruleTypeKey = this.exceptionRuleKeyValueComboBox.getSelectedItemKey();
        this.ruleDetailsJPanel = factoryDetails.create(this.appliedRuleController, ruleTypeKey);
        this.ruleDetailsJPanel.setIsUsedAsException(true);
        this.ruleDetailsJPanel.initGui(true);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();

        this.saveButton = new JButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Add"));
        buttonPanel.add(this.saveButton);
        this.saveButton.addActionListener(this);

        this.cancelButton = new JButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Cancel"));
        buttonPanel.add(this.cancelButton);
        this.cancelButton.addActionListener(this);


        return buttonPanel;
    }

    /**
     * Handling ActionPerformed
     */
    @Override
    public void actionPerformed(ActionEvent action) {
        if (action.getSource() == this.saveButton) {
            this.save();
        } else if (action.getSource() == this.cancelButton) {
            this.cancel();
        }
    }

    /**
     * Handling ItemListener
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == this.exceptionRuleKeyValueComboBox) {
            String ruleTypeKey = this.exceptionRuleKeyValueComboBox.getSelectedItemKey();
            this.appliedRuleController.setSelectedRuleTypeKey(ruleTypeKey);

            this.mainPanel.remove(this.ruleDetailsJPanel);

            this.ruleDetailsJPanel = factoryDetails.create(this.appliedRuleController, ruleTypeKey);
            this.ruleDetailsJPanel.initGui(true);

            // updating panel!
            if (this.getComponentCount() > 0) {
                this.getRootPane().revalidate();
            }
            this.getContentPane().repaint();
            mainPanel.add(this.ruleDetailsJPanel, new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

        }
    }

    private void cancel() {
        this.dispose();
    }

    private void save() {
        if (ruleDetailsJPanel.hasValidData()) {
            HashMap<String, Object> ruleDetails = this.ruleDetailsJPanel.saveToHashMap();
            String ruleTypeKey = this.exceptionRuleKeyValueComboBox.getSelectedItemKey();
            ruleDetails.put("ruleTypeKey", ruleTypeKey);
            this.appliedRuleController.addException(ruleDetails);
            this.appliedRuleFrame.updateExceptionTable();
            this.dispose();
        } else {
            UiDialogs.errorDialog(this, ServiceProvider.getInstance().getLocaleService().getTranslatedString("CorrectDataError"));
        }
    }

    /**
     * Handling KeyPresses
     */
    public void keyPressed(KeyEvent arg0) {
        // Ignore
    }

    public void keyReleased(KeyEvent arg0) {
        if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.dispose();
        } else if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
            this.save();
        }
    }

    public void keyTyped(KeyEvent arg0) {
        // Ignore
    }
}
