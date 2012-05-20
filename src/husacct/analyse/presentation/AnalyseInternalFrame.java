package husacct.analyse.presentation;

import husacct.ServiceProvider;
import husacct.control.ILocaleChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.JButton;

public class AnalyseInternalFrame extends JInternalFrame implements ActionListener, ILocaleChangeListener{

	private static final long serialVersionUID = 1L;
	private ApplicationStructurePanel treePanel;
	private DependencyPanel dependencyPanel;
	private JTabbedPane tabPanel;
	private JButton cancelButton;
	private AnalyseUIController controller;

	public AnalyseInternalFrame() {
		setResizable(true);
		setBounds(200, 200, 550, 471);
		setFrameIcon(new ImageIcon("husacct/analyse/presentation/resources/husacct.png"));
		registerLocaleChangeListener();
		this.controller = new AnalyseUIController();
		
		tabPanel = new JTabbedPane(JTabbedPane.TOP);
		tabPanel.setBackground(UIManager.getColor("Panel.background"));
		getContentPane().add(tabPanel, BorderLayout.CENTER);
		
		setIconifiable(true);
		setVisible(true);
		
		treePanel = new ApplicationStructurePanel();
		dependencyPanel = new DependencyPanel();
		tabPanel.addTab(controller.translate("SourceOverview"), null, treePanel, null);
		tabPanel.addTab(controller.translate("DependencyOverview"), null, dependencyPanel, null);
		
		cancelButton = new JButton("Cancel");
		getContentPane().add(cancelButton, BorderLayout.SOUTH);
		cancelButton.addActionListener(this);
		cancelButton.setText(controller.translate("Cancel"));
	}
	
	private void registerLocaleChangeListener(){
		ServiceProvider.getInstance().getControlService().addLocaleChangeListener(this);
	}
	
	public void reloadText(){
		cancelButton.setText(controller.translate("Cancel"));
		tabPanel.setTitleAt(0, controller.translate("SourceOverview"));
		tabPanel.setTitleAt(1, controller.translate("DependencyOverview"));
		dependencyPanel.reload();
		treePanel.reload();
		tabPanel.repaint();
		this.invalidate();
		this.revalidate();
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent clickEvent) {
		if(clickEvent.getSource() == cancelButton){
			this.dispose();
		}
	}

	@Override
	public void update(Locale newLocale) {
		reloadText();		
	}
}
