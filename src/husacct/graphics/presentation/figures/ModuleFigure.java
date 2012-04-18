package husacct.graphics.presentation.figures;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.ModuleDTO;

/**
 * @todo this should be drawable
 * @author johan
 * 
 */
public class ModuleFigure extends BaseFigure {
	private static final long serialVersionUID = 3108329703302130066L;

	private AbstractDTO moduleDTO;

	public ModuleFigure(AbstractDTO moduleDTO) {
		super();

		this.moduleDTO = moduleDTO;
	}

	public String getName() {
		if (this.moduleDTO instanceof ModuleDTO) {
			return ((ModuleDTO) this.moduleDTO).logicalPath;
		} else if (this.moduleDTO instanceof AnalysedModuleDTO) {
			return ((AnalysedModuleDTO) this.moduleDTO).name;
		}

		throw new RuntimeException("incompatible dto type '"
				+ this.moduleDTO.getClass().getSimpleName() + "'");
	}

	public AbstractDTO getDTO() {
		return this.moduleDTO;
	}
}
