package husacct.graphics.task.modulelayout.layered;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.SoftwareUnitDTO;

public class CreateLayersInGraphic {
    private int layerThreshold = 10;
    private String dependencyType = RelationTypes.allDependencies;
    private IAnalyseService queryService = ServiceProvider.getInstance().getAnalyseService();
    private ArrayList<SoftwareUnitDTO> softwareUnitsInDiagram;
    private TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layers = new TreeMap<>();
    private final Logger logger = Logger.getLogger(CreateLayersInGraphic.class);

    public CreateLayersInGraphic () {
    }

    public TreeMap<Integer, ArrayList<SoftwareUnitDTO>> executeAlgorithm(List<SoftwareUnitDTO> softwareUnitDTOs) {
        try {
            // 1) Fill softwareUnitsInDiagram with software units in implemented architecture diagram.
//            softwareUnitsInDiagram = new ArrayList<SoftwareUnitDTO>();
            // ...

            // 2) Identify layers
            identifyLayers(softwareUnitDTOs, dependencyType);

        } catch (Exception e) {
            logger.warn(" Exception: "  + e );
        }
        // 3) Return layers
        return layers;
    }

    private void identifyLayers(List<SoftwareUnitDTO> units, String depedencyType) {
        // 1) Assign all units to bottom layer
        int layerId = 1;
        ArrayList<SoftwareUnitDTO> assignedUnits = new ArrayList<>();
        assignedUnits.addAll(units);
        layers.put(layerId, assignedUnits);

        // 2) Identify the second layer.
        identifyTopLayerBasedOnUnitsInBottomLayer(layerId, depedencyType);

        // 3) Look iteratively for packages on top of the previous layer.
        while (layers.lastKey() > layerId) {
            layerId++;
            identifyTopLayerBasedOnUnitsInBottomLayer(layerId, depedencyType);
        }

        // 4) Add the layers to the intended architecture
        int highestLevelLayer = layers.size();
        if (highestLevelLayer > 1) {
            // Reverse the layer levels. The numbering of the layers within the
            // intended architecture is different: the highest level layer has
            // hierarchcalLevel = 1
            int lowestLevelLayer = 1;
            int raise = highestLevelLayer - lowestLevelLayer;
            TreeMap<Integer, ArrayList<SoftwareUnitDTO>> tempLayers = new TreeMap<>();
            for (int i = lowestLevelLayer; i <= highestLevelLayer; i++) {
                ArrayList<SoftwareUnitDTO> unitsOfLayer = layers.get(i);
                int level = lowestLevelLayer + raise;
                tempLayers.put(level, unitsOfLayer);
                raise--;
            }
            layers = tempLayers;

        }
    }

    private void identifyTopLayerBasedOnUnitsInBottomLayer(int bottomLayerId, String dependencyType) {
        ArrayList<SoftwareUnitDTO> assignedUnitsOriginalBottomLayer = layers.get(bottomLayerId);
        @SuppressWarnings("unchecked")
        ArrayList<SoftwareUnitDTO> assignedUnitsBottomLayerClone = (ArrayList<SoftwareUnitDTO>) assignedUnitsOriginalBottomLayer
                .clone();
        ArrayList<SoftwareUnitDTO> assignedUnitsNewBottomLayer = new ArrayList<>();
        ArrayList<SoftwareUnitDTO> assignedUnitsTopLayer = new ArrayList<>();

        for (SoftwareUnitDTO softwareUnit : assignedUnitsOriginalBottomLayer) {
            boolean rootPackageDoesNotUseOtherPackage = true;

            for (SoftwareUnitDTO otherSoftwareUnit : assignedUnitsBottomLayerClone) {
                if (!otherSoftwareUnit.uniqueName.equals(softwareUnit.uniqueName)) {
                    int nrOfDependenciesFromsoftwareUnitToOther =0;
                    int nrOfDependenciesFromOtherTosoftwareUnit=0;

                    switch(dependencyType){
                        case RelationTypes.umlLinks:
                            nrOfDependenciesFromsoftwareUnitToOther = queryService.getUmlLinksFromSoftwareUnitToSoftwareUnit(softwareUnit.uniqueName, otherSoftwareUnit.uniqueName).length;
                            nrOfDependenciesFromOtherTosoftwareUnit = queryService.getUmlLinksFromSoftwareUnitToSoftwareUnit(otherSoftwareUnit.uniqueName, softwareUnit.uniqueName).length;
                            break;

                        case RelationTypes.accessCallReferenceDependencies:
                            nrOfDependenciesFromsoftwareUnitToOther = queryService.getDependencies_OnlyAccessCallAndReferences_FromSoftwareUnitToSoftwareUnit(softwareUnit.uniqueName, otherSoftwareUnit.uniqueName).length;
                            nrOfDependenciesFromOtherTosoftwareUnit = queryService.getDependencies_OnlyAccessCallAndReferences_FromSoftwareUnitToSoftwareUnit(otherSoftwareUnit.uniqueName, softwareUnit.uniqueName).length;
                            break;

                        case RelationTypes.allDependencies:
                            nrOfDependenciesFromsoftwareUnitToOther = queryService.getDependenciesFromSoftwareUnitToSoftwareUnit(softwareUnit.uniqueName, otherSoftwareUnit.uniqueName).length;
                            nrOfDependenciesFromOtherTosoftwareUnit = queryService.getDependenciesFromSoftwareUnitToSoftwareUnit(otherSoftwareUnit.uniqueName, softwareUnit.uniqueName).length;
                            break;
                    }

                    if (nrOfDependenciesFromsoftwareUnitToOther > ((nrOfDependenciesFromOtherTosoftwareUnit / 100) * layerThreshold)) {
                        rootPackageDoesNotUseOtherPackage = false;
                    }
                }
            }

            if (rootPackageDoesNotUseOtherPackage) { // Leave unit in the lower layer
                assignedUnitsNewBottomLayer.add(softwareUnit);
            } else { // Assign unit to the higher layer
                assignedUnitsTopLayer.add(softwareUnit);
            }

        }
        if ((assignedUnitsTopLayer.size() > 0) && (assignedUnitsNewBottomLayer.size() > 0)) {
            layers.remove(bottomLayerId);
            layers.put(bottomLayerId, assignedUnitsNewBottomLayer);
            bottomLayerId++;
            layers.put(bottomLayerId, assignedUnitsTopLayer);
        }
    }

    public static class RelationTypes{
        public static final String allDependencies = "AllDependencies";
        public static final String umlLinks = "UmlLinks";
        public static final String accessCallReferenceDependencies = "AccessCallReferenceDependencies";
    }


}
