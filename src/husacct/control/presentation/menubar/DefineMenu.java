package husacct.control.presentation.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class DefineMenu extends JMenu{
	public DefineMenu(){
		
		super("Define");
		
		JMenuItem defineLogicalArchitectureItem = new JMenuItem("Define logical architecture");
		this.add(defineLogicalArchitectureItem);
		defineLogicalArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: define logical
			}
		});
		
		JMenuItem importLogicalArchitectureItem = new JMenuItem("Import logical architecture");
		this.add(importLogicalArchitectureItem);
		importLogicalArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: import
			}
		});
		
		JMenuItem importArchitectureItem = new JMenuItem("Map architecture to project");
		this.add(importArchitectureItem);
		importArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: map
			}
		});
	}
}
