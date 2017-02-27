package husacct.validate.task.imexporting.importing;

import husacct.common.dto.ViolationImExportDTO;
import husacct.validate.domain.configuration.ViolationRepository;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.logicalmodule.LogicalModule;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;
import husacct.validate.task.TaskServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom2.Element;

public class IdentifyNewViolations {
	private TaskServiceImpl task;
	private ViolationRepository previousViolationRepository;
	private List<ViolationImExportDTO> newViolationsList; 	// Contains current violations not detected in the previousViolationsDtoList 
	private List<ViolationImExportDTO> previousViolationsDtoList;
    private Logger logger = Logger.getLogger(IdentifyNewViolations.class);
    
    public IdentifyNewViolations(TaskServiceImpl task) {
    	this.task = task;
    }

	public List<ViolationImExportDTO> identifyNewViolations(Element previousViolations) {
        this.logger.info(new Date().toString() + " Starting: Identify New Violations");
    	importPreviousViolations(previousViolations);
    	addPreviousViolationsToRepository();
    	filterNewViolations();
        this.logger.info(new Date().toString() + " Finished: Identify New Violations");
        return newViolationsList;
	}

	private void importPreviousViolations(Element previousViolations) {
		ImportViolations importer = new ImportViolations();
    	previousViolationsDtoList = importer.importViolations(previousViolations);
	}
	
	private void addPreviousViolationsToRepository() { // Only attributes needed for filtering are set.
        previousViolationRepository = new ViolationRepository();
        for (ViolationImExportDTO previousViolationDTO : previousViolationsDtoList) {
        	Violation previousViolation = new Violation()
			.setRuletypeKey(previousViolationDTO.getRuleType())
			.setLogicalModules(getLogicalModules(previousViolationDTO))
			.setClassPathFrom(previousViolationDTO.getFrom())
			.setClassPathTo(previousViolationDTO.getTo())
			.setLineNumber(previousViolationDTO.getLine())
			.setViolationTypeKey(previousViolationDTO.getDepType())
			.setdependencySubType(previousViolationDTO.getDepSubType())
			.setInDirect(previousViolationDTO.isIndirect());
        	previousViolationRepository.addViolation(previousViolation);
        }
        previousViolationRepository.filterAndSortAllViolations();
	}
	
	private LogicalModules getLogicalModules(ViolationImExportDTO previousViolationDTO) {
		LogicalModule logicalModuleFrom = new LogicalModule(previousViolationDTO.getFromMod(), "");
		LogicalModule logicalModuleTo = new LogicalModule(previousViolationDTO.getToMod(), ""); 
		LogicalModules logicalModules = new LogicalModules(logicalModuleFrom, logicalModuleTo);
		return logicalModules;
	}
	
	// Add each current violation that is not found in the previousViolationRepository to the newViolationsList 
	private void filterNewViolations() {
    	newViolationsList = new ArrayList<>();
		List<Violation> currentViolations = task.getAllViolations().getValue();
		for (Violation currenViolation : currentViolations) {
			boolean isViolationNew = true;
			// violationFromToKey = violation.getClassPathFrom() + "::" + violation.getClassPathTo();
			List<Violation> previousFromToViolations= previousViolationRepository.getViolationsFromTo(currenViolation.getClassPathFrom(), currenViolation.getClassPathTo());
			// violationDetailsKey = violation.getRuletypeKey() + "::" + violation.getLinenumber() + "::" + violation.getIsIndirect() + "::" + violation.getViolationTypeKey() + "::" + violation.getDependencySubType();
			for (Violation previousViolation : previousFromToViolations) {
				if (currenViolation.getRuletypeKey().equals(previousViolation.getRuletypeKey())) {
					if (currenViolation.getLinenumber() == previousViolation.getLinenumber()) {
						if (currenViolation.getIsIndirect() == previousViolation.getIsIndirect()) {
							if (currenViolation.getViolationTypeKey().equals(previousViolation.getViolationTypeKey())) {
								if (currenViolation.getDependencySubType().equals(previousViolation.getDependencySubType())) {
									isViolationNew = false;
								}
							}
						}
					}
				}
			}
			if (isViolationNew) {
				ViolationImExportDTO newViolation = new ViolationImExportDTO();
				newViolation.setFrom(currenViolation.getClassPathFrom());
				newViolation.setTo(currenViolation.getClassPathTo());
				newViolation.setLine(currenViolation.getLinenumber());
				newViolation.setDepType(currenViolation.getViolationTypeKey());
				newViolation.setDepSubType(currenViolation.getDependencySubType());
				newViolation.setFrom(currenViolation.getClassPathFrom());
				newViolation.setIndirect(currenViolation.getIsIndirect());
				newViolation.setSeverity(currenViolation.getSeverity().getSeverityKey());
				newViolation.setRuleType(currenViolation.getRuletypeKey());
				newViolation.setFromMod(currenViolation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath());
				newViolation.setToMod(currenViolation.getLogicalModules().getLogicalModuleTo().getLogicalModulePath());
				newViolationsList.add(newViolation);
			}

		}
	}
}
