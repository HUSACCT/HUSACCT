package husacct.validate.task.reporting.writer;

import husacct.ServiceProvider;
import husacct.common.dto.RuleDTO;
import husacct.common.enums.ExtensionTypes;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ruletype.RuleTypes;
import husacct.validate.task.TaskServiceImpl;
import husacct.validate.task.reporting.Report;
import husacct.validate.task.reporting.RuleWithNrOfViolationsDTO;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
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

	ReportWriter(Report report, String path, String fileName, ExtensionTypes extensionType, TaskServiceImpl taskServiceImpl) {
		this.report = report;
		this.path = path;
		this.fileName = fileName;
		this.extensionType = extensionType;
		this.taskServiceImpl = taskServiceImpl;
	}

	public abstract void createReport() throws IOException, URISyntaxException, DocumentException;

	protected TreeMap<Integer ,RuleWithNrOfViolationsDTO> getViolatedRulesWithNumberOfViolations(TaskServiceImpl taskServiceImpl) {
		TreeMap<Integer ,RuleWithNrOfViolationsDTO> rulesMap= new TreeMap<Integer, RuleWithNrOfViolationsDTO>();
		Set<String> violatedRules = taskServiceImpl.getViolatedRules();
		int nrOfRule = 1;
		for (String rule : violatedRules) {
			String[] ruleString = rule.split("::");
			List<Violation> violationsPerRule = taskServiceImpl.getViolationsByRule(ruleString[0], ruleString[1], ruleString[2]);
			RuleWithNrOfViolationsDTO ruleDTO = new RuleWithNrOfViolationsDTO(nrOfRule, ruleString[0], ruleString[2], ruleString[1], violationsPerRule.size());
			rulesMap.put(nrOfRule, ruleDTO);
			nrOfRule ++;
		}
		return rulesMap;
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
					RuleWithNrOfViolationsDTO ruleDTO = new RuleWithNrOfViolationsDTO(nrOfRule, rule.moduleFrom.logicalPath, rule.ruleTypeKey, rule.moduleTo.logicalPath, 0);
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
