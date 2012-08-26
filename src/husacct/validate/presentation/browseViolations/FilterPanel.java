package husacct.validate.presentation.browseViolations;

import husacct.ServiceProvider;
import husacct.control.task.threading.ThreadWithLoader;
import husacct.validate.domain.validation.Violation;
import husacct.validate.presentation.BrowseViolations;
import husacct.validate.presentation.FilterViolations;
import husacct.validate.task.TaskServiceImpl;
import java.awt.Dimension;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

public class FilterPanel extends JPanel {

	private static final long serialVersionUID = 8013809183676306609L;
	private final BrowseViolations browseViolations;
	private final FilterViolations filterViolations;
	private JCheckBox applyFilter;
	private JButton buttonEditFilter;
	private JRadioButton rdbtnIndirect, rdbtnAll, rdbtnDirect;
	private static Logger logger = Logger.getLogger(FilterPanel.class);

	public FilterPanel(BrowseViolations browseViolations, TaskServiceImpl taskServiceImpl) {
		this.browseViolations = browseViolations;
		this.filterViolations = new FilterViolations(taskServiceImpl, browseViolations);
		initComponents();
		loadAfterChange();
	}

	private void initComponents() {
		applyFilter = new JCheckBox("Apply Filter");
		buttonEditFilter = new JButton("Edit Filter");
		rdbtnAll = new JRadioButton("All");
		rdbtnDirect = new JRadioButton("Direct");
		rdbtnIndirect = new JRadioButton("Indirect");

		ButtonGroup filterIndirectButtonGroup = new ButtonGroup();
		filterIndirectButtonGroup.add(rdbtnAll);
		filterIndirectButtonGroup.add(rdbtnDirect);
		filterIndirectButtonGroup.add(rdbtnIndirect);
		rdbtnAll.setSelected(true);

		createBaseLayout();
		addListeners();
	}

	private void createBaseLayout() {
		GroupLayout filterPane = new GroupLayout(this);

		GroupLayout.SequentialGroup horizontalRadioButtonGroup = filterPane.createSequentialGroup();
		horizontalRadioButtonGroup.addContainerGap();
		horizontalRadioButtonGroup.addComponent(rdbtnAll);
		horizontalRadioButtonGroup.addPreferredGap(ComponentPlacement.RELATED);
		horizontalRadioButtonGroup.addComponent(rdbtnDirect);
		horizontalRadioButtonGroup.addPreferredGap(ComponentPlacement.RELATED);
		horizontalRadioButtonGroup.addComponent(rdbtnIndirect);

		GroupLayout.ParallelGroup horizontalGroup = filterPane.createParallelGroup(Alignment.LEADING);
		horizontalGroup.addComponent(buttonEditFilter);
		horizontalGroup.addComponent(applyFilter);
		horizontalGroup.addGroup(horizontalRadioButtonGroup);

		filterPane.setHorizontalGroup(horizontalGroup);

		GroupLayout.ParallelGroup verticalRadioButtonGroup = filterPane.createParallelGroup(Alignment.LEADING, false);
		verticalRadioButtonGroup.addComponent(rdbtnAll);
		verticalRadioButtonGroup.addComponent(rdbtnDirect);
		verticalRadioButtonGroup.addComponent(rdbtnIndirect);

		GroupLayout.SequentialGroup verticalGroup = filterPane.createSequentialGroup();
		verticalGroup.addComponent(applyFilter);
		verticalGroup.addPreferredGap(ComponentPlacement.RELATED);
		verticalGroup.addComponent(buttonEditFilter);
		verticalGroup.addPreferredGap(ComponentPlacement.RELATED);
		verticalGroup.addGroup(verticalRadioButtonGroup);

		filterPane.setVerticalGroup(verticalGroup);

		setLayout(filterPane);

	}

	private void addListeners() {
		applyFilter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				browseViolations.loadAfterChange();
				;
			}
		});
		buttonEditFilter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final Toolkit toolkit = Toolkit.getDefaultToolkit();
				final Dimension screenSize = toolkit.getScreenSize();
				final int x = (screenSize.width - filterViolations.getWidth()) / 2;
				final int y = (screenSize.height - filterViolations.getHeight()) / 2;
				filterViolations.setLocation(x, y);

				filterViolations.setVisible(true);
			}
		});
		rdbtnAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final Thread filterThread = new Thread() {

					@Override
					public void run() {
						try {
							Thread.sleep(1);
							browseViolations.loadAfterChange();
						} catch (InterruptedException e) {
							logger.debug(e.getMessage());
						}
					}
				};
				ThreadWithLoader validateThread = ServiceProvider.getInstance().getControlService().getThreadWithLoader(ServiceProvider.getInstance().getLocaleService().getTranslatedString("FilteringLoading"), filterThread);
				validateThread.run();

			}
		});
		rdbtnDirect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final Thread filterThread = new Thread() {

					@Override
					public void run() {
						try {
							Thread.sleep(1);
							SwingUtilities.invokeLater(new Runnable() {

								public void run() {
									browseViolations.loadAfterChange();
								}
							});
						} catch (InterruptedException e) {
							logger.debug(e.getMessage());
						}
					}
				};
				ThreadWithLoader validateThread = ServiceProvider.getInstance().getControlService().getThreadWithLoader(ServiceProvider.getInstance().getLocaleService().getTranslatedString("FilteringLoading"), filterThread);
				validateThread.run();

			}
		});
		rdbtnIndirect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				final Thread filterThread = new Thread() {

					@Override
					public void run() {
						try {
							Thread.sleep(1);
							SwingUtilities.invokeLater(new Runnable() {

								public void run() {
									browseViolations.loadAfterChange();
								}
							});
						} catch (InterruptedException e) {
							logger.debug(e.getMessage());
						}
					}
				};
				ThreadWithLoader validateThread = ServiceProvider.getInstance().getControlService().getThreadWithLoader(ServiceProvider.getInstance().getLocaleService().getTranslatedString("FilteringLoading"), filterThread);
				validateThread.run();

			}
		});
	}

	public List<Violation> fillViolationsTable(List<Violation> violations) {
		
		List<Violation> violationsIndirect = new ArrayList<Violation>();

		if(rdbtnAll.isSelected()){
			return violations;
		}
		boolean isIndirect = rdbtnIndirect.isSelected();
		for (Violation violation : violations) {
			if (violation.isIndirect() == isIndirect) {
				violationsIndirect.add(violation);
			}
		}
		
		return violationsIndirect;
	}

	public void loadAfterChange() {
		loadText();
		filterViolations.loadFilterValues();
	}

	private void loadText() {
		setBorder(new TitledBorder(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Filter")));
		applyFilter.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ApplyFilter"));
		buttonEditFilter.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("EditFilter"));
	}

	public void setApplyFilter(JCheckBox applyFilter) {
		this.applyFilter = applyFilter;
	}

	public JCheckBox getApplyFilter() {
		return applyFilter;
	}
}
