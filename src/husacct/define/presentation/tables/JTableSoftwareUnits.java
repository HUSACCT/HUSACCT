package husacct.define.presentation.tables;

import husacct.ServiceProvider;
import husacct.control.ILocaleChangeListener;

import java.util.Locale;

import javax.swing.table.TableColumn;

public class JTableSoftwareUnits extends AbstractJTable implements ILocaleChangeListener {

	private static final long serialVersionUID = 3535559394466714205L;

	public JTableSoftwareUnits() {
		super();
	}
	
	@Override
	protected void setDefaultTableSettings() {
		
	}
	
	@Override
	protected void setColumnHeaders() {
		tablemodel.addColumn(ServiceProvider.getInstance().getLocaleService().getTranslatedString("SoftwareUnitName"));
		tablemodel.addColumn(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Type"));
	}
	
	public void changeColumnHeaders() {
		this.getTableHeader().getColumnModel().getColumn(0).setHeaderValue(ServiceProvider.getInstance().getLocaleService().getTranslatedString("SoftwareUnitName"));
		this.getTableHeader().getColumnModel().getColumn(1).setHeaderValue(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Type"));
	}
	
	@Override
	protected void setColumnWidths() {
		TableColumn column = null;
		for (int i = 0; i < getColumnCount(); i++) {
			column = getColumnModel().getColumn(i);
			if (i == 0) {
				column.setPreferredWidth(275); // Software unit name
			} else if (i == 2) {
				column.setPreferredWidth(25); // Type
			}
		}
	}

	@Override
	public void update(Locale newLocale) {
		this.changeColumnHeaders();		
	}
}
