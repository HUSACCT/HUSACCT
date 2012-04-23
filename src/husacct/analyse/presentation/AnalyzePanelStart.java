package husacct.analyse.presentation;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class AnalyzePanelStart extends JPanel {  
	private static final long serialVersionUID = 1L;
	public JTextField urlInputField;
	private JComboBox languageCombo;
	private JButton urlBrowseButton;
	public  JButton analyseButton;
	
	private JScrollPane jScrollPaneStart = new JScrollPane();
	private JLabel filterHead = new JLabel("Set up the filters");
	public JCheckBox invocationConstuctor;
	public JCheckBox accessOfProperty;
	public JCheckBox extendingClass;
	public JCheckBox implementingInterface;
	public JCheckBox declaration;
	public JCheckBox annotationOfAttribut;
	public JCheckBox importFilter;
	public JCheckBox throwException; 
	
	private JPanel containerFilters;
	private JPanel urlLanguagContainer;
	private JPanel analyseContainer;
	private JPanel parentContainer;
	
	public AnalyzePanelStart(){
		setContainerPanelSettings();
		addStartComponents();
	} 
	
	public void setContainerPanelSettings(){
		Dimension containerPanelSize = new Dimension(900,700); 
		this.setPreferredSize(containerPanelSize);
		this.setVisible(true); 
	}
	
	private void addStartComponents() {  
		
		analyseButton = new JButton("Analyze");
		urlLanguagContainer= new JPanel();
	
		urlInputField = new JTextField(30);
		urlInputField.setText("");
		this.add(urlInputField); 
		 
		urlBrowseButton = new JButton("Browse");
		urlBrowseButton.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent arg0) { 
				JFileChooser chooser = new JFileChooser();
        	    chooser.setCurrentDirectory(new File("."));
        	    chooser.setDialogTitle("choosertitle");
        	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        	    chooser.setAcceptAllFileFilterUsed(false);

        	    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        	    	urlInputField.setText(chooser.getSelectedFile().getAbsolutePath()); 
        	      //jTextField2.setText(chooser.getSelectedFile().getAbsolutePath()); 
        	    } else {
        	    	urlInputField.setText("No Selection"); 
        	    }
			} 
		});
		languageCombo = new JComboBox();
		languageCombo.setModel(new  DefaultComboBoxModel(new String[] { "Java", "C#" }));
		languageCombo.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent evt) {
	        	// TODO Knop Language Wissel van mapper !!!
	         }
	     });
		  
		 javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(urlLanguagContainer);
		 urlLanguagContainer.setLayout(jPanel4Layout);
	        jPanel4Layout.setHorizontalGroup(
	            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(jPanel4Layout.createSequentialGroup()
	                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                    .addGroup(jPanel4Layout.createSequentialGroup()
	                        .addContainerGap()
	                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	                            .addGroup(jPanel4Layout.createSequentialGroup()
	                                .addComponent(urlInputField) 
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                                .addComponent(urlBrowseButton)
	                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                                .addComponent(languageCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
	                            .addComponent(jScrollPaneStart, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)))
	                    .addGroup(jPanel4Layout.createSequentialGroup()
	                        .addGap(20, 20, 20)
	                        .addComponent(filterHead)
	                        .addGap(0, 0, Short.MAX_VALUE)))
	                .addContainerGap())
	        );
	        jPanel4Layout.setVerticalGroup(
	            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(jPanel4Layout.createSequentialGroup()
	                .addContainerGap()
	                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
	                    .addComponent(urlInputField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
	                    .addComponent(urlBrowseButton)
	                    .addComponent(languageCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
	                .addGap(19, 19, 19)
	                .addComponent(filterHead)
	                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
	                .addComponent(jScrollPaneStart))
	        ); 
	        
		invocationConstuctor = new JCheckBox();
		accessOfProperty = new JCheckBox();
		extendingClass = new JCheckBox();
		implementingInterface = new JCheckBox();
		declaration = new JCheckBox();
		annotationOfAttribut = new JCheckBox();
		importFilter = new JCheckBox();
		throwException = new JCheckBox();
 
		invocationConstuctor.setSelected(true);
		invocationConstuctor.setText("Invocation of a method or Constructor");
		accessOfProperty.setSelected(true);
		accessOfProperty.setText("Access of a property or field");
		
		extendingClass.setSelected(true);
		extendingClass.setText("Extending a class or Struct");
		
		implementingInterface.setSelected(true);
		implementingInterface.setText("Implementing an interface");
		
		declaration.setSelected(true);
		declaration.setText("Declaration");
		
		annotationOfAttribut.setSelected(true);
		annotationOfAttribut.setText("Annotation of an attribute");
		
		importFilter.setSelected(true);
		importFilter.setText("Import");
		
		throwException.setSelected(true);
		throwException.setText("Throw an exception of a class");

			containerFilters = new JPanel();
			javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(containerFilters);
			containerFilters.setLayout(jPanel6Layout);
			jPanel6Layout.setHorizontalGroup(
			jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			 .addGroup(jPanel6Layout.createSequentialGroup()
			  .addContainerGap()
			   .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			    .addComponent(invocationConstuctor)
			    .addComponent(accessOfProperty)
			    .addComponent(extendingClass)
			    .addComponent(implementingInterface)
			    .addComponent(declaration)
			    .addComponent(annotationOfAttribut)
			    .addComponent(importFilter)
			    .addComponent(throwException))
			    .addContainerGap(223, Short.MAX_VALUE))
			);
			jPanel6Layout.setVerticalGroup(
			jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			 .addGroup(jPanel6Layout.createSequentialGroup()
			  .addContainerGap()
			    .addComponent(invocationConstuctor)
			    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
			    .addComponent(accessOfProperty)
			    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
			    .addComponent(extendingClass)
			    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
			    .addComponent(implementingInterface)
			    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
			    .addComponent(declaration)
			    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
			    .addComponent(annotationOfAttribut)
			    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
			    .addComponent(importFilter)
			    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
			    .addComponent(throwException)
			    .addContainerGap(123, Short.MAX_VALUE))
			); 

			analyseContainer= new JPanel(); 
			javax.swing.GroupLayout analyseContainerLaout = new javax.swing.GroupLayout(analyseContainer);
			analyseContainer.setLayout(analyseContainerLaout);
			analyseContainerLaout.setHorizontalGroup(
			analyseContainerLaout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			 .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, analyseContainerLaout.createSequentialGroup()
			  .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			   .addComponent(analyseButton)
			    .addContainerGap())
			);
			analyseContainerLaout.setVerticalGroup(
			analyseContainerLaout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			 .addGroup(analyseContainerLaout.createSequentialGroup()
			  .addContainerGap()
			   .addComponent(analyseButton)
			    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
			);
    	  
			parentContainer= new JPanel();
			javax.swing.GroupLayout parentContainerLayout = new javax.swing.GroupLayout(parentContainer);
			parentContainer.setLayout(parentContainerLayout);
			parentContainerLayout.setHorizontalGroup(
			parentContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			 .addComponent(urlLanguagContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			  .addComponent(analyseContainer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			);
			parentContainerLayout.setVerticalGroup(
			parentContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			 .addGroup(parentContainerLayout.createSequentialGroup()
			  .addComponent(urlLanguagContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
			   .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			    .addComponent(analyseContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
			  );

        jScrollPaneStart.setViewportView(containerFilters); 
		this.add(parentContainer); 
	} 
}