package husacct.control.presentation.workspace.savers;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.control.IControlService;
import husacct.control.presentation.util.FileDialog;
import husacct.control.presentation.util.Regex;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class HusacctSavePanel extends SaverPanel {

    private static final long serialVersionUID = 1L;
    private JLabel pathLabel, descriptionLabel;
    private JTextField pathText;
    private JButton browseButton;
    private File selectedFile;
    private GridBagConstraints constraints;
    private IControlService controlService = ServiceProvider.getInstance().getControlService();
    private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();

    public HusacctSavePanel() {
        super();
        setup();
        addComponents();
        setListeners();
    }

    private void setup() {
        setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
    }

    private void addComponents() {

        descriptionLabel = new JLabel(localeService.getTranslatedString("SaveToHusacct"));
        pathLabel = new JLabel(localeService.getTranslatedString("PathLabel"));
        pathText = new JTextField(20);
        browseButton = new JButton(localeService.getTranslatedString("BrowseButton"));
        pathText.setEnabled(false);

        JPanel hiddenPanel = new JPanel();
        hiddenPanel.setPreferredSize(new Dimension(100, 10));

        add(descriptionLabel, getConstraint(0, 0, 3, 1));
        add(pathLabel, getConstraint(0, 1, 1, 1));
        add(pathText, getConstraint(1, 1, 2, 1));
        add(hiddenPanel, getConstraint(1, 2, 1, 1));
        add(browseButton, getConstraint(2, 2, 1, 1));
    }

    private GridBagConstraints getConstraint(int gridx, int gridy, int gridwidth, int gridheight) {
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(3, 3, 3, 3);
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
        return constraints;
    }

    private void setListeners() {
        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                showFileDialog();
            }
        });
    }

    protected void showFileDialog() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("hu", "hu");
        FileDialog fileDialog = new FileDialog(JFileChooser.FILES_ONLY, localeService.getTranslatedString("SaveButton"), filter);
        int returnVal = fileDialog.showDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (fileDialog.getSelectedFile().exists()) {
                setFile(fileDialog.getSelectedFile());
            } else {
                setFile(new File(fileDialog.getSelectedFile().getAbsolutePath() + "." + fileDialog.getFileFilter().getDescription()));
            }
        }
    }

    private void setFile(File file) {
        selectedFile = file;
        pathText.setText(file.getAbsolutePath());
    }

    @Override
    public HashMap<String, Object> getData() {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("file", selectedFile);
        return data;
    }

    @Override
    public boolean validateData() {
        if (selectedFile == null) {
            controlService.showErrorMessage(localeService.getTranslatedString("NoFileLocationError"));
            return false;
        } else if (!Regex.matchRegex(Regex.filenameRegex, selectedFile.getName())) {
            controlService.showErrorMessage(localeService.getTranslatedString("InvalidFilenameError"));
            return false;
        }
        return true;
    }
}
