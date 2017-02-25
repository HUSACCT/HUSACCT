package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.control.task.MainController;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

import javax.swing.JDialog;
import javax.swing.JLabel;

public class DocumentationDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private JLabel hyperLinkLabel;
	
	public DocumentationDialog(MainController mainController) {
		super(mainController.getMainGui(), true);
		setTitle(localeService.getTranslatedString("Documentation"));
		setup();
		addComponents();
		this.setVisible(true);
	}

	private void setup(){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(new Dimension(420, 150));
		this.setLayout(new FlowLayout(0,0,0));
		this.setResizable(false);
		DialogUtils.alignCenter(this);
		
	}
	
	private void openBrowser(String link) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
            	URI uri = new URI(link);
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
	
	private void addComponents() {
		
		JLabel textLabel = new JLabel(localeService.getTranslatedString("clickLinkForDocumentation"));
		hyperLinkLabel = new JLabel("http://husacct.github.io/HUSACCT/#user_documentation");
		hyperLinkLabel.setForeground(Color.BLUE);
		
		hyperLinkLabel.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseReleased(MouseEvent e)  
		    {  
		    	openBrowser(hyperLinkLabel.getText());
		    	
		    }  
		}); 
		
		add(textLabel);
		add(hyperLinkLabel);
	}

	
}