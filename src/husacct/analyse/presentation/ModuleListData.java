package husacct.analyse.presentation;

import java.util.List;
import husacct.common.dto.AnalysedModuleDTO;
import javax.swing.AbstractListModel;

class ModuleListData extends AbstractListModel{

	private static final long serialVersionUID = 1L;
	private List<AnalysedModuleDTO> modules;

	public ModuleListData(List<AnalysedModuleDTO> modules){
		this.modules = modules;
	}
	
	@Override
	public Object getElementAt(int index) {
		return modules.get(index);
	}

	@Override
	public int getSize() {
		return modules.size();
	}
	
}
