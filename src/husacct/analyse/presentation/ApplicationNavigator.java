 package husacct.analyse.presentation;

import husacct.analyse.AnalyseServiceImpl;
import husacct.analyse.IAnalyseService;
import husacct.analyse.domain.famix.FamixModel;
import husacct.analyse.domain.famix.FamixObject;
import husacct.analyse.domain.famix.FamixPackage;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
 

/**
 *
 * @author tim
 */
public class ApplicationNavigator extends JFrame implements TreeSelectionListener {

	  public JButton showFamix;
	 private class BookInfo {
	        public String bookName;
	        public URL bookURL;

	        public BookInfo(String book, String filename) {
	            bookName = book;
	            bookURL = getClass().getResource(filename);
	            if (bookURL == null) {
	                System.err.println("Couldn't find file: "
	                                   + filename);
	            }
	        }

	        public String toString() {
	            return bookName;
	        }
	    }
 
    /**
     * Creates new form ApplicationNavigator
     */
    public ApplicationNavigator() {
        initComponents();
    }
    HashMap<String, ArrayList<Object>> analysed;
    @Override
	public void valueChanged(TreeSelectionEvent e) {
    	 
    	DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
    	
    	  
				if (node == null) return;
				 
				if (node.isLeaf()) {
 
					System.out.println(node.toString());
					
					DefaultTableModel model = new DefaultTableModel();
					JTable table1 = new JTable(model);

					// Create a couple of columns
					model.addColumn("Property");
					model.addColumn("Value");

				 
					String nodeData = node.toString();
					String[] splitObjects = node.toString().split("\\.");
					 
					String tempName = "";
					String tempUniqueName = "";
					String tempBelongsToPackage = "";
					
					int aantalObjecten = splitObjects.length;
					
					if(splitObjects.length == 0){
						
						tempName = nodeData; 
						tempUniqueName = nodeData;
						tempBelongsToPackage = "null";
					}
					else
					{
						for(int iSplitCounter = 0 ; iSplitCounter < aantalObjecten; iSplitCounter++ ){
							
							tempUniqueName = nodeData;
							
							if (iSplitCounter == aantalObjecten-1 ){
								
								tempBelongsToPackage += splitObjects[iSplitCounter].toString() + "" ;
								tempName = splitObjects[iSplitCounter].toString();
							}
							else
							{
								tempBelongsToPackage += splitObjects[iSplitCounter].toString() + "." ;
							} 
						} 	
						
					}
					model.addRow(new Object[]{"ClassUniqueName", tempUniqueName}); 
					model.addRow(new Object[]{"ClassName", tempName}); 
					
					
					
					jScrollPane4.setViewportView(table1);
				}
				else
				{
					 
					DefaultTableModel model = new DefaultTableModel();
					JTable table2 = new JTable(model);

					// Create a couple of columns
					model.addColumn("Property");
					model.addColumn("Value");
 
					String nodeData = node.toString();
					String[] splitObjects = node.toString().split("\\.");
					 
					String tempName = "";
					String tempUniqueName = "";
					String tempBelongsToPackage = "";
					
					int aantalObjecten = splitObjects.length;
					
					if(splitObjects.length == 0){
						
						tempName = nodeData; 
						tempUniqueName = nodeData;
						tempBelongsToPackage = "null";
					}
					else
					{
						for(int iSplitCounter = 0 ; iSplitCounter < aantalObjecten; iSplitCounter++ ){
							
							tempUniqueName = nodeData;
							
							if (iSplitCounter == aantalObjecten-1 ){
 
								tempName = splitObjects[iSplitCounter].toString();
							}
							else
							{
					//			tempBelongsToPackage += splitObjects[iSplitCounter].toString() + "." ;
								
							} 
						} 	
						
					}
					 
					model.addRow(new Object[]{"PackageUniqueName", tempUniqueName}); 
					model.addRow(new Object[]{"PackageName", tempName}); 
					//model.addRow(new Object[]{"BelongsToPackageName", tempBelongsToPackage}); 
					
					jScrollPane4.setViewportView(table2);
				}
				 
	}
    
	 
  
	public DependencyDTO[] getDependency(String from) {
		
		ArrayList<DependencyDTO> allDependencies = new ArrayList<DependencyDTO>();
		
		for(String s : analysed.keySet()){
			if(s.indexOf(from) == -1){
				continue;
			}
			
			ArrayList<Object> currentElement = analysed.get(s);
			for(DependencyDTO dependency: (ArrayList<DependencyDTO>) currentElement.get(1)){
				allDependencies.add(dependency);
			}
		}
		
		
		DependencyDTO[] matchDependency = new DependencyDTO[allDependencies.size()];
		int iterator = 0;
		for(DependencyDTO d : matchDependency){
			matchDependency[iterator] = d;
		}
		
		return matchDependency;
	}
	 
	public DependencyDTO[] getDependency(String from, String to) {
		
		ArrayList<DependencyDTO> allDependencies = new ArrayList<DependencyDTO>();
			
		for(String s : analysed.keySet()){
			if(s.indexOf(from) == -1){
				continue;
			}
			
			ArrayList<Object> currentElement = analysed.get(s);
			for(DependencyDTO dependency: (ArrayList<DependencyDTO>) currentElement.get(1)){
				if(dependency.to.indexOf(to) != -1){
					allDependencies.add(dependency);
				}
			}
		}
		
		
		if(allDependencies.size() != 0){
			DependencyDTO[] dependencyDTO = new DependencyDTO[allDependencies.size()];
			
			int iterator = 0;
			for(DependencyDTO d : allDependencies){
				dependencyDTO[iterator] = d;
				iterator++;
			}
			
			return dependencyDTO;
			
			
		}
		
		return new DependencyDTO[0];
	}

	public JTree tree;
	
	/**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    
    	
    	
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jCheckBox7 = new javax.swing.JCheckBox();
        jCheckBox8 = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel11 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        showFamix = new JButton("show Famix");
        showFamix.setEnabled(false);
        
        tfSelectedClass = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Analyse");

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jTextField1.setText("");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        
        jTextField2.setText("");
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        
   
        tfSelectedClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
             
                tfSelectedClass.setText("actionPerformed");
                
                
            
            }
        });

        jButton1.setText("Browse");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	JFileChooser chooser = new JFileChooser();
        	    chooser.setCurrentDirectory(new java.io.File("."));
        	    chooser.setDialogTitle("choosertitle");
        	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        	    chooser.setAcceptAllFileFilterUsed(false);

        	    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        	       jTextField1.setText(chooser.getSelectedFile().getAbsolutePath()); 
        	      //jTextField2.setText(chooser.getSelectedFile().getAbsolutePath()); 
        	    } else {
        	    	jTextField1.setText("No Selection"); 
        	    }
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Java", "C#" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Invocation of a method or Constructor");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setSelected(true);
        jCheckBox2.setText("Access of a property or field");

        jCheckBox3.setSelected(true);
        jCheckBox3.setText("Extending a class or Struct");

        jCheckBox4.setSelected(true);
        jCheckBox4.setText("Implementing an interface");

        jCheckBox5.setSelected(true);
        jCheckBox5.setText("Declaration");

        jCheckBox6.setSelected(true);
        jCheckBox6.setText("Annotation of an attribute");

        jCheckBox7.setSelected(true);
        jCheckBox7.setText("Import");

        jCheckBox8.setSelected(true);
        jCheckBox8.setText("Throw an exception of a class");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox3)
                    .addComponent(jCheckBox4)
                    .addComponent(jCheckBox5)
                    .addComponent(jCheckBox6)
                    .addComponent(jCheckBox7)
                    .addComponent(jCheckBox8))
                .addContainerGap(223, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox8)
                .addContainerGap(123, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel6);

        jLabel1.setText("Set up the filters");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jTextField1)
                                //.addComponent(jTextField2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    // .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1))
        );

        jButton2.setText("Analyse");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
 
            	
          
            	ArrayList<AnalysedModuleDTO> foursquareSub = new ArrayList<AnalysedModuleDTO>();
        		foursquareSub.add(new AnalysedModuleDTO("domain.locationbased.foursquare.Account", "Account", "class"));
        		foursquareSub.add(new AnalysedModuleDTO("domain.locationbased.foursquare.Friends", "Friends", "class"));
        		foursquareSub.add(new AnalysedModuleDTO("domain.locationbased.foursquare.Map", "Map", "class"));
        		foursquareSub.add(new AnalysedModuleDTO("domain.locationbased.foursquare.History", "History", "class"));
        		
        		ArrayList<AnalysedModuleDTO> latitudeSub = new ArrayList<AnalysedModuleDTO>();
        		latitudeSub.add(new AnalysedModuleDTO("domain.locationbased.latitude.Account", "Account", "class"));
        		latitudeSub.add(new AnalysedModuleDTO("domain.locationbased.latitude.Friends", "Friends", "class"));
        		latitudeSub.add(new AnalysedModuleDTO("domain.locationbased.latitude.Map", "Map", "class"));		
        		
        		ArrayList<AnalysedModuleDTO> locationbasedSub = new ArrayList<AnalysedModuleDTO>();
        		locationbasedSub.add(new AnalysedModuleDTO("domain.locationbased.foursquare", "foursquare", "package", foursquareSub));
        		locationbasedSub.add(new AnalysedModuleDTO("domain.locationbased.latitude", "latitude", "package", latitudeSub));
        		
        		
        		ArrayList<AnalysedModuleDTO> domainSub = new ArrayList<AnalysedModuleDTO>();
        		domainSub.add(new AnalysedModuleDTO("domain.locationbased", "locationbased", "package", locationbasedSub));
        		
        		
        		ArrayList<AnalysedModuleDTO> foursquare1Sub = new ArrayList<AnalysedModuleDTO>();
        		foursquare1Sub.add(new AnalysedModuleDTO("infrastructure.socialmedia.locationbased.foursquare.AccountDAO", "AccountDAO", "class"));
        		foursquare1Sub.add(new AnalysedModuleDTO("infrastructure.socialmedia.locationbased.foursquare.FriendsDAO", "FriendsDAO", "class"));
        		foursquare1Sub.add(new AnalysedModuleDTO("infrastructure.socialmedia.locationbased.foursquare.IMap", "IMap", "class"));
        		foursquare1Sub.add(new AnalysedModuleDTO("infrastructure.socialmedia.locationbased.foursquare.HistoryDAO", "HistoryDAO", "class"));
        		
        		ArrayList<AnalysedModuleDTO> locationbased1Sub = new ArrayList<AnalysedModuleDTO>();
        		locationbased1Sub.add(new AnalysedModuleDTO("infrastructure.socialmedia.locationbased.foursquare", "foursquare", "package", foursquare1Sub));
        		
        		ArrayList<AnalysedModuleDTO> socialmediaSub = new ArrayList<AnalysedModuleDTO>();
        		socialmediaSub.add(new AnalysedModuleDTO("infrastructure.socialmedia.locationbased", "locationbased", "package", locationbased1Sub));
        		
        		ArrayList<AnalysedModuleDTO> infrastructureSub = new ArrayList<AnalysedModuleDTO>();
        		infrastructureSub.add(new AnalysedModuleDTO("infrastructure.socialmedia", "socialmedia", "package", socialmediaSub));
        		
        		ArrayList<AnalysedModuleDTO> analysedSub = new ArrayList<AnalysedModuleDTO>();
        		analysedSub.add(new AnalysedModuleDTO("domain", "domain", "package", domainSub));
        		analysedSub.add(new AnalysedModuleDTO("infrastructure", "infrastructure", "package", infrastructureSub));
        		
        	       DefaultMutableTreeNode rootNode =  new DefaultMutableTreeNode("root");
       	        
        	       
       
       	      
   
        		
        		for(AnalysedModuleDTO rootPackages: analysedSub){
        			
//        		 System.out.println("rootPackage =" + rootPackages.uniqueName);
        		 DefaultMutableTreeNode rootpackageNode = new DefaultMutableTreeNode(rootPackages.uniqueName);
         	        rootNode.add(rootpackageNode);
                 
         	       
        			for(AnalysedModuleDTO packageL2: rootPackages.subModules){
//        				System.out.println("packageL2 =" + packageL2.uniqueName);
        				DefaultMutableTreeNode rootpackageNodeL2 = new DefaultMutableTreeNode(packageL2.uniqueName);
        				rootpackageNode.add(rootpackageNodeL2);
        				for(AnalysedModuleDTO packageL3: packageL2.subModules){
//            				System.out.println("packageL3 =" + packageL3.uniqueName);
            				DefaultMutableTreeNode rootpackageNodeL3 = new DefaultMutableTreeNode(packageL3.uniqueName);
            				rootpackageNodeL2.add(rootpackageNodeL3);
            				for(AnalysedModuleDTO classInPackage: packageL3.subModules){
//                				System.out.println("classInPackage =" + classInPackage.uniqueName);
                				 DefaultMutableTreeNode rootclassNameNode = new DefaultMutableTreeNode(classInPackage.uniqueName );
                				 rootpackageNodeL3.add(rootclassNameNode); 
                			} 
            			} 
        			} 	
        		}
        		
     
       		 
            
            	
	        tree = new JTree(rootNode);
	        tree.getSelectionModel().setSelectionMode
	        (TreeSelectionModel.SINGLE_TREE_SELECTION);

            ImageIcon tutorialIcon = createImageIcon("class-icon.jpg");
           if (tutorialIcon != null) {
               tree.setCellRenderer(new MyRenderer(tutorialIcon));
           } else {
               System.err.println("Tutorial icon missing; using default.");
           }

//        
           
           jScrollPane3.setViewportView(tree);
           setListener();
           jTabbedPane1.setSelectedIndex(1);
           showFamix.setEnabled(true);
            }

			
            
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Start", jPanel1);

        
//        int aantalPackages = 3;
//        int aantalClasses= 5;
//        
//        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("application");
//        
//        for (int iPackages = 0; iPackages < aantalPackages; iPackages++){
//        	
//        	 javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("packages");
//        	 
//        	
//        	 
//        	 for(int iClasses = 0; iClasses < aantalClasses; iClasses++){
//        		 
//        		 javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("AnalyseFacade");
//        	     treeNode2.add(treeNode3);
//        	     treeNode1.add(treeNode2);
//        	 }
//        	 
//        }
         
        int aantalPackages = 3;
        int aantalClasses= 5;
        
        DefaultMutableTreeNode top =  new DefaultMutableTreeNode("root");
        
   
        DefaultMutableTreeNode packageNode = null;
        DefaultMutableTreeNode classNode = null;
 
        IAnalyseService analyseService = new AnalyseServiceImpl(); 
        analyseService.analyseApplication();
    	
    	AnalysedModuleDTO[] getRootModules = analyseService.getRootModules();
  
    	
    	ArrayList<AnalysedModuleDTO> foursquareSub = new ArrayList<AnalysedModuleDTO>();
		foursquareSub.add(new AnalysedModuleDTO("domain.locationbased.foursquare.Account", "Account", "class"));
		foursquareSub.add(new AnalysedModuleDTO("domain.locationbased.foursquare.Friends", "Friends", "class"));
		foursquareSub.add(new AnalysedModuleDTO("domain.locationbased.foursquare.Map", "Map", "class"));
		foursquareSub.add(new AnalysedModuleDTO("domain.locationbased.foursquare.History", "History", "class"));
		
		ArrayList<AnalysedModuleDTO> latitudeSub = new ArrayList<AnalysedModuleDTO>();
		latitudeSub.add(new AnalysedModuleDTO("domain.locationbased.latitude.Account", "Account", "class"));
		latitudeSub.add(new AnalysedModuleDTO("domain.locationbased.latitude.Friends", "Friends", "class"));
		latitudeSub.add(new AnalysedModuleDTO("domain.locationbased.latitude.Map", "Map", "class"));		
		
		ArrayList<AnalysedModuleDTO> locationbasedSub = new ArrayList<AnalysedModuleDTO>();
		locationbasedSub.add(new AnalysedModuleDTO("domain.locationbased.foursquare", "foursquare", "package", foursquareSub));
		locationbasedSub.add(new AnalysedModuleDTO("domain.locationbased.latitude", "latitude",  "package", latitudeSub));
		
		
		ArrayList<AnalysedModuleDTO> domainSub = new ArrayList<AnalysedModuleDTO>();
		domainSub.add(new AnalysedModuleDTO("domain.locationbased", "locationbased", "package", locationbasedSub));
		 
    	for(AnalysedModuleDTO curDTO: locationbasedSub){
//    		System.out.println(curDTO.name);
//    		System.out.println(curDTO.uniqueName);
//    		System.out.println(curDTO.type); 
    		
    		packageNode = new DefaultMutableTreeNode(curDTO.uniqueName);
             top.add(packageNode);
 
         	if(curDTO.subModules != null){
    			
    			for(AnalysedModuleDTO curDTOClass: curDTO.subModules){
    				  
//    				 (name "DocentAbstract")
//    				 (uniqueName "domain::DocentAbstract")
//    				 (isAbstract "true")
//    				 (belongsToPackage "domain") 
    					
    				
//    				(Invocation
//    						 (invokedBy "domain::Student.Student()")
//    						 (invokes "domain::Persoon.Persoon()")
//    						 (base "null")
//    						 (lineNumber "6")
//    						 (sourceFilePath "C:\Users\Asim\workspaceMaster\AnalyseBenchmarkApplication\src\domain\Student.java")
//    						)
    				
    				
//    				Unique Name: (Invocation
//    						 (invokedBy "domain::Student.Student()")
//    						 (invokes "domain::Persoon.Persoon()")
//    						 (base "null")
//    						 (lineNumber "6")
//    						 (sourceFilePath "C:\Users\Asim\workspaceMaster\AnalyseBenchmarkApplication\src\domain\Student.java")
//    						)

    				
    				DefaultMutableTreeNode classNameNode = new DefaultMutableTreeNode(curDTOClass.uniqueName );
    				
    			
//    			 	DefaultMutableTreeNode classUniqueNameNode = new DefaultMutableTreeNode(curDTOClass.uniqueName );
//    				DefaultMutableTreeNode classIsAbstractNode = new DefaultMutableTreeNode("isAbstract : true/false" );
//    				DefaultMutableTreeNode classbelongsToNode = new DefaultMutableTreeNode(curDTOClass.uniqueName.substring(0,10) );
//    				
//    				classNameNode.add(classUniqueNameNode);
//    				classNameNode.add(classIsAbstractNode);
//    				classNameNode.add(classbelongsToNode);
    				packageNode.add(classNameNode);
 
    			} 
 
    			
    		}
    	}
    	
        
//        for (int iPackages = 0; iPackages < aantalPackages; iPackages++){
//        	
//        	  
//        	 for(int iClasses = 0; iClasses < aantalClasses; iClasses++){
//        		 
//        	     //original Tutorial
//        	        book = new DefaultMutableTreeNode(new BookInfo ("Class"+iClasses+".java","tutorial.html"));
//        	        category.add(book);
//        	 }
//        	 
//        } 
//        
//        tree = new JTree(top);
//        tree.getSelectionModel().setSelectionMode
//                (TreeSelectionModel.SINGLE_TREE_SELECTION);
//
//        
//        ImageIcon packageIcon = createImageIcon("package-icon.jpg");
//        if (packageIcon != null) {
//            tree.setCellRenderer(new MyRendererV2(packageIcon));
//        } else {
//            System.err.println("packageIcon icon missing; using default.");
//        }
//        
//        ImageIcon tutorialIcon = createImageIcon("class-icon.jpg");
//        if (tutorialIcon != null) {
//            tree.setCellRenderer(new MyRenderer(tutorialIcon));
//        } else {
//            System.err.println("Tutorial icon missing; using default.");
//        }
//
//     
//        
//        //Listen for when the selection changes.
//        tree.addTreeSelectionListener(this);
     

  
        
        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 906, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(jPanel7);


        jScrollPane4.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(307, 307, 307))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Navigator", jPanel2);

        jPanel3.setPreferredSize(new java.awt.Dimension(500, 509));

        jLabel4.setText("Location");

//        jTextField2.setText("analyse.domain.Famix");
//        jTextField2.addActionListener(new java.awt.event.ActionListener() {
//            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                jTextField2ActionPerformed(evt);
//            }
//        });

        jButton3.setText("Execute");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               
            	
            	
            	
            	
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tfSelectedClass, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tfSelectedClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

//        jTable2.setModel(new javax.swing.table.DefaultTableModel(
//            new Object [][] {
//                {"Package", "analyse.domain.FAMIX.FObject"},
//                {"Class", "Usages"},
//                {"Type", "Constructor parameters"},
//                {"Line", "8"},
//                {"Code", "Usages usages = new Usages();"}
//            },
//            new String [] {
//                "Property", "Value"
//            }
//        ) {
//            Class[] types = new Class [] {
//                java.lang.String.class, java.lang.String.class
//            };
//
//            public Class getColumnClass(int columnIndex) {
//                return types [columnIndex];
//            }
//        });
//        jScrollPane7.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(307, 307, 307))
        );

        jLabel2.setText("Dependency from");

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "analyse.domain.FAMIX.FamixFacade", "analyse.domain.FAMIX.FObject", "analyse.domain.FAMIX.Entity" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane5.setViewportView(jList1);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 253, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jLabel3.setText("Dependency to");

        jList2.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "analyse.domain.FAMIX.usages", "analyse.domain.FAMIX.FObject", "analyse.domain.FAMIX.Model", "analyse.Persistence.XML", "analyse.Mapper.JavaMapperfacade" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane6.setViewportView(jList2);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
//        jPanel3.setLayout(jPanel3Layout);
//        jPanel3Layout.setHorizontalGroup(
//            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//            .addGroup(jPanel3Layout.createSequentialGroup()
//                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//        );
//        jPanel3Layout.setVerticalGroup(
//            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(jPanel3Layout.createSequentialGroup()
//                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
//        );
        
        
         
        showFamix.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				analyseController analyseController = new analyseController();
				
//				System.out.println(analyseController.analyseApplication().toString());
				FamixFrame ff = new FamixFrame();
				 
				ff.setContentArea(analyseController.analyseApplication());
				
			}
        
        });
        
        jPanel3.add(showFamix);

        jTabbedPane1.addTab("Dependency Overview", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1)
        );

       pack();
        
        
    }// </editor-fold>//GEN-END:initComponents

    
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ApplicationNavigator.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    private class MyRenderer extends DefaultTreeCellRenderer {
        Icon tutorialIcon; 

        public MyRenderer(Icon icon) {
            tutorialIcon = icon; 
        } 
        public Component getTreeCellRendererComponent(
                            JTree tree,
                            Object value,
                            boolean sel,
                            boolean expanded,
                            boolean leaf,
                            int row,
                            boolean hasFocus) {

            super.getTreeCellRendererComponent(
                            tree, value, sel,
                            expanded, leaf, row,
                            hasFocus);
            
          
            
            
            if (leaf) {
                setIcon(tutorialIcon); 
                
                
            } 
            else {
                setToolTipText(null); //no tool tip 
            }

          
            return this;
        }

     
    }
    
    private class MyRendererV2 extends DefaultTreeCellRenderer {
  
        Icon packageIcon;

        public MyRendererV2(Icon icon) {
 
            packageIcon = icon;
        } 
        

        public Component getTreeCellRendererComponent(
                            JTree tree,
                            Object value,
                            boolean sel,
                            boolean expanded,
                            boolean leaf,
                            int row,
                            boolean hasFocus) {

            super.getTreeCellRendererComponent(
                            tree, value, sel,
                            expanded, leaf, row,
                            hasFocus); 
            
            if (leaf == false) {
            	setIcon(packageIcon);
             
                
            } 
            else { 
            } 
            return this;
        }

     
    }

    
    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
    
        try {  
        	javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");      
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ApplicationNavigator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ApplicationNavigator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ApplicationNavigator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ApplicationNavigator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ApplicationNavigator().setVisible(true);
            }
        });
    }
    
    private void setListener() {
        tree.addTreeSelectionListener(this);
	}
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTableClassInfo;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField tfSelectedClass;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables
	
}
