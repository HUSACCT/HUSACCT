package husacct.analyse.presentation.decompositionview;

import husacct.analyse.presentation.AnalyseUIController;
import husacct.analyse.serviceinterface.dto.AnalysisStatisticsDTO;
import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
import husacct.common.help.presentation.HelpableJPanel;

import java.awt.Color;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

public class StatisticsPanel extends HelpableJPanel {

	private static final long serialVersionUID = 8505333261388679299L;
    private static final Color PANELBACKGROUND = UIManager.getColor("Panel.background");
    private AnalyseUIController dataControl;
    private StatisticsPanelWholeApplication wholeApplicationPanel;
    private StatisticsPanelSelection selectionPanel;

    public StatisticsPanel(AnalyseUIController uiController) {
		this.dataControl = uiController;
		createBaseLayout();
        setBorder(new TitledBorder(dataControl.translate("Statistics")));
        setBackground(PANELBACKGROUND);
	}

	private void createBaseLayout() {
		wholeApplicationPanel = new StatisticsPanelWholeApplication(dataControl);
		selectionPanel = new StatisticsPanelSelection(dataControl);

        GroupLayout group1Layout = new GroupLayout(this);
        group1Layout.setHorizontalGroup(
    		group1Layout.createParallelGroup(Alignment.TRAILING)
     		.addGroup(group1Layout.createSequentialGroup()
 				.addContainerGap()
 				.addGroup(group1Layout.createParallelGroup(Alignment.LEADING)
					.addComponent(wholeApplicationPanel, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
	                .addComponent(selectionPanel, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
            );
        group1Layout.setVerticalGroup(
    		group1Layout.createParallelGroup(Alignment.LEADING)
     		.addGroup(group1Layout.createSequentialGroup()
				.addComponent(wholeApplicationPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
 				.addGap(10)
                .addComponent(selectionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
			);
        setLayout(group1Layout);
	}

    public void reload(SoftwareUnitDTO selectedModule) {
    	AnalysisStatisticsDTO statistics;
    	if ((selectedModule != null) && !selectedModule.name.equals("xLibraries")) {
	    	statistics = dataControl.getAnalyseTaskControl().getAnalysisStatistics(selectedModule);
		} else {
	    	statistics = dataControl.getAnalyseTaskControl().getAnalysisStatistics(null);
		}
        wholeApplicationPanel.reload(statistics.totalNrOfPackages, statistics.totalNrOfClasses, statistics.totalNrOfLinesOfCode, statistics.totalNrOfDependencies);
        selectionPanel.reload(statistics.selectionNrOfPackages, statistics.selectionNrOfClasses, statistics.selectionNrOfLinesOfCode);
        this.repaint();
    }
}
