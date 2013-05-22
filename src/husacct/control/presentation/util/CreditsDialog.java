package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.common.credits.creditsFetcher;
import husacct.common.locale.ILocaleService;
import husacct.control.task.MainController;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class CreditsDialog extends JDialog {
	
		private static final long serialVersionUID = 1L;

		private JPanel logoPanel, textPanel;
		private JLabel teacherLabelDescription, studentLabelDescription;
		private JTextArea teacherLabel;
		private JTextArea studentLabel;
		private JButton okButton, creditsButton;
		private String versionNumber = "1.0";
		
		private GridBagConstraints constraint = new GridBagConstraints();
		
		private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
		
		public CreditsDialog(MainController mainController) {
			super(mainController.getMainGui(), true);
			setTitle(localeService.getTranslatedString("Credits"));
			setup();
			addComponents();
			setListeners();
			this.setVisible(true);
			
		}
		private String getCreditsTeachers() {
			creditsFetcher cf = new creditsFetcher();
			List<String> teachers = cf.fetchTeacherNames();
			Collections.sort(teachers, new LastNameComperator());
			String teacherCreditsString = "";
			for(int i = 0; i < teachers.size() ; i++) {
				teacherCreditsString += teachers.get(i) + "\n";
			}
			return teacherCreditsString;
		}
		
		
		private String getCreditsStudents() {
			creditsFetcher cf = new creditsFetcher();
			List<String> students = cf.fetchStudentNames();
			Collections.sort(students, new LastNameComperator());
			String studentCreditsString = "";
			for(int i = 0; i < students.size() ; i++) {
				studentCreditsString += students.get(i) + ", ";
			}
			return studentCreditsString.substring(0, studentCreditsString.length() - 2) ;
			//cf.fetchTeacherNames();
		}

		private void setup(){
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.setSize(new Dimension(500, 300));
			this.setLayout(new FlowLayout());
			this.setResizable(false);
			DialogUtils.alignCenter(this);
		}

		private void addComponents(){
			
			textPanel = new JPanel();
			textPanel.setLayout(new GridBagLayout());
			teacherLabelDescription = new JLabel(localeService.getTranslatedString("Teachers") + ":");
			teacherLabelDescription.setFont(new Font("Arial", Font.BOLD, 14));
			teacherLabel = new JTextArea(getCreditsTeachers());
			teacherLabel.setLineWrap(true);
			teacherLabel.setWrapStyleWord(true);
			teacherLabel.setPreferredSize(new Dimension(400, 75));
			teacherLabel.setOpaque(false);
			teacherLabel.setFont(new Font("Arial", Font.PLAIN, 14));
			teacherLabel.setEditable(false);
			studentLabelDescription = new JLabel(localeService.getTranslatedString("Students") + ":");
			studentLabelDescription.setFont(new Font("Arial", Font.BOLD, 14));
			studentLabel = new JTextArea(getCreditsStudents());
			studentLabel.setLineWrap(true);
			studentLabel.setWrapStyleWord(true);
			studentLabel.setPreferredSize(new Dimension(400, 200));
			studentLabel.setOpaque(false);
			studentLabel.setFont(new Font("Arial", Font.PLAIN, 10));
			studentLabel.setEditable(false);
			textPanel.add(teacherLabelDescription, getConstraint(0,1, 6,1));
			textPanel.add(teacherLabel,getConstraint(0, 2,6,6));
			
			
			textPanel.add(studentLabelDescription, getConstraint(0,8,6,1));
			textPanel.add(studentLabel,getConstraint(0, 9,6,6));			
			okButton = new JButton("Close");
			
			add(textPanel);	
		}

		private void setListeners(){
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			
		
		}
		
		private GridBagConstraints getConstraint(int gridx, int gridy, int gridwidth, int gridheight){
			constraint.fill = GridBagConstraints.BOTH;
			constraint.insets = new Insets(3, 3, 3, 3);
			constraint.gridx = gridx;
			constraint.gridy = gridy;
			constraint.gridwidth = gridwidth;
			constraint.gridheight = gridheight;
			return constraint;		
		}
		private GridBagConstraints getConstraint(int gridx, int gridy){
			constraint.fill = GridBagConstraints.BOTH;
			constraint.insets = new Insets(3, 3, 3, 3);
			constraint.gridx = gridx;
			constraint.gridy = gridy;
			return constraint;		
		}
		
		public class LastNameComperator implements Comparator<String> {
		    @Override
		    public int compare(String s1, String s2) {
		       String[] StringArray1 = s1.split(" ");
		       String[] StringArray2 = s2.split(" ");
		       if(StringArray1.length == 0) {
		    	   return 0;
		       }
		       if(StringArray2.length == 0) {
		    	   return 0;
		       }
		       return (StringArray1[StringArray1.length-1].compareTo(StringArray2[StringArray2.length-1]));
		    }
		}

	}

