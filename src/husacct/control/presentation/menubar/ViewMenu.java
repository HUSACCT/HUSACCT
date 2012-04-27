package husacct.control.presentation.menubar;

import husacct.control.task.MainController;
import husacct.control.task.ViewController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class ViewMenu extends JMenu{
	
	private ViewController viewcontroller;
	private MainController maincontroller;
	
	public ViewMenu(ViewController viewController){
		super("View");
		this.viewcontroller = viewController;
		
		JMenuItem logicalArchitectureItem = new JMenuItem("Logical architecture");
		this.add(logicalArchitectureItem);
		logicalArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//maincontroller.getViewController().toggleDefineGui();
			}
		});
		
		
		JMenuItem physicalArchitectureItem = new JMenuItem("Physical architecture");
		this.add(physicalArchitectureItem);
		physicalArchitectureItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: Physical architecture
			}
		});
		
		JMenuItem violationsItem = new JMenuItem("Violations");
		this.add(violationsItem);
		violationsItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// TODO: Violations
			}
		});
	}
}
