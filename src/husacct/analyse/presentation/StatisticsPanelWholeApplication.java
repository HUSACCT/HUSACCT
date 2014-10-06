package husacct.analyse.presentation;

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
    private JLabel totalPackagesLabel, totalPackagesNumber, totalClassesLabel, totalClassesNumber, totalLinesOfCodeLabel, totalLinesOfCodeNumber, nrOfDependenciesLabel, nrOfDependenciesNumber;
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
		this.add(totalPackagesLabel);
		totalPackagesNumber = new JLabel("-");
		this.add(totalPackagesNumber);
		totalClassesLabel = new JLabel();
		this.add(totalClassesLabel);
		totalClassesNumber = new JLabel("-");
		this.add(totalClassesNumber);
		totalLinesOfCodeLabel = new JLabel();
		this.add(totalLinesOfCodeLabel);
		totalLinesOfCodeNumber = new JLabel("-");
		this.add(totalLinesOfCodeNumber);
		nrOfDependenciesLabel = new JLabel();
		this.add(nrOfDependenciesLabel);
		nrOfDependenciesNumber = new JLabel("-");
		this.add(nrOfDependenciesNumber);
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
	            		.addComponent(nrOfDependenciesLabel))
	                .addGap(20)
	                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
	            		.addComponent(totalPackagesNumber)
	            		.addComponent(totalClassesNumber)
	            		.addComponent(totalLinesOfCodeNumber)
	            		.addComponent(nrOfDependenciesNumber))
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
