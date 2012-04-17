package husacct.control.presentation.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class ExportMenu extends JMenu{
	public ExportMenu(){
		super("Export");
		
		JMenuItem exportLogicalArchitectureItem = new JMenuItem("Logical architecture");
		this.add(exportLogicalArchitectureItem);
		exportLogicalArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: logical architecture
			}
		});

		JMenuItem exportPhysicalArchitectureItem = new JMenuItem("Physical architecture");
		this.add(exportPhysicalArchitectureItem);
		exportPhysicalArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: logical architecture
			}
		});
		
		JMenuItem exportViolationReportItem = new JMenuItem("Violation report");
		this.add(exportViolationReportItem);
		
		exportViolationReportItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: Violation report
			}
		});
	}
}
