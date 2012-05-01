package husacct.analyse.presentation;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class DependencyPanel extends JPanel implements ListSelectionListener{  
	
	private static final long serialVersionUID = 1L;
	
	private GroupLayout theLayout;
	private JScrollPane fromModuleScrollPane, toModuleScrollPane, dependencyScrollPane;
	private JTable dependencyTable;
	private JList fromModuleList, toModuleList;
	
	private JLabel fromSelected, toSelected;
	private AnalyseUIController dataControl;
	
	public DependencyPanel(){
		dataControl = new AnalyseUIController();
		createLayout();
		
		dependencyTable = new JTable();
		dependencyScrollPane.setViewportView(dependencyTable);
		
		toModuleList = new JList();
		toModuleScrollPane.setViewportView(toModuleList);

		fromModuleList = new JList();
		fromModuleScrollPane.setViewportView(fromModuleList);
		
		fillListWithModules(fromModuleList);
		fillListWithModules(toModuleList);
		setLayout(theLayout);
	}
	
	private void fillListWithModules(JList list){
		ModuleListData listData = new ModuleListData(dataControl.listAllModules());
		list.setCellRenderer(new ModuleListCellRenderer());
		list.setModel(listData);
		list.repaint();
	}
	
	private void createLayout(){
		fromModuleScrollPane = new JScrollPane();
		fromModuleScrollPane.setBorder(new TitledBorder("From Module"));
		fromModuleScrollPane.setBackground(UIManager.getColor("Panel.background"));
		
		toModuleScrollPane = new JScrollPane();
		toModuleScrollPane.setBorder(new TitledBorder("To Module"));
		toModuleScrollPane.setBackground(UIManager.getColor("Panel.background"));
		
		dependencyScrollPane = new JScrollPane();
		dependencyScrollPane.setBorder(new TitledBorder("Found Depdencies for selected modules"));
		dependencyScrollPane.setBackground(UIManager.getColor("Panel.background"));
		theLayout = new GroupLayout(this);
		theLayout.setHorizontalGroup(
				theLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(theLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(theLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(dependencyScrollPane, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
						.addGroup(theLayout.createSequentialGroup()
							.addComponent(fromModuleScrollPane, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(toModuleScrollPane, GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)))
					.addContainerGap())
		);
		theLayout.setVerticalGroup(
				theLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(theLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(theLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(fromModuleScrollPane, GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
						.addComponent(toModuleScrollPane))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(dependencyScrollPane, GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
					.addContainerGap())
		);
	}

	@Override
	public void valueChanged(ListSelectionEvent clickEvent) {
		
	}
 }
