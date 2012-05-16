package husacct.define.presentation.jdialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class ViolationTypesJDialog extends JDialog{

	private static final long serialVersionUID = 6413960215557327449L;
//	private ArrayList<JCheckBox> violationCheckBoxes;
	private JPanel mainPanel;
	
	public ViolationTypesJDialog() {
		super();
		initGUI();
	}
	
	public void initGUI(){
		try {
			this.removeAll();
			this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Violation Types");
			this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("husacct/common/resources/husacct.png")).getImage());
			
			getContentPane().add(this.createViolationPanel(), BorderLayout.CENTER);
			
			this.setResizable(false);
			this.pack();
			this.setSize(300, 300);
			this.setModal(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Component createViolationPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(this.createViolationsLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		
//		GridBagConstraints gridBagConstraints;
		
//		for (Bla bla : blas){
//			gridBagConstraints = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
//			//addLabel
//			gridBagConstraints.gridx++;
//			gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
//			//AddCheckbox
//			//AddCgheckbox to violationCheckBoxes 
//
//			
//		}
		
		// TODO Auto-generated method stub
		return mainPanel;
	}

	private GridBagLayout createViolationsLayout() {
		GridBagLayout mainPanelLayout = new GridBagLayout();
		mainPanelLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.1 };
		mainPanelLayout.rowHeights = new int[] { 30, 23, 6, 0 };
		mainPanelLayout.columnWeights = new double[] { 0.0, 0.1 };
		mainPanelLayout.columnWidths = new int[] { 132, 7 };
		return mainPanelLayout;
	}

	
}
