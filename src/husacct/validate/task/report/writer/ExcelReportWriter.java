package husacct.validate.task.report.writer;

import husacct.ServiceProvider;
import husacct.analyse.abstraction.export.AbstractFileExporter;
import husacct.analyse.domain.IAnalyseDomainService;
import husacct.common.dto.AnalysisStatisticsDTO;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.DependencyDTO;
import husacct.validate.domain.factory.message.Messagebuilder;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.report.Report;
import husacct.validate.task.extensiontypes.ExtensionTypes.ExtensionType;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;


public class ExcelReportWriter extends ReportWriter {

    private IAnalyseDomainService analysedDomain;
    private Logger husacctLogger = Logger.getLogger(ExcelReportWriter.class);
    private WritableWorkbook workbook;
    private WritableCellFormat timesBold;
    private WritableCellFormat timesBold_AlignmentRight;
    private WritableCellFormat times;
    private Map<Integer, Integer> dimensions = new HashMap<Integer, Integer>();

    // Variables for statistics: dependency.type
    private int numberOfAllDependencies_Total = 0;
    private int numberOfAllDependencies_Direct = 0;
    private int numberOfAllDependencies_Indirect = 0;
    private int numberOfAllDependencies_Import = 0;
    private int numberOfAllDependencies_Import_Direct = 0;
    private int numberOfAllDependencies_Import_Indirect = 0;
    private int numberOfAllDependencies_Declaration = 0;
    private int numberOfAllDependencies_Declaration_Direct = 0;
    private int numberOfAllDependencies_Declaration_Indirect = 0;
    private int numberOfAllDependencies_Annotation = 0;
    private int numberOfAllDependencies_Annotation_Direct = 0;
    private int numberOfAllDependencies_Annotation_Indirect = 0;
    private int numberOfAllDependencies_Access = 0;
    private int numberOfAllDependencies_Access_Direct = 0;
    private int numberOfAllDependencies_Access_Indirect = 0;
    private int numberOfAllDependencies_Call = 0;
    private int numberOfAllDependencies_Call_Direct = 0;
    private int numberOfAllDependencies_Call_Indirect = 0;
    private int numberOfAllDependencies_Inheritance = 0;
    private int numberOfAllDependencies_Inheritance_Direct = 0;
    private int numberOfAllDependencies_Inheritance_Indirect = 0;
    private int numberOfInheritanceRelatedDependencies_Total = 0;
    private int numberOfInheritanceRelatedDependencies_Total_Direct = 0;
    private int numberOfInheritanceRelatedDependencies_Total_Indirect = 0;
    private int numberOfInheritanceRelatedDependencies_Access = 0;
    private int numberOfInheritanceRelatedDependencies_Access_Direct = 0;
    private int numberOfInheritanceRelatedDependencies_Access_Indirect = 0;
    private int numberOfInheritanceRelatedDependencies_Call = 0;
    private int numberOfInheritanceRelatedDependencies_Call_Direct = 0;
    private int numberOfInheritanceRelatedDependencies_Call_Indirect = 0;
    private int numberOfInnerClassRelatedDependencies_Total = 0;
    private int numberOfInnerClassRelatedDependencies_Total_Direct = 0;
    private int numberOfInnerClassRelatedDependencies_Total_Indirect = 0;
    // Variables for statistics: dependency.type
    private int numberOff_Access_Variable = 0;
    private int numberOff_Access_EnumerationVariable = 0;
    private int numberOff_Access_InterfaceVariable = 0;
    private int numberOff_Access_LibraryVariable = 0;
    private int numberOff_Access_InstanceVariable = 0;
    private int numberOff_Access_InstanceVariableConstant = 0;
    private int numberOff_Access_ClassVariable = 0;
    private int numberOff_Access_ClassVariableConstant = 0;
    private int numberOff_Access_Reference = 0;
    private int numberOff_Access_ReferenceReturnTypeUsedMethod = 0;
    private int numberOff_Access_ReferenceReturnTypeUsedMethod_Direct = 0;
    private int numberOff_Access_ReferenceReturnTypeUsedMethod_Indirect = 0;
    private int numberOff_Access_ReferenceTypeOfUsedVariable = 0;
    private int numberOff_Access_ReferenceTypeOfUsedVariable_Direct = 0;
    private int numberOff_Access_ReferenceTypeOfUsedVariable_Indirect = 0;
    private int numberOff_Call_Method = 0;
    private int numberOff_Call_EnumerationMethod = 0;
    private int numberOff_Call_InterfaceMethod = 0;
    private int numberOff_Call_LibraryMethod = 0;
    private int numberOff_Call_Constructor = 0;
    private int numberOff_Call_ClassMethod = 0;
    private int numberOff_Call_InstanceMethod = 0;
    private int numberOff_Declaration_ClassVariable = 0; 
    private int numberOff_Declaration_InstanceVariable = 0;
    private int numberOff_Declaration_LocalVariable = 0;
    private int numberOff_Declaration_Parameter = 0;
    private int numberOff_Declaration_ReturnType = 0;
    private int numberOff_Declaration_TypeCast = 0;
    private int numberOff_Declaration_Exception = 0;
    private int numberOff_Inheritance_ExtendsClass = 0;
    private int numberOff_Inheritance_ExtendsAbstractClass = 0;
    private int numberOff_Inheritance_ÏmplementsInterface = 0;
    private int numberOff_Inheritance_FromLibraryClass = 0;
    
    
    
    public ExcelReportWriter(Report report, String path, String fileName) {
    	super(report, path, fileName, ExtensionType.XLS);
        numberOfAllDependencies_Total = report.getViolations().getValue().size();
    }

    @Override
    public void createReport() throws DocumentException, MalformedURLException, IOException {
        String fullPath = path + "\\" + fileName;
    	File file = new File(fullPath);
        WorkbookSettings documentSettings = new WorkbookSettings();
        documentSettings.setLocale(ServiceProvider.getInstance().getLocaleService().getLocale());
        try {
            workbook = Workbook.createWorkbook(file);
            workbook.createSheet(super.translate("Statistics"), 0);
            WritableSheet statisticsSheet = workbook.getSheet(0);
            workbook.createSheet(super.translate("Violations") + "_1", 1);
            WritableSheet dependenciesSheet = workbook.getSheet(1);
            createLayoutDefaults();
            createLabels(dependenciesSheet);
            createContent(dependenciesSheet);
            writeStatistics(statisticsSheet);
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
        WritableFont times10Bold = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false);
        timesBold = new WritableCellFormat(times10Bold);
        timesBold.setWrap(false);
        timesBold_AlignmentRight = new WritableCellFormat(times10Bold);
        timesBold_AlignmentRight.setWrap(false);
        timesBold_AlignmentRight.setAlignment(Alignment.RIGHT);

        CellView cv = new CellView();
        cv.setFormat(times);
        cv.setFormat(timesBold);
        cv.setAutosize(false);
    }
    
    private void createLabels(WritableSheet sheet) throws WriteException {
        addCellBold(sheet, 0, 0, super.translate("From"));
        addCellBold(sheet, 1, 0, super.translate("To"));
        addCellBold(sheet, 2, 0, super.translate("DependencyType"));
        addCellBold(sheet, 3, 0, super.translate("DependencySubType"));
        addCellBold(sheet, 4, 0, super.translate("Linenumber"));
        addCellBold(sheet, 5, 0, super.translate("Direct") + "/" + super.translate("Indirect"));
        addCellBold(sheet, 6, 0, super.translate("InheritanceRelated"));
        addCellBold(sheet, 7, 0, super.translate("InnerClassRelated"));
        addCellBold(sheet, 8, 0, super.translate("Rule"));
    }

    private void createContent(WritableSheet sheet) throws WriteException, RowsExceededException {
    	int sheetNr = 1;
    	int row = 1;
        try {
    		for (Violation violation : report.getViolations().getValue()) {
	            writeDependency(sheet, row, violation);
	            updateStatistics(violation);
	            row++;
	            if (row == 60001) {
	            	sheetNr ++;
	                workbook.createSheet(super.translate("Dependencies") + "_" + sheetNr, sheetNr);
	                sheet = workbook.getSheet(sheetNr);
	                createLabels(sheet);
	                row = 1;
	            }
	        }
        } catch (Exception e) {
            husacctLogger.error("ExceptionMessage: " + e.getMessage());
        }
    }

    private void writeDependency(WritableSheet sheet, int row, Violation violation) throws RowsExceededException, WriteException {
        Label fromLabel = new Label(0, row, violation.getClassPathFrom(), times);
        Label toLabel = new Label(1, row, violation.getClassPathTo(), times);
        Label typeLabel = new Label(2, row, violation.getViolationTypeKey(), times);
        Label dependencySubTypeLabel = new Label(3, row, violation.getDependencySubType(), times);
        Label lineLabel = new Label(4, row, "" + violation.getLinenumber(), times);
        Label directLabel;
        if (violation.getIsIndirect()) {
            directLabel = new Label(5, row, super.translate("Indirect"), times);
        } else {
            directLabel = new Label(5, row, super.translate("Direct"), times);
        }
        Label inheritanceLabel = new Label(6, row, "" + violation.getIsInheritanceRelated(), times);
        Label innerClassLabel = new Label(7, row, "" + violation.getIsInnerClassRelated(), times);
		// Rule
        Label ruleLabel;
		if (violation.getMessage() != null) {
			String message = new Messagebuilder().createMessage(violation.getMessage(), violation);
			ruleLabel = new Label(8, row, message, times);
		} else {
			ruleLabel = new Label(8, row, "-", times);
		}
        List<Label> labelArray = new ArrayList<Label>();
        labelArray.add(fromLabel);
        labelArray.add(toLabel);
        labelArray.add(typeLabel);
        labelArray.add(dependencySubTypeLabel);
        labelArray.add(lineLabel);
        labelArray.add(directLabel);
        labelArray.add(inheritanceLabel);
        labelArray.add(innerClassLabel);
        labelArray.add(ruleLabel);
        
        for(Label label : labelArray){
        	sheet.addCell(label);
        	if(dimensions.get(label.getColumn()) == null || dimensions.get(label.getColumn()) < label.getString().length()){
        		if(label.getString().length() < 20)
        			dimensions.put(label.getColumn(), 20);
        		else
        			dimensions.put(label.getColumn(), label.getString().length());
            }
        }
        
        for(int i : dimensions.keySet()){
        	sheet.setColumnView(i, dimensions.get(i));
        }
    }

    private void updateStatistics(Violation violation) throws RowsExceededException, WriteException {
    	if (violation.getIsIndirect()) {
    		numberOfAllDependencies_Indirect ++;
    	} else {
    		numberOfAllDependencies_Direct ++;
    	}
    	
    	switch (violation.getViolationTypeKey()) {
    	case "Import":
    	    numberOfAllDependencies_Import ++;
        	if (violation.getIsIndirect()) {
        		numberOfAllDependencies_Import_Indirect ++;
        	} else {
        		numberOfAllDependencies_Import_Direct ++;
        	}
    	    break;
    	case "Declaration":
    	    numberOfAllDependencies_Declaration ++;
        	if (violation.getIsIndirect()) {
        		numberOfAllDependencies_Declaration_Indirect ++;
        	} else {
        		numberOfAllDependencies_Declaration_Direct ++;
        	}
        	if (violation.getDependencySubType().equals("Instance Variable")) {
        		numberOff_Declaration_InstanceVariable ++;
        	} else if (violation.getDependencySubType().equals("Local Variable")) {
        		numberOff_Declaration_LocalVariable ++;
        	} else if (violation.getDependencySubType().equals("Class Variable")) {
        		numberOff_Declaration_ClassVariable ++;
        	} else if (violation.getDependencySubType().equals("Parameter")) {
        		numberOff_Declaration_Parameter ++;
        	} else if (violation.getDependencySubType().equals("Return Type")) {
        		numberOff_Declaration_ReturnType ++;
        	} else if (violation.getDependencySubType().equals("Type Cast")) {
        		numberOff_Declaration_TypeCast ++;
        	} else if (violation.getDependencySubType().equals("Exception")) {
        		numberOff_Declaration_Exception ++;
        	}
    	    break;
    	case "Annotation":
    	    numberOfAllDependencies_Annotation ++;
        	if (violation.getIsIndirect()) {
        		numberOfAllDependencies_Annotation_Indirect ++;
        	} else {
        		numberOfAllDependencies_Annotation_Direct ++;
        	}
    	    break;
    	case "Access":
    		numberOfAllDependencies_Access ++;
        	if (violation.getIsIndirect()) {
        		numberOfAllDependencies_Access_Indirect ++;
        	} else {
        		numberOfAllDependencies_Access_Direct ++;
        	}
        	if (violation.getDependencySubType().equals("Variable")) {
        		numberOff_Access_Variable ++;
        	} else if (violation.getDependencySubType().equals("Instance Variable")) {
        		numberOff_Access_InstanceVariable ++;
        	} else if (violation.getDependencySubType().equals("Instance Variable Constant")) {
        		numberOff_Access_InstanceVariableConstant ++;
        	} else if (violation.getDependencySubType().equals("Class Variable")) {
        		numberOff_Access_ClassVariable ++;
        	} else if (violation.getDependencySubType().equals("Class Variable Constant")) {
        		numberOff_Access_ClassVariableConstant ++;
        	} else if (violation.getDependencySubType().equals("Enumeration Variable")) {
        		numberOff_Access_EnumerationVariable ++;
        	} else if (violation.getDependencySubType().equals("Interface Variable")) {
        		numberOff_Access_InterfaceVariable ++;
        	} else if (violation.getDependencySubType().equals("Library Variable")) {
        		numberOff_Access_LibraryVariable ++;
        	} else if (violation.getDependencySubType().equals("Reference")) {
        		numberOff_Access_Reference ++;
        	} else if (violation.getDependencySubType().equals("Reference ReturnTypeUsedMethod")) {
        		numberOff_Access_ReferenceReturnTypeUsedMethod ++;
        		if (violation.getIsIndirect()) {
        			numberOff_Access_ReferenceReturnTypeUsedMethod_Indirect ++;
        		} else {
        			numberOff_Access_ReferenceReturnTypeUsedMethod_Direct ++;
        		}
        	} else if (violation.getDependencySubType().equals("Reference TypeOfUsedVariable")) {
        		numberOff_Access_ReferenceTypeOfUsedVariable ++;
        		if (violation.getIsIndirect()) {
        			numberOff_Access_ReferenceTypeOfUsedVariable_Indirect ++;
        		} else {
        			numberOff_Access_ReferenceTypeOfUsedVariable_Direct ++;
        		}
        	}
        	break;
    	case "Call":
    		numberOfAllDependencies_Call ++;
        	if (violation.getIsIndirect()) {
        		numberOfAllDependencies_Call_Indirect ++;
        	} else {
        		numberOfAllDependencies_Call_Direct ++;
        	}
        	if (violation.getDependencySubType().equals("Method")) {
        		numberOff_Call_Method ++;
        	} else if (violation.getDependencySubType().equals("Instance Method")) {
        		numberOff_Call_InstanceMethod ++;
        	} else if (violation.getDependencySubType().equals("Class Method")) {
        		numberOff_Call_ClassMethod ++;
        	} else if (violation.getDependencySubType().equals("Constructor")) {
        		numberOff_Call_Constructor ++;
        	} else if (violation.getDependencySubType().equals("Enumeration Method")) {
        		numberOff_Call_EnumerationMethod ++;
        	} else if (violation.getDependencySubType().equals("Interface Method")) {
        		numberOff_Call_InterfaceMethod ++;
        	} else if (violation.getDependencySubType().equals("Library Method")) {
        		numberOff_Call_LibraryMethod ++;
        	}
        	break;
    	case "Inheritance":
    		numberOfAllDependencies_Inheritance ++;
        	if (violation.getIsIndirect()) {
        		numberOfAllDependencies_Inheritance_Indirect ++;
        	} else {
        		numberOfAllDependencies_Inheritance_Direct ++;
        	}
        	if (violation.getDependencySubType().equals("Extends Class")) {
        		numberOff_Inheritance_ExtendsClass ++;
        	} else if (violation.getDependencySubType().equals("Extends Abstract Class")) {
        		numberOff_Inheritance_ExtendsAbstractClass ++;
        	} else if (violation.getDependencySubType().equals("Ïmplements Interface")) {
        		numberOff_Inheritance_ÏmplementsInterface ++;
        	} else if (violation.getDependencySubType().equals("From Library Class")) {
        		numberOff_Inheritance_FromLibraryClass ++;
        	}
        	break;
    	}

    	if (violation.getIsInheritanceRelated()) {
    		numberOfInheritanceRelatedDependencies_Total ++;
        	if (violation.getIsIndirect()) {
        		numberOfInheritanceRelatedDependencies_Total_Indirect ++;
        	} else {
        		numberOfInheritanceRelatedDependencies_Total_Direct ++;
        	}
        	if (violation.getViolationTypeKey().equals("Access")) {
        		numberOfInheritanceRelatedDependencies_Access ++;
            	if (violation.getIsIndirect()) {
            		numberOfInheritanceRelatedDependencies_Access_Indirect ++;
            	} else {
            		numberOfInheritanceRelatedDependencies_Access_Direct ++;
            	}
        	} else if (violation.getViolationTypeKey().equals("Call")) {
        		numberOfInheritanceRelatedDependencies_Call ++;
            	if (violation.getIsIndirect()) {
            		numberOfInheritanceRelatedDependencies_Call_Indirect ++;
            	} else {
            		numberOfInheritanceRelatedDependencies_Call_Direct ++;
            	}
        	} 
    	}
        	
    	if (violation.getIsInnerClassRelated()) {
    		numberOfInnerClassRelatedDependencies_Total ++;
        	if (violation.getIsIndirect()) {
        		numberOfInnerClassRelatedDependencies_Total_Indirect ++;
        	} else {
        		numberOfInnerClassRelatedDependencies_Total_Direct ++;
        	}
    	}
    }
    
    private void writeStatistics(WritableSheet sheet) throws WriteException {
		ApplicationDTO applicationDTO = ServiceProvider.getInstance().getDefineService().getApplicationDetails();
    	AnalysisStatisticsDTO stat = ServiceProvider.getInstance().getAnalyseService().getAnalysisStatistics(null);
    	
        addCellBold(sheet, 0, 0, super.translate("Application") + ": " + applicationDTO.name);
        addCellBold_AlignmentRight(sheet, 1, 0, "Total");
        addCellDefault(sheet, 0, 1, super.translate("PackagesLabel"));
        addCellNumber(sheet, 1, 1, stat.totalNrOfPackages);
        addCellDefault(sheet, 0, 2, super.translate("ClassesLabel"));
        addCellNumber(sheet, 1, 2, stat.totalNrOfClasses);
        addCellDefault(sheet, 0, 3, super.translate("LinesOfCode"));
        addCellNumber(sheet, 1, 3, stat.totalNrOfLinesOfCode);
        addCellDefault(sheet, 0, 4, "----------------------------------------------------");
        
        addCellBold_AlignmentRight(sheet, 1, 5, "Total");
        addCellBold_AlignmentRight(sheet, 2, 5, "Direct");
        addCellBold_AlignmentRight(sheet, 3, 5, "Indirect");
        
        addCellBold(sheet, 0, 6, "Dependencies, all");
        addCellNumber(sheet, 1, 6, numberOfAllDependencies_Total);
        addCellNumber(sheet, 2, 6, numberOfAllDependencies_Direct);
        addCellNumber(sheet, 3, 6, numberOfAllDependencies_Indirect);
        if (numberOfAllDependencies_Total != (numberOfAllDependencies_Direct + numberOfAllDependencies_Indirect)) {
            addCellDefault(sheet, 4, 6, "Warning: Total does not match direct + indirect");
        }
        int derivedAll = numberOfAllDependencies_Import + numberOfAllDependencies_Declaration + numberOfAllDependencies_Call 
        		+ numberOfAllDependencies_Access + numberOfAllDependencies_Inheritance + numberOfAllDependencies_Annotation;
        if (derivedAll != numberOfAllDependencies_Total) {
            addCellDefault(sheet, 5, 6, "Warning: Total does not match total of types");
        }
        addCellDefault(sheet, 0, 7, "Import");
        addCellNumber(sheet, 1, 7, numberOfAllDependencies_Import);
        addCellNumber(sheet, 2, 7, numberOfAllDependencies_Import_Direct);
        addCellNumber(sheet, 3, 7, numberOfAllDependencies_Import_Indirect);
        if (numberOfAllDependencies_Import != (numberOfAllDependencies_Import_Direct + numberOfAllDependencies_Import_Indirect)) {
            addCellDefault(sheet, 4, 7, "Warning: Total does not match direct + indirect");
        }
        addCellDefault(sheet, 0, 8, "Declaration");
        addCellNumber(sheet, 1, 8, numberOfAllDependencies_Declaration);
        addCellNumber(sheet, 2, 8, numberOfAllDependencies_Declaration_Direct);
        addCellNumber(sheet, 3, 8, numberOfAllDependencies_Declaration_Indirect);
        if (numberOfAllDependencies_Declaration != (numberOfAllDependencies_Declaration_Direct + numberOfAllDependencies_Declaration_Indirect)) {
            addCellDefault(sheet, 4, 8, "Warning: Total does not match direct + indirect");
        }
        addCellDefault(sheet, 0, 9, "Call");
        addCellNumber(sheet, 1, 9, numberOfAllDependencies_Call);
        addCellNumber(sheet, 2, 9, numberOfAllDependencies_Call_Direct);
        addCellNumber(sheet, 3, 9, numberOfAllDependencies_Call_Indirect);
        if (numberOfAllDependencies_Call != (numberOfAllDependencies_Call_Direct + numberOfAllDependencies_Call_Indirect)) {
            addCellDefault(sheet, 4, 9, "Warning: Total does not match direct + indirect");
        }
        addCellDefault(sheet, 0, 10, "Access");
        addCellNumber(sheet, 1, 10, numberOfAllDependencies_Access);
        addCellNumber(sheet, 2, 10, numberOfAllDependencies_Access_Direct);
        addCellNumber(sheet, 3, 10, numberOfAllDependencies_Access_Indirect);
        if (numberOfAllDependencies_Access != (numberOfAllDependencies_Access_Direct + numberOfAllDependencies_Access_Indirect)) {
            addCellDefault(sheet, 4, 10, "Warning: Total does not match direct + indirect");
        }
        addCellDefault(sheet, 0, 11, "Inheritance");
        addCellNumber(sheet, 1, 11, numberOfAllDependencies_Inheritance);
        addCellNumber(sheet, 2, 11, numberOfAllDependencies_Inheritance_Direct);
        addCellNumber(sheet, 3, 11, numberOfAllDependencies_Inheritance_Indirect);
        if (numberOfAllDependencies_Inheritance != (numberOfAllDependencies_Inheritance_Direct + numberOfAllDependencies_Inheritance_Indirect)) {
            addCellDefault(sheet, 4, 11, "Warning: Total does not match direct + indirect");
        }
        addCellDefault(sheet, 0, 12, "Annotation");
        addCellNumber(sheet, 1, 12, numberOfAllDependencies_Annotation);
        addCellNumber(sheet, 2, 12, numberOfAllDependencies_Annotation_Direct);
        addCellNumber(sheet, 3, 12, numberOfAllDependencies_Annotation_Indirect);
        if (numberOfAllDependencies_Annotation != (numberOfAllDependencies_Annotation_Direct + numberOfAllDependencies_Annotation_Indirect)) {
            addCellDefault(sheet, 4, 12, "Warning: Total does not match direct + indirect");
        }

        addCellBold(sheet, 0, 14, "Inheritance related dependencies, all");
        addCellNumber(sheet, 1, 14, numberOfInheritanceRelatedDependencies_Total);
        addCellNumber(sheet, 2, 14, numberOfInheritanceRelatedDependencies_Total_Direct);
        addCellNumber(sheet, 3, 14, numberOfInheritanceRelatedDependencies_Total_Indirect);
        if (numberOfInheritanceRelatedDependencies_Total != (numberOfInheritanceRelatedDependencies_Total_Direct + numberOfInheritanceRelatedDependencies_Total_Indirect)) {
            addCellDefault(sheet, 4, 14, "Warning: Total does not match direct + indirect");
        }
        if (numberOfInheritanceRelatedDependencies_Total != (numberOfAllDependencies_Inheritance + numberOfInheritanceRelatedDependencies_Access + numberOfInheritanceRelatedDependencies_Call)) {
            addCellDefault(sheet, 5, 14, "Warning: Total does not match the totals per type");
        }
        addCellDefault(sheet, 0, 15, "Inheritance relation");
        addCellNumber(sheet, 1, 15, numberOfAllDependencies_Inheritance);
        addCellNumber(sheet, 2, 15, numberOfAllDependencies_Inheritance_Direct);
        addCellNumber(sheet, 3, 15, numberOfAllDependencies_Inheritance_Indirect);
        if (numberOfAllDependencies_Inheritance != (numberOfAllDependencies_Inheritance_Direct + numberOfAllDependencies_Inheritance_Indirect)) {
            addCellDefault(sheet, 4, 15, "Warning: Total does not match direct + indirect");
        }
        addCellDefault(sheet, 0, 16, "Access of inherited variable");
        addCellNumber(sheet, 1, 16, numberOfInheritanceRelatedDependencies_Access);
        addCellNumber(sheet, 2, 16, numberOfInheritanceRelatedDependencies_Access_Direct);
        addCellNumber(sheet, 3, 16, numberOfInheritanceRelatedDependencies_Access_Indirect);
        if (numberOfInheritanceRelatedDependencies_Access != (numberOfInheritanceRelatedDependencies_Access_Direct + numberOfInheritanceRelatedDependencies_Access_Indirect)) {
            addCellDefault(sheet, 4, 16, "Warning: Total does not match direct + indirect");
        }
        addCellDefault(sheet, 0, 17, "Call of inherited method");
        addCellNumber(sheet, 1, 17, numberOfInheritanceRelatedDependencies_Call);
        addCellNumber(sheet, 2, 17, numberOfInheritanceRelatedDependencies_Call_Direct);
        addCellNumber(sheet, 3, 17, numberOfInheritanceRelatedDependencies_Call_Indirect);
        if (numberOfInheritanceRelatedDependencies_Call != (numberOfInheritanceRelatedDependencies_Call_Direct + numberOfInheritanceRelatedDependencies_Call_Indirect)) {
            addCellDefault(sheet, 4, 17, "Warning: Total does not match direct + indirect");
        }

        addCellBold(sheet, 0, 19, "Inner class related dependencies, all");
        addCellNumber(sheet, 1, 19, numberOfInnerClassRelatedDependencies_Total);
        addCellNumber(sheet, 2, 19, numberOfInnerClassRelatedDependencies_Total_Direct);
        addCellNumber(sheet, 3, 19, numberOfInnerClassRelatedDependencies_Total_Indirect);
        if (numberOfInnerClassRelatedDependencies_Total != (numberOfInnerClassRelatedDependencies_Total_Direct + numberOfInnerClassRelatedDependencies_Total_Indirect)) {
            addCellDefault(sheet, 4, 19, "Warning: Total does not match direct + indirect");
        }

        addCellDefault(sheet, 0, 21, "----------------------------------------------------");
        addCellBold(sheet, 0, 23, "Number of Dependencies per dependencySubType");
        addCellBold(sheet, 0, 25, "Access");
        int accessTotaldependencySubTypes = numberOff_Access_Variable + numberOff_Access_InstanceVariable + numberOff_Access_InstanceVariableConstant 
        		+ numberOff_Access_ClassVariable + numberOff_Access_ClassVariableConstant + numberOff_Access_EnumerationVariable
        		+ numberOff_Access_InterfaceVariable + numberOff_Access_LibraryVariable + numberOff_Access_Reference
        		+ numberOff_Access_ReferenceReturnTypeUsedMethod + numberOff_Access_ReferenceTypeOfUsedVariable;
        addCellNumber(sheet, 1, 25, accessTotaldependencySubTypes);
        if (accessTotaldependencySubTypes != numberOfAllDependencies_Access) {
            addCellDefault(sheet, 2, 25, "Warning: Total of dependencySubTypes does not match total of types");
        }
        addCellDefault(sheet, 0, 26, "Variable");
        addCellNumber(sheet, 1, 26, numberOff_Access_Variable);
        addCellDefault(sheet, 0, 27, "Instance Variable");
        addCellNumber(sheet, 1, 27, numberOff_Access_InstanceVariable);
        addCellDefault(sheet, 0, 28, "Instance Variable Constant");
        addCellNumber(sheet, 1, 28, numberOff_Access_InstanceVariableConstant);
        addCellDefault(sheet, 0, 29, "Class Variable");
        addCellNumber(sheet, 1, 29, numberOff_Access_ClassVariable);
        addCellDefault(sheet, 0, 30, "Class Variable Constant");
        addCellNumber(sheet, 1, 30, numberOff_Access_ClassVariableConstant);
        addCellDefault(sheet, 0, 31, "Enumeration Variable");
        addCellNumber(sheet, 1, 31, numberOff_Access_EnumerationVariable);
        addCellDefault(sheet, 0, 32, "Interface Variable");
        addCellNumber(sheet, 1, 32, numberOff_Access_InterfaceVariable);
        addCellDefault(sheet, 0, 33, "Library Variable");
        addCellNumber(sheet, 1, 33, numberOff_Access_LibraryVariable);
        addCellDefault(sheet, 0, 34, "Reference");
        addCellNumber(sheet, 1, 34, numberOff_Access_Reference);
        addCellDefault(sheet, 0, 35, "Reference ReturnTypeUsedMethod");
        addCellNumber(sheet, 1, 35, numberOff_Access_ReferenceReturnTypeUsedMethod);
        addCellNumber(sheet, 2, 35, numberOff_Access_ReferenceReturnTypeUsedMethod_Direct);
        addCellNumber(sheet, 3, 35, numberOff_Access_ReferenceReturnTypeUsedMethod_Indirect);
        addCellDefault(sheet, 0, 36, "Reference TypeOfUsedVariable");
        addCellNumber(sheet, 1, 36, numberOff_Access_ReferenceTypeOfUsedVariable);
        addCellNumber(sheet, 2, 36, numberOff_Access_ReferenceTypeOfUsedVariable_Direct);
        addCellNumber(sheet, 3, 36, numberOff_Access_ReferenceTypeOfUsedVariable_Indirect);

        addCellBold(sheet, 0, 38, "Call");
        int callTotaldependencySubTypes = numberOff_Call_Method + numberOff_Call_InstanceMethod + numberOff_Call_ClassMethod 
        		+ numberOff_Call_Constructor + numberOff_Call_EnumerationMethod + numberOff_Call_InterfaceMethod
        		+ numberOff_Call_LibraryMethod;
        addCellNumber(sheet, 1, 38, callTotaldependencySubTypes);
        if (callTotaldependencySubTypes != numberOfAllDependencies_Call) {
            addCellDefault(sheet, 2, 38, "Warning: Total of dependencySubTypes does not match total of types");
        }
        addCellDefault(sheet, 0, 39, "Method");
        addCellNumber(sheet, 1, 39, numberOff_Call_Method);
        addCellDefault(sheet, 0, 40, "Instance Method");
        addCellNumber(sheet, 1, 40, numberOff_Call_InstanceMethod);
        addCellDefault(sheet, 0, 41, "Class Method");
        addCellNumber(sheet, 1, 41, numberOff_Call_ClassMethod);
        addCellDefault(sheet, 0, 42, "Constructor");
        addCellNumber(sheet, 1, 42, numberOff_Call_Constructor);
        addCellDefault(sheet, 0, 43, "Enumeration Method");
        addCellNumber(sheet, 1, 43, numberOff_Call_EnumerationMethod);
        addCellDefault(sheet, 0, 44, "Interface Method");
        addCellNumber(sheet, 1, 44, numberOff_Call_InterfaceMethod);
        addCellDefault(sheet, 0, 45, "Library Method");
        addCellNumber(sheet, 1, 45, numberOff_Call_LibraryMethod);

        addCellBold(sheet, 0, 47, "Declaration");
        int declarationTotaldependencySubTypes = numberOff_Declaration_ClassVariable + numberOff_Declaration_Exception + numberOff_Declaration_InstanceVariable 
        		+ numberOff_Declaration_LocalVariable + numberOff_Declaration_Parameter + numberOff_Declaration_ReturnType
        		+ numberOff_Declaration_TypeCast;
        addCellNumber(sheet, 1, 47, declarationTotaldependencySubTypes);
        if (declarationTotaldependencySubTypes != numberOfAllDependencies_Declaration) {
            addCellDefault(sheet, 2, 47, "Warning: Total of dependencySubTypes does not match total of types");
        }
        addCellDefault(sheet, 0, 48, "Class Variable");
        addCellNumber(sheet, 1, 48, numberOff_Declaration_ClassVariable);
        addCellDefault(sheet, 0, 49, "Exception");
        addCellNumber(sheet, 1, 49, numberOff_Declaration_Exception);
        addCellDefault(sheet, 0, 50, "Instance Variable");
        addCellNumber(sheet, 1, 50, numberOff_Declaration_InstanceVariable);
        addCellDefault(sheet, 0, 51, "Local Variable");
        addCellNumber(sheet, 1, 51, numberOff_Declaration_LocalVariable);
        addCellDefault(sheet, 0, 52, "Parameter");
        addCellNumber(sheet, 1, 52, numberOff_Declaration_Parameter);
        addCellDefault(sheet, 0, 53, "Return Type");
        addCellNumber(sheet, 1, 53, numberOff_Declaration_ReturnType);
        addCellDefault(sheet, 0, 54, "Type Cast");
        addCellNumber(sheet, 1, 54, numberOff_Declaration_TypeCast);

        addCellBold(sheet, 0, 56, "Inheritance");
        int InheritanceTotaldependencySubTypes = numberOff_Inheritance_ExtendsClass + numberOff_Inheritance_ExtendsAbstractClass 
        		+ numberOff_Inheritance_ÏmplementsInterface + numberOff_Inheritance_FromLibraryClass;
        addCellNumber(sheet, 1, 56, InheritanceTotaldependencySubTypes);
        if (InheritanceTotaldependencySubTypes != numberOfAllDependencies_Inheritance) {
            addCellDefault(sheet, 2, 56, "Warning: Total of dependencySubTypes does not match total of types");
        }
        addCellDefault(sheet, 0, 57, "Extends Class");
        addCellNumber(sheet, 1, 57, numberOff_Inheritance_ExtendsClass);
        addCellDefault(sheet, 0, 58, "Extends Abstract Class");
        addCellNumber(sheet, 1, 58, numberOff_Inheritance_ExtendsAbstractClass);
        addCellDefault(sheet, 0, 59, "Ïmplements Interface");
        addCellNumber(sheet, 1, 59, numberOff_Inheritance_ÏmplementsInterface);
        addCellDefault(sheet, 0, 60, "From Library Class");
        addCellNumber(sheet, 1, 60, numberOff_Inheritance_FromLibraryClass);

        sheet.setColumnView(0, 40);
    	sheet.setColumnView(1, 10);
    	sheet.setColumnView(2, 10);
    	sheet.setColumnView(3, 10);
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
