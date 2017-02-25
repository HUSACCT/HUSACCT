package husacct.control.presentation.toolbar;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IServiceListener;
import husacct.control.task.IStateChangeListener;
import husacct.control.task.StateController;
import husacct.control.task.States;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

class ToolBarItem extends JButton {
	private static final long serialVersionUID = 1L;
	
	protected Border raisedBorder;
	protected Border loweredBorder;
	protected Border inactiveBorder;
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
	private StateController stateController;
	private String toolTipIdentifier;
	private ImageIcon icon;
	
	private JMenuItem menuItem;
	
	public ToolBarItem(String toolTipIdentifier, ImageIcon icon, JMenuItem menuItem, StateController stateController) {
		super();
		this.localeService = ServiceProvider.getInstance().getLocaleService();
		this.stateController = stateController;
		this.raisedBorder = new BevelBorder(BevelBorder.RAISED);
		this.loweredBorder = new BevelBorder(BevelBorder.LOWERED);
		this.inactiveBorder = new EmptyBorder(2, 2, 2, 2);
		this.toolTipIdentifier = toolTipIdentifier;
		this.icon = icon;
		this.menuItem = menuItem;
		
		setup();
		addComponents();
		addListeners();
		setRequestFocusEnabled(false);
	}
	
	private void setup(){
		setBorder(inactiveBorder);
		setMargin(new Insets(1, 1, 1, 1));
		setToolTipText(localeService.getTranslatedString(toolTipIdentifier));
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
		
		addActionListener(arg0 -> menuItem.doClick());
		
		localeService.addServiceListener(() -> setToolTipText(localeService.getTranslatedString(toolTipIdentifier)));
		
		stateController.addStateChangeListener(states -> setEnabled(menuItem.isEnabled()));
	}
}