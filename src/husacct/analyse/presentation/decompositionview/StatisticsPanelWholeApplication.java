package husacct.analyse.presentation.decompositionview;

import husacct.analyse.presentation.AnalyseUIController;
import husacct.common.help.presentation.HelpableJPanel;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

public class StatisticsPanelWholeApplication extends HelpableJPanel {

	private static final long serialVersionUID = 8505356261388679299L;
    private static final Color PANELBACKGROUND = UIManager.getColor("Panel.background");
    private AnalyseUIController dataControl;
    private JLabel totalPackagesLabel, totalPackagesNumber, totalClassesLabel, totalClassesNumber, totalLinesOfCodeLabel, 
    	totalLinesOfCodeNumber, nrOfDependenciesLabel, nrOfDependenciesNumber, emtyLabelSettingTheSize1, emtyLabelSettingTheSize2;
    private GroupLayout groupLayout;

    public StatisticsPanelWholeApplication(AnalyseUIController uiController) {
		this.dataControl = uiController;
        setBackground(PANELBACKGROUND);
		createWholeApplicationPanel();
		createLayout();
        setLayout(groupLayout);
	}

	private void createWholeApplicationPanel() {
        FlowLayout flowLayout1 = (FlowLayout) this.getLayout();
        flowLayout1.setAlignment(FlowLayout.LEFT);
        this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        this.setBorder(new TitledBorder(dataControl.translate("FullApplication")));
		totalPackagesLabel = new JLabel();
		totalPackagesNumber = new JLabel("-");
		totalClassesLabel = new JLabel();
		totalClassesNumber = new JLabel("-");
		totalLinesOfCodeLabel = new JLabel();
		totalLinesOfCodeNumber = new JLabel("-");
		nrOfDependenciesLabel = new JLabel();
		nrOfDependenciesNumber = new JLabel("-");
		emtyLabelSettingTheSize1 = new JLabel("");
		emtyLabelSettingTheSize2 = new JLabel("");
	}

	private void createLayout() {
		groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
    		groupLayout.createParallelGroup(Alignment.LEADING)
	    		.addGroup(groupLayout.createSequentialGroup()
	                .addContainerGap()
	                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
	                	.addComponent(totalPackagesLabel)
	            		.addComponent(totalClassesLabel)
	            		.addComponent(totalLinesOfCodeLabel)
	            		.addComponent(nrOfDependenciesLabel)
	            		.addComponent(emtyLabelSettingTheSize1, 90, 90, 90))
	                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
	            		.addComponent(totalPackagesNumber)
	            		.addComponent(totalClassesNumber)
	            		.addComponent(totalLinesOfCodeNumber)
	            		.addComponent(nrOfDependenciesNumber)
	            		.addComponent(emtyLabelSettingTheSize1, 80, 80, 80))
	                .addContainerGap()));
        groupLayout.setVerticalGroup(
    		groupLayout.createParallelGroup(Alignment.LEADING)
     		.addGroup(groupLayout.createSequentialGroup()
                .addGap(5)
                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                	.addComponent(totalPackagesLabel)
            		.addComponent(totalPackagesNumber))
        		.addContainerGap()
        		.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
            		.addComponent(totalClassesLabel)
            		.addComponent(totalClassesNumber))
        		.addContainerGap()
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
		        		.addComponent(totalLinesOfCodeLabel)
		        		.addComponent(totalLinesOfCodeNumber))
	    		.addGap(15)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
		        		.addComponent(nrOfDependenciesLabel)
		        		.addComponent(nrOfDependenciesNumber))
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
		        		.addComponent(emtyLabelSettingTheSize1)
		        		.addComponent(emtyLabelSettingTheSize1))
				.addContainerGap()));
	}

	
    public void reload(int totalNrOfPackages, int totalNrOfClasses, int totalNrOfLinesOfCode, int totalNrOfDependencies) {
        totalPackagesLabel.setText(dataControl.translate("PackagesLabel") + ": ");
        totalPackagesNumber.setText("" + totalNrOfPackages);
        totalClassesLabel.setText(dataControl.translate("ClassesLabel") + ": ");
        totalClassesNumber.setText("" + totalNrOfClasses);
        totalLinesOfCodeLabel.setText(dataControl.translate("LinesOfCode") + ": ");
        totalLinesOfCodeNumber.setText("" + totalNrOfLinesOfCode);
        nrOfDependenciesLabel.setText(dataControl.translate("Dependencies") + ": ");
        nrOfDependenciesNumber.setText("" + totalNrOfDependencies);
        this.setBorder(new TitledBorder(dataControl.translate("FullApplication")));
        setLayout(groupLayout);
        this.repaint();
    }
}
