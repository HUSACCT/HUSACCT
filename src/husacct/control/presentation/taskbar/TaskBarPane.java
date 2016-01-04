package husacct.control.presentation.taskbar;

import java.awt.Dimension;

import husacct.common.help.presentation.HelpableJScrollPane;

public class TaskBarPane extends HelpableJScrollPane {
	private static final long serialVersionUID = 1L;
	private TaskBar taskBar;
	
	public TaskBarPane(TaskBar taskBar){
		this.taskBar = taskBar;
		setViewportView(taskBar);
		getHorizontalScrollBar().setOpaque(true);
	}

	 @Override
	    public Dimension getPreferredSize() {
	        Dimension preferredSize = taskBar.getPreferredSize();
	        boolean horShow = getHorizontalScrollBar().isVisible();
	        if (horShow) {
	        	preferredSize.height += 18;
	        }
	        return preferredSize;
	    }

}
