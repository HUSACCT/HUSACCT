package husacct.analyse.presentation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import javax.swing.AbstractListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

class DependencyPanel extends JPanel implements ListSelectionListener{  
	
	private static final long serialVersionUID = 1L;
	private static final Color PANELBACKGROUND = UIManager.getColor("Panel.background");
	
	private GroupLayout theLayout;
	private JScrollPane fromModuleScrollPane, toModuleScrollPane, dependencyScrollPane;
	private JTable dependencyTable;
	private JList fromModuleList, toModuleList;
	private AbstractListModel listModel;
	private AbstractTableModel tableModel;
	
	private List<AnalysedModuleDTO> fromSelected = new ArrayList<AnalysedModuleDTO>(); 
	private List<AnalysedModuleDTO> toSelected = new ArrayList<AnalysedModuleDTO>();
	
	private AnalyseUIController dataControl;
	
	public DependencyPanel(){
		dataControl = new AnalyseUIController();
		createLayout();
		
		dependencyTable = new JTable();
		tableModel = new DependencyTableModel(new ArrayList<DependencyDTO>());
		dependencyTable.setModel(tableModel);
		dependencyScrollPane.setViewportView(dependencyTable);
		
		toModuleList = new JList();
		toModuleScrollPane.setViewportView(toModuleList);

		fromModuleList = new JList();
		fromModuleList.setSelectionBackground(UIManager.getColor("textInactiveText"));
		fromModuleScrollPane.setViewportView(fromModuleList);
		
		listModel = new ModuleListData(dataControl.listAllModules());
		fillListWithModules(fromModuleList);
		fillListWithModules(toModuleList);
		fromModuleList.setBackground(PANELBACKGROUND);
		toModuleList.setBackground(PANELBACKGROUND);
		dependencyTable.setBackground(UIManager.getColor("Panel.background"));
		setLayout(theLayout);
	}
	
	private void fillListWithModules(JList list){
		list.setCellRenderer(new ModuleListCellRenderer());
		list.setModel(listModel);
		list.repaint();
		list.addListSelectionListener(this);
	}
	
	private void createLayout(){
		fromModuleScrollPane = new JScrollPane();
		fromModuleScrollPane.setBorder(new TitledBorder("From Module"));
		
		toModuleScrollPane = new JScrollPane();
		toModuleScrollPane.setBorder(new TitledBorder("To Module"));
		
		dependencyScrollPane = new JScrollPane();
		dependencyScrollPane.setBorder(new TitledBorder("Found Depdencies for selected modules"));
		
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
		fromModuleScrollPane.setBackground(PANELBACKGROUND);
		toModuleScrollPane.setBackground(PANELBACKGROUND);
		dependencyScrollPane.setBackground(UIManager.getColor("Panel.background"));
	}
	
	@Override
	public void valueChanged(ListSelectionEvent clickEvent) {
		if(clickEvent.getValueIsAdjusting()){
			if(clickEvent.getSource() == fromModuleList){
				fromSelected.clear();
				for(int index: fromModuleList.getSelectedIndices()){
					fromSelected.add((AnalysedModuleDTO)listModel.getElementAt(index));
				}
			}else if(clickEvent.getSource() == toModuleList){
				toSelected.clear();
				for(int index: fromModuleList.getSelectedIndices()){
					toSelected.add((AnalysedModuleDTO)listModel.getElementAt(index));
				}
			}
		}
		updateTableModel();
	}
	
	private void updateTableModel(){
		//TODO Erik Blanken Correctly fill the dependency-list from controller-call.
		List<DependencyDTO> allFoundDependencies = new ArrayList<DependencyDTO>();
		for(AnalysedModuleDTO selectedFrom: fromSelected){
			for(AnalysedModuleDTO selectedTo: toSelected){
				allFoundDependencies.addAll(dataControl.listDependencies(selectedFrom.uniqueName, selectedTo.uniqueName));
			}
		}
		dependencyTable.setModel(new DependencyTableModel(allFoundDependencies));
		dependencyTable.repaint();
	}
 }
