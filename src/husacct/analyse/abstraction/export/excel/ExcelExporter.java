package husacct.analyse.abstraction.export.excel;

import husacct.ServiceProvider;
import husacct.analyse.abstraction.export.AbstractReportFileExporter;
import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.serviceinterface.dto.AnalysisStatisticsDTO;
import husacct.analyse.serviceinterface.dto.DependencyDTO;
import husacct.analyse.serviceinterface.enums.DependencySubTypes;
import husacct.common.dto.ApplicationDTO;

import java.io.File;
import java.io.IOException;
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


public class ExcelExporter extends AbstractReportFileExporter {

    private IModelQueryService queryService;
    private Logger husacctLogger = Logger.getLogger(ExcelExporter.class);
    private WritableWorkbook workbook;
    private WritableCellFormat timesBold;
    private WritableCellFormat timesBold_AlignmentRight;
    private WritableCellFormat times;
    private Map<Integer, Integer> dimensions = new HashMap<Integer, Integer>();

    // Variables for statistics: dependency.type
    private int numberOfAllDependencies_Total = 0;
    private int numberOfAllDependencies_Direct = 0;
    private int numberOfAllDependencies_Indirect = 0;
    private int numberOfAllDependencies_Access = 0;
    private int numberOfAllDependencies_Access_Direct = 0;
    private int numberOfAllDependencies_Access_Indirect = 0;
    private int numberOfAllDependencies_Annotation = 0;
    private int numberOfAllDependencies_Annotation_Direct = 0;
    private int numberOfAllDependencies_Annotation_Indirect = 0;
    private int numberOfAllDependencies_Call = 0;
    private int numberOfAllDependencies_Call_Direct = 0;
    private int numberOfAllDependencies_Call_Indirect = 0;
    private int numberOfAllDependencies_Declaration = 0;
    private int numberOfAllDependencies_Declaration_Direct = 0;
    private int numberOfAllDependencies_Declaration_Indirect = 0;
    private int numberOfAllDependencies_Import = 0;
    private int numberOfAllDependencies_Import_Direct = 0;
    private int numberOfAllDependencies_Import_Indirect = 0;
    private int numberOfAllDependencies_Inheritance = 0;
    private int numberOfAllDependencies_Inheritance_Direct = 0;
    private int numberOfAllDependencies_Inheritance_Indirect = 0;
    private int numberOfAllDependencies_Reference = 0;
    private int numberOfAllDependencies_Reference_Direct = 0;
    private int numberOfAllDependencies_Reference_Indirect = 0;
    private int numberOfInheritanceRelatedDependencies_Total = 0;
    private int numberOfInheritanceRelatedDependencies_Total_Direct = 0;
    private int numberOfInheritanceRelatedDependencies_Total_Indirect = 0;
    private int numberOfInheritanceRelatedDependencies_Access = 0;
    private int numberOfInheritanceRelatedDependencies_Access_Direct = 0;
    private int numberOfInheritanceRelatedDependencies_Access_Indirect = 0;
    private int numberOfInheritanceRelatedDependencies_Call = 0;
    private int numberOfInheritanceRelatedDependencies_Call_Direct = 0;
    private int numberOfInheritanceRelatedDependencies_Call_Indirect = 0;
    private int numberOfInheritanceRelatedDependencies_Reference = 0;
    private int numberOfInheritanceRelatedDependencies_Reference_Direct = 0;
    private int numberOfInheritanceRelatedDependencies_Reference_Indirect = 0;
    private int numberOfInnerClassRelatedDependencies_Total = 0;
    private int numberOfInnerClassRelatedDependencies_Total_Direct = 0;
    private int numberOfInnerClassRelatedDependencies_Total_Indirect = 0;
    // Variables for statistics: dependency.type
    private int numberOf_Access_Variable = 0;
    private int numberOf_Access_EnumerationVariable = 0;
    private int numberOf_Access_InterfaceVariable = 0;
    private int numberOf_Access_LibraryVariable = 0;
    private int numberOf_Access_InstanceVariable = 0;
    private int numberOf_Access_InstanceVariableConstant = 0;
    private int numberOf_Access_ClassVariable = 0;
    private int numberOf_Access_ClassVariableConstant = 0;
    private int numberOf_Call_Method = 0;
    private int numberOf_Call_EnumerationMethod = 0;
    private int numberOf_Call_InterfaceMethod = 0;
    private int numberOf_Call_LibraryMethod = 0;
    private int numberOf_Call_Constructor = 0;
    private int numberOf_Call_ClassMethod = 0;
    private int numberOf_Call_InstanceMethod = 0;
    private int numberOf_Declaration_ClassVariable = 0; 
    private int numberOf_Declaration_InstanceVariable = 0;
    private int numberOf_Declaration_LocalVariable = 0;
    private int numberOf_Declaration_Parameter = 0;
    private int numberOf_Declaration_ReturnType = 0;
    private int numberOf_Declaration_GenericTypeParameter = 0;
    private int numberOf_Reference_TypeCast = 0;
    private int numberOf_Declaration_Exception = 0;
    private int numberOf_Inheritance_ExtendsClass = 0;
    private int numberOf_Inheritance_ExtendsAbstractClass = 0;
    private int numberOf_Inheritance_ImplementsInterface = 0;
    private int numberOf_Inheritance_FromLibraryClass = 0;
    private int numberOf_Reference_Type = 0;
    private int numberOf_Reference_ReferenceReturnTypeUsedMethod = 0;
    private int numberOf_Reference_ReferenceReturnTypeUsedMethod_Direct = 0;
    private int numberOf_Reference_ReferenceReturnTypeUsedMethod_Indirect = 0;
    private int numberOf_Reference_ReferenceTypeOfUsedVariable = 0;
    private int numberOf_Reference_ReferenceTypeOfUsedVariable_Direct = 0;
    private int numberOf_Reference_ReferenceTypeOfUsedVariable_Indirect = 0;
    
    
    
    public ExcelExporter(DependencyDTO[] data, IModelQueryService queryService) {
        super(data);
    	this.queryService = queryService;
        numberOfAllDependencies_Total = data.length;
    }

    @Override
    protected void write(String path) {
        File file = new File(path);
        WorkbookSettings documentSettings = new WorkbookSettings();
        documentSettings.setLocale(ServiceProvider.getInstance().getLocaleService().getLocale());
        try {
            workbook = Workbook.createWorkbook(file);
            workbook.createSheet(super.translate("Statistics"), 0);
            WritableSheet statisticsSheet = workbook.getSheet(0);
            workbook.createSheet(super.translate("Dependencies") + "_1", 1);
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
        addCellBold(sheet, 0, 0, super.translate("DependencyFrom"));
        addCellBold(sheet, 1, 0, super.translate("DependencyTo"));
        addCellBold(sheet, 2, 0, super.translate("DependencyType"));
        addCellBold(sheet, 3, 0, super.translate("DependencySubType"));
        addCellBold(sheet, 4, 0, super.translate("Linenumber"));
        addCellBold(sheet, 5, 0, super.translate("Direct") + "/" + super.translate("Indirect"));
        addCellBold(sheet, 6, 0, super.translate("InheritanceRelated"));
        addCellBold(sheet, 7, 0, super.translate("InnerClassRelated"));
        addCellBold(sheet, 8, 0, "Used Entity (variable or method)");
    }

    private void createContent(WritableSheet sheet) throws WriteException, RowsExceededException {
    	int sheetNr = 1;
    	int row = 1;
        try {
	        for (DependencyDTO dependency : data) {
	            writeDependency(sheet, row, dependency);
	            updateStatistics(dependency);
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

    private void writeDependency(WritableSheet sheet, int row, DependencyDTO dependency) throws RowsExceededException, WriteException {
        Label fromLabel = new Label(0, row, dependency.from, times);
        Label toLabel = new Label(1, row, dependency.to, times);
        Label typeLabel = new Label(2, row, dependency.type, times);
        Label subTypeLabel = new Label(3, row, dependency.subType, times);
        Label lineLabel = new Label(4, row, "" + dependency.lineNumber, times);
        Label directLabel;
        if (dependency.isIndirect) {
            directLabel = new Label(5, row, super.translate("Indirect"), times);
        } else {
            directLabel = new Label(5, row, super.translate("Direct"), times);
        }
        Label inheritanceLabel = new Label(6, row, "" + dependency.isInheritanceRelated, times);
        Label innerClassLabel = new Label(7, row, "" + dependency.isInnerClassRelated, times);
        Label usedEntityLabel = new Label(8, row, "" + dependency.usedEntity, times);
        
        List<Label> labelArray = new ArrayList<Label>();
        labelArray.add(fromLabel);
        labelArray.add(toLabel);
        labelArray.add(typeLabel);
        labelArray.add(subTypeLabel);
        labelArray.add(lineLabel);
        labelArray.add(directLabel);
        labelArray.add(inheritanceLabel);
        labelArray.add(innerClassLabel);
        labelArray.add(usedEntityLabel);
        
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

    private void updateStatistics(DependencyDTO dependency) throws RowsExceededException, WriteException {
    	if (dependency.isIndirect) {
    		numberOfAllDependencies_Indirect ++;
    	} else {
    		numberOfAllDependencies_Direct ++;
    	}
    	
    	switch (dependency.type) {
    	case "Import":
    	    numberOfAllDependencies_Import ++;
        	if (dependency.isIndirect) {
        		numberOfAllDependencies_Import_Indirect ++;
        	} else {
        		numberOfAllDependencies_Import_Direct ++;
        	}
    	    break;
    	case "Declaration":
    	    numberOfAllDependencies_Declaration ++;
        	if (dependency.isIndirect) {
        		numberOfAllDependencies_Declaration_Indirect ++;
        	} else {
        		numberOfAllDependencies_Declaration_Direct ++;
        	}
        	if (dependency.subType.equals(DependencySubTypes.DECL_INSTANCE_VAR.toString())) {
        		numberOf_Declaration_InstanceVariable ++;
        	} else if (dependency.subType.equals(DependencySubTypes.DECL_LOCAL_VAR.toString())) {
        		numberOf_Declaration_LocalVariable ++;
        	} else if (dependency.subType.equals(DependencySubTypes.DECL_CLASS_VAR.toString())) {
        		numberOf_Declaration_ClassVariable ++;
        	} else if (dependency.subType.equals(DependencySubTypes.DECL_PARAMETER.toString())) {
        		numberOf_Declaration_Parameter ++;
        	} else if (dependency.subType.equals(DependencySubTypes.DECL_RETURN_TYPE.toString())) {
        		numberOf_Declaration_ReturnType ++;
        	} else if (dependency.subType.equals(DependencySubTypes.DECL_EXCEPTION.toString())) {
        		numberOf_Declaration_Exception ++;
        	} else if (dependency.subType.equals(DependencySubTypes.DECL_TYPE_PARAMETER.toString())) {
        		numberOf_Declaration_GenericTypeParameter ++;
        	}
    	    break;
    	case "Annotation":
    	    numberOfAllDependencies_Annotation ++;
        	if (dependency.isIndirect) {
        		numberOfAllDependencies_Annotation_Indirect ++;
        	} else {
        		numberOfAllDependencies_Annotation_Direct ++;
        	}
    	    break;
    	case "Access":
    		numberOfAllDependencies_Access ++;
        	if (dependency.isIndirect) {
        		numberOfAllDependencies_Access_Indirect ++;
        	} else {
        		numberOfAllDependencies_Access_Direct ++;
        	}
        	if (dependency.subType.equals("Variable")) {
        		numberOf_Access_Variable ++;
        	} else if (dependency.subType.equals("Instance Variable")) {
        		numberOf_Access_InstanceVariable ++;
        	} else if (dependency.subType.equals("Instance Variable Constant")) {
        		numberOf_Access_InstanceVariableConstant ++;
        	} else if (dependency.subType.equals("Class Variable")) {
        		numberOf_Access_ClassVariable ++;
        	} else if (dependency.subType.equals("Class Variable Constant")) {
        		numberOf_Access_ClassVariableConstant ++;
        	} else if (dependency.subType.equals("Enumeration Variable")) {
        		numberOf_Access_EnumerationVariable ++;
        	} else if (dependency.subType.equals("Interface Variable")) {
        		numberOf_Access_InterfaceVariable ++;
        	} else if (dependency.subType.equals("Library Variable")) {
        		numberOf_Access_LibraryVariable ++;
        	} 
        	break;
    	case "Reference":
    		numberOfAllDependencies_Reference ++;
    		if (dependency.isIndirect) {
    			numberOfAllDependencies_Reference_Indirect ++;
    		} else {
    			numberOfAllDependencies_Reference_Direct ++;
    		}
    		if (dependency.subType.equals("Type")) {
        		numberOf_Reference_Type ++;
        	} else if (dependency.subType.equals("Type Cast")) {
        		numberOf_Reference_TypeCast ++;
        	} else if (dependency.subType.equals("Return Type")) {
        		numberOf_Reference_ReferenceReturnTypeUsedMethod ++;
        		if (dependency.isIndirect) {
        			numberOf_Reference_ReferenceReturnTypeUsedMethod_Indirect ++;
        		} else {
        			numberOf_Reference_ReferenceReturnTypeUsedMethod_Direct ++;
        		}
        	} else if (dependency.subType.equals("Type of Variable")) {
        		numberOf_Reference_ReferenceTypeOfUsedVariable ++;
        		if (dependency.isIndirect) {
        			numberOf_Reference_ReferenceTypeOfUsedVariable_Indirect ++;
        		} else {
        			numberOf_Reference_ReferenceTypeOfUsedVariable_Direct ++;
        		}
        	}
        	break;
    	case "Call":
    		numberOfAllDependencies_Call ++;
        	if (dependency.isIndirect) {
        		numberOfAllDependencies_Call_Indirect ++;
        	} else {
        		numberOfAllDependencies_Call_Direct ++;
        	}
        	if (dependency.subType.equals("Method")) {
        		numberOf_Call_Method ++;
        	} else if (dependency.subType.equals("Instance Method")) {
        		numberOf_Call_InstanceMethod ++;
        	} else if (dependency.subType.equals("Class Method")) {
        		numberOf_Call_ClassMethod ++;
        	} else if (dependency.subType.equals("Constructor")) {
        		numberOf_Call_Constructor ++;
        	} else if (dependency.subType.equals("Enumeration Method")) {
        		numberOf_Call_EnumerationMethod ++;
        	} else if (dependency.subType.equals("Interface Method")) {
        		numberOf_Call_InterfaceMethod ++;
        	} else if (dependency.subType.equals("Library Method")) {
        		numberOf_Call_LibraryMethod ++;
        	}
        	break;
    	case "Inheritance":
    		numberOfAllDependencies_Inheritance ++;
        	if (dependency.isIndirect) {
        		numberOfAllDependencies_Inheritance_Indirect ++;
        	} else {
        		numberOfAllDependencies_Inheritance_Direct ++;
        	}
        	if (dependency.subType.equals("Extends Class")) {
        		numberOf_Inheritance_ExtendsClass ++;
        	} else if (dependency.subType.equals("Extends Abstract Class")) {
        		numberOf_Inheritance_ExtendsAbstractClass ++;
        	} else if (dependency.subType.equals("Implements Interface")) {
        		numberOf_Inheritance_ImplementsInterface ++;
        	} else if (dependency.subType.equals("From Library Class")) {
        		numberOf_Inheritance_FromLibraryClass ++;
        	}
        	break;
    	}

    	if (dependency.isInheritanceRelated) {
    		numberOfInheritanceRelatedDependencies_Total ++;
        	if (dependency.isIndirect) {
        		numberOfInheritanceRelatedDependencies_Total_Indirect ++;
        	} else {
        		numberOfInheritanceRelatedDependencies_Total_Direct ++;
        	}
        	if (dependency.type.equals("Access")) {
        		numberOfInheritanceRelatedDependencies_Access ++;
            	if (dependency.isIndirect) {
            		numberOfInheritanceRelatedDependencies_Access_Indirect ++;
            	} else {
            		numberOfInheritanceRelatedDependencies_Access_Direct ++;
            	}
        	} else if (dependency.type.equals("Call")) {
        		numberOfInheritanceRelatedDependencies_Call ++;
            	if (dependency.isIndirect) {
            		numberOfInheritanceRelatedDependencies_Call_Indirect ++;
            	} else {
            		numberOfInheritanceRelatedDependencies_Call_Direct ++;
            	}
        	} else if (dependency.type.equals("Reference")) {
        		numberOfInheritanceRelatedDependencies_Reference ++;
            	if (dependency.isIndirect) {
            		numberOfInheritanceRelatedDependencies_Reference_Indirect ++;
            	} else {
            		numberOfInheritanceRelatedDependencies_Reference_Direct ++;
            	}
        	} 
    	}
        	
    	if (dependency.isInnerClassRelated) {
    		numberOfInnerClassRelatedDependencies_Total ++;
        	if (dependency.isIndirect) {
        		numberOfInnerClassRelatedDependencies_Total_Indirect ++;
        	} else {
        		numberOfInnerClassRelatedDependencies_Total_Direct ++;
        	}
    	}
    }
    
    private void writeStatistics(WritableSheet sheet) throws WriteException {
		ApplicationDTO applicationDTO = ServiceProvider.getInstance().getDefineService().getApplicationDetails();
    	AnalysisStatisticsDTO stat = queryService.getAnalysisStatistics(null);
    	
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
        		+ numberOfAllDependencies_Access + numberOfAllDependencies_Inheritance + numberOfAllDependencies_Annotation + numberOfAllDependencies_Reference;
        if (derivedAll != numberOfAllDependencies_Total) {
            addCellDefault(sheet, 5, 6, "Warning: Total does not match total of types");
        }
        addCellDefault(sheet, 0, 7, "Access");
        addCellNumber(sheet, 1, 7, numberOfAllDependencies_Access);
        addCellNumber(sheet, 2, 7, numberOfAllDependencies_Access_Direct);
        addCellNumber(sheet, 3, 7, numberOfAllDependencies_Access_Indirect);
        if (numberOfAllDependencies_Access != (numberOfAllDependencies_Access_Direct + numberOfAllDependencies_Access_Indirect)) {
            addCellDefault(sheet, 4, 7, "Warning: Total does not match direct + indirect");
        }
        addCellDefault(sheet, 0, 8, "Annotation");
        addCellNumber(sheet, 1, 8, numberOfAllDependencies_Annotation);
        addCellNumber(sheet, 2, 8, numberOfAllDependencies_Annotation_Direct);
        addCellNumber(sheet, 3, 8, numberOfAllDependencies_Annotation_Indirect);
        if (numberOfAllDependencies_Annotation != (numberOfAllDependencies_Annotation_Direct + numberOfAllDependencies_Annotation_Indirect)) {
            addCellDefault(sheet, 4, 18, "Warning: Total does not match direct + indirect");
        }
        addCellDefault(sheet, 0, 9, "Call");
        addCellNumber(sheet, 1, 9, numberOfAllDependencies_Call);
        addCellNumber(sheet, 2, 9, numberOfAllDependencies_Call_Direct);
        addCellNumber(sheet, 3, 9, numberOfAllDependencies_Call_Indirect);
        if (numberOfAllDependencies_Call != (numberOfAllDependencies_Call_Direct + numberOfAllDependencies_Call_Indirect)) {
            addCellDefault(sheet, 4, 9, "Warning: Total does not match direct + indirect");
        }
        addCellDefault(sheet, 0, 10, "Declaration");
        addCellNumber(sheet, 1, 10, numberOfAllDependencies_Declaration);
        addCellNumber(sheet, 2, 10, numberOfAllDependencies_Declaration_Direct);
        addCellNumber(sheet, 3, 10, numberOfAllDependencies_Declaration_Indirect);
        if (numberOfAllDependencies_Declaration != (numberOfAllDependencies_Declaration_Direct + numberOfAllDependencies_Declaration_Indirect)) {
            addCellDefault(sheet, 4, 10, "Warning: Total does not match direct + indirect");
        }
        addCellDefault(sheet, 0, 11, "Import");
        addCellNumber(sheet, 1, 11, numberOfAllDependencies_Import);
        addCellNumber(sheet, 2, 11, numberOfAllDependencies_Import_Direct);
        addCellNumber(sheet, 3, 11, numberOfAllDependencies_Import_Indirect);
        if (numberOfAllDependencies_Import != (numberOfAllDependencies_Import_Direct + numberOfAllDependencies_Import_Indirect)) {
            addCellDefault(sheet, 4, 11, "Warning: Total does not match direct + indirect");
        }
        addCellDefault(sheet, 0, 12, "Inheritance");
        addCellNumber(sheet, 1, 12, numberOfAllDependencies_Inheritance);
        addCellNumber(sheet, 2, 12, numberOfAllDependencies_Inheritance_Direct);
        addCellNumber(sheet, 3, 12, numberOfAllDependencies_Inheritance_Indirect);
        if (numberOfAllDependencies_Inheritance != (numberOfAllDependencies_Inheritance_Direct + numberOfAllDependencies_Inheritance_Indirect)) {
            addCellDefault(sheet, 4, 12, "Warning: Total does not match direct + indirect");
        }
        addCellDefault(sheet, 0, 13, "Reference");
        addCellNumber(sheet, 1, 13, numberOfAllDependencies_Reference);
        addCellNumber(sheet, 2, 13, numberOfAllDependencies_Reference_Direct);
        addCellNumber(sheet, 3, 13, numberOfAllDependencies_Reference_Indirect);
        if (numberOfAllDependencies_Annotation != (numberOfAllDependencies_Annotation_Direct + numberOfAllDependencies_Annotation_Indirect)) {
            addCellDefault(sheet, 4, 13, "Warning: Total does not match direct + indirect");
        }

        addCellBold(sheet, 0, 15, "Inheritance related dependencies, all");
        addCellNumber(sheet, 1, 15, numberOfInheritanceRelatedDependencies_Total);
        addCellNumber(sheet, 2, 15, numberOfInheritanceRelatedDependencies_Total_Direct);
        addCellNumber(sheet, 3, 15, numberOfInheritanceRelatedDependencies_Total_Indirect);
        if (numberOfInheritanceRelatedDependencies_Total != (numberOfInheritanceRelatedDependencies_Total_Direct + numberOfInheritanceRelatedDependencies_Total_Indirect)) {
            addCellDefault(sheet, 4, 15, "Warning: Total does not match direct + indirect");
        }
        if (numberOfInheritanceRelatedDependencies_Total != (numberOfAllDependencies_Inheritance + numberOfInheritanceRelatedDependencies_Access 
        		+ numberOfInheritanceRelatedDependencies_Call) + numberOfInheritanceRelatedDependencies_Reference) {
            addCellDefault(sheet, 5, 15, "Warning: Total does not match the totals per type");
        }
        addCellDefault(sheet, 0, 16, "Inheritance relation");
        addCellNumber(sheet, 1, 16, numberOfAllDependencies_Inheritance);
        addCellNumber(sheet, 2, 16, numberOfAllDependencies_Inheritance_Direct);
        addCellNumber(sheet, 3, 16, numberOfAllDependencies_Inheritance_Indirect);
        if (numberOfAllDependencies_Inheritance != (numberOfAllDependencies_Inheritance_Direct + numberOfAllDependencies_Inheritance_Indirect)) {
            addCellDefault(sheet, 4, 16, "Warning: Total does not match direct + indirect");
        }
        addCellDefault(sheet, 0, 17, "Access of inherited variable");
        addCellNumber(sheet, 1, 17, numberOfInheritanceRelatedDependencies_Access);
        addCellNumber(sheet, 2, 17, numberOfInheritanceRelatedDependencies_Access_Direct);
        addCellNumber(sheet, 3, 17, numberOfInheritanceRelatedDependencies_Access_Indirect);
        if (numberOfInheritanceRelatedDependencies_Access != (numberOfInheritanceRelatedDependencies_Access_Direct + numberOfInheritanceRelatedDependencies_Access_Indirect)) {
            addCellDefault(sheet, 4, 17, "Warning: Total does not match direct + indirect");
        }
        addCellDefault(sheet, 0, 18, "Call of inherited method");
        addCellNumber(sheet, 1, 18, numberOfInheritanceRelatedDependencies_Call);
        addCellNumber(sheet, 2, 18, numberOfInheritanceRelatedDependencies_Call_Direct);
        addCellNumber(sheet, 3, 18, numberOfInheritanceRelatedDependencies_Call_Indirect);
        if (numberOfInheritanceRelatedDependencies_Call != (numberOfInheritanceRelatedDependencies_Call_Direct + numberOfInheritanceRelatedDependencies_Call_Indirect)) {
            addCellDefault(sheet, 4, 18, "Warning: Total does not match direct + indirect");
        }
        addCellDefault(sheet, 0, 19, "Reference");
        addCellNumber(sheet, 1, 19, numberOfInheritanceRelatedDependencies_Reference);
        addCellNumber(sheet, 2, 19, numberOfInheritanceRelatedDependencies_Reference_Direct);
        addCellNumber(sheet, 3, 19, numberOfInheritanceRelatedDependencies_Reference_Indirect);
        if (numberOfInheritanceRelatedDependencies_Call != (numberOfInheritanceRelatedDependencies_Call_Direct + numberOfInheritanceRelatedDependencies_Call_Indirect)) {
            addCellDefault(sheet, 4, 19, "Warning: Total does not match direct + indirect");
        }

        addCellBold(sheet, 0, 21, "Inner class related dependencies, all");
        addCellNumber(sheet, 1, 21, numberOfInnerClassRelatedDependencies_Total);
        addCellNumber(sheet, 2, 21, numberOfInnerClassRelatedDependencies_Total_Direct);
        addCellNumber(sheet, 3, 21, numberOfInnerClassRelatedDependencies_Total_Indirect);
        if (numberOfInnerClassRelatedDependencies_Total != (numberOfInnerClassRelatedDependencies_Total_Direct + numberOfInnerClassRelatedDependencies_Total_Indirect)) {
            addCellDefault(sheet, 4, 21, "Warning: Total does not match direct + indirect");
        }

        addCellDefault(sheet, 0, 22, "----------------------------------------------------");
        addCellBold(sheet, 0, 24, "Number of Dependencies per subType");
        addCellBold(sheet, 0, 25, "Access");
        int accessTotalSubTypes = numberOf_Access_Variable + numberOf_Access_InstanceVariable + numberOf_Access_InstanceVariableConstant 
        		+ numberOf_Access_ClassVariable + numberOf_Access_ClassVariableConstant + numberOf_Access_EnumerationVariable
        		+ numberOf_Access_InterfaceVariable + numberOf_Access_LibraryVariable;
        addCellNumber(sheet, 1, 25, accessTotalSubTypes);
        if (accessTotalSubTypes != numberOfAllDependencies_Access) {
            addCellDefault(sheet, 2, 25, "Warning: Total of subTypes does not match total of types");
        }
        addCellDefault(sheet, 0, 26, "Variable");
        addCellNumber(sheet, 1, 26, numberOf_Access_Variable);
        addCellDefault(sheet, 0, 27, "Instance Variable");
        addCellNumber(sheet, 1, 27, numberOf_Access_InstanceVariable);
        addCellDefault(sheet, 0, 28, "Instance Variable Constant");
        addCellNumber(sheet, 1, 28, numberOf_Access_InstanceVariableConstant);
        addCellDefault(sheet, 0, 29, "Class Variable");
        addCellNumber(sheet, 1, 29, numberOf_Access_ClassVariable);
        addCellDefault(sheet, 0, 30, "Class Variable Constant");
        addCellNumber(sheet, 1, 30, numberOf_Access_ClassVariableConstant);
        addCellDefault(sheet, 0, 31, "Enumeration Variable");
        addCellNumber(sheet, 1, 31, numberOf_Access_EnumerationVariable);
        addCellDefault(sheet, 0, 32, "Interface Variable");
        addCellNumber(sheet, 1, 32, numberOf_Access_InterfaceVariable);
        addCellDefault(sheet, 0, 33, "Library Variable");
        addCellNumber(sheet, 1, 33, numberOf_Access_LibraryVariable);

        addCellBold(sheet, 0, 35, "Call");
        int callTotalSubTypes = numberOf_Call_Method + numberOf_Call_InstanceMethod + numberOf_Call_ClassMethod 
        		+ numberOf_Call_Constructor + numberOf_Call_EnumerationMethod + numberOf_Call_InterfaceMethod
        		+ numberOf_Call_LibraryMethod;
        addCellNumber(sheet, 1, 35, callTotalSubTypes);
        if (callTotalSubTypes != numberOfAllDependencies_Call) {
            addCellDefault(sheet, 2, 35, "Warning: Total of subTypes does not match total of types");
        }
        addCellDefault(sheet, 0, 36, "Method");
        addCellNumber(sheet, 1, 36, numberOf_Call_Method);
        addCellDefault(sheet, 0, 37, "Instance Method");
        addCellNumber(sheet, 1, 37, numberOf_Call_InstanceMethod);
        addCellDefault(sheet, 0, 38, "Class Method");
        addCellNumber(sheet, 1, 38, numberOf_Call_ClassMethod);
        addCellDefault(sheet, 0, 39, "Constructor");
        addCellNumber(sheet, 1, 39, numberOf_Call_Constructor);
        addCellDefault(sheet, 0, 40, "Enumeration Method");
        addCellNumber(sheet, 1, 40, numberOf_Call_EnumerationMethod);
        addCellDefault(sheet, 0, 41, "Interface Method");
        addCellNumber(sheet, 1, 41, numberOf_Call_InterfaceMethod);
        addCellDefault(sheet, 0, 42, "Library Method");
        addCellNumber(sheet, 1, 42, numberOf_Call_LibraryMethod);

        addCellBold(sheet, 0, 44, "Declaration");
        int declarationTotalSubTypes = numberOf_Declaration_ClassVariable + numberOf_Declaration_Exception + numberOf_Declaration_InstanceVariable 
        		+ numberOf_Declaration_LocalVariable + numberOf_Declaration_Parameter + numberOf_Declaration_ReturnType + numberOf_Declaration_GenericTypeParameter;
        addCellNumber(sheet, 1, 44, declarationTotalSubTypes);
        if (declarationTotalSubTypes != numberOfAllDependencies_Declaration) {
            addCellDefault(sheet, 2, 44, "Warning: Total of subTypes does not match total of types");
        }
        addCellDefault(sheet, 0, 45, "Class Variable");
        addCellNumber(sheet, 1, 45, numberOf_Declaration_ClassVariable);
        addCellDefault(sheet, 0, 46, "Exception");
        addCellNumber(sheet, 1, 46, numberOf_Declaration_Exception);
        addCellDefault(sheet, 0, 47, "Instance Variable");
        addCellNumber(sheet, 1, 47, numberOf_Declaration_InstanceVariable);
        addCellDefault(sheet, 0, 48, "Local Variable");
        addCellNumber(sheet, 1, 48, numberOf_Declaration_LocalVariable);
        addCellDefault(sheet, 0, 49, "Parameter");
        addCellNumber(sheet, 1, 49, numberOf_Declaration_Parameter);
        addCellDefault(sheet, 0, 50, "Return Type");
        addCellNumber(sheet, 1, 50, numberOf_Declaration_ReturnType);
        addCellDefault(sheet, 0, 51, DependencySubTypes.DECL_TYPE_PARAMETER.toString());
        addCellNumber(sheet, 1, 51, numberOf_Declaration_GenericTypeParameter);

        addCellBold(sheet, 0, 53, "Inheritance");
        int InheritanceTotalSubTypes = numberOf_Inheritance_ExtendsClass + numberOf_Inheritance_ExtendsAbstractClass 
        		+ numberOf_Inheritance_ImplementsInterface + numberOf_Inheritance_FromLibraryClass;
        addCellNumber(sheet, 1, 53, InheritanceTotalSubTypes);
        if (InheritanceTotalSubTypes != numberOfAllDependencies_Inheritance) {
            addCellDefault(sheet, 2, 53, "Warning: Total of subTypes does not match total of types");
        }
        addCellDefault(sheet, 0, 54, "Extends Class");
        addCellNumber(sheet, 1, 54, numberOf_Inheritance_ExtendsClass);
        addCellDefault(sheet, 0, 55, "Extends Abstract Class");
        addCellNumber(sheet, 1, 55, numberOf_Inheritance_ExtendsAbstractClass);
        addCellDefault(sheet, 0, 56, "Implements Interface");
        addCellNumber(sheet, 1, 56, numberOf_Inheritance_ImplementsInterface);
        addCellDefault(sheet, 0, 57, "From Library Class");
        addCellNumber(sheet, 1, 57, numberOf_Inheritance_FromLibraryClass);

        addCellBold(sheet, 0, 59, "Reference");
        int referenceTotalSubTypes = numberOf_Reference_Type + numberOf_Reference_ReferenceReturnTypeUsedMethod 
        		+ numberOf_Reference_ReferenceTypeOfUsedVariable + numberOf_Reference_TypeCast;
        addCellNumber(sheet, 1, 59, referenceTotalSubTypes);
        if (referenceTotalSubTypes != numberOfAllDependencies_Reference) {
            addCellDefault(sheet, 2, 59, "Warning: Total of subTypes does not match total of types");
        }
        addCellDefault(sheet, 0, 60, "Type");
        addCellNumber(sheet, 1, 60, numberOf_Reference_Type);
        addCellDefault(sheet, 0, 61, "Type Cast");
        addCellNumber(sheet, 1, 61, numberOf_Reference_TypeCast);
        addCellDefault(sheet, 0, 62, "Return Type");
        addCellNumber(sheet, 1, 62, numberOf_Reference_ReferenceReturnTypeUsedMethod);
        addCellNumber(sheet, 2, 62, numberOf_Reference_ReferenceReturnTypeUsedMethod_Direct);
        addCellNumber(sheet, 3, 62, numberOf_Reference_ReferenceReturnTypeUsedMethod_Indirect);
        addCellDefault(sheet, 0, 63, "Type of Variable");
        addCellNumber(sheet, 1, 63, numberOf_Reference_ReferenceTypeOfUsedVariable);
        addCellNumber(sheet, 2, 63, numberOf_Reference_ReferenceTypeOfUsedVariable_Direct);
        addCellNumber(sheet, 3, 63, numberOf_Reference_ReferenceTypeOfUsedVariable_Indirect);

        
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
