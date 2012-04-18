package husacct.graphics.task;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.ViolationDTO;

public interface MouseClickListener {

	public void moduleZoom(AbstractDTO zoomedModuleDTO);

	public void showViolations(ViolationDTO[] violationDTOs);
}
