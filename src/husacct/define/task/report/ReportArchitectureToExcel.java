package husacct.define.task.report;

import husacct.ServiceProvider;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.RuleDTO;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.ModuleDomainService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;


public class ReportArchitectureToExcel extends ReportArchitectureAbstract {

    private Logger husacctLogger = Logger.getLogger(ReportArchitectureToExcel.class);
    private WritableWorkbook workbook;
    private WritableCellFormat timesBold, timesBold_AlignmentRight, timesBold_AlignmentCentre;
    private WritableCellFormat times, times_AlignmentRight, times_AlignmentCentre;
    private Map<Integer, Integer> dimensions = new HashMap<Integer, Integer>();
    private int sheetNr = 0;
    private int rowNrAllModules = 1; // Identifies each row in the allModulesSheet
    private int moduleNr = 1; // identifies each module in the allModulesSheet

	public ReportArchitectureToExcel() {
        super();
    }

    public void write(String path) {
    	File file = new File(path);
        WorkbookSettings documentSettings = new WorkbookSettings();
        documentSettings.setLocale(ServiceProvider.getInstance().getLocaleService().getLocale());
        try {
            createLayoutDefaults();
            workbook = Workbook.createWorkbook(file);
            workbook.createSheet(super.translate("AllModules"), sheetNr);
            WritableSheet allModulesSheet = workbook.getSheet(sheetNr);
            sheetNr ++;
            writeAllModules(allModulesSheet);
            workbook.createSheet(super.translate("AllRulesWithExceptions"), sheetNr);
            WritableSheet allRulesSheet = workbook.getSheet(sheetNr);
            sheetNr ++;
            writeAllRulesWithExceptions(allRulesSheet);
            workbook.write();
            workbook.close();
        } catch (IOException e) {
            husacctLogger.warn("Analyse - Couldn export dependencies to xls - File unknwon");
        } catch (WriteException e) {
            husacctLogger.warn("ExceptionMessage: " + e.getMessage());
        }
   	
    }

    private void createLayoutDefaults() throws WriteException{
        WritableFont times10 = new WritableFont(WritableFont.TIMES, 10);
        times = new WritableCellFormat(times10);
        times.setWrap(false);
        times_AlignmentRight = new WritableCellFormat(times10);
        times_AlignmentRight.setWrap(false);
        times_AlignmentRight.setAlignment(Alignment.RIGHT);
        times_AlignmentCentre = new WritableCellFormat(times10);
        times_AlignmentCentre.setWrap(false);
        times_AlignmentCentre.setAlignment(Alignment.CENTRE);
        WritableFont times10Bold = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false);
        timesBold = new WritableCellFormat(times10Bold);
        timesBold.setWrap(false);
        timesBold_AlignmentRight = new WritableCellFormat(times10Bold);
        timesBold_AlignmentRight.setWrap(false);
        timesBold_AlignmentRight.setAlignment(Alignment.RIGHT);
        timesBold_AlignmentCentre = new WritableCellFormat(times10Bold);
        timesBold_AlignmentCentre.setWrap(false);
        timesBold_AlignmentCentre.setAlignment(Alignment.CENTRE);

        CellView cv = new CellView();
        cv.setFormat(times);
        cv.setFormat(timesBold);
        cv.setAutosize(false);
    }
    
    private void writeAllRulesWithExceptions(WritableSheet sheet) throws WriteException {
    	// Write Application Name
    	ApplicationDTO applicationDTO = ServiceProvider.getInstance().getDefineService().getApplicationDetails();
        addCellBold(sheet, 0, 1, super.translate("AllRulesWithExceptionsOfApplication") + ": " + applicationDTO.name);
        // Write all Rules
        writeAllRulesWithExceptionsTableHeaders(sheet, 3);
        int row = 4;
		RuleDTO[] allRules = super.getAllRulesWithExceptions();
		TreeMap<String, RuleDTO> rulesMap = new TreeMap<String ,RuleDTO>();
        for (RuleDTO rule : allRules) {
			String searchKey =  rule.moduleFrom.logicalPath + "::" + rule.moduleTo.logicalPath + "::" + rule.ruleTypeKey;
				rulesMap.put(searchKey, rule);
        }
        int id = 1;
        for (String searchKey : rulesMap.keySet()) {
        	RuleDTO rule = rulesMap.get(searchKey);
        	String ruleTypeKey = rule.ruleTypeKey;
        	String toModuleReported = "";
			if ((ruleTypeKey.equals("IsNotAllowedToUse")) || (ruleTypeKey.equals("IsOnlyAllowedToUse")) || (ruleTypeKey.equals("IsTheOnlyModuleAllowedToUse")) 
					|| (ruleTypeKey.equals("InheritanceConvention")) || (ruleTypeKey.equals("MustUse")) || (ruleTypeKey.equals("IsAllowedToUse"))){
				toModuleReported = rule.moduleTo.logicalPath;
			} else {
				toModuleReported = ""; //Do not show the module to. Logically there is no module to, but technically module to is the same as module from.
			}
        	
        	if (!rule.isException) {
            	writeRuleOrException(sheet, row, Integer.toString(id), "", rule.moduleFrom.logicalPath, rule.ruleTypeKey, toModuleReported, rule.regex);
            	id ++;
            	row ++;
            	RuleDTO[] exceptionRules = rule.exceptionRules;
            	for (RuleDTO exceptionRule : exceptionRules) {
            		writeRuleOrException(sheet, row, "", super.translate("Exception"), exceptionRule.moduleFrom.logicalPath, exceptionRule.ruleTypeKey, exceptionRule.moduleTo.logicalPath, exceptionRule.regex);
                	row ++;
            	}
        	}
        }
        sheet.setColumnView(0, 10);
        sheet.setColumnView(1, 10);
        sheet.setColumnView(2, 10);
    }

	private void writeAllRulesWithExceptionsTableHeaders(WritableSheet sheet, int row) {
        try {
	        Label idLabel = new Label(1, row, super.translate("Id"), timesBold_AlignmentCentre);
	        Label exceptionsLabel = new Label(2, row, super.translate("Exceptions"), timesBold);
	        Label fromLabel = new Label(3, row, super.translate("LogicalModuleFrom"), timesBold);
	        Label ruleTypeLabel = new Label(4, row, super.translate("RuleType"), timesBold);
	        Label toLabel = new Label(5, row, super.translate("LogicalModuleTo"), timesBold);
	        Label expressionLabel = new Label(6, row, super.translate("RegularExpression"), timesBold);
	        
	        sheet.addCell(idLabel);
	        sheet.addCell(exceptionsLabel);
	        sheet.addCell(fromLabel);
	        sheet.addCell(ruleTypeLabel);
	        sheet.addCell(toLabel);
	        sheet.addCell(expressionLabel);
        } catch (Exception e) {
            husacctLogger.error("ExceptionMessage: " + e.getMessage());
        }
	}
    
    private void writeRuleOrException(WritableSheet sheet, int row, String id, String exception, String from, String ruleType, String to, String expression) throws RowsExceededException, WriteException {
        Label idLabel = new Label(1, row, id, times_AlignmentCentre);
        Label exceptionLabel = new Label(2, row, exception, times);
        Label fromLabel = new Label(3, row, from, times);
        Label ruleTypeLabel = new Label(4, row, super.translate(ruleType), times);
        Label toLabel = new Label(5, row, to, times);
        Label expressionLabel = new Label(6, row, expression, times);

        List<Label> labelArray = new ArrayList<Label>();
        labelArray.add(idLabel);
        labelArray.add(exceptionLabel);
        labelArray.add(fromLabel);
        labelArray.add(ruleTypeLabel);
        labelArray.add(toLabel);
        labelArray.add(expressionLabel);
        
        for(Label label : labelArray){
        	sheet.addCell(label);
        	if(dimensions.get(label.getColumn()) == null || dimensions.get(label.getColumn()) < label.getString().length()){
        		if(label.getString().length() < 10)
        			dimensions.put(label.getColumn(), 10);
        		else
        			dimensions.put(label.getColumn(), label.getString().length() - 3);
            }
        }
        
        for(int i : dimensions.keySet()){
        	sheet.setColumnView(i, dimensions.get(i));
        }
    }


    private void writeAllModules(WritableSheet sheet) throws WriteException {
    	// Write Application Name
    	ApplicationDTO applicationDTO = ServiceProvider.getInstance().getDefineService().getApplicationDetails();
        addCellBold(sheet, 0, rowNrAllModules, super.translate("AllModulesOfApplication") + ": " + applicationDTO.name);
        // Write all Modules
        rowNrAllModules = rowNrAllModules + 2;
        writeAllModulesTableHeaders(sheet, rowNrAllModules);
        rowNrAllModules ++;
    	writeModuleRows(sheet, getRootModules(), 0);
    	
    	
    }
    
	private void writeAllModulesTableHeaders(WritableSheet sheet, int row) {
        try {
	        Label idLabel = new Label(1, row, super.translate("Id"), timesBold_AlignmentCentre);
	        Label moduleLabel = new Label(2, row, super.translate("Module"), timesBold);
	        Label softwareUnitLabel = new Label(3, row, super.translate("AssignedSoftwareUnitsTitle"), timesBold);
	        Label ruleTypeLabel = new Label(4, row, super.translate("EnabledRules"), timesBold);
	        sheet.addCell(idLabel);
	        sheet.addCell(moduleLabel);
	        sheet.addCell(softwareUnitLabel);
	        sheet.addCell(ruleTypeLabel);
        } catch (Exception e) {
            husacctLogger.error("ExceptionMessage: " + e.getMessage());
        }
	}
    
    private void writeModuleRows(WritableSheet sheet, List<ModuleStrategy> modules, int moduleIndentNr){ // moduleIndent = Whitespace before submodules; enlarges with depth of tree
		for(ModuleStrategy module : modules) {
			String moduleName = getModuleIndent(moduleIndentNr) + module.getName() + " (" + super.translate(module.getType()).toLowerCase() + ")";
			HashMap<String, String> softwareUnits = module.getSoftwareUnitNames(); // HashMap<name, type>
			Set<String> suSet = new TreeSet<String>(softwareUnits.keySet());
			Object[] suArray = suSet.toArray();
			int suArraySize = suArray.length;
			HashMap<String, Boolean> appliedRules = getAppliedRules(module.getId()); // HashMap<name, type>
			Set<String> arSet = new TreeSet<String>(appliedRules.keySet());
			Object[] arArray = arSet.toArray();
			int arArraySize = arArray.length;
			int largestSetSize = suArraySize;
			if (arArraySize > suArraySize) {
				largestSetSize = arArraySize;
			}
			for(int rowNrForModule = 0; rowNrForModule < largestSetSize; rowNrForModule++) { // Number which rises with the number of software units or rules of the module
				String mNumber = "";
				String mName = "";
				String suName = "";
				String rtName = "";
				if (rowNrForModule == 0) {
					mNumber = Integer.toString(moduleNr);
					mName = moduleName;
				}
				if (rowNrForModule < suArraySize) {
					suName = (String) suArray[rowNrForModule] + " (" + softwareUnits.get(suArray[rowNrForModule]).toLowerCase() + ")";
				}
				if ((rowNrForModule < arArraySize) && appliedRules.get(arArray[rowNrForModule])) {
					rtName = (String) arArray[rowNrForModule];
				}
				writeModuleRow(sheet, mNumber, mName, suName, rtName);
			}
			moduleNr ++;
			if(module.hasSubModules()){
				int indentNr = moduleIndentNr + 2;
				writeModuleRows(sheet, module.getSubModules(), indentNr);
			}
		}
	}

    
    private String getModuleIndent(int moduleIndentNr) {
		String moduleIndent = "";
		for(int i = 0; i < moduleIndentNr; i++) {
			moduleIndent = moduleIndent + "    ";
		}
		return moduleIndent;
    }
    
    private void writeModuleRow(WritableSheet sheet, String id, String module, String softwareUnit, String ruleType) {
    	try {
	    	Label idLabel = new Label(1, rowNrAllModules, id, times_AlignmentCentre);
	        Label moduleLabel = new Label(2, rowNrAllModules, module, times);
	        Label softwareUnitLabel = new Label(3, rowNrAllModules, softwareUnit, times);
	        Label ruleTypeLabel = new Label(4, rowNrAllModules, super.translate(ruleType), times);
	
	        List<Label> labelArray = new ArrayList<Label>();
	        labelArray.add(idLabel);
	        labelArray.add(moduleLabel);
	        labelArray.add(softwareUnitLabel);
	        labelArray.add(ruleTypeLabel);
	        
	        for(Label label : labelArray){
	        	sheet.addCell(label);
	        	if(dimensions.get(label.getColumn()) == null || dimensions.get(label.getColumn()) < label.getString().length()){
	        		if(label.getString().length() < 10)
	        			dimensions.put(label.getColumn(), 10);
	        		else
	        			dimensions.put(label.getColumn(), label.getString().length() - 3);
	            }
	        }
	        rowNrAllModules ++;
	        
	        for(int i : dimensions.keySet()){
	        	sheet.setColumnView(i, dimensions.get(i));
	        }
        } catch (Exception e) {
            husacctLogger.error("ExceptionMessage: " + e.getMessage());
        }
	        
    }

    private void addCellBold(WritableSheet sheet, int column, int row, String s) throws RowsExceededException, WriteException {
        Label label;
        label = new Label(column, row, s, timesBold);
        sheet.addCell(label);
    }

    private void addCellBold_AlignmentRight(WritableSheet sheet, int column, int row, String s) throws RowsExceededException, WriteException {
        Label label;
        label = new Label(column, row, s, timesBold_AlignmentRight);
        sheet.addCell(label);
    }

    private void addCellNumber(WritableSheet sheet, int column, int row, int value) throws RowsExceededException, WriteException {
        Number number;
        number = new Number(column, row, value, times);
        sheet.addCell(number);
    }
    private void addCellDefault(WritableSheet sheet, int column, int row, String s) throws RowsExceededException, WriteException {
        Label label;
        label = new Label(column, row, s, times);
        sheet.addCell(label);
    }
    
}
