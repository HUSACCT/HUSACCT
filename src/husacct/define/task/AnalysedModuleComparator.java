package husacct.define.task;

import husacct.common.dto.AnalysedModuleDTO;

import java.util.Comparator;

public class AnalysedModuleComparator implements Comparator<AnalysedModuleDTO> {

    @Override
    public int compare(AnalysedModuleDTO dto1, AnalysedModuleDTO dto2) {
    	int result;
    	if ( dto1 == null){
    		result = 1;
    		return result;
    	} 
    	if (dto2 == null){
    		result = -1;
    		return result;
    	} 
    	result = dto1.name.compareTo(dto2.name);
    	return result;
    }
}
