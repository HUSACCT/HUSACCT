package husacct.validate.presentation.browseViolations;

import husacct.ServiceProvider;
import husacct.validate.domain.validation.Violation;
import husacct.validate.presentation.BrowseViolations;

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
import javax.swing.border.TitledBorder;

public class FilterPanel extends JPanel {
	
	private JCheckBox applyFilter;
	private JButton buttonEditFilter;
	private JRadioButton rdbtnIndirect, rdbtnAll, rdbtnDirect;
	private final BrowseViolations browseViolations;
	
	public FilterPanel(BrowseViolations browseViolations) {
		this.browseViolations = browseViolations;
		createBaseLayout();
	}
	
	public void createBaseLayout() {
		applyFilter = new JCheckBox("Apply Filter");
		applyFilter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				browseViolations.applyFilterChanged(arg0);
			}
		});

		buttonEditFilter = new JButton("Edit Filter");
		buttonEditFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				browseViolations.editFilterActionPerformed(e);
			}

		});

		rdbtnAll = new JRadioButton("All");

		rdbtnDirect = new JRadioButton("Direct");

		rdbtnIndirect = new JRadioButton("Indirect");
		
		ButtonGroup filterIndirectButtonGroup = new ButtonGroup();
		filterIndirectButtonGroup.add(rdbtnAll);
		filterIndirectButtonGroup.add(rdbtnDirect);
		filterIndirectButtonGroup.add(rdbtnIndirect);
		rdbtnAll.setSelected(true);

		rdbtnAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//TODO: add LoadingDialog from next merge
				browseViolations.updateViolationsTable();				
			}
		});
		rdbtnDirect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//TODO: add LoadingDialog from next merge

				List<Violation> violationsIndirect = new ArrayList<Violation>();
				List<Violation> violations = browseViolations.getViolationsFilteredOrNormal();
				for(Violation violation : violations) {
					if(!violation.isIndirect()) {
						violationsIndirect.add(violation);
					}
				}
				browseViolations.fillViolationsTable(violationsIndirect);		

			}
		});
		rdbtnIndirect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//TODO: add LoadingDialog from next merge
				List<Violation> violationsIndirect = new ArrayList<Violation>();
				List<Violation> violations = browseViolations.getViolationsFilteredOrNormal();
				for(Violation violation : violations) {
					if(violation.isIndirect()) {
						violationsIndirect.add(violation);
					}
				}
				browseViolations.fillViolationsTable(violationsIndirect);

			}
		});
		
		
		
		GroupLayout gl_filterPane = new GroupLayout(this);
		gl_filterPane.setHorizontalGroup(
				gl_filterPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_filterPane.createSequentialGroup()
						.addGroup(gl_filterPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_filterPane.createSequentialGroup()
										.addGap(26)
										.addGroup(gl_filterPane.createParallelGroup(Alignment.LEADING)
												.addComponent(buttonEditFilter)
												.addComponent(applyFilter)))
												.addGroup(gl_filterPane.createSequentialGroup()
														.addContainerGap()
														.addComponent(rdbtnAll)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(rdbtnDirect)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(rdbtnIndirect)))
														.addContainerGap(15, Short.MAX_VALUE))
				);
		gl_filterPane.setVerticalGroup(
				gl_filterPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_filterPane.createSequentialGroup()
						.addGap(37)
						.addComponent(applyFilter)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(buttonEditFilter)
						.addPreferredGap(ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
						.addGroup(gl_filterPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(rdbtnAll)
								.addComponent(rdbtnDirect)
								.addComponent(rdbtnIndirect))
								.addContainerGap())
				);
		setLayout(gl_filterPane);

	}
	
	public void loadGuiText() {
		setBorder(new TitledBorder(ServiceProvider.getInstance().getControlService().getTranslatedString("Filter")));
		applyFilter.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("ApplyFilter"));
		buttonEditFilter.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("EditFilter"));
	}
	
	public void setApplyFilter(JCheckBox applyFilter) {
		this.applyFilter = applyFilter;
	}

	public JCheckBox getApplyFilter() {
		return applyFilter;
	}

}
