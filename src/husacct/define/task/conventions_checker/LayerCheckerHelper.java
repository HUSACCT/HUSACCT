package husacct.define.task.conventions_checker;

import java.util.ArrayList;

import husacct.define.abstraction.language.DefineTranslator;
import husacct.define.domain.module.Module;
import husacct.define.domain.module.Layer;
import husacct.define.domain.services.ModuleDomainService;

public class LayerCheckerHelper {
	
	private ArrayList<Layer> layers;
	private String errorMessage;
	
	public LayerCheckerHelper(Module moduleFrom) {
		this.layers = new ArrayList<Layer>();
		this.fillLayerList(moduleFrom);
		this.setErrorMessage("");
	}
	
	public void fillLayerList(Module moduleFrom) {
		ArrayList<Module> currentModules = getCurrentModules(moduleFrom);
		for(Module module : currentModules) {
			if(module instanceof Layer) {
				layers.add((Layer) module);
			}
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
	
	public boolean checkTypeIsLayer(Module module) {
		if(module.getType() == "Layer") {
			return true;
		} else {
			this.setErrorMessage(DefineTranslator.translate("RuleOnlyForLayers"));
			return false;
		}
	}
	
	public boolean checkHasSkippedToLayer(Module moduleFrom) {
		Long skippedLayerId = getLayerSkippedToId(moduleFrom.getId());
		if(skippedLayerId != -1L) {
			return true;
		} else {
			this.setErrorMessage(DefineTranslator.translate("SkipLayerNotExists"));
			return false;
		}
	}
	
	public boolean checkHasLayerCalledBackTo(Module moduleFrom) {
		Long calledBackLayerId = getPreviousLayerId(moduleFrom.getId());
		if(calledBackLayerId != -1L) {
			return true;
		} else {
			this.setErrorMessage(DefineTranslator.translate("CallbackLayerNotExists"));
			return false;
		}
	}
	
	public Long getLayerSkippedToId(Long moduleFromId) {
		Long nextLayerId = getNextLayerId(moduleFromId);
		Long layerSkipperToId = getNextLayerId(nextLayerId);
		return layerSkipperToId;
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
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
