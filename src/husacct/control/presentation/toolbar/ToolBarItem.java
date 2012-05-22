package husacct.control.presentation.toolbar;

import husacct.ServiceProvider;
import husacct.control.IControlService;
import husacct.control.ILocaleChangeListener;

import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

class ToolBarItem extends JButton {
	private static final long serialVersionUID = 1L;
	
	protected Border raisedBorder;
	protected Border loweredBorder;
	protected Border inactiveBorder;
	IControlService controlService;
	String toolTipIdentifier;
	ImageIcon icon;
	
	public ToolBarItem(String toolTipIdentifier, ImageIcon icon) {
		super();
		controlService = ServiceProvider.getInstance().getControlService();
		raisedBorder = new BevelBorder(BevelBorder.RAISED);
		loweredBorder = new BevelBorder(BevelBorder.LOWERED);
		inactiveBorder = new EmptyBorder(2, 2, 2, 2);
		this.toolTipIdentifier = toolTipIdentifier;
		this.icon = icon;
		
		setup();
		addComponents();
		addListeners();
		setRequestFocusEnabled(false);
	}
	
	private void setup(){
		setBorder(inactiveBorder);
		setMargin(new Insets(1, 1, 1, 1));
		setToolTipText(controlService.getTranslatedString(toolTipIdentifier));
	}
	
	private void addComponents(){
		setIcon(icon);
	}

	private void addListeners(){
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				setBorder(inactiveBorder);
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				setBorder(loweredBorder);
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				setBorder(inactiveBorder);
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setBorder(raisedBorder);
				
			}
		});
		
		controlService.addLocaleChangeListener(new ILocaleChangeListener() {
			@Override
			public void update(Locale newLocale) {
				setToolTipText(controlService.getTranslatedString(toolTipIdentifier));
			}
		});
	}
}