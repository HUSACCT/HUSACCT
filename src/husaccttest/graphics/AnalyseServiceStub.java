package husaccttest.graphics;

import javax.swing.JInternalFrame;

import husacct.analyse.IAnalyseService;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.services.IServiceListener;

public class AnalyseServiceStub implements IAnalyseService {

	@Override
	public void addServiceListener(IServiceListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyServiceListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	public void analyseApplication() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAnalysed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JInternalFrame getJInternalFrame() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DependencyDTO[] getDependencies(String from, String to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DependencyDTO[] getDependenciesFrom(String from) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DependencyDTO[] getDependenciesTo(String to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DependencyDTO[] getDependencies(String from, String to, String[] dependencyFilter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DependencyDTO[] getDependenciesFrom(String from, String[] dependencyFilter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DependencyDTO[] getDependenciesTo(String to, String[] dependencyFilter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getAvailableLanguages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnalysedModuleDTO getModuleForUniqueName(String uniquename) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnalysedModuleDTO[] getRootModules() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnalysedModuleDTO[] getChildModulesInModule(String from) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnalysedModuleDTO[] getChildModulesInModule(String from, int depth) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AnalysedModuleDTO getParentModuleForModule(String child) {
		// TODO Auto-generated method stub
		return null;
	}

}
