package husacct.validate.presentation.tableModels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.AbstractListModel;

@SuppressWarnings("serial")
public class CalendarListModel extends AbstractListModel{
	
	private final Calendar[] dates;
	private final List<Test> datesInText;
	
	public CalendarListModel(Calendar[] dates) {
		this.dates = dates;
		this.datesInText = new ArrayList<Test>();
		for(Calendar date : dates) {
			Test test = new Test();
			test.date = date;
			datesInText.add(test);
		}
		
	}
	
	public Calendar getDate(int index) {
		return dates[index];
	}

	@Override
	public Object getElementAt(int arg0) {
		return datesInText.get(arg0);
	}

	@Override
	public int getSize() {
		return dates.length;
	}

} 

class Test {
	protected String dateInText;
	protected Calendar date;
	
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm");
		if(date != null) {
			return format.format(date.getTime());
		}
		return "";
		
	}
}
