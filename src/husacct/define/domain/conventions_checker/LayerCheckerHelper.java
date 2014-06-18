package husacct.define.domain.conventions_checker;

import husacct.ServiceProvider;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.module.modules.Layer;
import husacct.define.domain.services.ModuleDomainService;

import java.util.ArrayList;


public class LayerCheckerHelper {

    private String errorMessage;
    private ArrayList<Layer> layers;

    public LayerCheckerHelper(ModuleStrategy moduleFrom) {
	//layers = new ArrayList<Layer>();
	//fillLayerList(moduleFrom);
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
    
    public ArrayList<ModuleStrategy> getBackCallLayers(Long moduleFromId) {
    	ArrayList<ModuleStrategy> backCallLayers = new ArrayList<ModuleStrategy>();
    	Long firstBackCallLayerId = getPreviousLayerId(moduleFromId);
    	if (firstBackCallLayerId != -1L) {
    	    for (Layer layer : layers) {
    		backCallLayers.add(layer);
    		ArrayList<ModuleStrategy> subModules = layer.getSubModules();
    		for(ModuleStrategy module : subModules) {
    			backCallLayers.add(module);
    		}
    		if (layer.getId() == firstBackCallLayerId) {
    		    break;
    		}
    	    }
    	}
    	return backCallLayers;
        }

    private ArrayList<ModuleStrategy> getCurrentModules(ModuleStrategy moduleFrom) {
    	ModuleDomainService moduleService =new  ModuleDomainService();
    	return moduleService.getSortedModules();
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

    public ArrayList<ModuleStrategy> getSkipCallLayers(Long moduleFromId) {
	ArrayList<ModuleStrategy> skipCallLayers = new ArrayList<ModuleStrategy>();
	Long firstSkipCallLayerId = getFirstSkipCallLayer(moduleFromId);
	boolean getLayers = false;
	for (Layer layer : layers) {
	    if (layer.getId() == firstSkipCallLayerId) {
		getLayers = true;
	    }
	    if (getLayers) {
		skipCallLayers.add(layer);
		ArrayList<ModuleStrategy> subModules = layer.getSubModules();
		for(ModuleStrategy module : subModules) {
			skipCallLayers.add(module);
		}
	    }
	}
	return skipCallLayers;
    }

    public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
    }
}
