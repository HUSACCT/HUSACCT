package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.credits.creditsFetcher;
import husacct.common.locale.ILocaleService;
import husacct.control.task.MainController;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class CreditsDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel textPanel1, textPanel2, textPanel3, buttonPanel;
	private JLabel architectLabelDescription, processDescription, developerLabelDescription;
	private JTextArea architectLabel;
	private JTextArea processLabel;
	private JTextArea developersLabel;
	private JButton okButton;
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

	private String getCreditsDevelopers() {
		creditsFetcher cf = new creditsFetcher();
		List<String> developers = cf.fetchDeveloperNames();
		Collections.sort(developers, new LastNameComperator());
		String developerCreditsString = "";
		for(int i = 0; i < developers.size() ; i++) {
			developerCreditsString += developers.get(i) + ", ";
		}
		return developerCreditsString.substring(0, developerCreditsString.length() - 2) ;
	}

	private void setup(){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(500, 450));
		this.setLayout(new FlowLayout());
		this.setResizable(false);
		DialogUtils.alignCenter(this);
	}

	private void addComponents(){

		textPanel1 = new JPanel();
		textPanel1.setLayout(new GridBagLayout());
		architectLabelDescription = new JLabel("Architect and lead developer" + ":");
		architectLabelDescription.setFont(new Font("Arial", Font.BOLD, 14));
		architectLabel = new JTextArea("\n" + "Leo Pruijt" + "\n" + "HU University of Applied Sciences, Utrecht, The Netherlands");
		architectLabel.setLineWrap(true);
		architectLabel.setWrapStyleWord(true);
		architectLabel.setSize(new Dimension(400, 150));
		architectLabel.setOpaque(false);
		architectLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		architectLabel.setEditable(false);

		textPanel2 = new JPanel();
		textPanel2.setLayout(new GridBagLayout());
		processDescription = new JLabel("Process support version 1.0 and 2.0"  + ":");
		processDescription.setFont(new Font("Arial", Font.BOLD, 14));
		processLabel = new JTextArea("\n" + "Christian K\u00F6ppe, Michiel Borkent");
		processLabel.setLineWrap(true);
		processLabel.setWrapStyleWord(true);
		processLabel.setSize(new Dimension(400, 150));
		processLabel.setOpaque(false);
		processLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		processLabel.setEditable(false);
		
		textPanel3 = new JPanel();
		textPanel3.setLayout(new GridBagLayout());
		developerLabelDescription = new JLabel(localeService.getTranslatedString("Developers") + ":");
		developerLabelDescription.setFont(new Font("Arial", Font.BOLD, 14));
		developersLabel = new JTextArea("\n" + getCreditsDevelopers());
		developersLabel.setLineWrap(true);
		developersLabel.setWrapStyleWord(true);
		developersLabel.setSize(new Dimension(400, 300));
		developersLabel.setOpaque(false);
		developersLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		developersLabel.setEditable(false);

		textPanel1.add(architectLabelDescription, getConstraint(0,2,6,1));
		textPanel1.add(architectLabel,getConstraint(0,2,6,6));
		textPanel2.add(processDescription, getConstraint(0,5,6,1));
		textPanel2.add(processLabel,getConstraint(0,5,6,6));
		textPanel3.add(developerLabelDescription, getConstraint(0,8,6,1));
		textPanel3.add(developersLabel,getConstraint(0,8,6,6));			

		buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(400,70));
		okButton = new JButton(localeService.getTranslatedString("Close"));
		buttonPanel.add(okButton);

		add(textPanel1);
		add(textPanel2);
		add(textPanel3);
		add(buttonPanel);
	}

	private void setListeners(){
		okButton.addActionListener(new ActionListener() {
			@Override
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

