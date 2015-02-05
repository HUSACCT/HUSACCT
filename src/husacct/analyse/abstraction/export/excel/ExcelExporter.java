package husacct.analyse.abstraction.export.excel;

import husacct.ServiceProvider;
import husacct.analyse.abstraction.export.AbstractFileExporter;
import husacct.analyse.domain.IAnalyseDomainService;
import husacct.common.dto.AnalysisStatisticsDTO;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.DependencyDTO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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


public class ExcelExporter extends AbstractFileExporter {

    private IAnalyseDomainService analysedDomain;
    private Logger husacctLogger = Logger.getLogger(ExcelExporter.class);
    private WritableWorkbook workbook;
    private WritableCellFormat timesBold;
    private WritableCellFormat timesBold_AlignmentRight;
    private WritableCellFormat times;
    private Map<Integer, Integer> dimensions = new HashMap<Integer, Integer>();

    // Variables for statistics
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
    
    public ExcelExporter(DependencyDTO[] data, IAnalyseDomainService analyseDomainService) {
        super(data);
    	this.analysedDomain = analyseDomainService;
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
        addCellBold(sheet, 3, 0, super.translate("Linenumber"));
        addCellBold(sheet, 4, 0, super.translate("Direct") + "/" + super.translate("Indirect"));
        addCellBold(sheet, 5, 0, super.translate("InheritanceRelated"));
        addCellBold(sheet, 6, 0, super.translate("InnerClassRelated"));
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
        Label lineLabel = new Label(3, row, "" + dependency.lineNumber, times);
        Label directLabel;
        if (dependency.isIndirect) {
            directLabel = new Label(4, row, super.translate("Indirect"), times);
        } else {
            directLabel = new Label(4, row, super.translate("Direct"), times);
        }
        Label inheritanceLabel = new Label(5, row, "" + dependency.isInheritanceRelated, times);
        Label innerClassLabel = new Label(6, row, "" + dependency.isInnerClassRelated, times);
        
        List<Label> labelArray = new ArrayList<Label>();
        labelArray.add(fromLabel);
        labelArray.add(toLabel);
        labelArray.add(typeLabel);
        labelArray.add(lineLabel);
        labelArray.add(directLabel);
        labelArray.add(inheritanceLabel);
        labelArray.add(innerClassLabel);
        
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
        	break;
    	case "Call":
    		numberOfAllDependencies_Call ++;
        	if (dependency.isIndirect) {
        		numberOfAllDependencies_Call_Indirect ++;
        	} else {
        		numberOfAllDependencies_Call_Direct ++;
        	}
        	break;
    	case "Inheritance":
    		numberOfAllDependencies_Inheritance ++;
        	if (dependency.isIndirect) {
        		numberOfAllDependencies_Inheritance_Indirect ++;
        	} else {
        		numberOfAllDependencies_Inheritance_Direct ++;
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
    	AnalysisStatisticsDTO stat = analysedDomain.getAnalysisStatistics(null);
    	
        addCellBold(sheet, 0, 0, super.translate("Application") + ": " + applicationDTO.name);
        addCellBold_AlignmentRight(sheet, 1, 0, "Total");
        addCellDefault(sheet, 0, 1, super.translate("PackagesLabel"));
        addCellNumber(sheet, 1, 1, stat.totalNrOfPackages);
        addCellDefault(sheet, 0, 2, super.translate("ClassesLabel"));
        addCellNumber(sheet, 1, 2, stat.totalNrOfClasses);
        addCellDefault(sheet, 0, 3, super.translate("LinesOfCode"));
        addCellNumber(sheet, 1, 3, stat.totalNrOfLinesOfCode);
        
        addCellBold_AlignmentRight(sheet, 1, 5, "Total");
        addCellBold_AlignmentRight(sheet, 2, 5, "Direct");
        addCellBold_AlignmentRight(sheet, 3, 5, "Indirect");
        
        addCellBold(sheet, 0, 6, "Dependencies, all");
        addCellNumber(sheet, 1, 6, numberOfAllDependencies_Total);
        addCellNumber(sheet, 2, 6, numberOfAllDependencies_Direct);
        addCellNumber(sheet, 3, 6, numberOfAllDependencies_Indirect);
        addCellDefault(sheet, 0, 7, "Import");
        addCellNumber(sheet, 1, 7, numberOfAllDependencies_Import);
        addCellNumber(sheet, 2, 7, numberOfAllDependencies_Import_Direct);
        addCellNumber(sheet, 3, 7, numberOfAllDependencies_Import_Indirect);
        addCellDefault(sheet, 0, 8, "Declaration");
        addCellNumber(sheet, 1, 8, numberOfAllDependencies_Declaration);
        addCellNumber(sheet, 2, 8, numberOfAllDependencies_Declaration_Direct);
        addCellNumber(sheet, 3, 8, numberOfAllDependencies_Declaration_Indirect);
        addCellDefault(sheet, 0, 9, "Call");
        addCellNumber(sheet, 1, 9, numberOfAllDependencies_Call);
        addCellNumber(sheet, 2, 9, numberOfAllDependencies_Call_Direct);
        addCellNumber(sheet, 3, 9, numberOfAllDependencies_Call_Indirect);
        addCellDefault(sheet, 0, 10, "Access");
        addCellNumber(sheet, 1, 10, numberOfAllDependencies_Access);
        addCellNumber(sheet, 2, 10, numberOfAllDependencies_Access_Direct);
        addCellNumber(sheet, 3, 10, numberOfAllDependencies_Access_Indirect);
        addCellDefault(sheet, 0, 11, "Inheritance");
        addCellNumber(sheet, 1, 11, numberOfAllDependencies_Inheritance);
        addCellNumber(sheet, 2, 11, numberOfAllDependencies_Inheritance_Direct);
        addCellNumber(sheet, 3, 11, numberOfAllDependencies_Inheritance_Indirect);
        addCellDefault(sheet, 0, 12, "Annotation");
        addCellNumber(sheet, 1, 12, numberOfAllDependencies_Annotation);
        addCellNumber(sheet, 2, 12, numberOfAllDependencies_Annotation_Direct);
        addCellNumber(sheet, 3, 12, numberOfAllDependencies_Annotation_Indirect);

        addCellBold(sheet, 0, 14, "Inheritance related dependencies, all");
        addCellNumber(sheet, 1, 14, numberOfInheritanceRelatedDependencies_Total);
        addCellNumber(sheet, 2, 14, numberOfInheritanceRelatedDependencies_Total_Direct);
        addCellNumber(sheet, 3, 14, numberOfInheritanceRelatedDependencies_Total_Indirect);
        addCellDefault(sheet, 0, 15, "Inheritance relation");
        addCellNumber(sheet, 1, 15, numberOfAllDependencies_Inheritance);
        addCellNumber(sheet, 2, 15, numberOfAllDependencies_Inheritance_Direct);
        addCellNumber(sheet, 3, 15, numberOfAllDependencies_Inheritance_Indirect);
        addCellDefault(sheet, 0, 16, "Access of inherited variable");
        addCellNumber(sheet, 1, 16, numberOfInheritanceRelatedDependencies_Access);
        addCellNumber(sheet, 2, 16, numberOfInheritanceRelatedDependencies_Access_Direct);
        addCellNumber(sheet, 3, 16, numberOfInheritanceRelatedDependencies_Access_Indirect);
        addCellDefault(sheet, 0, 17, "Call of inherited method");
        addCellNumber(sheet, 1, 17, numberOfInheritanceRelatedDependencies_Call);
        addCellNumber(sheet, 2, 17, numberOfInheritanceRelatedDependencies_Call_Direct);
        addCellNumber(sheet, 3, 17, numberOfInheritanceRelatedDependencies_Call_Indirect);

        addCellBold(sheet, 0, 19, "Inner class related dependencies, all");
        addCellNumber(sheet, 1, 19, numberOfInnerClassRelatedDependencies_Total);
        addCellNumber(sheet, 2, 19, numberOfInnerClassRelatedDependencies_Total_Direct);
        addCellNumber(sheet, 3, 19, numberOfInnerClassRelatedDependencies_Total_Indirect);

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
