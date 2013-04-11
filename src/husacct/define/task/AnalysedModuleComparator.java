package husacct.define.task;

import husacct.common.dto.AnalysedModuleDTO;
import java.util.Comparator;


public class AnalysedModuleComparator implements Comparator<AnalysedModuleDTO>{
 
    @Override
    public int compare(AnalysedModuleDTO dto1, AnalysedModuleDTO dto2) {
 
        return dto1.name.compareTo(dto2.name);
    }
}
