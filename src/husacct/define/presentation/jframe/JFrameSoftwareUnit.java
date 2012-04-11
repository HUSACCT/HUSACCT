package husacct.define.presentation.jframe;

import husacct.define.presentation.tables.JTableException;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;



/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class JFrameSoftwareUnit extends JFrame {

	{
		// Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final long serialVersionUID = 3093579720278942807L;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JPanel jPanel3;
	private JPanel jPanel4;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JScrollPane jScrollPane1;
//	public JButton jButtonAddExceptionRow;
//	public JButton jButtonRemoveExceptionRow;
	public JButton jButtonSave;
	public JButton jButtonCancel;
	public JComboBox jComboBoxSoftwareUnit;
//	public JTableException jTableException;

	/**
	 * Auto-generated main method to display this JFrame
	 */

	public JFrameSoftwareUnit(String[] comboBoxValues) {
		super();
		initUI(comboBoxValues);
	}

	private void initUI(String[] comboBoxValues) {
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
					jLabel2 = new JLabel();
					jPanel1.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
					jLabel2.setText("Selecteer software definitie");
				}
				{
//					old =
//					ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(new String[] { 
//							SoftwareUnitDefinition.PACKAGE, 
//							SoftwareUnitDefinition.CLASS, 
//							SoftwareUnitDefinition.METHOD });
					ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(comboBoxValues);
					jComboBoxSoftwareUnit = new JComboBox();
					jPanel1.add(jComboBoxSoftwareUnit, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
					jComboBoxSoftwareUnit.setModel(jComboBox1Model);
				}
				
				
				
				
				
				// IS DIT GEDEELTE NODIG? DENK HET NIET?
//				{
//					jLabel3 = new JLabel();
//					jPanel1.add(jLabel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//					jLabel3.setText("Exceptions");
//				}
//				{
//					jPanel3 = new JPanel();
//					BorderLayout jPanel3Layout = new BorderLayout();
//					jPanel3.setLayout(jPanel3Layout);
//					jPanel1.add(jPanel3, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//					{
//						jScrollPane1 = new JScrollPane();
//						jPanel3.add(jScrollPane1, BorderLayout.CENTER);
//						jScrollPane1.setPreferredSize(new java.awt.Dimension(516, 55));
//						{
//							jTableException = new JTableException();
//							jScrollPane1.setViewportView(jTableException);
//						}
//					}
//					{
//						jPanel4 = new JPanel();
//						GridBagLayout jPanel4Layout = new GridBagLayout();
//						jPanel3.add(jPanel4, BorderLayout.EAST);
//						jPanel4Layout.rowWeights = new double[] { 0.0, 0.1 };
//						jPanel4Layout.rowHeights = new int[] { 15, 7 };
//						jPanel4Layout.columnWeights = new double[] { 0.1 };
//						jPanel4Layout.columnWidths = new int[] { 7 };
//						jPanel4.setLayout(jPanel4Layout);
//						jPanel4.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
//						{
//							jButtonAddExceptionRow = new JButton();
//							jPanel4.add(jButtonAddExceptionRow, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//							jButtonAddExceptionRow.setText("Add row");
//						}
//						{
//							jButtonRemoveExceptionRow = new JButton();
//							jPanel4.add(jButtonRemoveExceptionRow, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//							jButtonRemoveExceptionRow.setText("Remove row");
//						}
//					}
//				}
			}
			{
				jPanel2 = new JPanel();
				getContentPane().add(jPanel2, BorderLayout.SOUTH);
				{
					jButtonCancel = new JButton();
					jPanel2.add(jButtonCancel);
					jButtonCancel.setText("Cancel");
				}
				{
					jButtonSave = new JButton();
					jPanel2.add(jButtonSave);
					jButtonSave.setText("Add");
				}

			}
			pack();
			this.setSize(677, 300);
		} catch (Exception e) {
			// add your error handling code here
			e.printStackTrace();
		}
	}

}
