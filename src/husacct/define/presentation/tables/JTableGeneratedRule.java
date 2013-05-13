package husacct.define.presentation.tables;

import husacct.ServiceProvider;
import husacct.control.ILocaleChangeListener;

import java.util.Locale;

import javax.swing.table.TableColumn;

public class JTableGeneratedRule extends AbstractJTable implements ILocaleChangeListener {
	
	private static final long serialVersionUID = -2157002105809963858L;

	public JTableGeneratedRule() {
		super();
	}
	
	@Override
	protected void setDefaultTableSettings() {
		
	}
	
	@Override
	protected void setColumnHeaders() {
		tablemodel.addColumn(ServiceProvider.getInstance().getLocaleService().getTranslatedString("RuleType"));
		tablemodel.addColumn(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ToModule"));
		tablemodel.addColumn("# " + ServiceProvider.getInstance().getLocaleService().getTranslatedString("Exceptions"));
	}
	
	public void changeColumnHeaders() {
		this.getTableHeader().getColumnModel().getColumn(0).setHeaderValue(ServiceProvider.getInstance().getLocaleService().getTranslatedString("RuleType"));
		this.getTableHeader().getColumnModel().getColumn(1).setHeaderValue(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ToModule"));
		this.getTableHeader().getColumnModel().getColumn(2).setHeaderValue(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Exceptions"));
	}
	
	@Override
	protected void setColumnWidths() {
		TableColumn column = null;
		for (int i = 0; i < getColumnCount(); i++) {
			column = getColumnModel().getColumn(i);
			if (i == 0) {
				column.setPreferredWidth(75); // Rule Type
			} else if (i == 1) {
				column.setPreferredWidth(75); // To layer
			} else if (i == 2) {
				column.setPreferredWidth(50); // Enabled
			} 
//			} else if (i == 4) {
//				column.setPreferredWidth(50); // Exceptions
//			}
		}
	}
	public int getRuleTypeColumnIndex(){
		return 0;
	}

	@Override
	public void update(Locale newLocale) {
		this.changeColumnHeaders();
		
	}
}