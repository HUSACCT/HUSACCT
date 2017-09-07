package husacct.validate.task.imexporting.reporting;

import husacct.ServiceProvider;
import husacct.common.dto.RuleDTO;
import husacct.common.enums.ExtensionTypes;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ruletype.RuleTypes;
import husacct.validate.task.TaskServiceImpl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import com.itextpdf.text.DocumentException;

public abstract class ReportWriter {

	protected Report report;
	protected String path;
	protected String fileName;
	protected ExtensionTypes extensionType;
	protected final TaskServiceImpl taskServiceImpl;

	protected ReportWriter(Report report, String path, String fileName, ExtensionTypes extensionType, TaskServiceImpl taskServiceImpl) {
		this.report = report;
		this.path = path;
		this.fileName = fileName;
		this.extensionType = extensionType;
		this.taskServiceImpl = taskServiceImpl;
	}

	public abstract void createReport() throws IOException, URISyntaxException, DocumentException;

	protected TreeMap<Integer ,RuleWithNrOfViolationsDTO> getViolatedRulesWithNumberOfViolations(TaskServiceImpl taskServiceImpl) {
		TreeMap<Integer ,RuleWithNrOfViolationsDTO> violatedRulesMap= new TreeMap<Integer, RuleWithNrOfViolationsDTO>();
		Set<String> violatedRules = taskServiceImpl.getViolatedRules();
		int nrOfRule = 1;
		for (String rule : violatedRules) {
			String[] ruleString = rule.split("::");
			List<Violation> violationsPerRule = taskServiceImpl.getViolationsByRule(ruleString[0], ruleString[1], ruleString[2]);
			List<NrOfViolationsPerFromClassToClassDTO> violatingFromToClasses = getNrOfViolationsPerFromClassToClass(violationsPerRule);
			RuleWithNrOfViolationsDTO ruleDTO = new RuleWithNrOfViolationsDTO(nrOfRule, ruleString[0], ruleString[2], ruleString[1], violationsPerRule.size(), violatingFromToClasses);
			violatedRulesMap.put(nrOfRule, ruleDTO);
			nrOfRule ++;
		}
		return violatedRulesMap;
	}
	
	private List<NrOfViolationsPerFromClassToClassDTO> getNrOfViolationsPerFromClassToClass(List<Violation> violationsPerRule){
		ArrayList<NrOfViolationsPerFromClassToClassDTO> violatingFromToClasses = new ArrayList<>();
		// Count number of violations per fromClas-toClass combination.
		TreeMap<String, Integer> violationsPerFromToClassMap = new TreeMap<>();
		for(Violation violation : violationsPerRule){
			String violationFromToKey = "";
			violationFromToKey = violation.getClassPathFrom() + "::" + violation.getClassPathTo();
			violationFromToKey.toLowerCase();
			int nrOfViolations = 1;
			if(violationsPerFromToClassMap.containsKey(violationFromToKey)){
				nrOfViolations = violationsPerFromToClassMap.get(violationFromToKey);
				nrOfViolations ++;
				violationsPerFromToClassMap.put(violationFromToKey, nrOfViolations);
			} else {
				violationsPerFromToClassMap.put(violationFromToKey, nrOfViolations);
			}
		}
		// Create a DTO for each fromClas-toClass combination.
		for (String violationFromToKey : violationsPerFromToClassMap.keySet()) {
			String[] subStrings = violationFromToKey.split("::");
			String fromClass = subStrings[0];
			String toClass = subStrings[1];
			int nrOfViolations = violationsPerFromToClassMap.get(violationFromToKey); 
			NrOfViolationsPerFromClassToClassDTO fromToClassDTO = new NrOfViolationsPerFromClassToClassDTO(fromClass, toClass, nrOfViolations);
			violatingFromToClasses.add(fromToClassDTO);
		}
		return violatingFromToClasses;
	}
	
	protected TreeMap<String ,RuleWithNrOfViolationsDTO> getNonViolatedRulesWithNumberOfViolations(TaskServiceImpl taskServiceImpl) {
		TreeMap<String ,RuleWithNrOfViolationsDTO> nonViolatedRulesMap= new TreeMap<String, RuleWithNrOfViolationsDTO>();
		Set<String> violatedRules = taskServiceImpl.getViolatedRules();
		RuleDTO[] allRules = getAllRulesWithExceptions();
		int nrOfRule = 1;
		for (RuleDTO rule : allRules) {
			if (!rule.isException) {
				String searchKey =  rule.moduleFrom.logicalPath + "::" + rule.moduleTo.logicalPath + "::" + rule.ruleTypeKey;
				if (!violatedRules.contains(searchKey)) {
					ArrayList<NrOfViolationsPerFromClassToClassDTO> violatingFromToClasses = new ArrayList<>();
					RuleWithNrOfViolationsDTO ruleDTO = new RuleWithNrOfViolationsDTO(nrOfRule, rule.moduleFrom.logicalPath, rule.ruleTypeKey, rule.moduleTo.logicalPath, 0, violatingFromToClasses);
					nonViolatedRulesMap.put(searchKey, ruleDTO);
					nrOfRule ++;
				}
			}
		}
		return nonViolatedRulesMap;
	}
	
	protected RuleDTO[] getAllRulesWithExceptions() {
		return ServiceProvider.getInstance().getDefineService().getDefinedRules();
	}
	
	protected String convertIsIndirectBooleanToString(boolean isIndirect) {
		if (isIndirect) {
			return "direct";
		} else {
			return "indirect";
		}
	}

	protected String getDependencyKindValue(String violationtypeKey, boolean indirect) {
		if (!violationtypeKey.isEmpty()) {
			String value = ServiceProvider.getInstance().getLocaleService().getTranslatedString(violationtypeKey);
			value += ", ";
			if (!violationtypeKey.equals(RuleTypes.VISIBILITY_CONVENTION.toString())) {
				if (indirect) {
					value += "indirect";
				} else {
					value += "direct";
				}
			}
			return value;
		}
		return "";
	}

	protected String getCurrentDate() {
		return new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
	}

	protected void createFile() {
		File file = new File(path);
		file.mkdirs();
	}

	protected String getFileName() {
		return path + "/" + fileName;
	}

	protected String translate(String key) {
        return ServiceProvider.getInstance().getLocaleService().getTranslatedString(key);
    }

}
