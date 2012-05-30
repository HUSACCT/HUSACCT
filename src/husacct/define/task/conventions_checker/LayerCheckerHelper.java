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
			moduleService.sortModuleChildren(parentModule);
			return parentModule.getSubModules();
		} else {
			return moduleService.getSortedModules();
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
	
	public ArrayList<Layer> getSkipCallLayers(Long moduleFromId) {
		ArrayList<Layer> skipCallLayers = new ArrayList<Layer>();
		Long firstSkipCallLayerId = getFirstSkipCallLayer(moduleFromId);
		boolean getLayers = false;
		for(Layer layer : layers) {
			if(layer.getId() == firstSkipCallLayerId) {
				getLayers = true;
			}
			if(getLayers) {
				skipCallLayers.add(layer);
			}
		}
		return skipCallLayers;
	}
	
	public ArrayList<Layer> getBackCallLayers(Long moduleFromId) {
		ArrayList<Layer> backCallLayers = new ArrayList<Layer>();
		Long firstBackCallLayerId = getPreviousLayerId(moduleFromId);
		if(firstBackCallLayerId != -1L) {
			for(Layer layer : layers) {
				backCallLayers.add(layer);
				if(layer.getId() == firstBackCallLayerId) {
					break;
				}
			}
		}
		return backCallLayers;
	}
	
	public Long getFirstSkipCallLayer(Long moduleFromId) {
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
