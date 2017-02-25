package husacct.analyse.presentation.reconstruct.approaches;

import java.awt.event.ActionListener;
import java.io.IOException;

import husacct.analyse.task.AnalyseTaskControl;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.Algorithm;

public class ResearchApproachesHUPanel extends ApproachesJPanel implements ActionListener{
	private static final long serialVersionUID = 10903678111609565L;
	
	public ResearchApproachesHUPanel(AnalyseTaskControl analyseTaskControl) throws IOException {
		super(analyseTaskControl);
	}
	
	@Override
	protected Object[][] getApproachesRows(){
		Object approachesRows[][] = { 
				{Algorithm.Layers_HUSACCT_SAEroCon2016, getTranslation(Algorithm.Layers_HUSACCT_SAEroCon2016)},
				{Algorithm.Layers_Scanniello_Improved, getTranslation(Algorithm.Layers_Scanniello_Improved)},
				{Algorithm.Layers_Scanniello_Original, getTranslation(Algorithm.Layers_Scanniello_Original)},
				{Algorithm.Layers_Goldstein_HUSACCT_SelectedModule, getTranslation(Algorithm.Layers_Goldstein_HUSACCT_SelectedModule)},
				{Algorithm.Layers_Goldstein_Root_Original, getTranslation(Algorithm.Layers_Goldstein_Root_Original)},
				{Algorithm.Gateways_HUSACCT_Root, getTranslation(Algorithm.Gateways_HUSACCT_Root)}};
		return approachesRows;
	}
	
}
