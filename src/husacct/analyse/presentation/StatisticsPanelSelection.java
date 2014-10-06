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

public class StatisticsPanelSelection extends HelpableJPanel {

	private static final long serialVersionUID = 8505356261336679299L;
    private static final Color PANELBACKGROUND = UIManager.getColor("Panel.background");
    private AnalyseUIController dataControl;
    private JLabel selectionPackagesLabel, selectionPackagesNumber, selectionClassesLabel, selectionClassesNumber, selectionLinesOfCodeLabel, selectionLinesOfCodeNumber;
    private GroupLayout groupLayout;

    public StatisticsPanelSelection(AnalyseUIController uiController) {
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
        this.setBorder(new TitledBorder(dataControl.translate("Selection")));
		selectionPackagesLabel = new JLabel();
		this.add(selectionPackagesLabel);
		selectionPackagesNumber = new JLabel("-");
		this.add(selectionPackagesNumber);
		selectionClassesLabel = new JLabel();
		this.add(selectionClassesLabel);
		selectionClassesNumber = new JLabel("-");
		this.add(selectionClassesNumber);
		selectionLinesOfCodeLabel = new JLabel();
		this.add(selectionLinesOfCodeLabel);
		selectionLinesOfCodeNumber = new JLabel("-");
		this.add(selectionLinesOfCodeNumber);
	}

	private void createLayout() {
		groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
    		groupLayout.createParallelGroup(Alignment.LEADING)
	    		.addGroup(groupLayout.createSequentialGroup()
	                .addContainerGap()
	                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
	                	.addComponent(selectionPackagesLabel)
	            		.addComponent(selectionClassesLabel)
	            		.addComponent(selectionLinesOfCodeLabel))
	                .addGap(22)
	                .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
	            		.addComponent(selectionPackagesNumber)
	            		.addComponent(selectionClassesNumber)
	            		.addComponent(selectionLinesOfCodeNumber))
	                .addContainerGap()));
        groupLayout.setVerticalGroup(
    		groupLayout.createParallelGroup(Alignment.LEADING)
     		.addGroup(groupLayout.createSequentialGroup()
                .addGap(5)
                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                	.addComponent(selectionPackagesLabel)
            		.addComponent(selectionPackagesNumber))
        		.addContainerGap()
        		.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
            		.addComponent(selectionClassesLabel)
            		.addComponent(selectionClassesNumber))
        		.addContainerGap()
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
		        		.addComponent(selectionLinesOfCodeLabel)
		        		.addComponent(selectionLinesOfCodeNumber))
				.addContainerGap()));
	}

	
    public void reload(int selectionNrOfPackages, int selectionNrOfClasses, int selectionNrOfLinesOfCode) {
        selectionPackagesLabel.setText(dataControl.translate("PackagesLabel") + ": ");
        String shownValue;
        if (selectionNrOfPackages > 0) {
        	shownValue = "" + selectionNrOfPackages;
        } else {
        	shownValue = "-";
        }
        selectionPackagesNumber.setText(shownValue);
        selectionClassesLabel.setText(dataControl.translate("ClassesLabel") + ": ");
        if (selectionNrOfClasses > 0) {
        	shownValue = "" + selectionNrOfClasses;
        } else {
        	shownValue = "-";
        }
        selectionClassesNumber.setText(shownValue);
        selectionLinesOfCodeLabel.setText(dataControl.translate("LinesOfCode") + ": ");
        if (selectionNrOfLinesOfCode > 0) {
        	shownValue = "" + selectionNrOfLinesOfCode;
        } else {
        	shownValue = "-";
        }
        selectionLinesOfCodeNumber.setText(shownValue);
        this.setBorder(new TitledBorder(dataControl.translate("Selection")));
        setLayout(groupLayout);
        this.repaint();
    }
}
