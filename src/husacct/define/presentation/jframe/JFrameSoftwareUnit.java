package husacct.define.presentation.jframe;

import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.task.SoftwareUnitController;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class JFrameSoftwareUnit extends JFrame implements ActionListener, KeyListener{

	private static final long serialVersionUID = 3093579720278942807L;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JLabel jLabelSelectSu;
	public JButton jButtonSave;
	public JButton jButtonCancel;
	public JComboBox jComboBoxSoftwareUnit;
	
	private SoftwareUnitController softwareUnitController;


	public JFrameSoftwareUnit(SoftwareUnitController softwareUnitController) {
		super();
		this.softwareUnitController = softwareUnitController;
		initUI();
		fillSoftwareUnitCombobox();
	}

	/**
	 * Creating Gui
	 */
	private void initUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setTitle("New Software Unit");
			setIconImage(new ImageIcon(getClass().getClassLoader().getResource("husacct/define/presentation/resources/jframeicon.jpg")).getImage());
			{
				jPanel1 = new JPanel();
				GridBagLayout jPanel1Layout = new GridBagLayout();
				jPanel1Layout.rowWeights = new double[] { 0.0, 0.0, 0.1 };
				jPanel1Layout.rowHeights = new int[] { 23, 29, 7 };
				jPanel1Layout.columnWeights = new double[] { 0.0, 0.1 };
				jPanel1Layout.columnWidths = new int[] { 132, 7 };
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				jPanel1.setLayout(jPanel1Layout);
				jPanel1.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
				{
					jLabelSelectSu = new JLabel();
					jPanel1.add(jLabelSelectSu, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabelSelectSu.setText("Selecteer software definitie");
				}
				{
					jComboBoxSoftwareUnit = new JComboBox();
					jPanel1.add(jComboBoxSoftwareUnit, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				}
			}
			{
				jPanel2 = new JPanel();
				getContentPane().add(jPanel2, BorderLayout.SOUTH);
				{
					jButtonCancel = new JButton();
					jPanel2.add(jButtonCancel);
					jButtonCancel.setText("Cancel");
					jButtonCancel.addActionListener(this);
				}
				{
					jButtonSave = new JButton();
					jPanel2.add(jButtonSave);
					jButtonSave.setText("Add");
					jButtonSave.addActionListener(this);
				}

			}
			pack();
			this.setSize(677, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void fillSoftwareUnitCombobox() {
		ArrayList<SoftwareUnitDefinition> softwareUnitList = new ArrayList<SoftwareUnitDefinition>();
		softwareUnitController.fillSoftwareUnitsList(softwareUnitList);	
		ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(softwareUnitList.toArray());
		jComboBoxSoftwareUnit.setModel(jComboBox1Model);
	}

	/**
	 * Handling ActionPerformed
	 */
	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == this.jButtonSave) {
			this.save();
		} else if (action.getSource() == this.jButtonCancel) {
			this.cancel();
		}
	}

	private void cancel() {
		this.dispose();
	}

	private void save() {
		String displayedRow = jComboBoxSoftwareUnit.getSelectedItem().toString();
		String[] softwareUnitDetails = displayedRow.split("-");
		String softwareUnitName = softwareUnitDetails[0].trim();
		String softwareUnitType = softwareUnitDetails[1].trim();
		softwareUnitController.save(softwareUnitName, softwareUnitType);
		this.dispose();
	}
	
	/**
	 * Handling KeyPresses
	 */
	public void keyPressed(KeyEvent arg0) {
		// Ignore
	}

	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.dispose();
		}
	}

	public void keyTyped(KeyEvent arg0) {
		// Ignore
	}
	

}
