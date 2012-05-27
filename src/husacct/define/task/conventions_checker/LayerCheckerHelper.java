package husacct.define.task.conventions_checker;

import java.util.ArrayList;

import husacct.define.abstraction.language.DefineTranslator;
import husacct.define.domain.module.Module;
import husacct.define.domain.module.Layer;
import husacct.define.domain.services.ModuleDomainService;

public class LayerCheckerHelper {
	
	private ArrayList<Layer> layers;
	private String errorMessage;
	
	private ModuleCheckerHelper moduleCheckerHelper;
	
	public LayerCheckerHelper(Module moduleFrom) {
		this.layers = new ArrayList<Layer>();
		this.fillLayerList(moduleFrom);
		this.setErrorMessage("");
		this.moduleCheckerHelper = new ModuleCheckerHelper();
	}
	
	public void fillLayerList(Module moduleFrom) {
		ArrayList<Module> currentModules = getCurrentModules(moduleFrom);
		for(Module module : currentModules) {
			if(module instanceof Layer) {
				layers.add((Layer) module);
			}
		}
	}
	
	public boolean checkLayerSkippedTo(Module moduleFrom) {
		if(checkTypeIsLayer(moduleFrom)) {
			Long skippedLayerId = getLayerSkippedToId(moduleFrom.getId());
			if(skippedLayerId != -1L) {
				return moduleCheckerHelper.checkRuleTypeAlreadyFromThisToSelected("IsNotAllowedToUse", moduleFrom, getLayerById(skippedLayerId));
			} else {
				this.setErrorMessage(DefineTranslator.translate("SkipLayerNotExists"));
				return false;
			}
		} else {
			return false;
		}
	}
	
	private boolean checkTypeIsLayer(Module module) {
		if(module.getType() == "Layer") {
			return true;
		} else {
			this.setErrorMessage(DefineTranslator.translate("RuleOnlyForLayers"));
			return false;
		}
	}
	
	private ArrayList<Module> getCurrentModules(Module moduleFrom) {
		ModuleDomainService moduleService = new ModuleDomainService();
		Long parentId = moduleService.getParentModuleIdByChildId(moduleFrom.getId());
		if(parentId != -1) {
			Module parentModule = moduleService.getModuleById(parentId);
			return parentModule.getSubModules();
		} else {
			ArrayList<Module> currentModules = new ArrayList<Module>();
			for(Module module : moduleService.getRootModules()) {
				currentModules.add(module);
			}
			return currentModules;
		}
	}
	
	public Long getLayerSkippedToId(Long moduleFromId) {
		Long nextLayerId = getNextLayerId(moduleFromId);
		Long LayerSkipperToId = getNextLayerId(nextLayerId);
		return LayerSkipperToId;
	}
	
	public Long getNextLayerId(Long currentLayerId) {
		int index = 0;
		while(index != layers.size()) {
			Layer layer = layers.get(index);
			if(layer.getId() == currentLayerId && index != (layers.size()-1)) {
				return layers.get(index+1).getId();
			}
			index++;
		}
		return -1L;
	}
	
	public Long getPreviousLayerId(Long currentLayerId) {
		int index = 0;
		while(index != layers.size()) {
			Layer layer = layers.get(index);
			if(layer.getId() == currentLayerId && index != 0) {
				return layers.get(index-1).getId();
			}
			index++;
		}
		return -1L;
	}
	
	public Module getLayerById(Long layerId) {
		Layer returnLayer = new Layer();
		for(Layer layer : layers) {
			if(layer.getId() == layerId) {
				returnLayer = layer;
			}
		}
		return returnLayer;
	}

	public String getErrorMessage() {
		if(this.errorMessage == "") {
			return moduleCheckerHelper.getErrorMessage();
		}
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
