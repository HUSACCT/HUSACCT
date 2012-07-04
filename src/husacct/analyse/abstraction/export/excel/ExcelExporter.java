package husacct.analyse.abstraction.export.excel;

import common.Logger;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import husacct.ServiceProvider;
import husacct.analyse.abstraction.export.AbstractFileExporter;
import husacct.common.dto.DependencyDTO;

public class ExcelExporter extends AbstractFileExporter{

	private Logger husacctLogger = Logger.getLogger(ExcelExporter.class);
	private WritableCellFormat timesBold;
	private WritableCellFormat times;
	private String outputFile;
	
	public ExcelExporter(HashMap<String, DependencyDTO> data) {
		super(data);
	}

	@Override
	protected void write(String path){
		File file = new File(path);
		this.outputFile = path;
		WorkbookSettings documentSettings = new WorkbookSettings();
		documentSettings.setLocale(ServiceProvider.getInstance().getLocaleService().getLocale());
		try{
			WritableWorkbook workbook = Workbook.createWorkbook(file);
			workbook.createSheet(super.translate("Dependencies"), 0);
			WritableSheet excelSheet = workbook.getSheet(0);
			createLabels(excelSheet);
			createContent(excelSheet);
			workbook.write();
			workbook.close();
		} catch (IOException e) {
			husacctLogger.warn("Analyse - Couldn export dependencies to xls - File unknwon");
		} catch (WriteException e) {
			husacctLogger.warn("Analyse - Couldn write to xls - Reason unknwon");
		}
	}	
	
	private void createLabels(WritableSheet sheet) throws WriteException {
		WritableFont times10 = new WritableFont(WritableFont.TIMES, 10);
		times = new WritableCellFormat(times10);
		times.setWrap(true);
		WritableFont times10Bold = new WritableFont( WritableFont.TIMES, 10, WritableFont.BOLD, false);
		timesBold = new WritableCellFormat(times10Bold);
		timesBold.setWrap(true);
		
		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBold);
		cv.setAutosize(true);
		
		addCaption(sheet, 0, 0, super.translate("DependencyFrom"));
		addCaption(sheet, 1, 0, super.translate("DependencyTo"));
		addCaption(sheet, 2, 0, super.translate("DependencyType"));
		addCaption(sheet, 3, 0, super.translate("Linenumber"));
		addCaption(sheet, 4, 0, super.translate("Direct") + "/" + super.translate("Indirect"));
	}
	
	private void createContent(WritableSheet sheet) throws WriteException, RowsExceededException{
		int row = 3;
		Iterator<Entry<String, DependencyDTO>> iterator = data.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, DependencyDTO> currentEntry = (Entry<String, DependencyDTO>)iterator.next();
			writeDependency(sheet, row, currentEntry.getValue());
			row++;
		}
	}
	
	private void writeDependency(WritableSheet sheet, int row, DependencyDTO dependency) throws RowsExceededException, WriteException{
		Label fromLabel = new Label(0, row, dependency.from, times);
		Label toLabel = new Label(1, row, dependency.to, times);
		Label typeLabel = new Label(2, row, dependency.type, times);
		Label lineLabel = new Label(3, row, "" + dependency.lineNumber, times);
		Label directLabel;
		if(dependency.isIndirect) directLabel = new Label(4, row, super.translate("Indirect"), times);
		else directLabel = new Label(4, row, super.translate("Direct"), times);
		sheet.addCell(fromLabel); sheet.addCell(toLabel);
		sheet.addCell(typeLabel); sheet.addCell(lineLabel);
		sheet.addCell(directLabel);
	}
	
	private void addCaption(WritableSheet sheet, int column, int row, String s) throws RowsExceededException, WriteException {
		Label label;
		label = new Label(column, row, s, timesBold);
		sheet.addCell(label);
	}
}
