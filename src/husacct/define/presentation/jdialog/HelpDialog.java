package husacct.define.presentation.jdialog;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.common.locale.ILocaleService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HelpDialog extends JDialog {

	private static final long serialVersionUID = 6529710009481789767L;
	private JPanel imagePanel, introductionPanel, textPanel, containerPanel, buttonPanel;
	private JLabel pictureLabel, introductionText, contentText;
	private JButton closeButton;
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	
	public HelpDialog(){
		super();
		this.setTitle(localeService.getTranslatedString("?TooltipDefineArchitecture"));
		setup();
		addComponents();
		setListeners();
		this.setVisible(true);
	}
	
	private void setup(){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(600, 675));
		this.setLayout(new BorderLayout());
		this.setResizable(false);
		this.getContentPane().setBackground(Color.WHITE);
		ServiceProvider.getInstance().getControlService().centerDialog(this);
		setIconImage(new ImageIcon(Resource.get(Resource.ICON_QUESTIONMARK)).getImage());
	}
	
	private void addComponents(){
		imagePanel = new JPanel();
		introductionPanel = new JPanel();
		textPanel = new JPanel();
		buttonPanel = new JPanel();
		
		
		containerPanel = new JPanel(new FlowLayout());
		containerPanel.setBackground(Color.WHITE);
		
		introductionPanel.setLayout(new GridBagLayout());
		textPanel.setLayout(new GridBagLayout());
		
		Image logo = Toolkit.getDefaultToolkit().getImage(Resource.get(Resource.DEFINE_WORKFLOW));
		pictureLabel = new JLabel(new ImageIcon(logo));
		closeButton = new JButton(localeService.getTranslatedString("Close"));
		
		getRootPane().setDefaultButton(closeButton);
		imagePanel.add(pictureLabel);
		
		introductionText = new JLabel();
		initiateLabel(introductionText, "DefineHelpDialogIntroductionText");
		introductionPanel.add(introductionText);
		
		contentText = new JLabel();
		initiateLabel(contentText, "DefineHelpDialogContentText");
		textPanel.add(contentText);
		
		containerPanel.add(introductionPanel);
		containerPanel.add(pictureLabel);
		containerPanel.add(textPanel);
		buttonPanel.add(closeButton);
		
		this.add(containerPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	private void setListeners(){
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
	
	private void initiateLabel(JLabel label, String name){
		label.setOpaque(true);
		label.setBorder(BorderFactory.createLineBorder(Color.WHITE,0));
		label.setBackground(Color.WHITE);
		label.setText(String.format("<html><div width=%d>%s</div></html>", 495, localeService.getTranslatedString(name)));
	}
}
