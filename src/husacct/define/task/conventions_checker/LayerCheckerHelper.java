package husacct.define.task.conventions_checker;

import husacct.ServiceProvider;
import husacct.define.domain.module.ToBeImplemented.ModuleStrategy;
import husacct.define.domain.module.ToBeImplemented.modules.Layer;
import husacct.define.domain.services.ModuleDomainService;

import java.util.ArrayList;

public class LayerCheckerHelper {

    private String errorMessage;
    private ArrayList<Layer> layers;

    public LayerCheckerHelper(ModuleStrategy moduleFrom) {
	layers = new ArrayList<Layer>();
	fillLayerList(moduleFrom);
	setErrorMessage("");
    }

    public boolean checkTypeIsLayer(ModuleStrategy module) {
	if (module.getType() == "Layer") {
	    return true;
	} else {
	    setErrorMessage(ServiceProvider.getInstance().getLocaleService()
		    .getTranslatedString("RuleOnlyForLayers"));
	    return false;
	}
    }

    public void fillLayerList(ModuleStrategy moduleFrom) {
	ArrayList<ModuleStrategy> currentModules = getCurrentModules(moduleFrom);
	for (ModuleStrategy module : currentModules) {
	    if (module instanceof Layer) {
		layers.add((Layer) module);
	    }
	}
    }

    public ArrayList<Layer> getBackCallLayers(Long moduleFromId) {
	ArrayList<Layer> backCallLayers = new ArrayList<Layer>();
	Long firstBackCallLayerId = getPreviousLayerId(moduleFromId);
	if (firstBackCallLayerId != -1L) {
	    for (Layer layer : layers) {
		backCallLayers.add(layer);
		if (layer.getId() == firstBackCallLayerId) {
		    break;
		}
	    }
	}
	return backCallLayers;
    }

    private ArrayList<ModuleStrategy> getCurrentModules(ModuleStrategy moduleFrom) {
	ModuleDomainService moduleService = new ModuleDomainService();
	Long parentId = moduleService.getParentModuleIdByChildId(moduleFrom
		.getId());
	if (parentId != -1) {
	    ModuleStrategy parentModule = moduleService.getModuleById(parentId);
	    moduleService.sortModuleChildren(parentModule);
	    return parentModule.getSubModules();
	} else {
	    return moduleService.getSortedModules();
	}
    }

    public String getErrorMessage() {
	return errorMessage;
    }

    public Long getFirstSkipCallLayer(Long moduleFromId) {
	Long nextLayerId = getNextLayerId(moduleFromId);
	Long layerSkipperToId = getNextLayerId(nextLayerId);
	return layerSkipperToId;
    }

    public ModuleStrategy getLayerById(Long layerId) {
	Layer returnLayer = new Layer();
	for (Layer layer : layers) {
	    if (layer.getId() == layerId) {
		returnLayer = layer;
	    }
	}
	return returnLayer;
    }

    public Long getNextLayerId(Long currentLayerId) {
	int index = 0;
	while (index != layers.size()) {
	    Layer layer = layers.get(index);
	    if (layer.getId() == currentLayerId && index != layers.size() - 1) {
		return layers.get(index + 1).getId();
	    }
	    index++;
	}
	return -1L;
    }

    public Long getPreviousLayerId(Long currentLayerId) {
	int index = 0;
	while (index != layers.size()) {
	    Layer layer = layers.get(index);
	    if (layer.getId() == currentLayerId && index != 0) {
		return layers.get(index - 1).getId();
	    }
	    index++;
	}
	return -1L;
    }

    public ArrayList<Layer> getSkipCallLayers(Long moduleFromId) {
	ArrayList<Layer> skipCallLayers = new ArrayList<Layer>();
	Long firstSkipCallLayerId = getFirstSkipCallLayer(moduleFromId);
	boolean getLayers = false;
	for (Layer layer : layers) {
	    if (layer.getId() == firstSkipCallLayerId) {
		getLayers = true;
	    }
	    if (getLayers) {
		skipCallLayers.add(layer);
	    }
	}
	return skipCallLayers;
    }

    public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
    }
}
