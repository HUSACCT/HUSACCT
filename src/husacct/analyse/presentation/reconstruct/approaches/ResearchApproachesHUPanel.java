package husacct.analyse.presentation.reconstruct.approaches;

import java.awt.event.ActionListener;
import java.io.IOException;

import husacct.analyse.task.AnalyseTaskControl;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.Algorithms;

import javax.swing.*;

public class ResearchApproachesHUPanel extends ApproachesJPanel implements ActionListener{
	private static final long serialVersionUID = 10903678111609565L;
	
	public ResearchApproachesHUPanel(JFrame mainFrame, AnalyseTaskControl analyseTaskControl) throws IOException {
		super(mainFrame, analyseTaskControl);
	}
	
	@Override
	protected Object[][] getApproachesRows(){
		Object approachesRows[][] = { 
				{Algorithms.Layers_HUSACCT_SAEroCon2016, getTranslation(Algorithms.Layers_HUSACCT_SAEroCon2016)},
				{Algorithms.Layers_Scanniello_Improved, getTranslation(Algorithms.Layers_Scanniello_Improved)},
				{Algorithms.Layers_Scanniello_Original, getTranslation(Algorithms.Layers_Scanniello_Original)},
				{Algorithms.Layers_Goldstein_HUSACCT_SelectedModule, getTranslation(Algorithms.Layers_Goldstein_HUSACCT_SelectedModule)},
				{Algorithms.Layers_Goldstein_Root_Original, getTranslation(Algorithms.Layers_Goldstein_Root_Original)},
				{Algorithms.Gateways_HUSACCT_Root, getTranslation(Algorithms.Gateways_HUSACCT_Root)}};
		return approachesRows;
	}
	
}
