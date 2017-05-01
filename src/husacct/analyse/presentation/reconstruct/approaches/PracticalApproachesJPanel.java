package husacct.analyse.presentation.reconstruct.approaches;


import java.awt.event.ActionListener;
import java.io.IOException;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.Algorithms;

public class PracticalApproachesJPanel extends ApproachesJPanel implements ActionListener {
	private static final long serialVersionUID = 8208626960034851199L;
	
	public PracticalApproachesJPanel(AnalyseTaskControl analyseTaskControl) throws IOException {
		super(analyseTaskControl);
	}
	
	@Override
	protected Object[][] getApproachesRows(){
		Object approachesRows[][] = { 
				{Algorithms.Layers_HUSACCT_SelectedModule, getTranslation(Algorithms.Layers_HUSACCT_SelectedModule)},
				{Algorithms.Component_HUSACCT_SelectedModule, getTranslation(Algorithms.Component_HUSACCT_SelectedModule)},
				{Algorithms.Externals_Recognition, getTranslation(Algorithms.Externals_Recognition)},
				{Algorithms.CombinedAndIterative_HUSACCT_SelectedModule, getTranslation(Algorithms.CombinedAndIterative_HUSACCT_SelectedModule)}};
		return approachesRows;
	}
}
