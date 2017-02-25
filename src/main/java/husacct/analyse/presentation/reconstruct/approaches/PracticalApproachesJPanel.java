package husacct.analyse.presentation.reconstruct.approaches;


import java.awt.event.ActionListener;
import java.io.IOException;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.Algorithm;

public class PracticalApproachesJPanel extends ApproachesJPanel implements ActionListener {
	private static final long serialVersionUID = 8208626960034851199L;
	
	public PracticalApproachesJPanel(AnalyseTaskControl analyseTaskControl) throws IOException {
		super(analyseTaskControl);
	}
	
	@Override
	protected Object[][] getApproachesRows(){
		Object approachesRows[][] = { 
				{Algorithm.Layers_HUSACCT_SelectedModule, getTranslation(Algorithm.Layers_HUSACCT_SelectedModule)},
				{Algorithm.Component_HUSACCT_SelectedModule, getTranslation(Algorithm.Component_HUSACCT_SelectedModule)},
				{Algorithm.Externals_Recognition, getTranslation(Algorithm.Externals_Recognition)},
				{Algorithm.CombinedAndIterative_HUSACCT_SelectedModule, getTranslation(Algorithm.CombinedAndIterative_HUSACCT_SelectedModule)}};
		return approachesRows;
	}

	/*
	 * Method that gets all approaches dynamically.
	@Override
	protected Object[][] getApproachesRows(){
		ArrayList<Object[]> approachesRowsList = new ArrayList<>();
		analyseTaskControl.createReconstructArchitectureList();
		analyseTaskControl.getReconstructArchitectureDTOList().createDynamicReconstructArchitectureDTOs();
		for (ReconstructArchitectureDTO dto : analyseTaskControl.getReconstructArchitectureDTOList().reconstructArchitectureDTOList){
			Object[] rowObject = {dto.approachConstant, dto.getTranslation()};
			approachesRowsList.add(rowObject);
		}
		
		Object[][] approachesRows = new Object[approachesRowsList.size()][];
		approachesRows = approachesRowsList.toArray(approachesRows);
		return approachesRows;
	}
	*/
	
}
