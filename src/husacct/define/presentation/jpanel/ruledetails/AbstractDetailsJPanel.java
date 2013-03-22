package husacct.define.presentation.jpanel.ruledetails;

import husacct.ServiceProvider;
import husacct.control.presentation.util.DialogUtils;
import husacct.define.presentation.jdialog.ViolationTypesJDialog;
import husacct.define.task.AppliedRuleController;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

public abstract class AbstractDetailsJPanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = -3429272079796935062L;
    protected AppliedRuleController appliedRuleController;
    protected ViolationTypesJDialog violationTypesJDialog;
    protected int componentCount;
    protected Logger logger;
    protected boolean isException;
    protected boolean showFilterConfigurationButton;
    private JButton configureViolationTypesJButton;

    public AbstractDetailsJPanel(AppliedRuleController appliedRuleController) {
        super();
        this.appliedRuleController = appliedRuleController;
        this.logger = Logger.getLogger(AbstractDetailsJPanel.class);
        isException = false;
        showFilterConfigurationButton = true;
    }

    public void initGui() {
        initGui(false);
    }

    public void initGui(boolean isUsedAsException) {
        try {
            this.removeAll();
            this.setLayout(this.createRuleDetailsLayout());
            this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            setIsUsedAsException(isUsedAsException);
            initDetails();
            initViolationTypes();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    protected GridBagLayout createRuleDetailsLayout() {
        GridBagLayout ruleDetailsLayout = new GridBagLayout();
        ruleDetailsLayout.columnWeights = new double[]{0.0, 0.1};
        ruleDetailsLayout.columnWidths = new int[]{132, 7};
        return ruleDetailsLayout;
    }

    public abstract void initDetails();

    private void initViolationTypes() {
        configureViolationTypesJButton = new JButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ConfigureFilter"));
        configureViolationTypesJButton.addActionListener(this);
        violationTypesJDialog = new ViolationTypesJDialog(appliedRuleController);

        GridBagConstraints gbc = new GridBagConstraints(1, componentCount++, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
        this.add(configureViolationTypesJButton, gbc);

        if (appliedRuleController.isAnalysed()) {
            configureViolationTypesJButton.setEnabled(true);
            configureViolationTypesJButton.setToolTipText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ValidateOnSpecificDependencies"));
        } else {
            configureViolationTypesJButton.setEnabled(false);
            configureViolationTypesJButton.setToolTipText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("NeedToAnalyseFirst"));
        }

        configureViolationTypesJButton.setVisible(showFilterConfigurationButton);
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        if (action.getSource() == this.configureViolationTypesJButton) {
            this.initViolationTypeJDialog();
        }

    }

    private void initViolationTypeJDialog() {
        if (violationTypesJDialog == null) {
            violationTypesJDialog = new ViolationTypesJDialog(appliedRuleController);
        }
        violationTypesJDialog.initGUI();
        DialogUtils.alignCenter(violationTypesJDialog);
        violationTypesJDialog.setVisible(true);
    }

    public void updateDetails(HashMap<String, Object> ruleDetails) {
        violationTypesJDialog.load(ruleDetails);
    }

    public HashMap<String, Object> saveToHashMap() {
        HashMap<String, Object> hashMap = saveDefaultDataToHashMap();
        hashMap.put("dependencies", violationTypesJDialog.save());
        return hashMap;
    }

    private HashMap<String, Object> saveDefaultDataToHashMap() {
        HashMap<String, Object> ruleDetails = new HashMap<String, Object>();
        long moduleFromId = -1;
        long moduleToId = -1;
        boolean enabled = true;
        String description = "";
        String regex = "";
        String[] violationTypes = {};

        ruleDetails.put("moduleFromId", moduleFromId);
        ruleDetails.put("moduleToId", moduleToId);
        ruleDetails.put("enabled", enabled);
        ruleDetails.put("description", description);
        ruleDetails.put("regex", regex);
        ruleDetails.put("violationTypes", violationTypes);
        return ruleDetails;
    }

    public void setIsUsedAsException(boolean isException) {
        this.isException = isException;
    }

    @Override
    public void add(Component comp, Object constraint) {
        super.add(comp, constraint);
        componentCount++;
    }

    public abstract boolean hasValidData();
}
