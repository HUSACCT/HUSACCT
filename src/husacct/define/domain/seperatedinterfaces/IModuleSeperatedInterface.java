package husacct.define.domain.seperatedinterfaces;

import husacct.define.domain.module.ModuleStrategy;

public interface IModuleSeperatedInterface extends IseparatedDefinition {
	public void addSeperatedModule(ModuleStrategy module);
	public void removeSeperatedModule(ModuleStrategy module);
	public void layerUp(long moduleID);
	public void layerDown(long moduleID);
}
