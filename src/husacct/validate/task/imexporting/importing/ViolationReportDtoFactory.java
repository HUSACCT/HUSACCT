package husacct.validate.task.imexporting.importing;

import husacct.common.dto.ViolationImExportDTO;
import husacct.common.dto.ViolationReportDTO;
import husacct.validate.domain.configuration.ViolationRepository;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.logicalmodule.LogicalModule;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;
import husacct.validate.task.TaskServiceImpl;
import husacct.validate.task.imexporting.exporting.ExportNewViolations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jdom2.Element;

public class ViolationReportDtoFactory {
	private TaskServiceImpl task;
	private List<Violation> allCurrentViolations;
	private List<ViolationImExportDTO> allCurrentViolationsImExportList;
	private Calendar timeCurrentCheck;

	private ViolationRepository previousViolationRepository;
	private List<ViolationImExportDTO> newViolationsList; 	// Contains current violations not detected in the previousViolationsDtoList 
	private List<ViolationImExportDTO> previousViolationsDtoList;
	private Calendar previousValidationDate;
	private HashMap<String, Boolean> hasNumberOfViolationsPerRuleIncreased; // Contains value "true" if the number of violations for a rule (key) has increased. 
    private Logger logger = Logger.getLogger(ViolationReportDtoFactory.class);
    
    public ViolationReportDtoFactory(TaskServiceImpl task) {
    	this.task = task;
    	allCurrentViolations = task.getAllViolations().getValue();
    	allCurrentViolationsImExportList = createAllCurrentViolationsImExportList(allCurrentViolations);
    	timeCurrentCheck = task.getAllViolations().getKey();
    }

	public ViolationReportDTO getViolationReportDTO(Element previousViolations, String exportFilePathAllCurrentViolations, String exportFilePathNewViolations) {
		ViolationReportDTO violationReportDTO = new ViolationReportDTO();
		try {
			// Add the results of the current SACC to violationReportDTO
	        violationReportDTO.setTimeCurrentCheck(timeCurrentCheck);
	        int nrOfAllCurrentViolations = allCurrentViolationsImExportList.size();
	        violationReportDTO.setAllViolations(allCurrentViolationsImExportList.toArray(new ViolationImExportDTO[nrOfAllCurrentViolations]));
			violationReportDTO.setNrOfAllCurrentViolations(nrOfAllCurrentViolations);

	        // Create allCurrentViolations exportFile, if needed
	        if ((exportFilePathAllCurrentViolations != null) && !exportFilePathAllCurrentViolations.equals("")) {
	        	
	        }
	        
			// Determine if new violations have to be identified
			if ((previousViolations != null) && !previousViolations.equals("")) {
		        this.logger.info(new Date().toString() + " Start: Identify New Violations");
				importPreviousViolations(previousViolations);
		    	addPreviousViolationsToRepository();
		    	determineIncreaseOfViolationsPerRule();
		    	violationReportDTO = filterNewViolations(violationReportDTO);
		        this.logger.info(new Date().toString() + " Finished: Identify New Violations");
			}

	        // Create newViolations exportFile, if needed
	        if ((newViolationsList.size() > 0) && (exportFilePathNewViolations != null) && !exportFilePathAllCurrentViolations.equals("")) {
	        	new ExportNewViolations().createReport(newViolationsList, timeCurrentCheck, exportFilePathNewViolations);
	        }
		} catch (Exception e){
			logger.warn("Exception: " + e.getCause().toString());
		}
        return violationReportDTO;
	}

	private void importPreviousViolations(Element previousViolations) {
		ImportViolations importer = new ImportViolations(previousViolations);
    	previousViolationsDtoList = importer.importViolations();
    	previousValidationDate = importer.getValidationDate();
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
	
	// Fills map hasNumberOfViolationsPerRUleIncreased
	private void determineIncreaseOfViolationsPerRule() {
    	hasNumberOfViolationsPerRuleIncreased = new HashMap<>();
    	Set<String> previouslyViolatedRules = previousViolationRepository.getViolatedRules();
    	for (String previouslyViolatedRule : previouslyViolatedRules) {
    		String[] ruleKeys = previouslyViolatedRule.split("::");
    		int previousNrOfViolations = previousViolationRepository.getViolationsByRule(ruleKeys[0], ruleKeys[1], ruleKeys[2]).size();
    		int currentNrOfViolations = task.getViolationsByRule(ruleKeys[0], ruleKeys[1], ruleKeys[2]).size();
    		boolean increaseOfViolationsPerRule = false;
    		if (currentNrOfViolations > previousNrOfViolations) {
    			increaseOfViolationsPerRule = true;
    		}
    		hasNumberOfViolationsPerRuleIncreased.put(previouslyViolatedRule, increaseOfViolationsPerRule);
    	}
	}
	
	/* Add each current violation that is not found in the previousViolationRepository to the newViolationsList if
	 * the number of violations has increased for the rule.
	 * The last check limits the number of false positives in cases where the class-name or lineNumber have changed.
	 */
	private ViolationReportDTO filterNewViolations(ViolationReportDTO violationReportDTO) {
    	newViolationsList = new ArrayList<ViolationImExportDTO>();
		for (Violation currenViolation : allCurrentViolations) {
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
				// Determine if the number of violations has increased for the rule.
				String searchKey = currenViolation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath() 
						+ "::" + currenViolation.getLogicalModules().getLogicalModuleTo().getLogicalModulePath() 
						+ "::" + currenViolation.getRuletypeKey();
				if (hasNumberOfViolationsPerRuleIncreased.containsKey(searchKey) && hasNumberOfViolationsPerRuleIncreased.get(searchKey)) {
					// If so, add the violation to the newViolationsList
					newViolationsList.add(createViolationImportExportDTO(currenViolation));
				}
			}
		}
        violationReportDTO.setNewViolations(newViolationsList.toArray(new ViolationImExportDTO[newViolationsList.size()]));
        violationReportDTO.setNrOfNewViolations(newViolationsList.size());
        violationReportDTO.setNrOfAllPreviousViolations(previousViolationsDtoList.size());
        violationReportDTO.setTimePreviousCheck(previousValidationDate);
		return violationReportDTO;
	}

	private List<ViolationImExportDTO> createAllCurrentViolationsImExportList(List<Violation> allCurrentViolations) {
		List<ViolationImExportDTO> returnList = new ArrayList<ViolationImExportDTO>();
		for (Violation violation : allCurrentViolations) {
			returnList.add(createViolationImportExportDTO(violation));
		}
		return returnList;
	}
	
	private ViolationImExportDTO createViolationImportExportDTO(Violation violation) {
		ViolationImExportDTO newViolation = new ViolationImExportDTO();
		newViolation.setFrom(violation.getClassPathFrom());
		newViolation.setTo(violation.getClassPathTo());
		newViolation.setLine(violation.getLinenumber());
		newViolation.setDepType(violation.getViolationTypeKey());
		newViolation.setDepSubType(violation.getDependencySubType());
		newViolation.setFrom(violation.getClassPathFrom());
		newViolation.setIndirect(violation.getIsIndirect());
		newViolation.setSeverity(violation.getSeverity().getSeverityKey());
		newViolation.setMessage(task.getMessage(violation));
		newViolation.setRuleType(violation.getRuletypeKey());
		newViolation.setFromMod(violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath());
		newViolation.setToMod(violation.getLogicalModules().getLogicalModuleTo().getLogicalModulePath());
		return newViolation;
	}
}
