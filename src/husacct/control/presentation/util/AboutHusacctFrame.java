package husacct.control.presentation.util;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AboutHusacctFrame extends JFrame {
	
		private static final long serialVersionUID = 1L;

		private JPanel logoPanel, textPanel;
		private JLabel pictureLabel, husacctLabel, versionLabel, versionNumberLabel;
		private JButton okButton;
		private String versionNumber = "1.0";
		
		private GridBagConstraints constraint = new GridBagConstraints();
		
		public AboutHusacctFrame() {
			super();
			setTitle("About HUSACCT");
			setup();
			addComponents();
			setListeners();
		}

		private void setup(){
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.setSize(new Dimension(420, 380));
			this.setLayout(new FlowLayout());
			this.setResizable(false);
			this.setVisible(true);
		}

		private void addComponents(){
			logoPanel = new JPanel();
			textPanel = new JPanel();
			textPanel.setLayout(new GridBagLayout());
			
			Image logo = Toolkit.getDefaultToolkit().getImage("img/husacct.png");
			pictureLabel = new JLabel(new ImageIcon(logo));
			husacctLabel = new JLabel("HUSACCT");
			versionLabel = new JLabel("Version");
			versionNumberLabel = new JLabel(versionNumber);
			okButton = new JButton("OK");
			
			logoPanel.add(pictureLabel);
			textPanel.add(husacctLabel, getConstraint(0, 0));
			textPanel.add(versionLabel, getConstraint(0, 1));	
			textPanel.add(versionNumberLabel, getConstraint(1, 1));
			
			add(logoPanel);
			add(textPanel);
			add(okButton);	
		}

		private void setListeners(){
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		
		private GridBagConstraints getConstraint(int gridx, int gridy){
			constraint.fill = GridBagConstraints.BOTH;
			constraint.insets = new Insets(3, 3, 3, 3);
			constraint.gridx = gridx;
			constraint.gridy = gridy;
			return constraint;		
		}

	}

