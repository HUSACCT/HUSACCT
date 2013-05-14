package husacct.define.presentation.tables;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.TableColumn;

import husacct.ServiceProvider;
import husacct.control.ILocaleChangeListener;
import husacct.define.domain.services.WarningMessageService;
import husacct.define.domain.warningmessages.CodeLevelWarning;
import husacct.define.domain.warningmessages.WarningMessage;

public class JTableWarningTable extends AbstractJTable implements
		ILocaleChangeListener, Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 406911091374481179L;
	private ArrayList<WarningMessage> warnings;

	public JTableWarningTable() {
		super();
		warnings = WarningMessageService.getInstance().getWarningMessages();

	}

	@Override
	protected void setDefaultTableSettings() {

	}

	@Override
	protected void setColumnHeaders() {
		tablemodel.addColumn("Description");
		tablemodel.addColumn("Resource");
		tablemodel.addColumn("Location");
		tablemodel.addColumn("Type");

	}

	public void changeColumnHeaders() {
		this.getTableHeader().getColumnModel().getColumn(0)
				.setHeaderValue("Description");
		this.getTableHeader().getColumnModel().getColumn(1)
				.setHeaderValue("Resource");
		this.getTableHeader().getColumnModel().getColumn(2)
				.setHeaderValue("Location");
		this.getTableHeader().getColumnModel().getColumn(3)
				.setHeaderValue("Type");
	}

	@Override
	protected void setColumnWidths() {
		TableColumn column = null;
		for (int i = 0; i < getColumnCount(); i++) {
			column = getColumnModel().getColumn(i);
			if (i == 0) {
				column.setPreferredWidth(150);
			} else if (i == 1) {
				column.setPreferredWidth(150);
			} else if (i == 2) {
				column.setPreferredWidth(150);
			}
		}
	}

	public int getRuleTypeColumnIndex() {
		return 0;
	}

	@Override
	public void update(Locale newLocale) {
		this.changeColumnHeaders();

	}

	@Override
	public int getRowCount() {
		return warnings.size();
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		WarningMessage message = warnings.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return message.getDescription();
		case 1:
			return message.getResource();
		case 2:
			return message.getLocation();
		case 3:
			return message.getType();

		}
		return "defualt";

	}

	@Override
	public void update(Observable o, Object arg) {
		this.revalidate();

	}

}
