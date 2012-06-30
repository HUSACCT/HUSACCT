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
import javax.swing.JPanel;
import java.awt.FlowLayout;

public class AnalyseInternalFrame extends JInternalFrame implements ActionListener, ILocaleChangeListener{

	private static final long serialVersionUID = 1L;
	private ApplicationStructurePanel treePanel;
	private DependencyPanel dependencyPanel;
	private JTabbedPane tabPanel;
	private AnalyseUIController controller;
	private JPanel cancelPanel;
	private JButton cancelButton;
	private JButton exportDependenciesButton;

	public AnalyseInternalFrame() {
		this.controller = new AnalyseUIController();
		registerLocaleChangeListener();
		setResizable(true);
		setBounds(200, 200, 550, 522);
		
		tabPanel = new JTabbedPane(JTabbedPane.TOP);
		tabPanel.setBackground(UIManager.getColor("Panel.background"));
		getContentPane().add(tabPanel, BorderLayout.CENTER);
		
		setIconifiable(true);
		setVisible(true);
		
		treePanel = new ApplicationStructurePanel();
		dependencyPanel = new DependencyPanel();
		tabPanel.addTab(controller.translate("SourceOverview"), null, treePanel, null);
		tabPanel.addTab(controller.translate("DependencyOverview"), null, dependencyPanel, null);
		
		cancelPanel = new JPanel();
		getContentPane().add(cancelPanel, BorderLayout.SOUTH);
		cancelPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		
		exportDependenciesButton = new JButton(controller.translate("ExportDependencies"));
		exportDependenciesButton.addActionListener(this);
		cancelPanel.add(exportDependenciesButton);
		
		cancelButton = new JButton(controller.translate("Cancel"));
		cancelPanel.add(cancelButton);
		cancelButton.addActionListener(this);
		reloadText();
	}
	
	private void registerLocaleChangeListener(){
		ServiceProvider.getInstance().getControlService().addLocaleChangeListener(this);
	}
	
	public void reloadText(){
		this.setTitle(controller.translate("AnalysedWindowTitle"));
		tabPanel.setTitleAt(0, controller.translate("SourceOverview"));
		tabPanel.setTitleAt(1, controller.translate("DependencyOverview"));
		cancelButton.setText(controller.translate("Cancel"));
		exportDependenciesButton.setText(controller.translate("ExportDependencies"));
		exportDependenciesButton.repaint();
		cancelButton.repaint();
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
		else if(clickEvent.getSource() == exportDependenciesButton){
			new ExportDependenciesDialog(controller);
		}
	}

	@Override
	public void update(Locale newLocale) {
		reloadText();		
	}
}
