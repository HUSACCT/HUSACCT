package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.common.locale.ILocaleService;
import husacct.control.task.MainController;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AboutDialog extends JDialog{

	private static final long serialVersionUID = 1L;

	private Image  img = null;
	private JPanel logoPanel, textPanel, gitForkPanel;
	private JLabel forkLabel,pictureLabel, husacctLabel, versionLabel, versionNumberLabel;
	private JButton okButton, creditsButton;
	private String versionNumber = "1.0";
	private MainController mainController;
	private GridBagConstraints constraint = new GridBagConstraints();
	private JPanel forkLabelPanel;

	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();

	public AboutDialog(MainController mainController) {
		super(mainController.getMainGui(), true);
		this.mainController = mainController;
		setTitle(localeService.getTranslatedString("About"));
		setup();
		addComponents();
		setListeners();
		this.setVisible(true);
	}

	private void setup(){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(420, 380));
		this.setLayout(new FlowLayout(0,0,0));
		this.setResizable(false);
		DialogUtils.alignCenter(this);
	}

	private void addComponents(){


		int i = 1+ (int)(Math.random() * 4);
		switch(i) {
		case 1: 
			img = Toolkit.getDefaultToolkit().getImage(Resource.get(Resource.GIT_FORK_1));
			break;
		case 2:
			img = Toolkit.getDefaultToolkit().getImage(Resource.get(Resource.GIT_FORK_2));
			break;
		case 3:
			img = Toolkit.getDefaultToolkit().getImage(Resource.get(Resource.GIT_FORK_3));
			break;
		case 4:
			img = Toolkit.getDefaultToolkit().getImage(Resource.get(Resource.GIT_FORK_4));
			break;
		default:
			img = Toolkit.getDefaultToolkit().getImage(Resource.get(Resource.GIT_FORK_1));
			break;
		}



		gitForkPanel = new JPanel();
		gitForkPanel.setLayout(new FlowLayout(0,0,0));
		gitForkPanel.setPreferredSize(new Dimension(420,256));		
		gitForkPanel.setBounds(0,0,420,300);
		gitForkPanel.setLocation(0,0);

		forkLabelPanel = new JPanel();
		forkLabelPanel.setLayout(new FlowLayout(0,0,0));
		forkLabelPanel.setPreferredSize(new Dimension(150,256));

		forkLabel = new JLabel(new ImageIcon (img));
		forkLabel.setBounds(0,0,150,150);	

		forkLabelPanel.add(forkLabel);


		JPanel logoLabelPanel = new JPanel();
		logoLabelPanel.setLayout(new FlowLayout(0,0,0));
		logoLabelPanel.setPreferredSize(new Dimension(256,256));

		Image logo = Toolkit.getDefaultToolkit().getImage(Resource.get(Resource.HUSACCT_LOGO));
		pictureLabel = new JLabel(new ImageIcon(logo));
		pictureLabel.setBounds(0,0,256,256);
		logoLabelPanel.add(pictureLabel);		


		gitForkPanel.add(forkLabelPanel);
		gitForkPanel.add(logoLabelPanel);

		textPanel = new JPanel();
		husacctLabel = new JLabel("\u00a9 2013 HUSACCT");
		versionLabel = new JLabel(localeService.getTranslatedString("VersionLabel"));
		versionNumberLabel = new JLabel(versionNumber);


		JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(420,70));

		okButton = new JButton(localeService.getTranslatedString("OkButton"));
		creditsButton = new JButton(localeService.getTranslatedString("Credits"));
		getRootPane().setDefaultButton(okButton);
		getRootPane().add(creditsButton);
		buttonPanel.add(okButton);
		buttonPanel.add(creditsButton);
		textPanel.add(husacctLabel);//, getConstraint(0, 0));
		textPanel.add(versionLabel);//, getConstraint(0, 1));	
		textPanel.add(versionNumberLabel);//, getConstraint(1, 1));

		add(gitForkPanel);

		add(buttonPanel);	
		add(textPanel);
	}

	private void setListeners(){
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		creditsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreditsDialog cd = new CreditsDialog(mainController);
			}
		});
		forkLabelPanel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if(e.getX() + e.getY() > 90 && e.getX() + e.getY() < 140) {

						Desktop.getDesktop().browse(new URI("https://github.com/HUSACCT/HUSACCT"));
					}

				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}
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